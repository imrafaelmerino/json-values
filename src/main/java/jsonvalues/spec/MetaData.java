package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;


record MetaData(String name, String namespace,
                List<String> aliases, String doc,
                Map<String, String> fieldsDoc,
                Map<String, JsObjSpecBuilder.ORDERS> fieldsOrder,
                Map<String, List<String>> fieldsAliases,
                Map<String, JsValue> fieldsDefault) {

    public MetaData {
        // Make lists and maps immutable (if they're not null)
        if (aliases != null) {
            aliases = Collections.unmodifiableList(aliases);
        }
        if (fieldsDoc != null) {
            fieldsDoc = Collections.unmodifiableMap(fieldsDoc);
        }
        if (fieldsOrder != null) {
            fieldsOrder = Collections.unmodifiableMap(fieldsOrder);
        }
        if (fieldsAliases != null) {
            fieldsAliases = Collections.unmodifiableMap(fieldsAliases);
        }
        if (fieldsDefault != null) {
            fieldsDefault = Collections.unmodifiableMap(fieldsDefault);
        }
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null, empty, or blank.");
        }
    }

    /**
     * given an alias, returns the main field
     *
     * @param alias the alias
     * @return the field that has as one possible alias the given one
     */
    public String getAliasField(String alias) {
        if (fieldsAliases == null) return null;
        for (String key : fieldsAliases.keySet()) {
            if (fieldsAliases.get(key).contains(alias)) return key;
        }
        return null;
    }

    public String getFullName() {
        return namespace != null ? "%s.%s".formatted(namespace, name) : name;
    }

}
