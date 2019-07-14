package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 Immutable pair which represents a JsElem of a Json and its JsPath location: (path, element).
 */
public final class JsPair
{

    /**
     the json element.
     */
    public final JsElem elem;


    /**
     the location of the element.
     */
    public final JsPath path;


    private JsPair(final JsPath path,
                   final JsElem elem
                  )
    {
        this.path = path;
        this.elem = elem;
    }


    /**

     Returns a json pair from the path-like string and the json element.
     @param path the path-like string
     @param elem the JsElem
     @return an immutable JsPair
     */
    public static JsPair of(String path,
                            JsElem elem
                           )
    {
        return new JsPair(JsPath.of(Objects.requireNonNull(path)),
                          Objects.requireNonNull(elem)
        );
    }

    /**
     Returns a json pair from the path-like string and the integer.
     @param path the path-like string
     @param i the integer
     @return an immutable JsPair
     */
    public static JsPair of(String path,
                            int i
                           )
    {
        return new JsPair(JsPath.of(Objects.requireNonNull(path)),
                          JsInt.of(i)
        );
    }
    /**
     Returns a json pair from the path-like string and the double.
     @param path the path-like string
     @param d the double
     @return an immutable JsPair
     */
    public static JsPair of(String path,
                            double d
                           )
    {
        return new JsPair(JsPath.of(Objects.requireNonNull(path)),
                          JsDouble.of(d)
        );
    }
    /**
     Returns a json pair from the path-like string and the long.
     @param path the path-like string
     @param l the long
     @return an immutable JsPair
     */
    public static JsPair of(String path,
                            long l
                           )
    {
        return new JsPair(JsPath.of(Objects.requireNonNull(path)),
                          JsLong.of(l)
        );
    }
    /**
     Returns a json pair from the path-like string and the boolean.
     @param path the path-like string
     @param b the boolean
     @return an immutable JsPair
     */
    public static JsPair of(String path,
                            boolean b
                           )
    {
        return new JsPair(JsPath.of(Objects.requireNonNull(path)),
                          JsBool.of(b)
        );
    }
    /**
     Returns a json pair from the path-like string and the string.
     @param path the path-like string
     @param s the string
     @return an immutable JsPair
     */
    public static JsPair of(String path,
                            String s
                           )
    {
        return new JsPair(JsPath.of(Objects.requireNonNull(path)),
                          JsStr.of(Objects.requireNonNull(s))
        );
    }
    /**
     Returns a json pair from the path-like string and the big decimal.
     @param path the path-like string
     @param bd the big decimal
     @return an immutable JsPair
     */
    public static JsPair of(String path,
                            BigDecimal bd
                           )
    {
        return new JsPair(JsPath.of(Objects.requireNonNull(path)),
                          JsBigDec.of(Objects.requireNonNull(bd))
        );
    }
    /**
     Returns a json pair from the path-like string and the big integer.
     @param path the path-like string
     @param bi the big integer
     @return an immutable JsPair
     */
    public static JsPair of(String path,
                            BigInteger bi
                           )
    {
        return new JsPair(JsPath.of(Objects.requireNonNull(path)),
                          JsBigInt.of(Objects.requireNonNull(bi))
        );
    }

    /**
     Returns a json pair from the path and the json element.
     @param path the JsPath object
     @param elem the JsElem
     @return an immutable JsPair
     */
    public static JsPair of(JsPath path,
                            JsElem elem
                           )
    {
        return new JsPair(Objects.requireNonNull(path),
                          Objects.requireNonNull(elem)
        );
    }

    /**
     Returns a json pair from the path and the integer.
     @param path the JsPath
     @param i the integer
     @return an immutable JsPair
     */
    public static JsPair of(JsPath path,
                            int i
                           )
    {
        return new JsPair(Objects.requireNonNull(path),
                          JsInt.of(i)
        );
    }
    /**
     Returns a json pair from the path and the double.
     @param path the JsPath
     @param d the double
     @return an immutable JsPair
     */
    public static JsPair of(JsPath path,
                            double d
                           )
    {
        return new JsPair(Objects.requireNonNull(path),
                          JsDouble.of(d)
        );
    }
    /**
     Returns a json pair from the path and the long.
     @param path the JsPath
     @param l the long
     @return an immutable JsPair
     */
    public static JsPair of(JsPath path,
                            long l
                           )
    {
        return new JsPair(Objects.requireNonNull(path),
                          JsLong.of(l)
        );
    }
    /**
     Returns a json pair from the path and the boolean.
     @param path the JsPath
     @param b the boolean
     @return an immutable JsPair
     */
    public static JsPair of(JsPath path,
                            boolean b
                           )
    {
        return new JsPair(Objects.requireNonNull(path),
                          JsBool.of(b)
        );
    }
    /**
     Returns a json pair from the path and the string.
     @param path the JsPath
     @param s the string
     @return an immutable JsPair
     */
    public static JsPair of(JsPath path,
                            String s
                           )
    {
        return new JsPair(Objects.requireNonNull(path),
                          JsStr.of(Objects.requireNonNull(s))
        );
    }
    /**
     Returns a json pair from the path and the big decimal.
     @param path the JsPath
     @param bd the big decimal
     @return an immutable JsPair
     */
    public static JsPair of(JsPath path,
                            BigDecimal bd
                           )
    {
        return new JsPair(Objects.requireNonNull(path),
                          JsBigDec.of(Objects.requireNonNull(bd))
        );
    }
    /**
     Returns a json pair from the path and the big integer.
     @param path the JsPath
     @param bi the big integer
     @return an immutable JsPair
     */
    public static JsPair of(JsPath path,
                            BigInteger bi
                           )
    {
        return new JsPair(Objects.requireNonNull(path),
                          JsBigInt.of(Objects.requireNonNull(bi))
        );
    }


    /**

     @return string representation of this pair: (path, elem)
     */
    @Override
    public String toString()
    {
        return String.format("(%s, %s)",
                             path,
                             elem
                            );
    }

    /**
     Returns true if that is a pair and both represents the same element at the same location.
     @param that the reference object with which to compare.
     @return true if this.element.equals(that.element) and this.path.equals(that.path)
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        final JsPair thatPair = (JsPair) that;
        return Objects.equals(elem,
                              thatPair.elem
                             ) &&
        Objects.equals(path,
                       thatPair.path
                      );
    }

    /**
     Returns the hashcode of this pair.
     @return the hashcode of this pair
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elem,
                            path
                           );
    }


    /**
     Returns a new pair with the same path and a new element result of applying the mapping function
     @param map the mapping function which maps the JsElem
     @return a new JsPair
     */
    public JsPair mapElem(UnaryOperator<JsElem> map)
    {
        return JsPair.of(this.path,
                         map.apply(this.elem)
                        );
    }

    /**
     Returns a new pair with the same element and a new path result of applying the mapping function
     @param map the mapping function which maps the JsPath
     @return a new JsPair
     */
    public JsPair mapPath(UnaryOperator<JsPath> map)
    {
        return JsPair.of(map.apply(this.path),
                         this.elem
                        );
    }


}
