package jsonvalues.gen;

import jsonvalues.JsValue;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;

public interface JsGen<R extends JsValue> extends Function<Random, Supplier<R>> {

    /**
     returns a new generator that generates the same elements as this generator and NULL

     @return a new generator
     */
    default JsGen<?> nullable() {
        return this.flatMap(value -> JsGens.oneOf(value,
                                                  NULL
                                                 )
                           );
    }

    /**
     Creates a new generator that passes the result of this generator into the given function
     `f`. `f` should return a new generator. This allows you to create new
     generators that depend on the value of other generators

     @param f   the function
     @param <T> the type of the generated values
     @return a generator
     */
    default <T extends JsValue> JsGen<T> flatMap(final Function<R, JsGen<T>> f) {
        Objects.requireNonNull(f);
        return r -> f.apply(JsGen.this.apply(requireNonNull(r))
                                      .get())
                     .apply(r);
    }

    /**
     returns a new generator that generates the same elements as this generator and NOTHING.
     Inserting NOTHING in a json removes the element. Comes in handy to generate Json objects with
     different keys.

     @return a new generator
     */
    default JsGen<?> optional() {

        return this.flatMap(value -> JsGens.oneOf(value,
                                                  NOTHING
                                                 )
                           );

    }

    /**
     Returns this generator but with the values transformed by the given map function

     @param f   map function
     @param <T> type of the generated value
     @return a new generator
     */
    default <T extends JsValue> JsGen<T> map(final Function<R, T> f) {
        requireNonNull(f);
        return random -> () -> f.apply(JsGen.this.apply(requireNonNull(random))
                                                 .get()
                                      );
    }

    /**
     Return a supplier of realized values from this generator

     @return a supplier of values
     */
    default Supplier<R> sample() {
        return apply(new Random());
    }

    /**
     Return a supplier of realized values from this generator and the given seed

     @param random the seed of the generator
     @return a supplier of values
     */
    default Supplier<R> sample(final Random random) {
        return apply(requireNonNull(random));
    }

    /**
     Creates a generator that generates values from this generator that satisfy the given predicate.
     Care is needed to ensure there is a high chance this generator will satisfy
     the predicate. By default, `suchThat` will try 100 times to generate a value that
     satisfies the predicate. If no value passes this predicate after this number
     of iterations, a runtime exception will be thrown.

     @param predicate the predicate satisfied by every generated value
     @return a generator
     */
    default JsGen<R> suchThat(final Predicate<R> predicate) {
        return suchThat(requireNonNull(predicate),
                        100
                       );
    }

    /**
     Creates a generator that generates values from this generator that satisfy the given predicate.
     Care is needed to ensure there is a high chance this generator will satisfy
     the predicate. By default, `suchThat` will try specified number of times to generate a value that
     satisfies the predicate. If no value passes this predicate after this number
     of iterations, a runtime exception will be thrown.

     @param predicate the predicate satisfied by every generated value
     @param tries     the number of tries
     @return a generator
     */
    default JsGen<R> suchThat(final Predicate<R> predicate,
                              final int tries
                             ) {
        requireNonNull(predicate);
        if (tries < 0) throw new IllegalArgumentException("tries negative");
        return r -> () ->
        {
            Objects.requireNonNull(r);
            for (int i = 0; i < tries; i++) {
                final R value = apply(r).get();
                if (predicate.test(value))
                    return value;
            }
            throw new RuntimeException(String.format("Couldn't satisfy such-that predicate after %s tries",
                                                     tries
                                                    ));
        };
    }

}
