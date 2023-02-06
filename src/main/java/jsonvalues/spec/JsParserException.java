package jsonvalues.spec;


public class JsParserException extends RuntimeException {

	private JsParserException(String reason) {
		super(reason);
	}

	private JsParserException(String reason, Throwable cause) {
		super(reason, cause);
	}

	public static JsParserException create(String reason, boolean withStackTrace) {
		return withStackTrace
				? new JsParserException(reason)
				: new ParsingStacklessException(reason);
	}

	public static JsParserException create(String reason, int index, boolean withStackTrace) {
		return withStackTrace
				? new JsParserException(reason+" @ position="+index)
				: new ParsingStacklessException(reason+" @ position="+index);
	}


	public static JsParserException create(String reason, Throwable cause, boolean withStackTrace) {
		return withStackTrace
				? new JsParserException(reason, cause)
				: new ParsingStacklessException(reason, cause);
	}

	private static class ParsingStacklessException extends JsParserException {

		private ParsingStacklessException(String reason) {
			super(reason);
		}

		private ParsingStacklessException(String reason, Throwable cause) {
			super(reason, cause);
		}

		@Override
		public synchronized Throwable fillInStackTrace() {
			return this;
		}
	}
}
