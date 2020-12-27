package jsonvalues;

public abstract class JsPrimitive implements JsValue {

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean isJson() {
        return false;
    }
}
