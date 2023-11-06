/**
 * The `jsonvalues.spec` package provides classes and interfaces for defining and working with JSON specifications (specs) and parsers. JSON specifications describe the expected structure and constraints of JSON data, while parsers are used to validate and parse JSON data against these specifications.
 * <p>
 * JSON Specifications (Specs):
 * <p>
 * The core concept in this package is the JSON specification, represented by various interfaces such as {@link jsonvalues.spec.JsSpec}, {@link jsonvalues.spec.JsArraySpec}, and {@link jsonvalues.spec.JsObjSpec}. These specifications define the schema and validation rules for JSON data, ensuring that it adheres to the expected structure.
 * <p>
 * - {@link jsonvalues.spec.JsSpec}: An interface that represents a generic JSON specification. Implementations of this interface can be used to specify constraints on JSON data types.
 * <p>
 * - {@link jsonvalues.spec.JsArraySpec}: An interface that extends `JsSpec` and is used to define specifications for JSON arrays. It allows you to specify constraints on the elements within a JSON array.
 * <p>
 * - {@link jsonvalues.spec.JsObjSpec}: An interface that extends `JsSpec` and is used to define specifications for JSON objects (maps). It allows you to specify constraints on the keys and values within a JSON object.
 * <p>
 * JSON Parsers:
 * <p>
 * JSON parsers, represented by classes like {@link jsonvalues.spec.JsParser}, are used to validate and parse JSON data based on the provided JSON specifications. Parsers ensure that the input JSON data conforms to the specified schema.
 * <p>
 * - {@link jsonvalues.spec.JsParser}: A class used to create JSON data parsers from JSON specifications. These parsers validate input JSON data against the specified schema and return the parsed JSON data if it conforms.
 * <p>
 * - {@link jsonvalues.spec.JsArraySpecParser}: A class for creating JSON array parsers from array specifications. These parsers validate and parse JSON arrays against the defined schema.
 * <p>
 * - {@link jsonvalues.spec.JsObjSpecParser}: A class for creating JSON object parsers from object specifications. These parsers validate and parse JSON objects against the specified schema.
 * <p>
 * Usage:
 * <p>
 * To use the classes and interfaces in this package, you typically follow these steps:
 * <p>
 * 1. Define a JSON specification using one of the `JsSpec` implementations like `JsArraySpec` or `JsObjSpec`. Specify the expected structure and constraints for JSON data.
 * <p>
 * 2. Create a JSON parser using the corresponding parser class, such as `JsArraySpecParser` or `JsObjSpecParser`, passing in the JSON specification.
 * <p>
 * 3. Use the parser to validate and parse JSON data. If the input data conforms to the specification, the parser returns the parsed JSON data; otherwise, it raises an exception indicating the validation failure.
 * <p>
 *
 * @see jsonvalues.spec.JsSpec
 * @see jsonvalues.spec.JsArraySpec
 * @see jsonvalues.spec.JsObjSpec
 * @see jsonvalues.spec.JsParser
 * @see jsonvalues.spec.JsArraySpecParser
 * @see jsonvalues.spec.JsObjSpecParser
 */

package jsonvalues.spec;