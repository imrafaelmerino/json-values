package com.fasterxml.jackson.core;

/**
 * Intermediate base class for all read-side streaming processing problems, including
 * parsing and input value coercion problems.
 *<p>
 * Added in 2.10 to eventually replace {@link JsonParseException}.
 *
 * @since 2.10
 */
 abstract class StreamReadException
    extends JsonProcessingException
{
    final static long serialVersionUID = 2L;

    protected transient JsonParser _processor;



    protected StreamReadException(JsonParser p, String msg) {
        super(msg, (p == null) ? null : p.getCurrentLocation());
        _processor = p;
    }

    protected StreamReadException(JsonParser p, String msg, Throwable root) {
        super(msg, (p == null) ? null : p.getCurrentLocation(), root);
        _processor = p;
    }

    protected StreamReadException(JsonParser p, String msg, JsonLocation loc) {
        super(msg, loc, null);
        _processor = p;
    }



    @Override
     JsonParser getProcessor() {
        return _processor;
    }




    /**
     * Overriding the getMessage() to include the request body
     */
    @Override
    public String getMessage() {
        String msg = super.getMessage();

        return msg;
    }
}
