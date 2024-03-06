package jsonvalues.spec;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.ListGen;
import fun.gen.NamedGen;
import fun.gen.SplitGen;
import fun.gen.StrGen;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import jsonvalues.JsInt;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsStr;
import jsonvalues.JsValue;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsBigDecGen;
import jsonvalues.gen.JsBigIntGen;
import jsonvalues.gen.JsBinaryGen;
import jsonvalues.gen.JsBoolGen;
import jsonvalues.gen.JsDoubleGen;
import jsonvalues.gen.JsInstantGen;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsLongGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import jsonvalues.gen.JsTupleGen;

/**
 * Utility class for converting JSON specifications (JsObjSpec or JsArraySpec) to JSON schemas represented with a
 * JsObj.
 */
public final class SpecToGen {

  private static final int MAX_ARRAY_SIZE = 100;
  private static final int MIN_ARRAY_SIZE = 0;
  private static final int MAX_MAP_SIZE = 20;
  private static final int MIN_MAP_SIZE = 1;
  private static final int MIN_KEY_MAP_SIZE = 1;
  private static final int MAX_KEY_MAP_SIZE = 10;
  private static final int MIN_STRING_LENGTH = 0;
  private static final int MAX_STRING_LENGTH = 100;
  private static final int MIN_INT_SIZE = Integer.MIN_VALUE;
  private static final int MAX_INT_SIZE = Integer.MAX_VALUE;
  private static final long MIN_LONG_SIZE = Long.MIN_VALUE;
  private static final long MAX_LONG_SIZE = Long.MAX_VALUE;
  private static final double MIN_DOUBLE_SIZE = Double.MIN_VALUE;
  private static final double MAX_DOUBLE_SIZE = Double.MAX_VALUE;

  private static final Gen<JsValue> primitiveValueGen =
      Combinators.oneOf(createJsDoubleGen(null),
                        createJsBigDecimalGen(null),
                        createJsLongGen(null),
                        createJsIntGen(null),
                        createJsStrGen(null,
                                       null),
                        createJsBoolGen(),
                        createJsBinaryGen(),
                        createJsInstantGen(null));
  private static final Gen<JsValue> objGen =
      Combinators.oneOf(createMapOfDoubleGen(null),
                        createMapOfStrGen(null,
                                          null),
                        createMapOfIntGen(null),
                        createMapOfLongGen(null),
                        createMapOfDoubleGen(null),
                        createMapOfInstantGen(null),
                        createMapOfBoolGen());
  private static final int MIN_BINARY_LENGTH = 0;
  private static final int MAX_BINARY_LENGTH = 100;


  private SpecToGen() {
  }


  public static JsObjGen convert(final JsObjSpec objSpec) {
    return convert(objSpec,
                   Map.of());
  }

  /**
   * Converts a JsObjSpec to a JSON schema (JsObj).
   *
   * @param objSpec The JsObjSpec to be converted.
   * @return The resulting JSON schema as a JsObj.
   */
  public static JsObjGen convert(JsObjSpec objSpec,
                                 Map<JsPath, Gen<? extends JsValue>> overrides) {
    return convert(objSpec,
                   overrides,
                   new HashSet<>()
                  );
  }

  public static Gen<? extends JsValue> convert(final JsSpec spec) {
    return convert(spec,
                   Map.of());
  }

