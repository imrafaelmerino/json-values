package jsonvalues.api;

import jsonvalues.JsBinary;
import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecBuilder;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class TestRecursiveType {


    @Test
    public void testWithBuilder() {

        var spec = JsObjSpecBuilder.withName("person")
                                   .build(JsObjSpec.of("name", JsSpecs.str(),
                                                       "image", JsSpecs.fixedBinary(1),
                                                       "age", JsSpecs.integer(),
                                                       "father", JsSpecs.ofNamedSpec("person")
                                                      )
                                                   .withOptKeys("father")
                                         );

        var parser = JsObjSpecParser.of(spec);

        var person = JsObj.of("name", JsStr.of("Rafa"),
                              "image", JsBinary.of("a".getBytes(StandardCharsets.UTF_8)),
                              "age", JsInt.of(40),
                              "father", JsObj.of("name", JsStr.of("Luis"),
                                                 "age", JsInt.of(73),
                                                 "image", JsBinary.of("b".getBytes(StandardCharsets.UTF_8))
                                                )
                             );

        var errors = spec.test(person);
        Assertions.assertTrue(errors.isEmpty());
        var parsed = parser.parse(person.toString());
        Assertions.assertEquals(person, parsed);


    }

    @Test
    public void testWitNamedSpecMethod() {

        var spec = JsSpecs.ofNamedSpec("person_1",
                                       JsObjSpec.of("name", JsSpecs.str(),
                                                    "image", JsSpecs.fixedBinary(1),
                                                    "age", JsSpecs.integer(),
                                                    "father", JsSpecs.ofNamedSpec("person_1")
                                                   )
                                                .withOptKeys("father")
                                      );

        var parser = JsObjSpecParser.of(spec);

        var person = JsObj.of("name", JsStr.of("Rafa"),
                              "image", JsBinary.of("a".getBytes(StandardCharsets.UTF_8)),
                              "age", JsInt.of(40),
                              "father", JsObj.of("name", JsStr.of("Luis"),
                                                 "age", JsInt.of(73),
                                                 "image", JsBinary.of("b".getBytes(StandardCharsets.UTF_8))
                                                )
                             );

        var errors = spec.test(person);
        Assertions.assertTrue(errors.isEmpty());
        var parsed = parser.parse(person.toString());
        Assertions.assertEquals(person, parsed);


    }
}
