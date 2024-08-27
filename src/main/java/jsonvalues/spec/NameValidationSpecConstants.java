package jsonvalues.spec;

import java.util.function.Predicate;
import java.util.regex.Pattern;

class NameValidationSpecConstants {

  static final String NAMESPACE_REGEX = "[A-Za-z_][A-Za-z0-9_.]*";
  static final String NAME_REGEX = "[A-Za-z_][A-Za-z0-9_]*";
  static final Pattern AVRO_NAME_PATTERN = Pattern.compile(NAME_REGEX);
  static final Predicate<String> isValidName = name -> AVRO_NAME_PATTERN.matcher(name)
                                                                        .matches();
  static final Pattern AVRO_NAMESPACE_PATTERN = Pattern.compile(NAMESPACE_REGEX);
  static final Predicate<String> isValidNamespace = name -> AVRO_NAMESPACE_PATTERN.matcher(name)
                                                                                  .matches();

  private NameValidationSpecConstants() {
  }
}
