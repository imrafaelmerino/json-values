package jsonvalues;


/**
 * It's a special json element that represents 'nothing'. Inserting nothing in a json leaves the json
 * unchanged. The functions that return a JsValue, like {@link Json#get(JsPath)},
 * return nothing when no element is found, what makes them total on the input path.
 */
public final class JsNothing implements JsValue {
    public static final int TYPE_ID = 4;
    /**
     * The singleton nothing value.
     */
    public static final JsNothing NOTHING = new JsNothing();

    private JsNothing() {
    }

    @Override
    public int id() {
        return TYPE_ID;
    }

    @Override
    public JsPrimitive toJsPrimitive() {
        throw UserError.isNotAJsPrimitive(JsNothing.NOTHING);
    }

    @Override
    public boolean isNothing() {
        return true;
    }

    /**
     * Returns the hashcode of this JsNothing.
     *
     * @return 1
     */
    @Override
    public int hashCode() {
        return 1;
    }

    /**
     * Returns true if that is the singleton {@link JsNothing#NOTHING}.
     *
     * @param that the reference object with which to compare.
     * @return true if that is {@link JsNothing#NOTHING}
     */
    @Override
    public boolean equals(final Object that) {
        return this == that;
    }

    /**
     * @return "NOTHING"
     */
    @Override
    public String toString() {
        return "NOTHING";
    }

}
