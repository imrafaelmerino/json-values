package jsonvalues;

/**
 * Represent an exception that is thrown while serializing a JSON into an array of bytes.
 *
 */
public class JsSerializerException extends RuntimeException {
	public JsSerializerException(String reason) {
		super(reason);
	}

	public JsSerializerException(String reason, Throwable cause) {
		super(reason, cause);
	}
}
