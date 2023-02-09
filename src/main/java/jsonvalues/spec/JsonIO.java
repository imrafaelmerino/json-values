package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsParserException;
import jsonvalues.Json;

import java.io.*;
import java.nio.charset.StandardCharsets;


public final class JsonIO {
    public static final JsonIO INSTANCE = new JsonIO();

    final StringCache keyCache;
    final StringCache valuesCache;
    private final JsReader.DoublePrecision doublePrecision;
    private final int maxNumberDigits;
    private final int maxStringSize;
    final ThreadLocal<JsWriter> localWriter;
    final ThreadLocal<JsReader> localReader;
    static final JsValueWritter valueSerializer = new JsValueWritter();
    static final JsObjWriter objSerializer = new JsObjWriter(valueSerializer);
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
            protected JsReader initialValue() {
                return new JsReader(new byte[4096],
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

    JsonIO() {
        this(new Settings()
                           .doublePrecision(JsReader.DoublePrecision.HIGH));
    }

     public JsObj parseToJsObj(final byte[] bytes) {
         JsReader reader = getReader(bytes);
         try {
             reader.getNextToken();
             return JsParsers.PARSERS.objParser.value(reader);
         } catch (IOException e) {
             throw JsParserException.reasonFrom("Exception parsing an object @ position=" + reader.getPositionInStream(),
                                                e
                                               );

         } finally {
             reader.reset();
         }
     }

     public JsArray parseToJsArray(final byte[] bytes) {
         JsReader reader = getReader(bytes);
         try {
             reader.getNextToken();
             return JsParsers.PARSERS.arrayOfValueParser.value(reader);
         } catch (IOException e) {
             throw JsParserException.reasonFrom("Exception parsing an array @ position=" + reader.getPositionInStream(),
                                                e
                                               );

         } finally {
             reader.reset();
         }
     }
    JsObj parseToJsObj(final byte[] bytes,
                       final JsSpecParser parser
                      ) {
        JsReader reader = getReader(bytes);
        try {
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsObj();
        } catch (IOException e) {
            throw JsParserException.reasonFrom("Exception parsing an object @ position=" + reader.getPositionInStream(),
                                               e
                                              );

        } finally {
            reader.reset();
        }
    }

    private JsReader getReader(final byte[] bytes) {
        return localReader.get()
                          .process(bytes,
                                   bytes.length
                                  );


    }

    JsArray deserializeToJsArray(final byte[] bytes,
                                 final JsSpecParser parser
                                ) {
        JsReader reader = getReader(bytes);
        try {
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsArray();
        } catch (IOException e) {
            throw JsParserException.reasonFrom("Exception deserializing an array @ position=" + reader.getPositionInStream(),
                                               e
                                              );
        } finally {
            reader.reset();
        }
    }

    JsObj parseToJsObj(final InputStream is,
                       final JsSpecParser parser

                      ) {
        JsReader reader = null;
        try {
            reader = getReader(is);
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsObj();
        } catch (IOException e) {
            throw JsParserException.reasonFrom("Exception while parsing an object",
                                               e
                                              );
        } finally {
            if (reader != null) reader.reset();
        }
    }

    private JsReader getReader(final InputStream is) throws IOException {

        return localReader.get()
                          .process(is);

    }

     JsArray deserializeToJsArray(final InputStream is,
                                        final JsSpecParser parser
                                       ) {
        JsReader reader = null;
        try {
            reader = getReader(is);
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsArray();
        } catch (IOException e) {
            throw JsParserException.reasonFrom("Exception while deserialization an array", e);
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
        final JsWriter jw = localWriter.get();
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
    JsWriter newWriter(int size) {
        return new JsWriter(size);
    }

    /**
     * Create a reader bound to this DSL-JSON.
     * Bound reader can reuse key cache (which is used during Map deserialization)
     * This reader can be reused via process method.
     *
     * @param bytes input bytes
     * @return bound reader
     */
    JsReader newReader(byte[] bytes) {
        return new JsReader(bytes,
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
    JsReader newReader(InputStream stream, byte[] buffer) throws IOException {
        JsReader reader = newReader(buffer);
        reader.process(stream);
        return reader;
    }


}
