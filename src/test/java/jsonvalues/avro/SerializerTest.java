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


    @Test
    public void test() {
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


        Schema.Parser parser = new Schema.Parser();

        String strSchema = AvroSchemaFromSpec.toSchema(spec)
                                             .toString();
        System.out.println(strSchema);
        Schema avroSchema = parser.parse(strSchema);

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

        Assertions.assertTrue(spec.test(obj1).isEmpty());

        GenericData.Record record = AvroRecordFromJsValue.toAvro(obj1,
                                                                 avroSchema);

        Assertions.assertTrue(GenericData.get().validate(avroSchema, record));


        System.out.println(record);

        JsObj obj2 = AvroRecordToJsValue.toJsObj(record);

        System.out.println(obj2);

        Assertions.assertEquals(obj1, obj2);

    }


}
