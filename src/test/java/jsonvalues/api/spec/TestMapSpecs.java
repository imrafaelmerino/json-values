package jsonvalues.api.spec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import jsonvalues.JsBinary;
import jsonvalues.JsDouble;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.spec.ERROR_CODE;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsSpecs;
import jsonvalues.spec.SpecError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMapSpecs {

  @Test
  public void test() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.mapOfBool()
                                         .nullable(),
                                  "b",
                                  JsSpecs.mapOfStr()
                                         .nullable()
                                 );
    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    JsObj obj = JsObj.of("a",
                         JsNull.NULL,
                         "b",
                         JsNull.NULL);
    List<SpecError> errors = spec.test(obj);

    Assertions.assertTrue(errors.isEmpty());
    Assertions.assertEquals(obj,
                            parser.parse(obj.toString()));


  }

  @Test
  public void tesMapOfDouble() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.mapOfDouble());

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    JsObj obj = JsObj.of("a",
                         JsObj.of("b",
                                  JsDouble.of(1 / 2d),
                                  "c",
                                  JsDouble.of(1 / 4d)));
    List<SpecError> errors = spec.test(obj);

    Assertions.assertTrue(errors.isEmpty());
    Assertions.assertEquals(obj,
                            parser.parse(obj.toString()));

    JsObj obj1 = JsObj.of("a",
                          JsNull.NULL);

    Assertions.assertFalse(spec.test(obj1)
                               .isEmpty());
    Assertions.assertEquals(ERROR_CODE.OBJ_EXPECTED,
                            spec.test(obj1)
                                .get(0).error.code());

  }

  @Test
  public void tesMapOfBinary() {
    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.mapOfBinary());

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    JsObj obj = JsObj.of("a",
                         JsObj.of("b",
                                  JsBinary.of("hi".getBytes(StandardCharsets.UTF_8)),
                                  "c",
                                  JsStr.of(Base64.getEncoder()
                                                 .encodeToString("hola".getBytes(StandardCharsets.UTF_8)))));
    List<SpecError> errors = spec.test(obj);

    Assertions.assertTrue(errors.isEmpty());
    Assertions.assertEquals(obj,
                            parser.parse(obj.toString()));

    JsObj obj1 = JsObj.of("a",
                          JsNull.NULL);

    Assertions.assertFalse(spec.test(obj1)
                               .isEmpty());
    Assertions.assertEquals(ERROR_CODE.OBJ_EXPECTED,
                            spec.test(obj1)
                                .get(0).error.code());


  }

  @Test
  public void testNullableMaps() {

    var spec = JsObjSpec.of("a",
                            JsSpecs.mapOfDecimal()
                                   .nullable(),
                            "b",
                            JsSpecs.mapOfInstant()
                                   .nullable(),
                            "c",
                            JsSpecs.mapOfLong()
                                   .nullable(),
                            "d",
                            JsSpecs.mapOfInteger()
                                   .nullable(),
                            "e",
                            JsSpecs.mapOfStr()
                                   .nullable(),
                            "f",
                            JsSpecs.mapOfDouble()
                                   .nullable(),
                            "g",
                            JsSpecs.mapOfBinary()
                                   .nullable(),
                            "h",
                            JsSpecs.mapOfBigInteger()
                                   .nullable()
                           );

    var parser = JsObjSpecParser.of(spec);

    JsObj obj = JsObj.of("a",
                         JsNull.NULL,
                         "b",
                         JsNull.NULL,
                         "c",
                         JsNull.NULL,
                         "d",
                         JsNull.NULL,
                         "e",
                         JsNull.NULL,
                         "f",
                         JsNull.NULL,
                         "g",
                         JsNull.NULL,
                         "h",
                         JsNull.NULL
                        );

    Assertions.assertEquals(obj,
                            parser.parse(obj.toString()));

    Assertions.assertTrue(spec.test(obj
                                   )
                              .isEmpty()
                         );
  }
}
