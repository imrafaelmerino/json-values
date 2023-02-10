package jsonvalues;


/**
 * Represents null. It's a singleton, only the instance {@link jsonvalues.JsNull#NULL} exists.
 */
public final class JsNull extends JsPrimitive {
    /**
     * The singleton null value.
     */
    public static final JsNull NULL = new JsNull();

    private JsNull() {
    }


    @Override
    public JsPrimitive toJsPrimitive() {
        return this;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    /**
     * Returns the hashcode of this JSON null
     *
     * @return 1
     */
    @Override
    public int hashCode() {
        return 1;
    }

    /**
     * Returns true if that is the singleton {@link JsNull#NULL}.
     *
     * @param that the reference object with which to compare.
     * @return true if that is {@link JsNull#NULL}
     */
    @Override
    public boolean equals(final Object that) {
        return that == this;
    }

    /**
     * @return "null"
     */
    @Override
    public String toString() {
        return "null";
    }

}

