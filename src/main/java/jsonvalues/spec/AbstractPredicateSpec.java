package jsonvalues.spec;

abstract class AbstractPredicateSpec {

    final boolean required;
    final boolean nullable;

    public AbstractPredicateSpec(final boolean required,
                                 final boolean nullable
                                ) {
        this.required = required;
        this.nullable = nullable;
    }


}
