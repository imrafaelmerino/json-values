package jsonvalues.spec;

import jsonvalues.*;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;

public class AvroRecordFromJsValue {

    private static boolean unionContain(Schema unionSchema, Type type) {
        return unionSchema.getTypes()
                          .stream()
                          .map(Schema::getType)
                          .anyMatch(it -> it == type);
    }

    private static boolean unionContain(Schema unionSchema, Type type, String logicalType) {
        return unionSchema.getTypes()
                          .stream()
                          .anyMatch(it -> it.getType() == type && it.getLogicalType()
                                                                    .getName()
                                                                    .equals(logicalType)
                                   );
    }

    public static GenericRecordBuilder toAvro(String field,
                                              JsValue value,
                                              GenericRecordBuilder builder,
                                              Schema fieldSchema
                                             ) {

        if (value instanceof JsStr js) builder.set(field, js.value);
        else if (value instanceof JsInt js) builder.set(field, js.value);
        else if (value instanceof JsLong js) builder.set(field, js.value);
        else if (value instanceof JsBigDec js) builder.set(field, js.toString());
        else if (value instanceof JsBigInt js) builder.set(field, js.toString());
        else if (value instanceof JsDouble js) builder.set(field, js.value);
        else if (value instanceof JsInstant js) builder.set(field, js.value.toString());
        else if (value instanceof JsBool js) builder.set(field, js.value);
        else if (value instanceof JsNull) builder.set(field, null);
        else if (value instanceof JsBinary js) builder.set(field, js.value);
        else if (value instanceof JsObj js) builder.set(field, toAvro(js, fieldSchema));
        else if (value instanceof JsArray js) builder.set(field, toAvro(js, fieldSchema));
        else throw new IllegalArgumentException("%s not supported".formatted(value.getClass().getName()));
        return builder;
    }

    private static boolean arrayValueIsExpected(Schema fieldSchema, Type type) {
        return type == Type.ARRAY || (type == Type.UNION && unionContain(fieldSchema, Type.ARRAY));
    }

    private static boolean objValueIsExpected(Schema fieldSchema, Type type) {
        return type == Type.RECORD || (type == Type.UNION && unionContain(fieldSchema, Type.RECORD));
    }

    private static boolean binaryValueIsExpected(Schema fieldSchema, Type type) {
        return type == Type.FIXED || type == Type.BYTES || (type == Type.UNION && (unionContain(fieldSchema, Type.BYTES) || unionContain(fieldSchema, Type.FIXED)));
    }

    private static boolean nullValueIsExpected(Schema fieldSchema, Type type) {
        return false;
    }

    private static boolean boolValueIsExpected(Schema fieldSchema, Type type) {
        return type == Type.BOOLEAN || (type == Type.UNION && unionContain(fieldSchema, Type.BOOLEAN));
    }

    private static boolean instantValueIsExpected(Schema fieldSchema, Type type) {
        return type == Type.STRING && fieldSchema.getLogicalType().getName().equals("iso-8601");
    }

    private static boolean doubleIsExpected(Schema fieldSchema, Type type) {
        return type == Type.DOUBLE || (type == Type.UNION && unionContain(fieldSchema, Type.DOUBLE));
    }

    private static boolean bigIntegerIsExpected(Schema fieldSchema, Type type) {
        return type == Type.STRING && fieldSchema.getLogicalType().getName().equals("biginteger");
    }

    private static boolean bigDecValueIsExpected(Schema fieldSchema, Type type) {
        return type == Type.STRING && fieldSchema.getLogicalType().getName().equals("bigdecimal");
    }

    private static boolean longValueIsExpected(Schema fieldSchema, Type type) {
        return type == Type.LONG || (type == Type.UNION && unionContain(fieldSchema, Type.LONG));
    }

    private static boolean intValueIsExpected(Schema fieldSchema, Type type) {
        return type == Type.INT || (type == Type.UNION && unionContain(fieldSchema, Type.INT));
    }

    private static boolean strValueIsExpected(Schema fieldSchema, Type type, String value) {
        //todo es un string codificado en base 64 y el typo del schema es fixed or binary
        if (type == Type.STRING) return true;
        if (type == Type.UNION && unionContain(fieldSchema, Type.STRING)) return true;
        if (type == Type.ENUM && fieldSchema.hasEnumSymbol(value)) return true;
        if (type == Type.UNION && unionContain(fieldSchema, Type.ENUM) && getEnum(fieldSchema).hasEnumSymbol(value))
            return true;
        return false;
    }

    private static Schema getEnum(Schema fieldSchema) {
        return fieldSchema.getTypes().stream().filter(it -> it.getType() == Type.ENUM).findFirst().get();
    }

    public static GenericData.Array<Object> toAvro(int index,
                                                   JsValue value,
                                                   GenericData.Array<Object> array,
                                                   Schema schema
                                                  ) {

        assert (schema.getType() == Type.ARRAY);

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
        assert (schema.getType() == Type.ARRAY);
        GenericData.Array<Object> array = new GenericData.Array<>(arr.size(), schema);
        for (int i = 0; i < arr.size(); i++) toAvro(i, arr.get(i), array, schema);
        return array;

    }

    public static GenericRecord toAvro(JsObj obj, Schema schema) {
        assert (schema.getType() == Type.RECORD);
        GenericRecordBuilder builder = new GenericRecordBuilder(schema);
        for (var key : obj.keySet()) toAvro(key, obj.get(key), builder, schema.getField(key).schema());
        return builder.build();
    }
}
