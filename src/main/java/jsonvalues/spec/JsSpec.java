package jsonvalues.spec;

import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * The `JsSpec` interface represents a specification for validating JSON data structures. It provides methods and
 * contracts for defining validation rules and constraints on JSON values to ensure they conform to expected formats and
 * patterns.
 * <p>
 * JSON specifications are essential for parsing and validating JSON data in applications, ensuring that the data
 * adheres to predefined rules before processing or storing it.
 * <p>
 * Implementations of this interface define custom validation rules and provide methods for testing JSON values against
 * these rules. The primary goal is to verify whether a given JSON value satisfies the specification.
 * <p>
 * The `JsSpec` interface offers the following key functionality: - Enabling the nullable flag, indicating whether the
 * JSON value can be null. - Retrieving the deserializer used during the parsing process for parsing arrays of bytes or
 * strings into JSON values. - Reading and parsing JSON values token by token from a reader while verifying if they
 * conform to the specification. - Testing JSON values against the specification and returning a set of path/code pairs
 * for validation errors.
 * <p>
 * This interface serves as a foundation for building a JSON validation framework and allows for the creation of custom
 * JSON specifications for various data types, including numbers, strings, arrays, objects, and more.
 * <p>
 * Implementations of this interface should be immutable and thread-safe to support concurrent usage.
 *
 * @see JsValue
 * @see JsParser
 * @see SpecError
 * @see JsPath
 * @see JsReader
 */
public sealed interface JsSpec permits JsArraySpec, JsObjSpec, JsOneErrorSpec, NamedSpec, OneOf {

    /**
     * Returns the same spec with the nullable flag enabled.
     *
     * @return A new `JsSpec` instance with the nullable flag enabled.
     */
    JsSpec nullable();

    /**
     * Returns the deserializer used during the parsing process to parse an array of bytes or strings into a JSON
     * value.
     *
     * @return The deserializer used during parsing.
     */
    JsParser parser();

    /**
     * Low-level method to parse a JSON value from their string representation. Returns the JsValue if it conforms to
     * this spec, otherwise throws a `JsParserException`.
     *
     * @param json The reader to parse JSON values from.
     * @return The next token as a `JsValue`.
     * @throws JsParserException If the parsed value does not conform to this spec.
     */
    default JsValue parse(final String json) throws JsParserException {
        var reader = JsIO.INSTANCE.createReader(Objects.requireNonNull(json).getBytes(StandardCharsets.UTF_8));
        reader.readNextToken();
        return parser().parse(reader);
    }

    /**
     * Verify if the given JSON value satisfies this spec.
     *
     * @param parentPath The path where the tested value is located within the JSON structure.
     * @param value      The JSON value to be tested.
     * @return A set of path/code pairs representing validation errors.
     */
    List<SpecError> test(final JsPath parentPath, final JsValue value);

    /**
     * Verify if the given JSON value satisfies this spec, starting from the root path.
     *
     * @param value The JSON value to be tested.
     * @return A set of path/code pairs representing validation errors.
     */
    default List<SpecError> test(final JsValue value) {
        return test(JsPath.empty(), value);
    }


    boolean isNullable();

}


