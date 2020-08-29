package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import io.vavr.collection.Vector;
import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.HashSet;
import java.util.Set;

import static jsonvalues.spec.ERROR_CODE.ARRAY_EXPECTED;
import static jsonvalues.spec.ERROR_CODE.SPEC_MISSING;

/**
 Represents a specification of every element of a Json array. It allows to define
 tuples and the schema of every of its elements.
 */
public class JsTupleSpec implements JsArraySpec {

    private final boolean required;
    private final boolean nullable;
    private final Vector<JsSpec> specs;
    private final boolean strict = true;

    private JsTupleSpec(final Vector<JsSpec> specs) {
        this(specs,
             true,
             false
            );
    }
    private JsTupleSpec(final Vector<JsSpec> specs,
                        boolean required,
                        boolean nullable
                       ) {
        this.specs = specs;
        this.required = required;
        this.nullable = nullable;
    }

    static JsTupleSpec of(JsSpec spec,
                          JsSpec... others
                         ) {
        Vector<JsSpec> specs = Vector.empty();
        specs = specs.append(spec);
        for (JsSpec s : others) specs = specs.append(s);
        return new JsTupleSpec(specs);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsTupleSpec nullable() {
        return new JsTupleSpec(specs,
                               required,
                               true
        );
    }

    @Override
    public JsTupleSpec optional() {
        return new JsTupleSpec(specs,
                               false,
                               nullable
        );
    }

    @Override
    public JsSpecParser parser() {

        Vector<JsSpecParser> parsers = Vector.empty();
        for (final JsSpec spec : specs) {
            parsers = parsers.append(spec.parser());
        }
        return JsSpecParsers.INSTANCE.ofArraySpec(parsers,
                                                  nullable
                                                 );
    }

    @Override
    public Set<JsErrorPair> test(final JsPath parentPath,
                                 final JsValue value
                                ) {
        return test(JsPath.empty()
                          .append(JsPath.fromIndex(-1)),
                    this,
                    new HashSet<>(),
                    value
                   );
    }

    @Override
    public Set<JsErrorPair> test(final JsArray array) {
        return test(JsPath.empty(),
                    array);
    }

    private Set<JsErrorPair> test(final JsPath parent,
                                  final JsTupleSpec tupleSpec,
                                  final Set<JsErrorPair> errors,
                                  final JsValue value
                                 ) {
        if (value.isNull() && nullable) return errors;

        if (!value.isArray()) {
            errors.add(JsErrorPair.of(parent,
                                      new Error(value,
                                                ARRAY_EXPECTED)));
            return errors;
        }
        JsArray              array     = value.toJsArray();
        final Vector<JsSpec> tupleSpecs     = tupleSpec.specs;
        final int            specsSize = tupleSpec.specs.size();
        if (specsSize > 0 && array.size() > specsSize && tupleSpec.strict) {
            errors.add(JsErrorPair.of(parent.tail()
                                            .index(specsSize),
                                      new Error(array.get(specsSize),
                                                SPEC_MISSING
                                      )
                                     )
                      );
            return errors;
        }
        JsPath currentPath = parent;

        for (int i = 0; i < specsSize; i++) {
            currentPath = currentPath.inc();
            final JsSpec spec = tupleSpecs.get(i);
            errors.addAll(spec.test(currentPath,
                                    array.get(i)));
        }
        return errors;
    }


}
