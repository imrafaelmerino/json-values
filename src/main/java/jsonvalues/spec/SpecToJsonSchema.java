package jsonvalues.spec;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsBool;
import jsonvalues.JsDouble;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsNothing;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.JsValue;


/**
 * This class is designed for converting JSON specifications into JSON schemas represented by a JsObj. It adheres to the
 * structure defined in the draft at <a href="https://json-schema.org/draft/2019-09/schema">...</a>. It is important to
 * note that not all keywords from the draft are supported, but rather only those that are pertinent to json-spec.
 * However, these supported keywords are comprehensive enough to effectively convey the structure of the JSON. Unlike
 * JSON schema, json-spec allows for the definition of arbitrary validations, as predicates can be used to validate
 * data.
 */

public final class SpecToJsonSchema {

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
  private static final String DEFINITIONS = "defs";
  private static final String REF = "$ref";
  private static final String ENUM = "enum";
  private static final String ONE_OF = "oneOf";
  private static final String ARRAY = "array";
  private static final String ITEMS = "items";
  private static final String CONST = "const";
  private static final String MIN_ITEMS = "minItems";
  private static final String MAX_ITEMS = "maxItems";
  private static final String ADDITIONAL_ITEMS = "additionalItems";
  private static final String CONTENT_ENCODING = "contentEncoding";
  private static final String MIN_LENGTH = "minLength";
  private static final String MAX_LENGTH = "maxLength";
  private static final String PATTERN = "pattern";
  private static final String FORMAT = "format";
  private static final String MINIMUM = "minimum";
  private static final String MAXIMUM = "maximum";
  private static final String UNIQUE_ITEMS = "uniqueItems";
  private static final String DESCRIPTION = "description";
  private static final String SCHEMA = "$schema";
  private static final String ID = "$id";
  private static final String DRAFT = "https://json-schema.org/draft/2019-09/schema";
  private static final String DEFAULT = "default";

  private SpecToJsonSchema() {
  }

  /**
   * Converts a JsObjSpec to a JSON schema.
   *
   * @param jsObjSpec The JsObjSpec to be converted.
   * @return The resulting JSON schema as a JsObj.
   */
  public static JsObj convert(final JsObjSpec jsObjSpec) {
    return JsObj.of(SCHEMA,
                    JsStr.of(DRAFT),
                    DEFINITIONS,
                    getDefinitions(jsObjSpec,
                                   new HashSet<>())
                   )
                .union(convert(jsObjSpec,
                               new HashSet<>()),
                       JsArray.TYPE.SET);
  }

  /**
   * Converts a spec to a JSON schema.
   *
   * @param spec The spec to be converted.
   * @return The resulting JSON schema as a JsObj.
   */
  public static JsObj convert(final JsSpec spec) {
    if (spec instanceof NamedSpec namedSpec) {
      return convert(spec,
                     namedSpec);
    }
    if (spec instanceof JsObjSpec jsObjSpec) {
      return convert(jsObjSpec);
    }
    if (spec instanceof JsArraySpec jsArraySpec) {
      return convert(jsArraySpec);
    }
    HashSet<String> visited = new HashSet<>();
    return JsObj.of(DEFINITIONS,
                    getDefinitions(spec,
                                   visited),
                    SCHEMA,
                    JsStr.of(DRAFT)
                   )
                .union(getSchema(spec,
                                 visited),
                       JsArray.TYPE.SET);


  }

  private static JsObj convert(final JsSpec spec,
                               final NamedSpec namedSpec) {
    HashSet<String> visited = new HashSet<>();
    return JsObj.of(DEFINITIONS,
                    getDefinitions(spec,
                                   visited),
                    SCHEMA,
                    JsStr.of(DRAFT),
                    ID,
                    JsStr.of(namedSpec.name),
                    REF,
                    JsStr.of("#/%s/%s".formatted(DEFINITIONS,
                                                 namedSpec.name))
                   );
  }


  /**
   * Converts a JsArraySpec to a JSON schema (JsObj).
   *
   * @param jsArraySpec The JsArraySpec to be converted.
   * @return The resulting JSON schema as a JsObj.
   */
  public static JsObj convert(final JsArraySpec jsArraySpec) {

    return JsObj.of(SCHEMA,
                    JsStr.of(DRAFT),
                    DEFINITIONS,
                    getDefinitions(jsArraySpec,
                                   new HashSet<>())
                   )
                .union(getArraySchema(jsArraySpec),
                       JsArray.TYPE.SET);
  }


