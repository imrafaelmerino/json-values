package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

interface MyMap<T extends MyMap<T>>

{

    String CLOSE_BRACKET = "}";
    String COMMA = ",";
    String EMPTY_OBJ_AS_STR = "{}";
    BiFunction<String, JsElem, String> MAP_PAIR_TO_STR = (key, elem) -> String.format("\"%s\":%s",
                                                                                      key,
                                                                                      elem
                                                                                     );
    String OPEN_BRACKET = "{";

    boolean contains(String key);

    default boolean eq(final @Nullable Object that)
    {
        if (!(that instanceof MyMap)) return false;
        if (this == that) return true;
        final MyMap<?> thatObj = (MyMap) that;
        final boolean thisEmpty = isEmpty();
        final boolean thatEmpty = thatObj.isEmpty();
        if (thisEmpty && thatEmpty) return true;
        if (thisEmpty != thatEmpty) return false;

        return fields().stream()
                       .allMatch(f ->
                                 thatObj.getOptional(f)
                                        .map(it -> it.equals(get(f)))
                                        .orElse(false) && thatObj.fields()
                                                                 .stream()
                                                                 .allMatch(this::contains))
        ;


    }

    Set<String> fields();

    JsElem get(String key);

    Optional<JsElem> getOptional(String key);

    Map.Entry<String, JsElem> head();

    boolean isEmpty();

    Iterator<Map.Entry<String, JsElem>> iterator();

    T remove(String key);

    int size();

    T tail(String head);

    T update(String key,
             JsElem je
            );

    T updateAll(java.util.Map<String, JsElem> map);

}
