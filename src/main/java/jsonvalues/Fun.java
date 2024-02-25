package jsonvalues;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Base64;

class Fun {

  private Fun() {
  }

  static Long getLong(final JsValue value) {
    if (value.isLong()) {
      return value.toJsLong().value;
    }
    if (value.isInt()) {
      return (long) value.toJsInt().value;
    }
    if (value.isBigInt()) {
      return value.toJsBigInt()
                  .longValueExact()
                  .orElse(null);

    }
    return null;
  }

  static Integer getInt(final JsValue value) {
    if (value.isInt()) {
      return value.toJsInt().value;
    }
    if (value.isLong()) {
      try {
        return Math.toIntExact(value.toJsLong().value);
      } catch (ArithmeticException e) {
        return null;
      }
    }
    if (value.isBigInt()) {
      return value.toJsBigInt()
                  .intValueExact()
                  .orElse(null);
    }
    return null;
  }

  static Double getDouble(JsValue value) {
    if (value.isDouble()) {
      return value.toJsDouble().value;
    }
    if (value.isDecimal()) {
      return value.toJsBigDec()
                  .doubleValueExact()
                  .orElse(null);
    }
    return null;
  }

  static Instant getInstant(JsValue value) {
    if (value.isInstant()) {
      return value.toJsInstant().value;
    }
    if (value.isStr()) {
      try {
        return Instant.parse(value.toJsStr().value);
      } catch (DateTimeParseException e) {
        return null;
      }
    }
    return null;
  }

  static BigInteger getBigInt(JsValue value) {
    if (value.isLong()) {
      return BigInteger.valueOf(value.toJsLong().value);
    }
    if (value.isInt()) {
      return BigInteger.valueOf(value.toJsInt().value);
    }
    if (value.isBigInt()) {
      return value.toJsBigInt().value;
    }
    return null;
  }

  static byte[] getBytes(JsValue value) {
    if (value.isBinary()) {
      return value.toJsBinary().value;
    }
    if (value.isStr()) {
      try {
        return Base64.getDecoder()
                     .decode(value.toJsStr().value);
      } catch (IllegalArgumentException e) {
        return null;
      }

    }
    return null;
  }

  static BigDecimal getBigDec(JsValue value) {
    if (value.isDouble()) {
      return BigDecimal.valueOf(value.toJsDouble().value);
    }
    if (value.isDecimal()) {
      return value.toJsBigDec().value;
    }
    return null;
  }

}
