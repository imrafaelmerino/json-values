package jsonvalues.spec;

import java.io.IOException;


@FunctionalInterface
interface IOCallable<V> {
    /**
     * Computes a result, or throws an IO exception if unable to do so.
     *
     * @return computed result
     * @throws IOException if unable to compute a result
     */
    V call() throws IOException;
}
