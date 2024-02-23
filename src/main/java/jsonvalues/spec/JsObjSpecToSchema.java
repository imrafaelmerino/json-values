package jsonvalues.spec;

import java.util.Map.Entry;
import java.util.Set;
import jsonvalues.JsArray;
import jsonvalues.JsBool;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.JsValue;

public final class JsObjSpecToSchema {

  private JsObjSpecToSchema() {
  }

  public static JsObj convert(final JsObjSpec jsObjSpec) {
    return JsObj.of("type",
                    jsObjSpec.nullable ? JsArray.of(JsStr.of("object"),
                                                    JsStr.of("null")
                                                   ) : JsStr.of("object"),
                    "properties",
                    getProperties(jsObjSpec),
                    "additionalProperties",
                    JsBool.of(!jsObjSpec.strict),
                    "required",
                    getRequired(jsObjSpec)
                   );

  }

  private static JsArray getRequired(final JsObjSpec jsObjSpec) {
    return JsArray.ofStrs(jsObjSpec.requiredFields);
  }

  private static JsObj getProperties(final JsObjSpec jsObjSpec) {
    JsObj properties = JsObj.empty();
    Set<Entry<String, JsSpec>> entries = jsObjSpec.bindings.entrySet();
    for (Entry<String, JsSpec> entry : entries) {
      properties = properties.set(entry.getKey(),
                                  getSchema(entry.getValue()));
    }
    return properties;
  }


  private static JsValue getSchema(final JsSpec spec) {

    return switch (spec) {
      case JsIntSpec s -> getIntSchema(s);
      case JsIntSuchThat s -> getIntSchema(s);

      case JsLongSpec s -> getLongSchema(s);
      case JsLongSuchThat s -> getLongSchema(s);

      case JsBigIntSpec s -> getIntSchema(s);
      case JsBigIntSuchThat s -> getIntSchema(s);

      case JsDoubleSpec s -> getNumberSchema(s);
      case JsDoubleSuchThat s -> getNumberSchema(s);

      case JsDecimalSpec s -> getNumberSchema(s);
      case JsDecimalSuchThat s -> getNumberSchema(s);

      case JsBooleanSpec s -> getBoolSchema(s);

      case JsStrSpec s -> getStrSchema(s);
      case JsStrSuchThat s -> getStrSchema(s);

      case JsInstantSpec s -> getInstantSchema(s);
      case JsInstantSuchThat s -> getInstantSchema(s);

      case JsBinarySpec s -> getBinarySchema(s);
      case JsBinarySuchThat s -> getBinarySchema(s);
      case JsFixedBinary s -> getBinarySchema(s);

      case JsEnum jsEnum -> getEnumSchema(jsEnum);

      case AnySpec ignored -> getAnySpec();
      case AnySuchThat ignored -> getAnySpec();

      case JsArraySpec s -> getArraySchema(s);
      case JsObjSpec s -> convert(s);

      case IsJsObj ignored -> getObjType();
      case JsObjSuchThat ignored -> getObjType();

      case JsMapOfBigInt ignored -> getMapOfIntegerSchema();
      case JsMapOfInt ignored -> getMapOfIntegerSchema();
      case JsMapOfLong ignored -> getMapOfIntegerSchema();
      case JsMapOfDouble ignored -> getMapOfNumberSchema();
      case JsMapOfDec ignored -> getMapOfNumberSchema();
      case JsMapOfBinary ignored -> getMapOfBinarySpec();
      case JsMapOfBool ignored -> getMapOfBoolSpec();
      case JsMapOfInstant ignored -> getMapOfInstantSpec();
      case JsMapOfSpec s -> getMapOfSpec(s);
      case JsMapOfStr ignored -> getMapOfSpec();

      case NamedSpec namedSpec -> {
        //TODO, coger de la cache,meter en definitions el schema y hacer referencia a el
        yield null;
      }
      case OneOf oneOf -> getOneOf(oneOf);
    };

  }

