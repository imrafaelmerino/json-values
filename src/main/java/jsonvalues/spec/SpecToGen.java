package jsonvalues.spec;

import fun.gen.Combinators;
import fun.gen.Gen;
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
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsBinary;
import jsonvalues.JsBool;
import jsonvalues.JsDouble;
import jsonvalues.JsInstant;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
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
 * Class responsible for converting JSON specs to JSON generators. When a generator cannot be created for a given spec,
 * a {@code RuntimeException} is thrown. In this case, the user can provide a custom generator for the path using the
 * {@code override} parameter.
 */
public final class SpecToGen {

  /**
   * The default instance of the {@code SpecToGen} class. To customize how data is generated, use the
   * {@link #of(SpecGenConfBuilder)} method to create a new instance with the desired configuration.
   */
  public static final SpecToGen DEFAULT = new SpecToGen();

  private final GenConf conf;

  private SpecToGen(SpecGenConfBuilder confBuilder) {
    this.conf = Objects.requireNonNull(confBuilder)
                       .build();
    this.objGen =
        Combinators.oneOf(createMapOfDoubleGen(null),
                          createMapOfStrGen(null,
                                            null),
                          createMapOfIntGen(null),
                          createMapOfLongGen(null),
                          createMapOfDoubleGen(null),
                          createMapOfInstantGen(null),
                          createMapOfBoolGen());

    this.primitiveValueGen =
        Combinators.oneOf(createJsDoubleGen(null),
                          createJsBigDecimalGen(null),
                          createJsLongGen(null),
                          createJsIntGen(null),
                          createJsStrGen(null,
                                         null),
                          createJsBoolGen(),
                          createJsBinaryGen(),
                          createJsInstantGen(null));
  }

  private SpecToGen() {
    this(new SpecGenConfBuilder());
  }


  /**
   * Creates a new instance of the {@code SpecToGen} class with the specified configuration.
   *
   * @param confBuilder The configuration to customize how data is generated.
   * @return A new instance of the {@code SpecToGen} class with the specified configuration.
   */
  public static SpecToGen of(SpecGenConfBuilder confBuilder) {
    return new SpecToGen(confBuilder);
  }

  /**
   * Converts a JsObjSpec to a JSON object generator. It throws a {@code RuntimeException} if the generator cannot be
   * created. In this case, use the alternative method {@link #convert(JsObjSpec, Map)} that accepts an override
   * parameter to provide a custom generator
   *
   * @param objSpec The JsObjSpec to be converted.
   * @return The resulting JsObjGen
   */
  public JsObjGen convert(final JsObjSpec objSpec) {
    return convert(objSpec,
                   Map.of());
  }

  /**
   * Converts a JsObjSpec to a JSON object generator with the specified overrides. It throws a {@code RuntimeException}
   * if the generator cannot be created. In this case, override the generator for the path using the {@code overrides}
   * parameter.
   *
   * @param objSpec   The JsObjSpec to be converted.
   * @param overrides The overrides to apply to the generator.
   * @return The resulting JsObjGen
   */
  public JsObjGen convert(JsObjSpec objSpec,
                          Map<JsPath, Gen<? extends JsValue>> overrides) {
    return convert(objSpec,
                   overrides,
                   new HashSet<>()
                  );
  }

  /**
   * Converts a spec to a JSON object generator. It throws a {@code RuntimeException} if the generator cannot be
   * created. In this case, override the generator for the path using the method {@link #convert(JsSpec, Map)}.
   *
   * @param spec The spec to be converted.
   * @return The resulting JSON value generator
   */
  public Gen<JsValue> convert(final JsSpec spec) {
    return convert(spec,
                   Map.of());
  }

