package jsonvalues.spec;

import jsonvalues.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class AvroSchemaFromSpec {

    private AvroSchemaFromSpec() {
    }

    //ver si se puede hacer tambien al objeto Schema directamente

    public static JsValue toSchema(final AvroSpec spec) {

        if (spec instanceof JsStrSpec js) return strSchema(js);
        if (spec instanceof JsStrSuchThat js) return strSchema(js);

        if (spec instanceof JsIntSpec js) return intSchema(js);
        if (spec instanceof JsIntSuchThat js) return intSchema(js);

        if (spec instanceof JsLongSpec js) return longSchema(js);
        if (spec instanceof JsLongSuchThat js) return longSchema(js);

        if (spec instanceof JsNumberSpec js) return numberSchema(js);
        if (spec instanceof JsNumberSuchThat js) return numberSchema(js);

        if (spec instanceof JsDoubleSpec js) return doubleSchema(js);
        if (spec instanceof JsDoubleSuchThat js) return doubleSchema(js);

        if (spec instanceof JsDecimalSpec js) return stringSchema("bigdecimal",
                                                                  js.isNullable());
        if (spec instanceof JsDecimalSuchThat js) return stringSchema("bigdecimal",
                                                                      js.isNullable());

        if (spec instanceof JsBigIntSpec js) return stringSchema("biginteger",
                                                                 js.isNullable());
        if (spec instanceof JsBigIntSuchThat js) return stringSchema("biginteger",
                                                                     js.isNullable());

        if (spec instanceof JsBooleanSpec js) return boolSchema(js);
        if (spec instanceof JsBinarySpec js) return binarySchema(js);
        if (spec instanceof JsBinarySuchThat js) return binarySchema(js);

        if (spec instanceof JsFixedBinary js) return fixedSchema(js);

        if (spec instanceof JsArrayOfStrSuchThat js) return arrayOfStringSchema(js);
        if (spec instanceof JsArrayOfStr js) return arrayOfStringSchema(js);
        if (spec instanceof JsArrayOfTestedStr js) return arrayOfStringSchema(js);

        if (spec instanceof JsArrayOfBigIntSuchThat js) return arrayOfBigIntSchema(js);
        if (spec instanceof JsArrayOfBigInt js) return arrayOfBigIntSchema(js);
        if (spec instanceof JsArrayOfTestedBigInt js) return arrayOfBigIntSchema(js);

        if (spec instanceof JsArrayOfIntSuchThat js) return arrayOfIntSchema(js);
        if (spec instanceof JsArrayOfInt js) return arrayOfIntSchema(js);
        if (spec instanceof JsArrayOfTestedInt js) return arrayOfIntSchema(js);


        if (spec instanceof JsArrayOfDoubleSuchThat js) return arrayOfDoubleSchema(js);
        if (spec instanceof JsArrayOfDouble js) return arrayOfDoubleSchema(js);
        if (spec instanceof JsArrayOfTestedDouble js) return arrayOfDoubleSchema(js);

        if (spec instanceof JsArrayOfLongSuchThat js) return arrayOfLongSchema(js);
        if (spec instanceof JsArrayOfLong js) return arrayOfLongSchema(js);
        if (spec instanceof JsArrayOfTestedLong js) return arrayOfLongSchema(js);

        if (spec instanceof JsArrayOfNumberSuchThat js) return arrayOfNumberSchema(js);
        if (spec instanceof JsArrayOfNumber js) return arrayOfNumberSchema(js);
        if (spec instanceof JsArrayOfTestedNumber js) return arrayOfNumberSchema(js);

        if (spec instanceof JsArrayOfDecimal js) return arrayOfDecimalSchema(js);
        if (spec instanceof JsArrayOfTestedDecimal js) return arrayOfDecimalSchema(js);
        if (spec instanceof JsArrayOfDecimalSuchThat js) return arrayOfDecimalSchema(js);

        if (spec instanceof JsArrayOfBool js) return arrayOfBooleanSchema(js);
        if (spec instanceof JsArrayOfBoolSuchThat js) return arrayOfBooleanSchema(js);

        if (spec instanceof JsInstantSpec js) return instantSchema(js);
        if (spec instanceof JsInstantSuchThat js) return instantSchema(js);

        if (spec instanceof JsArrayOfObjSpec js) return arrayOfObjSpecSchema(js);
        if (spec instanceof JsObjSpec js) return objSpecSchema(js);

        if (spec instanceof JsMapOfInt js) return mapOfIntSchema(js);
        if (spec instanceof JsMapOfDouble js) return mapOfDoubleSchema(js);
        if (spec instanceof JsMapOfLong js) return mapOfLongSchema(js);
        if (spec instanceof JsMapOfBool js) return mapOfBoolSchema(js);
        if (spec instanceof JsMapOfBigInt js) return mapOfBigIntegerSchema(js);
        if (spec instanceof JsMapOfObjSpec js) return mapOfObjSpecSchema(js);
        if (spec instanceof JsMapOfStr js) return mapOfStrSchema(js);
        if (spec instanceof JsMapOfDec js) return mapOfDecSchema(js);
        if (spec instanceof JsMapOfInstant js) return mapOfInstantSchema(js);
        if (spec instanceof JsMapOfArraySpec js) return mapOfArraySpecSchema(js);

        if (spec instanceof JsEnum js) return enumSchema(js);
        if (spec instanceof OneOf js) return oneOfSchema(js);
        if (spec instanceof OneOfObjSpec js) return oneOfObjSpecSchema(js);

        throw new IllegalArgumentException("Spec " + spec.getClass().getName() + " not supported yet!");

    }

    private static JsArray oneOfObjSpecSchema(OneOfObjSpec js) {
        var specs = js.getSpecs();
        JsArray schema = JsArray.ofIterable(specs.stream().map(AvroSchemaFromSpec::toSchema).toList());
        return js.isNullable() ? schema.prepend(JsStr.of("null")) : schema;
    }

    private static JsArray oneOfSchema(OneOf js) {
        var specs = js.getSpecs();
        List<JsValue> avroSchemas = new ArrayList<>();

        for (int i = 0; i < specs.size(); i++) {
            JsSpec spec = specs.get(i);
            if (spec instanceof AvroSpec avroSpec) {
                avroSchemas.add(toSchema(avroSpec));
            } else {
                throw SpecNotSupportedInAvro.errorConvertingOneOfIntoSchema(spec, i);
            }
        }
        JsArray schema = JsArray.ofIterable(avroSchemas);
        validateNotDuplicatedTypes(schema);

        if (js.isNullable())
            if(schema.containsValue(JsStr.of("null"))) return schema;
            else return schema.prepend(JsStr.of("null"));
        else return schema;
    }

    private static void validateNotDuplicatedTypes(JsArray schema) {

    }

    private static JsValue mapOfArraySpecSchema(JsMapOfArraySpec js) {
        var valueSpec = js.getSpec();
        if (valueSpec instanceof AvroSpec avroSpec) {
            JsObj schema = JsObj.of("type", JsStr.of("map"),
                                    "vales", toSchema(avroSpec));
            return js.isNullable() ?
                    JsArray.of(JsStr.of("null"), schema) :
                    schema;
        }
        throw SpecNotSupportedInAvro.errorConvertingMapOfArraySpecIntoSchema(valueSpec);
    }

    private static JsValue mapOfObjSpecSchema(JsMapOfObjSpec js) {
        JsObj schema = JsObj.of("type", JsStr.of("map"),
                                "vales", toSchema(js.getSpec()));
        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private static JsValue objSpecSchema(JsObjSpec objSpec) {
        var metadata = objSpec.getMetaData();

        if (metadata == null) throw MetadataNotFound.errorParsingJsObSpecToSchema();
        var bindings = objSpec.getBindings();
        var schema = JsObj.of("name",
                              JsStr.of(metadata.name()),
                              "type", JsStr.of("record"));
        if (metadata.namespace() != null)
            schema = schema.set("namespace",
                                JsStr.of(metadata.namespace()));
        if (metadata.doc() != null)
            schema = schema.set("doc",
                                JsStr.of(metadata.doc()));
        if (metadata.aliases() != null)
            schema = schema.set("aliases",
                                JsArray.ofStrs(metadata.aliases()));
        var fields = JsArray.empty();

        var fieldsDoc = metadata.fieldsDoc();
        var fieldsAliases = metadata.fieldsAliases();
        var fieldsOrder = metadata.fieldsOrder();
        var fieldsDefault = metadata.fieldsDefault();

        for (Map.Entry<String, JsSpec> entry : bindings.entrySet()) {
            var spec = entry.getValue();
            var key = entry.getKey();
            if (spec instanceof AvroSpec avroSpec) {
                var fieldSchema = JsObj.of("name", JsStr.of(key),
                                           "type", toAvro(key,
                                                          objSpec.getRequiredFields(),
                                                          avroSpec,
                                                          spec.isNullable())
                                          );
                if (fieldsDoc != null && fieldsDoc.containsKey(key)) fieldSchema.set("doc", fieldsDoc.get(key));
                if (fieldsOrder != null && fieldsOrder.containsKey(key))
                    fieldSchema.set("order", fieldsOrder.get(key).name());
                if (fieldsDefault != null && fieldsDefault.containsKey(key))
                    fieldSchema.set("default", fieldsDefault.get(key));
                if (fieldsAliases != null && fieldsAliases.containsKey(key))
                    fieldSchema.set("aliases", JsArray.ofStrs(fieldsAliases.get(key)));

                fields = fields.append(fieldSchema);
            } else throw SpecNotSupportedInAvro.errorConvertingObjSpecIntoSchema(key, spec, metadata);
        }

        schema = schema.set("fields", fields);

        return objSpec.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private static JsValue toAvro(String key,
                                  List<String> requiredFields,
                                  AvroSpec spec,
                                  boolean isNullable
                                 ) {
        JsValue schema = toSchema(spec);
        if(isNullable || !requiredFields.contains(key)){
            if(schema instanceof JsArray arrSchema) return arrSchema.prepend(JsStr.of("null"));
            else return JsArray.of(JsStr.of("null"),schema);
        }
        return schema;

    }

    private static JsValue arrayOfObjSpecSchema(JsArrayOfObjSpec spec) {
        JsValue items = toSchema(spec);

        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", items);

        return spec.isNullable() ?
                JsArray.of(JsStr.of("null"),
                           schema) :
                schema;
    }

    private static JsValue arrayOfStringSchema(JsSpec js) {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("string"));

        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private static JsValue arrayOfBooleanSchema(JsSpec js) {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("boolean"));

        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private static JsValue arrayOfDecimalSchema(JsSpec js) {
        return containerOfLogicalType("bigdecimal", "array", "items", js.isNullable());
    }

    //TODO incluir yo creo bigdecimal con el tipo string y logical type
    private static JsValue arrayOfNumberSchema(JsSpec js) {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsArray.of("int", "long", "double"));

        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private static JsValue arrayOfLongSchema(JsSpec js) {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("long"));

        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private static JsValue binarySchema(JsSpec spec) {
        return spec.isNullable() ? JsArray.of("null", "bytes") : JsStr.of("bytes");

    }

    private static JsValue instantSchema(JsSpec spec) {
        JsObj schema = JsObj.of("type", JsStr.of("string"),
                                "logicalType", JsStr.of("iso-8601"));
        return spec.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }


    private static JsValue strSchema(JsSpec js) {
        return js.isNullable() ?
                JsArray.of("null", "string") :
                JsStr.of("string");
    }

    private static JsValue boolSchema(JsSpec js) {
        return js.isNullable() ?
                JsArray.of("null", "boolean") :
                JsStr.of("boolean");
    }

    private static JsValue stringSchema(String logicalType, boolean nullable) {
        JsObj schema = JsObj.of("type", JsStr.of("string"),
                                "logicalType", JsStr.of(logicalType)
                               );
        return nullable ?
                JsArray.of(JsStr.of("null"), schema) :
                schema;
    }

    private static JsValue longSchema(JsSpec js) {
        return js.isNullable() ?
                JsArray.of("null", "long") :
                JsStr.of("long");
    }

    private static JsValue intSchema(JsSpec js) {
        return js.isNullable() ?
                JsArray.of("null", "int") :
                JsStr.of("int");
    }

    private static JsValue doubleSchema(JsSpec js) {
        return js.isNullable() ?
                JsArray.of("null", "double") :
                JsStr.of("double");
    }

    private static JsValue fixedSchema(JsFixedBinary js) {
        var metadata = js.getMetaData();
        if (metadata == null) throw MetadataNotFound.errorParsingFixedToSchema();
        var schema = JsObj.of("name",
                              JsStr.of(metadata.name()),
                              "type", JsStr.of("fixed"),
                              "size", JsInt.of(js.getSize()));
        if (metadata.namespace() != null)
            schema = schema.set("namespace",
                                JsStr.of(metadata.namespace()));

        if (metadata.aliases() != null)
            schema = schema.set("aliases",
                                JsArray.ofStrs(metadata.aliases()));


        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private static JsValue arrayOfIntSchema(JsSpec js) {
        var schema = JsObj.of("type", JsStr.of("array"),
                              "items", JsStr.of("int"));

        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private static JsValue arrayOfDoubleSchema(JsSpec js) {
        var schema = JsObj.of("type", JsStr.of("array"),
                              "items", JsStr.of("double"));

        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private static JsValue arrayOfBigIntSchema(JsSpec js) {
        return containerOfLogicalType("biginteger", "array", "items", js.isNullable());
    }

    private static JsArray numberSchema(JsSpec js) {
        var decSchema = JsObj.of("type", JsStr.of("string"),
                                 "logicalType", JsStr.of("bigdecimal"));
        return js.isNullable() ?
                JsArray.of("null", "double", "int", "long").append(decSchema) :
                JsArray.of("double", "int", "long").append(decSchema);
    }

    private static JsValue mapOfInstantSchema(JsMapOfInstant js) {
        return containerOfLogicalType("iso-8601", "map", "values", js.isNullable());
    }

    private static JsValue mapOfDecSchema(JsMapOfDec js) {
        return containerOfLogicalType("bigdecimal", "map", "values", js.isNullable());
    }

    private static JsValue containerOfLogicalType(String logicalType,
                                                  String containerType,
                                                  String elementsKey,
                                                  boolean isNullable
                                                 ) {
        var schema = JsObj.of("type", JsStr.of("string"),
                              "logicalType", JsStr.of(logicalType));
        var mapSchema = JsObj.of("type", JsStr.of(containerType),
                                 elementsKey, schema
                                );
        return isNullable ?
                JsArray.of(JsStr.of("null"), mapSchema) :
                mapSchema;
    }

    private static JsValue mapOfStrSchema(JsMapOfStr js) {
        var mapSchema = JsObj.of("type", JsStr.of("map"),
                                 "values", JsStr.of("string"));
        return js.isNullable() ?
                JsArray.of(JsStr.of("null"), mapSchema) :
                mapSchema;
    }

    private static JsValue mapOfBigIntegerSchema(JsMapOfBigInt js) {
        return containerOfLogicalType("biginteger", "map", "values", js.isNullable());
    }

    private static JsValue mapOfBoolSchema(JsMapOfBool js) {
        var mapSchema = JsObj.of("type", JsStr.of("map"),
                                 "values", JsStr.of("boolean")
                                );
        return js.isNullable() ?
                JsArray.of(JsStr.of("null"), mapSchema) :
                mapSchema;
    }

    private static JsValue mapOfLongSchema(JsMapOfLong js) {
        var mapSchema = JsObj.of("type", JsStr.of("map"),
                                 "values", JsStr.of("long")
                                );
        return js.isNullable() ?
                JsArray.of(JsStr.of("null"), mapSchema) :
                mapSchema;
    }

    private static JsValue mapOfIntSchema(JsMapOfInt js) {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"),
                                   "values", JsStr.of("int")
                                  );
        return js.isNullable() ?
                JsArray.of(JsStr.of("null"), mapSchema) :
                mapSchema;
    }

    private static JsValue mapOfDoubleSchema(JsMapOfDouble js) {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"),
                                   "values", JsStr.of("double")
                                  );
        return js.isNullable() ?
                JsArray.of(JsStr.of("null"), mapSchema) :
                mapSchema;
    }

    private static JsValue enumSchema(JsEnum jsEnum) {
        var metaData = jsEnum.getMetaData();
        if (metaData == null) throw MetadataNotFound.errorParsingEnumToSchema();
        var schema = JsObj.of("type", JsStr.of("enum"), "name", JsStr.of(metaData.name()));
        if (metaData.doc() != null) schema = schema.set("doc", JsStr.of(metaData.doc()));
        if (metaData.namespace() != null) schema = schema.set("namespace", JsStr.of(metaData.namespace()));
        if (metaData.aliases() != null) schema = schema.set("aliases", JsArray.ofStrs(metaData.aliases()));
        if (metaData.defaultSymbol() != null) schema = schema.set("default", JsArray.ofStrs(metaData.defaultSymbol()));
        schema = schema.set("symbols", jsEnum.getSymbols());
        return jsEnum.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }
}