  private static JsObj getOneOf(final OneOf oneOf) {
    return JsObj.of("oneOf",
                    JsArray.ofIterable(oneOf.specs.stream()
                                                  .map(JsObjSpecToSchema::getSchema)
                                                  .toList())
                   );
  }

  private static JsObj getMapOfSpec() {
    return JsObj.of("type",
                    JsStr.of("object"),
                    "additionalProperties",
                    JsObj.of("type",
                             JsStr.of("string")
                            )
                   );
  }

  private static JsObj getMapOfSpec(final JsMapOfSpec jsMapOfSpec) {
    return JsObj.of("type",
                    JsStr.of("object"),
                    "additionalProperties",
                    getSchema(jsMapOfSpec.getValueSpec())
                   );
  }

  private static JsObj getMapOfInstantSpec() {
    return JsObj.of("type",
                    JsStr.of("object"),
                    "additionalProperties",
                    JsObj.of("type",
                             JsStr.of("string"),
                             "format",
                             JsStr.of("date-time")
                            )
                   );
  }

  private static JsObj getMapOfBoolSpec() {
    return JsObj.of("type",
                    JsStr.of("object"),
                    "additionalProperties",
                    JsObj.of("type",
                             JsStr.of("boolean")
                            )
                   );
  }

  private static JsObj getMapOfBinarySpec() {
    return JsObj.of("type",
                    JsStr.of("object"),
                    "additionalProperties",
                    JsObj.of("type",
                             JsStr.of("string"),
                             "contentEncoding",
                             JsStr.of("base64")
                            )
                   );
  }

  private static JsObj getMapOfIntegerSchema() {
    return JsObj.of("type",
                    JsStr.of("object"),
                    "additionalProperties",
                    JsObj.of("type",
                             JsStr.of("integer")
                            )
                   );
  }

  private static JsObj getMapOfNumberSchema() {
    return JsObj.of("type",
                    JsStr.of("object"),
                    "additionalProperties",
                    JsObj.of("type",
                             JsStr.of("number")
                            )
                   );
  }

  private static JsObj getObjType() {
    return JsObj.of("type",
                    JsStr.of("object")
                   );
  }


  private static JsObj getArraySchema(final JsArraySpec s) {
    if (s.isNullable()) {
      return JsObj.of("type",
                      JsArray.of(JsStr.of("array"),
                                 JsStr.of("null")
                                ),
                      "items",
                      getSchemaOfArrayOfSpec(s));
    }
    return JsObj.of("type",
                    JsStr.of("array"),
                    "items",
                    getSchemaOfArrayOfSpec(s));
  }

  private static JsObj getAnySpec() {
    return JsObj.of("type",
                    JsStr.of("*")
                   );
  }

  private static JsObj getEnumSchema(final JsEnum jsEnum) {
    if (jsEnum.isNullable()) {
      return JsObj.of("type",
                      JsArray.of(JsStr.of("string"),
                                 JsStr.of("null")
                                ),
                      "enum",
                      jsEnum.symbols
                     );
    }
    return JsObj.of("type",
                    JsStr.of("string"),
                    "enum",
                    jsEnum.symbols);
  }

