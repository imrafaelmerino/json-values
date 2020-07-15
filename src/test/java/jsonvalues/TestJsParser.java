package jsonvalues;

import jsonvalues.spec.JsObjParser;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

import static jsonvalues.spec.JsSpecs.*;

public class TestJsParser {


    @Test
    public void test_parse_string_into_obj() throws IOException {

        final JsObjParser parser = new JsObjParser(JsObjSpec.strict("a",
                                                                    str,
                                                                    "b",
                                                                    integer,
                                                                    "c",
                                                                    bool,
                                                                    "d",
                                                                    longInteger,
                                                                    "e",
                                                                    decimal,
                                                                    "f",
                                                                    JsSpecs.tuple(decimal,
                                                                                  decimal
                                                                                 ),
                                                                    "g",
                                                                    integral,
                                                                    "h",
                                                                    obj,
                                                                    "i",
                                                                    array,
                                                                    "j",
                                                                    arrayOf(JsObjSpec.lenient("a",
                                                                                              str
                                                                                             ))
                                                                   ));


        final JsObj example = JsObj.of("a",
                                       JsStr.of("a"),
                                       "b",
                                       JsInt.of(10),
                                       "c",
                                       JsBool.TRUE,
                                       "d",
                                       JsLong.of(10),
                                       "e",
                                       JsBigDec.of(BigDecimal.TEN),
                                       "f",
                                       JsArray.of(1.5,
                                                  1.5
                                                 ),
                                       "g",
                                       JsBigInt.of(BigInteger.TEN),
                                       "h",
                                       JsObj.empty(),
                                       "i",
                                       JsArray.empty(),
                                       "j",
                                       JsArray.of(JsObj.of("a",
                                                           JsStr.of("hi")
                                                          ))
                                      );

        final JsObj parsed = parser.parse(example
                                                  .toString());

        Assertions.assertEquals(parsed,
                                example
                               );
    }


    @Test
    public void testParseInstant() {

        JsObjParser parser =
                new JsObjParser(JsObjSpec.strict("a",
                                                 instant,
                                                 "b",
                                                 instant(i -> i.isAfter(Instant.now()
                                                                                .minus(Duration.ofDays(1))
                                                                        )
                                                        )
                                                )
                );

        JsObj obj = JsObj.of("a",
                            JsInstant.of(Instant.now()),
                            "b",
                            JsInstant.of(Instant.now())
                           );
        JsObj parse = parser.parse(obj.toString());

        Assertions.assertEquals(JsInstant.class,
                                parse.get("a")
                                     .getClass()
                               );
        Assertions.assertEquals(JsInstant.class,
                                parse.get("b")
                                     .getClass()
                               );

        Assertions.assertEquals(obj,parse);
    }


    @Test
    public void testParseBinary() {

        JsObjParser parser =
                new JsObjParser(JsObjSpec.strict("a",
                                                 binary,
                                                 "b",
                                                 binary(i -> i.length <= 1024)
                                                )
                );

        JsObj obj = JsObj.of("a",
                             JsStr.of("hola"),
                             "b",
                             JsBinary.of("foo".getBytes())
                            );
        JsObj parse = parser.parse(obj.toString());

        Assertions.assertEquals(JsBinary.class,
                                parse.get("a")
                                     .getClass()
                               );
        Assertions.assertEquals(JsBinary.class,
                                parse.get("b")
                                     .getClass()
                               );

        Assertions.assertEquals(obj,parse);
    }

}
