package jsonvalues;


/**
 * Represent an exception that is thrown while parsing an array of bytes or string into a JSON.
 *
 */
@SuppressWarnings("serial")
public class JsParserException extends RuntimeException {

	private JsParserException(String reason) {
		super(reason);
	}

	public static JsParserException reasonAt(String reason, long index) {
		return new JsParserException(reason+" @ position="+index);

	}





}
