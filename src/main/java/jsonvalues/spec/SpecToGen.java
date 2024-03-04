package jsonvalues.spec;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.ListGen;
import fun.gen.SplitGen;
import fun.gen.StrGen;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsBool;
import jsonvalues.JsDouble;
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
 * Utility class for converting JSON specifications (JsObjSpec or JsArraySpec) to JSON schemas represented with a JsObj.
 * .
 */
public final class SpecToGen {


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
  public static JsObjGen convert(final JsObjSpec objSpec,
                                 Map<JsPath, Gen<? extends JsValue>> overrides) {
    var gen = JsObjGen.empty();
    var currentPath = JsPath.empty();
    for (var binding : objSpec.bindings.entrySet()) {
      JsPath path = currentPath.key(binding.getKey());
      Gen<? extends JsValue> keyGen =
          overrides.containsKey(path) ?
          overrides.get(path) :
          createGen(binding.getValue(),
                    overrides,
                    path
                   );
      gen = gen.set(binding.getKey(),
                    keyGen
                   );
    }
    return gen;
  }


  private static Gen<? extends JsValue> createGen(JsSpec spec,
                                                  Map<JsPath, Gen<? extends JsValue>> overrides,
                                                  JsPath currentPath) {
    return switch (spec) {
      case JsIntSpec s -> createJsIntGen(s.constraints,
                                         s.nullable);
      case JsIntSuchThat s -> createJsIntSuchThatGen(s.nullable,
                                                     s.predicate);
      case JsLongSpec s -> createJsLongGen(s.constraints,
                                           s.nullable);
      case JsLongSuchThat s -> createJsLongSuchThatGen(s.nullable,
                                                       s.predicate);
      case JsBigIntSpec s -> createJsBigIntGen(s.constraints,
                                               s.nullable);
      case JsBigIntSuchThat s -> createJsBigIntGenSuchThat(s.nullable,
                                                           s.predicate);
      case JsDoubleSpec s -> createJsDoubleGen(s.constraints,
                                               s.nullable);
      case JsDoubleSuchThat s -> createJsDoubleSuchThatGen(s.nullable,
                                                           s.predicate);
      case JsDecimalSpec s -> createJsBigDecimalGen(s.constraints,
                                                    s.nullable);
      case JsDecimalSuchThat s -> createJsBigDecimalGenSuchThat(s.nullable,
                                                                s.predicate);
      case JsBooleanSpec s -> createJsBoolGen(s.nullable);
      case JsStrSpec s -> createJsStrGen(s.constraints,
                                         s.nullable,
                                         currentPath);
      case JsStrSuchThat s -> createJsStrSuchThatGen(s.nullable,
                                                     s.predicate);
      case JsInstantSpec s -> createJsInstantGen(s.nullable,
                                                 s.constraints);
      case JsInstantSuchThat s -> null;
      case JsBinarySpec s -> createJsBinarySpec(s.nullable);
      case JsBinarySuchThat s -> null;
      case JsFixedBinary s -> createJsFixedBinaryGen(s.getSize(),
                                                     s.nullable);
      case JsEnum jsEnum -> createEnumGen(jsEnum);
      case AnySpec ignored -> null;
      case AnySuchThat ignored -> null;
      case JsArraySpec s -> createJsArrayGen(s.isNullable(),
                                             s,
                                             currentPath,
                                             overrides);
      case JsObjSpec s -> convert(s,
                                  overrides);
      case IsJsObj ignored -> null;
      case JsObjSuchThat ignored -> null;
      case JsMapOfBigInt s -> createMapOfBigIntGen(s.nullable,
                                                   s.valuesConstraints);
      case JsMapOfInt s -> createMapOfIntGen(s.nullable,
                                             s.valuesConstraints);
      case JsMapOfLong s -> createMapOfLongtGen(s.nullable,
                                                s.valuesConstraints);

      case JsMapOfDouble s -> createMapOfDoubleGen(s.nullable,
                                                   s.valuesConstraints);

      case JsMapOfDec s -> createMapOfDecGen(s.nullable,
                                             s.valuesConstraints);

      case JsMapOfBinary s -> createMapOfBinaryGen(s.nullable);

      case JsMapOfBool s -> createMapOfBoolGen(s.nullable);

      case JsMapOfInstant s -> createMapOfInstantGen(s.nullable,
                                                     s.valuesConstraints);

      case JsMapOfSpec s -> createMapOfSpecGen(s.nullable,
                                               s.getValueSpec(),
                                               overrides,
                                               currentPath);

      case JsMapOfStr s -> createMapOfStrGen(s.nullable,
                                             s.valuesConstraints,
                                             currentPath);

      case NamedSpec namedSpec -> null;
      case OneOf oneOf -> null;
      default -> throw new IllegalArgumentException();
    };

  }

