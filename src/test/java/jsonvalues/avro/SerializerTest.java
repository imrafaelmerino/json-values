package jsonvalues.avro;

import jsonvalues.*;
import jsonvalues.spec.AvroAttBuilder;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericArray;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class SerializerTest {

    public static JsArray toJsArray(GenericArray<?> genericArray) {
        JsArray jsArray = JsArray.empty();
        Schema elementType = genericArray.getSchema().getElementType();
        for (Object item : genericArray) {
            jsArray = jsArray.append(toJsValue(item, elementType));
        }
        return jsArray;
    }

    public static JsObj fromMapToJsOb(Object value, Schema valueSchema) {
        JsObj jsObj = JsObj.empty();
        Schema valueTypeSchema = valueSchema.getValueType();
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
            String key = entry.getKey().toString();
            Object entryValue = entry.getValue();
            JsValue jsValue = toJsValue(entryValue, valueTypeSchema);
            jsObj = jsObj.set(key, jsValue);
        }
        return jsObj;
    }

    public static JsValue fromUnionToJsValue(Object value, List<Schema> unionSchemas) {
        for (Schema schema : unionSchemas) {
            try {
                return toJsValue(value, schema);
            } catch (Exception e) {
                // If conversion fails, try the next schema in the union
            }
        }
        throw new IllegalArgumentException("Value does not match any of the union types");
    }


    public static JsValue toJsValue(Object value, Schema schema) {
        var type = schema.getType();

        if (value == null) {
            if (type == Schema.Type.NULL) {
                return JsNull.NULL;
            } else {
                throw new IllegalArgumentException("Value is null, but the schema type is not NULL");
            }
        }

        if (type == Schema.Type.BOOLEAN) {
            if (value instanceof Boolean) {
                return JsBool.of((Boolean) value);
            } else {
                throw new IllegalArgumentException("Expected a Boolean value for BOOLEAN schema type");
            }
        }

        if (type == Schema.Type.STRING) {

            if (value instanceof CharSequence) {
                if (schema.getLogicalType().equals("bigdecimal"))
                    return JsBigDec.of(new BigDecimal(value.toString()));
                if (schema.getLogicalType().equals("biginteger"))
                    return JsBigInt.of(new BigInteger(value.toString()));
                if (schema.getLogicalType().equals("iso-8601"))
                    return JsInstant.of(value.toString());
                return JsStr.of(value.toString());
            } else {
                throw new IllegalArgumentException("Expected a CharSequence value for STRING schema type");
            }
        }
        if (type == Schema.Type.ENUM) {
            if (value instanceof CharSequence) {
                return JsStr.of(value.toString());
            } else {
                throw new IllegalArgumentException("Expected a CharSequence value for ENUM schema type");
            }
        }

        if (type == Schema.Type.INT) {
            String logicalType = schema.getProp("logicalType");
            if (logicalType != null) {
                if (logicalType.equals("timestamp-millis")) {
                    Instant instant = Instant.ofEpochMilli((long) value);
                    return JsInstant.of(instant);
                }
                else if (logicalType.equals("timestamp-micros")) {
                    // Handle timestamp-micros logical type (convert int to timestamp)
                    Instant instant = Instant.ofEpochMilli(((long) value) / 1000);
                    return JsInstant.of(instant);
                }
                // Handle other logical types as needed
            }
            // If no logical type, treat the int as a JsInt
            return JsInt.of((int) value);
        }

        if (type == Schema.Type.LONG) {
            if (value instanceof Long) {
                return JsLong.of((long) value);
            } else {
                throw new IllegalArgumentException("Expected a Long value for LONG schema type");
            }
        }

        if (type == Schema.Type.DOUBLE || type == Schema.Type.FLOAT) {
            if (value instanceof Number) {
                return JsDouble.of(((Number) value).doubleValue());
            } else {
                throw new IllegalArgumentException("Expected a Number value for DOUBLE or FLOAT schema type");
            }
        }

        if (type == Schema.Type.BYTES || type == Schema.Type.FIXED) {
            if (value instanceof byte[]) {
                return JsBinary.of((byte[]) value);
            } else {
                throw new IllegalArgumentException("Expected a byte array value for BYTES or FIXED schema type");
            }
        }

        if (type == Schema.Type.RECORD) {
            if (value instanceof GenericRecord) {
                return toJsObj((GenericRecord) value);
            } else {
                throw new IllegalArgumentException("Expected a GenericRecord value for RECORD schema type");
            }
        }

        if (type == Schema.Type.ARRAY) {
            if (value instanceof GenericArray<?>) {
                return toJsArray((GenericArray<?>) value);
            } else {
                throw new IllegalArgumentException("Expected a GenericArray value for ARRAY schema type");
            }
        }

        if (type == Schema.Type.MAP) {
            if (value instanceof Map) {
                return fromMapToJsOb(value, schema.getValueType());
            } else {
                throw new IllegalArgumentException("Expected a Map value for MAP schema type");
            }
        }

        if (type == Schema.Type.UNION) {
            return fromUnionToJsValue(value, schema.getTypes());
        }

        throw new IllegalArgumentException("Type %s not supported".formatted(type.getName()));
    }


    public static JsObj toJsObj(GenericRecord record) {
        Schema schema = record.getSchema();
        JsObj result = JsObj.empty();
        assert schema.getType() == Schema.Type.RECORD;
        for (Schema.Field field : schema.getFields()) {
            result = result.set(field.name(), toJsValue(record.get(field.name()),
                                                        field.schema()));
        }

        return result;

    }

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
            array.set(index, js.value);
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

        JsObj obj1 = JsObj.of("a", JsStr.of("hi"),
                              "b", JsInt.of(1));
        GenericRecord record = toAvro(obj1,
                                      avroSchema);

        System.out.println(record);

        JsObj obj2 = toJsObj(record);

        System.out.println(obj2);

        Assertions.assertEquals(obj1, obj2);

    }
}
