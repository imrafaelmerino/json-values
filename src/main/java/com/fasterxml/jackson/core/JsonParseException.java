/* Jackson JSON-processor.
 *
 * Copyright (c) 2007- Tatu Saloranta, tatu.saloranta@iki.fi
 */

package com.fasterxml.jackson.core;

/**
 * Exception type for parsing problems, used when non-well-formed content
 * (content that does not conform to JSON syntax as per specification)
 * is encountered.
 */
 class JsonParseException
    extends StreamReadException
{
    private static final long serialVersionUID = 2L; // 2.7



    /**
     * Constructor that uses current parsing location as location, and
     * sets processor (accessible via {@link #getProcessor()}) to
     * specified parser.
     *
     * @param p Parser in use when encountering issue reported
     * @param msg Base exception message to use
     *
     * @since 2.7
     */
     JsonParseException(JsonParser p, String msg) {
        super(p, msg);
    }

    // @since 2.7
     JsonParseException(JsonParser p, String msg, Throwable root) {
        super(p, msg, root);
    }

    // @since 2.7




    // NOTE: overloaded in 2.10 just to retain binary compatibility with 2.9 (remove from 3.0)
    @Override
     JsonParser getProcessor() {
        return super.getProcessor();
    }




    // NOTE: overloaded in 2.10 just to retain binary compatibility with 2.9 (remove from 3.0)
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