  private static Gen<? extends JsValue> createMapOfBigIntGen(final boolean nullable,
                                                             final BigIntSchemaConstraints valuesConstraints) {

    var keyGen = StrGen.alphabetic(1,
                                   10)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 1,
                                 20);
    var value = createJsBigIntGen(valuesConstraints,
                                  false);

    return mapGen(nullable,
                  keys,
                  value);
  }

  private static Gen<? extends JsValue> createMapOfStrGen(boolean nullable,
                                                          StrConstraints valuesConstraints,
                                                          JsPath path) {
    var keyGen = StrGen.alphabetic(1,
                                   10)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 1,
                                 20);
    var value = createJsStrGen(valuesConstraints,
                               nullable,
                               path);

    return mapGen(nullable,
                  keys,
                  value);
  }

  private static Gen<? extends JsValue> mapGen(final boolean nullable,
                                               final Gen<List<String>> keys,
                                               final Gen<? extends JsValue> value) {
    Gen<JsValue> gen = random -> {
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
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             gen) :
           gen;
  }

  private static Gen<? extends JsValue> createMapOfSpecGen(final boolean nullable,
                                                           final JsSpec valueSpec,
                                                           final Map<JsPath, Gen<? extends JsValue>> overrides,
                                                           final JsPath path) {
    var keyGen = StrGen.alphabetic(1,
                                   10)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 1,
                                 20);
    var value = createGen(valueSpec,
                          overrides,
                          path
                         );

    return mapGen(nullable,
                  keys,
                  value);
  }

  private static Gen<? extends JsValue> createMapOfInstantGen(final boolean nullable,
                                                              final InstantSchemaConstraints valuesConstraints) {

    var keyGen = StrGen.alphabetic(1,
                                   10)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 1,
                                 20);
    var value = createJsInstantGen(nullable,
                                   valuesConstraints);

    return mapGen(nullable,
                  keys,
                  value);


  }

  private static Gen<? extends JsValue> createMapOfBoolGen(final boolean nullable) {
    var keyGen = StrGen.alphabetic(1,
                                   10)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 1,
                                 20);
    var value = JsBoolGen.arbitrary();

    return mapGen(nullable,
                  keys,
                  value);

  }

  private static Gen<? extends JsValue> createMapOfBinaryGen(final boolean nullable) {
    var keyGen = StrGen.alphabetic(1,
                                   10)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 1,
                                 20);
    //TODO binary constraints
    var value = JsBinaryGen.biased(0,
                                   50);

    return mapGen(nullable,
                  keys,
                  value);

  }

  private static Gen<? extends JsValue> createMapOfDecGen(final boolean nullable,
                                                          final DecimalSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(1,
                                   10)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 1,
                                 20);
    var value = createJsBigDecimalGen(valuesConstraints,
                                      false);

    return mapGen(nullable,
                  keys,
                  value);

  }

  private static Gen<? extends JsValue> createMapOfDoubleGen(final boolean nullable,
                                                             final DoubleSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(1,
                                   10)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 1,
                                 20);

    var value = createJsDoubleGen(valuesConstraints,
                                  false);

    return mapGen(nullable,
                  keys,
                  value);
  }

  private static Gen<? extends JsValue> createMapOfLongtGen(final boolean nullable,
                                                            final LongSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(1,
                                   10)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 1,
                                 20);
    var value = createJsLongGen(valuesConstraints,
                                false);

    return mapGen(nullable,
                  keys,
                  value);
  }

  private static Gen<? extends JsValue> createMapOfIntGen(final boolean nullable,
                                                          final IntegerSchemaConstraints valuesConstraints) {
    var keyGen = StrGen.alphabetic(1,
                                   10)
                       .distinct();
    var keys = ListGen.arbitrary(keyGen,
                                 1,
                                 20);
    var value = createJsIntGen(valuesConstraints,
                               false);

    return mapGen(nullable,
                  keys,
                  value);

  }

  private static Gen<? extends JsValue> createJsBinarySpec(final boolean nullable) {

    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             JsBinaryGen.arbitrary(0,
                                                   100)
                            ) :
           JsBinaryGen.arbitrary(0,
                                 100);
  }

  private static Gen<? extends JsValue> createJsArrayGen(boolean nullable,
                                                         JsArraySpec spec,
                                                         JsPath path,
                                                         Map<JsPath, Gen<? extends JsValue>> overrides) {

    return switch (spec) {
      case JsArrayOfInt s -> getSizableArrayOfIntGen(nullable,
                                                     s);
      case JsArrayOfTestedInt s -> null;
      case JsArrayOfIntSuchThat s -> null;

      case JsArrayOfLong s -> getSizableArrayOfLongGen(nullable,
                                                       s);
      case JsArrayOfTestedLong s -> null;
      case JsArrayOfLongSuchThat s -> null;

      case JsArrayOfBigInt s -> getSizableArrayOfBigIntGen(nullable,
                                                           s);
      case JsArrayOfTestedBigInt s -> null;
      case JsArrayOfBigIntSuchThat s -> null;

      case JsArrayOfDouble s -> getSizableArrayOfDoubleGen(nullable,
                                                           s);
      case JsArrayOfTestedDouble s -> null;
      case JsArrayOfDoubleSuchThat s -> null;

      case JsArrayOfDecimal s -> getSizableArrayOfDecGen(nullable,
                                                         s);
      case JsArrayOfTestedDecimal s -> null;
      case JsArrayOfDecimalSuchThat s -> null;

      case JsArrayOfBool s -> getSizableArrayOfBoolGen(nullable,
                                                       s);
      case JsArrayOfBoolSuchThat s -> null;

      case JsArrayOfStr s -> getSizableArrayOfStrGen(nullable,
                                                     s,
                                                     path);
      case JsArrayOfStrSuchThat s -> null;

      case JsArrayOfTestedStr s -> null;

      case JsArrayOfValue s -> getSizableArrayOfValueGen(nullable,
                                                         s);
      case JsArrayOfTestedValue s -> null;
      case JsArraySuchThat s -> null;

      case JsArrayOfObj s -> getSizableArrayOfObjGen(nullable,
                                                     s);
      case JsArrayOfObjSuchThat s -> null;
      case JsArrayOfTestedObj s -> null;

      case JsArrayOfSpec s -> getSizableArrayOfSpecGen(nullable,
                                                       s,
                                                       overrides,
                                                       path);
      case JsTuple tuple -> getTupleGen(nullable,
                                        tuple);
    };
  }

  private static Gen<? extends JsValue> getSizableArrayOfBigIntGen(final boolean nullable,
                                                                   final JsArrayOfBigInt spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      Gen<JsArray> arrGen =
          IntGen.arbitrary(0,
                           100)
                .then(max -> JsArrayGen.biased(createJsBigIntGen(constraints,
                                                                 false),
                                               0,
                                               max)
                     );
      return nullable ?
             Combinators.oneOf(Gen.cons(JsNull.NULL),
                               arrGen) :
             IntGen.arbitrary(0,
                              100)
                   .then(max -> JsArrayGen.biased(createJsBigIntGen(constraints,
                                                                    false),
                                                  0,
                                                  max)
                        );
    }

    Gen<JsArray> arrGen =
        JsArrayGen.biased(createJsBigIntGen(constraints,
                                            false),
                          arrayConstraints.minItems(),
                          arrayConstraints.maxItems()
                         );
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             arrGen) :
           arrGen;

  }

  private static Gen<? extends JsValue> getTupleGen(final boolean nullable,
                                                    final JsTuple tuple) {
    List<JsSpec> specs = tuple.specs;
    List<Gen<? extends JsValue>> gens = new ArrayList<>();
    for (JsSpec spec : specs) {
      gens.add(createGen(spec,
                         Map.of(),
                         JsPath.empty()));
    }
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             JsTupleGen.of(gens)) :
           JsTupleGen.of(gens);

  }

  private static Gen<? extends JsValue> getSizableArrayOfSpecGen(final boolean nullable,
                                                                 final JsArrayOfSpec spec,
                                                                 Map<JsPath, Gen<? extends JsValue>> overrides,
                                                                 JsPath path) {
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      Gen<JsArray> arrGen = IntGen.arbitrary(0,
                                             100)
                                  .then(max -> JsArrayGen.biased(createGen(spec.getElemSpec(),
                                                                           overrides,
                                                                           path),
                                                                 0,
                                                                 max)
                                       );
      return nullable ?
             Combinators.oneOf(Gen.cons(JsNull.NULL),
                               arrGen) :
             IntGen.arbitrary(0,
                              100)
                   .then(max -> JsArrayGen.biased(createGen(spec.getElemSpec(),
                                                            overrides,
                                                            path),
                                                  0,
                                                  max)
                        );
    }

    Gen<JsArray> arrGen = JsArrayGen.biased(createGen(spec.getElemSpec(),
                                                      overrides,
                                                      path),
                                            arrayConstraints.minItems(),
                                            arrayConstraints.maxItems()
                                           );
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             arrGen) :
           arrGen;

  }

  private static Gen<? extends JsValue> getSizableArrayOfObjGen(final boolean nullable,
                                                                final JsArrayOfObj spec) {
    var objGen = Combinators.oneOf(
        createMapOfDoubleGen(false,
                             null),
        createMapOfStrGen(false,
                          null,
                          null),
        createMapOfIntGen(false,
                          null),
        createMapOfLongtGen(false,
                            null),
        createMapOfDoubleGen(false,
                             null),
        createMapOfInstantGen(false,
                              null),
        createMapOfBoolGen(false));
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      Gen<JsArray> arrGen = IntGen.arbitrary(0,
                                             100)
                                  .then(max -> JsArrayGen.biased(objGen,
                                                                 0,
                                                                 max)
                                       );
      return nullable ?
             Combinators.oneOf(Gen.cons(JsNull.NULL),
                               arrGen) :
             IntGen.arbitrary(0,
                              100)
                   .then(max -> JsArrayGen.biased(objGen,
                                                  0,
                                                  max)
                        );
    }

    var arrGen = JsArrayGen.biased(objGen,
                                   arrayConstraints.minItems(),
                                   arrayConstraints.maxItems()
                                  );
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             arrGen) :
           arrGen;

  }

  private static Gen<? extends JsValue> getSizableArrayOfValueGen(final boolean nullable,
                                                                  final JsArrayOfValue spec) {
    var valueGen = Combinators.oneOf(
        createJsDoubleGen(null,
                          false),
        createJsBigDecimalGen(null,
                              false),
        createJsLongGen(null,
                        false),
        createJsIntGen(null,
                       false),
        createJsStrGen(null,
                       false,
                       null),
        createJsBoolGen(false),
        createJsBinarySpec(false),
        createJsInstantGen(false,
                           null)
                                    );

    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      Gen<JsArray> arrGen = IntGen.arbitrary(0,
                                             100)
                                  .then(max -> JsArrayGen.biased(valueGen,
                                                                 0,
                                                                 max)
                                       );
      return nullable ?
             Combinators.oneOf(Gen.cons(JsNull.NULL),
                               arrGen) :
             IntGen.arbitrary(0,
                              100)
                   .then(max -> JsArrayGen.biased(valueGen,
                                                  0,
                                                  max)
                        );
    }

    Gen<JsArray> arrGen = JsArrayGen.biased(valueGen,
                                            arrayConstraints.minItems(),
                                            arrayConstraints.maxItems()
                                           );
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             arrGen) :
           arrGen;
  }

  private static Gen<? extends JsValue> getSizableArrayOfStrGen(final boolean nullable,
                                                                final JsArrayOfStr spec,
                                                                final JsPath path) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      Gen<JsArray> arrGen = IntGen.arbitrary(0,
                                             100)
                                  .then(max -> JsArrayGen.biased(createJsStrGen(constraints,
                                                                                false,
                                                                                path),
                                                                 0,
                                                                 max)
                                       );
      return nullable ?
             Combinators.oneOf(Gen.cons(JsNull.NULL),
                               arrGen) :
             IntGen.arbitrary(0,
                              100)
                   .then(max -> JsArrayGen.biased(createJsStrGen(constraints,
                                                                 false,
                                                                 path),
                                                  0,
                                                  max)
                        );
    }

    Gen<JsArray> arrGen = JsArrayGen.biased(createJsStrGen(constraints,
                                                           false,
                                                           path),
                                            arrayConstraints.minItems(),
                                            arrayConstraints.maxItems()
                                           );
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             arrGen) :
           arrGen;

  }

  private static Gen<? extends JsValue> getSizableArrayOfBoolGen(final boolean nullable,
                                                                 final JsArrayOfBool spec) {
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      Gen<JsArray> arrGen = IntGen.arbitrary(0,
                                             100)
                                  .then(max -> JsArrayGen.biased(createJsBoolGen(false),
                                                                 0,
                                                                 max)
                                       );
      return nullable ?
             Combinators.oneOf(Gen.cons(JsNull.NULL),
                               arrGen) :
             IntGen.arbitrary(0,
                              100)
                   .then(max -> JsArrayGen.biased(createJsBoolGen(false),
                                                  0,
                                                  max));
    }

    Gen<JsArray> arrGen = JsArrayGen.biased(createJsBoolGen(false),
                                            arrayConstraints.minItems(),
                                            arrayConstraints.maxItems()
                                           );
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             arrGen) :
           arrGen;
  }

  private static Gen<? extends JsValue> getSizableArrayOfDecGen(final boolean nullable,
                                                                final JsArrayOfDecimal spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      Gen<JsArray> arrGen =
          IntGen.arbitrary(0,
                           100)
                .then(max -> JsArrayGen.biased(createJsBigDecimalGen(constraints,
                                                                     false),
                                               0,
                                               max)
                     );
      return nullable ?
             Combinators.oneOf(Gen.cons(JsNull.NULL),
                               arrGen) :
             IntGen.arbitrary(0,
                              100)
                   .then(max -> JsArrayGen.biased(createJsBigDecimalGen(constraints,
                                                                        false),
                                                  0,
                                                  max));
    }

    Gen<JsArray> arrGen = JsArrayGen.biased(createJsBigDecimalGen(constraints,
                                                                  false),
                                            arrayConstraints.minItems(),
                                            arrayConstraints.maxItems()
                                           );
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             arrGen) :
           arrGen;

  }

  private static Gen<? extends JsValue> getSizableArrayOfDoubleGen(final boolean nullable,
                                                                   final JsArrayOfDouble spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      Gen<JsArray> arrGen =
          IntGen.arbitrary(0,
                           100)
                .then(max -> JsArrayGen.biased(createJsDoubleGen(constraints,
                                                                 false),
                                               0,
                                               max)
                     );
      return nullable ?
             Combinators.oneOf(Gen.cons(JsNull.NULL),
                               arrGen) :
             IntGen.arbitrary(0,
                              100)
                   .then(max -> JsArrayGen.biased(createJsDoubleGen(constraints,
                                                                    false),
                                                  0,
                                                  max));
    }

    Gen<JsArray> arrGen = JsArrayGen.biased(createJsDoubleGen(constraints,
                                                              false),
                                            arrayConstraints.minItems(),
                                            arrayConstraints.maxItems()
                                           );
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             arrGen) :
           arrGen;
  }

  private static Gen<? extends JsValue> getSizableArrayOfLongGen(final boolean nullable,
                                                                 final JsArrayOfLong spec) {
    var constraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      Gen<JsArray> arrGen =
          IntGen.arbitrary(0,
                           100)
                .then(max -> JsArrayGen.biased(createJsLongGen(constraints,
                                                               false),
                                               0,
                                               max)
                     );
      return nullable ?
             Combinators.oneOf(Gen.cons(JsNull.NULL),
                               arrGen) :
             IntGen.arbitrary(0,
                              100)
                   .then(max -> JsArrayGen.biased(createJsLongGen(constraints,
                                                                  false),
                                                  0,
                                                  max));
    }

    Gen<JsArray> arrGen = JsArrayGen.biased(createJsLongGen(constraints,
                                                            false),
                                            arrayConstraints.minItems(),
                                            arrayConstraints.maxItems()
                                           );
    return nullable ? Combinators.oneOf(Gen.cons(JsNull.NULL),
                                        arrGen) :
           arrGen;
  }

  private static Gen<? extends JsValue> getSizableArrayOfIntGen(final boolean nullable,
                                                                final JsArrayOfInt spec) {
    var intConstraints = spec.constraints;
    var arrayConstraints = spec.arrayConstraints;
    if (arrayConstraints == null) {
      Gen<JsArray> arrGen = IntGen.arbitrary(0,
                                             100)
                                  .then(max -> JsArrayGen.biased(createJsIntGen(intConstraints,
                                                                                false),
                                                                 0,
                                                                 max)
                                       );
      return nullable ?
             Combinators.oneOf(Gen.cons(JsNull.NULL),
                               arrGen) :
             IntGen.arbitrary(0,
                              100)
                   .then(max -> JsArrayGen.biased(createJsIntGen(intConstraints,
                                                                 false),
                                                  0,
                                                  max));
    }

    Gen<JsArray> arrGen = JsArrayGen.biased(createJsIntGen(intConstraints,
                                                           false),
                                            arrayConstraints.minItems(),
                                            arrayConstraints.maxItems()
                                           );
    return nullable ? Combinators.oneOf(Gen.cons(JsNull.NULL),
                                        arrGen) :
           arrGen;

  }


  private static Gen<? extends JsValue> createJsFixedBinaryGen(final int size,
                                                               final boolean nullable) {
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             JsBinaryGen.arbitrary(size,
                                                   size)) :
           JsBinaryGen.arbitrary(size,
                                 size);
  }

  private static Gen<? extends JsValue> createJsInstantGen(final boolean nullable,
                                                           final InstantSchemaConstraints constraints) {
    if (constraints == null) {
      return JsInstantGen.biased();
    }
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             JsInstantGen.biased(constraints.minimum()
                                                            .getEpochSecond(),
                                                 constraints.maximum()
                                                            .getEpochSecond()
                                                )
                            ) :
           JsInstantGen.biased(constraints.minimum()
                                          .getEpochSecond(),
                               constraints.maximum()
                                          .getEpochSecond()
                              );
  }

  private static Gen<? extends JsValue> createJsBigDecimalGenSuchThat(final boolean nullable,
                                                                      final Function<BigDecimal, JsError> predicate) {
    return createJsBigDecimalGen(null,
                                 nullable)
        .suchThat(n -> n == JsNull.NULL || predicate.apply(n.toJsBigDec().value) == null);
  }

  private static Gen<? extends JsValue> createJsBigDecimalGen(final DecimalSchemaConstraints constraints,
                                                              final boolean nullable) {
    Gen<JsBigDec> gen;
    if (constraints == null) {
      gen = JsBigDecGen.biased();
    } else {
      gen = JsBigDecGen.biased(constraints.minimum(),
                               constraints.maximum()
                              );
    }
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             gen
                            ) :
           gen;
  }

  private static Gen<? extends JsValue> createJsBoolGen(final boolean nullable) {
    Gen<JsBool> gen = JsBoolGen.arbitrary();
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             gen
                            ) :
           gen;
  }

  private static Gen<? extends JsValue> createJsBigIntGenSuchThat(final boolean nullable,
                                                                  final Function<BigInteger, JsError> predicate) {
    return createJsBigIntGen(null,
                             nullable)
        .suchThat(n -> n == JsNull.NULL || predicate.apply(n.toJsBigInt().value) == null);
  }

  private static Gen<? extends JsValue> createJsDoubleSuchThatGen(final boolean nullable,
                                                                  final DoubleFunction<JsError> predicate) {

    return createJsDoubleGen(null,
                             nullable)
        .suchThat(n -> n == JsNull.NULL || predicate.apply(n.toJsDouble().value) == null);
  }

  private static Gen<? extends JsValue> createJsDoubleGen(final DoubleSchemaConstraints constraints,
                                                          final boolean nullable) {
    Gen<JsDouble> gen;
    if (constraints == null) {
      gen = JsDoubleGen.biased();
    } else {
      //todo multiple of
      gen = JsDoubleGen.biased(constraints.minimum(),
                               constraints.maximum()
                              );
    }
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             gen
                            ) :
           gen;

  }

  private static Gen<? extends JsValue> createEnumGen(final JsEnum jsEnum) {
    Gen<JsStr> symbolGen = Combinators.oneOf(jsEnum.symbols.stream()
                                                           .map(it -> it.value()
                                                                        .toJsStr())
                                                           .toList());
    return jsEnum.nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             symbolGen
                            ) :
           symbolGen;

  }

  private static Gen<? extends JsValue> createJsBigIntGen(final BigIntSchemaConstraints constraints,
                                                          final boolean nullable) {

    Gen<JsBigInt> gen;
    if (constraints == null) {
      gen = JsBigIntGen.biased();
    } else {
      //todo multiple of
      gen = JsBigIntGen.biased(constraints.minimum(),
                               constraints.maximum()
                              );
    }
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             gen
                            ) :
           gen;

  }


  private static Gen<? extends JsValue> createJsStrSuchThatGen(boolean nullable,
                                                               Function<String, JsError> predicate) {
    return createJsStrGen(null,
                          nullable,
                          null)
        .suchThat(n -> n == JsNull.NULL || predicate.apply(n.toJsStr().value) == null);
  }

  private static Gen<? extends JsValue> createJsStrGen(StrConstraints constraints,
                                                       boolean nullable,
                                                       JsPath path) {
    Gen<JsStr> gen;
    if (constraints == null) {
      gen = IntGen.arbitrary(0,
                             100)
                  .then(max -> JsStrGen.alphabetic(0,
                                                   max));
    } else if (constraints.pattern != null) {
      throw new IllegalArgumentException("Generators for regex patterns are not yet implemented. User `override` parameter to provide a custom generator for the path %s".formatted(path));
    } else {
      gen = JsStrGen.alphanumeric(constraints.minLength,
                                  constraints.maxLength
                                 );
    }
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             gen) :
           gen;
  }

  private static Gen<? extends JsValue> createJsIntSuchThatGen(boolean nullable,
                                                               IntFunction<JsError> predicate) {
    return createJsIntGen(null,
                          nullable)
        .suchThat(n -> n == JsNull.NULL || predicate.apply(n.toJsInt().value) == null);
  }

  private static Gen<? extends JsValue> createJsIntGen(IntegerSchemaConstraints constraints,
                                                       boolean nullable) {
    Gen<JsInt> gen;
    if (constraints == null) {
      gen = JsIntGen.biased();
    } else {
      //todo multiple of
      gen = JsIntGen.biased(constraints.minimum(),
                            constraints.maximum()
                           );
    }
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             gen
                            ) :
           gen;
  }

  private static Gen<? extends JsValue> createJsLongGen(LongSchemaConstraints constraints,
                                                        boolean nullable) {
    Gen<JsLong> gen;
    if (constraints == null) {
      gen = JsLongGen.biased();
    } else {
      //todo multiple of
      gen = JsLongGen.biased(constraints.minimum(),
                             constraints.maximum()
                            );
    }
    return nullable ?
           Combinators.oneOf(Gen.cons(JsNull.NULL),
                             gen
                            ) :
           gen;
  }

  private static Gen<? extends JsValue> createJsLongSuchThatGen(boolean nullable,
                                                                LongFunction<JsError> predicate) {
    return createJsLongGen(null,
                           nullable)
        .suchThat(n -> n == JsNull.NULL || predicate.apply(n.toJsLong().value) == null);
  }


}