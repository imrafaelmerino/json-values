package jsonvalues;


/**
 Represents null, which a valid json value. It's a singleton, only the instance JsNull.NULL exists.
 */
public final class JsNull implements JsValue {
    public static final int TYPE_ID = 1;
    /**
     The singleton null value.
     */
    public static final JsNull NULL = new JsNull();

    private JsNull() {
    }

    @Override
    public int id() {
        return TYPE_ID;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    /**
     Returns the hashcode of this json null

     @return 1
     */
    @Override
    public int hashCode() {
        return 1;
    }

    /**
     Returns true if that is the singleton {@link JsNull#NULL}.

     @param that the reference object with which to compare.
     @return true if that is {@link JsNull#NULL}
     */
    @Override
    public boolean equals(final Object that) {
        return that == this;
    }

    /**
     @return "null"
     */
    @Override
    public String toString() {
        return "null";
    }

}

