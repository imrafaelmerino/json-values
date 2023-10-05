/**
 * The `jsonvalues` package provides a set of classes and interfaces for working with JSON (JavaScript Object Notation) data in a type-safe and structured manner. It includes representations for various JSON data types, such as objects, arrays, primitives, and more.
 * <p>
 * JSON Data Types:
 * <p>
 * - {@link jsonvalues.JsValue}: An interface representing a JSON value, which can be one of the following types: object, array, primitive, null, or nothing. It serves as the root interface for all JSON data types.
 * <p>
 * - {@link jsonvalues.JsObj}: A class representing a JSON object (also known as a map or dictionary). It maps string keys to JSON values.
 * <p>
 * - {@link jsonvalues.JsArray}: A class representing a JSON array, which is an ordered collection of JSON values.
 * <p>
 * - {@link jsonvalues.JsPrimitive}: An interface representing a JSON primitive value, which can be one of the following types: string, number, boolean, or null. It extends `JsValue`.
 * <p>
 * - {@link jsonvalues.JsStr}: A class representing a JSON string value. It extends `JsPrimitive`.
 * <p>
 * - {@link jsonvalues.JsInt}: A class representing a JSON integer value. It extends `JsPrimitive`.
 * <p>
 * - {@link jsonvalues.JsLong}: A class representing a JSON long (64-bit integer) value. It extends `JsPrimitive`.
 * <p>
 * - {@link jsonvalues.JsBigInt}: A class representing a JSON big integer value. It extends `JsPrimitive`.
 * <p>
 * - {@link jsonvalues.JsDouble}: A class representing a JSON double-precision floating-point number value. It extends `JsPrimitive`.
 * <p>
 * - {@link jsonvalues.JsBigDec}: A class representing a JSON big decimal value. It extends `JsPrimitive`.
 * <p>
 * - {@link jsonvalues.JsBool}: A class representing a JSON boolean value. It extends `JsPrimitive`.
 * <p>
 * - {@link jsonvalues.JsNull}: A class representing a JSON null value. It extends `JsPrimitive`.
 * <p>
 * - {@link jsonvalues.JsNothing}: A class representing a special JSON value called "nothing," which represents a non-existing or undefined value. It extends `JsPrimitive`.
 * <p>
 * JSON Manipulation and Creation:
 * <p>
 * The classes and interfaces in this package allow you to create, manipulate, and traverse JSON data structures. You can build JSON objects, arrays, and primitives, perform operations on them, and serialize/deserialize them as needed.
 * <p>
 * The classes and interfaces in the `jsonvalues` package provide a powerful and flexible way to work with JSON data, making it easier to handle JSON-related tasks in your applications.
 * <p>
 *
 * @see jsonvalues.JsValue
 * @see jsonvalues.JsObj
 * @see jsonvalues.JsArray
 * @see jsonvalues.JsPrimitive
 * @see jsonvalues.JsStr
 * @see jsonvalues.JsInt
 * @see jsonvalues.JsLong
 * @see jsonvalues.JsBigInt
 * @see jsonvalues.JsDouble
 * @see jsonvalues.JsBigDec
 * @see jsonvalues.JsBool
 * @see jsonvalues.JsNull
 * @see jsonvalues.JsNothing
 */
package jsonvalues;
