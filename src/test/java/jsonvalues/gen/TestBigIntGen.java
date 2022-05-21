package jsonvalues.gen;

import jsonvalues.JsBigInt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestBigIntGen {

    @Test
    public void testBigInt() {
        int bits = 5;

        Map<JsBigInt, Long> counts = TestFun.generate(100000,
                                                      JsBigIntGen.arbitrary(bits,
                                                                            bits + 1));

        List<JsBigInt> values = IntStream.range(0,
                                                1 << bits)
                                         .mapToObj(BigInteger::valueOf)
                                         .map(JsBigInt::of)
                                         .collect(Collectors.toList());


        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         values,
                                                         0.1);


    }

    @Test
    public void arbitraryBigInt() {
        Map<JsBigInt, Long> counts = TestFun.generate(100000,
                                                      JsBigIntGen.arbitrary(1,
                                                                            3));

        Assertions.assertTrue(IntStream.range(0,
                                              7)
                                       .allMatch(it -> counts.containsKey(JsBigInt.of(BigInteger.valueOf(it)))));
    }


}
