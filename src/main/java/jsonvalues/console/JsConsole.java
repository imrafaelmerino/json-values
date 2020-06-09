package jsonvalues.console;

import jsonvalues.JsPath;
import jsonvalues.JsValue;
import jsonvalues.future.JsFuture;

import java.util.function.Consumer;
import java.util.function.Function;


/**
 Represents a functional effect than when execute creates e JsValue

 @param <T> type of the JsValue returned */
public interface JsConsole<T extends JsValue> extends Function<JsPath, JsFuture<T>> {

    /**
     @return consumer that accepts a path and prints out a message indicating to the user that they must
     type in the value associated to that path
     */
    default Consumer<JsPath> promptMessage() {
        return JsIOs.printIndentedPath();
    }


}
