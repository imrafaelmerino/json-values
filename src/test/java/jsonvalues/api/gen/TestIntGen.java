package jsonvalues.api.gen;

import jsonvalues.JsInt;
import jsonvalues.gen.JsIntGen;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestIntGen {

    @Test
    public void biasedInt() {

        Map<JsInt, Long> counts = TestFun.generate(100000,
                                                   JsIntGen.biased());

        List<JsInt> problematic = TestFun.list(Integer.MAX_VALUE,
                                               Integer.MIN_VALUE,
                                               (int) Short.MAX_VALUE,
                                               (int) Short.MIN_VALUE,
                                               (int) Byte.MAX_VALUE,
                                               (int) Byte.MIN_VALUE,
                                               0)
                                         .stream()
                                         .map(JsInt::of)
                                         .collect(Collectors.toList());

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);


    }

    @Test
    public void arbitraryInt() {

        Map<JsInt, Long> counts = TestFun.generate(10000000,
                                                   JsIntGen.arbitrary(Integer.MAX_VALUE - 10,
                                                                      Integer.MAX_VALUE));

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         TestFun.list(Integer.MAX_VALUE - 10,
                                                                      Integer.MAX_VALUE - 9,
                                                                      Integer.MAX_VALUE - 8,
                                                                      Integer.MAX_VALUE - 7,
                                                                      Integer.MAX_VALUE - 6,
                                                                      Integer.MAX_VALUE - 5,
                                                                      Integer.MAX_VALUE - 4,
                                                                      Integer.MAX_VALUE - 3,
                                                                      Integer.MAX_VALUE - 2,
                                                                      Integer.MAX_VALUE - 1,
                                                                      Integer.MAX_VALUE)
                                                                .stream()
                                                                .map(JsInt::of)
                                                                .collect(Collectors.toList()),
                                                         0.05);


    }


    @Test
    public void biasedIntInterval() {

        Map<JsInt, Long> counts = TestFun.generate(100000,
                                                   JsIntGen.biased(-1000000000,
                                                                   100000000));

        List<JsInt> problematic = TestFun.list(100000000,
                                               -1000000000,
                                               (int) Short.MAX_VALUE,
                                               (int) Byte.MAX_VALUE,
                                               (int) Short.MIN_VALUE,
                                               (int) Byte.MIN_VALUE,
                                               0)
                                         .stream()
                                         .map(JsInt::of)
                                         .collect(Collectors.toList());

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);
    }
}
