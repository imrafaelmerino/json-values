package jsonvalues;

import com.dslplatform.json.MyDslJson;
import fun.gen.BigIntGen;
import fun.gen.Gen;
import fun.gen.StrGen;
import jsonvalues.gen.JsBinaryGen;
import jsonvalues.gen.JsObjGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestToString {

    @Test
    public void testBigIntToStr() {

        Assertions.assertTrue(BigIntGen.arbitrary(10000)
                                       .sample(1000)
                                       .allMatch(it -> it.equals(new BigInteger(it.toString()))));
    }

    @Test
    public void testBinaryToStr() {


        Assertions.assertTrue(JsBinaryGen.arbitrary(0,
                                                    100)
                                         .sample(1000).allMatch(it -> it.equals(JsBinary.of(it.toString()))));


    }
}
