package jsonvalues.spec;

public final class StrSchema {

  public enum BUILT_INT_FORMAT {
    DATE_TIME,
    DATE,
    BASE64,
    TIME,
    EMAIL,
    HOSTNAME,
    IPV4,
    IPV6,
    URI,
    URI_REFERENCE,
    URI_TEMPLATE,
    JSON_POINTER,
    RELATIVE_JSON_POINTER,
    REGEX,
    UUID
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