package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 * Represents the index of a JsElem in a JsArray.
 */
public final class Index implements Position
{


    /**
     * The index number.
     */
    public final int n;

    private Index(final int n)
    {
        this.n = n;
    }

    /**
     * Returns a new instance witch represents the given index in an array. The special index -1
     * points to the last element of an array.
     *
     * @param index the given position
     * @return an Index object
     * @throws IndexOutOfBoundsException if the index is less than -1
     */
    public static Index of(final int index)
    {
        if (index < -1) throw new IndexOutOfBoundsException(String.format("%s is not between [-1,U+221E)",
                                                                          index
                                                                         ));
        return new Index(index);
    }


    /**
     Compares this index with another given position. If the given position is an index, both are
     compared numerically, if it's a key, both are compared lexicographically.
     @param o the given position
     @return 0 if they are equal, +1 if this is greater, -1 otherwise
     */
    @Override
    public int compareTo(final Position o)
    {
        if (Objects.requireNonNull(o)
                   .isIndex()) return Integer.compare(n,o.asIndex().n);
        return toString().compareTo(o.asKey().name);
    }


    /**

     Returns true.
     @return true
     */
    @Override
    public boolean isIndex()
    {
        return true;
    }

    /**
     Returns false.
     @return false
     */
    @Override
    public boolean isKey()
    {
        return false;
    }


    /**
     Throws an UnsupportedOperationException.
     * @throws UnsupportedOperationException an Index can't be casted into an Key
     */
    @Override
    public Key asKey()
    {
        throw new UnsupportedOperationException("asKey of index");
    }

    /**
     Returns this index.
     * @return this object
     */
    @Override
    public Index asIndex()
    {
        return this;
    }


    /**
     * Returns the value of the index as a string.
     * @return the value of the index as a string
     */
    @Override
    public String toString()
    {
        return String.valueOf(n);
    }


    /**
     Returns true if that is an index and both have the same value.
     * @param that other object
     * @return true if both objects are indexes representing the same position
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        if (that == null || getClass() != that.getClass()) return false;
        if (this == that) return true;
        final Index thatObj = (Index) that;
        return n == thatObj.n;
    }

    /**
     Returns the hashcode of this index.
     * @return the index value
     */
    @Override
    public int hashCode()
    {
        return n;
    }
}
