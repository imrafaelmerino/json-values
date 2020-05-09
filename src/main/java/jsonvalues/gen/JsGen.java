package jsonvalues.gen;

import jsonvalues.JsValue;
import jsonvalues.gen.state.JsStateGen;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import static java.util.Objects.requireNonNull;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;

public interface JsGen<R extends JsValue> extends Function<Random, Supplier<R>>
{

  default JsGen<?> nullable(){
    return this.flatMap(value -> JsGens.oneOf(value,
                                              NULL
                                             )
                       );
  }

  default JsGen<?> optional()
  {

    return this.flatMap(value -> JsGens.oneOf(value,
                                              NOTHING
                                             )
                       );

  }

  default <T extends JsValue> JsGen<T> map(final Function<R, T> f)
  {
    requireNonNull(f);
    return random -> () -> f.apply(JsGen.this.apply(requireNonNull(random))
                                             .get()
                                  );
  }

  default Supplier<R> sample()
  {
    return apply(new Random());
  }

  default Supplier<R> sample(final Random random)
  {
    return apply(requireNonNull(random));
  }

  default JsGen<R> suchThat(final Predicate<R> predicate)
  {
    return suchThat(requireNonNull(predicate),
                    100);
  }

  default JsGen<R> suchThat(final Predicate<R> predicate,
                            final int tries
                           )
  {
    requireNonNull(predicate);
    if(tries<0)throw new IllegalArgumentException("tries negative");
    return r -> () ->
    {
      Objects.requireNonNull(r);
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
    Objects.requireNonNull(f);
    return o -> r -> () -> f.apply(JsGen.this.apply(requireNonNull(r))
                                                  .get()
                                       )
                                 .apply(requireNonNull(o))
                                 .apply(r)
                                 .get();
  }

  default <T extends JsValue> JsGen<T> flatMap(final Function<R, JsGen<T>> f)
  {
    Objects.requireNonNull(f);
    return r -> f.apply(JsGen.this.apply(requireNonNull(r))
                                                       .get())
                                      .apply(r);
  }


}
