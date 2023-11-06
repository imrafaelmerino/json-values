package jsonvalues.spec;

import jsonvalues.*;
import org.apache.avro.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static jsonvalues.spec.Constants.*;

public final class AvroSchemaFromSpec {

    static Schema.Parser parser = new Schema.Parser();
    static ConcurrentHashMap<String, Schema> cache = new ConcurrentHashMap<>();

    private AvroSchemaFromSpec() {
    }

    /**
     * hay veces que si no se cache el esquema y se intenta obtener el mismo se produce la excepcion
     * org.apache.avro.SchemaParseException: Can't redefine: spec, por eso hay que cachear por nombre
     *
     * @param spec
     * @return
     * @throws SpecNotSupportedInAvro
     */
    public static Schema toAvroSchema(final JsObjSpec spec) throws SpecNotSupportedInAvro {
        if (spec.metaData == null) throw MetadataNotFound.errorParsingJsObSpecToSchema();

        var fullName = spec.metaData.getFullName();

        if (cache.containsKey(fullName)) return cache.get(fullName);

        var jsSchema = objSpecSchema(spec, JsNothing.NOTHING);

        var schema = parser.parse(jsSchema.toString());

        cache.put(fullName,
                  schema
                 );

        return schema;
    }

    public static Schema toAvroSchema(final JsArraySpec spec) throws SpecNotSupportedInAvro {

        var jsSchema = toJsSchema(spec, JsNothing.NOTHING);

        return parser.parse(jsSchema.toString());


    }

