package jsonvalues.api;

import fun.gen.BigIntGen;
import java.math.BigInteger;
import jsonvalues.JsBinary;
import jsonvalues.gen.JsBinaryGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ToStringTest {

  @Test
  public void shouldBigIntToStr() {

    Assertions.assertTrue(BigIntGen.arbitrary(BigInteger.ZERO,BigInteger.valueOf(10000))
                                   .sample(1000)
                                   .allMatch(it -> it.equals(new BigInteger(it.toString()))));
  }

  @Test
  public void shouldBinaryToStr() {

    Assertions.assertTrue(JsBinaryGen.arbitrary(0,
                                                100)
                                     .sample(1000)
                                     .allMatch(it -> it.equals(JsBinary.of(it.toString()))));


  }
}
