package jsonvalues.gen;
import jsonvalues.JsValue;

import static java.util.Objects.requireNonNull;

public class JsGenPair<O extends JsValue>
{

  public final String key;
  public final JsGen<O> gen;

  public static <O extends JsValue> JsGenPair<O> of(final String key,
                                                    final JsGen<O> gen
                                                )
  {
    return new JsGenPair<>(requireNonNull(key),
                           requireNonNull(gen)
    );
  }

  private JsGenPair(final String key,
                    final JsGen<O> gen
                   )
  {
    this.key = key;
    this.gen = gen;
  }

}
