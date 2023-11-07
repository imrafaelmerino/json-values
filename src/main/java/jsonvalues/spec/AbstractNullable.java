package jsonvalues.spec;

abstract class AbstractNullable {

    final boolean nullable;

    protected AbstractNullable(final boolean nullable) {
        this.nullable = nullable;
    }


    public boolean isNullable() {
        return nullable;
    }
}
