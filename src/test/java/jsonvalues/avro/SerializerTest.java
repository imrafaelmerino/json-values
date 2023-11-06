package jsonvalues.avro;

import fun.gen.StrGen;
import jsonvalues.*;
import jsonvalues.spec.*;
import org.apache.avro.generic.GenericData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SerializerTest {

    Supplier<String> nameGen = StrGen.alphabetic(10, 20).sample();


    private static void testSpec(JsObjSpec spec, JsObj input) {
        testSpec(spec, input, input);
    }

    private static void testSpec(JsObjSpec spec, JsObj input, JsObj expected) {


        GenericData.Record record = AvroRecordFromJsValue.toAvro(input,
                                                                 spec);


        JsObjSpecParser parser = JsObjSpecParser.of(spec);

        Assertions.assertEquals(expected, parser.parse(input.toString()), "input != parser.parse(input.toString())");


        Assertions.assertEquals(expected, AvroRecordToJsValue.toJsObj(record), "input -> record -> !input");
    }

    @Test
    public void testJsObjSpec() {
        var spec = JsObjSpecBuilder.name("Rafa")
                                   .defaultFields(Map.of("a", JsNull.NULL))
                                   .spec(JsObjSpec.of("a", JsSpecs.str().nullable(),
                                                      "b", JsSpecs.integer(),
                                                      "c", JsEnumBuilder.name("enum")
                                                                        .symbols(List.of("A", "B")),
                                                      "d", JsFixedBuilder.name("fixed").build(1),
                                                      "e", JsSpecs.arrayOfStr(),
                                                      "f", JsSpecs.arrayOfInt(),
                                                      "g", OneOfObjSpec.of(List.of(JsObjSpecBuilder.name("Merino")
                                                                                                   .spec(JsObjSpec.of("x", JsSpecs.bool()).withAllOptKeys()),
                                                                                   JsObjSpecBuilder.name("Garcia")
                                                                                                   .spec(JsObjSpec.of("z", JsSpecs.bool()).withAllOptKeys())
                                                                                  )
                                                                          )
                                                     )
                                                  .withAllOptKeys()

                                        );





        testSpec(spec, JsObj.of("a", JsStr.of("a"),
                                "b", JsInt.of(1),
                                "c", JsStr.of("A"),
                                "d", JsBinary.of("a".getBytes(StandardCharsets.UTF_8)),
                                "e", JsArray.ofStrs("a", "b", "c"),
                                "f", JsArray.ofInts(1, 2, 3),
                                "g", JsObj.of("z", JsBool.FALSE)
                               ));
        testSpec(spec, JsObj.of(
                         "b", JsInt.of(1),
                         "c", JsStr.of("A"),
                         "d", JsBinary.of("a".getBytes(StandardCharsets.UTF_8)),
                         "e", JsArray.ofStrs("a", "b", "c"),
                         "f", JsArray.ofInts(1, 2, 3),
                         "g", JsObj.of("z", JsBool.FALSE)
                               ),
                 JsObj.of("a", JsNull.NULL,
                          "b", JsInt.of(1),
                          "c", JsStr.of("A"),
                          "d", JsBinary.of("a".getBytes(StandardCharsets.UTF_8)),
                          "e", JsArray.ofStrs("a", "b", "c"),
                          "f", JsArray.ofInts(1, 2, 3),
                          "g", JsObj.of("z", JsBool.FALSE)
                         ));

    }

    @Test
    public void testValidateNotDuplicatedArrays() {

        var spec = JsObjSpecBuilder.name(nameGen.get())
                                   .spec(JsObjSpec.of("a", JsSpecs.oneSpecOf(List.of(JsSpecs.arrayOfDouble(),
                                                                                     JsSpecs.arrayOfInt())))
                                        );

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> AvroSchemaFromSpec.toAvroSchema(spec));

    }

    @Test
    public void testValidateNotDuplicatedMaps() {

        var spec = JsObjSpecBuilder.name(nameGen.get())
                                   .spec(JsObjSpec.of("a", JsSpecs.oneSpecOf(List.of(JsSpecs.mapOfDouble(),
                                                                                     JsSpecs.mapOfBool())))
                                        );

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> AvroSchemaFromSpec.toAvroSchema(spec));

    }


    @Test
    public void testValidateNotDuplicatedStr() {

        var spec = JsObjSpecBuilder.name(nameGen.get())
                                   .spec(JsObjSpec.of("a", JsSpecs.oneSpecOf(List.of(JsSpecs.str(),
                                                                                     JsSpecs.str(s -> !s.isBlank()))))
                                        );

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> AvroSchemaFromSpec.toAvroSchema(spec));

    }


    @Test
    public void testValidateNotDuplicatedRecord() {

        var spec = JsObjSpecBuilder.name(nameGen.get())
                                   .namespace("org")
                                   .spec(JsObjSpec.of("a", JsSpecs.integer()));

        var invalid = JsObjSpecBuilder.name(nameGen.get())
                                      .spec(JsObjSpec.of("a", JsSpecs.oneSpecOf(List.of(spec,
                                                                                        spec)))
                                           );

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> AvroSchemaFromSpec.toAvroSchema(invalid));

    }

    @Test
    public void oneOfEnum() {

        JsEnum enum1 = JsEnumBuilder.name("uppercase").symbols(List.of("A", "B", "C"));
        JsEnum enum2 = JsEnumBuilder.name("lowercas").symbols(List.of("a", "b", "c"));
        JsSpec oneEnum = JsSpecs.oneSpecOf(List.of(enum1, enum2));

        JsObjSpec spec = JsObjSpecBuilder.name("a").spec(JsObjSpec.of("key", oneEnum));


        testSpec(spec, JsObj.of("key", JsStr.of("a")));
    }

    @Test
    public void testOptionalFields() {

        JsObjSpec spec = JsObjSpecBuilder.name(nameGen.get())
                                         .defaultFields(Map.of("a", JsInt.of(1),
                                                               "b", JsStr.of("a")))
                                         .spec(JsObjSpec.of("a", JsSpecs.integer().nullable(),
                                                            "b", JsSpecs.str().nullable())
                                                        .withAllOptKeys()

                                              );

        testSpec(spec, JsObj.empty(), JsObj.of("a", JsInt.of(1), "b", JsStr.of("a")));


    }

    @Test
    public void testAliasesFields() {

        JsObjSpec spec = JsObjSpecBuilder.name("spec")
                                         .aliasesFields(Map.of("a", List.of("a1", "a2"),
                                                               "b", List.of("b1", "b2"))
                                                       )
                                         .spec(JsObjSpec.of("a", JsSpecs.integer().nullable(),
                                                            "b", JsSpecs.str().nullable())
                                                        .withAllOptKeys()

                                              );

        testSpec(spec,
                 JsObj.of("a1", JsInt.of(1), "b1", JsStr.of("a")),
                 JsObj.of("a", JsInt.of(1), "b", JsStr.of("a")));

        testSpec(spec,
                 JsObj.of("a2", JsInt.of(1), "b2", JsStr.of("a")),
                 JsObj.of("a", JsInt.of(1), "b", JsStr.of("a")));


    }


}
