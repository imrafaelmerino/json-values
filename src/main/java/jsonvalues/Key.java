package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

/**
 Represents the Key of a JsElem in a JsObj.
 */
public final class Key implements Position
{

    /**
     * name of the key.
     */
    public final String name;

    private Key(final String name)
    {
        this.name = name;
    }

    /**
     * Returns a new instance which represents a key with the given name in a json object. Any string,
     * but null, can represent a key in a json object, even the empty string.
     *
     * @param key the given name of the key
     * @return a new Key object
     */
    public static Key of(String key)
    {
        return new Key(Objects.requireNonNull(key));
    }


    /**
     compares this key with another given position. If the given position is a key, both are compared
     lexicographically, if it's an index, both are compared numerically.
     @param o the given position
     @return 0 if they are equal, +1 if this is greater, -1 otherwise
     */
    @Override
    public int compareTo(final Position o)
    {
        if (Objects.requireNonNull(o)
                   .isKey()) return name.compareTo(o.asKey().name);
        return name.compareTo(o.asIndex().toString());

    }

    /**
     * Returns this key.
     * @return this object
     */
    @Override
    public Key asKey()
    {
        return this;
    }

    /**
     throws UnsupportedOperationException.
     * @throws UnsupportedOperationException a Key can't be casted into an Index
     */
    @Override
    public Index asIndex()
    {
        throw new UnsupportedOperationException("key.asIndex");
    }


    /**
     * Returns false.
     * @return false
     */
    @Override
    public boolean isIndex()
    {
        return false;
    }

    /**
     * Returns true.
     * @return true
     */
    @Override
    public boolean isKey()
    {
        return true;
    }

    /**
     *
     * Returns the name of the key.
     * @return the name of the key
     */
    @Override
    public String toString()
    {
        return name;
    }

    /**
     *
     * Returns true if that is a key and both have the same name.
     * @param that other object
     * @return true if both object are Keys with the same name
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        if (that == null || getClass() != that.getClass()) return false;
        if (this == that) return true;
        final Key thatObj = (Key) that;
        return name.equals(thatObj.name);
    }

    /**
     * Returns the hashcode of this key.
     * @return the hashcode of the name.
     */
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}
