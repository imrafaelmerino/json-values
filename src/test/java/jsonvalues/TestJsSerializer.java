package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class TestJsSerializer {

    @Test
    public void test() throws UnsupportedEncodingException {

        JsObj obj = JsObj.of("a",
                             JsInt.of(1),
                             "b",
                             JsStr.of("hi"),
                             "c",
                             JsBool.TRUE,
                             "d",
                             JsObj.empty(),
                             "e",
                             JsArray.empty(),
                             "f",
                             JsNull.NULL);

        Assertions.assertTrue(obj.isNotEmpty());

        final byte[] bytes = obj.serialize();

        Assertions.assertEquals(obj,
                                JsObj.parse(new String(bytes,StandardCharsets.UTF_8)));

        final ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        obj.serialize(outputstream);
        Assertions.assertEquals(obj,
                                JsObj.parse(outputstream.toString("UTF-8")));

    }
}
