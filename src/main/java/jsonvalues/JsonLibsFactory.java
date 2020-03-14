package jsonvalues;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.serializers.JsArraySerializer;
import com.dslplatform.json.serializers.JsObjSerializer;
import com.dslplatform.json.serializers.JsValueSerializer;
import com.fasterxml.jackson.core.JsonFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

class JsonLibsFactory
{
    static JsonFactory jackson = new JsonFactory();
    static DslJson<Object> dslJson = new DslJson<>();


  static {
    final JsValueSerializer valueSerializer = new JsValueSerializer();
    final JsonWriter.WriteObject<JsObj> objSerializer = new JsObjSerializer<>(valueSerializer);
    final JsonWriter.WriteObject<JsArray> arraySerializer = new JsArraySerializer<>(valueSerializer);
     valueSerializer.setArraySerializer(arraySerializer);
     valueSerializer.setObjectSerializer(objSerializer);
     dslJson.registerWriter(JsObj.class,
                            objSerializer
                            );

     dslJson.registerWriter(JsArray.class,
                            arraySerializer
                           );
  }

    /** Returns the string representation of this Json
     *
     * @return the string representation of this Json
     */
    static String toString(Json<?> json)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            dslJson.serialize(json,
                              baos
                             );
            return baos.toString(StandardCharsets.UTF_8.name());
        }
        catch (IOException e)
        {
            throw  InternalError.unexpectedErrorSerializingAJsonIntoString(e);
        }
    }

    /**
     * Returns a zero-argument function that when called, it serializes this Json into the given
     * output stream, no returning anything
     *
     * @param outputStream the output stream
     * @return () => Unit function that serializes this Json into the given output stream
     */
    static void serialize(Json<?> json,OutputStream outputStream) throws IOException
    {
        dslJson.serialize(json,
                          requireNonNull(outputStream)
                         );
    }

    /** Serialize this Json into an array of bytes. When possible,
     * it's more efficient to work on byte level that with strings
     *
     * @return this Json serialized into an array of bytes
     */
    static byte[] serialize(Json<?> json)
    {

        try
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            dslJson.serialize(json,
                              outputStream
                             );
            outputStream.flush();
           return outputStream.toByteArray();
        }
        catch (IOException e)
        {
            throw InternalError.unexpectedErrorSerializingAJsonIntoBytes(e);
        }
}

}
