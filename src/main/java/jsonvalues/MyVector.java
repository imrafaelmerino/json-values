package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

interface MyVector<T extends MyVector<T>> extends Iterable<JsElem>


{

    String EMPTY_ARR_AS_STR = "[]";
    String OPEN_BRACKET = "[";
    String CLOSE_BRACKET = "]";
    String COMMA = ",";


    default boolean eq(final @Nullable Object that)
    {
        if (!(that instanceof MyVector)) return false;
        if (this == that) return true;
        final MyVector<?> thatArray = (MyVector) that;
        final boolean thatEmpty = thatArray.isEmpty();
        final boolean thisEmpty = isEmpty();
        if (thatEmpty && thisEmpty) return true;
        if (this.size() != thatArray.size()) return false;
        return YContainsX(this,
                          thatArray
                         ) && YContainsX(thatArray,
                                         this
                                        );

    }

    static boolean YContainsX(MyVector<?> x,
                              MyVector<?> y
                             )
    {
        for (int i = 0; i < x.size(); i++)
        {
            if (!Objects.equals(x.get(i),
                                y.get(i)
                               ))
                return false;

        }
        return true;

    }

    T add(java.util.Collection<? extends JsElem> list);

    T add(T list);

    T appendFront(JsElem elem);

    T appendBack(JsElem elem);

    T update(int index,
             JsElem ele
            );

    T remove(int index);

    JsElem head();

    T tail();

    T init();

    JsElem last();

    JsElem get(int index);

    int size();

    boolean isEmpty();

    boolean contains(JsElem e);

    Stream<JsElem> stream();
}
