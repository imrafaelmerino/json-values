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

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static jsonvalues.benchmark.Conf.PERSON_JSON;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
public class JsSerializers {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsObj json;
    private static final JsonNode node;
    private static final Person person;

    static {
        try {
            json = JsObj.parse(PERSON_JSON);
            node = objectMapper.readTree(PERSON_JSON);
            person = objectMapper.readValue(PERSON_JSON,Person.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Benchmark
    public void jackson_node(Blackhole bh) throws JsonProcessingException {
        byte[] bytes = objectMapper.writeValueAsBytes(node);
        bh.consume(bytes);
    }

    @Benchmark
    public void jackson_pojo(Blackhole bh) throws JsonProcessingException {
        byte[] bytes = objectMapper.writeValueAsBytes(person);
        bh.consume(bytes);
    }

    @Benchmark
    public void json_values(Blackhole bh) {
        byte[] str = json.serialize();
        bh.consume(str);
    }


}

