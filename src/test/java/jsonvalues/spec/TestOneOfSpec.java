package jsonvalues.spec;

import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
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
        System.out.println(spec.test(obj));
        JsObjSpecParser parser =  JsObjSpecParser.of(spec);
        var obj1 = parser.parse(obj.toString());

        Assertions.assertEquals(obj, obj1);


    }
}