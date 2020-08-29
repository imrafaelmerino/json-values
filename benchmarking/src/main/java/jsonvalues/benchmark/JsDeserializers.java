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
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static jsonvalues.benchmark.Conf.*;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class JsDeserializers {

    // jackson mapper
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // json-values parser
    private static final JsObjParser jsonParser = new JsObjParser(PERSON_SPEC);

   // hibernate validator init
    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();

    // justify init
    private static final JsonValidationService serviceJustify = JsonValidationService.newInstance();
    private static final org.leadpony.justify.api.JsonSchema schemaJustify =
            serviceJustify.readSchema(new StringReader(PERSON_jSON_SCHEMA));

    // json schema validator init
    private static final LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
                                                                        .dereferencing(Dereferencing.INLINE)
                                                                        .freeze();
    private static final JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder()
                                                                      .setLoadingConfiguration(cfg)
                                                                      .freeze();
    private static final JsonSchema schema;

    static {
        try {
            schema = jsonSchemaFactory.getJsonSchema(JsonLoader.fromString(PERSON_jSON_SCHEMA));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void json_schema_validator(Blackhole bh) throws ProcessingException, JsonProcessingException {
        JsonNode json = objectMapper.readTree(PERSON_JSON);
        bh.consume(schema.validate(json));
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
    public void jackson_node(Blackhole bh) throws IOException {
        bh.consume(objectMapper.readTree(PERSON_JSON));
    }

    @Benchmark
    public void jackson_pojo(Blackhole bh) throws JsonProcessingException {
        Person person = objectMapper.readValue(PERSON_JSON, Person.class);
        bh.consume(person);
    }

    @Benchmark
    public void jackson_pojo_bean_validation(Blackhole bh) throws JsonProcessingException {
        PersonWithAnnotations person = objectMapper.readValue(PERSON_JSON, PersonWithAnnotations.class);
        Set<ConstraintViolation<PersonWithAnnotations>> validate = validator.validate(person);
        bh.consume(validate.size() == 0);
    }

    @Benchmark
    public void json_values(Blackhole bh) {
        bh.consume(JsObj.parse(PERSON_JSON));
    }

    @Benchmark
    public void json_spec(Blackhole bh) {
        bh.consume(jsonParser.parse(PERSON_JSON));
    }

}
