package jsonvalues.spec;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import jsonvalues.JsArray;
import jsonvalues.JsBool;
import jsonvalues.JsInt;
import jsonvalues.JsNothing;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.spec.StrSchema.BUILT_INT_FORMAT;

public final class JsObjSpecToSchema {

  private static final String TYPE = "type";
  private static final String STAR = "*";
  private static final String OBJECT = "object";
  private static final String STRING = "string";
  private static final String INTEGER = "integer";
  private static final String NUMBER = "number";
  private static final String BOOLEAN = "boolean";
  private static final String NULL = "null";
  private static final String PROPERTIES = "properties";
  private static final String ADDITIONAL_PROPERTIES = "additionalProperties";
  private static final String REQUIRED = "required";
  private static final String DEFINITIONS = "definitions";
  private static final String REF = "$ref";
  private static final String ENUM = "enum";
  private static final String ONE_OF = "oneOf";
  private static final String ARRAY = "array";
  private static final String ITEMS = "items";
  private static final String MIN_ITEMS = "minItems";
  private static final String MAX_ITEMS = "maxItems";
  private static final String ADDITIONAL_ITEMS = "additionalItems";
  private static final String CONTENT_ENCODING = "contentEncoding";
  private static final String MIN_LENGTH = "minLength";
  private static final String MAX_LENGTH = "maxLength";
  private static final String PATTERN = "pattern";
  private static final String FORMAT = "format";

  private JsObjSpecToSchema() {
  }

  public static JsObj convert(final JsObjSpec jsObjSpec) {
    return convert(jsObjSpec,
                   new HashSet<>());
  }

  private static JsObj convert(final JsObjSpec jsObjSpec,
                               Set<String> nameSpecsVisited) {
    return JsObj.of(TYPE,
                    jsObjSpec.nullable ? JsArray.of(JsStr.of(OBJECT),
                                                    JsStr.of(NULL)
                                                   ) : JsStr.of(OBJECT),
                    PROPERTIES,
                    getProperties(jsObjSpec,
                                  nameSpecsVisited),
                    ADDITIONAL_PROPERTIES,
                    JsBool.of(!jsObjSpec.strict),
                    REQUIRED,
                    getRequired(jsObjSpec),
                    DEFINITIONS,
                    getDefinitions(jsObjSpec,
                                   nameSpecsVisited)
                   );

  }

  private static JsValue getDefinitions(final JsObjSpec jsObjSpec,
                                        final Set<String> nameSpecsVisited) {
    JsObj definitions = JsObj.empty();
    var names = new HashSet<String>();
    findNamedSpecsRecursively(jsObjSpec,
                              names,
                              nameSpecsVisited);
    for (String name : names) {
      nameSpecsVisited.add(name);
      definitions = definitions.set(name,
                                    getSchema(JsSpecCache.get(name),
                                              nameSpecsVisited));
    }
    return definitions.isEmpty() ? JsNothing.NOTHING : definitions;
  }

  private static void findNamedSpecsRecursively(final JsSpec spec,
                                                Set<String> found,
                                                final Set<String> nameSpecsVisited) {
    if (spec instanceof NamedSpec namedSpec && !nameSpecsVisited.contains(namedSpec.name)) {
      found.add(namedSpec.name);
      nameSpecsVisited.add(namedSpec.name);
    } else if (spec instanceof JsObjSpec jsObjSpec) {
      for (JsSpec a : jsObjSpec.bindings.values()) {
        findNamedSpecsRecursively(a,
                                  found,
                                  nameSpecsVisited);
      }
    } else if (spec instanceof JsArrayOfSpec arraySpec) {
      findNamedSpecsRecursively(arraySpec.getElemSpec(),
                                found,
                                nameSpecsVisited);
    } else if (spec instanceof OneOf oneOf) {
      for (JsSpec s : oneOf.specs) {
        findNamedSpecsRecursively(s,
                                  found,
                                  nameSpecsVisited);
      }
    } else if (spec instanceof JsTuple tuple) {
      for (JsSpec s : tuple.specs) {
        findNamedSpecsRecursively(s,
                                  found,
                                  nameSpecsVisited);
      }
    }

  }

