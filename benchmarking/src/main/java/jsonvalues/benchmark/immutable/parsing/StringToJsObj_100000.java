package jsonvalues.benchmark.immutable.parsing;

import jsonvalues.*;
import org.openjdk.jmh.annotations.Benchmark;

public class StringToJsObj_100000
{
    private static final String object = jsonvalues.benchmark.Data.OBJ_100000.get();


    @Benchmark
    public JsObj scala_hash_map() throws MalformedJson
    {

        return JsObj.parse(object)
                                   ;
    }


}
