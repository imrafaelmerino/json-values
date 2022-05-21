package jsonvalues.gen;

import fun.gen.Gen;
import jsonvalues.JsBinary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestBytesGen {

    @Test
    public void testArbitrary() {

        Assertions.assertTrue(JsBinaryGen.arbitrary(0,
                                                    10).sample(10000)
                                         .allMatch(it -> it.value.length <= 10)
        );


        Map<Integer, Long> count =
                TestFun.generate(1000000,
                                 JsBinaryGen.arbitrary(0,
                                                       10).map(it -> it.value.length));


        TestFun.assertGeneratedValuesHaveSameProbability(count,
                                                         TestFun.list(0,
                                                                      1,
                                                                      2,
                                                                      3,
                                                                      4,
                                                                      5,
                                                                      6,
                                                                      7,
                                                                      8,
                                                                      9,
                                                                      10),
                                                         0.05);

    }

    @Test
    public void testBiased() {
        Gen<JsBinary> gen = JsBinaryGen.biased(0,
                                               3);

        Assertions.assertTrue(gen.sample(10000)
                                 .allMatch(it -> it.value.length < 4));

        Map<Integer, Long> count = TestFun.generate(100000,
                                                    gen.map(it -> it.value.length));

        TestFun.isInMargin(100000 / 3,
                           0.1).test(count.get(0));
        TestFun.isInMargin(100000 / 3,
                           0.1).test(count.get(3));
        TestFun.isInMargin(100000 / 3,
                           0.1).test(count.get(1) + count.get(2));
    }
}
