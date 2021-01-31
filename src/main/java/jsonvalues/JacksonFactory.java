package jsonvalues;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
class JacksonFactory {

    static final JsonFactory INSTANCE = new JsonFactory();

    static final YAMLFactory YAML_FACTORY = new YAMLFactory();

    private JacksonFactory() {}
}
