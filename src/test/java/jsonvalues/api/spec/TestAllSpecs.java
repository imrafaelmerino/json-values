package jsonvalues.api.spec;

import fun.gen.Combinators;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsBigDecGen;
import jsonvalues.gen.JsBigIntGen;
import jsonvalues.gen.JsBoolGen;
import jsonvalues.gen.JsInstantGen;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsLongGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import jsonvalues.spec.ArraySchema;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAllSpecs {

  @Test
  public void test() {

    JsObjSpec spec = JsObjSpec.of("a1",
                                  JsSpecs.str(),
                                  "a2",
                                  JsSpecs.bool(),
                                  "a3",
                                  JsSpecs.integer(),
                                  "a4",
                                  JsSpecs.longInteger(),
                                  "a5",
                                  JsSpecs.decimal(),
                                  "a6",
                                  JsSpecs.instant(),
                                  "a7",
                                  JsSpecs.arrayOfStr(ArraySchema.sizeBetween(1, 10)),
                                  "a8",
                                  JsSpecs.arrayOfInt(ArraySchema.sizeBetween(1, 10)),
                                  "a9",
                                  JsSpecs.arrayOfDec(),
                                  "a10",
                                  JsSpecs.arrayOfBigInt()
                                 );

    JsObjGen gen = JsObjGen.of("a1",
                               Combinators.oneOf(JsStrGen.arbitrary(10),
                                                 JsBoolGen.arbitrary()),
                               "a2",
                               Combinators.oneOf(JsBoolGen.arbitrary(),
                                                 JsIntGen.arbitrary()),
                               "a3",
                               Combinators.oneOf(JsIntGen.arbitrary(),
                                                 JsBoolGen.arbitrary()),
                               "a4",
                               Combinators.oneOf(JsLongGen.biased(),
                                                 JsBoolGen.arbitrary()),
                               "a5",
                               Combinators.oneOf(JsBigDecGen.biased(),
                                                 JsBoolGen.arbitrary()),
                               "a6",
                               Combinators.oneOf(JsInstantGen.arbitrary(),
                                                 JsBoolGen.arbitrary()),
                               "a7",
                               Combinators.oneOf(JsArrayGen.arbitrary(JsStrGen.arbitrary(10),
                                                                      1,
                                                                      10),
                                                 JsInstantGen.arbitrary()),
                               "a8",
                               Combinators.oneOf(JsArrayGen.arbitrary(JsIntGen.arbitrary(),
                                                                      1,
                                                                      10),
                                                 JsBoolGen.arbitrary()),
                               "a9",
                               Combinators.oneOf(JsArrayGen.arbitrary(JsBigDecGen.arbitrary(),
                                                                      1,
                                                                      10)),
                               "a10",
                               JsArrayGen.arbitrary(JsBigIntGen.arbitrary(),
                                                    1,
                                                    10)
                              )
                           .withAllNullValues()
                           .withAllOptKeys();

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertTrue(
        gen.suchThat(spec,
                     100000)
           .sample(100)
           .allMatch(it ->
                         parser.parse(it.toPrettyString())
                               .equals(it)
                    )
                         );

    Assertions.assertTrue(
        gen.suchThatNo(spec,
                       10000)
           .sample(100)
           .allMatch(it -> {
                       try {
                         parser.parse(it.toPrettyString());
                         return false;
                       } catch (Exception e) {
                         return true;
                       }
                     }
                    )
                         );

  }


}
