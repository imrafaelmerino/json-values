package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.NameValidationSpecConstants.*;


/**
 * The {@code JsObjSpecBuilder} class is a builder for creating instances of {@code JsObjSpec} with additional metadata.
 * It allows configuring the name, namespace, documentation, field orders, aliases, and default values for a JSON object
 * specification.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * // Creating a simple JsObjSpec
 * JsObjSpec simpleSpec = JsObjSpecBuilder
 *         .withName("Person")
 *         .withNamespace("example.namespace")
 *         .withDoc("Specification for representing a person")
 *         .withFieldDocs(Map.of("name", "Full name of the person", "age", "Age of the person"))
 *         .withFieldOrders(Map.of("name", MetaData.ORDERS.DESC))
 *         .withFieldAliases(Map.of("firstName", List.of("name")))
 *         .withAliases(List.of("PersonRecord"))
 *         .withFieldsDefaults(Map.of("age", JsInt.of(30)))
 *         .build(existingJsObjSpec);
 *
 * // Creating a recursive JsObjSpec with defaults
 * JsObjSpec recursiveSpec = JsObjSpecBuilder.withName("person")
 *                                   .withFieldsDefaults(Map.of("father", JsNull.NULL))
 *                                   .build(JsObjSpec.of("name", JsSpecs.str(),
 *                                                       "age", JsSpecs.integer(),
 *                                                       "father", JsSpecs.ofNamedSpec("person").nullable()
 *                                                      )
 *                                                   .withOptKeys("father")
 *                                         );
 * }
 * </pre>
 * <p>
 * Note: When creating named specs using this builder, the specs are cached by `[namespace.]?name`. Aliases are also
 * cached. Attempting to create two specs with the same full name will result in an error.
 * <p>
 * When dealing with recursive specifications or generating Avro schemas with [avro-spec](<a
 * href="https://github.com/imrafaelmerino/avro-spec">...</a>), using the builder pattern is mandatory to create complex
 * specifications. For example, the creation of a recursive JsObjSpec with defaults is shown above.
 * <p>
 * Field aliases and field defaults configured through this builder are utilized by the {@link JsObjSpecParser} when
 * parsing JSON objects.
 */
public final class JsObjSpecBuilder {


    private final String name;
    private String doc;
    private Map<String, String> fieldsDoc;
    private Map<String, ORDERS> fieldsOrder;
    private Map<String, List<String>> fieldsAliases;
    private Map<String, JsValue> fieldsDefaults;
    private String nameSpace;
    private List<String> aliases;

    private JsObjSpecBuilder(String name) {
        if (!isValidName.test(requireNonNull(name)))
            throw new IllegalArgumentException(("The name of the JsObjSpec with name %s doesn't follow the " +
                                                "pattern %s"
                                               ).formatted(name,
                                                           AVRO_NAME_PATTERN));
        this.name = name;
    }

