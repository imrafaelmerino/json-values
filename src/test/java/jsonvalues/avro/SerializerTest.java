package jsonvalues.avro;

import jsonvalues.*;
import jsonvalues.spec.*;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class SerializerTest {


    private static void testSpec(JsObjSpec spec, JsObj obj) {


        GenericData.Record record = AvroRecordFromJsValue.toAvro(obj,
                                                                 spec);


        JsObj obj2 = AvroRecordToJsValue.toJsObj(record);

        System.out.println(obj2);

        Assertions.assertEquals(obj, obj2, "The obj was transformed into a Record and then into an JsObj again, and they aren't equals.");
    }

    @Test
    public void testJsObjSpec() {
        var spec = JsObjSpecBuilder.name("Rafa")
                                   .spec(JsObjSpec.of("a", JsSpecs.str(),
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


        JsObj obj1 = JsObj.of("a", JsStr.of("a"),
                              "b", JsInt.of(1),
                              "c", JsStr.of("A"),
                              "d", JsBinary.of("a".getBytes(StandardCharsets.UTF_8)),
                              "e", JsArray.ofStrs("a", "b", "c"),
                              "f", JsArray.ofInts(1, 2, 3),
                              "g", JsObj.of("z", JsBool.FALSE)
                             );

//        JsObj obj1 = JsObj.of("a", JsNull.NULL,
//                              "b", JsNull.NULL,
//                              "c", JsNull.NULL,
//                              "d", JsNull.NULL,
//                              "e", JsNull.NULL,
//                              "f", JsNull.NULL,
//                              "g", JsObj.of("z", JsNull.NULL)
//                             );

        testSpec(spec, obj1);

    }

    @Test
    public void testValidateNotDuplicatedArrays() {

        var spec = JsObjSpecBuilder.name("orange")
                                   .spec(JsObjSpec.of("a", JsSpecs.oneSpecOf(List.of(JsSpecs.arrayOfDouble(),
                                                                                     JsSpecs.arrayOfInt())))
                                        );

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> AvroSchemaFromSpec.toAvroSchema(spec));

    }

    @Test
    public void testValidateNotDuplicatedMaps() {

        var spec = JsObjSpecBuilder.name("orange")
                                   .spec(JsObjSpec.of("a", JsSpecs.oneSpecOf(List.of(JsSpecs.mapOfDouble(),
                                                                                     JsSpecs.mapOfBool())))
                                        );

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> AvroSchemaFromSpec.toAvroSchema(spec));

    }


    @Test
    public void testValidateNotDuplicatedStr() {

        var spec = JsObjSpecBuilder.name("orange")
                                   .spec(JsObjSpec.of("a", JsSpecs.oneSpecOf(List.of(JsSpecs.str(),
                                                                                     JsSpecs.str(s -> !s.isBlank()))))
                                        );

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> AvroSchemaFromSpec.toAvroSchema(spec));

    }


    @Test
    public void testValidateNotDuplicatedRecord() {

        var spec = JsObjSpecBuilder.name("duplicated")
                                   .namespace("org")
                                   .spec(JsObjSpec.of("a", JsSpecs.integer()));

        var invalid = JsObjSpecBuilder.name("orange")
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
    public void testOptionalFields(){

        JsObjSpec spec = JsObjSpecBuilder.name("spec")
                                         .spec(JsObjSpec.of("a", JsSpecs.integer(),
                                                            "b", JsSpecs.str())
                                                       .withAllOptKeys()
                                              );

        testSpec(spec,JsObj.empty());


    }
}
