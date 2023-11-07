package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsStr;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * The `SpecError` class represents a code that occurs during the validation of a JSON value against a specification.
 * It encapsulates information about the code, including the path where the code occurred, the code, and the
 * value that triggered the code.
 * <p>
 * json-values and json-specs in particular, uses `SpecError` instances to report validation errors when a JSON value
 * does not conform to the specified rules and constraints.
 * <p>
 * Instances of this class are immutable and provide methods for creating and working with validation errors.
 * <p>
 * The `SpecError` class offers the following key functionality: - Generating a human-readable string representation of
 * the code for debugging and logging purposes. - Implementing equality and hash code methods to compare code
 * instances.
 *
 * @see ERROR_CODE
 * @see JsPath
 * @see JsError
 */
public final class SpecError {

    /**
     * The path where the code occurred within the JSON structure.
     */
    public final JsPath path;
    /**
     * The code indicating the type of validation code.
     */
    public final JsError error;
    /**
     * The JSON value that triggered the code.
     */
    public String spec;

    private SpecError(final JsPath path, final JsError error) {
        this.path = path;
        this.error = error;
    }

    /**
     * Creates a new `SpecError` instance with the specified path and code.
     *
     * @param path  The path where the code occurred within the JSON structure.
     * @param error The code object containing code and value information.
     * @return A new `SpecError` instance representing the validation code.
     */
    static SpecError of(final JsPath path, final JsError error) {
        return new SpecError(requireNonNull(path), requireNonNull(error));
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    /**
     * Returns a human-readable string representation of the `SpecError` instance.
     *
     * @return A string representation of the code, including path, code, and value.
     */
    @Override
    public String toString() {
        JsObj error = JsObj.of("path", JsStr.of(path.toString()),
                               "value", this.error.value(),
                               "code", JsStr.of(this.error.code().name()));
        if (spec != null && !spec.isEmpty()) error = error.set("spec", JsStr.of(spec));
        return error.toString();
    }

    /**
     * Checks whether this `SpecError` instance is equal to another object.
     *
     * @param o The object to compare to this `SpecError`.
     * @return `true` if the objects are equal, `false` otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecError specError = (SpecError) o;
        return Objects.equals(path, specError.path) &&
               Objects.equals(spec, specError.spec) &&
               error.code() == specError.error.code() &&
               Objects.equals(error.value(), specError.error.value());
    }

    /**
     * Computes the hash code of this `SpecError` instance.
     *
     * @return The hash code of the code instance based on its components.
     */
    @Override
    public int hashCode() {
        return Objects.hash(path,error, spec);
    }
}
