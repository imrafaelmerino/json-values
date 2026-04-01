package jsonvalues.spec;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

/**
 * Contract for JSON specifications used to parse and validate JSON values.
 * <p>
 * A {@code JsSpec} can:
 * <ul>
 *   <li>Expose a parser for streaming/byte-based validation ({@link #parser()}).</li>
 *   <li>Parse a JSON string and return a validated {@link JsValue} ({@link #parse(String)}).</li>
 *   <li>Validate an in-memory {@link JsValue} and return semantic errors ({@link #test(JsPath, JsValue)}).</li>
 * </ul>
 * Implementations are expected to be immutable and safe for concurrent use.
 *
 * @see JsValue
 * @see JsParser
 * @see SpecError
 * @see JsPath
 * @see DslJsReader
 */
public sealed interface JsSpec permits JsArraySpec, JsMapOfBigInt, JsMapOfBinary, JsMapOfBool, JsMapOfDec,
                                       JsMapOfDouble, JsMapOfInstant, JsMapOfInt, JsMapOfLong, JsMapOfSpec, JsMapOfStr,
                                       JsObjSpec, JsOneErrorSpec, NamedSpec, OneOf {

  /**
   * Returns a variant of this spec that accepts {@code null} values.
   *
   * @return a nullable variant of this spec.
   */
  JsSpec nullable();

  /**
   * Returns the low-level parser backing this specification.
   *
   * @return parser used to validate and decode JSON input according to this spec.
   */
  JsParser parser();

  /**
   * Parses a JSON string and validates it against this spec.
   *
   * @param json JSON payload as text.
   * @return validated JSON value.
   * @throws NullPointerException if {@code json} is {@code null}.
   * @throws JsParserException if JSON is malformed or does not satisfy this spec.
   */
  default JsValue parse(final String json) throws JsParserException {
    var reader = JsIO.INSTANCE.createReader(Objects.requireNonNull(json)
                                                   .getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    return parser().parse(reader);
  }

  /**
   * Validates a JSON value against this spec using the provided parent path.
   *
   * @param parentPath logical location of {@code value} in a larger JSON structure.
   * @param value JSON value to validate.
   * @return list of validation errors; empty when value conforms.
   */
  List<SpecError> test(final JsPath parentPath,
                       final JsValue value);

  /**
   * Validates a JSON value against this spec from the root path.
   *
   * @param value JSON value to validate.
   * @return list of validation errors; empty when value conforms.
   */
  default List<SpecError> test(final JsValue value) {
    return test(JsPath.empty(),
                value);
  }


  boolean isNullable();

}

