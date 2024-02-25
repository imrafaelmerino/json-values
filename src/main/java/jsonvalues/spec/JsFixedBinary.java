package jsonvalues.spec;

import static jsonvalues.spec.ERROR_CODE.BINARY_FIXED_LENGTH_EXPECTED;

import jsonvalues.JsValue;

final class JsFixedBinary extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  private final int size;

  private final FixedMetaData metaData;

  JsFixedBinary(boolean nullable,
                int size,
                FixedMetaData metaData) {
    super(nullable);
    this.size = size;
    if (size <= 0) {
      throw new IllegalArgumentException("size of fixed binary spec <= 0");
    }
    this.metaData = metaData;

  }

  JsFixedBinary(int size) {
    this(false,
         size,
         null);
  }

  int getSize() {
    return size;
  }

  @Override
  public JsSpec nullable() {
    return new JsFixedBinary(true,
                             size,
                             metaData);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofFixedBinary(size,
                                            nullable);
  }

  @Override
  public JsError testValue(final JsValue value) {
    return Fun.testValue(val -> val.isBinary() && val.toJsBinary().value.length == size,
                         BINARY_FIXED_LENGTH_EXPECTED,
                         nullable,
                         value
                        );

  }

  FixedMetaData getMetaData() {
    return metaData;
  }
}
