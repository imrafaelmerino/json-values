package com.dslplatform.json;

import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.Json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

public final class MyDslJson<A> extends DslJson<A> {

    public static final MyDslJson<java.lang.Object> INSTANCE = new MyDslJson<>();

    static {
        final JsValueSerializer valueSerializer = new JsValueSerializer();
        final JsonWriter.WriteObject<JsObj> objSerializer = new JsObjSerializer(valueSerializer);
        final JsonWriter.WriteObject<JsArray> arraySerializer = new JsArraySerializer(valueSerializer);
        valueSerializer.setArraySerializer(arraySerializer);
        valueSerializer.setObjectSerializer(objSerializer);
        INSTANCE.registerWriter(JsObj.class,
                                objSerializer
        );

        INSTANCE.registerWriter(JsArray.class,
                                arraySerializer
        );
    }

    private MyDslJson() {
        super((new Settings<A>().errorInfo(JsonReader.ErrorInfo.MINIMAL)));
    }

    public JsObj parseToJsObj(final byte[] bytes,
                              final JsSpecParser parser
    ) {
        JsonReader<?> reader = getReader(bytes);

        try {
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsObj();
        }

        catch (IOException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());

        } finally {
            reader.reset();
        }
    }

    public JsonReader<?> getReader(final byte[] bytes
    ) {
        return localReader.get()
                          .process(bytes,
                                   bytes.length
                          );
    }

    public JsArray deserializeToJsArray(final byte[] bytes,
                                        final JsSpecParser parser
    ) {
        JsonReader<?> reader = getReader(bytes);
        try {
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsArray();
        }

        catch (IOException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        } finally {
            reader.reset();
        }
    }

    public JsObj parseToJsObj(final InputStream is,
                              final JsSpecParser parser

    ) {
        JsonReader<?> reader = getReader(is);
        try {
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsObj();
        }

        catch (IOException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        } finally {
            reader.reset();
        }
    }

    public JsonReader<?> getReader(final InputStream is) {
        try {
            return localReader.get()
                              .process(is);
        } catch (IOException e) {
            throw new JsParserException(e);

        }
    }

    public JsArray deserializeToJsArray(final InputStream is,
                                        final JsSpecParser parser
    ) {
        JsonReader<?> reader = getReader(is);
        try {
            reader.getNextToken();
            return parser.parse(reader)
                         .toJsArray();
        }
        catch (IOException e) {
            throw new JsParserException(e.getMessage(),
                                        reader.getCurrentIndex());
        } finally {
            reader.reset();
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
            throw new SerializerException(e);
        }
    }

    public void serialize(final Json<?> json,
                          final OutputStream outputstream
    ) {
        try {
            super.serialize(json,
                            requireNonNull(outputstream)
            );
        } catch (IOException e) {
            throw new SerializerException(e);
        }

    }


    public String toPrettyString(final Json<?> json,
                                 int indentLength) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            INSTANCE.serialize(json,
                               new MyPrettifyOutputStream(baos,
                                                          MyPrettifyOutputStream.IndentType.SPACES,
                                                          indentLength)
            );
            return baos.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new SerializerException(e);
        }
    }


}
