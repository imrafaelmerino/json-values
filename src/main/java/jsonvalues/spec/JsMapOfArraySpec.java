package jsonvalues.spec;

import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.List;

import static java.util.Objects.requireNonNull;


final class JsMapOfArraySpec extends AbstractMap implements JsSpec, AvroSpec {

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

    public JsArraySpec getSpec() {
        return spec;
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
    public List<SpecError> test(JsPath path,
                                JsValue value
                               ) {

        return test(path,
                    value, it -> !spec.test(it).isEmpty(),
                    ERROR_CODE.ARRAY_CONDITION);
    }


}
