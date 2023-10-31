package jsonvalues.avro;

import jsonvalues.*;
import jsonvalues.spec.AvroAttBuilder;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.junit.jupiter.api.Test;

public class SerializerTest {

    public static GenericRecordBuilder toAvro(String key,
                                              JsValue value,
                                              GenericRecordBuilder builder,
                                              Schema schema
                                             ) {

        assert (schema.getType() == Schema.Type.RECORD);

        if (value instanceof JsStr js) {
            builder.set(key, js.value);
        } else if (value instanceof JsInt js) {
            builder.set(key, js.value);
        } else if (value instanceof JsLong js) {
            builder.set(key, js.value);
        } else if (value instanceof JsBigDec js) {
            builder.set(key, js.toString());
        } else if (value instanceof JsBigInt js) {
            builder.set(key, js.toString());
        } else if (value instanceof JsDouble js) {
            builder.set(key, js.value);
        } else if (value instanceof JsInstant js) {
            builder.set(key, js.value.toString());
        } else if (value instanceof JsBool js) {
            builder.set(key, js.value);
        } else if (value instanceof JsNull js) {
            builder.set(key, null);
        } else if (value instanceof JsBinary js) {
            builder.set(key, js.value);
        } else if (value instanceof JsObj js) {
            builder.set(key, toAvro(js, schema.getField(key).schema()));
        } else if (value instanceof JsArray js) {
            builder.set(key, toAvro(js, schema.getField(key).schema()));
        } else {
            throw new IllegalArgumentException("%s not supported".formatted(value.getClass().getName()));
        }

        return builder;
    }

    public static GenericData.Array<Object> toAvro(int index,
                                                   JsValue value,
                                                   GenericData.Array<Object> array,
                                                   Schema schema
                                                  ) {

        assert (schema.getType() == Schema.Type.ARRAY);

        if (value instanceof JsStr js) {
            array.set(index, null);
        } else if (value instanceof JsInt js) {
            array.set(index, js.value);
        } else if (value instanceof JsLong js) {
            array.set(index, js.value);
        } else if (value instanceof JsBigDec js) {
            array.set(index, js.toString());
        } else if (value instanceof JsBigInt js) {
            array.set(index, js.toString());
        } else if (value instanceof JsDouble js) {
            array.set(index, js.value);
        } else if (value instanceof JsInstant js) {
            array.set(index, js.value.toString());
        } else if (value instanceof JsBool js) {
            array.set(index, js.value);
        } else if (value instanceof JsNull js) {
            array.set(index, null);
        } else if (value instanceof JsBinary js) {
            array.set(index, js.value);
        } else if (value instanceof JsObj js) {
            array.set(index, toAvro(js, schema.getElementType()));
        } else if (value instanceof JsArray js) {
            array.set(index, toAvro(js, schema.getElementType()));
        } else {
            throw new IllegalArgumentException("%s not supported".formatted(value.getClass().getName()));
        }
        return array;
    }

    public static GenericData.Array<Object> toAvro(JsArray arr, Schema schema) {
        assert (schema.getType() == Schema.Type.ARRAY);

        GenericData.Array<Object> array = new GenericData.Array<>(arr.size(), schema);
        for (int i = 0; i < arr.size(); i++) {
            toAvro(i, arr.get(i), array, schema);
        }

        return array;

    }

    public static GenericRecord toAvro(JsObj obj, Schema schema) {
        assert (schema.getType() == Schema.Type.RECORD);

        GenericRecordBuilder builder = new GenericRecordBuilder(schema);


        for (var key : obj.keySet()) {

            toAvro(key, obj.get(key), builder, schema);

        }

        return builder.build();


    }

    @Test
    public void test() {
        JsObjSpec spec =
                JsObjSpec.of("a", JsSpecs.str(),
                             "b", JsSpecs.integer()
                            )
                         .withReqKeys("a")
                         .withAvroAtt(AvroAttBuilder.of("myrecord"));

        Schema.Parser parser = new Schema.Parser();

        String string = spec.toAvroSchema().toString();
        System.out.println(string);
        Schema avroSchema = parser.parse(string);

        GenericRecord record = toAvro(JsObj.of("a", JsStr.of("hi")),
                                      avroSchema);

        System.out.println(record);


    }
}
