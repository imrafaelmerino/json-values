package jsonvalues.spec;

import static java.util.Objects.requireNonNull;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import jsonvalues.JsArray;

/**
 * Parser facade for array-oriented specs.
 * <p>
 * This parser accepts only specs that resolve to {@link JsArraySpec}, including {@link OneOf} and
 * {@link NamedSpec} compositions.
 * <p>
 * Instances are immutable and can be reused across threads.
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
   * Creates an array parser from a compatible spec.
   *
   * @param spec array spec, a named array spec, or a one-of composition of array specs.
   * @return array parser.
   * @throws NullPointerException if {@code spec} is {@code null}.
   * @throws IllegalArgumentException if {@code spec} cannot resolve to array specs.
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
   * Parses and validates a JSON array from UTF-8 bytes.
   *
   * @param bytes JSON payload.
   * @return validated array.
   * @throws NullPointerException if {@code bytes} is {@code null}.
   * @throws JsParserException if input is malformed or fails spec validation.
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
   * Parses and validates a JSON array from a string.
   *
   * @param str JSON payload.
   * @return validated array.
   * @throws NullPointerException if {@code str} is {@code null}.
   * @throws JsParserException if input is malformed or fails spec validation.
   */
  public JsArray parse(final String str) {
    JsArray arr = JsIO.INSTANCE
        .parseToJsArray(requireNonNull(str).getBytes(StandardCharsets.UTF_8),
                        parser
                       );

    assert spec.test(arr)
               .isEmpty();

    return arr;
  }

  /**
   * Parses and validates a JSON array from an input stream.
   *
   * @param inputStream stream containing JSON payload.
   * @return validated array.
   * @throws NullPointerException if {@code inputStream} is {@code null}.
   * @throws JsParserException if reading/parsing fails or input does not satisfy the spec.
   */
  public JsArray parse(final InputStream inputStream) {
    JsArray arr = JsIO.INSTANCE.parseToJsArray(requireNonNull(inputStream),
                                               parser
                                              );

    assert spec.test(arr)
               .isEmpty();

    return arr;

  }


}
