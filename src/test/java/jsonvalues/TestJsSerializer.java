package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

public class TestJsSerializer {

    @Test
    public void test(){

        JsObj obj = JsObj.of("a",JsInt.of(1),
                             "b",JsStr.of("hi"),
                             "c",JsBool.TRUE,
                             "d",JsObj.empty(),
                             "e",JsArray.empty(),
                             "f",JsNull.NULL);

        Assertions.assertTrue(obj.isNotEmpty());

        final byte[] bytes = obj.serialize();

        Assertions.assertEquals(obj,JsObj.parse(new String(bytes)));

        final ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        obj.serialize(outputstream);
        Assertions.assertEquals(obj,JsObj.parse(outputstream.toString()));

    }
}
