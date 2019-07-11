package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 It's a special json element that represents 'nothing'. Inserting nothing in a json leaves the json
 unchanged. Some functions that return a JsElem, like {@link Json#get(JsPath)} and {@link Json#get(String)},
 return nothing when no element is found, what makes them total on the input path.
 */
public final class JsNothing implements JsElem
{

    private JsNothing()
    {
    }

    /**
     The singleton nothing value.
     */
    public static final JsNothing NOTHING = new JsNothing();

    /**
     * @return "NOTHING"
     */
    @Override
    public String toString()
    {
        return "NOTHING";
    }

    /**
     Returns true if that is the singleton {@link JsNothing#NOTHING}.
     @param that the reference object with which to compare.
     @return true if that is {@link JsNothing#NOTHING}
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        return this == that;
    }

    /**
     Returns the hashcode of this JsNothing.
     @return 1
     */
    @Override
    public int hashCode()
    {
        return 1;
    }
}