  public static Gen<? extends JsValue> convert(final JsSpec spec,
                                               final Map<JsPath, Gen<? extends JsValue>> overrides) {
    Gen<? extends JsValue> gen = switch (Objects.requireNonNull(spec)) {
      case NamedSpec namedSpec -> createNamedGen(namedSpec,
                                                 overrides,
                                                 JsPath.empty(),
                                                 new HashSet<>());
      case JsObjSpec objSpec -> convert(objSpec,
                                        Map.of());
      case JsArraySpec arraySpec -> createJsArrayGen(arraySpec,
                                                     JsPath.empty(),
                                                     overrides,
                                                     new HashSet<>());
      case OneOf oneOf -> createOneOfGen(oneOf,
                                         overrides,
                                         JsPath.empty(),
                                         new HashSet<>());
      default -> createGen(spec,
                           overrides,
                           JsPath.empty(),
                           new HashSet<>());
    };
    return spec.isNullable() ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             gen
                            ) :
           gen;

  }


  private static JsObjGen convert(JsObjSpec objSpec,
                                  Map<JsPath, Gen<? extends JsValue>> overrides,
                                  Set<String> nameSpecsVisited
                                 ) {
    var gen = new HashMap<String, Gen<? extends JsValue>>();
    var currentPath = JsPath.empty();
    Set<String> optionals = new HashSet<>();
    Set<String> nullables = new HashSet<>();
    for (var binding : objSpec.bindings.entrySet()) {
      var path = currentPath.key(binding.getKey());
      var optional = objSpec.optionalFields.contains(binding.getKey());
      var nullable = binding.getValue()
                            .isNullable();
      if (nullable) {
        nullables.add(binding.getKey());
      }
      if (optional) {
        optionals.add(binding.getKey());
      }

      var keyGen =
          overrides.containsKey(path) ?
          overrides.get(path) :
          createGen(binding.getValue(),
                    overrides,
                    path,
                    nameSpecsVisited
                   );
      gen.put(binding.getKey(),
              keyGen
             );
    }
    return new JsObjGen(gen,
                        optionals,
                        nullables
    );
  }


  private static Gen<? extends JsValue> createGen(JsSpec spec,
                                                  Map<JsPath, Gen<? extends JsValue>> overrides,
                                                  JsPath currentPath,
                                                  Set<String> nameSpecsVisited) {
    return switch (spec) {
      case Cons c -> Gen.cons(c.value);
      case JsIntSpec s -> createJsIntGen(s.constraints);
      case JsIntSuchThat s -> createJsIntSuchThatGen(s.predicate);
      case JsLongSpec s -> createJsLongGen(s.constraints);
      case JsLongSuchThat s -> createJsLongSuchThatGen(s.predicate);
      case JsBigIntSpec s -> createJsBigIntGen(s.constraints);
      case JsBigIntSuchThat s -> createJsBigIntGenSuchThat(s.predicate);
      case JsDoubleSpec s -> createJsDoubleGen(s.constraints);
      case JsDoubleSuchThat s -> createJsDoubleSuchThatGen(s.predicate);
      case JsDecimalSpec s -> createJsBigDecimalGen(s.constraints);
      case JsDecimalSuchThat s -> createJsBigDecimalGenSuchThat(s.predicate);
      case JsBooleanSpec s -> createJsBoolGen();

      case JsStrSpec s -> createJsStrGen(s.constraints,
                                         currentPath);

      case JsStrSuchThat s -> createJsStrSuchThatGen(s.predicate);
      case JsInstantSpec s -> createJsInstantGen(s.constraints);

      case JsInstantSuchThat s -> createJsInstantGenSuchThat(s.predicate);

      case JsBinarySpec s -> createJsBinaryGen();

      case JsBinarySuchThat s -> createJsBinaryGenSuchThat(s.predicate);

      case JsFixedBinary s -> createJsFixedBinaryGen(s.getSize());
      case JsEnum jsEnum -> createEnumGen(jsEnum);

      case AnySpec s -> createAnySpecGen();

      case AnySuchThat ignored -> throw createUnsupportedExc(currentPath,
                                                             AnySuchThat.class);
      case JsArraySpec s -> createJsArrayGen(s,
                                             currentPath,
                                             overrides,
                                             nameSpecsVisited);
      case JsObjSpec s -> convert(s,
                                  overrides,
                                  nameSpecsVisited);

      case IsJsObj s -> createIsObjGen();

      case JsObjSuchThat ignored -> throw createUnsupportedExc(currentPath,
                                                               JsObjSuchThat.class);

      case JsMapOfBigInt s -> createMapOfBigIntGen(s.valuesConstraints);
      case JsMapOfInt s -> createMapOfIntGen(s.valuesConstraints);
      case JsMapOfLong s -> createMapOfLongGen(s.valuesConstraints);

      case JsMapOfDouble s -> createMapOfDoubleGen(s.valuesConstraints);

      case JsMapOfDec s -> createMapOfBigDecGen(s.valuesConstraints);

      case JsMapOfBinary s -> createMapOfBinaryGen();

      case JsMapOfBool s -> createMapOfBoolGen();

      case JsMapOfInstant s -> createMapOfInstantGen(s.valuesConstraints);

      case JsMapOfSpec s -> createMapOfSpecGen(s.getValueSpec(),
                                               overrides,
                                               currentPath,
                                               nameSpecsVisited);

      case JsMapOfStr s -> createMapOfStrGen(s.valuesConstraints,
                                             currentPath);

      case NamedSpec namedSpec -> createNamedGen(namedSpec,
                                                 overrides,
                                                 currentPath,
                                                 nameSpecsVisited);
      case OneOf oneOf -> createOneOfGen(oneOf,
                                         overrides,
                                         currentPath,
                                         nameSpecsVisited);
    };
  }

  private static Gen<? extends JsValue> createNamedGen(NamedSpec namedSpec,
                                                       Map<JsPath, Gen<? extends JsValue>> overrides,
                                                       JsPath currentPath,
                                                       Set<String> nameSpecsVisited) {

    String name = namedSpec.name;
    Gen<? extends JsValue> gen;
    if (nameSpecsVisited.contains(name)) {
      gen = NamedGen.of(name);
    } else {
      nameSpecsVisited.add(name);
      gen =
          NamedGen.of(name,
                      createGen(JsSpecCache.get(name),
                                overrides,
                                currentPath,
                                nameSpecsVisited
                               )
                     );
    }
    return namedSpec.nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             gen
                            ) :
           gen;
  }

  private static Gen<JsValue> createAnySpecGen() {
    return Combinators.oneOf(createJsStrGen(null,
                                            null),
                             createJsLongGen(null),
                             createJsIntGen(null),
                             createJsBigIntGen(null),
                             createJsBigDecimalGen(null),
                             createJsDoubleGen(null),
                             createJsBoolGen(),
                             createJsBinaryGen(),
                             createIsObjGen()
                            );

  }

  private static Gen<JsValue> createIsObjGen() {
    return Combinators.oneOf(createMapOfBigIntGen(null),
                             createMapOfBoolGen(),
                             createMapOfBigDecGen(null),
                             createMapOfIntGen(null),
                             createMapOfLongGen(null),
                             createMapOfInstantGen(null),
                             createMapOfStrGen(null,
                                               null),
                             createMapOfDoubleGen(null)
                            );
  }

  private static Gen<? extends JsValue> createOneOfGen(OneOf oneOf,
                                                       Map<JsPath, Gen<? extends JsValue>> overrides,
                                                       JsPath currentPath,
                                                       Set<String> nameSpecsVisited) {
    List<Gen<? extends JsValue>> gens =
        new ArrayList<>(oneOf.specs.stream()
                                   .map(spec ->
                                        {
                                          var gen =
                                              createGen(spec,
                                                        overrides,
                                                        currentPath,
                                                        nameSpecsVisited
                                                       );
                                          return spec.isNullable() ?
                                                 Combinators.oneOf(Gen.cons(JsNull.NULL),
                                                                   gen
                                                                  ) :
                                                 gen;
                                        }
                                       )
                                   .toList());
    return Combinators.oneOfList(gens);
  }

  private static Gen<? extends JsValue> createMapOfBigIntGen(BigIntSchemaConstraints valuesConstraints) {

    var keyGen = StrGen.alphabetic(MIN_KEY_MAP_SIZE,
                                   MAX_KEY_MAP_SIZE)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 MIN_MAP_SIZE,
                                 MAX_MAP_SIZE);
    var value = createJsBigIntGen(valuesConstraints);

    return mapGen(keys,
                  value);
  }

  private static Gen<? extends JsValue> createMapOfStrGen(StrConstraints valuesConstraints,
                                                          JsPath path) {
    var keyGen = StrGen.alphabetic(MIN_KEY_MAP_SIZE,
                                   MAX_KEY_MAP_SIZE)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 MIN_MAP_SIZE,
                                 MAX_MAP_SIZE);
    var value = createJsStrGen(valuesConstraints,
                               path);

    return mapGen(keys,
                  value);
  }

  private static Gen<? extends JsValue> mapGen(Gen<List<String>> keys,
                                               Gen<? extends JsValue> value) {
    return random -> {
      var keysSupplier = keys.apply(random);
      var valueSupplier = value.apply(SplitGen.DEFAULT.apply(random));
      return () -> {
        List<String> keyList = keysSupplier.get();
        JsObj obj = JsObj.empty();
        for (String key : keyList) {
          obj = obj.set(key,
                        valueSupplier.get()
                       );
        }
        return obj;
      };
    };
  }

  private static Gen<? extends JsValue> createMapOfSpecGen(JsSpec valueSpec,
                                                           Map<JsPath, Gen<? extends JsValue>> overrides,
                                                           JsPath path,
                                                           Set<String> nameSpecsVisited) {
    var keyGen = StrGen.alphabetic(MIN_KEY_MAP_SIZE,
                                   MAX_KEY_MAP_SIZE)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 MIN_MAP_SIZE,
                                 MAX_MAP_SIZE);
    var value = valueSpec.isNullable() ?
                Combinators.oneOf(Gen.cons(JsNull.NULL),
                                  createGen(valueSpec,
                                            overrides,
                                            path,
                                            nameSpecsVisited)
                                 ) :
                createGen(valueSpec,
                          overrides,
                          path,
                          nameSpecsVisited);

    return mapGen(keys,
                  value);
  }

  private static Gen<? extends JsValue> createMapOfInstantGen(InstantSchemaConstraints valuesConstraints) {

    var keyGen = StrGen.alphabetic(MIN_KEY_MAP_SIZE,
                                   MAX_KEY_MAP_SIZE)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 MIN_MAP_SIZE,
                                 MAX_MAP_SIZE);
    var value = createJsInstantGen(valuesConstraints);

    return mapGen(keys,
                  value);


  }

  private static Gen<? extends JsValue> createMapOfBoolGen() {
    var keyGen = StrGen.alphabetic(MIN_KEY_MAP_SIZE,
                                   MAX_KEY_MAP_SIZE)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 MIN_MAP_SIZE,
                                 MAX_MAP_SIZE);
    var value = JsBoolGen.arbitrary();

    return mapGen(keys,
                  value);

  }

  private static Gen<? extends JsValue> createMapOfBinaryGen() {
    var keyGen = StrGen.alphabetic(MIN_KEY_MAP_SIZE,
                                   MAX_KEY_MAP_SIZE)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 MIN_MAP_SIZE,
                                 MAX_MAP_SIZE);
    //TODO binary constraints
    var value = JsBinaryGen.biased(MIN_BINARY_LENGTH,
                                   MAX_BINARY_LENGTH);

    return mapGen(keys,
                  value);

  }

  private static Gen<? extends JsValue> createMapOfBigDecGen(DecimalSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(MIN_KEY_MAP_SIZE,
                                   MAX_KEY_MAP_SIZE)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 MIN_MAP_SIZE,
                                 MAX_MAP_SIZE);
    var value = createJsBigDecimalGen(valuesConstraints);

    return mapGen(keys,
                  value);

  }

  private static Gen<? extends JsValue> createMapOfDoubleGen(DoubleSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(MIN_KEY_MAP_SIZE,
                                   MAX_KEY_MAP_SIZE)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 MIN_MAP_SIZE,
                                 MAX_MAP_SIZE);

    var value = createJsDoubleGen(valuesConstraints);

    return mapGen(keys,
                  value);
  }

  private static Gen<? extends JsValue> createMapOfLongGen(LongSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(MIN_KEY_MAP_SIZE,
                                   MAX_KEY_MAP_SIZE)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 MIN_MAP_SIZE,
                                 MAX_MAP_SIZE);
    var value = createJsLongGen(valuesConstraints);

    return mapGen(keys,
                  value);
  }

  private static Gen<? extends JsValue> createMapOfIntGen(IntegerSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(MIN_KEY_MAP_SIZE,
                                   MAX_KEY_MAP_SIZE)
                       .distinct();

    var keys = ListGen.arbitrary(keyGen,
                                 MIN_MAP_SIZE,
                                 MAX_MAP_SIZE);
    var value = createJsIntGen(valuesConstraints);

    return mapGen(keys,
                  value);

  }

  private static Gen<? extends JsValue> createJsBinaryGen() {
    return JsBinaryGen.arbitrary(MIN_BINARY_LENGTH,
                                 MAX_BINARY_LENGTH);
  }

  private static Gen<? extends JsValue> createJsArrayGen(JsArraySpec spec,
                                                         JsPath path,
                                                         Map<JsPath, Gen<? extends JsValue>> overrides,
                                                         Set<String> nameSpecsVisited) {

    return switch (spec) {
      case JsArrayOfInt s -> getSizableArrayOfIntGen(s);
      case JsArrayOfTestedInt s -> getSizableArrayOfTestedIntGen(s);
      case JsArrayOfIntSuchThat s -> throw createUnsupportedExc(path,
                                                                JsArrayOfIntSuchThat.class);

      case JsArrayOfLong s -> getSizableArrayOfLongGen(s);
      case JsArrayOfTestedLong s -> getSizableArrayOfTestedLongGen(s);
      case JsArrayOfLongSuchThat s -> throw createUnsupportedExc(path,
                                                                 JsArrayOfLongSuchThat.class);

      case JsArrayOfBigInt s -> getSizableArrayOfBigIntGen(s);
      case JsArrayOfTestedBigInt s -> getSizableArrayOfTestedBigIntGen(s);
      case JsArrayOfBigIntSuchThat s -> throw createUnsupportedExc(path,
                                                                   JsArrayOfBigIntSuchThat.class);

      case JsArrayOfDouble s -> getSizableArrayOfDoubleGen(s);
      case JsArrayOfTestedDouble s -> getSizableArrayOfTestedDoubleGen(s);
      case JsArrayOfDoubleSuchThat s -> throw createUnsupportedExc(path,
                                                                   JsArrayOfDoubleSuchThat.class);

      case JsArrayOfDecimal s -> getSizableArrayOfDecGen(s);
      case JsArrayOfTestedDecimal s -> getSizableArrayOfTestedDecGen(s);
      case JsArrayOfDecimalSuchThat s -> throw createUnsupportedExc(path,
                                                                    JsArrayOfDecimalSuchThat.class);

      case JsArrayOfBool s -> getSizableArrayOfBoolGen(s);
      case JsArrayOfBoolSuchThat s -> throw createUnsupportedExc(path,
                                                                 JsArrayOfBoolSuchThat.class);

      case JsArrayOfStr s -> getSizableArrayOfStrGen(s,
                                                     path);

      case JsArrayOfTestedStr s -> getSizableArrayOfTestedStrGen(s);

      case JsArrayOfStrSuchThat s -> throw createUnsupportedExc(path,
                                                                JsArrayOfStrSuchThat.class);

      case JsArrayOfValue s -> getSizableArrayOfValueGen(s);
      case JsArrayOfTestedValue s -> getSizableArrayOfTestedValueGen(s);

      case JsArraySuchThat s -> throw createUnsupportedExc(path,
                                                           JsArraySuchThat.class);

      case JsArrayOfObj s -> getSizableArrayOfObjGen(s);
      case JsArrayOfTestedObj s -> getSizableArrayOfTestedObjGen(s);

      case JsArrayOfObjSuchThat s -> throw createUnsupportedExc(path,
                                                                JsArrayOfObjSuchThat.class);

      case JsArrayOfSpec s -> getSizableArrayOfSpecGen(s,
                                                       overrides,
                                                       path,
                                                       nameSpecsVisited);
      case JsTuple tuple -> getTupleGen(tuple,
                                        nameSpecsVisited);
    };
  }

  private static Gen<? extends JsValue> getSizableArrayOfTestedObjGen(JsArrayOfTestedObj spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen =
        objGen.suchThat(value -> spec.test(value)
                                     .isEmpty());
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(elemenGen,
                                                  MIN_ARRAY_SIZE,
                                                  max));
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private static Gen<? extends JsValue> getSizableArrayOfTestedValueGen(JsArrayOfTestedValue spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = primitiveValueGen.suchThat(value -> spec.test(value)
                                                            .isEmpty());
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(elemenGen,
                                                  MIN_ARRAY_SIZE,
                                                  max));
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private static Gen<? extends JsValue> getSizableArrayOfTestedStrGen(final JsArrayOfTestedStr spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = JsStrGen.biased(MIN_STRING_LENGTH,
                                    MAX_STRING_LENGTH)
                            .suchThat(value -> spec.test(value)
                                                   .isEmpty());
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(elemenGen,
                                                  MIN_ARRAY_SIZE,
                                                  max));
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private static Gen<? extends JsValue> getSizableArrayOfTestedDecGen(final JsArrayOfTestedDecimal spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = JsBigDecGen.biased()
                               .suchThat(value -> spec.test(value)
                                                      .isEmpty());
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(elemenGen,
                                                  MIN_ARRAY_SIZE,
                                                  max));
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private static Gen<? extends JsValue> getSizableArrayOfTestedDoubleGen(final JsArrayOfTestedDouble spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = JsDoubleGen.biased(MIN_DOUBLE_SIZE,
                                       MAX_DOUBLE_SIZE)
                               .suchThat(value -> spec.test(value)
                                                      .isEmpty());
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(elemenGen,
                                                  MIN_ARRAY_SIZE,
                                                  max));
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private static Gen<? extends JsValue> getSizableArrayOfTestedBigIntGen(final JsArrayOfTestedBigInt spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = JsBigIntGen.biased()
                               .suchThat(value -> spec.test(value)
                                                      .isEmpty());
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(elemenGen,
                                                  MIN_ARRAY_SIZE,
                                                  max));
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private static Gen<? extends JsValue> getSizableArrayOfTestedLongGen(final JsArrayOfTestedLong spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = JsLongGen.biased(MIN_LONG_SIZE,
                                     MAX_LONG_SIZE)
                             .suchThat(value -> spec.test(value)
                                                    .isEmpty());
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(elemenGen,
                                                  MIN_ARRAY_SIZE,
                                                  max));
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private static Gen<? extends JsValue> getSizableArrayOfTestedIntGen(JsArrayOfTestedInt spec) {
    var arrayConstraints = spec.arrayConstraints;
    Gen<JsInt> elemenGen = JsIntGen.biased(MIN_INT_SIZE,
                                           MAX_INT_SIZE)
                                   .suchThat(value -> spec.test(value)
                                                          .isEmpty());
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(elemenGen,
                                                  MIN_ARRAY_SIZE,
                                                  max));
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private static IllegalArgumentException createUnsupportedExc(JsPath path,
                                                               Class<?> clazz) {
    return new IllegalArgumentException("Generators for `%s` spec are not supported. User `override` parameter to provide a custom generator for the path %s".formatted(clazz.getName(),
                                                                                                                                                                        path));
  }

  private static Gen<? extends JsValue> getSizableArrayOfBigIntGen(JsArrayOfBigInt spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(createJsBigIntGen(constraints),
                                                  MIN_ARRAY_SIZE,
                                                  max)
                        );
    }

    return JsArrayGen.biased(createJsBigIntGen(constraints),
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );

  }

  private static Gen<? extends JsValue> getTupleGen(final JsTuple tuple,
                                                    final Set<String> nameSpecsVisited) {
    List<JsSpec> specs = tuple.specs;
    List<Gen<? extends JsValue>> gens = new ArrayList<>();
    for (JsSpec spec : specs) {
      gens.add(spec.isNullable() ?
               Combinators.oneOf(Gen.cons(JsNull.NULL),
                                 createGen(spec,
                                           Map.of(),
                                           JsPath.empty(),
                                           nameSpecsVisited)) :
               createGen(spec,
                         Map.of(),
                         JsPath.empty(),
                         nameSpecsVisited)
              );
    }
    return JsTupleGen.of(gens);

  }

  private static Gen<? extends JsValue> getSizableArrayOfSpecGen(JsArrayOfSpec spec,
                                                                 Map<JsPath, Gen<? extends JsValue>> overrides,
                                                                 JsPath path,
                                                                 Set<String> nameSpecsVisited) {
    var arrayConstraints = spec.arrayConstraints;
    Gen<? extends JsValue> elemGen =
        spec.getElemSpec()
            .isNullable() ?
        Combinators.oneOf(Gen.cons(JsNull.NULL),
                          createGen(spec.getElemSpec(),
                                    overrides,
                                    path,
                                    nameSpecsVisited)
                         ) :
        createGen(spec.getElemSpec(),
                  overrides,
                  path,
                  nameSpecsVisited);

    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max ->
                             JsArrayGen.biased(elemGen,
                                               MIN_ARRAY_SIZE,
                                               max)
                        );
    }
    return JsArrayGen.biased(elemGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );

  }

  private static Gen<? extends JsValue> getSizableArrayOfObjGen(JsArrayOfObj spec) {

    var arrayConstraints = spec.arrayConstraints;

    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(objGen,
                                                  MIN_ARRAY_SIZE,
                                                  max)
                        );
    }
    return JsArrayGen.biased(objGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );


  }

  private static Gen<? extends JsValue> getSizableArrayOfValueGen(final JsArrayOfValue spec) {

    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(primitiveValueGen,
                                                  MIN_ARRAY_SIZE,
                                                  max)
                        );
    }
    return JsArrayGen.biased(primitiveValueGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );

  }

  private static Gen<? extends JsValue> getSizableArrayOfStrGen(final JsArrayOfStr spec,
                                                                final JsPath path) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      return
          IntGen.arbitrary(MIN_ARRAY_SIZE,
                           MAX_ARRAY_SIZE)
                .then(max -> JsArrayGen.biased(createJsStrGen(constraints,
                                                              path),
                                               MIN_ARRAY_SIZE,
                                               max)
                     );
    }
    return JsArrayGen.biased(createJsStrGen(constraints,
                                            path),
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private static Gen<? extends JsValue> getSizableArrayOfBoolGen(final JsArrayOfBool spec) {
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      return
          IntGen.arbitrary(MIN_ARRAY_SIZE,
                           MAX_ARRAY_SIZE)
                .then(max -> JsArrayGen.biased(createJsBoolGen(),
                                               MIN_ARRAY_SIZE,
                                               max));
    }
    return JsArrayGen.biased(createJsBoolGen(),
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );

  }

  private static Gen<? extends JsValue> getSizableArrayOfDecGen(JsArrayOfDecimal spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {

      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(createJsBigDecimalGen(constraints),
                                                  MIN_ARRAY_SIZE,
                                                  max));
    }
    return JsArrayGen.biased(createJsBigDecimalGen(constraints),
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private static Gen<? extends JsValue> getSizableArrayOfDoubleGen(final JsArrayOfDouble spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      return
          IntGen.arbitrary(MIN_ARRAY_SIZE,
                           MAX_ARRAY_SIZE)
                .then(max -> JsArrayGen.biased(createJsDoubleGen(constraints),
                                               MIN_ARRAY_SIZE,
                                               max));
    }
    return JsArrayGen.biased(createJsDoubleGen(constraints),
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );

  }

  private static Gen<? extends JsValue> getSizableArrayOfLongGen(final JsArrayOfLong spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(createJsLongGen(constraints),
                                                  MIN_ARRAY_SIZE,
                                                  max));
    }
    return JsArrayGen.biased(createJsLongGen(constraints),
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private static Gen<? extends JsValue> getSizableArrayOfIntGen(final JsArrayOfInt spec) {
    var intConstraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      return IntGen.arbitrary(MIN_ARRAY_SIZE,
                              MAX_ARRAY_SIZE)
                   .then(max -> JsArrayGen.biased(createJsIntGen(intConstraints),
                                                  MIN_ARRAY_SIZE,
                                                  max));
    }
    return JsArrayGen.biased(createJsIntGen(intConstraints),
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }


  private static Gen<? extends JsValue> createJsFixedBinaryGen(final int size) {
    return JsBinaryGen.arbitrary(size,
                                 size);
  }

  private static Gen<? extends JsValue> createJsInstantGen(final InstantSchemaConstraints constraints) {
    return constraints == null ?
           JsInstantGen.biased() :
           JsInstantGen.biased(constraints.minimum()
                                          .getEpochSecond(),
                               constraints.maximum()
                                          .getEpochSecond()
                              );
  }

  private static Gen<? extends JsValue> createJsBigDecimalGenSuchThat(final Function<BigDecimal, JsError> predicate) {
    return createJsBigDecimalGen(null)
        .suchThat(n -> predicate.apply(n.toJsBigDec().value) == null);
  }

  private static Gen<? extends JsValue> createJsBigDecimalGen(final DecimalSchemaConstraints constraints) {
    return constraints == null ?
           JsBigDecGen.biased() :
           JsBigDecGen.biased(constraints.minimum(),
                              constraints.maximum()
                             );
  }

  private static Gen<? extends JsValue> createJsBoolGen() {
    return JsBoolGen.arbitrary();
  }

  private static Gen<? extends JsValue> createJsBigIntGenSuchThat(final Function<BigInteger, JsError> predicate) {
    return createJsBigIntGen(null)
        .suchThat(n -> predicate.apply(n.toJsBigInt().value) == null);
  }

  private static Gen<? extends JsValue> createJsDoubleSuchThatGen(final DoubleFunction<JsError> predicate) {
    return createJsDoubleGen(null)
        .suchThat(n -> predicate.apply(n.toJsDouble().value) == null);
  }

  private static Gen<? extends JsValue> createJsDoubleGen(final DoubleSchemaConstraints constraints) {
    return constraints == null ?
           JsDoubleGen.biased(MIN_DOUBLE_SIZE,
                              MAX_DOUBLE_SIZE) :
           JsDoubleGen.biased(constraints.minimum(),
                              constraints.maximum()
                             );

  }

  private static Gen<? extends JsValue> createEnumGen(final JsEnum jsEnum) {
    return Combinators.oneOf(jsEnum.symbols.stream()
                                           .map(it -> it.value()
                                                        .toJsStr())
                                           .toList());

  }

  private static Gen<? extends JsValue> createJsBigIntGen(final BigIntSchemaConstraints constraints) {

    return constraints == null ?
           JsBigIntGen.biased() :
           JsBigIntGen.biased(constraints.minimum(),
                              constraints.maximum()
                             );
  }


  private static Gen<? extends JsValue> createJsStrSuchThatGen(Function<String, JsError> predicate) {
    return createJsStrGen(null,
                          null)
        .suchThat(n -> predicate.apply(n.toJsStr().value) == null);
  }

  private static Gen<? extends JsValue> createJsStrGen(StrConstraints constraints,
                                                       JsPath path) {
    Gen<JsStr> gen;
    if (constraints == null) {
      gen = IntGen.arbitrary(MIN_STRING_LENGTH,
                             MAX_STRING_LENGTH)
                  .then(max -> JsStrGen.alphabetic(MIN_STRING_LENGTH,
                                                   max));
    } else if (constraints.pattern != null) {
      throw new IllegalArgumentException("Generators for regex patterns spec are not supported. Use `override` parameter to provide a custom generator for the path %s".formatted(path));
    } else {
      gen = JsStrGen.alphanumeric(constraints.minLength,
                                  constraints.maxLength
                                 );
    }
    return gen;
  }

  private static Gen<? extends JsValue> createJsIntSuchThatGen(IntFunction<JsError> predicate) {
    return createJsIntGen(null)
        .suchThat(n -> predicate.apply(n.toJsInt().value) == null);
  }


  private static Gen<? extends JsValue> createJsIntGen(IntegerSchemaConstraints constraints) {
    return constraints == null ?
           JsIntGen.biased(MIN_INT_SIZE,
                           MAX_INT_SIZE) :
           JsIntGen.biased(constraints.minimum(),
                           constraints.maximum());
  }

  private static Gen<? extends JsValue> createJsLongGen(LongSchemaConstraints constraints) {
    return constraints == null ?
           JsLongGen.biased(MIN_LONG_SIZE,
                            MAX_LONG_SIZE) :
           JsLongGen.biased(constraints.minimum(),
                            constraints.maximum());

  }

  private static Gen<? extends JsValue> createJsLongSuchThatGen(LongFunction<JsError> predicate) {
    return createJsLongGen(null)
        .suchThat(n -> predicate.apply(n.toJsLong().value) == null);
  }

  private static Gen<? extends JsValue> createJsInstantGenSuchThat(Function<Instant, JsError> predicate) {
    return createJsInstantGen(null)
        .suchThat(n -> predicate.apply(n.toJsInstant().value) == null);
  }

  private static Gen<? extends JsValue> createJsBinaryGenSuchThat(Function<byte[], JsError> predicate) {
    return createJsBinaryGen()
        .suchThat(n -> predicate.apply(n.toJsBinary().value) == null);
  }

}