  private static JsObj convert(final JsObjSpec jsObjSpec,
                               Set<String> nameSpecsVisited) {
    MetaData metaData = jsObjSpec.metaData;
    return JsObj.of(TYPE,
                    jsObjSpec.nullable ? JsArray.of(JsStr.of(OBJECT),
                                                    JsStr.of(NULL)) : JsStr.of(OBJECT),
                    PROPERTIES,
                    getProperties(jsObjSpec,
                                  nameSpecsVisited,
                                  metaData),
                    ADDITIONAL_PROPERTIES,
                    JsBool.of(!jsObjSpec.strict),
                    REQUIRED,
                    getRequired(jsObjSpec),
                    DESCRIPTION,
                    metaData != null && metaData.doc() != null ? JsStr.of(metaData.doc()) : JsNothing.NOTHING,
                    ID,
                    metaData != null ? JsStr.of(metaData.getFullName()) : JsNothing.NOTHING
                   );

  }

  private static JsValue getDefinitions(final JsSpec jsObjSpec,
                                        final Set<String> nameSpecsVisited) {
    var definitions = JsObj.empty();
    var names = new HashSet<String>();
    findDefinitionsRecursively(jsObjSpec,
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

  private static void findDefinitionsRecursively(JsSpec spec,
                                                 Set<String> found,
                                                 Set<String> nameSpecsVisited) {
    if (spec instanceof NamedSpec namedSpec && !nameSpecsVisited.contains(namedSpec.name)) {
      found.add(namedSpec.name);
      nameSpecsVisited.add(namedSpec.name);
      findDefinitionsRecursively(JsSpecCache.get(namedSpec.name),
                                 found,
                                 nameSpecsVisited);
    } else if (spec instanceof JsObjSpec jsObjSpec) {
      for (JsSpec a : jsObjSpec.bindings.values()) {
        findDefinitionsRecursively(a,
                                   found,
                                   nameSpecsVisited);
      }
    } else if (spec instanceof JsArrayOfSpec arraySpec) {
      findDefinitionsRecursively(arraySpec.getElemSpec(),
                                 found,
                                 nameSpecsVisited);
    } else if (spec instanceof OneOf oneOf) {
      for (JsSpec oneOfSpec : oneOf.specs) {
        findDefinitionsRecursively(oneOfSpec,
                                   found,
                                   nameSpecsVisited);
      }
    } else if (spec instanceof JsTuple tuple) {
      for (JsSpec tupleSpec : tuple.specs) {
        findDefinitionsRecursively(tupleSpec,
                                   found,
                                   nameSpecsVisited);
      }
    } else if (spec instanceof JsMapOfSpec mapOfSpec) {
      findDefinitionsRecursively(mapOfSpec.getValueSpec(),
                                 found,
                                 nameSpecsVisited);
    }

  }

  private static JsArray getRequired(final JsObjSpec jsObjSpec) {
    return JsArray.ofStrs(jsObjSpec.requiredFields);
  }

  private static JsObj getProperties(JsObjSpec jsObjSpec,
                                     Set<String> nameSpecsVisited,
                                     MetaData metaData) {
    JsObj properties = JsObj.empty();
    Set<Entry<String, JsSpec>> entries = jsObjSpec.bindings.entrySet();
    for (Entry<String, JsSpec> entry : entries) {
      JsObj schema = getSchema(entry.getValue(),
                               nameSpecsVisited);
      if (metaData != null) {
        if (metaData.fieldsDoc() != null && metaData.fieldsDoc()
                                                    .containsKey(entry.getKey())) {
          schema = schema.set(DESCRIPTION,
                              JsStr.of(metaData.fieldsDoc()
                                               .get(entry.getKey())));
        }
        if (metaData.fieldsDefault() != null && metaData.fieldsDefault()
                                                        .containsKey(entry.getKey())) {
          schema = schema.set(DEFAULT,
                              metaData.fieldsDefault()
                                      .get(entry.getKey()));
        }

      }
      properties = properties.set(entry.getKey(),
                                  schema);
    }
    return properties;
  }

  private static JsObj getSchema(JsSpec spec,
                                 Set<String> nameSpecsVisited) {

    return switch (spec) {
      case Cons o -> getConsSchema(o);
      case JsIntSpec s -> getIntSchema(s,
                                       s.constraints);
      case JsIntSuchThat s -> getIntSchema(s);
      case JsLongSpec s -> getLongSchema(s,
                                         s.constraints);
      case JsLongSuchThat s -> getIntSchema(s);
      case JsBigIntSpec s -> getBigIntSchema(s,
                                             s.constraints);
      case JsBigIntSuchThat s -> getIntSchema(s);
      case JsDoubleSpec s -> getDoubleSchema(s,
                                             s.constraints);
      case JsDoubleSuchThat s -> getNumberSchema(s);
      case JsDecimalSpec s -> getDecimalSchema(s,
                                               s.constraints);
      case JsDecimalSuchThat s -> getNumberSchema(s);
      case JsBooleanSpec s -> getBoolSchema(s);
      case JsStrSpec s -> getStrSchema(s,
                                       s.constraints);
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
      case JsMapOfBigInt s -> getMapOfBigIntSchema(s.valuesConstraints);
      case JsMapOfInt s -> getMapOfIntegerSchema(s.valuesConstraints);
      case JsMapOfLong s -> getMapOfLongSchema(s.valuesConstraints);
      case JsMapOfDouble s -> getMapOfDoubleSchema(s.valuesConstraints);
      case JsMapOfDec s -> getMapOfDecSchema(s.valuesConstraints);
      case JsMapOfBinary ignored -> getMapOfBinarySchema();
      case JsMapOfBool ignored -> getMapOfBoolSchema();
      case JsMapOfInstant ignored -> getMapOfInstantSchema();
      case JsMapOfSpec s -> getMapOfSpec(s,
                                         nameSpecsVisited);
      case JsMapOfStr s -> getMapOfStrSchema(s.valuesConstraints);
      case NamedSpec namedSpec -> JsObj.of(REF,
                                           JsStr.of("#/%s/%s".formatted(DEFINITIONS,
                                                                        namedSpec.name))
                                          );
      case OneOf oneOf -> getOneOfSchema(oneOf,
                                         nameSpecsVisited);
    };

  }

  private static JsObj getConsSchema(Cons s) {

    return JsObj.of(CONST,
                    s.value);

  }

  private static JsObj getMapOfIntegerSchema(IntegerSchemaConstraints valuesConstraints) {
    if (valuesConstraints != null) {
      return JsObj.of(TYPE,
                      JsStr.of(OBJECT),
                      ADDITIONAL_PROPERTIES,
                      JsObj.of(TYPE,
                               JsStr.of(INTEGER),
                               MINIMUM,
                               valuesConstraints.minimum() == Integer.MIN_VALUE ? JsNothing.NOTHING
                                                                                : JsInt.of(valuesConstraints.minimum()),
                               MAXIMUM,
                               valuesConstraints.maximum() == Integer.MAX_VALUE ? JsNothing.NOTHING
                                                                                : JsInt.of(valuesConstraints.maximum())
                              ));
    }
    return getMapOfIntegerSchema();
  }

  private static JsObj getMapOfDecSchema(DecimalSchemaConstraints valuesConstraints) {
    if (valuesConstraints != null) {
      return JsObj.of(TYPE,
                      JsStr.of(OBJECT),
                      ADDITIONAL_PROPERTIES,
                      JsObj.of(TYPE,
                               JsStr.of(NUMBER),
                               MINIMUM,
                               valuesConstraints.minimum() == null ? JsNothing.NOTHING
                                                                   : JsBigDec.of(valuesConstraints.minimum()),
                               MAXIMUM,
                               valuesConstraints.maximum() == null ? JsNothing.NOTHING
                                                                   : JsBigDec.of(valuesConstraints.maximum())
                              ));
    }
    return getMapOfNumberSchema();
  }

  private static JsObj getMapOfDoubleSchema(DoubleSchemaConstraints valuesConstraints) {

    if (valuesConstraints != null) {
      return JsObj.of(TYPE,
                      JsStr.of(OBJECT),
                      ADDITIONAL_PROPERTIES,
                      JsObj.of(TYPE,
                               JsStr.of(NUMBER),
                               MINIMUM,
                               valuesConstraints.minimum() == Double.NEGATIVE_INFINITY ? JsNothing.NOTHING
                                                                                       : JsDouble.of(valuesConstraints.minimum()),
                               MAXIMUM,
                               valuesConstraints.maximum() == Double.POSITIVE_INFINITY ? JsNothing.NOTHING
                                                                                       : JsDouble.of(valuesConstraints.maximum())
                              ));
    }
    return getMapOfNumberSchema();
  }

  private static JsObj getMapOfLongSchema(LongSchemaConstraints valuesConstraints) {
    if (valuesConstraints != null) {
      return JsObj.of(TYPE,
                      JsStr.of(OBJECT),
                      ADDITIONAL_PROPERTIES,
                      JsObj.of(TYPE,
                               JsStr.of(INTEGER),
                               MINIMUM,
                               valuesConstraints.minimum() == Long.MIN_VALUE ? JsNothing.NOTHING
                                                                             : JsLong.of(valuesConstraints.minimum()),
                               MAXIMUM,
                               valuesConstraints.maximum() == Long.MAX_VALUE ? JsNothing.NOTHING
                                                                             : JsLong.of(valuesConstraints.maximum())
                              ));
    }
    return getMapOfIntegerSchema();
  }

  private static JsObj getMapOfBigIntSchema(BigIntSchemaConstraints valuesConstraints) {
    if (valuesConstraints != null) {
      return JsObj.of(TYPE,
                      JsStr.of(OBJECT),
                      ADDITIONAL_PROPERTIES,
                      JsObj.of(TYPE,
                               JsStr.of(INTEGER),
                               MINIMUM,
                               valuesConstraints.minimum() == null ? JsNothing.NOTHING
                                                                   : JsBigInt.of(valuesConstraints.minimum()),
                               MAXIMUM,
                               valuesConstraints.maximum() == null ? JsNothing.NOTHING
                                                                   : JsBigInt.of(valuesConstraints.maximum()))
                     );
    }
    return getMapOfIntegerSchema();
  }

  private static JsObj getDecimalSchema(JsDecimalSpec s,
                                        DecimalSchemaConstraints constraints) {

    if (constraints != null) {
      return JsObj.of(TYPE,
                      s.nullable ? JsArray.of(JsStr.of(NUMBER),
                                              JsStr.of(NULL)
                                             ) : JsStr.of(NUMBER),
                      MINIMUM,
                      constraints.minimum() == null ? JsNothing.NOTHING : JsBigDec.of(constraints.minimum()),
                      MAXIMUM,
                      constraints.maximum() == null ? JsNothing.NOTHING : JsBigDec.of(constraints.maximum())
                     );
    }
    return getNumberSchema(s);
  }

  private static JsObj getDoubleSchema(JsDoubleSpec s,
                                       DoubleSchemaConstraints constraints) {
    if (constraints != null) {
      return JsObj.of(TYPE,
                      s.nullable ? JsArray.of(JsStr.of(NUMBER),
                                              JsStr.of(NULL)
                                             ) : JsStr.of(NUMBER),
                      MINIMUM,
                      constraints.minimum() == Double.NEGATIVE_INFINITY ? JsNothing.NOTHING
                                                                        : JsDouble.of(constraints.minimum()),
                      MAXIMUM,
                      constraints.maximum() == Double.POSITIVE_INFINITY ? JsNothing.NOTHING
                                                                        : JsDouble.of(constraints.maximum())
                     );
    }
    return getNumberSchema(s);
  }

  private static JsObj getBigIntSchema(JsBigIntSpec s,
                                       BigIntSchemaConstraints constraints) {
    if (constraints != null) {
      return JsObj.of(TYPE,
                      s.nullable ? JsArray.of(JsStr.of(INTEGER),
                                              JsStr.of(NULL)
                                             ) : JsStr.of(INTEGER),
                      MINIMUM,
                      constraints.minimum() == null ? JsNothing.NOTHING : JsBigInt.of(constraints.minimum()),
                      MAXIMUM,
                      constraints.maximum() == null ? JsNothing.NOTHING : JsBigInt.of(constraints.maximum())
                     );
    }
    return getIntSchema(s);
  }

  private static JsObj getLongSchema(JsLongSpec s,
                                     LongSchemaConstraints constraints) {
    if (constraints != null) {
      return JsObj.of(TYPE,
                      s.nullable ? JsArray.of(JsStr.of(INTEGER),
                                              JsStr.of(NULL)
                                             ) : JsStr.of(INTEGER),
                      MINIMUM,
                      constraints.minimum() == Long.MIN_VALUE ? JsNothing.NOTHING : JsLong.of(constraints.minimum()),
                      MAXIMUM,
                      constraints.maximum() == Long.MAX_VALUE ? JsNothing.NOTHING : JsLong.of(constraints.maximum())
                     );
    }
    return getIntSchema(s);
  }

  private static JsObj getIntSchema(JsIntSpec s,
                                    IntegerSchemaConstraints constraints) {
    if (constraints != null) {
      return JsObj.of(TYPE,
                      s.nullable ? JsArray.of(JsStr.of(INTEGER),
                                              JsStr.of(NULL)
                                             ) : JsStr.of(INTEGER),
                      MINIMUM,
                      constraints.minimum() == Integer.MIN_VALUE ? JsNothing.NOTHING
                                                                 : JsInt.of(constraints.minimum()),
                      MAXIMUM,
                      constraints.maximum() == Integer.MAX_VALUE ? JsNothing.NOTHING
                                                                 : JsInt.of(constraints.maximum())
                     );
    }
    return getIntSchema(s);
  }

  private static JsObj getMapOfStrSchema(StrConstraints schema) {
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

  private static JsObj getStrSchema(JsStrSpec s,
                                    StrConstraints schema) {
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

  private static JsObj getOneOfSchema(OneOf oneOf,
                                      Set<String> nameSpecsVisited) {
    return JsObj.of(ONE_OF,
                    JsArray.ofIterable(oneOf.specs.stream()
                                                  .map(spec -> getSchema(spec,
                                                                         nameSpecsVisited))
                                                  .toList())
                   );
  }

  private static JsObj getMapOfSpec(JsMapOfSpec jsMapOfSpec,
                                    Set<String> nameSpecsVisited) {
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT),
                    ADDITIONAL_PROPERTIES,
                    getSchema(jsMapOfSpec.getValueSpec(),
                              nameSpecsVisited)
                   );
  }

  private static JsObj getMapOfInstantSchema() {
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT),
                    ADDITIONAL_PROPERTIES,
                    JsObj.of(TYPE,
                             JsStr.of(STRING),
                             FORMAT,
                             JsStr.of("date-time")
                            )
                   );
  }

