package jsonvalues;

import com.fasterxml.jackson.core.JsonFactory;

class JacksonFactory {

    static final JsonFactory INSTANCE = new JsonFactory();

    private JacksonFactory() {
    }
}
