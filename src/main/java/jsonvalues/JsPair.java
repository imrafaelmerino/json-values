package jsonvalues;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 Immutable pair which represents a value and its location: (path, value).
 */
public final class JsPair {

    /**
     lens that focuses on the path of a pair
     */
    public static final Lens<JsPair, JsPath> pathLens = new JsPathPairLens();
    /**
     lens that focuses on the value of a pair
     */
    public static final Lens<JsPair, JsValue> valueLens = new JsValuePairLens();
    /**
     the json value.
     */
    public final JsValue value;
    /**
     the location of the value.
     */
    public final JsPath path;


    private JsPair(final JsPath path,
                   final JsValue value) {
        this.path = path;
        this.value = value;
    }


    /**
     Returns a json pair from the path and the json element.

     @param path the JsPath object
     @param elem the JsElem
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final JsValue elem
                           ) {
        return new JsPair(requireNonNull(requireNonNull(path)),
                          requireNonNull(requireNonNull(elem))
        );
    }

    /**
     Returns a json pair from the path and the integer.

     @param path the JsPath
     @param i    the integer
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final int i
                           ) {
        return new JsPair(requireNonNull(path),
                          JsInt.of(i)
        );
    }


    /**
     Returns a json pair from the key and the integer.

     @param key the key
     @param i   the integer
     @return an immutable JsPair
     */
    public static JsPair of(final String key,
                            final int i
                           ) {
        return new JsPair(requireNonNull(JsPath.fromKey(key)),
                          JsInt.of(i)
        );
    }

    /**
     Returns a json pair from the path and the double.

     @param path the JsPath
     @param d    the double
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final double d
                           ) {
        return new JsPair(requireNonNull(path),
                          JsDouble.of(d)
        );
    }

    /**
     Returns a json pair from the key and the double.

     @param key the key
     @param d   the double
     @return an immutable JsPair
     */
    public static JsPair of(final String key,
                            final double d
                           ) {
        return new JsPair(requireNonNull(JsPath.fromKey(key)),
                          JsDouble.of(d)
        );
    }

    /**
     Returns a json pair from the path and the long.

     @param path the JsPath
     @param l    the long
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final long l
                           ) {
        return new JsPair(requireNonNull(path),
                          JsLong.of(l)
        );
    }

    /**
     Returns a json pair from the key and the long.

     @param key the key
     @param l   the long
     @return an immutable JsPair
     */
    public static JsPair of(final String key,
                            final long l
                           ) {
        return new JsPair(requireNonNull(JsPath.fromKey(key)),
                          JsLong.of(l)
        );
    }

    /**
     Returns a json pair from the path and the boolean.

     @param path the JsPath
     @param b    the boolean
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final boolean b
                           ) {
        return new JsPair(requireNonNull(path),
                          JsBool.of(b)
        );
    }

    /**
     Returns a json pair from the key and the boolean.

     @param key the key
     @param b   the boolean
     @return an immutable JsPair
     */
    public static JsPair of(final String key,
                            final boolean b
                           ) {
        return new JsPair(requireNonNull(JsPath.fromKey(key)),
                          JsBool.of(b)
        );
    }

    /**
     Returns a json pair from the path and the string.

     @param path the JsPath
     @param s    the string
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final String s
                           ) {
        return new JsPair(requireNonNull(path),
                          JsStr.of(requireNonNull(s))
        );
    }

    /**
     Returns a json pair from the key and the string.

     @param key the JsPath
     @param s   the string
     @return an immutable JsPair
     */
    public static JsPair of(final String key,
                            final String s
                           ) {
        return new JsPair(requireNonNull(JsPath.fromKey(key)),
                          JsStr.of(requireNonNull(s))
        );
    }

    /**
     Returns a json pair from the path and the big decimal.

     @param path the JsPath
     @param bd   the big decimal
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final BigDecimal bd
                           ) {
        return new JsPair(requireNonNull(path),
                          JsBigDec.of(requireNonNull(bd))
        );
    }

    /**
     Returns a json pair from the key and the big decimal.

     @param key the key
     @param bd  the big decimal
     @return an immutable JsPair
     */
    public static JsPair of(final String key,
                            final BigDecimal bd
                           ) {
        return new JsPair(requireNonNull(JsPath.fromKey(key)),
                          JsBigDec.of(requireNonNull(bd))
        );
    }

    /**
     Returns a json pair from the path and the big integer.

     @param path the JsPath
     @param bi   the big integer
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final BigInteger bi
                           ) {
        return new JsPair(requireNonNull(path),
                          JsBigInt.of(requireNonNull(bi))
        );
    }

    /**
     Returns a json pair from the key and the big integer.

     @param key the key
     @param bi  the big integer
     @return an immutable JsPair
     */
    public static JsPair of(final String key,
                            final BigInteger bi
                           ) {
        return new JsPair(requireNonNull(JsPath.fromKey(key)),
                          JsBigInt.of(requireNonNull(bi))
        );
    }

    /**
     Returns the hashcode of this pair.

     @return the hashcode of this pair
     */
    @Override
    public int hashCode() {
        return Objects.hash(value,
                            path
                           );
    }

    /**
     Returns true if that is a pair and both represents the same element at the same location.

     @param that the reference object with which to compare.
     @return true if this.element.equals(that.element) and this.path.equals(that.path)
     */
    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        final JsPair thatPair = (JsPair) that;
        return Objects.equals(value,
                              thatPair.value
                             ) &&
                Objects.equals(path,
                               thatPair.path
                              );
    }

    /**
     @return string representation of this pair: (path, elem)
     */
    @Override
    public String toString() {
        return String.format("(%s, %s)",
                             path,
                             value
                            );
    }


}