  private static JsObj getMapOfBoolSchema() {
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT),
                    ADDITIONAL_PROPERTIES,
                    JsObj.of(TYPE,
                             JsStr.of(BOOLEAN)
                            )
                   );
  }

  private static JsObj getMapOfBinarySchema() {
    return JsObj.of(TYPE,
                    JsStr.of(OBJECT),
                    ADDITIONAL_PROPERTIES,
                    JsObj.of(TYPE,
                             JsStr.of(STRING),
                             CONTENT_ENCODING,
                             JsStr.of("base64")
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

  private static JsObj getArraySchema(JsArraySpec s) {
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

  private static JsObj getEnumSchema(JsEnum jsEnum) {
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

  private static JsObj getBinarySchema(JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of(TYPE,
                      JsArray.of(JsStr.of(STRING),
                                 JsStr.of(NULL)
                                ),
                      CONTENT_ENCODING,
                      JsStr.of("base64")
                     );
    }
    return JsObj.of(TYPE,
                    JsStr.of(STRING),
                    CONTENT_ENCODING,
                    JsStr.of("base64"));
  }

  private static JsObj getInstantSchema(JsSpec s) {
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

  private static JsObj getStrSchema(JsSpec s) {
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

  private static JsObj getBoolSchema(JsBooleanSpec s) {
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

  private static JsObj getNumberSchema(JsSpec s) {
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

  private static JsObj getIntSchema(JsSpec s) {
    if (s.isNullable()) {
      return JsObj.of(TYPE,
                      JsArray.of(JsStr.of(INTEGER),
                                 JsStr.of(NULL)
                                ));
    }
    return JsObj.of(TYPE,
                    JsStr.of(INTEGER));
  }

  private static JsValue getSchemaOfArrayOfSpec(JsArraySpec spec) {

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
                                                        s.constraints);
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

  private static JsValue getSizableArrayOfStrSchema(JsArrayOfStr s,
                                                    StrConstraints schema) {
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

  private static JsValue getArrayOfTupleSchema(JsTuple jsTuple,
                                               Set<String> nameSpecsVisited) {
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

  private static JsValue getSizableArrayOfSpecSchema(JsArrayOfSpec arraySpec,
                                                     Set<String> nameSpecsVisited) {
    JsSpec elemSpec = arraySpec.getElemSpec();
    if (elemSpec instanceof NamedSpec namedSpec) {
      return
          JsObj.of(REF,
                   JsStr.of("#/%s/%s".formatted(DEFINITIONS,
                                                namedSpec.name))
                  );
    }
    return
        getSizableArraySchema(arraySpec,
                              JsObj.of(TYPE,
                                       getSchema(elemSpec,
                                                 nameSpecsVisited)
                                      ));

  }

  private static JsValue getSizableArrayOfObjSchema(AbstractSizableArr jsArrayOfObj) {
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

    if (spec.arrayConstraints != null) {
      var minElems = spec.arrayConstraints.minItems();
      var maxElems = spec.arrayConstraints.maxItems();
      if (minElems > 0) {
        schema = schema.set(MIN_ITEMS,
                            JsInt.of(minElems));
      }
      if (maxElems < Integer.MAX_VALUE) {
        schema = schema.set(MAX_ITEMS,
                            JsInt.of(maxElems));
      }
      if (spec.arrayConstraints.uniqueItems()) {
        schema = schema.set(UNIQUE_ITEMS,
                            JsBool.TRUE);
      }
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