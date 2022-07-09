package com.dslplatform.json;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


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
