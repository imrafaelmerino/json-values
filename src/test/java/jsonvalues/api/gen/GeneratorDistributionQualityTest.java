package jsonvalues.api.gen;

import fun.gen.Gen;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import jsonvalues.JsArray;
import jsonvalues.JsDouble;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsDoubleGen;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsLongGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GeneratorDistributionQualityTest {

  @Test
  public void shouldGenerateAllValuesUniformlyInSmallBoundedIntRange() {
    Map<JsInt, Long> counts = FunTest.generate(600_000,
                                               JsIntGen.arbitrary(0,
                                                                  9));
    JsSpec boundedIntSpec = JsSpecs.integer(i -> i >= 0 && i <= 9);
    Assertions.assertTrue(JsIntGen.arbitrary(0,
                                             9)
                                  .sample(50_000)
                                  .allMatch(v -> boundedIntSpec.test(v)
                                                               .isEmpty()));

    var expectedValues = IntStream.rangeClosed(0,
                                               9)
                                  .mapToObj(JsInt::of)
                                  .toList();

    FunTest.assertGeneratedValuesHaveSameProbability(counts,
                                                     expectedValues,
                                                     0.08);
  }

  @Test
  public void shouldGenerateAllSizesUniformlyInSmallArrayRange() {
    Gen<JsArray> gen = JsArrayGen.arbitrary(JsStrGen.letter(),
                                            0,
                                            4);
    var elemSpec = JsSpecs.arrayOfStr(s -> s.length() == 1 && Character.isLetter(s.charAt(0)));
    var sizeSpec = JsSpecs.arraySuchThat(a -> a.size() <= 4);
    Assertions.assertTrue(gen.sample(50_000)
                             .allMatch(a -> elemSpec.test(a)
                                                    .isEmpty()
                                            && sizeSpec.test(a)
                                                       .isEmpty()));

    Map<Integer, Long> sizes = FunTest.generate(300_000,
                                                gen.map(JsArray::size));

    FunTest.assertGeneratedValuesHaveSameProbability(sizes,
                                                     FunTest.list(0,
                                                                  1,
                                                                  2,
                                                                  3,
                                                                  4),
                                                     0.10);
  }

  @Test
  public void shouldGenerateEveryOptionalCombinationForTwoKeys() {
    JsObjGen gen = JsObjGen.of("a",
                               Gen.cons(JsInt.of(1)),
                               "b",
                               Gen.cons(JsInt.of(2)))
                           .withOptKeys("a",
                                        "b");

    Map<Integer, Long> counts = countMasks(gen.sample(80_000),
                                           this::optionalMask);
    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.integer(),
                                  "b",
                                  JsSpecs.integer())
                              .withOptKeys("a",
                                           "b");
    Assertions.assertTrue(gen.sample(20_000)
                             .allMatch(obj -> spec.test(obj)
                                                  .isEmpty()));

    Assertions.assertEquals(4,
                            counts.size());
    Assertions.assertTrue(counts.containsKey(0)); // a and b present
    Assertions.assertTrue(counts.containsKey(1)); // a missing
    Assertions.assertTrue(counts.containsKey(2)); // b missing
    Assertions.assertTrue(counts.containsKey(3)); // both missing
  }

  @Test
  public void shouldGenerateEveryNullableCombinationForTwoKeys() {
    JsObjGen gen = JsObjGen.of("a",
                               Gen.cons(JsInt.of(1)),
                               "b",
                               Gen.cons(JsInt.of(2)))
                           .withNullValues("a",
                                           "b");

    Map<Integer, Long> counts = countMasks(gen.sample(80_000),
                                           this::nullableMask);
    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.integer().nullable(),
                                  "b",
                                  JsSpecs.integer().nullable());
    Assertions.assertTrue(gen.sample(20_000)
                             .allMatch(obj -> spec.test(obj)
                                                  .isEmpty()));

    Assertions.assertEquals(4,
                            counts.size());
    Assertions.assertTrue(counts.containsKey(0)); // none null
    Assertions.assertTrue(counts.containsKey(1)); // a null
    Assertions.assertTrue(counts.containsKey(2)); // b null
    Assertions.assertTrue(counts.containsKey(3)); // both null
  }

  @Test
  public void shouldGenerateAllValuesUniformlyInSmallBoundedLongRange() {
    Map<JsLong, Long> counts = FunTest.generate(400_000,
                                                JsLongGen.arbitrary(0,
                                                                    5));
    JsSpec boundedLongSpec = JsSpecs.longInteger(l -> l >= 0 && l <= 5);
    Assertions.assertTrue(JsLongGen.arbitrary(0,
                                              5)
                                   .sample(50_000)
                                   .allMatch(v -> boundedLongSpec.test(v)
                                                                 .isEmpty()));

    var expectedValues = IntStream.rangeClosed(0,
                                               5)
                                  .mapToObj(i -> JsLong.of((long) i))
                                  .toList();

    FunTest.assertGeneratedValuesHaveSameProbability(counts,
                                                     expectedValues,
                                                     0.10);
  }

  @Test
  public void shouldGenerateAllDigitsForSingleDigitGenerator() {
    Map<String, Long> counts = FunTest.generate(200_000,
                                                JsStrGen.digit()
                                                        .map(v -> v.value));
    JsSpec oneDigitSpec = JsSpecs.str(s -> s.length() == 1 && Character.isDigit(s.charAt(0)));
    Assertions.assertTrue(JsStrGen.digit()
                               .sample(50_000)
                               .allMatch(v -> oneDigitSpec.test(v)
                                                          .isEmpty()));

    FunTest.assertGeneratedValuesHaveSameProbability(counts,
                                                     FunTest.list("0",
                                                                  "1",
                                                                  "2",
                                                                  "3",
                                                                  "4",
                                                                  "5",
                                                                  "6",
                                                                  "7",
                                                                  "8",
                                                                  "9"),
                                                     0.12);
  }

  @Test
  public void shouldDistributeDoublesAcrossBucketsInArbitraryRange() {
    int samples = 400_000;
    JsSpec boundedDoubleSpec = JsSpecs.doubleNumber(d -> d >= 0.0 && d <= 1.0);
    Assertions.assertTrue(JsDoubleGen.arbitrary(0.0,
                                                1.0)
                                     .sample(50_000)
                                     .allMatch(v -> boundedDoubleSpec.test(v)
                                                                     .isEmpty()));

    Map<Integer, Long> buckets = FunTest.generate(samples,
                                                  JsDoubleGen.arbitrary(0.0,
                                                                        1.0)
                                                             .map(this::bucket));

    FunTest.assertGeneratedValuesHaveSameProbability(buckets,
                                                     FunTest.list(0,
                                                                  1,
                                                                  2,
                                                                  3,
                                                                  4,
                                                                  5,
                                                                  6,
                                                                  7,
                                                                  8,
                                                                  9),
                                                     0.15);
  }

  @Test
  public void shouldIncreaseOptionalOmissionRateWhenOptionalProbabilityGrows() {
    JsObjGen low = JsObjGen.of("a",
                               Gen.cons(JsInt.of(1)))
                           .withOptKeys("a")
                           .withOptionalProbability(2);
    JsObjGen high = JsObjGen.of("a",
                                Gen.cons(JsInt.of(1)))
                            .withOptKeys("a")
                            .withOptionalProbability(10);

    long lowMissing = low.sample(60_000)
                         .filter(obj -> !obj.containsKey("a"))
                         .count();
    long highMissing = high.sample(60_000)
                           .filter(obj -> !obj.containsKey("a"))
                           .count();

    Assertions.assertTrue(highMissing > lowMissing);
  }

  @Test
  public void shouldIncreaseNullableAssignmentsWhenNullableProbabilityGrows() {
    JsObjGen low = JsObjGen.of("a",
                               Gen.cons(JsInt.of(1)))
                           .withNullValues("a")
                           .withNullableProbability(2);
    JsObjGen high = JsObjGen.of("a",
                                Gen.cons(JsInt.of(1)))
                            .withNullValues("a")
                            .withNullableProbability(10);

    long lowNulls = low.sample(60_000)
                       .filter(obj -> obj.get("a") == JsNull.NULL)
                       .count();
    long highNulls = high.sample(60_000)
                         .filter(obj -> obj.get("a") == JsNull.NULL)
                         .count();

    Assertions.assertTrue(highNulls > lowNulls);
  }

  private int optionalMask(final JsObj obj) {
    int mask = 0;
    if (!obj.containsKey("a")) {
      mask |= 1;
    }
    if (!obj.containsKey("b")) {
      mask |= 2;
    }
    return mask;
  }

  private int nullableMask(final JsObj obj) {
    int mask = 0;
    if (obj.get("a") == JsNull.NULL) {
      mask |= 1;
    }
    if (obj.get("b") == JsNull.NULL) {
      mask |= 2;
    }
    return mask;
  }

  private Map<Integer, Long> countMasks(final java.util.stream.Stream<JsObj> stream,
                                        final java.util.function.ToIntFunction<JsObj> classifier
                                       ) {
    Map<Integer, Long> counts = new HashMap<>();
    stream.forEach(obj -> counts.merge(classifier.applyAsInt(obj),
                                       1L,
                                       Long::sum));
    return counts;
  }

  private int bucket(final JsDouble value) {
    double d = value.value;
    int bucket = (int) Math.floor(d * 10.0);
    if (bucket < 0) {
      return 0;
    }
    if (bucket > 9) {
      return 9;
    }
    return bucket;
  }
}
