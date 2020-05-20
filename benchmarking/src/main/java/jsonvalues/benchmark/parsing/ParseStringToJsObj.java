package jsonvalues.benchmark.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jsonvalues.JsObj;
import jsonvalues.benchmark.JacksonObjectMapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
public abstract class ParseStringToJsObj {

    private final String object;

    public ParseStringToJsObj(final String object) {
        this.object = object;
    }

    @Benchmark
    public JsObj json_values() {
        return JsObj.parse(object)
                ;
    }

    @Benchmark
    public JsonNode jackson() throws JsonProcessingException {
        return JacksonObjectMapper.get()
                                  .readTree(object);

    }

}
