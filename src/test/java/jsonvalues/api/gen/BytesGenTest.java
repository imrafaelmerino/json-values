package jsonvalues.api.gen;

import fun.gen.Gen;
import java.util.Map;
import jsonvalues.JsBinary;
import jsonvalues.gen.JsBinaryGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BytesGenTest {

  @Test
  public void shouldArbitrary() {

    Assertions.assertTrue(JsBinaryGen.arbitrary(0,
                                                10)
                                     .sample(10000)
                                     .allMatch(it -> it.value.length <= 10)
                         );

    Map<Integer, Long> count =
        FunTest.generate(1000000,
                         JsBinaryGen.arbitrary(0,
                                               10)
                                    .map(it -> it.value.length));

    FunTest.assertGeneratedValuesHaveSameProbability(count,
                                                     FunTest.list(0,
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
  public void shouldBiased() {
    Gen<JsBinary> gen = JsBinaryGen.biased(0,
                                           3);

    int times = 2000000;
    Map<Integer, Long> count = FunTest.generate(times,
                                                gen.map(it -> it.value.length));

    Assertions.assertTrue(FunTest.isInMargin((long) (times * 0.75),
                                             0.1)
                                 .test(count.get(0) + count.get(3)));

    Assertions.assertTrue(FunTest.isInMargin((long) (times * 0.25),
                                             0.1)
                                 .test(count.get(1) + count.get(2)));
  }
}
