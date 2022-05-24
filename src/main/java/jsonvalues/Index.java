package jsonvalues;


import static java.util.Objects.requireNonNull;

/**
 * represents the index of a value in a json array.
 */
public final class Index implements Position {

    /**
     * The index number.
     */
    public final int n;

    private Index(final int n) {
        this.n = n;
    }

    /**
     * Returns a new instance witch represents the given index in an array.
     *
     * @param index the given position
     * @return an Index object
     * @throws IndexOutOfBoundsException if the index is less than -1
     */
    public static Index of(final int index) {
        if (index < -1)
            throw new IndexOutOfBoundsException(String.format("%s is not between [0,U+221E)",
                                                              index));
        return new Index(index);
    }

    /**
     * Compares this index with another given position. If the given position is an index, both are
     * compared numerically, if it's a key, both are compared lexicographically.
     *
     * @param o the given position
     * @return 0 if they are equal, +1 if this is greater, -1 otherwise
     */
    @Override
    public int compareTo(final Position o) {
        return requireNonNull(o)
                       .isIndex() ?
               Integer.compare(n,
                               o.asIndex().n
               ) :
               toString().compareTo(o.asKey().name);
    }

    /**
     * throws an UserError exception.
     *
     * @throws UserError an Index can't be cast into a Key
     */
    @Override
    public Key asKey() {
        throw UserError.asKeyOfIndex();
    }

    /**
     * Returns this index.
     *
     * @return this object
     */
    @Override
    public Index asIndex() {
        return this;
    }

    /**
     * Returns true.
     *
     * @return true
     */
    @Override
    public boolean isIndex() {
        return true;
    }

    /**
     * Returns false.
     *
     * @return false
     */
    @Override
    public boolean isKey() {
        return false;
    }

    /**
     * Returns the hashcode of this index.
     *
     * @return the index value
     */
    @Override
    public int hashCode() {
        return n;
    }

    /**
     * Returns true if that is an index and both have the same value.
     *
     * @param that other object
     * @return true if both objects are indexes representing the same position
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null || getClass() != that.getClass()) return false;
        if (this == that) return true;
        final Index thatObj = (Index) that;
        return n == thatObj.n;
    }

    /**
     * Returns the value of the index as a string.
     *
     * @return the value of the index as a string
     */
    @Override
    public String toString() {
        return String.valueOf(n);
    }
}
