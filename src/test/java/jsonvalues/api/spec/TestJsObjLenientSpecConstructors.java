package jsonvalues.api.spec;

import jsonvalues.JsInt;
import jsonvalues.JsObj;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpec;
import jsonvalues.spec.JsSpecs;
import jsonvalues.spec.SpecError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class TestJsObjLenientSpecConstructors {
    @Test
    public void test_20_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                      number,
                                      "b",
                                      number,
                                      "c",
                                      number,
                                      "d",
                                      number,
                                      "e",
                                      number,
                                      "f",
                                      number,
                                      "g",
                                      number,
                                      "h",
                                      number,
                                      "i",
                                      number,
                                      "j",
                                      number,
                                      "k",
                                      number,
                                      "l",
                                      number,
                                      "m",
                                      number,
                                      "n",
                                      number,
                                      "o",
                                      number,
                                      "p",
                                      number,
                                      "q",
                                      number,
                                      "r",
                                      number,
                                      "s",
                                      number,
                                      "t",
                                      number
                                     ).lenient();

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

        List<SpecError> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());

        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());


    }
    @Test
    public void test_19_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number,
                                          "o",
                                          number,
                                          "p",
                                          number,
                                          "q",
                                          number,
                                          "r",
                                          number,
                                          "s",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());

        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());


    }
    @Test
    public void test_18_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number,
                                          "o",
                                          number,
                                          "p",
                                          number,
                                          "q",
                                          number,
                                          "r",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());

        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());



    }
    @Test
    public void test_17_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number,
                                          "o",
                                          number,
                                          "p",
                                          number,
                                          "q",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());

        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());



    }
    @Test
    public void test_16_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number,
                                          "o",
                                          number,
                                          "p",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());
        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());


    }

    @Test
    public void test_15_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number,
                                          "o",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);

        Assertions.assertTrue(test.isEmpty());

        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());



    }

    @Test
    public void test_14_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number,
                                          "n",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());



    }

    @Test
    public void test_13_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number,
                                          "m",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());



    }


    @Test
    public void test_12_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number,
                                          "l",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());




    }

    @Test
    public void test_11_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number,
                                          "k",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());


    }

    @Test
    public void test_10_args() {
        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number,
                                          "i",
                                          number,
                                          "j",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());


    }

    @Test
    public void test_9_args() {
        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number,
                                          "h",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());

        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());

    }

    @Test
    public void test_8_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number,
                                          "g",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());



    }

    @Test
    public void test_6_args() {
        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number,
                                          "f",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());

        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());


    }

    @Test
    public void test_5_args() {
        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number,
                                          "e",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());


    }

    @Test
    public void test_4_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number,
                                          "d",
                                          number
        ).lenient();

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

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());

        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());


    }

    @Test
    public void test_3_args() {


        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number,
                                          "c",
                                          number
        ).lenient();

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE,
                             "c",
                             ONE
        );

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());


        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());

    }

    @Test
    public void test_2_args() {
        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number,
                                          "b",
                                          number
        ).lenient();

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE,
                             "b",
                             ONE
        );

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());

        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());

    }

    @Test
    public void test_1_args() {

        JsSpec number = JsSpecs.integer();
        JsObjSpec spec = JsObjSpec.of("a",
                                          number
        ).lenient();

        JsInt ONE = JsInt.of(1);
        JsObj obj = JsObj.of("a",
                             ONE
        );

        List<SpecError> test = spec.test(obj);
        Assertions.assertTrue(test.isEmpty());
        Assertions.assertTrue(spec.test(obj.set("z",ONE)).isEmpty());


    }
}
