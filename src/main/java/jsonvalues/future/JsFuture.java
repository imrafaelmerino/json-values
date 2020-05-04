package jsonvalues.future;

import jsonvalues.JsValue;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface JsFuture<T extends JsValue> extends Supplier<CompletableFuture<T>> {

}
