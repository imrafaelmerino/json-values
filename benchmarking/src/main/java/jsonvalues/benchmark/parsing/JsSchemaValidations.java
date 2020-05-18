package jsonvalues.benchmark.parsing;

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
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.leadpony.justify.api.JsonValidationService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.infra.Blackhole;

import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static jsonvalues.spec.JsSpecs.*;

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
public class JsSchemaValidations {

    private final static String jsonSchemaStr = "{ \"$schema\": \"http://json-schema.org/draft-07/schema#\", \"title\": \"Person\", \"type\": \"object\", \"properties\": { \"firstName\": { \"type\": \"string\", \"description\": \"The person's first name.\" }, "
            + "\"lastName\": { \"type\": \"string\", \"description\": \"The person's last name.\" }, " +
            "\"age\": {" +
            " \"description\": \"Age in years which must be equal to or greater than zero.\", \"type\": \"integer\", \"minimum\": 0 }, \"latitude\": { \"type\": \"number\", \"minimum\": -90, \"maximum\": 90 }, \"longitude\": { \"type\": "
            + "\"number\", \"minimum\": -180, \"maximum\": 180 }, " +
            "\"fruits\": { \"type\": \"array\", \"items\": { \"type\": \"string\" } }, \"numbers\": { \"type\": \"array\", \"items\": { \"type\": \"integer\" } }, \"vegetables\": { \"type\": \"array\", "
            + "\"items\": { \"$ref\": \"#/definitions/veggie\" } } }, " +
            "\"definitions\": { \"veggie\": { \"type\": \"object\", \"required\": [ \"veggieName\", \"veggieLike\" ], \"properties\": { \"veggieName\": { \"type\": \"string\", \"description\": \"The name of the vegetable"
            + ".\" }, \"veggieLike\": { \"type\": \"boolean\"," +
            "" +
            "\"description\": \"Do I like this vegetable?\" } } } }}";
    private final static LoadingConfiguration cfg = LoadingConfiguration.newBuilder()
                                                                        .dereferencing(Dereferencing.INLINE)
                                                                        .freeze();
    private final static JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
                                                                      .setLoadingConfiguration(cfg)
                                                                      .freeze();
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static String json_str = "{ \"firstName\": \"John\", \"lastName\": \"Doe\", \"age\": 21, \"latitude\": 48.858093, \"longitude\": 2.294694, \"fruits\": [ \"apple\", \"orange\", \"pear\" ], \"numbers\": [ 1, 2, 3, 4, 5, 6,"
            + " 7, 8, 9, 10 ], \"vegetables\": [ { \"veggieName\": \"potato\", " +
            " \"veggieLike\": true }, { \"veggieName\": \"broccoli\", \"veggieLike\": false } ]} " + "\"veggieName\": \"broccoli\", \"veggieLike\": false } ]}";
    private final static byte[] json_bytes = json_str.getBytes();
    private final static JsObjSpec spec = JsObjSpec.strict("firstName",
                                                           JsSpecs.str,
                                                           "lastName",
                                                           JsSpecs.str,
                                                           "age",
                                                           JsSpecs.integer(greaterOrEqualThan(0)),
                                                           "latitude",
                                                           decimal(interval(new BigDecimal(-90),
                                                                            new BigDecimal(90)
                                                                           )
                                                                  ),
                                                           "longitude",
                                                           decimal(interval(new BigDecimal(-180),
                                                                            new BigDecimal(180)
                                                                           )
                                                                  ),
                                                           "fruits",
                                                           JsSpecs.arrayOfStr,
                                                           "numbers",
                                                           JsSpecs.arrayOfInt,
                                                           "vegetables",
                                                           arrayOf(JsObjSpec.strict("veggieName",
                                                                                    str,
                                                                                    "veggieLike",
                                                                                    bool
                                                                                   )
                                                                  )
                                                          );
    private final static JsObjParser parser = new JsObjParser(spec);
    private final static JsonValidationService serviceJustify = JsonValidationService.newInstance();
    private final static org.leadpony.justify.api.JsonSchema schemaJustify = serviceJustify.readSchema(new StringReader(jsonSchemaStr));
    private static JsonNode jsonSchema;
    private static JsonSchema schema;

    static {
        try {
            jsonSchema = JsonLoader.fromString(jsonSchemaStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            schema = factory.getJsonSchema(jsonSchema);
        } catch (ProcessingException e) {
            e.printStackTrace();
        }
    }

    static IntPredicate greaterOrEqualThan(int value) {
        return i -> i >= value;
    }

    private static Predicate<BigDecimal> interval(BigDecimal min,
                                                  BigDecimal max
                                                 ) {
        return v -> v.doubleValue() <= max.doubleValue() && v.doubleValue() >= min.doubleValue();
    }

    /**
     validation with json-schema-validator is performed after the deserialization

     @param bh
     */
    @Benchmark
    public void json_schema_validator(Blackhole bh) throws JsonProcessingException, ProcessingException {
        JsonNode json = objectMapper.readTree(json_str);
        bh.consume(schema.validate(json));
    }

    /**
     validations and parsing are performed simultaneously

     @param bh
     */
    @Benchmark
    public void json_values_deserialization_with_spec(Blackhole bh) {
        bh.consume(parser.parse(json_bytes));
    }


    /**
     validation with justify is performed after the deserialization

     @param bh
     */
    @Benchmark
    public void justify(Blackhole bh) {

        JsonReader reader = serviceJustify.createReader(new StringReader(json_str),
                                                        schemaJustify,
                                                        bh::consume
                                                       );

        JsonObject json = reader.readObject();

        reader.close();

        bh.consume(json);
    }

    /**
     validation with spec is performed after the deserialization

     @param bh
     */
    @Benchmark
    public void json_values_parse_and_validation_with_spec(Blackhole bh) {
        bh.consume(spec.test(JsObj.parse(json_str)));
    }
}
