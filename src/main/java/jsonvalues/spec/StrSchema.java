package jsonvalues.spec;

public final class StrSchema {

  public enum BUILT_INT_FORMAT {
    DATE_TIME("date-time"),
    DATE("date"),
    BASE64("base64"),
    TIME("time"),
    EMAIL("email"),
    HOSTNAME("hostname"),
    IPV4("ipv4"),
    IPV6("ipv6"),
    URI("uri"),
    URI_REFERENCE("uri-reference"),
    URI_TEMPLATE("uri-template"),
    JSON_POINTER("json-pointer"),
    RELATIVE_JSON_POINTER("relative-json-pointer"),
    REGEX("regex"),
    UUID("uuid");

    final String format;

    BUILT_INT_FORMAT(final String format) {
      this.format = format;
    }
  }

  private int minLength = 0;
  private int maxLength = Integer.MAX_VALUE;
  private String pattern;
  private String format;

  public StrSchema setMinLength(final int minLength) {
    this.minLength = minLength;
    return this;
  }

  public StrSchema setMaxLength(final int maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  public StrSchema setPattern(final String pattern) {
    this.pattern = pattern;
    return this;
  }

  public StrSchema setFormat(final String format) {
    this.format = format;
    return this;
  }

  StrConstraints build() {
    return new StrConstraints(minLength,
                              maxLength,
                              pattern,
                              format);
  }
}