    /**
     * Sets the name of the JSON object specification. The name must follow the Avro naming conventions, adhering to the
     * regex pattern: {@code [A-Za-z_][A-Za-z0-9_]*}.
     *
     * @param name The name of the JSON object specification.
     * @return A reference to this {@code JsObjSpecBuilder} instance for method chaining.
     * @throws IllegalArgumentException If the provided name does not follow the Avro naming conventions.
     */
    public static JsObjSpecBuilder withName(final String name) {
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

    /**
     * Sets the namespace of the JSON object specification. The namespace must follow the Avro naming conventions,
     * adhering to the regex pattern: {@code [A-Za-z_][A-Za-z0-9_.]+}. It should start with a letter or an underscore,
     * followed by letters, numbers, underscores, or dots.
     *
     * @param nameSpace The namespace of the JSON object specification.
     * @return A reference to this {@code JsObjSpecBuilder} instance for method chaining.
     * @throws IllegalArgumentException If the provided namespace does not follow the Avro naming conventions.
     */
    public JsObjSpecBuilder withNamespace(final String nameSpace) {
        this.nameSpace = requireNonNull(nameSpace);
        if (!isValidNamespace.test(nameSpace))
            throw new IllegalArgumentException(("The namespace of the JsObjSpec with name %s doesn't follow the " +
                                                "pattern %s").formatted(name,
                                                                        AVRO_NAMESPACE_PATTERN));
        return this;
    }

    /**
     * Sets the documentation for the JsObjSpec.
     *
     * @param doc The documentation for the JsObjSpec.
     * @return This JsObjSpecBuilder for method chaining.
     */
    public JsObjSpecBuilder withDoc(final String doc) {
        this.doc = requireNonNull(doc);
        return this;
    }

    /**
     * Sets the field-level documentation for the JsObjSpec.
     *
     * @param fieldsDoc A map containing field names as keys and their corresponding documentation as values.
     * @return This JsObjSpecBuilder for method chaining.
     */
    public JsObjSpecBuilder withFieldDocs(final Map<String, String> fieldsDoc) {
        this.fieldsDoc = Collections.unmodifiableMap(requireNonNull(fieldsDoc));
        return this;
    }

    /**
     * Sets the order for fields in the JsObjSpec.
     *
     * @param fieldsOrder A map containing field names as keys and their corresponding order as values.
     * @return This JsObjSpecBuilder for method chaining.
     */
    public JsObjSpecBuilder withFieldOrders(final Map<String, ORDERS> fieldsOrder) {
        this.fieldsOrder = Collections.unmodifiableMap(requireNonNull(fieldsOrder));
        return this;
    }

    /**
     * Sets field aliases for the JsObjSpec.
     * <p>
     * Field aliases are used by the `JsObjSpecParser` to replace an alias with its corresponding main field during JSON
     * parsing. For example, if field 'b' is specified as an alias for field 'a', and the JSON input contains a field
     * named 'b', the parser will replace 'b' with 'a'.
     *
     * @param fieldsAliases A map containing field names as keys and a list of their corresponding aliases as values.
     * @return This JsObjSpecBuilder for method chaining.
     */
    public JsObjSpecBuilder withFieldAliases(Map<String, List<String>> fieldsAliases) {
        this.fieldsAliases = Collections.unmodifiableMap(requireNonNull(fieldsAliases));
        for (Map.Entry<String, List<String>> entry : fieldsAliases.entrySet()) {
            for (String alias : entry.getValue()) {
                if (requireNonNull(alias).isEmpty() || alias.isBlank())
                    throw new IllegalArgumentException("Alias empty or blank");
            }
        }
        return this;
    }

    /**
     * Sets aliases for the JsObjSpec. Must follow the avro naming conventions and, adhering to the regex pattern:
     * {@code [A-Za-z_][A-Za-z0-9_.]+}
     * <p>
     * Aliases provide alternative names for the JsObjSpec, and they can be used interchangeably when referring to the
     * same specification.
     *
     * @param aliases A list of alternative names (aliases) for the JsObjSpec.
     * @return This JsObjSpecBuilder for method chaining.
     * @throws IllegalArgumentException If any of the provided aliases does not follow the naming pattern.
     */
    public JsObjSpecBuilder withAliases(List<String> aliases) {
        this.aliases = Collections.unmodifiableList(requireNonNull(aliases));
        for (String alias : aliases) {
            if (!isValidNamespace.test(alias)) {
                throw new IllegalArgumentException(("The alias `%s` of the JsObjSpec with name `%s` doesn't follow " +
                                                    "the pattern `%s`").formatted(alias,
                                                                                  name,
                                                                                  AVRO_NAME_PATTERN));
            }

        }
        return this;
    }

    /**
     * Sets default values for fields in the JsObjSpec.
     * <p>
     * Default values are used when parsing JSON objects to replace missing fields with specified values. The provided
     * default values must conform to the specifications of the corresponding fields in the JsObjSpec.
     *
     * @param fieldsDefaults A map containing field names and their corresponding default values.
     * @return This JsObjSpecBuilder for method chaining.
     * @throws IllegalArgumentException If any default value does not conform to the field specification, or if a field
     *                                  with a default value is not defined in the JsObjSpec.
     * @see JsObjSpecParser
     */

    public JsObjSpecBuilder withFieldsDefaults(final Map<String, JsValue> fieldsDefaults) {
        this.fieldsDefaults = Collections.unmodifiableMap(requireNonNull(fieldsDefaults));
        return this;
    }

    /**
     * Builds the final JsObjSpec by combining the specified configuration with the provided base JsObjSpec.
     * <p>
     * This method validates and merges the configured metadata with the base JsObjSpec to create a new JsObjSpec. The
     * resulting JsObjSpec is then cached for future reference.
     *
     * @param spec The base JsObjSpec to which the configured metadata is applied.
     * @return The final JsObjSpec with the specified configuration.
     * @throws IllegalArgumentException If any configured metadata is invalid or conflicts with the base JsObjSpec.
     * @see JsObjSpecParser
     */
    public JsObjSpec build(final JsObjSpec spec) {
        Objects.requireNonNull(spec);
        if (fieldsDefaults != null) validateDefaults(spec, fieldsDefaults);
        if (fieldsDoc != null) validateDocs(spec, fieldsDoc);
        if (fieldsOrder != null) validateOrders(spec, fieldsOrder);
        if (fieldsAliases != null) validateAliases(spec, fieldsAliases);


        var metadata = new MetaData(name, nameSpace, aliases, doc, fieldsDoc, fieldsOrder, fieldsAliases, fieldsDefaults);
        var metadataSpec = new JsObjSpec(spec.bindings,
                                         spec.nullable,
                                         spec.strict,
                                         spec.predicate,
                                         spec.requiredFields,
                                         metadata);
        JsSpecCache.putAll(metadata.getFullName(), aliases, metadataSpec);

        return metadataSpec;

    }


    private void validateAliases(JsObjSpec spec, Map<String, List<String>> fieldsAlias) {
        Map<String, JsSpec> bindings = spec.getBindings();
        List<String> allAliases = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : fieldsAlias.entrySet()) {
            var key = entry.getKey();
            if (entry.getValue().contains(key))
                throw new IllegalArgumentException("The field `%s` can not be contained in the aliases".formatted(key));
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("The field `%s`is not defined in the JsObjSpec with name `%s`".formatted(key, name));
            if (containsDuplicates(entry.getValue()))
                throw new IllegalArgumentException("The field `%s` has duplicated aliases".formatted(key));
            allAliases.addAll(entry.getValue());
        }
        if (containsDuplicates(allAliases))
            throw new IllegalArgumentException("Found duplicate in aliases for spec `%s`.".formatted(name));

    }

