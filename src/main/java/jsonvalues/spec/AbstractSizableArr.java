package jsonvalues.spec;

abstract class AbstractSizableArr extends AbstractNullable {

    final int min;
    final int max;

    protected AbstractSizableArr(boolean nullable, int min, int max) {
        super(nullable);
        this.min = min;
        this.max = max;
    }

    protected AbstractSizableArr(boolean nullable) {
        super(nullable);
        this.min = 0;
        this.max = Integer.MAX_VALUE;
    }
}
