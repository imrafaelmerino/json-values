package jsonvalues.spec;

import static java.util.Objects.requireNonNull;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import jsonvalues.JsArray;

/**
 * The {@code JsArraySpecParser} class is responsible for creating JSON array parsers based on provided JSON
 * specifications (specs). It allows you to define a schema for JSON arrays using a specification and then use that
 * schema to parse JSON array data into structured Java objects. This class is part of the JSONValues library, which
 * focuses on providing a type-safe and functional approach to JSON parsing and manipulation.
 * <p>
 * A parser, in the context of this class, refers to an instance of {@link JsParser} that encapsulates the rules and
 * constraints defined in a JSON specification. The parser ensures that the JSON array data being parsed conforms to the
 * specified schema, enforcing rules such as element data types, array length, and user-defined conditions.
 * <p>
 * Usage of this class typically involves creating an instance with a JSON specification and then using that instance to
 * parse JSON array data. If the JSON array data does not adhere to the specified schema, a {@link JsParserException}
 * will be raised, providing detailed information about the parsing code.
 * <p>
 * This class provides three main methods for parsing JSON array data:
 * <ul>
 *     <li>{@link #parse(byte[]) parse(byte[] bytes)}: Parses a byte array representing JSON array data into a JSON array.</li>
 *     <li>{@link #parse(String) parse(String str)}: Parses a JSON array string into a JSON array.</li>
 *     <li>{@link #parse(InputStream) parse(InputStream inputStream)}: Parses JSON array data from an input stream into a JSON array. This method also handles potential I/O exceptions when reading from the stream.</li>
 * </ul>
 *
 * <p>
 * It's important to note that the provided JSON specification should match the structure and constraints of the JSON array data you expect to parse. The parser will enforce these constraints during parsing.
 */
public final class JsArraySpecParser {

  private final JsParser parser;

  private final JsSpec spec;


  private JsArraySpecParser(final JsSpec spec) {
    if (!isValid(requireNonNull(spec))) {
      throw new IllegalArgumentException("`%s` constructor requires a `%s` or `OneSpecOf(%s)`".formatted(JsArraySpecParser.class.getName(),
                                                                                                         JsArraySpec.class.getName(),
                                                                                                         JsArraySpec.class.getName()
                                                                                                        ));
    }
    this.spec = spec;
    parser = spec.parser();

  }

  /**
   * Creates a JSON array parser based on the provided JSON array specification (spec). The parser will validate that
   * every element in a JSON array adheres to the schema defined in the given specification.
   *
   * @param spec The JSON array specification that defines the expected schema for each element in the array.
   * @return a Json array parser
   */
  public static JsArraySpecParser of(final JsSpec spec) {
    return new JsArraySpecParser(spec);
  }

  private boolean isValid(JsSpec spec) {
    if (requireNonNull(spec) instanceof JsArraySpec) {
      return true;
    }
    if (spec instanceof OneOf oneOf) {
      return oneOf
          .specs
          .stream()
          .allMatch(this::isValid);
    }
    if (spec instanceof NamedSpec namedSpec) {
      return isValid(JsSpecCache.get(namedSpec.name));
    }
    return false;
  }

  /**
   * Parses an array of bytes representing JSON array data into a structured JSON array. The parsed JSON array must
   * conform to the schema defined in the associated JSON specification (spec). If the input bytes do not represent a
   * well-formed JSON array or if the parsed array does not adhere to the specified schema, a {@link JsParserException}
   * is thrown.
   *
   * @param bytes An array of bytes containing JSON array data.
   * @return The parsed JSON array if parsing is successful.
   * @throws JsParserException If parsing fails due to JSON syntax errors or specification violations.
   */
  public JsArray parse(final byte[] bytes) {

    JsArray arr = JsIO.INSTANCE.parseToJsArray(requireNonNull(bytes),
                                               parser
                                              );

    assert spec.test(arr)
               .isEmpty();

    return arr;
  }


  /**
   * Parses a JSON array string into a structured JSON array. The parsed JSON array must conform to the schema defined
   * in the associated JSON specification (spec). If the input string does not represent a well-formed JSON array or if
   * the parsed array does not adhere to the specified schema, a {@link JsParserException} is thrown.
   *
   * @param str A string containing JSON array data.
   * @return The parsed JSON array if parsing is successful.
   * @throws JsParserException If parsing fails due to JSON syntax errors or specification violations.
   */
  public JsArray parse(String str) {
    JsArray arr = JsIO.INSTANCE
        .parseToJsArray(requireNonNull(str).getBytes(StandardCharsets.UTF_8),
                        parser
                       );

    assert spec.test(arr)
               .isEmpty();

    return arr;
  }

  /**
   * Parses JSON array data from an input stream into a structured JSON array. The parsed JSON array must conform to the
   * schema defined in the associated JSON specification (spec). If the input stream does not contain a well-formed JSON
   * array or if the parsed array does not adhere to the specified schema, a {@link JsParserException} is thrown. Any
   * I/O exceptions encountered while reading from the input stream are also captured and wrapped in the thrown
   * exception.
   *
   * @param inputstream An input stream containing JSON array data.
   * @return The parsed JSON array if parsing is successful.
   * @throws JsParserException If parsing fails due to JSON syntax errors, specification violations, or I/O exceptions.
   */
  public JsArray parse(InputStream inputstream) {
    JsArray arr = JsIO.INSTANCE.parseToJsArray(requireNonNull(inputstream),
                                               parser
                                              );

    assert spec.test(arr)
               .isEmpty();

    return arr;

  }


}

