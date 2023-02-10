package jsonvalues.spec;

import jsonvalues.JsArray;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

/**
 * Class to create JSON array parsers from specs
 */
public final class JsArraySpecParser {

    private final JsSpecParser parser;

    /**
     * @param spec the Json spec what defines the schema that every element in the array has to conform
     */
    public JsArraySpecParser(final JsArraySpec spec) {

        parser = requireNonNull(spec).parser();

    }

    /**
     * parses an array of bytes into a Json array that must conform the spec of the parser. If the
     * array of bytes doesn't represent a well-formed Json  or is a well-formed Json that doesn't
     * conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
     * returned.
     *
     * @param bytes a Json array serialized in an array of bytes
     * @return a try computation with the result
     */
    public JsArray parse(final byte[] bytes) {

        return JsIO.INSTANCE.deserializeToJsArray(requireNonNull(bytes),
                                                  this.parser
                                                 );
    }


    /**
     * parses a string into a Json array that must conform the spec of the parser. If the
     * string doesn't represent a well-formed Json array or is a well-formed Json that doesn't
     * conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
     * returned.
     *
     * @param str a Json array serialized in a string
     * @return a try computation with the result
     */
    public JsArray parse(String str) {
        return JsIO.INSTANCE
                .deserializeToJsArray(requireNonNull(str).getBytes(StandardCharsets.UTF_8),
                                      this.parser
                );
    }

    /**
     * parses an input stream of bytes into a Json array that must conform the spec of the parser. If the
     * the input stream of bytes doesn't represent a well-formed Json array or is a well-formed Json that doesn't
     * conform the spec of the parser, a ParsingException failure wrapped in a Try computation is
     * returned. Any I/O exception processing the input stream is wrapped in a Try computation as well
     *
     * @param inputstream the input stream of bytes
     * @return a try computation with the result
     */
    public JsArray parse(InputStream inputstream) {
        return JsIO.INSTANCE.deserializeToJsArray(requireNonNull(inputstream),
                                                  this.parser
                                                 );

    }


}

