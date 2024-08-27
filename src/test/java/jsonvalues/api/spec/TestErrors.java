package jsonvalues.api.spec;

import java.math.BigDecimal;
import java.math.BigInteger;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsParserException;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestErrors {

  @Test
  public void test_parsing_obj() {

    JsObjSpec spec = JsObjSpec.of("a",
                                  JsSpecs.integer());

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    Assertions.assertThrows(JsParserException.class,
                            () -> parser.parse("[]"));
  }

  @Test
  public void test_parsing_numbers() {

    //language=JSON
    String json1 = """
        {
          "a": 111111111111111111111111111111111111111111111111111
        }""";

    String json2 = """
        {
          "a": -
        }""";

    JsObjSpecParser parserAsLong = JsObjSpecParser.of(JsObjSpec.of("a",
                                                                   JsSpecs.longInteger()));
    JsObjSpecParser parserAsInt = JsObjSpecParser.of(JsObjSpec.of("a",
                                                                  JsSpecs.integer()));

    JsObjSpecParser parserAsDec = JsObjSpecParser.of(JsObjSpec.of("a",
                                                                  JsSpecs.decimal()));
    JsObjSpecParser parserAsBigInt = JsObjSpecParser.of(JsObjSpec.of("a",
                                                                     JsSpecs.bigInteger()));

    Assertions.assertThrows(JsParserException.class,
                            () -> parserAsLong.parse(json1));
    Assertions.assertThrows(JsParserException.class,
                            () -> parserAsInt.parse(json1));

    Assertions.assertEquals(new BigInteger("111111111111111111111111111111111111111111111111111"),
                            parserAsBigInt.parse(json1)
                                          .getBigInt("a"));

    Assertions.assertThrows(JsParserException.class,
                            () -> parserAsInt.parse(json2));

    String json3 = """
        {
          "a": -1E-2a
        }""";

    Assertions.assertThrows(JsParserException.class,
                            () -> parserAsDec.parse(json3));

    String json4 = """
        {
          "a": -1E-2
        }""";

    Assertions.assertEquals(new BigDecimal("-1E-2"),
                            parserAsDec.parse(json4)
                                       .getBigDec("a"));

    Assertions.assertThrows(JsParserException.class,
                            () -> parserAsInt.parse(json4));

    Assertions.assertThrows(JsParserException.class,
                            () -> parserAsLong.parse(json4));

    String json5 = """
        {
          "a": 1000E3
        }""";

    Assertions.assertEquals(1000000,
                            parserAsInt.parse(json5)
                                       .getInt("a"));
    Assertions.assertEquals(1000000,
                            parserAsLong.parse(json5)
                                        .getInt("a"));

    String json6 = """
        {
          "a": 1000000000000000000E3
        }""";

    Assertions.assertEquals(new BigInteger("1000000000000000000000"),
                            parserAsBigInt.parse(json6)
                                          .getBigInt("a"));
    Assertions.assertEquals(1000000,
                            parserAsLong.parse(json5)
                                        .getInt("a"));


  }


}