  private static JsObj getBinarySchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of("type",
                      JsArray.of(JsStr.of("string"),
                                 JsStr.of("null")
                                ),
                      "contentEncoding",
                      JsStr.of("base64")
                     );
    }
    return JsObj.of("type",
                    JsStr.of("string"),
                    "contentEncoding",
                    JsStr.of("base64"));
  }

  private static JsObj getInstantSchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of("type",
                      JsArray.of(JsStr.of("string"),
                                 JsStr.of("null")
                                ),
                      "format",
                      JsStr.of("date-time"));
    }
    return JsObj.of("type",
                    JsStr.of("string"),
                    "format",
                    JsStr.of("date-time"));
  }

  private static JsObj getStrSchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of("type",
                      JsArray.of(JsStr.of("string"),
                                 JsStr.of("null")
                                )
                     );
    }
    return JsObj.of("type",
                    JsStr.of("string"));
  }

  private static JsObj getBoolSchema(final JsBooleanSpec s) {
    if (s.isNullable()) {
      return JsObj.of("type",
                      JsArray.of(JsStr.of("boolean"),
                                 JsStr.of("null")
                                )
                     );
    }
    return JsObj.of("type",
                    JsStr.of("boolean"));
  }

  private static JsObj getNumberSchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of("type",
                      JsArray.of(JsStr.of("number"),
                                 JsStr.of("null")
                                )
                     );
    }
    return JsObj.of("type",
                    JsStr.of("number")
                   );
  }


  private static JsObj getIntSchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of("type",
                      JsArray.of(JsStr.of("integer"),
                                 JsStr.of("null")
                                ));
    }
    return JsObj.of("type",
                    JsStr.of("integer"));
  }

  private static JsObj getLongSchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of("type",
                      JsArray.of(JsStr.of("integer"),
                                 JsStr.of("null")
                                )
                     );
    }
    return JsObj.of("type",
                    JsStr.of("integer"));
  }


  private static JsValue getSchemaOfArrayOfSpec(final JsArraySpec spec) {

    return switch (spec) {
      case JsArrayOfInt s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfTestedInt s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfIntSuchThat s -> getArrayOfIntSchema(s);

      case JsArrayOfLong s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfTestedLong s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfLongSuchThat s -> getArrayOfIntSchema(s);

      case JsArrayOfBigInt s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfTestedBigInt s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfBigIntSuchThat s -> getArrayOfIntSchema(s);

      case JsArrayOfDouble s -> getSizableArrayOfNumberSchema(s);
      case JsArrayOfTestedDouble s -> getSizableArrayOfNumberSchema(s);
      case JsArrayOfDoubleSuchThat s -> getArrayOfNumberSchema(s);

      case JsArrayOfDecimal s -> getSizableArrayOfNumberSchema(s);
      case JsArrayOfTestedDecimal s -> getSizableArrayOfNumberSchema(s);
      case JsArrayOfDecimalSuchThat s -> getArrayOfNumberSchema(s);

      case JsArrayOfBool s -> getSizableArrayOfBoolSchema(s);
      case JsArrayOfBoolSuchThat s -> getArrayOfBoolSchema(s);

      case JsArrayOfStr s -> getSizableArrayOfStrSchema(s);
      case JsArrayOfStrSuchThat s -> getArrayOfStrSchema(s);
      case JsArrayOfTestedStr s -> getSizableArrayOfStrSchema(s);

      case JsArrayOfValue s -> getSizableArrayOfValueSchema(s);
      case JsArrayOfTestedValue s -> getSizableArrayOfValueSchema(s);
      case JsArraySuchThat s -> getArrayOfValueSchema(s);

      case JsArrayOfObj s -> getSizableArrayOfObjSchema(s);
      case JsArrayOfObjSuchThat s -> getArrayOfObjSchema(s);
      case JsArrayOfTestedObj s -> getSizableArrayOfObjSchema(s);

      case JsArrayOfSpec s -> getSizableArrayOfSpecSchema(s);
      case JsTuple jsTuple -> getArrayOfTupleSchema(jsTuple);
    };
  }

  private static JsValue getArrayOfObjSchema(final JsArraySpec s) {
    return getArraySchema(s,
                          JsObj.of("type",
                                   JsStr.of("object")
                                  ));
  }

  private static JsValue getArrayOfValueSchema(final JsArraySpec s) {
    return getArraySchema(s,
                          JsObj.of("type",
                                   JsStr.of("*")
                                  ));
  }

  private static JsValue getArrayOfStrSchema(final JsSpec s) {
    return getArraySchema((JsArraySpec) s,
                          JsObj.of("type",
                                   JsStr.of("string")
                                  ));
  }

  private static JsValue getArrayOfBoolSchema(final JsArraySpec s) {
    return getArraySchema(s,
                          JsObj.of("type",
                                   JsStr.of("boolean")
                                  ));
  }

  private static JsValue getArrayOfNumberSchema(final JsArraySpec s) {
    return getArraySchema(s,
                          JsObj.of("type",
                                   JsStr.of("number")
                                  ));
  }

  private static JsValue getArrayOfIntSchema(final JsArraySpec s) {
    return getArraySchema(s,
                          JsObj.of("type",
                                   JsStr.of("integer")
                                  ));
  }

  private static JsValue getArrayOfTupleSchema(final JsTuple jsTuple) {
    return JsObj.of("type",
                    jsTuple.isNullable() ?
                    JsArray.of(JsStr.of("array"),
                               JsStr.of("null")
                              ) :
                    JsStr.of("array"),
                    "items",
                    JsArray.ofIterable(jsTuple.specs.stream()
                                                    .map(JsObjSpecToSchema::getSchema)
                                                    .toList()
                                      ),
                    "additionalItems",
                    JsBool.FALSE
                   );
  }

  private static JsValue getSizableArrayOfSpecSchema(final JsArrayOfSpec s) {
    return getSizableArraySchema(s,
                                 JsObj.of("type",
                                          getSchema(s.getElemSpec())
                                         ));

  }

  private static JsValue getSizableArrayOfObjSchema(final AbstractSizableArr jsArrayOfObj) {
    return getSizableArraySchema(jsArrayOfObj,
                                 JsObj.of("type",
                                          JsStr.of("object")
                                         ));
  }

  private static JsValue getSizableArrayOfValueSchema(AbstractSizableArr spec) {
    return getSizableArraySchema(spec,
                                 JsObj.of("type",
                                          JsStr.of("*")
                                         ));
  }


  private static JsValue getSizableArrayOfStrSchema(AbstractSizableArr spec) {
    return getSizableArraySchema(spec,
                                 JsObj.of("type",
                                          JsStr.of("string")
                                         ));
  }

  private static JsValue getSizableArrayOfBoolSchema(AbstractSizableArr spec) {
    return getSizableArraySchema(spec,
                                 JsObj.of("type",
                                          JsStr.of("boolean")
                                         ));
  }

  private static JsObj getSizableArraySchema(AbstractSizableArr spec,
                                             JsObj elemSchema) {

    JsObj schema = JsObj.of("type",
                            spec.isNullable() ?
                            JsArray.of(JsStr.of("array"),
                                       JsStr.of("null")
                                      ) :
                            JsStr.of("array"),
                            "items",
                            elemSchema);
    var minElems = spec.min;
    var maxElems = spec.max;
    if (minElems > 0) {
      schema = schema.set("minItems",
                          JsInt.of(minElems));
    }
    if (maxElems < Integer.MAX_VALUE) {
      schema = schema.set("maxItems",
                          JsInt.of(maxElems));
    }

    return schema;
  }

  private static JsObj getArraySchema(JsArraySpec spec,
                                      JsObj elemSchema) {

    return JsObj.of("type",
                    spec.isNullable() ?
                    JsArray.of(JsStr.of("array"),
                               JsStr.of("null")
                              ) :
                    JsStr.of("array"),
                    "items",
                    elemSchema);

  }

  private static JsObj getSizableArrayOfIntSchema(AbstractSizableArr spec) {

    return getSizableArraySchema(spec,
                                 JsObj.of("type",
                                          JsStr.of("integer")
                                         ));
  }

  private static JsObj getSizableArrayOfNumberSchema(AbstractSizableArr spec) {
    return getSizableArraySchema(spec,
                                 JsObj.of("type",
                                          JsStr.of("number")
                                         ));
  }
}