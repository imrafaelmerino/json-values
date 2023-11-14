package jsonvalues.api;

import fun.gen.StrGen;
import jsonvalues.JsBool;
import jsonvalues.JsInt;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecBuilder;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TestAliasSpec {

   static Supplier<String> nameGen = StrGen.alphabetic(10, 10).sample();

    static JsObjSpec spec =
            JsObjSpecBuilder.withName(nameGen.get())
                            .withFieldAliases(Map.of("a", List.of("a1", "a2")))
                            .build(JsObjSpec.of("a", JsSpecs.integer().nullable()));

    static JsObjSpecParser parser = JsObjSpecParser.of(spec);

    @Test
    public void testNotErrorBecauseAliasIsFound() {
        Assertions.assertTrue(spec.test(JsObj.of("a", JsInt.of(1))).isEmpty());
        Assertions.assertTrue(spec.test(JsObj.of("a", JsNull.NULL)).isEmpty());
        Assertions.assertFalse(spec.test(JsObj.of("a", JsBool.FALSE)).isEmpty());

        Assertions.assertTrue(spec.test(JsObj.of("a1",JsInt.of(1))).isEmpty());
        Assertions.assertTrue(spec.test(JsObj.of("a1",JsNull.NULL)).isEmpty());
        Assertions.assertFalse(spec.test(JsObj.of("a1",JsBool.FALSE)).isEmpty());
        Assertions.assertFalse(spec.lenient().test(JsObj.of("a1",JsBool.FALSE)).isEmpty());

        Assertions.assertTrue(spec.test(JsObj.of("a2",JsInt.of(1))).isEmpty());
        Assertions.assertTrue(spec.test(JsObj.of("a2",JsNull.NULL)).isEmpty());
        Assertions.assertFalse(spec.test(JsObj.of("a2",JsBool.FALSE)).isEmpty());
        Assertions.assertFalse(spec.lenient().test(JsObj.of("a2",JsBool.FALSE)).isEmpty());

        Assertions.assertFalse(spec.test(JsObj.of("a3",JsInt.of(1))).isEmpty());
        Assertions.assertTrue(spec.withAllOptKeys().lenient().test(JsObj.of("a3",JsInt.of(1))).isEmpty());

        JsObj x = JsObj.of("a1", JsInt.of(1));
        Assertions.assertEquals(JsObj.of("a", JsInt.of(1)),
                                parser.parse(x.toString()));
    }
}
