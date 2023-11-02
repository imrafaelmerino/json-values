package jsonvalues.spec;

import jsonvalues.*;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericArray;
import org.apache.avro.generic.GenericRecord;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class AvroRecordToJsValue {

    private AvroRecordToJsValue() {
    }

    public static JsArray toJsArray(GenericArray<?> genericArray) {
        JsArray jsArray = JsArray.empty();
        Schema elementType = genericArray.getSchema().getElementType();
        for (Object item : genericArray) {
            jsArray = jsArray.append(toJsValue(item, elementType));
        }
        return jsArray;
    }

    static JsObj fromMapToJsOb(Object value, Schema valueSchema) {
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

    static JsValue fromUnionToJsValue(Object value, List<Schema> unionSchemas) {
        for (Schema schema : unionSchemas) {
            try {
                return toJsValue(value, schema);
            } catch (Exception e) {
                // If conversion fails, try the next schema in the union
            }
        }
        throw new IllegalArgumentException("Value does not match any of the union types");
    }


    static JsValue toJsValue(Object value, Schema schema) {
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
                LogicalType logicalType = schema.getLogicalType();
                if (logicalType != null) {
                    String name = logicalType.getName();
                    if (name.equals("bigdecimal"))
                        return JsBigDec.of(new BigDecimal(value.toString()));
                    if (name.equals("biginteger"))
                        return JsBigInt.of(new BigInteger(value.toString()));
                    if (name.equals("iso-8601"))
                        return JsInstant.of(value.toString());
                }
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
            LogicalType logicalType = schema.getLogicalType();
            if (logicalType != null) {
                var name = logicalType.getName();
                if (name.equals("timestamp-millis")) {
                    Instant instant = Instant.ofEpochMilli((long) value);
                    return JsInstant.of(instant);
                } else if (name.equals("timestamp-micros")) {
                    // Handle timestamp-micros logical type (convert int to timestamp)
                    Instant instant = Instant.ofEpochMilli(((long) value) / 1000);
                    return JsInstant.of(instant);
                }
            }
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
            // si no existe en record el fields, puede que este un alias y hay que leerlo
            // pero setearlo en jsobj con el nombre del schema (no el alias)
            result = result.set(field.name(), toJsValue(record.get(field.name()),
                                                        field.schema()));
        }

        return result;

    }


}
