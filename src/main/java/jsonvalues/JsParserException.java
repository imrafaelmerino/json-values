package jsonvalues;


public class JsParserException extends RuntimeException {

	private JsParserException(String reason) {
		super(reason);
	}

	private JsParserException(String reason, Throwable cause) {
		super(reason, cause);
	}
	

	public static JsParserException reasonAt(String reason, long index) {
		return new JsParserException(reason+" @ position="+index);

	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

	public static JsParserException reasonFrom(String reason, Throwable cause) {
		return new JsParserException(reason, cause);
	}


}
