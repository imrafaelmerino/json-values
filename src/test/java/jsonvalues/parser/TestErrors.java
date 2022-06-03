package jsonvalues.parser;

import com.dslplatform.json.JsParserException;
import jsonvalues.spec.JsObjParser;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TestErrors {

    @Test
    public void test_parsing_obj() {

        JsObjSpec spec = JsObjSpec.strict("a",
                                          JsSpecs.integer());

        JsObjParser parser = new JsObjParser(spec);


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

        JsObjParser parserAsLong = new JsObjParser(JsObjSpec.strict("a",JsSpecs.longInteger()));
        JsObjParser parserAsInt = new JsObjParser(JsObjSpec.strict("a",JsSpecs.integer()));

        JsObjParser parserAsDec = new JsObjParser(JsObjSpec.strict("a",JsSpecs.decimal()));
        JsObjParser parserAsBigInt = new JsObjParser(JsObjSpec.strict("a",JsSpecs.bigInteger()));

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
