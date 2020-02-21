package jsonvalues;

import com.dslplatform.json.MyDslJson;
import com.fasterxml.jackson.core.JsonFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

class JsonLibsFactory
{
    static JsonFactory jackson = new JsonFactory();
    static MyDslJson dslJson = new MyDslJson();

    /** Returns the string representation of this Json
     *
     * @return the string representation of this Json
     */
    static String toString(Json json)
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

}
