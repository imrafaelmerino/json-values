package jsonvalues.api;

import fun.gen.Combinators;
import jsonvalues.JsInt;
import jsonvalues.JsStr;
import jsonvalues.gen.JsDoubleGen;
import jsonvalues.gen.JsStrGen;
import jsonvalues.gen.JsTupleGen;
import jsonvalues.spec.JsArraySpecParser;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OneOfTests {


    @Test
    public void test() {

        var spec = JsSpecs.tuple(JsSpecs.oneValOf(JsInt.of(1),
                                                  JsStr.of("cat")),
                                 JsSpecs.oneSpecOf(JsSpecs.str(),
                                                   JsSpecs.doubleNumber(d -> d > 1.5d)),
                                 JsSpecs.oneStringOf("a",
                                                     "b")
                                );

        var parser = JsArraySpecParser.of(spec);

        var gen = JsTupleGen.of(Combinators.oneOf(JsInt.of(1), JsStr.of("cat")),
                                Combinators.oneOf(JsStrGen.ascii(1, 10),
                                                  JsDoubleGen.arbitrary(1.6d, 10.0d)),
                                Combinators.oneOf("a", "b").map(JsStr::of)
                               );

        gen.sample(100).forEach(obj -> {
            Assertions.assertEquals(obj, parser.parse(obj.toPrettyString()));
            Assertions.assertTrue(spec.test(obj).isEmpty());
        });
    }
}
