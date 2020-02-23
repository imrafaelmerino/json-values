package jsonvalues.benchmark.immutable.parsing;

import jsonvalues.*;
import org.openjdk.jmh.annotations.Benchmark;

public class StringToJsArray_100
{

    private static final String array = jsonvalues.benchmark.Data.ARR_100.get();

    @Benchmark
    public JsArray scala_vector() throws MalformedJson
    {
        return JsArray.parse(array)
                          ;
    }


}