    public static JsValue toJsSchema(final JsSpec spec, JsValue defaultValue) throws SpecNotSupportedInAvro {
        if (spec instanceof JsStrSpec) return strSchema(spec, defaultValue);
        if (spec instanceof JsStrSuchThat) return strSchema(spec, defaultValue);

        if (spec instanceof JsIntSpec) return intSchema(spec, defaultValue);
        if (spec instanceof JsIntSuchThat) return intSchema(spec, defaultValue);

        if (spec instanceof JsLongSpec) return longSchema(spec, defaultValue);
        if (spec instanceof JsLongSuchThat) return longSchema(spec, defaultValue);

        if (spec instanceof JsNumberSpec) return numberSchema(spec, defaultValue);
        if (spec instanceof JsNumberSuchThat) return numberSchema(spec, defaultValue);

        if (spec instanceof JsDoubleSpec) return doubleSchema(spec, defaultValue);
        if (spec instanceof JsDoubleSuchThat) return doubleSchema(spec, defaultValue);

        if (spec instanceof JsDecimalSpec) return stringSchema(BIG_DECIMAL_TYPE,
                                                               spec.isNullable(),
                                                               defaultValue);
        if (spec instanceof JsDecimalSuchThat) return stringSchema(BIG_DECIMAL_TYPE,
                                                                   spec.isNullable(),
                                                                   defaultValue);

        if (spec instanceof JsBigIntSpec) return stringSchema(BIG_INTEGER_TYPE,
                                                              spec.isNullable(),
                                                              defaultValue
                                                             );
        if (spec instanceof JsBigIntSuchThat) return stringSchema(BIG_INTEGER_TYPE,
                                                                  spec.isNullable(),
                                                                  defaultValue);

        if (spec instanceof JsBooleanSpec) return boolSchema(spec, defaultValue);
        if (spec instanceof JsBinarySpec) return binarySchema(spec, defaultValue);
        if (spec instanceof JsBinarySuchThat) return binarySchema(spec, defaultValue);

        if (spec instanceof JsFixedBinary fixedBinary) return fixedSchema(fixedBinary, defaultValue);

        if (spec instanceof JsArrayOfStrSuchThat)
            return arrayOfStringSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfStr)
            return arrayOfStringSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfTestedStr)
            return arrayOfStringSchema(spec, defaultValue);

        if (spec instanceof JsArrayOfBigIntSuchThat) return arrayOfBigIntSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfBigInt) return arrayOfBigIntSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfTestedBigInt) return arrayOfBigIntSchema(spec, defaultValue);

        if (spec instanceof JsArrayOfIntSuchThat) return arrayOfIntSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfInt) return arrayOfIntSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfTestedInt) return arrayOfIntSchema(spec, defaultValue);


        if (spec instanceof JsArrayOfDoubleSuchThat) return arrayOfDoubleSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfDouble) return arrayOfDoubleSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfTestedDouble) return arrayOfDoubleSchema(spec, defaultValue);

        if (spec instanceof JsArrayOfLongSuchThat) return arrayOfLongSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfLong) return arrayOfLongSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfTestedLong) return arrayOfLongSchema(spec, defaultValue);

        if (spec instanceof JsArrayOfDecimal) return arrayOfDecimalSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfTestedDecimal) return arrayOfDecimalSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfDecimalSuchThat) return arrayOfDecimalSchema(spec, defaultValue);

        if (spec instanceof JsArrayOfBool) return arrayOfBooleanSchema(spec, defaultValue);
        if (spec instanceof JsArrayOfBoolSuchThat) return arrayOfBooleanSchema(spec, defaultValue);

        if (spec instanceof JsInstantSpec) return instantSchema(spec, defaultValue);
        if (spec instanceof JsInstantSuchThat) return instantSchema(spec, defaultValue);

        if (spec instanceof JsArrayOfObjSpec arrayOfObjSpec) return arrayOfObjSpecSchema(arrayOfObjSpec, defaultValue);
        if (spec instanceof JsObjSpec objSpec) return objSpecSchema(objSpec, defaultValue);

        if (spec instanceof JsMapOfInt) return mapOfIntSchema(spec, defaultValue);
        if (spec instanceof JsMapOfDouble) return mapOfDoubleSchema(spec, defaultValue);
        if (spec instanceof JsMapOfLong) return mapOfLongSchema(spec, defaultValue);
        if (spec instanceof JsMapOfBool) return mapOfBoolSchema(spec, defaultValue);
        if (spec instanceof JsMapOfBigInt) return mapOfBigIntegerSchema(spec, defaultValue);
        if (spec instanceof JsMapOfObjSpec mapOfObjSpec) return mapOfObjSpecSchema(mapOfObjSpec, defaultValue);
        if (spec instanceof JsMapOfStr) return mapOfStrSchema(spec, defaultValue);
        if (spec instanceof JsMapOfDec) return mapOfDecSchema(spec, defaultValue);
        if (spec instanceof JsMapOfInstant) return mapOfInstantSchema(spec, defaultValue);
        if (spec instanceof JsMapOfArraySpec mapOfArraySpec) return mapOfArraySpecSchema(mapOfArraySpec, defaultValue);

        if (spec instanceof JsEnum jsEnum) return enumSchema(jsEnum, defaultValue);
        if (spec instanceof OneOf oneOf) return oneOfSchema(oneOf, defaultValue);
        if (spec instanceof OneOfObjSpec oneOfObjSpec) return oneOfObjSpecSchema(oneOfObjSpec, defaultValue);

        throw new IllegalArgumentException("Spec " + spec.getClass().getName() + " not supported yet!");

    }

    private static JsArray oneOfObjSpecSchema(OneOfObjSpec js, JsValue defaultValue) {
        var specs = js.getSpecs();
        JsArray schema = JsArray.ofIterable(specs.stream()
                                                 .map(it -> toJsSchema(it, JsNothing.NOTHING))
                                                 .toList());
        return getTypeSorted(js.isNullable(), defaultValue, schema);

    }

    private static JsArray oneOfSchema(OneOf js, JsValue keyDefault) {
        var specs = js.getSpecs();
        List<JsValue> avroSchemas = new ArrayList<>();

        for (int i = 0; i < specs.size(); i++) {
            JsSpec spec = specs.get(i);
            if (spec instanceof AvroSpec) avroSchemas.add(toJsSchema(spec,JsNothing.NOTHING));
            else throw SpecNotSupportedInAvro.errorConvertingOneOfIntoSchema(spec, i);
        }
        JsArray schema = JsArray.ofIterable(avroSchemas);
        validateNotDuplicatedTypes(schema);

        if (js.isNullable())
            if (schema.containsValue(NULL_TYPE)) return schema;
            else return keyDefault.isNull()? schema.prepend(NULL_TYPE) : schema.append(NULL_TYPE);
        else return schema;
    }

    private static void validateNotDuplicatedTypes(JsArray schema) {
        Map<String, Integer> typeCounter = new HashMap<>();

        for (JsValue type : schema) {
            if (type instanceof JsStr name) {
                typeCounter.compute(name.value,
                                    (n, i) -> i == null ? 1 : i + 1);
            } else if (type instanceof JsObj objType) {
                if (objType.getStr(NAME_FIELD) != null) {
                    var nameSpace = objType.getStr(Constants.NAMESPACE_FIELD);
                    if (nameSpace != null) {
                        String fullName = "%s.%s (%s)".formatted(nameSpace,
                                                                 objType.getStr(NAME_FIELD),
                                                                 objType.getStr(TYPE_FIELD)
                                                                );
                        typeCounter.compute(fullName,
                                            (n, i) -> i == null ? 1 : i + 1);
                    } else {
                        String fullName = "%s (%s)".formatted(
                                objType.getStr(NAME_FIELD),
                                objType.getStr(TYPE_FIELD)
                                                             );
                        typeCounter.compute(fullName,
                                            (n, i) -> i == null ? 1 : i + 1);
                    }

                } else {
                    var name = objType.getStr(TYPE_FIELD);
                    typeCounter.compute(name,
                                        (n, i) -> i == null ? 1 : i + 1);
                }

            } else
                throw new IllegalArgumentException("invalid type: either a string or a Json object (nested types are not allowed in Avro)");
        }

        typeCounter.entrySet()
                   .stream()
                   .filter(e -> e.getValue() > 1)
                   .findFirst()
                   .ifPresent(e -> {
                       throw new IllegalArgumentException("type duplicated: " + e.getKey());
                   });

        System.out.println(typeCounter);

    }

    private static JsValue mapOfArraySpecSchema(JsMapOfArraySpec js, JsValue keyDefault) {
        var valueSpec = js.getSpec();
        if (valueSpec instanceof AvroSpec) {
            JsObj schema = JsObj.of(TYPE_FIELD, MAP_TYPE,
                                    VALUES_FIELD, toJsSchema(valueSpec,
                                                             JsNothing.NOTHING));
            return getTypeSorted(js.isNullable(), keyDefault, schema);

        }
        throw SpecNotSupportedInAvro.errorConvertingMapOfArraySpecIntoSchema(valueSpec);
    }

    private static JsValue mapOfObjSpecSchema(JsMapOfObjSpec js, JsValue keyDefault) {
        JsObj schema = JsObj.of(TYPE_FIELD, MAP_TYPE,
                                VALUES_FIELD, objSpecSchema(js.getSpec(),
                                                            JsNothing.NOTHING));
        return getTypeSorted(js.isNullable(), keyDefault, schema);

    }

    private static JsValue objSpecSchema(JsObjSpec objSpec, JsValue defaultValue) {
        var metadata = objSpec.getMetaData();

        if (metadata == null) throw MetadataNotFound.errorParsingJsObSpecToSchema();
        var bindings = objSpec.getBindings();
        var schema = JsObj.of(NAME_FIELD,
                              JsStr.of(metadata.name()),
                              TYPE_FIELD, Constants.RECORD_TYPE);
        if (metadata.namespace() != null)
            schema = schema.set(Constants.NAMESPACE_FIELD,
                                JsStr.of(metadata.namespace()));
        if (metadata.doc() != null)
            schema = schema.set(DOC_FIELD,
                                JsStr.of(metadata.doc()));
        if (metadata.aliases() != null)
            schema = schema.set(ALIASES_FIELD,
                                JsArray.ofStrs(metadata.aliases()));
        var fields = JsArray.empty();

        var fieldsDoc = metadata.fieldsDoc();
        var fieldsAliases = metadata.fieldsAliases();
        var fieldsOrder = metadata.fieldsOrder();
        var fieldsDefault = metadata.fieldsDefault();

        for (Map.Entry<String, JsSpec> entry : bindings.entrySet()) {
            var spec = entry.getValue();
            var key = entry.getKey();
            if (spec instanceof AvroSpec) {
                var keyDefault = fieldsDefault != null && fieldsDefault.get(key)!=null ?
                        fieldsDefault.get(key) :
                        JsNothing.NOTHING;

                var fieldSchema = JsObj.of(NAME_FIELD, JsStr.of(key),
                                           TYPE_FIELD, toAvro(key,
                                                              keyDefault,
                                                              objSpec.getRequiredFields(),
                                                              spec)
                                          );
                var doc = fieldsDoc != null ? fieldsDoc.get(key) : null;
                var order = fieldsOrder != null ? fieldsOrder.get(key) : null;
                var aliases = fieldsAliases != null ? fieldsAliases.get(key) : null;

                if (doc != null)
                    fieldSchema = fieldSchema.set(DOC_FIELD,
                                                  doc);
                if (order != null)
                    fieldSchema = fieldSchema.set(ORDER_FIELD,
                                                  order.name());
                if (keyDefault.isNotNothing())
                    fieldSchema = fieldSchema.set(DEFAULT_FIELD,
                                                  keyDefault);
                if (aliases != null)
                    fieldSchema = fieldSchema.set(ALIASES_FIELD,
                                                  JsArray.ofStrs(aliases));

                fields = fields.append(fieldSchema);
            } else throw SpecNotSupportedInAvro.errorConvertingObjSpecIntoSchema(key, spec, metadata);
        }

        schema = schema.set(FIELDS_FIELD, fields);

        return getTypeSorted(objSpec.isNullable(), defaultValue, schema);
    }

    private static JsValue toAvro(String key,
                                  JsValue keyDefault,
                                  List<String> requiredFields,
                                  JsSpec spec
                                 ) {
        assert keyDefault != null;
        JsValue schema = toJsSchema(spec, keyDefault);
        //if it were nullable, the above method toJsSchema would've added null to the type...
        if (!spec.isNullable() && !requiredFields.contains(key)) {
            return schema instanceof JsArray arrSchema ?
                    (keyDefault.isNull() ?
                            arrSchema.prepend(NULL_TYPE) :
                            arrSchema.append(NULL_TYPE)) :
                    (keyDefault.isNull() ?
                            JsArray.of(NULL_TYPE, schema) :
                            JsArray.of(schema, NULL_TYPE));
        }
        return schema;

    }

    private static JsValue arrayOfObjSpecSchema(JsArrayOfObjSpec spec,
                                                JsValue keyDefault
                                               ) {
        JsValue items = toJsSchema(spec, JsNothing.NOTHING);

        JsObj schema = JsObj.of(TYPE_FIELD, ARRAY_TYPE,
                                ITEMS_FIELD, items);

        return getTypeSorted(spec.isNullable(), keyDefault, schema);

    }

    private static JsValue arrayOfStringSchema(JsSpec js, JsValue keyDefault) {
        JsObj schema = JsObj.of(TYPE_FIELD, ARRAY_TYPE,
                                ITEMS_FIELD, STRING_TYPE);

        return getTypeSorted(js.isNullable(), keyDefault, schema);
    }

    private static JsValue arrayOfBooleanSchema(JsSpec js, JsValue keyDefault) {
        JsObj schema = JsObj.of(TYPE_FIELD, ARRAY_TYPE,
                                ITEMS_FIELD, BOOLEAN_TYPE);

        return getTypeSorted(js.isNullable(), keyDefault, schema);
    }

    private static JsValue arrayOfDecimalSchema(JsSpec js, JsValue defaultValue) {
        return containerOfLogicalType(BIG_DECIMAL_TYPE,
                                      ARRAY_TYPE,
                                      ITEMS_FIELD,
                                      js.isNullable(), defaultValue);
    }


    private static JsValue arrayOfLongSchema(JsSpec js, JsValue keyDefault) {
        JsObj schema = JsObj.of(TYPE_FIELD, ARRAY_TYPE,
                                ITEMS_FIELD, LONG_TYPE);

        return getTypeSorted(js.isNullable(), keyDefault, schema);

    }

    private static JsValue binarySchema(JsSpec js, JsValue keyDefault) {
        return js.isNullable() ?
                (keyDefault.isNull() ?
                        Constants.NULL_BINARY_TYPE :
                        Constants.BINARY_NULL_TYPE
                ) : Constants.BINARY_TYPE;

    }

    private static JsValue instantSchema(JsSpec js, JsValue keyDefault) {
        JsObj schema = JsObj.of(TYPE_FIELD, STRING_TYPE,
                                LOGICAL_TYPE_FIELD, ISO_TYPE);
        return getTypeSorted(js.isNullable(), keyDefault, schema);
    }

    private static JsValue strSchema(JsSpec js, JsValue keyDefault) {
        return js.isNullable() ?
                (keyDefault.isNull() ?
                        NULL_STR_TYPE :
                        STR_NULL_TYPE
                ) : STR_TYPE;
    }

    private static JsValue boolSchema(JsSpec js, JsValue keyDefault) {
        return js.isNullable() ?
                (keyDefault.isNull() ?
                        NULL_BOOL_TYPE :
                        BOOL_NULL_TYPE
                ) : Constants.BOOL_TYPE;
    }

    private static JsValue stringSchema(JsStr logicalType, boolean nullable, JsValue keyDefault) {
        JsObj schema = JsObj.of(TYPE_FIELD, STRING_TYPE,
                                LOGICAL_TYPE_FIELD, logicalType
                               );
        return getTypeSorted(nullable, keyDefault, schema);

    }

    private static JsValue getTypeSorted(boolean nullable,
                                         JsValue defaultValue,
                                         JsValue schema
                                        ) {
        return nullable ?
                (defaultValue.isNull() ?
                        JsArray.of(NULL_TYPE, schema) :
                        JsArray.of(schema, NULL_TYPE)
                ) : schema;
    }

    private static JsArray getTypeSorted(boolean nullable,
                                         JsValue defaultValue,
                                         JsArray schema
                                        ) {
        return nullable ?
                (defaultValue.isNull() ?
                        schema.prepend(NULL_TYPE) :
                        schema.append(NULL_TYPE)
                ) : schema;
    }


    private static JsValue longSchema(JsSpec js, JsValue keyDefault) {
        return js.isNullable() ?
                (keyDefault.isNull() ?
                        NULL_LONG_TYPE :
                        LONG_NULL_TYPE
                ) : LONG_TYPE;
    }

    private static JsValue intSchema(JsSpec js, JsValue keyDefault) {
        return js.isNullable() ?
                (keyDefault.isNull() ?
                        NULL_INT_TYPE :
                        INT_NULL_TYPE
                ) : Constants.INT_TYPE;
    }

    private static JsValue doubleSchema(JsSpec js, JsValue keyDefault) {
        return js.isNullable() ?
                (keyDefault.isNull() ?
                        NULL_DOUBLE_TYPE :
                        DOUBLE_NULL_TYPE
                ) : DOUBLE_TYPE;
    }

    private static JsValue fixedSchema(JsFixedBinary js, JsValue keyDefault) {
        var metadata = js.getMetaData();
        if (metadata == null) throw MetadataNotFound.errorParsingFixedToSchema();
        var schema = JsObj.of(NAME_FIELD,
                              JsStr.of(metadata.name()),
                              TYPE_FIELD, FIXED_TYPE,
                              SIZE_FIELD, JsInt.of(js.getSize()));
        if (metadata.namespace() != null)
            schema = schema.set(Constants.NAMESPACE_FIELD,
                                JsStr.of(metadata.namespace()));

        if (metadata.aliases() != null)
            schema = schema.set(ALIASES_FIELD,
                                JsArray.ofStrs(metadata.aliases()));


        return getTypeSorted(js.isNullable(), keyDefault, schema);

    }

    private static JsValue arrayOfIntSchema(JsSpec js, JsValue keyDefault) {
        var schema = JsObj.of(TYPE_FIELD, ARRAY_TYPE,
                              ITEMS_FIELD, INT_TYPE);

        return getTypeSorted(js.isNullable(), keyDefault, schema);
    }

    private static JsValue arrayOfDoubleSchema(JsSpec js, JsValue keyDefault) {
        var schema = JsObj.of(TYPE_FIELD, ARRAY_TYPE,
                              ITEMS_FIELD, DOUBLE_TYPE);

        return getTypeSorted(js.isNullable(), keyDefault, schema);
    }

    private static JsValue arrayOfBigIntSchema(JsSpec js,
                                               JsValue defaultValue
                                              ) {
        return containerOfLogicalType(BIG_INTEGER_TYPE,
                                      ARRAY_TYPE,
                                      ITEMS_FIELD,
                                      js.isNullable(),
                                      defaultValue);
    }

    private static JsArray numberSchema(JsSpec js, JsValue keyDefault) {

        return getTypeSorted(js.isNullable(), keyDefault, NUMBER_TYPE);
    }

    private static JsValue mapOfInstantSchema(JsSpec js, JsValue defaultValue) {
        return containerOfLogicalType(ISO_TYPE,
                                      MAP_TYPE,
                                      VALUES_FIELD,
                                      js.isNullable(),
                                      defaultValue);
    }

    private static JsValue mapOfDecSchema(JsSpec js,
                                          JsValue defaultValue
                                         ) {
        return containerOfLogicalType(BIG_DECIMAL_TYPE,
                                      MAP_TYPE,
                                      VALUES_FIELD,
                                      js.isNullable(),
                                      defaultValue);
    }

    private static JsValue containerOfLogicalType(JsStr logicalType,
                                                  JsStr containerType,
                                                  String elementsKey,
                                                  boolean isNullable,
                                                  JsValue defaultValue
                                                 ) {
        var schema = JsObj.of(TYPE_FIELD, STRING_TYPE,
                              LOGICAL_TYPE_FIELD, logicalType);
        var mapSchema = JsObj.of(TYPE_FIELD, containerType,
                                 elementsKey, schema
                                );
        return getTypeSorted(isNullable, defaultValue, mapSchema);

    }

    private static JsValue mapOfStrSchema(JsSpec js, JsValue defaultValue) {
        var schema = JsObj.of(TYPE_FIELD, MAP_TYPE,
                              VALUES_FIELD, STRING_TYPE);
        return getTypeSorted(js.isNullable(), defaultValue, schema);

    }

    private static JsValue mapOfBigIntegerSchema(JsSpec js, JsValue defaultValue) {
        return containerOfLogicalType(BIG_INTEGER_TYPE, MAP_TYPE, VALUES_FIELD, js.isNullable(), defaultValue);
    }

    private static JsValue mapOfBoolSchema(JsSpec js, JsValue keyDefault) {
        var schema = JsObj.of(TYPE_FIELD, MAP_TYPE,
                              VALUES_FIELD, BOOLEAN_TYPE
                             );
        return getTypeSorted(js.isNullable(), keyDefault, schema);
    }

    private static JsValue mapOfLongSchema(JsSpec js, JsValue keyDefault) {
        var schema = JsObj.of(TYPE_FIELD, MAP_TYPE,
                              VALUES_FIELD, LONG_TYPE
                             );
        return getTypeSorted(js.isNullable(), keyDefault, schema);

    }

    private static JsValue mapOfIntSchema(JsSpec js, JsValue keyDefault) {
        JsObj schema = JsObj.of(TYPE_FIELD, MAP_TYPE,
                                VALUES_FIELD, INT_TYPE
                               );
        return getTypeSorted(js.isNullable(), keyDefault, schema);

    }

    private static JsValue mapOfDoubleSchema(JsSpec js, JsValue keyDefault) {
        JsObj schema = JsObj.of(TYPE_FIELD, MAP_TYPE,
                                VALUES_FIELD, DOUBLE_TYPE
                               );
        return getTypeSorted(js.isNullable(), keyDefault, schema);

    }

    private static JsValue enumSchema(JsEnum jsEnum, JsValue keyDefault) {
        var metaData = jsEnum.getMetaData();
        if (metaData == null) throw MetadataNotFound.errorParsingEnumToSchema();
        var schema = JsObj.of(TYPE_FIELD, ENUM_TYPE, NAME_FIELD, JsStr.of(metaData.name()));
        if (metaData.doc() != null) schema = schema.set(DOC_FIELD, JsStr.of(metaData.doc()));
        if (metaData.namespace() != null)
            schema = schema.set(Constants.NAMESPACE_FIELD, JsStr.of(metaData.namespace()));
        if (metaData.aliases() != null)
            schema = schema.set(ALIASES_FIELD, JsArray.ofStrs(metaData.aliases()));
        if (metaData.defaultSymbol() != null)
            schema = schema.set(DEFAULT_FIELD, JsArray.ofStrs(metaData.defaultSymbol()));
        schema = schema.set(SYMBOLS_FIELD, jsEnum.getSymbols());
        return getTypeSorted(jsEnum.isNullable(), keyDefault, schema);
    }
}
