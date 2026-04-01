package jsonvalues.api.gen;

import fun.gen.Gen;
import java.math.BigInteger;
import java.util.List;
import jsonvalues.JsBigInt;
import jsonvalues.gen.JsBigIntGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BigIntGenTest {


  @Test
  public void shouldArbitrary() {

    BigInteger min = new BigInteger("1000000000");
    BigInteger max = new BigInteger("2000000000");
    Gen<JsBigInt> arbitraryBigIntGen =
        JsBigIntGen.arbitrary(min,
                              max
                             );

    Assertions.assertTrue(
        arbitraryBigIntGen
            .sample(100000)
            .allMatch(bi -> bi.value.compareTo(min) >= 0 && bi.value.compareTo(max) <= 0)
                         );
  }


  @Test
  public void shouldBiasedWithInterval() {
    BigInteger min = new BigInteger("-100000000000000000000");
    BigInteger max = new BigInteger("1000000000000000000000");
    var gen =
        JsBigIntGen.biased(min,
                           max
                          );

    List<JsBigInt> problematic =
        FunTest.list(JsBigInt.of(min),
                     JsBigInt.of(max),
                     JsBigInt.of(BigInteger.valueOf(Integer.MIN_VALUE)
                                           .subtract(BigInteger.ONE)),
                     JsBigInt.of(BigInteger.valueOf(Integer.MAX_VALUE)
                                           .add(BigInteger.ONE)),
                     JsBigInt.of(BigInteger.valueOf(Long.MAX_VALUE)
                                           .add(BigInteger.ONE)),
                     JsBigInt.of(BigInteger.valueOf(Long.MIN_VALUE)
                                           .subtract(BigInteger.ONE)),
                     JsBigInt.of(BigInteger.ZERO)
                    );

    FunTest.assertGeneratedValuesHaveSameProbability(gen.collect(1000000),
                                                     problematic,
                                                     0.05);


  }

  @Test
  public void shouldBiased() {
    var gen = JsBigIntGen.biased();

    List<JsBigInt> problematic =
        FunTest.list(JsBigInt.of(BigInteger.valueOf(Integer.MIN_VALUE)
                                           .subtract(BigInteger.ONE)),
                     JsBigInt.of(BigInteger.valueOf(Integer.MAX_VALUE)
                                           .add(BigInteger.ONE)),
                     JsBigInt.of(BigInteger.valueOf(Long.MAX_VALUE)
                                           .add(BigInteger.ONE)),
                     JsBigInt.of(BigInteger.valueOf(Long.MIN_VALUE)
                                           .subtract(BigInteger.ONE)),
                     JsBigInt.of(BigInteger.ZERO));

    FunTest.assertGeneratedValuesHaveSameProbability(gen.collect(1000000),
                                                     problematic,
                                                     0.05);


  }


}
