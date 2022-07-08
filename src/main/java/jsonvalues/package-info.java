/**
 * json-values is a one-package and zero-dependency library to work with jsons in a declarative and functional way.
 * The main exceptions thrown by the library are the following:
 * <ul>
 * <li>the custom unchecked {@link jsonvalues.UserError}, when the client makes a programming error. A suggestion in the message
 * to avoid the error is returned.</li>
 * <li>the custom unchecked {@link jsonvalues.JsValuesInternalError}, when something unexpected happens because a developer made a mistake.
 * An issue in GitHub must be created</li>
 * <li>the checked {@link jsonvalues.MalformedJson}, when a string can not be parsed into a json.</li>
 * <li>the unchecked NullPointerException, when a method different than equals receives a null parameter.</li>
 * </ul>
 * </pre>
 */
package jsonvalues;