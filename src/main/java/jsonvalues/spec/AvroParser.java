package jsonvalues.spec;

import jsonvalues.*;

import java.util.List;
import java.util.Map;

public final class AvroParser {

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

    private static JsValue fixexSchema(JsFixedBinarySpec js, AvroAttBuilder avroAttBuilder, int size) {
        if (avroAttBuilder == null)
            throw new IllegalArgumentException("avroAttBuilder is null. Set one with `withAvroAtt(builder)`");
        AvroAtt avroAtt = avroAttBuilder.build();
        JsObj schema = JsObj.of("name",
                                JsStr.of(avroAtt.name),
                                "type", JsStr.of("fixed"),
                                "size", JsInt.of(size));
        if (avroAtt.namespace != null)
            schema = schema.set("namespace",
                                JsStr.of(avroAtt.namespace));

        if (avroAtt.aliases != null)
            schema = schema.set("aliases",
                                avroAtt.aliases);


        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private static JsValue arrayOfIntSchema(JsSpec js) {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("int"));

        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private static JsValue arrayOfBigIntSchema(JsSpec js) {
        return containerOfLogicalType("biginteger", "array", "items", js.isNullable());
    }

    private static JsArray numberSchema(JsSpec js) {
        JsObj decSchema = JsObj.of("type", JsStr.of("string"),
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
        JsObj schema = JsObj.of("type", JsStr.of("string"),
                                "logicalType", JsStr.of(logicalType));
        JsObj mapSchema = JsObj.of("type", JsStr.of(containerType),
                                   elementsKey, schema
                                  );
        return isNullable ?
                JsArray.of(JsStr.of("null"), mapSchema) :
                mapSchema;
    }

    private static JsValue mapOfStrSchema(JsMapOfStr js) {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"),
                                   "values", JsStr.of("string"));
        return js.isNullable() ?
                JsArray.of(JsStr.of("null"), mapSchema) :
                mapSchema;
    }

    private static JsValue mapOfBigIntegerSchema(JsMapOfBigInt js) {
        return containerOfLogicalType("biginteger", "map", "values", js.isNullable());
    }

    private static JsValue mapOfBoolSchema(JsMapOfBool js) {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"),
                                   "values", JsStr.of("boolean")
                                  );
        return js.isNullable() ?
                JsArray.of(JsStr.of("null"), mapSchema) :
                mapSchema;
    }

