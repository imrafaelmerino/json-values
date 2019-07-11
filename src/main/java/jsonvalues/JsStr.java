package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable json string.
 */
public final class JsStr implements JsElem, Comparable<JsStr>
{


    /**
     The string value.
     */
    public final String x;

    JsStr(String x)
    {
        this.x = x;
    }

    /**
     Compares two {@code JsStr} objects lexicographically.
     @see String#compareTo(String)
     */
    @Override
    public int compareTo(final JsStr o)
    {
        return x.compareTo(requireNonNull(o).x);
    }


    /**
     Indicates whether some other object is "equal to" this json string.
     @param that the reference object with which to compare.
     @return true if <code>that</code> is a JsStr with the same value as <code>this</code> JsStr
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        final JsStr thatStr = (JsStr) that;
        return Objects.equals(x,
                              thatStr.x
                             );
    }

    /**
     Tests this JsStr on a predicate.
     @param predicate the predicate
     @return true if this string satisfies the predicate
     */
    public boolean test(Predicate<String> predicate)
    {
        return predicate.test(x);
    }


    /**
     Returns the hashcode of this json string.
     @return hashcode of this JsStr
     */
    @Override
    public int hashCode()
    {
        return x.hashCode();
    }

    /**
     Maps this JsStr into another one.
     @param fn the mapping function
     @return a new JsStr
     */
    public JsStr map(final UnaryOperator<String> fn)
    {
        return JsStr.of(requireNonNull(fn).apply(x));
    }

    /**
     * Static factory method to create a JsStr from a string.
     * @param str the string
     * @return a new JsStr
     */
    public static JsStr of(String str)
    {
        return new JsStr(str);
    }

    /**
     Returns the string representation parse this json string which is its value quoted.
     @return the value quoted.
     */
    @Override
    public String toString()
    {
        return "\"" + x + "\"";
    }


}
