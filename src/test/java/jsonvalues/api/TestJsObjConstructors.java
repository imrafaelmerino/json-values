package jsonvalues.api;

import jsonvalues.JsInt;
import jsonvalues.JsObj;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static jsonvalues.JsPath.path;

public class TestJsObjConstructors {


    @Test
    public void test_15_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE
                            );

        Assertions.assertEquals(15,
                                obj.size());


    }

    @Test
    public void test_14_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE
        );

        Assertions.assertEquals(14,
                                obj.size());


    }

    @Test
    public void test_13_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE
        );

        Assertions.assertEquals(13,
                                obj.size());


    }


    @Test
    public void test_12_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE
        );

        Assertions.assertEquals(12,
                                obj.size());


    }

    @Test
    public void test_11_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE
        );

        Assertions.assertEquals(11,
                                obj.size());


    }

    @Test
    public void test_10_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE
        );

        Assertions.assertEquals(10,
                                obj.size());


    }

    @Test
    public void test_9_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE
        );

        Assertions.assertEquals(9,
                                obj.size());


    }

    @Test
    public void test_8_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE
        );

        Assertions.assertEquals(8,
                                obj.size());


    }

    @Test
    public void test_7_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE
        );

        Assertions.assertEquals(7,
                                obj.size());


    }

    @Test
    public void test_6_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE
        );

        Assertions.assertEquals(6,
                                obj.size());


    }

    @Test
    public void test_5_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE
        );

        Assertions.assertEquals(5,
                                obj.size());


    }

    @Test
    public void test_4_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE
        );

        Assertions.assertEquals(4,
                                obj.size());


    }

    @Test
    public void test_3_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE
        );

        Assertions.assertEquals(3,
                                obj.size());


    }

    @Test
    public void test_2_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE
        );

        Assertions.assertEquals(2,
                                obj.size());


    }

    @Test
    public void test_1_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE
        );

        Assertions.assertEquals(1,
                                obj.size());


    }

    @Test
    public void test_path_15_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE,
                             path("/j"),
                             ONE,
                             path("/k"),
                             ONE,
                             path("/l"),
                             ONE,
                             path("/m"),
                             ONE,
                             path("/n"),
                             ONE,
                             path("/o"),
                             ONE);

        Assertions.assertEquals(15,
                                obj.size());


    }

    @Test
    public void test_path_14_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE,
                             path("/j"),
                             ONE,
                             path("/k"),
                             ONE,
                             path("/l"),
                             ONE,
                             path("/m"),
                             ONE,
                             path("/n"),
                             ONE);


        Assertions.assertEquals(14,
                                obj.size());


    }

    @Test
    public void test_path_13_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE,
                             path("/j"),
                             ONE,
                             path("/k"),
                             ONE,
                             path("/l"),
                             ONE,
                             path("/m"),
                             ONE);

        Assertions.assertEquals(13,
                                obj.size());


    }


    @Test
    public void test_path_12_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE,
                             path("/j"),
                             ONE,
                             path("/k"),
                             ONE,
                             path("/l"),
                             ONE);


        Assertions.assertEquals(12,
                                obj.size());


    }

    @Test
    public void test_path_11_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE,
                             path("/j"),
                             ONE,
                             path("/k"),
                             ONE);

        Assertions.assertEquals(11,
                                obj.size());


    }

    @Test
    public void test_path_10_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE,
                             path("/j"),
                             ONE);
        Assertions.assertEquals(10,
                                obj.size());


    }

    @Test
    public void test_path9_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE);

        Assertions.assertEquals(9,
                                obj.size());


    }

    @Test
    public void test_path_8_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE
        );


        Assertions.assertEquals(8,
                                obj.size());


    }

    @Test
    public void test_path_7_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE
        );

        Assertions.assertEquals(7,
                                obj.size());


    }

    @Test
    public void test_path_6_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE
        );

        Assertions.assertEquals(6,
                                obj.size());


    }

    @Test
    public void test_path_5_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE
        );

        Assertions.assertEquals(5,
                                obj.size());


    }

    @Test
    public void test_path_4_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE
        );

        Assertions.assertEquals(4,
                                obj.size());


    }

    @Test
    public void test_path_3_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE
        );

        Assertions.assertEquals(3,
                                obj.size());


    }

    @Test
    public void test_path_2_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE
        );

        Assertions.assertEquals(2,
                                obj.size());


    }

    @Test
    public void test_path_1_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE
        );

        Assertions.assertEquals(1,
                                obj.size());


    }




    @Test
    public void test_16_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE,
                             "p",
                             ONE
        );

        Assertions.assertEquals(16,
                                obj.size());


    }

    @Test
    public void test_17_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE,
                             "p",
                             ONE,
                             "q",
                             ONE
        );

        Assertions.assertEquals(17,
                                obj.size());


    }

    @Test
    public void test_18_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE,
                             "p",
                             ONE,
                             "q",
                             ONE,
                             "r",
                             ONE
        );

        Assertions.assertEquals(18,
                                obj.size());


    }

    @Test
    public void test_19_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE,
                             "p",
                             ONE,
                             "q",
                             ONE,
                             "r",
                             ONE,
                             "s",
                             ONE
        );

        Assertions.assertEquals(19,
                                obj.size());


    }

    @Test
    public void test_20_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE,
                             "d",
                             ONE,
                             "e",
                             ONE,
                             "f",
                             ONE,
                             "g",
                             ONE,
                             "h",
                             ONE,
                             "i",
                             ONE,
                             "j",
                             ONE,
                             "k",
                             ONE,
                             "l",
                             ONE,
                             "m",
                             ONE,
                             "n",
                             ONE,
                             "o",
                             ONE,
                             "p",
                             ONE,
                             "q",
                             ONE,
                             "r",
                             ONE,
                             "s",
                             ONE,
                             "t",
                             ONE
        );

        Assertions.assertEquals(20,
                                obj.size());


    }


    @Test
    public void test_path_16_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE,
                             path("/j"),
                             ONE,
                             path("/k"),
                             ONE,
                             path("/l"),
                             ONE,
                             path("/m"),
                             ONE,
                             path("/n"),
                             ONE,
                             path("/o"),
                             ONE,
                             path("/p"),
                             ONE);

        Assertions.assertEquals(16,
                                obj.size());


    }

    @Test
    public void test_path_17_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE,
                             path("/j"),
                             ONE,
                             path("/k"),
                             ONE,
                             path("/l"),
                             ONE,
                             path("/m"),
                             ONE,
                             path("/n"),
                             ONE,
                             path("/o"),
                             ONE,
                             path("/p"),
                             ONE,
                             path("/q"),
                             ONE);

        Assertions.assertEquals(17,
                                obj.size());


    }

    @Test
    public void test_path_18_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE,
                             path("/j"),
                             ONE,
                             path("/k"),
                             ONE,
                             path("/l"),
                             ONE,
                             path("/m"),
                             ONE,
                             path("/n"),
                             ONE,
                             path("/o"),
                             ONE,
                             path("/p"),
                             ONE,
                             path("/q"),
                             ONE,
                             path("/r"),
                             ONE);

        Assertions.assertEquals(18,
                                obj.size());


    }

    @Test
    public void test_path_19_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE,
                             path("/j"),
                             ONE,
                             path("/k"),
                             ONE,
                             path("/l"),
                             ONE,
                             path("/m"),
                             ONE,
                             path("/n"),
                             ONE,
                             path("/o"),
                             ONE,
                             path("/p"),
                             ONE,
                             path("/q"),
                             ONE,
                             path("/r"),
                             ONE,
                             path("/s"),
                             ONE);

        Assertions.assertEquals(19,
                                obj.size());


    }

    @Test
    public void test_path_20_args() {


        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of(path("/a"),
                             ONE,
                             path("/b"),
                             ONE,
                             path("/c"),
                             ONE,
                             path("/d"),
                             ONE,
                             path("/e"),
                             ONE,
                             path("/f"),
                             ONE,
                             path("/g"),
                             ONE,
                             path("/h"),
                             ONE,
                             path("/i"),
                             ONE,
                             path("/j"),
                             ONE,
                             path("/k"),
                             ONE,
                             path("/l"),
                             ONE,
                             path("/m"),
                             ONE,
                             path("/n"),
                             ONE,
                             path("/o"),
                             ONE,
                             path("/p"),
                             ONE,
                             path("/q"),
                             ONE,
                             path("/r"),
                             ONE,
                             path("/s"),
                             ONE,
                             path("/t"),
                             ONE);

        Assertions.assertEquals(20,
                                obj.size());


    }


}

