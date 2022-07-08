package jsonvalues;

/**
 * Exception that models an internal error made by a developer of json-values. An issue in GitHub should be open reporting
 * the exception message.
 */
final class JsValuesInternalError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private JsValuesInternalError( String message) {
        super(String.format("Create an issue in https://github.com/imrafaelmerino/values: %s.",
                            message
        ));
    }

    static JsValuesInternalError arrayOptionNotImplemented(final String option) {
        return new JsValuesInternalError(String.format("New option %s in enum JsArray.TYPE not implemented.",
                                                       option
        ));
    }

    static JsValuesInternalError tokenNotExpected(String token) {
        return new JsValuesInternalError(String.format("token %s not expected during parsing",
                                                       token
        ));
    }
}
