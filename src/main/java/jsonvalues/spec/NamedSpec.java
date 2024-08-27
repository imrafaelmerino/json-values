package jsonvalues.spec;

import static java.util.Objects.requireNonNull;

import java.util.List;
import jsonvalues.JsNull;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

final class NamedSpec extends AbstractNullable implements JsSpec,AvroSpec {

  final String name;

  NamedSpec(final String name) {
    this(false,
         name);
  }

  NamedSpec(final boolean nullable,
            String name) {
    super(nullable);
    this.name = requireNonNull(name);
  }


  @Override
  public JsSpec nullable() {
    return new NamedSpec(true,
                         name);
  }

  @Override
  public JsParser parser() {
    return reader -> {
      if (reader.wasNull()) {
        if (nullable) {
          return JsNull.NULL;
        } else {
          throw reader.newParseError(ParserErrors.INVALID_NULL);
        }
      }
      return JsSpecCache.get(name)
                        .parser()
                        .parse(reader);
    };
  }

  @Override
  public List<SpecError> test(JsPath parentPath,
                              JsValue value) {
    if (nullable && value.isNull()) {
      return List.of();
    }
    return JsSpecCache.get(name)
                      .test(parentPath,
                            value);
  }


}
