/**
 * This package contains a set of generators for creating JSON-like data structures using the `jsonvalues` library.
 * These generators allow you to create random or biased instances of JSON-like data structures, such as `JsObject`, `JsArray`, and various primitive JSON value types.
 * You can use these generators for testing and generating sample data for your JSON-related applications.
 *
 * The package includes the following classes:
 *
 * - {@link jsonvalues.gen.JsArrayGen}: A generator for creating instances of {@link jsonvalues.JsArray}, which represent JSON arrays.
 * - {@link jsonvalues.gen.JsBigIntGen}: A generator for creating instances of {@link jsonvalues.JsBigInt}, which represent JSON integer values.
 * - {@link jsonvalues.gen.JsBigDecGen}: A generator for creating instances of {@link jsonvalues.JsBigDec}, which represent JSON decimal values.
 * - {@link jsonvalues.gen.JsBinaryGen}: A generator for creating instances of {@link jsonvalues.JsBinary}, which represent JSON binary values.
 * - {@link jsonvalues.gen.JsBoolGen}: A generator for creating instances of {@link jsonvalues.JsBool}, which represent JSON boolean values.
 * - {@link jsonvalues.gen.JsDoubleGen}: A generator for creating instances of {@link jsonvalues.JsDouble}, which represent JSON double values.
 * - {@link jsonvalues.gen.JsInstantGen}: A generator for creating instances of {@link jsonvalues.JsInstant}, which represent JSON instant values.
 * - {@link jsonvalues.gen.JsIntGen}: A generator for creating instances of {@link jsonvalues.JsInt}, which represent JSON integer values.
 * - {@link jsonvalues.gen.JsObjGen}: A generator for creating instances of {@link jsonvalues.JsObj}, which represent JSON objects.
 * - {@link jsonvalues.gen.JsStrGen}: A generator for creating instances of {@link jsonvalues.JsStr}, which represent JSON string values.
 *
 * The {@link fun.gen.Combinators} class provides utility methods for combining and manipulating generators, allowing you to create complex generator compositions easily.
 *
 * You can use these generators to create JSON-like data structures for testing and generating data for your applications.
 *
 * @see <a href="https://github.com/l15k4/js-values">js-values on GitHub</a>
 */
package jsonvalues.gen;
