package jsonvalues.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import jsonvalues.JsObj;
import jsonvalues.spec.JsObjParser;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static jsonvalues.benchmark.Conf.PERSON_JSON;
import static jsonvalues.benchmark.Conf.PERSON_SPEC;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class JsDeserializers {



    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final static JsObjParser jsonParser = new JsObjParser(PERSON_SPEC);

    @Benchmark
    public void json_spec(Blackhole bh) {
        bh.consume(jsonParser.parse(PERSON_JSON));
    }

    @Benchmark
    public void jackson(Blackhole bh) throws IOException {
        bh.consume(objectMapper.readTree(PERSON_JSON));
    }

    @Benchmark
    public void json_values(Blackhole bh) {
        bh.consume(JsObj.parse(PERSON_JSON));
    }
}
