package jsonvalues.benchmark.parsing;

import com.fasterxml.jackson.databind.JsonNode;
import jsonvalues.JsArray;
import jsonvalues.benchmark.JacksonObjectMapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
public abstract class ParseStringToJsArray {

    private final String array;

    public ParseStringToJsArray(String array) {
        this.array = array;
    }

    @Benchmark
    public JsArray json_values() {
        return JsArray.parse(array)
                ;
    }


    @Benchmark
    public JsonNode jackson() throws IOException {
        return JacksonObjectMapper.get()
                                  .readTree(array);
    }


    @Benchmark
    public JsArray json_values_with_spec() {
        return JsArray.parse(array)
                ;
    }


}
