package jsonvalues.spec;

import jsonvalues.*;

import java.util.Set;

import static java.util.Objects.requireNonNull;


final class JsMapOfArraySpec extends AbstractMap implements JsSpec {

    final JsArraySpec spec;

    JsMapOfArraySpec(final boolean nullable,
                     final JsArraySpec spec
                    ) {
        super(nullable);
        this.spec = spec;
    }

    JsMapOfArraySpec(final JsArraySpec spec) {
        this(false, requireNonNull(spec));
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfArraySpec(true, spec);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfSpec(spec.parser(),
                                                  nullable);
    }

    @Override
    public Set<SpecError> test(JsPath path,
                               JsValue value
                              ) {

        ???
    }

    @Override
    public JsValue toAvro() {
        JsObj schema = JsObj.of("type", JsStr.of("map"),
                                "vales", spec.toAvro());
        return nullable ? JsArray.of(JsNull.NULL, schema) : schema;

    }

}
