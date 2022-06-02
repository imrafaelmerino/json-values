package jsonvalues.gen;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.tuple.Pair;
import jsonvalues.JsInt;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

public class TestCombinators {


    @Test
    public void freqCombinator() {

        Gen<JsInt> gen = Combinators.freq(Pair.of(1,
                                                     Gen.cons(JsInt.of(1))),
                                          Pair.of(1,
                                                     Gen.cons(JsInt.of(2))),
                                          Pair.of(1,
                                                     Gen.cons(JsInt.of(3))),
                                          Pair.of(1,
                                                     Gen.cons(JsInt.of(4))),
                                          Pair.of(1,
                                                     Gen.cons(JsInt.of(5))));

        Map<JsInt, Long> counts = TestFun.generate(100000,
                                                   gen);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         TestFun.list(1,
                                                                      2,
                                                                      3,
                                                                      4,
                                                                      5).stream()
                                                                .map(JsInt::of)
                                                                .collect(Collectors.toList()),
                                                         0.05);
    }


}
