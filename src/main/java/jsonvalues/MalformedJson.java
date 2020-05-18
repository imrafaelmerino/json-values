package jsonvalues;

/**
 Exception returned when a string can not be parsed into a Json or the json parsed has a different
 type than the expected.
 */
public final class MalformedJson extends RuntimeException {

    private static final long serialVersionUID = 1L;

    MalformedJson(final String message) {
        super(message);
    }

    static MalformedJson expectedArray(String str) {
        return new MalformedJson(String.format("Expected a json array [...]. Received: %s",
                                               str
                                              ));
    }

    static MalformedJson expectedObj(String str) {
        return new MalformedJson(String.format("Expected a json object {...}. Received: %s",
                                               str));
    }

}
