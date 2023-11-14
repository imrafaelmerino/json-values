package jsonvalues.spec;

import java.util.List;

record FixedMetaData(String name, String namespace, List<String> aliases, String doc) {
    public String getFullName() {
        return namespace != null ? "%s.%s".formatted(namespace, name) : name;
    }
}
