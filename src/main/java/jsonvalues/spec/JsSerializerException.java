package jsonvalues.spec;

/**
 * Represent an exception that is thrown while serializing a JSON into an array of bytes.
 */
@SuppressWarnings("serial")
public class JsSerializerException extends RuntimeException {

  JsSerializerException(String reason) {
    super(reason);
  }

  JsSerializerException(String reason,
                        Throwable cause) {
    super(reason,
          cause);
  }
}