    private void validateOrders(JsObjSpec spec, Map<String, ORDERS> fieldsOrder) {
        Map<String, JsSpec> bindings = spec.getBindings();

        for (Map.Entry<String, ORDERS> entry : fieldsOrder.entrySet()) {
            var key = entry.getKey();
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("The key `%s`is not defined in the JsObjSpec with name %s".formatted(key, name));
        }
    }

    private void validateDocs(JsObjSpec spec, Map<String, String> fieldsDoc) {
        Map<String, JsSpec> bindings = spec.getBindings();

        for (Map.Entry<String, String> entry : fieldsDoc.entrySet()) {
            var key = entry.getKey();
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("The key `%s` is not defined in the JsObjSpec with name %s".formatted(key, name));
        }
    }

    private void validateDefaults(JsObjSpec spec, Map<String, JsValue> fieldsDefaults) {

        Map<String, JsSpec> bindings = spec.getBindings();

        for (Map.Entry<String, JsValue> entry : fieldsDefaults.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            if (value == null)
                throw new IllegalArgumentException("The value of the key `%s` of `fieldsDefaults` can not be null".formatted(key));
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("The key `%s` is not defined in the JsObjSpec with name %s".formatted(key, name));
            JsSpec keySpec = bindings.get(key);
            if (keySpec instanceof OneOf oneOf) {
                var errors = oneOf.getSpecs().get(0).test(value);
                if (!errors.isEmpty())
                    throw new IllegalArgumentException(("The default value `%s` doesn't conform the FIRST spec " +
                                                        "associated to the key `%s` of the JsObjSpec with name `%s`"
                                                       ).formatted(value,
                                                                   key,
                                                                   name));
            } else if (keySpec instanceof NamedSpec) {
                if (value.isNotNull())
                    throw new IllegalArgumentException("Named specs doesn't support default values different than null. " +
                                                       "They can't be used to validate the default values before being " +
                                                       "created and cached.");
                else if (!keySpec.isNullable())
                    throw new IllegalArgumentException("The default value for `%s` is null but the spec is not nullable".formatted(key));
            }
            else {
                var errors = keySpec.test(value);
                if (!errors.isEmpty())
                    throw new IllegalArgumentException(("The default value `%s` doesn't conform the spec associated to" +
                                                        " the key `%s` of the JsObjSpec with name `%s`"
                                                       ).formatted(value, key, name));
            }
        }
    }

    public enum ORDERS {ascending, descending, ignore}
}