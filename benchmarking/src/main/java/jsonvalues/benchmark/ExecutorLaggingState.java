package jsonvalues.benchmark;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

import java.util.concurrent.TimeUnit;

/*
 * This is the *extension* of the basic state, which also
 * has the Level.Invocation fixture method, sleeping for some time.
 * This allows us to formulate the task: measure the task turnaround in
 * "hot" mode when we are not sleeping between the submits, and "cold" mode,
 * when we are sleeping.
 */

public class ExecutorLaggingState extends ExecutorState {
    public static final int SLEEP_TIME = Integer.getInteger("sleepTime",
                                                            10
                                                           );

    @Setup(Level.Invocation)
    public void lag() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
    }
}
