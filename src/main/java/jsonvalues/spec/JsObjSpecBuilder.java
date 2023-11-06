package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.AvroUtils.*;


//todo get from full name e implementar tipos recursivos
public final class JsObjSpecBuilder {

    private static final List<String> namesCreated = new ArrayList<>();

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

    private static boolean containsDuplicates(List<String> list) {
        Set<String> uniqueSet = new HashSet<>();

        for (String element : list) {
            if (!uniqueSet.add(element)) {
                return true;
            }
        }
        return false;
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

    public JsObjSpecBuilder docFields(Map<String, String> fieldsDoc) {
        this.fieldsDoc = Collections.unmodifiableMap(requireNonNull(fieldsDoc));
        return this;
    }

    public JsObjSpecBuilder orderFields(Map<String, MetaData.ORDERS> fieldsOrder) {
        this.fieldsOrder = Collections.unmodifiableMap(requireNonNull(fieldsOrder));
        return this;
    }

    public JsObjSpecBuilder aliasesFields(Map<String, List<String>> fieldsAliases) {
        this.fieldsAliases = Collections.unmodifiableMap(requireNonNull(fieldsAliases));
        for (Map.Entry<String, List<String>> entry : fieldsAliases.entrySet()) {
            for (String alias : entry.getValue()) {
                if (requireNonNull(alias).isEmpty() || alias.isBlank())
                    throw new IllegalArgumentException("Alias empty of blank");
            }
        }
        return this;
    }

    public JsObjSpecBuilder aliases(List<String> aliases) {
        this.aliases = Collections.unmodifiableList(requireNonNull(aliases));
        for (String alias : aliases) {
            if (!isValidName.test(alias)) {
                throw new IllegalArgumentException("The alias %s of the JsObjSpec with name %s doesn't follow the pattern %s".formatted(alias,
                                                                                                                                        name,
                                                                                                                                        AVRO_NAME_PATTERN));
            }

        }
        return this;
    }


    public JsObjSpecBuilder defaultFields(Map<String, JsValue> fieldsDefaults) {
        this.fieldsDefaults = Collections.unmodifiableMap(requireNonNull(fieldsDefaults));
        return this;
    }

    public JsObjSpec spec(JsObjSpec spec) {
        if (fieldsDefaults != null) validateDefaults(spec, fieldsDefaults);
        if (fieldsDoc != null) validateDocs(spec, fieldsDoc);
        if (fieldsOrder != null) validateOrders(spec, fieldsOrder);
        if (fieldsAliases != null) validateAliases(spec, fieldsAliases);

        var metadata = new MetaData(name, nameSpace, aliases, doc, fieldsDoc, fieldsOrder, fieldsAliases, fieldsDefaults);
        validateSpecWithSameNameNotCreatedSoFar(metadata.getFullName());
        return new JsObjSpec(spec.bindings,
                             spec.nullable,
                             spec.strict,
                             spec.predicate,
                             spec.requiredFields,
                             metadata);

    }

    private void validateSpecWithSameNameNotCreatedSoFar(String fullName) {

        synchronized (JsObjSpecBuilder.class) {
            if (namesCreated.contains(fullName))
                throw new IllegalArgumentException("The spec %s has already been created.Choose another namespace/name".formatted(fullName));
            else namesCreated.add(fullName);
        }
    }

    private void validateAliases(JsObjSpec spec, Map<String, List<String>> fieldsAlias) {
        Map<String, JsSpec> bindings = spec.getBindings();
        List<String> allAliases = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : fieldsAlias.entrySet()) {
            var key = entry.getKey();
            if (entry.getValue().contains(key))
                throw new IllegalArgumentException("The field `%s` can to be contained in the aliases".formatted(key));
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("The field `%s` of the aliases map is not defined in the JsObjSpec with name %s".formatted(key, name));
            if (containsDuplicates(entry.getValue()))
                throw new IllegalArgumentException("The field `%s` has duplicated aliases".formatted(key));
            allAliases.addAll(entry.getValue());
        }
        if (containsDuplicates(allAliases))
            throw new IllegalArgumentException("Found duplicate in aliases for spec `%s`.".formatted(name));

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
            if (value == null) throw new IllegalArgumentException("key `%s` of `fieldsDefaults` can not be null");
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("The key %s of the defaults map is not defined in the JsObjSpec with name %s".formatted(key, name));
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
                    throw new IllegalArgumentException("The default value `%s` doesn't conform the FIRST spec associated to the key `%s` of the JsObjSpec with name `%s`".formatted(value,
                                                                                                                                                                                    key, name));
            } else {
                var errors = keySpec.test(value);
                if (!errors.isEmpty())
                    throw new IllegalArgumentException("The default value `%s` doesn't conform the spec associated to the key `%s` of the JsObjSpec with name `%s`".formatted(value, key, name));
            }
        }
    }
}