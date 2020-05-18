package jsonvalues.gen.state;


import jsonvalues.JsNothing;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import jsonvalues.gen.JsGen;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class JsStateGens {

    public static JsStateGen ifContains(final String key,
                                        final Function<JsValue, JsGen<?>> gen) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(gen);
        return obj -> random -> () -> {
            if (obj.get(key) == JsNothing.NOTHING) return JsNothing.NOTHING;
            else return gen.apply(obj.get(key))
                           .apply(random)
                           .get();
        };

    }

    public static JsStateGen ifNotContains(final String key,
                                           final JsGen<?> gen) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(gen);
        return obj -> random -> () -> {
            if (obj.get(key) == JsNothing.NOTHING) return gen.apply(random)
                                                             .get();
            else return JsNothing.NOTHING;
        };
    }

    public static JsStateGen ifElse(final Predicate<JsObj> condition,
                                    final JsGen<?> ifTrue,
                                    final JsGen<?> ifFalse
                                   ) {
        Objects.requireNonNull(condition);
        Objects.requireNonNull(ifTrue);
        Objects.requireNonNull(ifFalse);
        return current -> random -> () -> {
            if (condition.test(current)) return ifTrue.apply(random)
                                                      .get();
            else return ifFalse.apply(random)
                               .get();
        };
    }


}
