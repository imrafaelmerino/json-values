package com.fasterxml.jackson.core;

import java.io.*;
import java.net.URL;

/**
 * Intermediate base class for actual format-specific factories for constructing
 * parsers (reading) and generators (writing). Although full power will only be
 * available with Jackson 3, skeletal implementation added in 2.10 to help conversion
 * of code for 2.x to 3.x migration of projects depending on Jackson
 *
 * @since 2.10
 */
 abstract class TokenStreamFactory
    implements Versioned
{

    /*
    /**********************************************************************
    /* Capability introspection
    /**********************************************************************
     */



    /**
     * Introspection method that higher-level functionality may call
     * to see whether underlying data format can read and write binary
     * data natively; that is, embeded it as-is without using encodings
     * such as Base64.
     *<p>
     * Default implementation returns <code>false</code> as JSON does not
     * support native access: all binary content must use Base64 encoding.
     * Most binary formats (like Smile and Avro) support native binary content.
     *
     * @return Whether format supported by this factory
     *    supports native binary content
     */
     abstract boolean canHandleBinaryNatively();








    /**
     * Method that returns short textual id identifying format
     * this factory supports.
     *
     * @return Name of the format handled by parsers, generators this factory creates
     */
     abstract String getFormatName();





     abstract JsonParser createParser(Reader r) throws IOException;
     abstract JsonParser createParser(String content) throws IOException;


    /*
    /**********************************************************************
    /* Factory methods, generators
    /**********************************************************************
     */



}
