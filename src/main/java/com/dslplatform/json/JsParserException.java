package com.dslplatform.json;

public final class JsParserException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JsParserException(final String message,int currentIndex) {
        super(message+" @ index="+currentIndex);
    }

    public JsParserException(final Throwable cause) {
        super(cause.getMessage(),cause);
    }

}
