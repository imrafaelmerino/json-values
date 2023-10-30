package jsonvalues.spec;

import jsonvalues.*;

import java.util.*;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.SPEC_MISSING;

/**
 * Represents a specification of every element of a Json array. It allows to define tuples and the schema of every of
 * its elements.
 */
final class JsTuple implements JsArraySpec {

    private final boolean required;
    private final boolean nullable;
    private final List<JsSpec> specs;
    private final boolean strict = true;

    private JsTuple(final List<JsSpec> specs) {
        this(specs,
             true,
             false
            );
    }

    private JsTuple(final List<JsSpec> specs,
                    boolean required,
                    boolean nullable
                   ) {
        this.specs = specs;
        this.required = required;
        this.nullable = nullable;
    }

    static JsTuple of(JsSpec spec,
                      JsSpec... others
                     ) {
        List<JsSpec> specs = new ArrayList<>();
        specs.add(spec);
        Collections.addAll(specs,
                           others);
        return new JsTuple(specs);
    }


    @Override
    public JsTuple nullable() {
        return new JsTuple(specs,
                           required,
                           true
        );
    }

    @Override
    public JsSpecParser parser() {

        List<JsSpecParser> parsers = new ArrayList<>();
        for (JsSpec spec : specs)
            parsers.add(spec.parser());

        return JsSpecParsers.INSTANCE.ofArraySpec(parsers,
                                                  nullable
                                                 );
    }

    @Override
    public JsValue toAvro() {
        throw new AvroNotSupported(JsTuple.class);
    }


    @Override
    public Set<SpecError> test(final JsPath parentPath,
                               final JsValue value
                              ) {
        return test(JsPath.empty()
                          .append(JsPath.fromIndex(-1)),
                    this,
                    new HashSet<>(),
                    value
                   );
    }




    private Set<SpecError> test(final JsPath parent,
                                final JsTuple tupleSpec,
                                final Set<SpecError> errors,
                                final JsValue value
                               ) {
        if (value.isNull() && nullable) return errors;

        if (!value.isArray()) {
            errors.add(SpecError.of(parent,
                                    new JsError(value,
                                                ARRAY_EXPECTED)));
            return errors;
        }
        JsArray array = value.toJsArray();
        int specsSize = tupleSpec.specs.size();
        if (specsSize > 0 && array.size() > specsSize && tupleSpec.strict) {
            errors.add(SpecError.of(parent.tail()
                                          .index(specsSize),
                                    new JsError(array.get(specsSize),
                                                SPEC_MISSING
                                    )
                                   )
                      );
            return errors;
        }
        JsPath currentPath = parent;

        for (int i = 0; i < specsSize; i++) {
            currentPath = currentPath.inc();
            JsSpec spec = tupleSpec.specs.get(i);
            errors.addAll(spec.test(currentPath,
                                    array.get(i)));
        }
        return errors;
    }


}
