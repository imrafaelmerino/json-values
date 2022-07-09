package jsonvalues.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonvalues.JsObj;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.infra.Blackhole;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static jsonvalues.benchmark.Fun.PERSON_JSON;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
public class JsSerializers {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsObj json;
    private static final JsonNode node;
    private static final Person object;

    static {
        try {
            json = JsObj.parse(PERSON_JSON);
            node = objectMapper.readTree(PERSON_JSON);
            object = objectMapper.readValue(PERSON_JSON,
                                            Person.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Benchmark
    public void jackson_node(Blackhole bh) throws JsonProcessingException {
        bh.consume(objectMapper.writeValueAsBytes(node));
    }

    @Benchmark
    public void jackson_pojo(Blackhole bh) throws JsonProcessingException {
        bh.consume(objectMapper.writeValueAsBytes(object));
    }

    @Benchmark
    public void json_values(Blackhole bh) {
        bh.consume(json.serialize());
    }


}

