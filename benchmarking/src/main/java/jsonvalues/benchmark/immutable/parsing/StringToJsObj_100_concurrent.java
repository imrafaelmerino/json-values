package jsonvalues.benchmark.immutable.parsing;

import jsonvalues.JsObj;
import jsonvalues.MalformedJson;
import jsonvalues.benchmark.ExecutorState;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class StringToJsObj_100_concurrent
{
    private final static int NUMBER_TASKS = 50;

    private static final String object = jsonvalues.benchmark.Data.OBJ_100.get();

    @Benchmark
    public boolean scala_hash_map(final ExecutorState e
                                 ) throws InterruptedException
    {

        CountDownLatch count = new CountDownLatch(NUMBER_TASKS);
        for (int i = 0; i < NUMBER_TASKS; i++)
        {


            e.service.submit(() ->
                             {
                                 try
                                 {
                                     return JsObj.parse(object)
                                                           ;
                                 }
                                 catch (MalformedJson ex)
                                 {
                                     throw new RuntimeException(ex);
                                 }
                                 finally
                                 {
                                     count.countDown();
                                 }
                             });
        }

        return count.await(10,
                           TimeUnit.SECONDS
                          );
    }

    ;


}
