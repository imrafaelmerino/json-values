package jsonvalues;

import com.fasterxml.jackson.core.JsonFactory;

/**
 Singleton which contains the default Json factories.
 */
public final class Jsons
{
    private Jsons()
    {
    }

    static JsonFactory factory = new JsonFactory();
    /**
     Factory of immutable jsons. It's a singleton and can not be modified.
     */
    public static final ImmutableJsons immutable = new ImmutableJsons();


}
