package jsonvalues.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.Dereferencing;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import jsonvalues.JsObj;
import jsonvalues.spec.JsObjParser;
import org.leadpony.justify.api.JsonValidationService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.infra.Blackhole;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.concurrent.TimeUnit;
import static jsonvalues.benchmark.Conf.*;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
public class JsSchemaValidations {

    // json-values parser
    private static final JsObjParser parser = new JsObjParser(PERSON_SPEC);

    // justify init
    private static final JsonValidationService serviceJustify = JsonValidationService.newInstance();
    private static final org.leadpony.justify.api.JsonSchema schemaJustify =
            serviceJustify.readSchema(new StringReader(PERSON_jSON_SCHEMA));


    // json schema validator init
    private static final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
                                                                        .dereferencing(Dereferencing.INLINE)
                                                                        .freeze();
    private static final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
                                                                      .setLoadingConfiguration(cfg)
                                                                      .freeze();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JsonNode jsonSchema;
    private static final JsonSchema schema;

    static {
        try {
            jsonSchema = JsonLoader.fromString(PERSON_jSON_SCHEMA);
            schema = factory.getJsonSchema(jsonSchema);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void json_schema_validator(Blackhole bh) throws JsonProcessingException, ProcessingException {
        JsonNode json = objectMapper.readTree(PERSON_JSON);
        bh.consume(schema.validate(json));
    }

    @Benchmark
    public void json_spec_during_parsing(Blackhole bh) {
        bh.consume(parser.parse(PERSON_JSON.getBytes()));
    }

    @Benchmark
    public void justify(Blackhole bh) {
        JsonReader reader = serviceJustify.createReader(new StringReader(PERSON_JSON),
                                                        schemaJustify,
                                                        bh::consume
                                                       );
        JsonObject json = reader.readObject();

        reader.close();

        bh.consume(json);
    }

    @Benchmark
    public void json_spec(Blackhole bh) {
        bh.consume(PERSON_SPEC.test(JsObj.parse(PERSON_JSON)));
    }
}
