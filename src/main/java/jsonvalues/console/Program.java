package jsonvalues.console;

import jsonvalues.Json;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 Represents a side effect that interact with the user through the standard console to compose
 a Json

 @param <O> the type of the Json, either an object or an array */
@SuppressWarnings("squid:S106")
public interface Program<O extends Json<?>> {


    /**
     Execute the program, printing out a message before and after executing.

     @param promptMessage the message printed out before the execution
     @param resultFn      function that takes the result as a parameter and return the
     messages printed out after the execution
     @return a Json
     @throws ExecutionException   if this future completed exceptionally
     @throws InterruptedException if the current thread was interrupted
     */
    default O exec(final String promptMessage,
                   final Function<O, String> resultFn
                  ) throws ExecutionException, InterruptedException {
        System.out.println(Objects.requireNonNull(promptMessage));
        Objects.requireNonNull(resultFn);
        final O o = exec();
        System.out.println(resultFn.apply(o));
        return o;
    }

    /**
     Execute the program

     @return a Json
     @throws ExecutionException   if this future completed exceptionally
     @throws InterruptedException if the current thread was interrupted
     */
    O exec() throws ExecutionException, InterruptedException;

}
