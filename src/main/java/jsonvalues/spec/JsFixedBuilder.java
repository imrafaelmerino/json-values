package jsonvalues.spec;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.NameValidationSpecConstants.AVRO_NAMESPACE_PATTERN;
import static jsonvalues.spec.NameValidationSpecConstants.AVRO_NAME_PATTERN;
import static jsonvalues.spec.NameValidationSpecConstants.isValidName;
import static jsonvalues.spec.NameValidationSpecConstants.isValidNamespace;

import java.util.List;

/**
 * Builder class for creating instances of {@link JsFixedBinary}, which represents a fixed-size binary type. Fixed
 * binary types have a fixed size specified at the time of creation and are used in Avro schemas to represent fixed-size
 * binary data.
 * <p>
 * Note: This builder is specifically designed for creating Avro schemas. If you are not working with Avro schemas and
 * just need a fixed-size binary specification, consider using {@link JsSpecs#fixedBinary(int)}.
 */
public final class JsFixedBuilder {

  private final String name;

  private String nameSpace;
  private List<String> aliases;
  private String doc;

  private JsFixedBuilder(String name) {
    //validate name
    this.name = name;
  }

  /**
   * Sets the name of the fixed binary specification. The name must follow the Avro naming conventions, adhering to the
   * regex pattern: {@code [A-Za-z_][A-Za-z0-9_]*}.
   *
   * @param name The name of the enum.
   * @return A new {@link JsFixedBuilder} instance.
   * @throws IllegalArgumentException If the provided name does not follow the specified pattern.
   */
  public static JsFixedBuilder withName(String name) {
    if (!isValidName.test(requireNonNull(name))) {
      throw new IllegalArgumentException(("The name `%s` of the Fixed binary doesn't follow the " +
                                          "pattern `%s`").formatted(name,
                                                                    AVRO_NAME_PATTERN));
    }
    return new JsFixedBuilder(name.formatted());
  }

  /**
   * Sets the namespace of the fixed binary specification. The namespace must follow the Avro naming conventions,
   * adhering to the regex pattern: {@code [A-Za-z_][A-Za-z0-9_.]+}. It should start with a letter or an underscore,
   * followed by letters, numbers, underscores, or dots.
   *
   * @param nameSpace The namespace of the enum specification.
   * @return A reference to this {@code JsFixedBuilder} instance for method chaining.
   * @throws IllegalArgumentException If the provided namespace does not follow the Avro naming conventions.
   */
  public JsFixedBuilder withNamespace(String nameSpace) {
    this.nameSpace = requireNonNull(nameSpace);
    if (!isValidNamespace.test(nameSpace)) {
      throw new IllegalArgumentException(("The namespace `%s` of the Fixed binary with name `%s` doesn't follow " +
                                          "the pattern `%s`").formatted(nameSpace,
                                                                        name,
                                                                        AVRO_NAMESPACE_PATTERN));
    }

    return this;
  }

  /**
   * Sets the documentation for the fixed binary.
   *
   * @param doc The documentation fixed binary.
   * @return This JsFixedBuilder for method chaining.
   */
  public JsFixedBuilder withDoc(final String doc) {
    this.doc = requireNonNull(doc);
    return this;
  }

  /**
   * Sets aliases for the fixed binary. Must follow the avro naming conventions and, adhering to the regex pattern:
   * {@code [A-Za-z_][A-Za-z0-9_.]+}
   * <p>
   * Aliases provide alternative names for the fixed binary, and they can be used interchangeably when referring to the
   * same specification.
   *
   * @param aliases A list of alternative names (aliases) for the fixed binary.
   * @return This JsFixedBuilder for method chaining.
   * @throws IllegalArgumentException If any of the provided aliases does not follow the naming pattern.
   */
  public JsFixedBuilder withAliases(List<String> aliases) {
    this.aliases = requireNonNull(aliases);
    for (String alias : aliases) {
      if (!isValidNamespace.test(alias)) {
        throw new IllegalArgumentException(("The alias `%s` of the Fixed binary with name `%s` doesn't follow " +
                                            "the pattern `%s`").formatted(alias,
                                                                          name,
                                                                          AVRO_NAME_PATTERN));
      }

    }
    return this;
  }

  /**
   * Builds and returns a {@link JsFixedBinary} specification with the specified size.
   *
   * @param size The size of the fixed binary.
   * @return The constructed {@link JsFixedBinary} specification.
   * @throws IllegalArgumentException If the size is less than or equal to 0.
   */
  public JsSpec build(final int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("size < 0");
    }
    var metadata = new FixedMetaData(name,
                                     nameSpace,
                                     aliases,
                                     doc);
    var spec = new JsFixedBinary(false,
                                 size,
                                 metadata);
    JsSpecCache.putAll(metadata.getFullName(),
                       aliases,
                       spec);
    return spec;

  }
}