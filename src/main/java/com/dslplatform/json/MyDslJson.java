package com.dslplatform.json;

import com.dslplatform.json.parsers.JsParserException;
import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.serializers.JsArraySerializer;
import com.dslplatform.json.serializers.JsObjSerializer;
import com.dslplatform.json.serializers.JsValueSerializer;
import com.dslplatform.json.serializers.SerializerException;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.Json;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

public final class MyDslJson<Object> extends DslJson<Object>
{
  public static final MyDslJson<java.lang.Object> INSTANCE = new MyDslJson<>();

  private MyDslJson() { }

  static
  {
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

  public JsonReader<?> getReader(final byte[] bytes
                                 )
  {
    return localReader.get()
                      .process(bytes,
                               bytes.length
                              );
  }

  public JsonReader<?> getReader(final InputStream is) throws JsParserException
  {
    try
    {
      return localReader.get()
                        .process(is);
    }
    catch (IOException e)
    {
      throw new JsParserException(e.getMessage());

    }
  }

  public JsObj parseToJsObj(final byte[] bytes,
                            final JsSpecParser parser
                           ) throws JsParserException
  {
    JsonReader<?> reader = getReader(bytes);
    try
    {
      reader.getNextToken();
      return parser.parse(reader)
                         .toJsObj();
    }
    catch (IOException e)
    {
      throw new JsParserException(e.getMessage());

    }
    finally
    {
      reader.reset();
    }
  }

  public JsArray deserializeToJsArray(final byte[] bytes,
                                      final JsSpecParser parser
                                     ) throws JsParserException
  {
    JsonReader<?> reader = getReader(bytes);
    try
    {
      reader.getNextToken();
      return parser.parse(reader)
                         .toJsArray();
    }
    catch (IOException e)
    {
      throw new JsParserException(e.getMessage());
    }
    finally
    {
      reader.reset();
    }
  }

  public JsObj parseToJsObj(final InputStream is,
                            final JsSpecParser parser

                           ) throws JsParserException
  {
    JsonReader<?> reader = getReader(is);
    try
    {
      reader.getNextToken();
      return parser.parse(reader)
                         .toJsObj();
    }
    catch (IOException e)
    {
      throw new JsParserException(e.getMessage());
    }
    finally
    {
      reader.reset();
    }
  }

  public JsArray deserializeToJsArray(final InputStream is,
                                      final JsSpecParser parser
                                     ) throws SerializerException
  {
    JsonReader<?> reader = getReader(is);
    try
    {
      reader.getNextToken();
      return parser.parse(reader)
                         .toJsArray();
    }
    catch (IOException e)
    {
      throw new JsParserException(e.getMessage());
    }
    finally
    {
      reader.reset();
    }
  }

  public byte[] serialize(final Json<?> json) throws SerializerException
  {
    try
    {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      INSTANCE.serialize(json,
                         outputStream
                        );
      outputStream.flush();
      return outputStream.toByteArray();
    }
    catch (IOException e)
    {
      throw new SerializerException(e);
    }
  }

  public void serialize(final Json<?> json,
                        final OutputStream ouputstream
                       ) throws SerializerException
  {
    try
    {
      super.serialize(json,
                      requireNonNull(ouputstream)
                     );
    }
    catch (IOException e)
    {
      throw new SerializerException(e);
    }

  }

  public String toPrettyString(final Json<?> json)
  {

    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      INSTANCE.serialize(json,
                         new MyPrettifyOutputStream(baos)
                        );
      return baos.toString(StandardCharsets.UTF_8.name());
    }
    catch (IOException e)
    {
      throw new SerializerException(e);
    }
  }
}
