package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class TestPrims {
    @Test
    public void test_string_prism(){


        Assertions.assertEquals(Optional.empty(), JsStr.prism.getOptional.apply(JsInt.of(1)));
        Assertions.assertTrue(JsStr.prism.isEmpty.test(JsInt.of(1)));
        Assertions.assertTrue(JsStr.prism.nonEmpty.test(JsStr.of("a")));

        Assertions.assertEquals(JsStr.of("a"), JsStr.prism.reverseGet.apply("a"));

        Assertions.assertEquals(Optional.of("a"),
                                JsStr.prism.find.apply(it -> it.startsWith("a"))
                                           .apply(JsStr.of("a")));
        Assertions.assertEquals(true,
                                JsStr.prism.exists.apply(it -> it.startsWith("a"))
                                           .test(JsStr.of("a"))
                               );

        Assertions.assertEquals(JsStr.of("A"),
                                JsStr.prism.modify.apply(String::toUpperCase)
                                           .apply(JsStr.of("a")));

        Assertions.assertEquals(Optional.of(JsStr.of("A")),
                                JsStr.prism.modifyOpt.apply(String::toUpperCase)
                                           .apply(JsStr.of("a")));

        Assertions.assertEquals(JsInt.of(1),
                                JsStr.prism.modify.apply(String::toUpperCase)
                                           .apply(JsInt.of(1)));

        Assertions.assertEquals(Optional.empty(),
                                JsStr.prism.modifyOpt.apply(String::toUpperCase)
                                           .apply(JsInt.of(1)));
    }

    @Test
    public void test_integer_prism(){


        Assertions.assertEquals(Optional.empty(), JsInt.prism.getOptional.apply(JsStr.of("a")));
        Assertions.assertTrue(JsInt.prism.isEmpty.test(JsStr.of("a")));
        Assertions.assertTrue(JsInt.prism.nonEmpty.test(JsInt.of(1)));

        Assertions.assertEquals(JsInt.of(1), JsInt.prism.reverseGet.apply(1));

        Assertions.assertEquals(Optional.of(1),
                                JsInt.prism.find.apply(it -> it < 2)
                                           .apply(JsInt.of(1)));
        Assertions.assertEquals(true,
                                JsInt.prism.exists.apply(it ->it < 2)
                                           .test(JsInt.of(1)));

        Assertions.assertEquals(JsStr.of("a"),
                                JsInt.prism.modify.apply(it->it+1)
                                           .apply(JsStr.of("a")));

        Assertions.assertEquals(JsInt.of(2),
                                JsInt.prism.modify.apply(it->it+1)
                                           .apply(JsInt.of(1)));
    }


    @Test
    public void test_long_prism(){


        Assertions.assertEquals(Optional.empty(), JsLong.prism.getOptional.apply(JsStr.of("a")));
        Assertions.assertTrue(JsLong.prism.isEmpty.test(JsStr.of("a")));
        Assertions.assertTrue(JsLong.prism.nonEmpty.test(JsLong.of(1)));

        Assertions.assertEquals(JsLong.of(1), JsInt.prism.reverseGet.apply(1));

        Assertions.assertEquals(Optional.of(1L),
                                JsLong.prism.find.apply(it -> it < 2)
                                           .apply(JsLong.of(1)));
        Assertions.assertEquals(true,
                                JsLong.prism.exists.apply(it ->it < 2)
                                           .test(JsLong.of(1)));

        Assertions.assertEquals(JsStr.of("a"),
                                JsLong.prism.modify.apply(it->it+1)
                                           .apply(JsStr.of("a")));

        Assertions.assertEquals(JsLong.of(2),
                                JsLong.prism.modify.apply(it->it+1)
                                           .apply(JsLong.of(1)));
    }
}
