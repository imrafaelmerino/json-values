package jsonvalues.benchmark.immutable.parsing;

import jsonvalues.*;
import org.openjdk.jmh.annotations.Benchmark;

public class StringToJsArray_10000
{

    private static final String array = jsonvalues.benchmark.Data.ARR_10000.get();


    @Benchmark
    public JsArray scala_vector() throws MalformedJson
    {

        return JsArray.parse(array)
                                    ;
    }


}
