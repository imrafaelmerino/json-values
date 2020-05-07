package jsonvalues.future;

import jsonvalues.JsValue;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 Represents a supplier of a completable future which result is a JsValue
 @param <T> the type returned by the completable future
 */
@FunctionalInterface
public interface JsFuture<T extends JsValue> extends Supplier<CompletableFuture<T>> {}
