package jsonvalues.spec;

import java.util.function.Function;
import jsonvalues.JsArray;
import jsonvalues.JsValue;

final class JsArrayOfStrSuchThat extends AbstractNullable implements JsOneErrorSpec, JsArraySpec, AvroSpec {

  final Function<JsArray, JsError> predicate;
  private final JsArrayOfStr arrayOfStringSpec;

  JsArrayOfStrSuchThat(final Function<JsArray, JsError> predicate,
                       final boolean nullable
                      ) {
    super(nullable);
    this.arrayOfStringSpec = new JsArrayOfStr(nullable,
                                              null,
                                              null);
    this.predicate = predicate;
  }


  @Override
  public JsSpec nullable() {
    return new JsArrayOfStrSuchThat(predicate,
                                    true
    );
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofArrayOfStrSuchThat(predicate,
                                                   nullable
                                                  );
  }

  @Override
  public JsError testValue(final JsValue value) {
    JsError result = arrayOfStringSpec.testValue(value);
    return result != null || value.isNull() ?
           result :
           predicate.apply(value.toJsArray());
  }

}
