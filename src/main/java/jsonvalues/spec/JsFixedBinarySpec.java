package jsonvalues.spec;

import jsonvalues.*;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.ERROR_CODE.BINARY_FIXED_LENGTH_EXPECTED;

public final class JsFixedBinarySpec extends AbstractNullable implements JsValuePredicate, AvroSpec {

    private final int size;
    private AvroAttBuilder avroAttBuilder;

    public int getSize() {
        return size;
    }

    public AvroAttBuilder getAvroAttBuilder() {
        return avroAttBuilder;
    }

    public JsFixedBinarySpec withAvroAtt(final AvroAttBuilder builder) {
        this.avroAttBuilder = requireNonNull(builder);
        return this;
    }

    public JsFixedBinarySpec(boolean nullable, int size) {
        super(nullable);
        this.size = size;
        if(size <= 0 ) throw new IllegalArgumentException("size of fixed binary spec <= 0");

    }


    public JsFixedBinarySpec(int size) {
        this(false, size);
    }

    @Override
    public JsSpec nullable() {
        return new JsFixedBinarySpec(true, size);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofFixedBinary(size, nullable);
    }

    @Override
    public Optional<JsError> testValue(final JsValue value) {
        return Functions.testElem(val -> val.isBinary() && val.toJsBinary().value.length == size,
                                  BINARY_FIXED_LENGTH_EXPECTED,
                                  nullable
                                 )
                        .apply(value);

    }



}
