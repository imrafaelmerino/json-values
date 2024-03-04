package jsonvalues.spec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SpecToGenTests {


  @Test
  public void test_primitives_without_constraints() {

    var spec = JsObjSpec.of("a",
                            JsSpecs.str(),
                            "b",
                            JsSpecs.decimal(),
                            "c",
                            JsSpecs.doubleNumber(),
                            "d",
                            JsSpecs.integer(),
                            "e",
                            JsSpecs.longInteger(),
                            "f",
                            JsSpecs.bool(),
                            "g",
                            JsSpecs.instant(),
                            "h",
                            JsObjSpec.of("a",
                                         JsSpecs.str(),
                                         "b",
                                         JsSpecs.decimal(),
                                         "c",
                                         JsSpecs.doubleNumber(),
                                         "d",
                                         JsSpecs.integer(),
                                         "e",
                                         JsSpecs.longInteger(),
                                         "f",
                                         JsSpecs.bool(),
                                         "g",
                                         JsSpecs.instant(),
                                         "h",
                                         JsSpecs.decimal(),
                                         "i",
                                         JsSpecs.bigInteger(),
                                         "j",
                                         JsSpecs.binary()
                                        )
                           );

    var gen = SpecToGen.convert(spec);
    var parser = JsObjSpecParser.of(spec);

    gen.sample(100)
       .forEach(generated -> {
         Assertions.assertEquals(generated,
                                 parser.parse(generated.toPrettyString()));

         Assertions.assertTrue(spec.test(generated)
                                   .isEmpty());
       });
  }

  @Test
  public void test_array_of_primitives_without_constraints() {

    var spec = JsObjSpec.of("a",
                            JsSpecs.arrayOfStr(),
                            "b",
                            JsSpecs.arrayOfDec(),
                            "c",
                            JsSpecs.arrayOfDouble(),
                            "d",
                            JsSpecs.arrayOfInt(),
                            "e",
                            JsSpecs.arrayOfLong(),
                            "f",
                            JsSpecs.arrayOfBool(),
                            "g",
                            JsSpecs.arrayOfSpec(JsSpecs.instant()),
                            "h",
                            JsObjSpec.of("a",
                                         JsSpecs.arrayOfStr(),
                                         "b",
                                         JsSpecs.arrayOfDec(),
                                         "c",
                                         JsSpecs.arrayOfDouble(),
                                         "d",
                                         JsSpecs.arrayOfInt(),
                                         "e",
                                         JsSpecs.arrayOfLong(),
                                         "f",
                                         JsSpecs.arrayOfBool(),
                                         "g",
                                         JsSpecs.arrayOfSpec(JsSpecs.instant()),
                                         "h",
                                         JsSpecs.arrayOfDec(),
                                         "i",
                                         JsSpecs.arrayOfBigInt(),
                                         "j",
                                         JsSpecs.arrayOfSpec(JsSpecs.binary()
                                                            )
                                        )
                           );

    var parser = JsObjSpecParser.of(spec);

    var gen = SpecToGen.convert(spec);

    gen.sample(100)
       .forEach(generated -> {
         Assertions.assertEquals(generated,
                                 parser.parse(generated.toPrettyString()));
         Assertions.assertTrue(spec.test(generated)
                                   .isEmpty());
       });
  }

  @Test
  public void test_maps(){
    JsObjSpec spec =
        JsObjSpec.of("a", JsSpecs.mapOfStr(),
                    "b", JsSpecs.mapOfDecimal(),
                    "c", JsSpecs.mapOfDouble(),
                    "d", JsSpecs.mapOfInteger(),
                    "e", JsSpecs.mapOfLong(),
                    "f", JsSpecs.mapOfBool(),
                    "g", JsSpecs.mapOfSpec(JsSpecs.instant()),
                    "h", JsObjSpec.of("a", JsSpecs.mapOfStr(),
                                     "b", JsSpecs.mapOfDecimal(),
                                     "c", JsSpecs.mapOfDouble(),
                                     "d", JsSpecs.mapOfInteger(),
                                     "e", JsSpecs.mapOfLong(),
                                     "f", JsSpecs.mapOfBool(),
                                     "g", JsSpecs.mapOfSpec(JsSpecs.instant()),
                                     "h", JsSpecs.mapOfDecimal(),
                                     "i", JsSpecs.mapOfBigInteger(),
                                     "j", JsSpecs.mapOfSpec(JsSpecs.binary())
                                    )
                   );

    var parser = JsObjSpecParser.of(spec);

    var gen = SpecToGen.convert(spec);

    gen.sample(100)
       .forEach(generated -> {
         Assertions.assertEquals(generated,
                                 parser.parse(generated.toPrettyString()));
         Assertions.assertTrue(spec.test(generated)
                                   .isEmpty());
       });
  }

}
