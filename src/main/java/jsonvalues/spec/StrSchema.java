package jsonvalues.spec;

/**
 * A class representing the schema for string values in a JSON structure. It allows setting constraints such as minimum
 * and maximum length, a pattern to match, and a format.
 */
public final class StrSchema {

  private int minLength = 0;
  private int maxLength = Integer.MAX_VALUE;
  private String pattern;
  private String format;

  private StrSchema() {
  }

  public static StrSchema withLength(final int minLength,
                                     final int maxLength) {
    var schema = new StrSchema();
    schema.minLength = minLength;
    schema.maxLength = maxLength;
    return schema;
  }

  public static StrSchema withMinLength(final int minLength) {
    var schema = new StrSchema();
    schema.minLength = minLength;
    return schema;
  }

  public static StrSchema withMaxLength(final int maxLength) {
    var schema = new StrSchema();
    schema.maxLength = maxLength;
    return schema;
  }

  public static StrSchema withPattern(final String pattern) {
    var schema = new StrSchema();
    schema.pattern = pattern;
    return schema;
  }


  /**
   * Sets the minimum length for string values in the schema.
   *
   * @param minLength The minimum length (inclusive).
   * @return This StrSchema instance for method chaining.
   */
  public StrSchema setMinLength(final int minLength) {
    this.minLength = minLength;
    return this;
  }

  /**
   * Sets the maximum length for string values in the schema.
   *
   * @param maxLength The maximum length (inclusive).
   * @return This StrSchema instance for method chaining.
   */
  public StrSchema setMaxLength(final int maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  /**
   * Sets a pattern that the string values must match.
   *
   * @param pattern The regular expression pattern.
   * @return This StrSchema instance for method chaining.
   */
  public StrSchema setPattern(final String pattern) {
    this.pattern = pattern;
    return this;
  }

  /**
   * Sets a format for string values based on predefined formats.
   *
   * @param format The predefined format from the BUILT_INT_FORMAT enum.
   * @return This StrSchema instance for method chaining.
   */
  public StrSchema setFormat(final String format) {
    this.format = format;
    return this;
  }

  /**
   * Builds and returns an instance of StrConstraints based on the specified constraints.
   *
   * @return An instance of StrConstraints with the specified constraints.
   */
  StrConstraints build() {
    return new StrConstraints(minLength,
                              maxLength,
                              pattern,
                              format);
  }


}
