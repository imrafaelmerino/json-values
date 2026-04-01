package jsonvalues.api.spec;

import fun.gen.Combinators;
import java.util.ArrayList;
import java.util.List;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsBoolGen;
import jsonvalues.gen.JsDoubleGen;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import jsonvalues.spec.JsArraySpecParser;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OneOfSpecTest {


  @Test
  void shouldParseObjectWhenAnyOneOfBranchMatches() {

    var spec = JsSpecs.oneSpecOf(List.of(JsObjSpec.of("a",
                                                      JsSpecs.integer(),
                                                      "b",
                                                      JsSpecs.str(),
                                                      "c",
                                                      JsSpecs.oneSpecOf(List.of(JsSpecs.bool(),
                                                                                JsSpecs.str()))
                                                     ),
                                         JsObjSpec.of("a",
                                                      JsSpecs.integer(),
                                                      "b",
                                                      JsSpecs.str(),
                                                      "c",
                                                      JsSpecs.oneSpecOf(List.of(JsSpecs.bool(),
                                                                                JsSpecs.integer()))
                                                     )
                                        )
                                );

    JsObj obj = JsObj.of("a",
                         JsInt.of(1),
                         "b",
                         JsStr.of("hi"),
                         "c",
                         JsInt.of(3)
                        );
    JsObjSpecParser parser = JsObjSpecParser.of(spec);
    var obj1 = parser.parse(obj.toString());

    Assertions.assertEquals(obj,
                            obj1);


  }

  @Test
  void shouldParseArrayWhenAnyOneOfBranchMatches() {

    JsSpec spec = JsSpecs.oneSpecOf(JsSpecs.oneSpecOf(JsSpecs.arrayOfStr(),
                                                      JsSpecs.arrayOfDouble()),
                                    JsSpecs.oneSpecOf(JsSpecs.arrayOfStr(),
                                                      JsSpecs.arrayOfInt())
                                   );

    JsArraySpecParser parser = JsArraySpecParser.of(spec);

    var gen = Combinators.oneOf(Combinators.oneOf(JsArrayGen.ofN(JsDoubleGen.arbitrary(),
                                                                 10),
                                                  JsArrayGen.ofN(JsStrGen.alphabetic(),
                                                                 10)),
                                Combinators.oneOf(JsArrayGen.ofN(JsStrGen.alphabetic(),
                                                                 10),
                                                  JsArrayGen.ofN(JsIntGen.arbitrary(),
                                                                 10))
                               );

    gen.sample(1000)
       .forEach(obj ->
                    Assertions.assertEquals(obj,
                                            parser.parse(obj.toString()))
               );


  }

  @Test
  void shouldParseObjectsForNestedOneOfCombinations() {

    JsObjSpec spec1 = JsObjSpec.of("a",
                                   JsSpecs.str());
    JsObjSpec spec2 = JsObjSpec.of("a",
                                   JsSpecs.integer());
    JsObjSpec spec3 = JsObjSpec.of("a",
                                   JsSpecs.doubleNumber());
    JsObjSpec spec4 = JsObjSpec.of("a",
                                   JsSpecs.bool());

    JsSpec spec = JsSpecs.oneSpecOf(JsSpecs.oneSpecOf(spec1,
                                                      spec2),
                                    JsSpecs.oneSpecOf(spec3,
                                                      spec4)
                                   );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    JsObjGen gen1 = JsObjGen.of("a",
                                JsStrGen.alphabetic());
    JsObjGen gen2 = JsObjGen.of("a",
                                JsIntGen.arbitrary());
    JsObjGen gen3 = JsObjGen.of("a",
                                JsDoubleGen.arbitrary());
    JsObjGen gen4 = JsObjGen.of("a",
                                JsBoolGen.arbitrary());

    var gen = Combinators.oneOf(Combinators.oneOf(gen1,
                                                  gen2),
                                Combinators.oneOf(gen3,
                                                  gen4)
                               );

    gen.sample(1000)
       .forEach(obj ->
                    Assertions.assertEquals(obj,
                                            parser.parse(obj.toString()))
               );


  }

  @Test
  void shouldRejectEmptySpecListInOneOfFactory() {
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsSpecs.oneSpecOf(List.of()));
  }

  @Test
  void shouldRejectNullSpecsInOneOfFactoryList() {
    List<JsSpec> specs = new ArrayList<>();
    specs.add(JsSpecs.str());
    specs.add(null);
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsSpecs.oneSpecOf(specs));
  }
}
