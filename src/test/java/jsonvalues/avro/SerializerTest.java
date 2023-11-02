package jsonvalues.avro;

import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.spec.*;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SerializerTest {


    @Test
    public void test() {
        var spec = JsObjSpecBuilder.name("Rafa")
                                   .build(JsObjSpec.of("a", JsSpecs.str(),
                                                       "b", JsSpecs.integer(),
                                                       "c", JsEnumBuilder.name("enum").build(List.of("A", "B"))
                                                      )
                                                   .withReqKeys("a")
                                         );


        Schema.Parser parser = new Schema.Parser();

        String strSchema = AvroSchemaFromSpec.toSchema(spec)
                                             .toString();
        System.out.println(strSchema);
        Schema avroSchema = parser.parse(strSchema);

        JsObj obj1 = JsObj.of("a", JsStr.of("a"),
                              "b", JsInt.of(1),
                              "c", JsStr.of("D")
                             );

        Assertions.assertTrue(spec.test(obj1).isEmpty());

        GenericRecord record = AvroRecordFromJsValue.toAvro(obj1,
                                                            avroSchema);

        System.out.println(record);

        JsObj obj2 = AvroRecordToJsValue.toJsObj(record);

        System.out.println(obj2);

        Assertions.assertEquals(obj1, obj2);

    }



    public class AvroSchemaValidation {

        public static void main(String[] args) {
            // Create a schema
            Schema schema = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"example\",\"fields\":[{\"name\":\"field1\",\"type\":\"int\"}]}");

            // Create a GenericRecord
            GenericRecord record = new GenericData.Record(schema);
            record.put("field1", 42);

            // Create AvroData with the schema
            AvroData avroData = new AvroData(schema);

            // Validate the GenericRecord against the schema
            boolean isValid = avroData.validate(schema, record);
            System.out.println("Is valid? " + isValid);
        }
    }

}
