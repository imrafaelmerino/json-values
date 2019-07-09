import jsonvalues.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static jsonvalues.JsNull.NULL;

public class TestsTry
{


    @Test
    void test_try_obj() throws MalformedJson
    {

        TryObj tryFailure = JsObj.parse("{");
        TryObj trySuccess = JsObj.parse("{\"a\": null}");
        Assertions.assertTrue(tryFailure.isFailure());
        Assertions.assertFalse(tryFailure.isSuccess());
        Assertions.assertThrows(MalformedJson.class,
                                tryFailure::orElseThrow
                               );
        Assertions.assertEquals(JsObj.empty(),
                                tryFailure.orElse(JsObj::empty));
        Assertions.assertEquals(JsObj.of("a",
                                         NULL),
                                trySuccess.orElseThrow());
        Assertions.assertEquals(Optional.empty(),
                                tryFailure.toOptional());
        Assertions.assertEquals(Optional.of(JsObj.of("a",
                                                     NULL)),
                                trySuccess.toOptional());
    }

    @Test
    void test_try_arr() throws MalformedJson
    {
        TryArr tryObjFailure = JsArray.parse("[");
        TryArr tryArrSuccess = JsArray.parse("[null]");
        Assertions.assertTrue(tryObjFailure.isFailure());
        Assertions.assertFalse(tryObjFailure.isSuccess());
        Assertions.assertThrows(MalformedJson.class,
                                tryObjFailure::orElseThrow
                               );
        Assertions.assertEquals(JsArray.empty(),
                                tryObjFailure.orElse(JsArray::empty));
        Assertions.assertEquals(JsArray.of(NULL),
                                tryArrSuccess.orElseThrow());
        Assertions.assertEquals(Optional.empty(),
                                tryObjFailure.toOptional());
        Assertions.assertEquals(Optional.of(JsArray.of(NULL)),
                                tryArrSuccess.toOptional());
    }

    @Test
    void test_try() throws MalformedJson
    {
        Try tryFailure = Json.parse("{");
        Try trySuccess = Json.parse("{\"a\": null}");
        Assertions.assertTrue(tryFailure.isFailure());
        Assertions.assertFalse(tryFailure.isSuccess());
        Assertions.assertThrows(MalformedJson.class,
                                tryFailure::orElseThrow
                               );
        Assertions.assertThrows(MalformedJson.class,
                                tryFailure::objOrElseThrow
                               );
        Assertions.assertEquals(JsObj.empty(),
                                tryFailure.objOrElse(JsObj::empty));
        Assertions.assertEquals(JsObj.of("a",
                                         NULL),
                                trySuccess.objOrElseThrow());
        Assertions.assertEquals(JsObj.of("a",
                                         NULL),
                                trySuccess.orElseThrow());
//        Assertions.assertEquals(Optional.empty(),tryFailure.toOptional());
//        Assertions.assertEquals(Optional.of(JsObj.of("a",NULL)),trySuccess.toOp
//
        Try tryArrFailure = Json.parse("[");
        Try tryArrSuccess = Json.parse("[null]");
        Assertions.assertTrue(tryArrFailure.isFailure());
        Assertions.assertFalse(tryArrFailure.isSuccess());
        Assertions.assertThrows(MalformedJson.class,
                                tryArrFailure::orElseThrow
                               );
        Assertions.assertThrows(MalformedJson.class,
                                tryArrFailure::arrOrElseThrow
                               );
        Assertions.assertEquals(JsArray.empty(),
                                tryArrFailure.arrOrElse(JsArray::empty));
        Assertions.assertEquals(JsArray.of(NULL),
                                tryArrSuccess.arrOrElseThrow());
        Assertions.assertEquals(JsArray.of(NULL),
                                tryArrSuccess.orElseThrow());
//        Assertions.assertEquals(Optional.empty(),tryArrFailure.toOptional());
//        Assertions.assertEquals(Optional.of(JsArray.of(NULL)),tryArrSuccess.toOptional());
    }

    @Test
    void test_parsing_errors() throws MalformedJson
    {

        Assertions.assertThrows(MalformedJson.class,
                                () -> Json.parse("{}")
                                          .arrOrElseThrow());

        Assertions.assertThrows(MalformedJson.class,
                                () -> Json.parse("[]")
                                          .objOrElseThrow());
    }
}
