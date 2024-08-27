package jsonvalues;


import fun.optic.Prism;
import java.util.Optional;

/**
 * Represents an immutable JSON boolean. Only two instances are created: {@link #FALSE} and {@link #TRUE}
 */
public final class JsBool extends JsPrimitive {

  /**
   * The singleton false value.
   */
  public static final JsBool FALSE = new JsBool(false);
  /**
   * The singleton true value.
   */
  public static final JsBool TRUE = new JsBool(true);
  /**
   * prism between the sum type JsValue and JsBool
   */
  public static final Prism<JsValue, Boolean> prism =
      new Prism<>(s -> s.isBool() ?
                       Optional.of(s.toJsBool().value) :
                       Optional.empty(),
                  JsBool::of
      );
  /**
   * the boolean value.
   */
  public final boolean value;


  private JsBool(final boolean value) {
    this.value = value;
  }

  /**
   * Static factory method to create a JsBool from a boolean primitive type.
   *
   * @param b the boolean value
   * @return either {@link JsBool#TRUE} or {@link JsBool#FALSE}
   */
  public static JsBool of(boolean b) {
    return b ?
           TRUE :
           FALSE;
  }

  @Override
  public JsPrimitive toJsPrimitive() {
    return this;
  }

  @Override
  public boolean isBool() {
    return true;
  }

  @Override
  public boolean isTrue() {
    return value;
  }

  @Override
  public boolean isFalse() {
    return !value;
  }

  /**
   * Returns the hashcode of this JSON boolean.
   *
   * @return 1 if true, 0 if false
   */
  @Override
  public int hashCode() {
    return value ?
           1 :
           0;
  }

  /**
   * Indicates whether some other object is "equal to" this JSON boolean.
   *
   * @param that the reference object with which to compare.
   * @return true if <code>that</code> is a JsBool with the same value as <code>this</code> JsBool
   */
  @Override
  public boolean equals(final Object that) {
    if (this == that) {
      return true;
    }
    if (that == null || getClass() != that.getClass()) {
      return false;
    }
    JsBool thatBool = (JsBool) that;
    return value == thatBool.value;
  }

  /**
   * @return "true" or "false"
   */
  @Override
  public String toString() {
    return String.valueOf(value);
  }


}
