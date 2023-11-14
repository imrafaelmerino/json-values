package jsonvalues.spec;

import jsonvalues.JsObj;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

/**
 * The {@code JsObjSpecParser} class is responsible for creating JSON object parsers based on provided JSON
 * specifications (specs). It allows you to define a JSON schema using a specification and then use that schema to parse
 * JSON data into structured Java objects. This class is part of the JSONValues library, which focuses on providing a
 * type-safe and functional approach to JSON parsing and manipulation.
 * <p>
 * A parser, in the context of this class, refers to an instance of {@link JsParser} that encapsulates the rules and
 * constraints defined in a JSON specification. The parser ensures that the JSON data being parsed conforms to the
 * specified schema, enforcing rules such as data types, required fields, and user-defined conditions.
 * <p>
 * Usage of this class typically involves creating an instance with a JSON specification and then using that instance to
 * parse JSON data. If the JSON data does not adhere to the specified schema, a {@link JsParserException} will be
 * raised, providing detailed information about the parsing code.
 * <p>
 * This class provides three main methods for parsing JSON data:
 * <ul>
 *     <li>{@link #parse(byte[]) parse(byte[] bytes)}: Parses a byte array representing JSON data into a JSON object.</li>
 *     <li>{@link #parse(String) parse(String str)}: Parses a JSON string into a JSON object.</li>
 *     <li>{@link #parse(InputStream) parse(InputStream inputStream)}: Parses JSON data from an input stream into a JSON object. This method also handles potential I/O exceptions when reading from the stream.</li>
 * </ul>
 * <p>
 * It's important to note that the provided JSON specification should match the structure and constraints of the JSON data you expect
 * to parse. The parser will enforce these constraints during parsing.
 */
public final class JsObjSpecParser {


    private final JsParser parser;

    private final JsSpec spec;


    private JsObjSpecParser(final JsSpec spec) {
        if (!isValid(requireNonNull(spec)))
            throw new IllegalArgumentException("`%s` constructor requires a `%s` or `OneSpecOf(%s)`".formatted(JsObjSpecParser.class.getName(),
                                                                                                               JsObjSpec.class.getName(),
                                                                                                               JsObjSpec.class.getName()
                                                                                                              ));
        this.spec = spec;
        parser = spec.parser();
    }

    /**
     * Creates a JSON object parser based on the provided JSON object specification (spec). The parser will validate
     * that every field in a JSON object adheres to the schema defined in the given specification.
     *
     * @param spec The JSON object specification that defines the expected schema for each field-value in the object.
     * @return a Json object parser
     */
    public static JsObjSpecParser of(final JsSpec spec) {
        return new JsObjSpecParser(spec);
    }

    private boolean isValid(JsSpec spec) {
        if (spec instanceof JsObjSpec) return true;
        if (spec instanceof OneOf oneOf)
            return oneOf.specs.stream().allMatch(it -> it instanceof JsObjSpec || it instanceof OneOf);
        return false;


    }

    /**
     * Parses an array of bytes into a JSON object that must conform to the spec of the parser. If the array of bytes
     * doesn't represent a well-formed JSON or is a well-formed JSON that doesn't conform to the spec of the parser, a
     * ParsingException failure wrapped in a Try computation is returned.
     *
     * @param bytes A JSON object serialized as an array of bytes.
     * @return A Try computation with the parsed JSON object or a ParsingException failure.
     * @throws NullPointerException if the provided byte array is null.
     * @throws JsParserException    If parsing fails due to JSON syntax errors or specification violations.
     */
    public JsObj parse(final byte[] bytes) {
        JsObj obj = JsIO.INSTANCE.parseToJsObj(requireNonNull(bytes),
                                               parser
                                              );

        assert spec.test(obj).isEmpty();

        return obj;

    }

    /**
     * Parses a string into a JSON object that must conform to the spec of the parser. If the string doesn't represent a
     * well-formed JSON or is a well-formed JSON that doesn't conform to the spec of the parser, a ParsingException
     * failure wrapped in a Try computation is returned.
     *
     * @param str A JSON object serialized as a string.
     * @return A Try computation with the parsed JSON object or a ParsingException failure.
     * @throws NullPointerException if the provided string is null.
     * @throws JsParserException    If parsing fails due to JSON syntax errors or specification violations.
     */
    public JsObj parse(final String str) {

        JsObj obj = JsIO.INSTANCE.parseToJsObj(requireNonNull(str).getBytes(StandardCharsets.UTF_8),
                                               parser
                                              );

        assert spec.test(obj).isEmpty();
        return obj;
    }

    /**
     * Parses an input stream of bytes into a JSON object that must conform to the spec of the parser. If the input
     * stream of bytes doesn't represent a well-formed JSON object or is a well-formed JSON that doesn't conform to the
     * spec of the parser, a ParsingException failure wrapped in a Try computation is returned. Any I/O exception that
     * occurs while processing the input stream is also wrapped in a Try computation.
     *
     * @param inputstream The input stream of bytes.
     * @return A Try computation with the parsed JSON object or a ParsingException failure.
     * @throws NullPointerException if the provided input stream is null.
     * @throws JsParserException    If parsing fails due to JSON syntax errors or specification violations.
     */
    public JsObj parse(final InputStream inputstream) {
        JsObj obj = JsIO.INSTANCE.parseToJsObj(requireNonNull(inputstream),
                                               parser
                                              );
        assert spec.test(obj).isEmpty();

        return obj;
    }


}


