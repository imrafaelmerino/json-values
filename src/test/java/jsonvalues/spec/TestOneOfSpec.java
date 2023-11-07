package jsonvalues.spec;

import fun.gen.Combinators;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsDoubleGen;
import jsonvalues.gen.JsStrGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestOneOfSpec {


    @Test
    public void testOneOfPrimitive() {

        var spec = JsSpecs.oneSpecOf(List.of(JsObjSpec.of("a", JsSpecs.integer(),
                                                          "b", JsSpecs.str(),
                                                          "c", JsSpecs.oneSpecOf(List.of(JsSpecs.bool(), JsSpecs.str()))
                                                         ),
                                             JsObjSpec.of("a", JsSpecs.integer(),
                                                          "b", JsSpecs.str(),
                                                          "c", JsSpecs.oneSpecOf(List.of(JsSpecs.bool(), JsSpecs.integer()))
                                                         )
                                            )
                                    );


        JsObj obj = JsObj.of("a", JsInt.of(1),
                             "b", JsStr.of("hi"),
                             "c", JsInt.of(3)
                            );
        JsObjSpecParser parser = JsObjSpecParser.of(spec);
        var obj1 = parser.parse(obj.toString());

        Assertions.assertEquals(obj, obj1);


    }

    @Test
    public void testOneOfArray() {

        JsSpec spec = JsSpecs.oneSpecOf(JsSpecs.arrayOfDouble(),
                                        JsSpecs.arrayOfStr()
                                       );

        JsArraySpecParser parser = JsArraySpecParser.of(spec);

        var gen = Combinators.oneOf(JsArrayGen.ofN(JsDoubleGen.arbitrary(), 10),
                                    JsArrayGen.ofN(JsStrGen.alphabetic(), 10)
                                   );

        gen.sample(1000).forEach(obj ->
                                         Assertions.assertEquals(obj,
                                                                 parser.parse(obj.toString()))
                                );


    }
}