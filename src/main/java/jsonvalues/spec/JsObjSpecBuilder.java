package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.AvroUtils.*;

public final class JsObjSpecBuilder {


    private final String name;
    private String doc;
    private Map<String, String> fieldsDoc;
    private Map<String, MetaData.ORDERS> fieldsOrder;
    private Map<String, List<String>> fieldsAliases;
    private Map<String, JsValue> fieldsDefaults;
    private String nameSpace;
    private List<String> aliases;

    private JsObjSpecBuilder(String name) {
        if (!isValidName.test(name))
            throw new IllegalArgumentException("The name of the JsObjSpec with name %s doesn't follow the pattern %s".formatted(name, AVRO_NAME_PATTERN));
        this.name = name;
    }

    public static JsObjSpecBuilder name(final String name) {
        return new JsObjSpecBuilder(name);
    }

    public JsObjSpecBuilder namespace(String nameSpace) {
        this.nameSpace = requireNonNull(nameSpace);
        if (!isValidNamespace.test(nameSpace))
            throw new IllegalArgumentException("The namespace of the JsObjSpec with name %s doesn't follow the pattern %s".formatted(name,
                                                                                                                                     AVRO_NAMESPACE_PATTERN));
        return this;
    }

    public JsObjSpecBuilder doc(String doc) {
        this.doc = requireNonNull(doc);
        return this;
    }

    public JsObjSpecBuilder fieldsDoc(Map<String, String> fieldsDoc) {
        this.fieldsDoc = requireNonNull(fieldsDoc);
        return this;
    }

    public JsObjSpecBuilder fieldsOrder(Map<String, MetaData.ORDERS> fieldsOrder) {
        this.fieldsOrder = requireNonNull(fieldsOrder);
        return this;
    }

    public JsObjSpecBuilder fieldsAliases(Map<String, List<String>> fieldsAliases) {
        this.fieldsAliases = requireNonNull(fieldsAliases);
        for (Map.Entry<String, List<String>> entry : fieldsAliases.entrySet()) {
            for (String alias : entry.getValue()) {
                if (!isValidName.test(alias)) {
                    throw new IllegalArgumentException("The alias %s associated to key %s of the JsObjSpec with name %s doesn't follow the pattern %s".formatted(alias,
                                                                                                                                                                 entry.getKey(),
                                                                                                                                                                 name,
                                                                                                                                                                 AVRO_NAME_PATTERN));
                }
            }
        }
        return this;
    }

    public JsObjSpecBuilder aliases(List<String> aliases) {
        this.aliases = requireNonNull(aliases);
        for (String alias : aliases) {
            if (!isValidName.test(alias)) {
                throw new IllegalArgumentException("The alias %s of the JsObjSpec with name %s doesn't follow the pattern %s".formatted(alias,
                                                                                                                                        name,
                                                                                                                                        AVRO_NAME_PATTERN));
            }

        }
        return this;
    }

    public JsObjSpecBuilder fieldsDefault(Map<String, JsValue> fieldsDefaults) {
        this.fieldsDefaults = requireNonNull(fieldsDefaults);
        return this;
    }

    public JsObjSpec spec(JsObjSpec spec) {
        if (fieldsDefaults != null) validateDefaults(spec, fieldsDefaults);
        if (fieldsDoc != null) validateDocs(spec, fieldsDoc);
        if (fieldsOrder != null) validateOrders(spec, fieldsOrder);
        if (fieldsAliases != null) validateAliases(spec, fieldsAliases);
        var metadata = new MetaData(name, nameSpace, aliases, doc, fieldsDoc, fieldsOrder, fieldsAliases, fieldsDefaults);
        return new JsObjSpec(spec.bindings,
                             spec.nullable,
                             spec.strict,
                             spec.predicate,
                             spec.requiredFields,
                             metadata);

    }

    private void validateAliases(JsObjSpec spec, Map<String, List<String>> fieldsAlias) {
        Map<String, JsSpec> bindings = spec.getBindings();

        for (Map.Entry<String, List<String>> entry : fieldsAlias.entrySet()) {
            var key = entry.getKey();
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("The key %s of the aliases map is not defined in the JsObjSpec with name %s".formatted(key, name));
        }
    }

    private void validateOrders(JsObjSpec spec, Map<String, MetaData.ORDERS> fieldsOrder) {
        Map<String, JsSpec> bindings = spec.getBindings();

        for (Map.Entry<String, MetaData.ORDERS> entry : fieldsOrder.entrySet()) {
            var key = entry.getKey();
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("The key %s of the orders map is not defined in the JsObjSpec with name %s".formatted(key, name));
        }
    }

    private void validateDocs(JsObjSpec spec, Map<String, String> fieldsDoc) {
        Map<String, JsSpec> bindings = spec.getBindings();

        for (Map.Entry<String, String> entry : fieldsDoc.entrySet()) {
            var key = entry.getKey();
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("The key %s of the docs map is not defined in the JsObjSpec with name %s".formatted(key, name));
        }
    }

    private void validateDefaults(JsObjSpec spec, Map<String, JsValue> fieldsDefaults) {

        Map<String, JsSpec> bindings = spec.getBindings();

        for (Map.Entry<String, JsValue> entry : fieldsDefaults.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("The key %s of the defaults map is not defined in the JsObjSpec with name %s".formatted(key, name));
            if (value.isNull()) throw new IllegalArgumentException("The default value can't be null");
            JsSpec keySpec = bindings.get(key);
            if (keySpec instanceof OneOf oneOf) {
                var errors = oneOf.getSpecs().get(0).test(value);
                if (!errors.isEmpty())
                    throw new IllegalArgumentException("The default value `%s` doesn't conform the FIRST spec associated to the key %s of the JsObjSpec with name %s".formatted(value,
                                                                                                                                                                                key,
                                                                                                                                                                                name));
            } else if (keySpec instanceof OneOfObjSpec oneOf) {
                var errors = oneOf.getSpecs().get(0).test(value);
                if (!errors.isEmpty())
                    throw new IllegalArgumentException("The default value `%s` doesn't conform the FIRST spec associated to the key %s of the JsObjSpec with name %s".formatted(value,
                                                                                                                                                                                key, name));
            } else {
                var errors = keySpec.test(value);
                if (!errors.isEmpty())
                    throw new IllegalArgumentException("The default value `%s` doesn't conform the spec associated to the key %s of the JsObjSpec with name %s".formatted(value, key, name));
            }
        }
    }
}