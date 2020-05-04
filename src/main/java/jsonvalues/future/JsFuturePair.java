package jsonvalues.future;
import jsonvalues.JsValue;

import static java.util.Objects.requireNonNull;

public class JsFuturePair<O extends JsValue>
{

  public final String key;
  public final JsFuture<O> future;

  public static <O extends JsValue> JsFuturePair<O> of(final String key,
                                                       final JsFuture<O> gen
                                                      )
  {
    return new JsFuturePair<>(requireNonNull(key),
                              requireNonNull(gen)
    );
  }

  private JsFuturePair(final String key,
                       final JsFuture<O> future
                      )
  {
    this.key = key;
    this.future = future;
  }

}
