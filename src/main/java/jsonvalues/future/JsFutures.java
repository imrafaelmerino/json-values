package jsonvalues.future;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class JsFutures {
    /**
     it creates a new future that retries a number of times the given one when an exception is produced

     @param supplier   the completable future wrapped in a supplier
     @param maxRetries the number of retries
     @param <R>        the type of the value returned when the future is completed
     @return a new CompletableFuture wrapped in a supplier
     */

    //TODO retornar supplier
    public static <R> Supplier<CompletableFuture<R>> retry(final Supplier<CompletableFuture<R>> supplier,
                                                           final int maxRetries) {
        Objects.requireNonNull(supplier);
        if (maxRetries < 1) throw new IllegalArgumentException("maxRetries must be greater than 0");
        return () -> {
            CompletableFuture<R> f = supplier.get();
            for (int i = 0; i < maxRetries; i++) {
                f = f.thenApply(CompletableFuture::completedFuture)
                     .exceptionally(t -> supplier.get())
                     .thenCompose(Function.identity());
            }
            return f;
        };
    }
}
