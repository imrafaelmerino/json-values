package jsonvalues.spec;

import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * The `SpecError` class represents an error that occurs during the validation of a JSON value against a
 * specification. It encapsulates information about the error, including the path where the error occurred,
 * the error code, and the value that triggered the error.
 * <p>
 * json-values and json-specs in particular, uses `SpecError` instances to report validation errors when a JSON value does not
 * conform to the specified rules and constraints.
 * <p>
 * Instances of this class are immutable and provide methods for creating and working with validation errors.
 * <p>
 * The `SpecError` class offers the following key functionality:
 * - Generating a human-readable string representation of the error for debugging and logging purposes.
 * - Implementing equality and hash code methods to compare error instances.
 *
 * @see ERROR_CODE
 * @see JsPath
 * @see JsError
 */
public final class SpecError {

    /**
     * The path where the error occurred within the JSON structure.
     */
    public final JsPath path;

    /**
     * The error code indicating the type of validation error.
     */
    public final ERROR_CODE errorCode;

    /**
     * The JSON value that triggered the error.
     */
    public final JsValue value;

    private SpecError(final JsPath path, final JsError pair) {
        this.path = path;
        this.errorCode = pair.error();
        this.value = pair.value();
    }

    /**
     * Creates a new `SpecError` instance with the specified path and error.
     *
     * @param path  The path where the error occurred within the JSON structure.
     * @param error The error object containing error code and value information.
     * @return A new `SpecError` instance representing the validation error.
     */
    static SpecError of(final JsPath path, final JsError error) {
        return new SpecError(requireNonNull(path), requireNonNull(error));
    }

    /**
     * Returns a human-readable string representation of the `SpecError` instance.
     *
     * @return A string representation of the error, including path, error code, and value.
     */
    @Override
    public String toString() {
        return "SpecError{" +
                "path=" + (path.isEmpty() ? "root" : path) +
                ", error=" + errorCode +
                ", value=" + value +
                '}';
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
                errorCode == specError.errorCode &&
                Objects.equals(value, specError.value);
    }

    /**
     * Computes the hash code of this `SpecError` instance.
     *
     * @return The hash code of the error instance based on its components.
     */
    @Override
    public int hashCode() {
        return Objects.hash(path, errorCode, value);
    }
}
