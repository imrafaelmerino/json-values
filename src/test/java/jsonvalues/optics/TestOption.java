package jsonvalues.optics;

import fun.optic.Option;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class TestOption {


    @Test
    public void testCompose() {

        Option<JsObj, JsObj> address = JsObj.optional.obj("address");

        Option<JsObj, String> street = JsObj.optional.str("street");

        Option<JsObj, String> compose = address.compose(street);

        JsObj obj = JsObj.of("address",
                             JsObj.of("street",
                                      JsStr.of("a")));

        Assertions.assertEquals(Optional.of("a"),
                                compose.get.apply(obj));

        Assertions.assertEquals(Optional.of("b"),
                                compose.get.apply(compose.set.apply("b").apply(obj)));
    }

    @Test
    public void testComposeEmpty() {

        Option<JsObj, JsObj> address = JsObj.optional.obj("address");

        Option<JsObj, String> street = JsObj.optional.str("street");

        Option<JsObj, String> compose = address.compose(street);

        JsObj obj = JsObj.empty();

        Assertions.assertEquals(Optional.empty(),
                                compose.get.apply(obj));

        Assertions.assertEquals(obj,
                                compose.set.apply("b").apply(obj));

    }


}
