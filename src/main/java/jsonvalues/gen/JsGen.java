package jsonvalues.gen;

import jsonvalues.JsValue;
import jsonvalues.gen.state.JsStateGen;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static jsonvalues.JsNothing.NOTHING;

public interface JsGen<R extends JsValue> extends Function<Random, Supplier<R>>
{

  default JsGen<?> optional()
  {

    return this.flatMap(value -> JsGens.oneOf(value,
                                              NOTHING
                                             )
                       );

  }

  default <T extends JsValue> JsGen<T> map(Function<R, T> f)
  {
    return random -> () -> f.apply(JsGen.this.apply(random)
                                             .get()
                                  );
  }

  default Supplier<R> sample()
  {
    return apply(new Random());
  }

  default Supplier<R> sample(final Random random)
  {
    return apply(random);
  }

  default JsGen<R> suchThat(final Predicate<R> predicate)
  {
    return suchThat(predicate,
                    100);
  }

  default JsGen<R> suchThat(final Predicate<R> predicate,
                            final int tries
                           )
  {
    return r -> () ->
    {
      for (int i = 0; i < tries; i++)
      {
        final R value = apply(r).get();
        if (predicate.test(value))
          return value;
      }
      throw new RuntimeException(String.format("Couldn't satisfy such-that predicate after %s tries",
                                               tries
                                              ));
    };
  }

  default JsStateGen stateMap(final Function<R, JsStateGen> f)
  {
    return o -> random -> () -> f.apply(JsGen.this.apply(random)
                                                  .get()
                                       )
                                 .apply(o)
                                 .apply(random)
                                 .get();
  }

  default <T extends JsValue> JsGen<T> flatMap(final Function<R, JsGen<T>> f)
  {
    return random -> requireNonNull(f).apply(JsGen.this.apply(random)
                                                       .get())
                                      .apply(random);
  }


}
