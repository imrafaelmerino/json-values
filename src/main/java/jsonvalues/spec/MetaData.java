package jsonvalues.spec;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jsonvalues.JsValue;


record MetaData(String name,
                String namespace,
                List<String> aliases,
                String doc,
                Map<String, String> fieldsDoc,
                Map<String, JsObjSpecBuilder.ORDERS> fieldsOrder,
                Map<String, List<String>> fieldsAliases,
                Map<String, JsValue> fieldsDefault,
                int minProperties,
                int maxProperties) {

  MetaData {
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
    if (minProperties > maxProperties) {
      throw new IllegalArgumentException("minProperties must be less than or equal to maxProperties");
    }
  }

  /**
   * given an alias, returns the main field
   *
   * @param alias the alias
   * @return the field that has as one possible alias the given one
   */
  String getAliasField(String alias) {
    if (fieldsAliases == null) {
      return null;
    }
    for (String key : fieldsAliases.keySet()) {
      if (fieldsAliases.get(key)
                       .contains(alias)) {
        return key;
      }
    }
    return null;
  }

  String getFullName() {
    return namespace != null ? "%s.%s".formatted(namespace,
                                                 name) : name;
  }

  MetaData concat(MetaData other) {
    Map<String, List<String>> newAliases = (fieldsAliases != null) ? new HashMap<>(fieldsAliases) : null;
    Map<String, String> newFieldsDoc = (fieldsDoc != null) ? new HashMap<>(fieldsDoc) : null;
    Map<String, JsObjSpecBuilder.ORDERS> newOrders = (fieldsOrder != null) ? new HashMap<>(fieldsOrder) : null;
    Map<String, JsValue> newDefaults = (fieldsDefault != null) ? new HashMap<>(fieldsDefault) : null;

    if (other.fieldsAliases != null) {
      if (newAliases == null) {
        newAliases = other.fieldsAliases;
      } else {
        newAliases.putAll(other.fieldsAliases);
      }
    }

    if (other.fieldsDoc != null) {
      if (newFieldsDoc == null) {
        newFieldsDoc = other.fieldsDoc;
      } else {
        newFieldsDoc.putAll(other.fieldsDoc);
      }
    }

    if (other.fieldsOrder != null) {
      if (newOrders == null) {
        newOrders = other.fieldsOrder;
      } else {
        newOrders.putAll(other.fieldsOrder);
      }
    }

    if (other.fieldsDefault != null) {
      if (newDefaults == null) {
        newDefaults = other.fieldsDefault;
      } else {
        newDefaults.putAll(other.fieldsDefault);
      }
    }

    return new MetaData(name,
                        namespace,
                        aliases,
                        doc,
                        newFieldsDoc,
                        newOrders,
                        newAliases,
                        newDefaults,
                        minProperties,
                        maxProperties);
  }

}
