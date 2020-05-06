package jsonvalues.future;
import jsonvalues.JsValue;

import static java.util.Objects.requireNonNull;

/**
 Represents an immutable key-future pair
 @param <O> the type of the json future
 */
public class JsFuturePair<O extends JsValue>
{

  /**
   the key of the pair
   */
  public final String key;
  /**
   the json future of the pair
   */
  public final JsFuture<O> future;

  /**
   If a parameter is null, then a NullPointerException is thrown
   @param key the key of the pair
   @param future the future of the pair
   @param <O> the type of the future
   @return a new immutable pair
   */
  public static <O extends JsValue> JsFuturePair<O> of(final String key,
                                                       final JsFuture<O> future
                                                      )
  {
    return new JsFuturePair<>(requireNonNull(key),
                              requireNonNull(future)
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
