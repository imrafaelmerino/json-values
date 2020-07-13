package jsonvalues.console;

import jsonvalues.JsArray;
import jsonvalues.JsPath;
import jsonvalues.future.JsFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;


/**
 represents a supplier of a completable future than composes a json array from the user inputs in
 the standard console. It has the same recursive structure as a json array. Each value of the array
 has a supplier of a completable future associated that, when executed, prints out its path  in the
 standard console and waits for the user to type in a value and press Enter. When the user fills out
 all the values, all the futures are completed and a json array is composed.
 */
public class JsArrayConsole implements JsConsole<JsArray>, Program<JsArray> {

    private final List<JsConsole<?>> seq = new ArrayList<>();

    /**
     static factory method to create a JsArrayIO

     @param head the head
     @param tail the tail
     @return a JsArrayIO
     */
    public static JsArrayConsole tuple(final JsConsole<?> head,
                                       final JsConsole<?>... tail
                                      ) {
        final JsArrayConsole array = new JsArrayConsole();
        array.seq.add(requireNonNull(head));
        array.seq.addAll(Arrays.asList(requireNonNull(tail)));
        return array;
    }

    @Override
    public JsArray exec() throws ExecutionException, InterruptedException {
        return apply(JsPath.empty()).get()
                                    .get();
    }

    /**
     @param path the parent path of the array
     @return a JsFuture that wen completed will return JsArray
     */
    @Override
    public JsFuture<JsArray> apply(final JsPath path) {
        requireNonNull(path);
        return () ->
        {

            CompletableFuture<JsArray> result = CompletableFuture.completedFuture(JsArray.empty());

            for (int i = 0; i < seq.size(); i++) {
                JsPath             p  = path.index(i);
                final JsConsole<?> io = seq.get(i);
                result = result.thenApply(array ->
                                          {
                                              io
                                                      .promptMessage()
                                                      .accept(p);
                                              return array;

                                          })
                               .thenCombine(io.apply(p)
                                              .get(),
                                            (array, value) -> array.append(value)
                                           );
            }

            return result;
        };
    }


    @Override
    public Consumer<JsPath> promptMessage() {
        return JsIOs.printlnIndentedPath();
    }


}
