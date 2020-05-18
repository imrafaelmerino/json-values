package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJsPath {


    @Test
    public void test_to_string() {

        Assertions.assertEquals("/a/b/0/1",
                                JsPath.path("/a/b/0/1")
                                      .toString()
                               );

        Assertions.assertEquals("/a/b/0/1",
                                JsPath.path("#/a/b/0/1")
                                      .toString()
                               );
        Assertions.assertEquals("/b/0/1",
                                JsPath.path("/b/0/1")
                                      .toString()
                               );
        Assertions.assertEquals("/a b/'0'/0/1",
                                JsPath.path("#/a+b/'0'/0/1")
                                      .toString()
                               );
        Assertions.assertEquals("/'/'0'/0/1/",
                                JsPath.path("#/%27/'0'/0/1/")
                                      .toString()
                               );
        Assertions.assertEquals("/'/'0'/0/1/",
                                JsPath.path("/'/'0'/0/1/")
                                      .toString()
                               );

    }

    @Test
    public void test_jspath_comparator() {
        Assertions.assertEquals("a".compareTo("b"),
                                JsPath.path("/a/b")
                                      .compareTo(JsPath.path("/b/a"))
                               );

        Assertions.assertEquals(0,
                                JsPath.path("/a/b")
                                      .compareTo(JsPath.path("/a/b"))
                               );

        Assertions.assertEquals("1".compareTo("a"),
                                JsPath.path("/1/b")
                                      .compareTo(JsPath.path("/a/b"))
                               );
    }

    @Test
    public void test_map_keys() {

        Assertions.assertEquals(JsPath.path("/a!/1/b!/1/c!"),
                                JsPath.path("/a/1/b/1/c")
                                      .mapKeys(it -> it.concat("!"))
                               );
    }

    @Test
    public void test_equals() {
        Assertions.assertEquals(JsPath.path("#/a/b/c/d/0//%20"),
                                JsPath.path("#/a/b/c/d/0//+")
                               );
        Assertions.assertEquals(JsPath.path("#/a/b/c/d/0//%20")
                                      .hashCode(),
                                JsPath.path("#/a/b/c/d/0//+")
                                      .hashCode()
                               );

        Assertions.assertEquals(JsPath.path("/1/2/3"),
                                JsPath.path("/1/2/3")
                               );

        Assertions.assertEquals(JsPath.path("/1/2/3")
                                      .hashCode(),
                                JsPath.path("/1/2/3")
                                      .hashCode()
                               );

        Assertions.assertEquals(JsPath.path("#/a/1//+/'"),
                                JsPath.path("#/a/1//+/%27")
                               );

        Assertions.assertEquals(JsPath.path("#/a/1//+/'")
                                      .hashCode(),
                                JsPath.path("#/a/1//+/%27")
                                      .hashCode()
                               );


    }

    @Test
    public void test_path_dec() {
        Assertions.assertEquals(JsPath.path("/a/b/0"),
                                JsPath.path("/a/b/1")
                                      .dec()
                               );

        Assertions.assertThrows(UserError.class,
                                () -> JsPath.path("a")
                                            .dec()
                               );
    }

    @Test
    public void test_position() {
        assertTrue(JsPath.path("/a")
                         .last()
                         .isKey());
        Assertions.assertFalse(JsPath.path("/a")
                                     .last()
                                     .isIndex());
        assertTrue(JsPath.path("/a")
                         .last()
                         .isKey(i -> i.equals("a")));
        Assertions.assertThrows(UserError.class,
                                () -> JsPath.path("/0")
                                            .last()
                                            .asKey()
                               );

        assertTrue(JsPath.path("/0")
                         .last()
                         .isIndex());
        Assertions.assertFalse(JsPath.path("/0")
                                     .last()
                                     .isKey());
        Assertions.assertTrue(JsPath.path("/0")
                                    .last()
                                    .isIndex(i -> i == 0));
        Assertions.assertThrows(UserError.class,
                                () -> JsPath.path("/a")
                                            .last()
                                            .asIndex()
                               );

    }

    @Test
    public void test_empty_key_equals() {
        Assertions.assertEquals(JsPath.empty()
                                      .key(""),
                                JsPath.path("#/")
                               );
        Assertions.assertEquals(JsPath.empty()
                                      .key(""),
                                JsPath.path("/")
                               );
        Assertions.assertEquals(JsPath.empty()
                                      .key("."),
                                JsPath.path("#/%2E")
                               );
        Assertions.assertEquals(JsPath.path("#/a/b/+/1"),
                                JsPath.path("#/a/b/%20/1")
                               );
        Assertions.assertEquals(JsPath.path("#/a/b/+/1"),
                                JsPath.path("/a/b/ /1")
                               );
    }

    @Test
    public void test_starts_with() {

        final JsPath path = JsPath.path("/a/b/c/0/1");
        assertTrue(path.startsWith(JsPath.empty()));
        assertTrue(path.startsWith(JsPath.path("/a")));
        assertTrue(path.startsWith(JsPath.path("/a/b")));
        assertTrue(path.startsWith(JsPath.path("/a/b/c")));
        assertTrue(path.startsWith(JsPath.path("/a/b/c/0")));
        assertTrue(path.startsWith(JsPath.path("#/a/b/c/0/1")));
        Assertions.assertFalse(path.startsWith(JsPath.path("/a/b/c/0/1/a")));
    }

    @Test
    public void test_ends_with() {
        final JsPath path = JsPath.path("/a/b/c/0/1");
        assertTrue(path.endsWith(JsPath.empty()));
        assertTrue(path.endsWith(JsPath.path("/1")));
        assertTrue(path.endsWith(JsPath.path("/0/1")));
        assertTrue(path.endsWith(JsPath.path("/c/0/1")));
        assertTrue(path.endsWith(JsPath.path("/b/c/0/1")));
        assertTrue(path.endsWith(JsPath.path("/a/b/c/0/1")));
        Assertions.assertFalse(path.endsWith(JsPath.path("/a/b/c/0/1/a")));
    }

    @Test
    public void test_same() {
        Assertions.assertEquals(JsPath.path("/a/b/c/d/0"),
                                JsPath.path("#/a/b/c/d/0")
                               );
    }

    @Test
    public void test_error_if_index_has_leading_zeros() {
        final UserError userError = Assertions.assertThrows(UserError.class,
                                                            () -> JsPath.path("/a/01")
                                                           );

        Assertions.assertEquals("index 01 with leading zeros. Suggestion: removes the leading zeros.",
                                userError.getMessage()
                               );
    }

}
