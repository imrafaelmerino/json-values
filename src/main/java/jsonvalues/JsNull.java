package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 Represents null, which a valid json value. It's a singleton, only the instance JsNull.NULL exists.
 */
public final class JsNull implements JsElem
{
    /**
     * The singleton null value.
     */
    public static final JsNull NULL = new JsNull();


    private JsNull()
    {
    }


    /**
     Returns true if that is the singleton {@link JsNull#NULL}.
     @param that the reference object with which to compare.
     @return true if that is {@link JsNull#NULL}
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        return that == this;
    }

    /**
     Returns the hashcode of this json null
     @return 1
     */
    @Override
    public int hashCode()
    {
        return 1;
    }

    /**
     @return "null"
     */
    @Override
    public String toString()
    {
        return "null";
    }

    @Override
    public boolean isNull()
    {
        return true;
    }

}

