package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 Represents an immutable json number. It's a marker interface for the types {@link JsInt}, {@link JsLong}, {@link JsDouble}, {@link JsBigInt} and {@link JsBigDec}
 */
abstract class JsNumber implements JsValue
{
    @Override
    public boolean isNumber()
    {
        return true;
    }

    static JsNumber of(JsonParser parser) throws IOException
    {
        try
        {
            return JsInt.of(parser.getIntValue());
        }

        catch (IOException ex)
        {
            try
            {
                return JsLong.of(parser.getLongValue());
            }

            catch (IOException ex1)
            {
                return JsBigInt.of(parser.getBigIntegerValue());
            }
        }

    }

}
