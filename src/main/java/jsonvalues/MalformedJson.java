package jsonvalues;

import java.text.MessageFormat;

/**
 Exception returned when a string can not be parsed into a Json or the json parsed has a different
 type than the expected.
 */
public final class MalformedJson extends Exception
{

    MalformedJson(final String message)
    {
        super(message);
    }

    private static final long serialVersionUID = 1L;


    static MalformedJson expectedArray(String str)
    {
        return new MalformedJson(String.format("Expected a json array [...]. Received: %s",
                                               str
                                              ));
    }

    static MalformedJson expectedObj(String str)
    {
        return new MalformedJson(String.format("Expected a json object {...}. Received: %s",
                                               str
                                              ));
    }

    static MalformedJson invalidToken(JsParser.Tokenizer.Token token,
                                      JsParser.Location location,
                                      String expectedTokens
                                     )
    {

        return new MalformedJson(MessageFormat.format("Invalid token={0} at {1}. Expected tokens are: {2}",
                                                      token,
                                                      location,
                                                      expectedTokens
                                                     )
        );
    }


    static MalformedJson unexpectedChar(int unexpected,
                                        JsParser.Location location
                                       )
    {


        return new MalformedJson(MessageFormat.format("Unexpected char {0} at {1}",
                                                       unexpected,
                                                       location
                                                      )
        );
    }

    static MalformedJson expectedChar(int unexpected,
                                      JsParser.Location location,
                                      char expected
                                     )
    {

        return new MalformedJson(MessageFormat.format("Unexpected char {0} at {1}, expecting ''{2}''",
                                                      String.valueOf(unexpected),
                                                      location,
                                                      expected
                                                     )
        );
    }

    static MalformedJson expectedChar(int unexpected,
                                      JsParser.Location location,
                                      char expected,
                                      char expected1
                                     )
    {

        return new MalformedJson(MessageFormat.format("Unexpected char {0} at {1}, expecting ''{2}'' or ''{3}''",
                                                      String.valueOf(unexpected),
                                                      location,
                                                      expected,
                                                      expected1
                                                     )
        );
    }

}
