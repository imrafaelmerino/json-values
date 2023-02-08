package jsonvalues.spec;

import jsonvalues.JsParserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TestErrors {

    @Test
    public void test_parsing_obj() {

        JsObjSpec spec = JsObjSpec.of("a",
                                          JsSpecs.integer());

        JsObjSpecParser parser = new JsObjSpecParser(spec);


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse("[]"));
    }

    @Test
    public void test_parsing_numbers() {


        //language=JSON
        String json1 = "{\n" +
                "  \"a\": 111111111111111111111111111111111111111111111111111\n" +
                "}";

        String json2 = "{\n" +
                "  \"a\": -\n" +
                "}";

        JsObjSpecParser parserAsLong = new JsObjSpecParser(JsObjSpec.of("a", JsSpecs.longInteger()));
        JsObjSpecParser parserAsInt = new JsObjSpecParser(JsObjSpec.of("a", JsSpecs.integer()));

        JsObjSpecParser parserAsDec = new JsObjSpecParser(JsObjSpec.of("a", JsSpecs.decimal()));
        JsObjSpecParser parserAsBigInt = new JsObjSpecParser(JsObjSpec.of("a", JsSpecs.bigInteger()));

        Assertions.assertThrows(JsParserException.class,()->parserAsLong.parse(json1));
        Assertions.assertThrows(JsParserException.class,()->parserAsInt.parse(json1));


        Assertions.assertEquals(new BigInteger("111111111111111111111111111111111111111111111111111"),
                                parserAsBigInt.parse(json1).getBigInt("a"));

       Assertions.assertThrows(JsParserException.class,()->parserAsInt.parse(json2));

        String json3 = "{\n" +
                "  \"a\": -1E-2a\n" +
                "}";

        Assertions.assertThrows(JsParserException.class,()->parserAsDec.parse(json3));


        String json4 = "{\n" +
                "  \"a\": -1E-2\n" +
                "}";

        Assertions.assertEquals(new BigDecimal("-1E-2"),parserAsDec.parse(json4).getBigDec("a"));


        Assertions.assertThrows(JsParserException.class,()->parserAsInt.parse(json4));

        Assertions.assertThrows(JsParserException.class,()->parserAsLong.parse(json4));

        String json5 = "{\n" +
                "  \"a\": 1000E3\n" +
                "}";

        Assertions.assertEquals(1000000,parserAsInt.parse(json5).getInt("a"));
        Assertions.assertEquals(1000000,parserAsLong.parse(json5).getInt("a"));


        String json6 = "{\n" +
                "  \"a\": 1000000000000000000E3\n" +
                "}";

        Assertions.assertEquals(new BigInteger("1000000000000000000000"),parserAsBigInt.parse(json6).getBigInt("a"));
        Assertions.assertEquals(1000000,parserAsLong.parse(json5).getInt("a"));



    }


}
