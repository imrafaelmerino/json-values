package jsonvalues;

import java.io.UnsupportedEncodingException;

/**
 Exception that models an internal error made by a developer of json-values. An issue in GitHub should be open reporting
 the exception message.
 */
final class JsValuesInternalError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private JsValuesInternalError(final String message) {
        super(String.format("Create an issue in https://github.com/imrafaelmerino/values: %s.",
                            message
                           ));
    }

    private JsValuesInternalError(final Exception e) {
        super(e);
    }

    static JsValuesInternalError arrayOptionNotImplemented(final String option) {
        return new JsValuesInternalError(String.format("New option %s in enum JsArray.TYPE not implemented.",
                                                       option
                                              ));
    }

    static JsValuesInternalError encodingNotSupported(final UnsupportedEncodingException e) {
        return new JsValuesInternalError(e);
    }


    static JsValuesInternalError tokenNotExpected(String token) {
        return new JsValuesInternalError(String.format("token %s not expected during parsing",
                                                       token
                                              ));
    }
}
