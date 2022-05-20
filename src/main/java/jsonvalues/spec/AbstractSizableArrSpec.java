package jsonvalues.spec;

abstract class AbstractSizableArrSpec extends AbstractNullableSpec{

    final int min;
    final int max;

    protected AbstractSizableArrSpec(boolean nullable,int min, int max) {
        super(nullable);
        this.min = min;
        this.max = max;
    }

    protected AbstractSizableArrSpec(boolean nullable) {
        super(nullable);
        this.min = 0;
        this.max = Integer.MAX_VALUE;
    }
}
