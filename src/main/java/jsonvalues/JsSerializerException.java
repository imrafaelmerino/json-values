package jsonvalues;

public class JsSerializerException extends RuntimeException {
	public JsSerializerException(String reason) {
		super(reason);
	}

	public JsSerializerException(String reason, Throwable cause) {
		super(reason, cause);
	}
}
