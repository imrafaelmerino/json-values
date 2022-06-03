package jsonvalues.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.Dereferencing;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.Draft;
import io.vertx.json.schema.JsonSchemaOptions;
import io.vertx.json.schema.OutputUnit;
import jsonvalues.JsObj;
import jsonvalues.spec.JsObjParser;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static jsonvalues.benchmark.Fun.*;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class JsDeserializers {

    private static final ObjectMapper objectMapper =
            new ObjectMapper();

    // hibernate validator init
    private static final ValidatorFactory validatorFactory =
            Validation.buildDefaultValidatorFactory();
    private static final Validator validator =
            validatorFactory.getValidator();

    // json-values parser from spec
    private static final JsObjParser jsonParser =
            new JsObjParser(PERSON_SPEC);

    private static final com.networknt.schema.JsonSchemaFactory NETWORKNT_FACTORY =
            com.networknt.schema.JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

    private static final com.networknt.schema.JsonSchema NETWORKNT_SCHEMA =
            NETWORKNT_FACTORY.getSchema(PERSON_JSON_SCHEMA);
    // justify
    private static final JsonValidationService serviceJustify =
            JsonValidationService.newInstance();
    private static final JsonSchema schemaJustify =
            serviceJustify.readSchema(new StringReader(PERSON_JSON_SCHEMA));

    private static final Schema EVERIT_SCHEMA =
            SchemaLoader.load(new JSONObject(new JSONTokener(PERSON_JSON_SCHEMA)));

    private static final io.vertx.json.schema.Validator VERTX_VALIDATOR = io.vertx.json.schema.Validator.create(
            io.vertx.json.schema.JsonSchema.of(new JsonObject(PERSON_JSON_SCHEMA)),
            new JsonSchemaOptions().setDraft(Draft.DRAFT7)
                                   .setBaseUri("https://vertx.io"));


    // json schema validator init
    private static final LoadingConfiguration cfg =
            LoadingConfiguration.newBuilder()
                                .dereferencing(Dereferencing.INLINE)
                                .freeze();
    private static final JsonSchemaFactory jsonSchemaFactory =
            JsonSchemaFactory.newBuilder()
                             .setLoadingConfiguration(cfg)
                             .freeze();
    private static final com.github.fge.jsonschema.main.JsonSchema schema;

    static {
        try {
            schema = jsonSchemaFactory.getJsonSchema(JsonLoader.fromString(PERSON_JSON_SCHEMA));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Benchmark
    public void vertx_schema(Blackhole bh) {
        bh.consume(VERTX_VALIDATOR.validate(new JsonObject(PERSON_JSON)));
    }

    @Benchmark
    public void json_schema_validator(Blackhole bh) throws ProcessingException, JsonProcessingException {
        bh.consume(schema.validate(objectMapper.readTree(PERSON_JSON)));
    }

    @Benchmark
    public void justify(Blackhole bh) {
        jakarta.json.JsonReader reader =
                serviceJustify.createReader(new StringReader(PERSON_JSON),
                                            schemaJustify,
                                            System.out::println);

        jakarta.json.JsonObject obj = reader.readObject();
        reader.close();
        bh.consume(obj);
    }

    @Benchmark
    public void jackson_node(Blackhole bh) throws IOException {
        bh.consume(objectMapper.readTree(PERSON_JSON));
    }

    @Benchmark
    public void jackson_pojo(Blackhole bh) throws JsonProcessingException {
        bh.consume(objectMapper.readValue(PERSON_JSON,
                                          Person.class
        ));
    }

    @Benchmark
    public void jackson_pojo_bean_validation(Blackhole bh) throws JsonProcessingException {
        bh.consume(validator.validate(objectMapper.readValue(PERSON_JSON,
                                                             PersonWithAnnotations.class
        )));
    }

    @Benchmark
    public void json_values(Blackhole bh) {
        bh.consume(JsObj.parse(PERSON_JSON));
    }

    @Benchmark
    public void json_values_and_spec(Blackhole bh) {
        bh.consume(PERSON_SPEC.test(JsObj.parse(PERSON_JSON)));
    }

    @Benchmark
    public void json_spec_parser(Blackhole bh) {
        bh.consume(jsonParser.parse(PERSON_JSON));
    }

    @Benchmark
    public void everit() {
        EVERIT_SCHEMA.validate(new JSONObject(PERSON_JSON));
    }

    @Benchmark
    public void networknt(Blackhole bh) throws JsonProcessingException {
        bh.consume(NETWORKNT_SCHEMA.validate(objectMapper.readTree(PERSON_JSON)));
    }

}
