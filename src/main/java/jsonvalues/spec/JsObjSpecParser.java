package jsonvalues.spec;

import static java.util.Objects.requireNonNull;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import jsonvalues.JsObj;

/**
 * Parser facade for object-oriented specs.
 * <p>
 * This parser accepts only specs that resolve to {@link JsObjSpec}, including {@link OneOf} and
 * {@link NamedSpec} compositions.
 * <p>
 * Instances are immutable and can be reused across threads.
 */


public final class JsObjSpecParser {

  private final JsParser parser;

  private final JsSpec spec;


  private JsObjSpecParser(final JsSpec spec) {
    if (!isValid(requireNonNull(spec))) {
      throw new IllegalArgumentException("`%s` constructor requires a `%s` or `OneSpecOf(%s)`".formatted(JsObjSpecParser.class.getName(),
                                                                                                         JsObjSpec.class.getName(),
                                                                                                         JsObjSpec.class.getName()
                                                                                                        ));
    }
    this.spec = spec;
    parser = spec.parser();
  }

  /**
   * Creates an object parser from a compatible spec.
   *
   * @param spec object spec, a named object spec, or a one-of composition of object specs.
   * @return object parser.
   * @throws NullPointerException if {@code spec} is {@code null}.
   * @throws IllegalArgumentException if {@code spec} cannot resolve to object specs.
   */
  public static JsObjSpecParser of(final JsSpec spec) {
    return new JsObjSpecParser(spec);
  }

  private boolean isValid(JsSpec spec) {
    if (spec instanceof JsObjSpec) {
      return true;
    }
    if (spec instanceof OneOf oneOf) {
      return oneOf.specs
          .stream()
          .allMatch(this::isValid);
    }
    if (spec instanceof NamedSpec namedSpec) {
      return isValid(JsSpecCache.get(namedSpec.name));
    }
    return false;


  }

  /**
   * Parses and validates a JSON object from UTF-8 bytes.
   *
   * @param bytes JSON payload.
   * @return validated object.
   * @throws NullPointerException if the provided byte array is null.
   * @throws JsParserException if input is malformed or fails spec validation.
   */
  public JsObj parse(final byte[] bytes) {
    JsObj obj = JsIO.INSTANCE.parseToJsObj(requireNonNull(bytes),
                                           parser
                                          );

    assert spec.test(obj)
               .isEmpty();

    return obj;

  }


  /**
   * Parses and validates a JSON object from a string.
   *
   * @param str JSON payload.
   * @return validated object.
   * @throws NullPointerException if the provided string is null.
   * @throws JsParserException if input is malformed or fails spec validation.
   */
  public JsObj parse(final String str) {

    JsObj obj = JsIO.INSTANCE.parseToJsObj(requireNonNull(str).getBytes(StandardCharsets.UTF_8),
                                           parser
                                          );

    assert spec.test(obj)
               .isEmpty();
    return obj;
  }

  /**
   * Parses and validates a JSON object from an input stream.
   *
   * @param inputStream stream containing JSON payload.
   * @return validated object.
   * @throws NullPointerException if the provided input stream is null.
   * @throws JsParserException if reading/parsing fails or input does not satisfy the spec.
   */
  public JsObj parse(final InputStream inputStream) {
    JsObj obj = JsIO.INSTANCE.parseToJsObj(requireNonNull(inputStream),
                                           parser
                                          );
    assert spec.test(obj)
               .isEmpty();

    return obj;
  }


}
