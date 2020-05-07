package jsonvalues.gen;

import jsonvalues.JsValue;
import jsonvalues.gen.state.JsStateGen;

import java.util.Random;
import java.util.function.Function;
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

 //TODO sample and sample(n), suchThat

  default JsStateGen stateMap(final Function<R,JsStateGen> f){
    return o -> random -> () -> f.apply(JsGen.this.apply(random)
                                            .get()
                                 ).apply(o).apply(random).get();
  }

  default <T extends JsValue> JsGen<T> flatMap(final Function<R, JsGen<T>> f)
  {
    return random -> requireNonNull(f).apply(JsGen.this.apply(random)
                                                       .get())
                                      .apply(random);
  }




}
