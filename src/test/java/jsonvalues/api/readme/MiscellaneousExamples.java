package jsonvalues.api.readme;

import static jsonvalues.spec.JsSpecs.arrayOfSpec;
import static jsonvalues.spec.JsSpecs.arrayOfStr;
import static jsonvalues.spec.JsSpecs.bool;
import static jsonvalues.spec.JsSpecs.decimal;
import static jsonvalues.spec.JsSpecs.integer;
import static jsonvalues.spec.JsSpecs.str;

import fun.gen.Gen;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsBigDecGen;
import jsonvalues.gen.JsBoolGen;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import jsonvalues.gen.JsTupleGen;
import jsonvalues.spec.JsArraySpec;
import jsonvalues.spec.JsArraySpecParser;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class MiscellaneousExamples {


  @Test
  public void basic() {

    JsObjSpec spec = JsObjSpec.of("firstName",
                                  str(),
                                  "lastName",
                                  str(),
                                  "age",
                                  integer(i -> i >= 0)
                                 )
                              .withOptKeys("firstName",
                                           "lastName",
                                           "age");

    Gen<JsObj> gen =
        JsObjGen.of("firstName",
                    JsStrGen.biased(0,
                                    100),
                    "lastName",
                    JsStrGen.biased(0,
                                    100),
                    "age",
                    JsIntGen.biased(0,
                                    200))
                .withOptKeys("firstName",
                             "lastName",
                             "age");

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertTrue(gen.sample(100000)
                             .allMatch(obj -> parser.parse(obj.toString())
                                                    .equals(obj)));


  }

  @Test
  public void testGeographicalCoordinates_Second_Option() {
    BigDecimal latMin = BigDecimal.valueOf(-90);
    BigDecimal latMax = BigDecimal.valueOf(90);
    BigDecimal longMin = BigDecimal.valueOf(-180);
    BigDecimal longMax = BigDecimal.valueOf(180);

    BiFunction<BigDecimal, BigDecimal, Predicate<BigDecimal>> between =
        (min, max) -> n -> n.compareTo(max) <= 0 && n.compareTo(min) >= 0;

    JsArraySpec spec = JsSpecs.tuple(decimal(between.apply(latMin,
                                                           latMax)),
                                     decimal(between.apply(longMin,
                                                           longMax))
                                    );

    Gen<JsArray> gen = JsTupleGen.of(JsBigDecGen.biased(latMin,
                                                        latMax),
                                     JsBigDecGen.biased(longMin,
                                                        longMax)
                                    );

    JsArraySpecParser parser = JsArraySpecParser.of(spec);

    Assertions.assertTrue(gen.sample(100000)
                             .allMatch(arr -> parser.parse(arr.toString())
                                                    .equals(arr)));


  }

  @Test
  public void testGeographicalCoordinates_First_Option() {
    BigDecimal latMin = BigDecimal.valueOf(-90);
    BigDecimal latMax = BigDecimal.valueOf(90);
    BigDecimal longMin = BigDecimal.valueOf(-180);
    BigDecimal longMax = BigDecimal.valueOf(180);

    BiFunction<BigDecimal, BigDecimal, Predicate<BigDecimal>> between =
        (min, max) -> n -> n.compareTo(max) <= 0 && n.compareTo(min) >= 0;

    JsObjSpec spec = JsObjSpec.of("latitude",
                                  decimal(between.apply(latMin,
                                                        latMax)),
                                  "longitude",
                                  decimal(between.apply(longMin,
                                                        longMax)));

    JsObjGen gen = JsObjGen.of("latitude",
                               JsBigDecGen.biased(latMin,
                                                  latMax),
                               "longitude",
                               JsBigDecGen.biased(longMin,
                                                  longMax)
                              );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertTrue(gen.sample(100000)
                             .allMatch(obj -> parser.parse(obj.toString())
                                                    .equals(obj)));


  }

  @Test
  public void testArrayOfThings() {
    JsObjSpec veggieSpec =
        JsObjSpec.of("veggieName",
                     str(),
                     "veggieLike",
                     bool());

    JsObjSpec spec =
        JsObjSpec.of("fruits",
                     arrayOfStr(),
                     "vegetables",
                     arrayOfSpec(veggieSpec));

    JsObjGen veggieGen =
        JsObjGen.of("veggieName",
                    JsStrGen.biased(0,
                                    50),
                    "veggieLike",
                    JsBoolGen.arbitrary());
    JsObjGen gen =
        JsObjGen.of("fruits",
                    JsArrayGen.biased(JsStrGen.biased(0,
                                                      20),
                                      0,
                                      10),
                    "vegetables",
                    JsArrayGen.biased(veggieGen,
                                      0,
                                      10)
                   );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertTrue(gen.sample(100000)
                             .allMatch(obj -> parser.parse(obj.toString())
                                                    .equals(obj)));

  }
}
