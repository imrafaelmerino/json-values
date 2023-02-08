package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.Json;

import java.io.*;
import java.nio.charset.StandardCharsets;


 public final class JsonIO {
    public static final JsonIO INSTANCE = new JsonIO();

    final StringCache keyCache;
    final StringCache valuesCache;
    private final JsonReader.ErrorInfo errorInfo;
    private final JsonReader.DoublePrecision doublePrecision;
    private final int maxNumberDigits;
    private final int maxStringSize;
    final ThreadLocal<JsonWriter> localWriter;
    final ThreadLocal<JsonReader> localReader;
    static final JsValueWritter valueSerializer = new JsValueWritter();
    static final JsObjWritter objSerializer = new JsObjWritter(valueSerializer);
    static final JsArrayWritter arraySerializer = new JsArrayWritter(valueSerializer);

    static {
        valueSerializer.setArraySerializer(arraySerializer);
        valueSerializer.setObjectSerializer(objSerializer);
    }


    JsonIO(Settings settings) {
        final JsonIO self = this;
        this.localWriter = ThreadLocal.withInitial(() -> newWriter(512));
        this.localReader = new ThreadLocal<>() {
            @Override
            protected JsonReader initialValue() {
                return new JsonReader(new byte[4096],
                                      4096,
                                      new char[64],
                                      self.keyCache,
                                      self.valuesCache,
                                      self.errorInfo,
                                      self.doublePrecision,
                                      self.maxNumberDigits,
                                      self.maxStringSize
                );
            }
        };
        this.keyCache = settings.keyCache;
        this.valuesCache = settings.valuesCache;
        this.errorInfo = settings.errorInfo;
        this.doublePrecision = settings.doublePrecision;
        this.maxNumberDigits = settings.maxNumberDigits;
        this.maxStringSize = settings.maxStringBuffer;
    }

    JsonIO() {
        this(new Settings().errorInfo(JsonReader.ErrorInfo.MINIMAL)
                           .doublePrecision(JsonReader.DoublePrecision.HIGH));
    }

    JsObj parseToJsObj(final byte[] bytes,
                       final JsSpecParser parser
                      ) {
        JsonReader reader = getReader(bytes);
        try {
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsObj();
        } catch (IOException e) {
            throw JsParserException.create("Exception parsing an object @ position=" + reader.getCurrentIndex(),
                                           e,
                                           true
                                          );

        } finally {
            reader.reset();
        }
    }

    private JsonReader getReader(final byte[] bytes) {
        return localReader.get()
                          .process(bytes,
                                   bytes.length
                                  );
    }

    JsArray deserializeToJsArray(final byte[] bytes,
                                 final JsSpecParser parser
                                ) {
        JsonReader reader = getReader(bytes);
        try {
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsArray();
        } catch (IOException e) {
            throw JsParserException.create("Exception deserializing an array @ position=" + reader.getCurrentIndex(),
                                           e,
                                           true
                                          );
        } finally {
            reader.reset();
        }
    }

    JsObj parseToJsObj(final InputStream is,
                       final JsSpecParser parser

                      ) {
        JsonReader reader = null;
        try {
            reader = getReader(is);
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsObj();
        } catch (IOException e) {
            throw JsParserException.create("Exception while parsing an object",
                                           e,
                                           true
                                          );
        } finally {
            if (reader != null) reader.reset();
        }
    }

    private JsonReader getReader(final InputStream is) throws IOException {

        return localReader.get()
                          .process(is);

    }

     JsArray deserializeToJsArray(final InputStream is,
                                        final JsSpecParser parser
                                       ) {
        JsonReader reader = null;
        try {
            reader = getReader(is);
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsArray();
        } catch (IOException e) {
            throw JsParserException.create("Exception while deserialization an array", e, true);
        } finally {
            if (reader != null) reader.reset();
        }
    }

    public byte[] serialize(final Json<?> json) {
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

    public void serialize(final Json<?> json,
                          final OutputStream stream
                         ) {
        final JsonWriter jw = localWriter.get();
        try {
            jw.reset(stream);
            switch (json) {
                case JsObj obj -> objSerializer.write(jw, obj);
                case JsArray arr -> arraySerializer.write(jw, arr);
            }
        } finally {
            jw.flush();
            jw.reset();
        }

    }


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
     * Create a writer bound to this DSL-JSON.
     * Ideally it should be reused.
     * Bound writer can use lookups to find custom writers.
     * This can be used to serialize unknown types such as Object.class
     *
     * @param size initial buffer size
     * @return bound writer
     */
    JsonWriter newWriter(int size) {
        return new JsonWriter(size);
    }

    /**
     * Create a reader bound to this DSL-JSON.
     * Bound reader can reuse key cache (which is used during Map deserialization)
     * This reader can be reused via process method.
     *
     * @param bytes input bytes
     * @return bound reader
     */
    JsonReader newReader(byte[] bytes) {
        return new JsonReader(bytes,
                              bytes.length,
                              new char[64],
                              keyCache,
                              valuesCache,
                              errorInfo,
                              doublePrecision,
                              maxNumberDigits,
                              maxStringSize
        );
    }


    /**
     * Create a reader bound to this DSL-JSON.
     * Bound reader can reuse key cache (which is used during Map deserialization)
     * Created reader can be reused (using process method).
     * This is convenience method for creating a new reader and binding it to stream.
     *
     * @param stream input stream
     * @param buffer temporary buffer
     * @return bound reader
     * @throws IOException unable to read from stream
     */
    JsonReader newReader(InputStream stream, byte[] buffer) throws IOException {
        JsonReader reader = newReader(buffer);
        reader.process(stream);
        return reader;
    }


}
