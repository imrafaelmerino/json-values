package jsonvalues.spec;

import jsonvalues.*;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;

import java.util.List;

public class AvroRecordFromJsValue {

    public static GenericData.Array<Object> toAvro(final JsArray arr,
                                                   final JsArraySpec spec
                                                  ) {

        assert spec.test(arr).isEmpty() :
                "The JsArray doesn't conform the spec.Errors: %s".formatted(spec.test(arr));

        var schema = AvroSchemaFromSpec.toAvroSchema(spec);

        return toAvro(arr, schema);

    }

    public static GenericData.Record toAvro(final JsObj obj,
                                            final JsObjSpec spec
                                           ) {
        assert spec.test(obj).isEmpty() :
                "The JsObj doesn't conform the spec.Errors: %s".formatted(spec.test(obj));

        var schema = AvroSchemaFromSpec.toAvroSchema(spec);

        GenericData.Record record = toAvro(obj, schema);

        assert GenericData.get().validate(schema, record) : "Avro validate methods fails validating the record %s against the schema %s".formatted(record, schema);

        return record;
    }

    public static GenericData.Array<Object> toAvro(final JsArray arr,
                                                   final Schema schema
                                                  ) {
        assert (schema.getType() == Type.ARRAY ||
                (schema.getType() == Type.UNION && unionContain(schema,
                                                                Type.ARRAY)));
        var arrSchema = getType(schema, Type.ARRAY);
        var array = new GenericData.Array<>(arr.size(), arrSchema);
        for (int i = 0; i < arr.size(); i++) toAvro(i, arr.get(i), array, arrSchema);
        assert GenericData.get().validate(schema, arr) : "Avro validate methods fails validating the Array %s against the schema %s".formatted(array, schema);
        return array;
    }

    public static GenericData.Record toAvro(final JsObj obj,
                                            final Schema schema
                                           ) {
        assert (schema.getType() == Type.RECORD
                || (schema.getType() == Type.UNION
                    && unionContain(schema, Type.RECORD))
        );

        List<Schema> recordSchemas = getAllRecords(schema,
                                                   obj.size());

        for (Schema recordSchema : recordSchemas) {
            GenericRecordBuilder builder = new GenericRecordBuilder(recordSchema);
            try {
                for (var key : obj.keySet()) {
                    Schema.Field field = recordSchema.getField(key);
                    if (field == null)
                        throw new IllegalArgumentException("JsObj doesn't conform the schema " + recordSchema.getName() + " because the key " + key + " has not been defined in the avro schema");
                    toAvro(key, obj.get(key), builder, field.schema());
                }
                return builder.build();
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        throw new IllegalArgumentException("No schema is valid");

    }

    static GenericData.Array<Object> toAvro(int index,
                                            JsValue value,
                                            GenericData.Array<Object> array,
                                            Schema schema
                                           ) {

        assert (schema.getType() == Type.ARRAY);
        if (value instanceof JsStr js) array.add(index, js.value);
        else if (value instanceof JsInt js) array.add(index, js.value);
        else if (value instanceof JsLong js) array.add(index, js.value);
        else if (value instanceof JsBigDec js) array.add(index, js.toString());
        else if (value instanceof JsBigInt js) array.add(index, js.toString());
        else if (value instanceof JsDouble js) array.add(index, js.value);
        else if (value instanceof JsInstant js) array.add(index, js.value.toString());
        else if (value instanceof JsBool js) array.add(index, js.value);
        else if (value instanceof JsNull) array.add(index, null);
        else if (value instanceof JsBinary js) array.add(index, js.value);
        else if (value instanceof JsObj js) array.add(index,
                                                      toAvro(js,
                                                             schema.getElementType()));
        else if (value instanceof JsArray js) array.add(index,
                                                        toAvro(js,
                                                               schema.getElementType()));
        else throw new IllegalArgumentException("%s not supported".formatted(value.getClass().getName()));

        return array;
    }


    static GenericRecordBuilder toAvro(String field,
                                       JsValue value,
                                       GenericRecordBuilder builder,
                                       Schema fieldSchema
                                      ) {

        if (value instanceof JsStr js) {
            if (fieldSchema.getType() == Type.ENUM ||
                (fieldSchema.getType() == Type.UNION && unionContain(fieldSchema, Type.ENUM)))
                builder.set(field,
                            new GenericData.EnumSymbol(getEnumType(fieldSchema,
                                                                   js.value),
                                                       js.value)
                           );
            else builder.set(field, js.value);
        } else if (value instanceof JsInt js) builder.set(field, js.value);
        else if (value instanceof JsLong js) builder.set(field, js.value);
        else if (value instanceof JsBigDec js) builder.set(field, js.toString());
        else if (value instanceof JsBigInt js) builder.set(field, js.toString());
        else if (value instanceof JsDouble js) builder.set(field, js.value);
        else if (value instanceof JsInstant js) builder.set(field, js.value.toString());
        else if (value instanceof JsBool js) builder.set(field, js.value);
        else if (value instanceof JsNull) builder.set(field, null);
        else if (value instanceof JsBinary js) {
            if (fieldSchema.getType() == Type.FIXED || (fieldSchema.getType() == Type.UNION
                                                        && unionContain(fieldSchema, Type.FIXED)))
                builder.set(field,
                            new GenericData.Fixed(getFixedType(fieldSchema,
                                                               js.value.length),
                                                  js.value));
            else builder.set(field,
                             js.value);

        } else if (value instanceof JsObj js) builder.set(field, toAvro(js, fieldSchema));
        else if (value instanceof JsArray js) builder.set(field, toAvro(js, fieldSchema));
        else throw new IllegalArgumentException("%s not supported".formatted(value.getClass().getName()));
        return builder;
    }

    //TODO
    private static Schema getEnumType(Schema schema, String symbol) {
        return getAllType(schema, Type.ENUM)
                .stream()
                .filter(it -> it.getEnumSymbols().contains(symbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(""));
    }

    //TODO
    private static Schema getFixedType(Schema schema, int size) {
        return getAllType(schema, Type.FIXED)
                .stream()
                .filter(it -> it.getFixedSize() == size)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(""));
    }


    private static Schema getType(Schema schema, Type type) {
        if (schema.getType() == type) return schema;
        return schema
                .getTypes()
                .stream()
                .filter(it -> it.getType() == type)
                .findFirst()
                .get();
    }

    private static List<Schema> getAllType(Schema schema, Type type) {
        if (schema.getType() == type) return List.of(schema);
        return schema
                .getTypes()
                .stream()
                .filter(it -> it.getType() == type)
                .toList();
    }

    private static boolean unionContain(Schema unionSchema, Type type) {
        return unionSchema.getTypes()
                          .stream()
                          .map(Schema::getType)
                          .anyMatch(it -> it == type);
    }


    private static List<Schema> getAllRecords(Schema schema,
                                              int sizeObj
                                             ) {
        return getAllType(schema, Type.RECORD)
                .stream()
                .filter(it -> it.getFields().size() == sizeObj)
                .toList();
    }
}
