package jsonvalues.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
 * This state handles the executor.
 * Note we create and shutdown executor with Level.Trial, so
 * it is kept around the same across all iterations.
 */

@State(Scope.Benchmark)
public class ExecutorState {
    public ExecutorService service;

    @Setup(Level.Trial)
    public void up() {
        service = Executors.newCachedThreadPool();
    }

    @TearDown(Level.Trial)
    public void down() {
        service.shutdown();
    }

}
