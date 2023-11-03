package jsonvalues;

import fun.gen.StrGen;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecBuilder;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TestAliasSpec {

    Supplier<String> nameGen = StrGen.alphabetic(10, 10).sample();

    JsObjSpec spec =
            JsObjSpecBuilder.name(nameGen.get())
                            .fieldsAliases(Map.of("a", List.of("a1", "a2")))
                            .spec(JsObjSpec.of("a", JsSpecs.integer().nullable()));

    @Test
    public void testNotErrorBecauseAliasIsFound() {
        Assertions.assertTrue(spec.test(JsObj.of("a",JsInt.of(1))).isEmpty());
        Assertions.assertTrue(spec.test(JsObj.of("a",JsNull.NULL)).isEmpty());
        Assertions.assertFalse(spec.test(JsObj.of("a",JsBool.FALSE)).isEmpty());

        Assertions.assertTrue(spec.test(JsObj.of("a1",JsInt.of(1))).isEmpty());
        Assertions.assertTrue(spec.test(JsObj.of("a1",JsNull.NULL)).isEmpty());
        Assertions.assertFalse(spec.test(JsObj.of("1",JsBool.FALSE)).isEmpty());

        Assertions.assertTrue(spec.test(JsObj.of("a2",JsInt.of(1))).isEmpty());
        Assertions.assertTrue(spec.test(JsObj.of("a2",JsNull.NULL)).isEmpty());
        Assertions.assertFalse(spec.test(JsObj.of("a2",JsBool.FALSE)).isEmpty());

        Assertions.assertFalse(spec.test(JsObj.of("a3",JsInt.of(1))).isEmpty());
    }
}
