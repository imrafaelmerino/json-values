package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsPairs {

    @Test
    public void test_creation_of_pair() {

        Assertions.assertEquals(JsPair.of(JsPath.path("/a/b"),
                                          1
                                         ),
                                JsPair.of(JsPath.path("/a/b"),
                                          1
                                         )
                               );

        Assertions.assertEquals(JsPair.of(JsPath.path("/a/b"),
                                          1L
                                         ),
                                JsPair.of(JsPath.path("/a/b"),
                                          1L
                                         )
                               );

        Assertions.assertEquals(JsPair.of(JsPath.path("/a/b"),
                                          "hi"
                                         ),
                                JsPair.of(JsPath.path("/a/b"),
                                          "hi"
                                         )
                               );

        Assertions.assertEquals(JsPair.of(JsPath.path("/a/b"),
                                          true
                                         ),
                                JsPair.of(JsPath.path("/a/b"),
                                          true
                                         )
                               );

    }
}