    private static JsValue mapOfLongSchema(JsMapOfLong js) {
        JsObj mapSchema = JsObj.of("type", JsStr.of("map"),
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

    private static JsValue enumSchema(JsEnum js) {
        var avroAttBuilder = js.getAvroAttBuilder();
        if (avroAttBuilder == null)
            throw new IllegalArgumentException("avroAttBuilder is null in JsEnum. Set one with `withAvroAtt(builder)`");
        AvroAtt avroAtt = avroAttBuilder.build();
        JsObj schema = JsObj.of("name", JsStr.of(avroAtt.name));
        if (avroAtt.doc != null) schema = schema.set("doc", JsStr.of(avroAtt.doc));
        if (avroAtt.aliases != null) schema = schema.set("aliases", avroAtt.aliases);
        schema = schema.set("symbols", js.getSymbols());
        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    public JsValue toAvroSchema(AvroSpec spec) {

        if (spec instanceof JsStrSpec js) return strSchema(js);
        if (spec instanceof JsStrSuchThat js) return strSchema(js);

        if (spec instanceof JsIntSpec js) return intSchema(js);
        if (spec instanceof JsIntSuchThat js) return intSchema(js);

        if (spec instanceof JsLongSpec js) return longSchema(js);
        if (spec instanceof JsLongSuchThat js) return longSchema(js);

        if (spec instanceof JsNumberSpec js) return numberSchema(js);
        if (spec instanceof JsNumberSuchThat js) return numberSchema(js);


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

        if (spec instanceof JsFixedBinarySpec js) return fixexSchema(js,
                                                                     js.getAvroAttBuilder(), js.getSize());

        if (spec instanceof JsArrayOfStrSuchThat js) return arrayOfStringSchema(js);
        if (spec instanceof JsArrayOfStr js) return arrayOfStringSchema(js);
        if (spec instanceof JsArrayOfTestedStr js) return arrayOfStringSchema(js);

        if (spec instanceof JsArrayOfBigIntSuchThat js) return arrayOfBigIntSchema(js);
        if (spec instanceof JsArrayOfBigInt js) return arrayOfBigIntSchema(js);
        if (spec instanceof JsArrayOfTestedBigInt js) return arrayOfBigIntSchema(js);

        if (spec instanceof JsArrayOfIntSuchThat js) return arrayOfIntSchema(js);
        if (spec instanceof JsArrayOfInt js) return arrayOfIntSchema(js);
        if (spec instanceof JsArrayOfTestedInt js) return arrayOfIntSchema(js);

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

    private JsArray oneOfObjSpecSchema(OneOfObjSpec js) {
        var specs = js.getSpecs();
        JsArray schema = JsArray.ofIterable(specs.stream().map(this::toAvroSchema).toList());
        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private JsArray oneOfSchema(OneOf js) {
        var specs = js.getSpecs();
        JsArray schema =
                JsArray.ofIterable(specs.stream()
                                        .map(x -> {
                                            if (x instanceof AvroSpec avroSpec) return toAvroSchema(avroSpec);
                                            else throw new SpecNotSupportedInAvro(x);
                                        }).toList());
        return js.isNullable() ?
                JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private JsValue mapOfArraySpecSchema(JsMapOfArraySpec js) {
        var valueSpec = js.getSpec();
        if (valueSpec instanceof AvroSpec avroSpec) {
            JsObj schema = JsObj.of("type", JsStr.of("map"),
                                    "vales", toAvroSchema(avroSpec));
            return js.isNullable() ?
                    JsArray.of(JsStr.of("null"), schema) :
                    schema;
        }
        throw new SpecNotSupportedInAvro(valueSpec);
    }

    private JsValue mapOfObjSpecSchema(JsMapOfObjSpec js) {
        JsObj schema = JsObj.of("type", JsStr.of("map"),
                                "vales", toAvroSchema(js.getSpec()));
        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private JsValue objSpecSchema(JsObjSpec objSpec) {
        var avroAttBuilder = objSpec.getAvroAttBuilder();
        var bindings = objSpec.getBindings();

        if (avroAttBuilder == null)
            throw new IllegalArgumentException("avroAttBuilder is null. Set one with `withAvroAtt(builder)`");
        AvroAtt avroAtt = avroAttBuilder.build();
        JsObj schema = JsObj.of("name",
                                JsStr.of(avroAtt.name),
                                "type", JsStr.of("record"));
        if (avroAtt.namespace != null)
            schema = schema.set("namespace",
                                JsStr.of(avroAtt.namespace));
        if (avroAtt.doc != null)
            schema = schema.set("doc",
                                JsStr.of(avroAtt.doc));
        if (avroAtt.aliases != null)
            schema = schema.set("aliases",
                                avroAtt.aliases);
        JsArray fields = JsArray.empty();
        for (Map.Entry<String, JsSpec> entry : bindings.entrySet()) {
            JsSpec spec = entry.getValue();
            if (spec instanceof AvroSpec avroSpec)
                fields = fields.append(JsObj.of("name", JsStr.of(entry.getKey()),
                                                "type", toAvro(entry.getKey(),
                                                               objSpec.getRequiredFields(),
                                                               avroSpec,
                                                               spec.isNullable())
                                               )
                                      );
            else throw new SpecNotSupportedInAvro(spec, avroAtt);
        }

        schema = schema.set("fields", fields);

        return objSpec.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private JsValue toAvro(String key, List<String> requiredFields, AvroSpec spec, boolean isNullable) {
        JsValue schema = toAvroSchema(spec);
        return (isNullable || requiredFields.contains(key)) ?
                schema :
                JsArray.of(JsStr.of("null"), schema);
    }

    private JsValue arrayOfObjSpecSchema(JsArrayOfObjSpec spec) {
        JsValue items = toAvroSchema(spec);

        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", items);

        return spec.isNullable() ?
                JsArray.of(JsStr.of("null"),
                           schema) :
                schema;
    }

    private JsValue arrayOfStringSchema(JsSpec js) {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("string"));

        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private JsValue arrayOfBooleanSchema(JsSpec js) {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("boolean"));

        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private JsValue arrayOfDecimalSchema(JsSpec js) {
        return containerOfLogicalType("bigdecimal", "array", "items", js.isNullable());
    }

    //TODO incluir yo creo bigdecimal con el tipo string y logical type
    private JsValue arrayOfNumberSchema(JsSpec js) {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsArray.of("int", "long", "double"));

        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private JsValue arrayOfLongSchema(JsSpec js) {
        JsObj schema = JsObj.of("type", JsStr.of("array"),
                                "items", JsStr.of("long"));

        return js.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }

    private JsValue binarySchema(JsSpec spec) {
        return spec.isNullable() ? JsArray.of("null", "bytes") : JsStr.of("bytes");

    }

    private JsValue instantSchema(JsSpec spec) {
        JsObj schema = JsObj.of("type", JsStr.of("string"),
                                "logicalType", JsStr.of("iso-8601"));
        return spec.isNullable() ? JsArray.of(JsStr.of("null"), schema) : schema;
    }
}
