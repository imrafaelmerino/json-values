package jsonvalues.spec;

import java.util.regex.Pattern;

final class StrConstraints {

  final int minLength;
  final int maxLength;
  final Pattern pattern;

  final String format;


  StrConstraints(final int minLength,
                 final int maxLength,
                 final String pattern,
                 final String format) {
    if (minLength < 0) {
      throw new IllegalArgumentException("minLength must be >= 0");
    }
    if (maxLength < 0) {
      throw new IllegalArgumentException("maxLength must be >= 0");
    }
    if (minLength > maxLength) {
      throw new IllegalArgumentException("minLength must be <= maxLength");
    }
    this.minLength = minLength;
    this.maxLength = maxLength;
    this.pattern = pattern != null ? Pattern.compile(pattern) : null;
    this.format = format;
  }


}
