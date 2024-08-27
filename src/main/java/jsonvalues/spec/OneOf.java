package jsonvalues.spec;

import java.util.ArrayList;
import java.util.List;
import jsonvalues.JsNull;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

final class OneOf extends AbstractNullable implements JsSpec, AvroSpec {

  final List<? extends JsSpec> specs;

  OneOf(boolean nullable,
        List<? extends JsSpec> specs) {
    super(nullable);
    this.specs = specs;
  }


  @Override
  public JsSpec nullable() {
    return new OneOf(true,
                     specs);
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
      return parse(reader,
                   0);
    };
  }

  private JsValue parse(DslJsReader reader,
                        int i) {
    if (i >= specs.size()) {
      throw JsParserException.reasonAt(ParserErrors.ONE_OF_EXHAUSTED,
                                       reader.getPositionInStream()
                                      );
    }
    JsSpec spec = specs.get(i);
    if (i < specs.size() - 1) {
      reader.setMark();
    }
    try {
      return spec.parser()
                 .parse(reader);
    } catch (JsParserException e) {
      assert debug(i,
                   e);
      if (i < specs.size() - 1) {
        reader.rollbackToMark();
      }
      return parse(reader,
                   i + 1);
    }
  }

  private boolean debug(int i,
                        JsParserException e) {
    System.out.printf("OneOf %d trie: %s. Keep trying with the next one spec%n",
                      i,
                      e.getMessage());
    return i >= 0;

  }

  @Override
  public List<SpecError> test(JsPath parentPath,
                              JsValue value) {
    if (nullable && value.isNull()) {
      return List.of();
    }
    return test(parentPath,
                value,
                0,
                new ArrayList<>());
  }

  private List<SpecError> test(JsPath parentPath,
                               JsValue value,
                               int i,
                               List<SpecError> accumulated
                              ) {

    if (i >= specs.size()) {
      return accumulated;
    }

    JsSpec spec = specs.get(i);
    List<SpecError> iErrors = spec.test(parentPath,
                                        value);
    if (iErrors.isEmpty()) {
      return iErrors;
    }

    iErrors.forEach(e -> e.setSpec(i + ""));
    accumulated.addAll(iErrors);

    return test(parentPath,
                value,
                i + 1,
                accumulated);
  }


  public List<? extends JsSpec> getSpecs() {
    return specs;
  }
}