  /**
   * Converts a JsSpec to a JSON generator. It throws a {@code RuntimeException} if the generator cannot be created. In
   * this case, use the alternative method {@link #convert(JsSpec, Map)} that accepts an override parameter to provide a
   * custom generator.
   *
   * @param spec      The JsSpec to be converted.
   * @param overrides The overrides to apply to the generator.
   * @return The resulting JSON value generator
   */
  public Gen<JsValue> convert(final JsSpec spec,
                              final Map<JsPath, Gen<? extends JsValue>> overrides) {
    var gen = createGen(spec,
                        overrides,
                        JsPath.empty(),
                        new HashSet<>());

    return spec.isNullable() ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             gen
                            ) :
           gen;

  }

  private final Gen<JsValue> objGen;

  private final Gen<JsValue> primitiveValueGen;

  private JsObjGen convert(JsObjSpec objSpec,
                           Map<JsPath, Gen<? extends JsValue>> overrides,
                           Set<String> nameSpecsVisited
                          ) {
    var gen = new HashMap<String, Gen<? extends JsValue>>();
    var currentPath = JsPath.empty();
    Set<String> optionals = new HashSet<>();
    Set<String> nullables = new HashSet<>();
    for (var binding : Objects.requireNonNull(objSpec).bindings.entrySet()) {
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
                        nullables)
        .withNullableProbability(conf.nullableProbability())
        .withOptionalProbability(conf.optionalProbability());
  }


  private Gen<JsValue> createGen(JsSpec spec,
                                 Map<JsPath, Gen<? extends JsValue>> overrides,
                                 JsPath currentPath,
                                 Set<String> nameSpecsVisited) {
    Gen<? extends JsValue> gen = switch (spec) {
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
    return gen.map(e -> e);
  }

  private Gen<JsValue> createNamedGen(NamedSpec namedSpec,
                                      Map<JsPath, Gen<? extends JsValue>> overrides,
                                      JsPath currentPath,
                                      Set<String> nameSpecsVisited) {

    String name = namedSpec.name;
    Gen<JsValue> gen;
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

  private Gen<JsValue> createAnySpecGen() {
    return Combinators.oneOf(objGen,
                             primitiveValueGen);

  }

  private Gen<JsValue> createIsObjGen() {
    return objGen;
  }

  private Gen<JsValue> createOneOfGen(OneOf oneOf,
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

  private Gen<JsObj> createMapOfBigIntGen(BigIntSchemaConstraints valuesConstraints) {

    var keyGen = StrGen.alphabetic(conf.keyMapLength()
                                       .first(),
                                   conf.keyMapLength()
                                       .second())
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 conf.mapSize()
                                     .first(),
                                 conf.mapSize()
                                     .second());
    var value = createJsBigIntGen(valuesConstraints);

    return mapGen(keys,
                  value);
  }

  private Gen<JsObj> createMapOfStrGen(StrConstraints valuesConstraints,
                                       JsPath path) {
    var keyGen = StrGen.alphabetic(conf.keyMapLength()
                                       .first(),
                                   conf.keyMapLength()
                                       .second())
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 conf.mapSize()
                                     .first(),
                                 conf.mapSize()
                                     .second());
    var value = createJsStrGen(valuesConstraints,
                               path);

    return mapGen(keys,
                  value);
  }

  private Gen<JsObj> mapGen(Gen<List<String>> keys,
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

  private Gen<JsObj> createMapOfSpecGen(JsSpec valueSpec,
                                        Map<JsPath, Gen<? extends JsValue>> overrides,
                                        JsPath path,
                                        Set<String> nameSpecsVisited) {
    var keyGen = StrGen.alphabetic(conf.keyMapLength()
                                       .first(),
                                   conf.keyMapLength()
                                       .second())
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 conf.mapSize()
                                     .first(),
                                 conf.mapSize()
                                     .second());
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

  private Gen<JsObj> createMapOfInstantGen(InstantSchemaConstraints valuesConstraints) {

    var keyGen = StrGen.alphabetic(conf.keyMapLength()
                                       .first(),
                                   conf.keyMapLength()
                                       .second())
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 conf.mapSize()
                                     .first(),
                                 conf.mapSize()
                                     .second());
    var value = createJsInstantGen(valuesConstraints);

    return mapGen(keys,
                  value);


  }

  private Gen<JsObj> createMapOfBoolGen() {
    var keyGen = StrGen.alphabetic(conf.keyMapLength()
                                       .first(),
                                   conf.keyMapLength()
                                       .second())
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 conf.mapSize()
                                     .first(),
                                 conf.mapSize()
                                     .second());
    var value = JsBoolGen.arbitrary();

    return mapGen(keys,
                  value);

  }

  private Gen<JsObj> createMapOfBinaryGen() {
    var keyGen = StrGen.alphabetic(conf.keyMapLength()
                                       .first(),
                                   conf.keyMapLength()
                                       .second())
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 conf.mapSize()
                                     .first(),
                                 conf.mapSize()
                                     .second());
    var value = JsBinaryGen.biased(conf.binaryLength()
                                       .first(),
                                   conf.binaryLength()
                                       .second());

    return mapGen(keys,
                  value);

  }

  private Gen<JsObj> createMapOfBigDecGen(DecimalSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(conf.keyMapLength()
                                       .first(),
                                   conf.keyMapLength()
                                       .second())
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 conf.mapSize()
                                     .first(),
                                 conf.mapSize()
                                     .second());
    var
        value = createJsBigDecimalGen(valuesConstraints);

    return mapGen(keys,
                  value);

  }

  private Gen<JsObj> createMapOfDoubleGen(DoubleSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(conf.keyMapLength()
                                       .first(),
                                   conf.keyMapLength()
                                       .second())
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 conf.mapSize()
                                     .first(),
                                 conf.mapSize()
                                     .second());

    var value = createJsDoubleGen(valuesConstraints);

    return mapGen(keys,
                  value);
  }

  private Gen<JsObj> createMapOfLongGen(LongSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(conf.keyMapLength()
                                       .first(),
                                   conf.keyMapLength()
                                       .second())
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 conf.mapSize()
                                     .first(),
                                 conf.mapSize()
                                     .second());
    var value = createJsLongGen(valuesConstraints);

    return mapGen(keys,
                  value);
  }

  private Gen<JsObj> createMapOfIntGen(IntegerSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(conf.keyMapLength()
                                       .first(),
                                   conf.keyMapLength()
                                       .second())
                       .distinct();

    var keys = ListGen.arbitrary(keyGen,
                                 conf.mapSize()
                                     .first(),
                                 conf.mapSize() //TODO
                                     .second());
    var value = createJsIntGen(valuesConstraints);

    return mapGen(keys,
                  value);

  }

  private Gen<JsBinary> createJsBinaryGen() {
    return JsBinaryGen.arbitrary(conf.binaryLength()
                                     .first(),
                                 conf.binaryLength()
                                     .second());
  }

  private Gen<JsArray> createJsArrayGen(JsArraySpec spec,
                                        JsPath path,
                                        Map<JsPath, Gen<? extends JsValue>> overrides,
                                        Set<String> nameSpecsVisited) {

    return switch (spec) {
      case JsArrayOfInt s -> createArrayOfIntGen(s);
      case JsArrayOfTestedInt s -> createArrayOfTestedIntGen(s);
      case JsArrayOfIntSuchThat s -> throw createUnsupportedExc(path,
                                                                JsArrayOfIntSuchThat.class);

      case JsArrayOfLong s -> createArrayOfLongGen(s);
      case JsArrayOfTestedLong s -> createArrayOfTestedLongGen(s);
      case JsArrayOfLongSuchThat s -> throw createUnsupportedExc(path,
                                                                 JsArrayOfLongSuchThat.class);

      case JsArrayOfBigInt s -> createArrayOfBigIntGen(s);
      case JsArrayOfTestedBigInt s -> createArrayOfTestedBigIntGen(s);
      case JsArrayOfBigIntSuchThat s -> throw createUnsupportedExc(path,
                                                                   JsArrayOfBigIntSuchThat.class);

      case JsArrayOfDouble s -> createArrayOfDoubleGen(s);
      case JsArrayOfTestedDouble s -> createArrayOfTestedDoubleGen(s);
      case JsArrayOfDoubleSuchThat s -> throw createUnsupportedExc(path,
                                                                   JsArrayOfDoubleSuchThat.class);

      case JsArrayOfDecimal s -> createArrayOfDecGen(s);
      case JsArrayOfTestedDecimal s -> createArrayOfTestedDecGen(s);
      case JsArrayOfDecimalSuchThat s -> throw createUnsupportedExc(path,
                                                                    JsArrayOfDecimalSuchThat.class);

      case JsArrayOfBool s -> createArrayOfBoolGen(s);
      case JsArrayOfBoolSuchThat s -> throw createUnsupportedExc(path,
                                                                 JsArrayOfBoolSuchThat.class);

      case JsArrayOfStr s -> createArrayOfStrGen(s,
                                                 path);

      case JsArrayOfTestedStr s -> createArrayOfTestedStrGen(s);

      case JsArrayOfStrSuchThat s -> throw createUnsupportedExc(path,
                                                                JsArrayOfStrSuchThat.class);

      case JsArrayOfValue s -> createArrayOfValueGen(s);
      case JsArrayOfTestedValue s -> createArrayOfTestedValueGen(s);

      case JsArraySuchThat s -> throw createUnsupportedExc(path,
                                                           JsArraySuchThat.class);

      case JsArrayOfObj s -> createArrayOfObjGen(s);
      case JsArrayOfTestedObj s -> createArrayOfTestedObjGen(s);

      case JsArrayOfObjSuchThat s -> throw createUnsupportedExc(path,
                                                                JsArrayOfObjSuchThat.class);

      case JsArrayOfSpec s -> createArrayOfSpecGen(s,
                                                   overrides,
                                                   path,
                                                   nameSpecsVisited
                                                  );
      case JsTuple tuple -> createTupleGen(tuple,
                                           nameSpecsVisited,
                                           path,
                                           overrides
                                          );
    };
  }

  private Gen<JsArray> createArrayOfTestedObjGen(JsArrayOfTestedObj spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = objGen.suchThat(value -> spec.test(value)
                                                 .isEmpty());
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemenGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private Gen<JsArray> createArrayOfTestedValueGen(JsArrayOfTestedValue spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = primitiveValueGen.suchThat(value -> spec.test(value)
                                                            .isEmpty());
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemenGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private Gen<JsArray> createArrayOfTestedStrGen(final JsArrayOfTestedStr spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = JsStrGen.biased(conf.stringLength()
                                        .first(),
                                    conf.stringLength()
                                        .second())
                            .suchThat(value -> spec.test(value)
                                                   .isEmpty());
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemenGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private Gen<JsArray> createArrayOfTestedDecGen(final JsArrayOfTestedDecimal spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = JsBigDecGen.biased(conf.bigDecSize()
                                           .first(),
                                       conf.bigDecSize()
                                           .second())
                               .suchThat(value -> spec.test(value)
                                                      .isEmpty());
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemenGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private Gen<JsArray> createArrayOfTestedDoubleGen(final JsArrayOfTestedDouble spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = JsDoubleGen.biased(conf.doubleSize()
                                           .first(),
                                       conf.doubleSize()
                                           .second())
                               .suchThat(value -> spec.test(value)
                                                      .isEmpty());
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemenGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private Gen<JsArray> createArrayOfTestedBigIntGen(final JsArrayOfTestedBigInt spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = JsBigIntGen.biased(conf.bigIntSize()
                                           .first(),
                                       conf.bigIntSize()
                                           .second())
                               .suchThat(value -> spec.test(value)
                                                      .isEmpty());
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemenGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private Gen<JsArray> createArrayOfTestedLongGen(final JsArrayOfTestedLong spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = JsLongGen.biased(conf.longSize()
                                         .first(),
                                     conf.longSize()
                                         .second())
                             .suchThat(value -> spec.test(value)
                                                    .isEmpty());
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemenGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private Gen<JsArray> createArrayOfTestedIntGen(JsArrayOfTestedInt spec) {
    var arrayConstraints = spec.arrayConstraints;
    var elemenGen = JsIntGen.biased(conf.intSize()
                                        .first(),
                                    conf.intSize()
                                        .second())
                            .suchThat(value -> spec.test(value)
                                                   .isEmpty());
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemenGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemenGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private IllegalArgumentException createUnsupportedExc(JsPath path,
                                                        Class<?> clazz) {
    return new IllegalArgumentException("Generators for `%s` spec are not supported. User `override` parameter to provide a custom generator for the path %s".formatted(clazz.getName(),
                                                                                                                                                                        path));
  }

  private Gen<JsArray> createArrayOfBigIntGen(JsArrayOfBigInt spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    Gen<JsBigInt> elemGen = createJsBigIntGen(constraints);
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }

    return JsArrayGen.biased(elemGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );

  }

  private Gen<JsArray> createTupleGen(final JsTuple tuple,
                                      final Set<String> nameSpecsVisited,
                                      final JsPath path,
                                      final Map<JsPath, Gen<? extends JsValue>> overrides) {
    List<JsSpec> specs = tuple.specs;
    List<Gen<? extends JsValue>> gens = new ArrayList<>();
    for (int i = 0; i < specs.size(); i++) {
      JsSpec spec = specs.get(i);
      JsPath currentPath = path.index(i);
      Gen<? extends JsValue> gen = overrides.containsKey(currentPath) ?
                                   overrides.get(currentPath) :
                                   createGen(spec,
                                             overrides,
                                             path.index(i),
                                             nameSpecsVisited
                                            );
      gens.add(spec.isNullable() ?
               Combinators.oneOf(Gen.cons(JsNull.NULL),
                                 gen
                                ) :
               gen
              );
    }
    return JsTupleGen.of(gens);

  }

  private Gen<JsArray> createArrayOfSpecGen(JsArrayOfSpec spec,
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
      return JsArrayGen.biased(elemGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );

  }

  private Gen<JsArray> createArrayOfObjGen(JsArrayOfObj spec) {
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      return JsArrayGen.biased(objGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(objGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );


  }

  private Gen<JsArray> createArrayOfValueGen(final JsArrayOfValue spec) {

    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      return JsArrayGen.biased(primitiveValueGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(primitiveValueGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );

  }

  private Gen<JsArray> createArrayOfStrGen(final JsArrayOfStr spec,
                                           final JsPath path) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    Gen<? extends JsValue> elemGen = createJsStrGen(constraints,
                                                    path);
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private Gen<JsArray> createArrayOfBoolGen(final JsArrayOfBool spec) {
    var arrayConstraints = spec.arrayConstraints;
    Gen<? extends JsValue> elemGen = createJsBoolGen();
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );

  }

  private Gen<JsArray> createArrayOfDecGen(JsArrayOfDecimal spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    Gen<? extends JsValue> elemGen = createJsBigDecimalGen(constraints);
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private Gen<JsArray> createArrayOfDoubleGen(final JsArrayOfDouble spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    Gen<? extends JsValue> elemGen = createJsDoubleGen(constraints);
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );

  }

  private Gen<JsArray> createArrayOfLongGen(final JsArrayOfLong spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    Gen<? extends JsValue> elemGen = createJsLongGen(constraints);
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }

  private Gen<JsArray> createArrayOfIntGen(final JsArrayOfInt spec) {
    var intConstraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    Gen<? extends JsValue> elemGen = createJsIntGen(intConstraints);
    if (arrayConstraints == null) {
      return JsArrayGen.biased(elemGen,
                               conf.arraySize()
                                   .first(),
                               conf.arraySize()
                                   .second());
    }
    return JsArrayGen.biased(elemGen,
                             arrayConstraints.minItems(),
                             arrayConstraints.maxItems()
                            );
  }


  private Gen<JsBinary> createJsFixedBinaryGen(final int size) {
    return JsBinaryGen.arbitrary(size,
                                 size);
  }

  private Gen<JsInstant> createJsInstantGen(final InstantSchemaConstraints constraints) {
    return constraints == null ?
           JsInstantGen.biased() :
           JsInstantGen.biased(constraints.minimum()
                                          .getEpochSecond(),
                               constraints.maximum()
                                          .getEpochSecond()
                              );
  }

  private Gen<JsBigDec> createJsBigDecimalGenSuchThat(Function<BigDecimal, JsError> predicate) {
    return
        createJsBigDecimalGen(null).suchThat(n -> predicate.apply(n.toJsBigDec().value) == null);
  }

  private Gen<JsBigDec> createJsBigDecimalGen(final DecimalSchemaConstraints constraints) {
    return constraints == null ?
           JsBigDecGen.biased(conf.bigDecSize()
                                  .first(),
                              conf.bigDecSize()
                                  .second()) :
           JsBigDecGen.biased(constraints.minimum(),
                              constraints.maximum()
                             );
  }

  private Gen<JsBool> createJsBoolGen() {
    return JsBoolGen.arbitrary();
  }

  private Gen<JsBigInt> createJsBigIntGenSuchThat(Function<BigInteger, JsError> predicate) {
    return createJsBigIntGen(null).suchThat(n -> predicate.apply(n.toJsBigInt().value) == null);
  }

  private Gen<JsDouble> createJsDoubleSuchThatGen(DoubleFunction<JsError> predicate) {
    return createJsDoubleGen(null).suchThat(n -> predicate.apply(n.toJsDouble().value) == null);
  }

  private Gen<JsDouble> createJsDoubleGen(DoubleSchemaConstraints constraints) {
    return constraints == null ?
           JsDoubleGen.biased(conf.doubleSize()
                                  .first(),
                              conf.doubleSize()
                                  .second()) :
           JsDoubleGen.biased(constraints.minimum(),
                              constraints.maximum()
                             );

  }

  private Gen<JsStr> createEnumGen(final JsEnum jsEnum) {
    return Combinators.oneOf(jsEnum.symbols.stream()
                                           .map(it -> it.value()
                                                        .toJsStr())
                                           .toList());

  }

  private Gen<JsBigInt> createJsBigIntGen(BigIntSchemaConstraints constraints) {

    return constraints == null ?
           JsBigIntGen.biased(conf.bigIntSize()
                                  .first(),
                              conf.bigIntSize()
                                  .second()) :
           JsBigIntGen.biased(constraints.minimum(),
                              constraints.maximum()
                             );
  }


  private Gen<JsStr> createJsStrSuchThatGen(Function<String, JsError> predicate) {
    return createJsStrGen(null,
                          null).suchThat(n -> predicate.apply(n.toJsStr().value) == null);
  }

  private Gen<JsStr> createJsStrGen(StrConstraints constraints,
                                    JsPath path) {
    Gen<JsStr> gen;
    if (constraints == null) {
      gen = JsStrGen.alphabetic(conf.stringLength()
                                    .first(),
                                conf.stringLength()
                                    .second());
    } else if (constraints.pattern != null) {
      throw new IllegalArgumentException("Generators for regex patterns spec are not supported. Use `override` parameter to provide a custom generator for the path %s".formatted(path));
    } else {
      gen = JsStrGen.alphabetic(constraints.minLength,
                                constraints.maxLength
                               );
    }
    return gen;
  }

  private Gen<JsInt> createJsIntSuchThatGen(IntFunction<JsError> predicate) {
    return createJsIntGen(null).suchThat(n -> predicate.apply(n.toJsInt().value) == null);
  }


  private Gen<JsInt> createJsIntGen(IntegerSchemaConstraints constraints) {
    return constraints == null ?
           JsIntGen.biased(conf.intSize()
                               .first(),
                           conf.intSize()
                               .second()) :
           JsIntGen.biased(constraints.minimum(),
                           constraints.maximum());
  }

  private Gen<JsLong> createJsLongGen(LongSchemaConstraints constraints) {
    return constraints == null ?
           JsLongGen.biased(conf.longSize()
                                .first(),
                            conf.longSize()
                                .second()) :
           JsLongGen.biased(constraints.minimum(),
                            constraints.maximum());

  }

  private Gen<JsLong> createJsLongSuchThatGen(LongFunction<JsError> predicate) {
    return createJsLongGen(null).suchThat(n -> predicate.apply(n.toJsLong().value) == null);
  }

  private Gen<JsInstant> createJsInstantGenSuchThat(Function<Instant, JsError> predicate) {
    return createJsInstantGen(null).suchThat(n -> predicate.apply(n.toJsInstant().value) == null);
  }

  private Gen<JsBinary> createJsBinaryGenSuchThat(Function<byte[], JsError> predicate) {
    return createJsBinaryGen().suchThat(n -> predicate.apply(n.toJsBinary().value) == null);
  }


}