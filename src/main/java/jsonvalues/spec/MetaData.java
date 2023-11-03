package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;


record MetaData(String name, String namespace,
                List<String> aliases, String doc,
                Map<String, String> fieldsDoc,
                Map<String, ORDERS> fieldsOrder,
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

        // Validate name
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null, empty, or blank.");
        }
    }

    enum ORDERS {ascending, descending, ignore}

}