  private static JsArray getRequired(final JsObjSpec jsObjSpec) {
    return JsArray.ofStrs(jsObjSpec.requiredFields);
  }

  private static JsObj getProperties(final JsObjSpec jsObjSpec,
                                     final Set<String> nameSpecsVisited) {
    JsObj properties = JsObj.empty();
    Set<Entry<String, JsSpec>> entries = jsObjSpec.bindings.entrySet();
    for (Entry<String, JsSpec> entry : entries) {
      properties = properties.set(entry.getKey(),
                                  getSchema(entry.getValue(),
                                            nameSpecsVisited));
    }
    return properties;
  }


  private static JsValue getSchema(final JsSpec spec,
                                   final Set<String> nameSpecsVisited) {

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

      case JsStrSpec s -> getStrSchema(s,
                                       s.schema);
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
      case JsObjSpec s -> convert(s,
                                  nameSpecsVisited);

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
      case JsMapOfSpec s -> getMapOfSpec(s,
                                         nameSpecsVisited);
      case JsMapOfStr mapOfStr -> getMapOfStrSpec(mapOfStr.schema);

      case NamedSpec namedSpec -> JsObj.of(REF,
                                           JsStr.of("#/definitions/" + namedSpec.name)
                                          );
      case OneOf oneOf -> getOneOf(oneOf,
                                   nameSpecsVisited);
    };

  }

  private static JsValue getMapOfStrSpec(final StrConstraints schema) {
    if (schema != null) {
      return JsObj.of(TYPE,
                      JsStr.of(OBJECT),
                      ADDITIONAL_PROPERTIES,
                      JsObj.of(TYPE,
                               JsStr.of(STRING),
                               MIN_LENGTH,
                               schema.minLength == 0 ? JsNothing.NOTHING : JsInt.of(schema.minLength),
                               MAX_LENGTH,
                               schema.maxLength == Integer.MAX_VALUE ? JsNothing.NOTHING : JsInt.of(schema.maxLength),
                               PATTERN,
                               schema.pattern == null ? JsNothing.NOTHING : JsStr.of(schema.pattern.pattern()),
                               FORMAT,
                               schema.format == null ? JsNothing.NOTHING : JsStr.of(schema.format)
                              )
                     );
    }
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT),
                    ADDITIONAL_PROPERTIES,
                    JsObj.of(TYPE,
                             JsStr.of(STRING)
                            )
                   );
  }

  private static JsValue getStrSchema(final JsStrSpec s,
                                      final StrConstraints schema) {
    if (schema != null) {
      return JsObj.of(TYPE,
                      s.nullable ? JsArray.of(JsStr.of(STRING),
                                              JsStr.of(NULL)
                                             ) : JsStr.of(STRING),
                      MIN_LENGTH,
                      schema.minLength == 0 ? JsNothing.NOTHING : JsInt.of(schema.minLength),
                      MAX_LENGTH,
                      schema.maxLength == Integer.MAX_VALUE ? JsNothing.NOTHING : JsInt.of(schema.maxLength),
                      PATTERN,
                      schema.pattern == null ? JsNothing.NOTHING : JsStr.of(schema.pattern.pattern()),
                      FORMAT,
                      schema.format == null ? JsNothing.NOTHING : JsStr.of(schema.format)
                     );
    }
    return getStrSchema(s);
  }

  private static JsObj getOneOf(final OneOf oneOf,
                                final Set<String> nameSpecsVisited) {
    return JsObj.of(ONE_OF,
                    JsArray.ofIterable(oneOf.specs.stream()
                                                  .map(spec -> getSchema(spec,
                                                                         nameSpecsVisited))
                                                  .toList())
                   );
  }


  private static JsObj getMapOfSpec(final JsMapOfSpec jsMapOfSpec,
                                    final Set<String> nameSpecsVisited) {
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT),
                    ADDITIONAL_PROPERTIES,
                    getSchema(jsMapOfSpec.getValueSpec(),
                              nameSpecsVisited)
                   );
  }

  private static JsObj getMapOfInstantSpec() {
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT),
                    ADDITIONAL_PROPERTIES,
                    JsObj.of(TYPE,
                             JsStr.of(STRING),
                             FORMAT,
                             JsStr.of(BUILT_INT_FORMAT.DATE_TIME.name())
                            )
                   );
  }

  private static JsObj getMapOfBoolSpec() {
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT),
                    ADDITIONAL_PROPERTIES,
                    JsObj.of(TYPE,
                             JsStr.of(BOOLEAN)
                            )
                   );
  }

  private static JsObj getMapOfBinarySpec() {
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT),
                    ADDITIONAL_PROPERTIES,
                    JsObj.of(TYPE,
                             JsStr.of(STRING),
                             CONTENT_ENCODING,
                             JsStr.of(BUILT_INT_FORMAT.BASE64.name())
                            )
                   );
  }

  private static JsObj getMapOfIntegerSchema() {
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT),
                    ADDITIONAL_PROPERTIES,
                    JsObj.of(TYPE,
                             JsStr.of(INTEGER)
                            )
                   );
  }

  private static JsObj getMapOfNumberSchema() {
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT),
                    ADDITIONAL_PROPERTIES,
                    JsObj.of(TYPE,
                             JsStr.of(NUMBER)
                            )
                   );
  }

  private static JsObj getObjType() {
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT)
                   );
  }


  private static JsObj getArraySchema(final JsArraySpec s) {
    if (s.isNullable()) {
      return JsObj.of(TYPE,
                      JsArray.of(JsStr.of(ARRAY),
                                 JsStr.of(NULL)
                                ),
                      ITEMS,
                      getSchemaOfArrayOfSpec(s));
    }
    return JsObj.of(TYPE,
                    JsStr.of(ARRAY),
                    ITEMS,
                    getSchemaOfArrayOfSpec(s));
  }

  private static JsObj getAnySpec() {
    return JsObj.of(TYPE,
                    JsStr.of(STAR)
                   );
  }

  private static JsObj getEnumSchema(final JsEnum jsEnum) {
    if (jsEnum.isNullable()) {
      return JsObj.of(TYPE,
                      JsArray.of(JsStr.of(STRING),
                                 JsStr.of(NULL)
                                ),
                      ENUM,
                      jsEnum.symbols
                     );
    }
    return JsObj.of(TYPE,
                    JsStr.of(STRING),
                    ENUM,
                    jsEnum.symbols);
  }

  private static JsObj getBinarySchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of(TYPE,
                      JsArray.of(JsStr.of(STRING),
                                 JsStr.of(NULL)
                                ),
                      CONTENT_ENCODING,
                      JsStr.of(BUILT_INT_FORMAT.BASE64.name())
                     );
    }
    return JsObj.of(TYPE,
                    JsStr.of(STRING),
                    CONTENT_ENCODING,
                    JsStr.of(BUILT_INT_FORMAT.BASE64.name()));
  }

  private static JsObj getInstantSchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of(TYPE,
                      JsArray.of(JsStr.of(STRING),
                                 JsStr.of(NULL)
                                ),
                      FORMAT,
                      JsStr.of("date-time"));
    }
    return JsObj.of(TYPE,
                    JsStr.of(STRING),
                    FORMAT,
                    JsStr.of("date-time"));
  }

  private static JsObj getStrSchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of(TYPE,
                      JsArray.of(JsStr.of(STRING),
                                 JsStr.of(NULL)
                                )
                     );
    }
    return JsObj.of(TYPE,
                    JsStr.of(STRING));
  }

  private static JsObj getBoolSchema(final JsBooleanSpec s) {
    if (s.isNullable()) {
      return JsObj.of(TYPE,
                      JsArray.of(JsStr.of(BOOLEAN),
                                 JsStr.of(NULL)
                                )
                     );
    }
    return JsObj.of(TYPE,
                    JsStr.of(BOOLEAN));
  }

  private static JsObj getNumberSchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of(TYPE,
                      JsArray.of(JsStr.of(NUMBER),
                                 JsStr.of(NULL)
                                )
                     );
    }
    return JsObj.of(TYPE,
                    JsStr.of(NUMBER)
                   );
  }


  private static JsObj getIntSchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of(TYPE,
                      JsArray.of(JsStr.of(INTEGER),
                                 JsStr.of(NULL)
                                ));
    }
    return JsObj.of(TYPE,
                    JsStr.of(INTEGER));
  }

  private static JsObj getLongSchema(final JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of(TYPE,
                      JsArray.of(JsStr.of(INTEGER),
                                 JsStr.of(NULL)
                                )
                     );
    }
    return JsObj.of(TYPE,
                    JsStr.of(INTEGER));
  }


  private static JsValue getSchemaOfArrayOfSpec(final JsArraySpec spec) {

    return switch (spec) {
      case JsArrayOfInt s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfTestedInt s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfIntSuchThat ignored -> JsObj.of(TYPE,
                                                    JsStr.of(INTEGER)
                                                   );

      case JsArrayOfLong s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfTestedLong s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfLongSuchThat ignored -> JsObj.of(TYPE,
                                                     JsStr.of(INTEGER)
                                                    );

      case JsArrayOfBigInt s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfTestedBigInt s -> getSizableArrayOfIntSchema(s);
      case JsArrayOfBigIntSuchThat ignored -> JsObj.of(TYPE,
                                                       JsStr.of(INTEGER)
                                                      );

      case JsArrayOfDouble s -> getSizableArrayOfNumberSchema(s);
      case JsArrayOfTestedDouble s -> getSizableArrayOfNumberSchema(s);
      case JsArrayOfDoubleSuchThat ignored -> JsObj.of(TYPE,
                                                       JsStr.of(NUMBER)
                                                      );

      case JsArrayOfDecimal s -> getSizableArrayOfNumberSchema(s);
      case JsArrayOfTestedDecimal s -> getSizableArrayOfNumberSchema(s);
      case JsArrayOfDecimalSuchThat ignored -> JsObj.of(TYPE,
                                                        JsStr.of(NUMBER)
                                                       );

      case JsArrayOfBool s -> getSizableArrayOfBoolSchema(s);
      case JsArrayOfBoolSuchThat ignored -> JsObj.of(TYPE,
                                                     JsStr.of(BOOLEAN)
                                                    );

      case JsArrayOfStr s -> getSizableArrayOfStrSchema(s,
                                                        s.schema);
      case JsArrayOfStrSuchThat ignored -> JsObj.of(TYPE,
                                                    JsStr.of(STRING)
                                                   );
      case JsArrayOfTestedStr s -> getSizableArrayOfStrSchema(s);

      case JsArrayOfValue s -> getSizableArrayOfValueSchema(s);
      case JsArrayOfTestedValue s -> getSizableArrayOfValueSchema(s);
      case JsArraySuchThat ignored -> JsObj.of(TYPE,
                                               JsStr.of(STAR)
                                              );

      case JsArrayOfObj s -> getSizableArrayOfObjSchema(s);
      case JsArrayOfObjSuchThat ignored -> JsObj.of(TYPE,
                                                    JsStr.of(OBJECT)
                                                   );
      case JsArrayOfTestedObj s -> getSizableArrayOfObjSchema(s);

      case JsArrayOfSpec s -> getSizableArrayOfSpecSchema(s,
                                                          new HashSet<>());
      case JsTuple tuple -> getArrayOfTupleSchema(tuple,
                                                  new HashSet<>());
    };
  }

  private static JsValue getSizableArrayOfStrSchema(final JsArrayOfStr s,
                                                    final StrConstraints schema) {
    if (schema != null) {
      return getSizableArraySchema(s,
                                   JsObj.of(TYPE,
                                            JsStr.of(STRING),
                                            MIN_LENGTH,
                                            schema.minLength == 0 ? JsNothing.NOTHING : JsInt.of(schema.minLength),
                                            MAX_LENGTH,
                                            schema.maxLength == Integer.MAX_VALUE ? JsNothing.NOTHING
                                                                                  : JsInt.of(schema.maxLength),
                                            PATTERN,
                                            schema.pattern == null ? JsNothing.NOTHING
                                                                   : JsStr.of(schema.pattern.pattern()),
                                            FORMAT,
                                            schema.format == null ? JsNothing.NOTHING : JsStr.of(schema.format)
                                           ));
    }
    return getSizableArraySchema(s,
                                 JsObj.of(TYPE,
                                          JsStr.of(STRING)
                                         ));
  }


  private static JsValue getArrayOfTupleSchema(final JsTuple jsTuple,
                                               final Set<String> nameSpecsVisited) {
    return JsObj.of(TYPE,
                    jsTuple.isNullable() ?
                    JsArray.of(JsStr.of(ARRAY),
                               JsStr.of(NULL)
                              ) :
                    JsStr.of(ARRAY),
                    ITEMS,
                    JsArray.ofIterable(jsTuple.specs.stream()
                                                    .map(spec -> getSchema(spec,
                                                                           nameSpecsVisited))
                                                    .toList()
                                      ),
                    ADDITIONAL_ITEMS,
                    JsBool.FALSE
                   );
  }

  private static JsValue getSizableArrayOfSpecSchema(final JsArrayOfSpec s,
                                                     final Set<String> nameSpecsVisited) {
    return getSizableArraySchema(s,
                                 JsObj.of(TYPE,
                                          getSchema(s.getElemSpec(),
                                                    nameSpecsVisited)
                                         ));

  }

  private static JsValue getSizableArrayOfObjSchema(final AbstractSizableArr jsArrayOfObj) {
    return getSizableArraySchema(jsArrayOfObj,
                                 JsObj.of(TYPE,
                                          JsStr.of(OBJECT)
                                         ));
  }

  private static JsValue getSizableArrayOfValueSchema(AbstractSizableArr spec) {
    return getSizableArraySchema(spec,
                                 JsObj.of(TYPE,
                                          JsStr.of(STAR)
                                         ));
  }


  private static JsValue getSizableArrayOfStrSchema(AbstractSizableArr spec) {
    return getSizableArraySchema(spec,
                                 JsObj.of(TYPE,
                                          JsStr.of(STRING)
                                         ));
  }

  private static JsValue getSizableArrayOfBoolSchema(AbstractSizableArr spec) {
    return getSizableArraySchema(spec,
                                 JsObj.of(TYPE,
                                          JsStr.of(BOOLEAN)
                                         ));
  }

  private static JsObj getSizableArraySchema(AbstractSizableArr spec,
                                             JsObj elemSchema) {

    JsObj schema = elemSchema;
    var minElems = spec.min;
    var maxElems = spec.max;
    if (minElems > 0) {
      schema = schema.set(MIN_ITEMS,
                          JsInt.of(minElems));
    }
    if (maxElems < Integer.MAX_VALUE) {
      schema = schema.set(MAX_ITEMS,
                          JsInt.of(maxElems));
    }

    return schema;
  }


  private static JsObj getSizableArrayOfIntSchema(AbstractSizableArr spec) {

    return getSizableArraySchema(spec,
                                 JsObj.of(TYPE,
                                          JsStr.of(INTEGER)
                                         ));
  }

  private static JsObj getSizableArrayOfNumberSchema(AbstractSizableArr spec) {
    return getSizableArraySchema(spec,
                                 JsObj.of(TYPE,
                                          JsStr.of(NUMBER)
                                         ));
  }
}