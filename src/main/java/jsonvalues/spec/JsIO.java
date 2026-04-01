package jsonvalues.spec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import jsonvalues.Json;

/**
 * Entry point for low-level JSON parsing and serialization.
 * <p>
 * {@link #INSTANCE} is thread-safe: readers and writers are reused per thread via {@link ThreadLocal}.
 * <p>
 * This type is also used internally by higher-level APIs such as {@link JsSpec#parse(String)},
 * {@link JsObjSpecParser}, and {@link JsArraySpecParser}.
 *
 * @see DslJsReader
 * @see JsSpec#parse(String)
 */
public final class JsIO {

  /**
   * Shared singleton configured with default settings.
   */
  public static final JsIO INSTANCE = new JsIO();
  static final JsValueWritter valueSerializer = new JsValueWritter();
  static final JsObjWriter objSerializer = new JsObjWriter(valueSerializer);
  static final JsArrayWriter arraySerializer = new JsArrayWriter(valueSerializer);

  static {
    valueSerializer.setArraySerializer(arraySerializer);
    valueSerializer.setObjectSerializer(objSerializer);
  }

  final StringCache keyCache;
  final StringCache valuesCache;
  final ThreadLocal<JsWriter> localWriter;
  final ThreadLocal<DslJsReader> localReader;
  private final DslJsReader.DoublePrecision doublePrecision;
  private final int maxNumberDigits;
  private final int maxStringSize;


  JsIO(Settings settings) {
    final JsIO self = this;
    this.localWriter = ThreadLocal.withInitial(() -> newWriter(512));
    this.localReader = new ThreadLocal<>() {
      @Override
      protected DslJsReader initialValue() {
        return new DslJsReader(new byte[4096],
                               4096,
                               new char[64],
                               self.keyCache,
                               self.valuesCache,
                               self.doublePrecision,
                               self.maxNumberDigits,
                               self.maxStringSize
        );
      }
    };
    this.keyCache = settings.keyCache;
    this.valuesCache = settings.valuesCache;
    this.doublePrecision = settings.doublePrecision;
    this.maxNumberDigits = settings.maxNumberDigits;
    this.maxStringSize = settings.maxStringBuffer;
  }

  JsIO() {
    this(new Settings()
             .doublePrecision(DslJsReader.DoublePrecision.HIGH));
  }

  /**
   * Parses a JSON object from UTF-8 bytes.
   *
   * @param bytes JSON payload.
   * @return parsed object.
   * @throws NullPointerException if {@code bytes} is {@code null}.
   * @throws JsParserException if input is malformed or does not encode a JSON object.
   */
  public JsObj parseToJsObj(final byte[] bytes) {
    DslJsReader reader = createReader(bytes);
    try {
      reader.readNextToken();
      return JsReaders.READERS.objReader.value(reader);
    } finally {
      reader.reset();
    }
  }

  /**
   * Parses a JSON array from UTF-8 bytes.
   *
   * @param bytes JSON payload.
   * @return parsed array.
   * @throws NullPointerException if {@code bytes} is {@code null}.
   * @throws JsParserException if input is malformed or does not encode a JSON array.
   */
  public JsArray parseToJsArray(final byte[] bytes) {
    DslJsReader reader = createReader(bytes);
    try {
      reader.readNextToken();
      return JsReaders.READERS.arrayOfValueReader.value(reader);
    } finally {
      reader.reset();
    }
  }

  JsObj parseToJsObj(final byte[] bytes,
                     final JsParser parser
                    ) {
    DslJsReader reader = createReader(bytes);
    try {
      reader.readNextToken();
      JsValue parsed = parser.parse(reader);
      if (parsed == JsNull.NULL) {
        throw reader.newParseError(ParserErrors.EXPECTING_FOR_OBJ_START);
      }
      return parsed.toJsObj();
    } finally {
      reader.reset();
    }
  }

  /**
   * Creates or reuses the current thread reader and binds it to the provided bytes.
   *
   * @param bytes input bytes.
   * @return bound reader.
   */
  DslJsReader createReader(final byte[] bytes) {
    return localReader.get()
                      .process(Objects.requireNonNull(bytes),
                               bytes.length
                              );


  }

  /**
   * Creates or reuses the current thread reader and binds it to the provided stream.
   *
   * @param is input stream.
   * @return bound reader.
   * @throws JsParserException if stream read fails while preparing the first buffer.
   */
  DslJsReader createReader(final InputStream is) throws JsParserException {

    return localReader.get()
                      .process(Objects.requireNonNull(is));

  }

