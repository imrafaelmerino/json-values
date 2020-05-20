package jsonvalues.supplier;

import io.vavr.collection.Vector;
import jsonvalues.JsArray;
import jsonvalues.JsValue;
import java.util.Arrays;
import java.util.function.Supplier;
import static java.util.Objects.requireNonNull;

/**
 Represents a Json array of suppliers that combines every supplier
 and produces as a result a Json array
 */
public class JsArraySupplier implements java.util.function.Supplier<JsArray> {
    private Vector<Supplier<? extends JsValue>> array = Vector.empty();

    @Override
    public JsArray get() {
        JsArray result = JsArray.empty();
        for (Supplier<? extends JsValue> supplier : array) {
            result = result.append(supplier.get());
        }

        return result;
    }


    private JsArraySupplier() {
    }

    private JsArraySupplier(final Supplier<? extends JsValue> fut,
                            final Supplier<? extends JsValue>... others
                           ) {
        array = array.append(fut)
                     .appendAll(Arrays.asList(others));
    }

    /**
     returns a JsArraySupplier from the given head and the tail
     @param head the head
     @param tail the tail
     @return a new JsArraySupplier
     */
    @SafeVarargs
    public static JsArraySupplier of(final Supplier<? extends JsValue> head,
                                     final Supplier<? extends JsValue>... tail
                                    ) {
        return new JsArraySupplier(requireNonNull(head),
                                   requireNonNull(tail)
        );
    }

    /**
     returns a JsArraySupplier that returns an empty Json array
     @return a JsArraySupplier
     */
    public static JsArraySupplier empty() {
        return new JsArraySupplier();
    }


    public JsArraySupplier append(final Supplier<? extends JsValue> future) {
        final JsArraySupplier arraySupplier = new JsArraySupplier();
        arraySupplier.array = this.array.append(future);
        return arraySupplier;
    }
}
