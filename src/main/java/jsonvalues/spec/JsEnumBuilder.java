package jsonvalues.spec;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.NameValidationSpecConstants.AVRO_NAMESPACE_PATTERN;
import static jsonvalues.spec.NameValidationSpecConstants.AVRO_NAME_PATTERN;
import static jsonvalues.spec.NameValidationSpecConstants.isValidName;
import static jsonvalues.spec.NameValidationSpecConstants.isValidNamespace;

import java.util.Arrays;
import java.util.List;
import jsonvalues.JsArray;
import jsonvalues.JsStr;

/**
 * Builder class for creating instances of {@link JsEnum}, which represents an enumeration of string symbols. Enums
 * define a fixed set of allowed values, and each value is associated with a unique symbol. Enums are useful for
 * specifying fields with a predefined set of options.
 *
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * var enumSpec = JsEnumBuilder.withName("Color")
 *                             .withAliases(List.of("Colour")) // Optional: Set aliases for the enum
 *                             .withNamespace("common")
 *                             .withDoc("Represents a color") // Optional: Set documentation for the enum
 *                             .withDefaultSymbol("RED") // Optional: Set a default symbol
 *                             .build("RED", "GREEN", "BLUE"); // Specify the allowed symbols
 * }
 * </pre>
 *
 * <p>
 * Note: If you are not creating specs specifically for Avro schemas, consider using the method
 * {@link JsSpecs#oneStringOf(String, String...)} to create string specs instead.
 */
public final class JsEnumBuilder {

  private final String name;
  private String doc;

  private String defaultSymbol;
  private String nameSpace;
  private List<String> aliases;

  private JsEnumBuilder(final String name) {
    if (!isValidName.test(name)) {
      throw new IllegalArgumentException("The name `%s` doesn't follow the pattern %s ".formatted(name,
                                                                                                  AVRO_NAME_PATTERN));
    }

    this.name = name;
  }

  /**
   * Sets the name of the JSON object specification. The name must follow the Avro naming conventions, adhering to the
   * regex pattern: {@code [A-Za-z_][A-Za-z0-9_]*}.
   *
   * @param name The name of the enum.
   * @return A new {@link JsEnumBuilder} instance.
   * @throws IllegalArgumentException If the provided name does not follow the specified pattern.
   */
  public static JsEnumBuilder withName(final String name) {
    return new JsEnumBuilder(requireNonNull(name));
  }


  /**
   * Sets aliases for the enum. Must follow the avro naming conventions and, adhering to the regex pattern:
   * {@code [A-Za-z_][A-Za-z0-9_.]+}
   * <p>
   * Aliases provide alternative names for the enum, and they can be used interchangeably when referring to the same
   * specification.
   *
   * @param aliases A list of alternative names (aliases) for the enum.
   * @return This JsEnumBuilder for method chaining.
   * @throws IllegalArgumentException If any of the provided aliases does not follow the naming pattern.
   */
  public JsEnumBuilder withAliases(List<String> aliases) {
    this.aliases = requireNonNull(aliases);
    for (String alias : aliases) {
      if (!isValidNamespace.test(alias)) {
        throw new IllegalArgumentException(("The alias `%s` of the Enum with name `%s` doesn't follow " +
                                            "the pattern %s"
                                           ).formatted(alias,
                                                       name,
                                                       AVRO_NAME_PATTERN));
      }

    }
    return this;
  }

  /**
   * Sets the namespace of the JSON object specification. The namespace must follow the Avro naming conventions,
   * adhering to the regex pattern: {@code [A-Za-z_][A-Za-z0-9_.]+}. It should start with a letter or an underscore,
   * followed by letters, numbers, underscores, or dots.
   *
   * @param nameSpace The namespace of the enum specification.
   * @return A reference to this {@code JsEnumBuilder} instance for method chaining.
   * @throws IllegalArgumentException If the provided namespace does not follow the Avro naming conventions.
   */
  public JsEnumBuilder withNamespace(final String nameSpace) {
    this.nameSpace = requireNonNull(nameSpace);
    if (!isValidNamespace.test(nameSpace)) {
      throw new IllegalArgumentException(("The namespace `%s` of the Enum with name `%s` doesn't follow the " +
                                          "pattern `%s`"
                                         ).formatted(nameSpace,
                                                     name,
                                                     AVRO_NAMESPACE_PATTERN));
    }
    return this;
  }

  /**
   * Sets the documentation for the enum.
   *
   * @param doc The documentation for the enum.
   * @return This JsEnumBuilder for method chaining.
   */
  public JsEnumBuilder withDoc(final String doc) {
    this.doc = requireNonNull(doc);
    return this;
  }

  /**
   * Sets the default symbol for the enum. The default symbol is the value used if no specific value is provided.
   *
   * @param symbol The default symbol for the enum.
   * @return This {@link JsEnumBuilder} instance for method chaining.
   */
  public JsEnumBuilder withDefaultSymbol(final String symbol) {
    this.defaultSymbol = requireNonNull(symbol);
    return this;
  }

  /**
   * Builds and returns a {@link JsEnum} specification with the specified symbols. The symbols represent the allowed
   * values for the enum.
   *
   * @param symbols The symbols allowed in the enum.
   * @return The constructed {@link JsEnum} specification.
   * @throws IllegalArgumentException If the default symbol is specified and is not contained in the list of symbols.
   */
  public JsSpec build(final String... symbols) {
    return build(Arrays.stream(requireNonNull(symbols))
                       .toList());
  }

  /**
   * Builds and returns a {@link JsEnum} specification with the specified symbols. The symbols represent the allowed
   * values for the enum.
   *
   * @param symbols The symbols allowed in the enum.
   * @return The constructed {@link JsEnum} specification.
   * @throws IllegalArgumentException If the default symbol is specified and is not contained in the list of symbols.
   */
  public JsSpec build(final List<String> symbols) {
    return build(JsArray.ofStrs(symbols));

  }

  /**
   * Builds and returns a {@link JsEnum} specification with the specified symbols. The symbols represent the allowed
   * values for the enum.
   *
   * @param symbols The symbols allowed in the enum.
   * @return The constructed {@link JsEnum} specification.
   * @throws IllegalArgumentException If the default symbol is specified and is not contained in the list of symbols.
   */
  public JsSpec build(final JsArray symbols) {
    if (!JsSpecs.arrayOfStr()
                .test(symbols)
                .isEmpty()) {
      throw new IllegalArgumentException("The list of symbols must be an array of strings");
    }
    if (defaultSymbol != null && !symbols.containsValue(JsStr.of(defaultSymbol))) {
      throw new IllegalArgumentException(("Default symbol `%s` must be contained in the list of possible " +
                                          "symbols of the enum.").formatted(defaultSymbol));
    }
    var metadata = new EnumMetaData(name,
                                    nameSpace,
                                    aliases,
                                    doc,
                                    defaultSymbol);
    var enumSpec = new JsEnum(false,
                              symbols,
                              metadata);
    JsSpecCache.putAll(metadata.getFullName(),
                       aliases,
                       enumSpec);
    return enumSpec;

  }
}