  JsArray parseToJsArray(final byte[] bytes,
                         final JsParser parser
                        ) {
    DslJsReader reader = createReader(bytes);
    try {
      reader.readNextToken();
      JsValue parsed = parser.parse(reader);
      if (parsed == JsNull.NULL) {
        throw reader.newParseError(ParserErrors.EXPECTING_FOR_ARRAY_START);
      }
      return parsed.toJsArray();
    } finally {
      reader.reset();
    }
  }

  JsObj parseToJsObj(final InputStream is,
                     final JsParser parser
                    ) {
    DslJsReader reader = null;
    try {
      reader = createReader(is);
      reader.readNextToken();
      JsValue parsed = parser.parse(reader);
      if (parsed == JsNull.NULL) {
        throw reader.newParseError(ParserErrors.EXPECTING_FOR_OBJ_START);
      }
      return parsed.toJsObj();
    } finally {
      if (reader != null) {
        reader.reset();
      }
    }
  }


  JsArray parseToJsArray(final InputStream is,
                         final JsParser parser
                        ) {
    DslJsReader reader = null;
    try {
      reader = createReader(is);
      reader.readNextToken();
      JsValue parsed = parser.parse(reader);
      if (parsed == JsNull.NULL) {
        throw reader.newParseError(ParserErrors.EXPECTING_FOR_ARRAY_START);
      }
      return parsed
          .toJsArray();
    } finally {
      if (reader != null) {
        reader.reset();
      }
    }
  }

  /**
   * Serializes JSON into UTF-8 bytes.
   *
   * @param json JSON value to serialize.
   * @return serialized bytes.
   */
  public byte[] serialize(final Json<?> json) throws JsSerializerException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    try {
      INSTANCE.serialize(json,
                         outputStream
                        );
      outputStream.flush();
      return outputStream.toByteArray();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Serializes JSON into the provided stream.
   *
   * @param json JSON value to serialize.
   * @param stream destination stream.
   */
  public void serialize(final Json<?> json,
                        final OutputStream stream
                       ) throws JsSerializerException {
    final JsWriter jw = localWriter.get();
    try {
      jw.reset(stream);
      if (Objects.requireNonNull(json) instanceof JsObj obj) {
        objSerializer.write(jw,
                            obj);
      } else if (json instanceof JsArray arr) {
        arraySerializer.write(jw,
                              arr);
      }
    } finally {
      jw.flush();
      jw.reset();
    }

  }


  /**
   * Serializes JSON into a pretty-printed string.
   *
   * @param json JSON value to serialize.
   * @param indentLength number of spaces used for indentation.
   * @return formatted JSON text.
   */
  public String toPrettyString(final Json<?> json,
                               int indentLength
                              ) {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    INSTANCE.serialize(json,
                       new MyPrettifyOutputStream(baos,
                                                  MyPrettifyOutputStream.IndentType.SPACES,
                                                  indentLength
                       )
                      );
    return baos.toString(StandardCharsets.UTF_8);
  }

  /**
   * Creates a writer. Ideally it should be reused. Bound writer can use lookups to find custom writers. This can be
   * used to serialize unknown types such as Object class
   *
   * @param size initial buffer size
   * @return bound writer
   */
  JsWriter newWriter(int size) {
    return new JsWriter(size);
  }

  /**
   * Creates a reader. Bound reader can reuse key cache (which is used during Map deserialization) This reader can be
   * reused via process method.
   *
   * @param bytes input bytes
   * @return bound reader
   */
  DslJsReader newReader(byte[] bytes) {
    return new DslJsReader(bytes,
                           bytes.length,
                           new char[64],
                           keyCache,
                           valuesCache,
                           doublePrecision,
                           maxNumberDigits,
                           maxStringSize
    );
  }


  /**
   * Creates a reader. Bound reader can reuse key cache (which is used during Map deserialization) Created reader can be
   * reused (using process method). This is convenience method for creating a new reader and binding it to stream.
   *
   * @param stream input stream
   * @param buffer temporary buffer
   * @return bound reader
   * @throws JsParserException unable to read from stream
   */
  DslJsReader newReader(InputStream stream,
                        byte[] buffer) throws JsParserException {
    DslJsReader reader = newReader(buffer);
    reader.process(stream);
    return reader;
  }


}
