package jsonvalues.api;

import java.math.BigInteger;
import jsonvalues.JsNull;
import jsonvalues.JsPrimitive;
import jsonvalues.gen.JsBigDecGen;
import jsonvalues.gen.JsBigIntGen;
import jsonvalues.gen.JsBinaryGen;
import jsonvalues.gen.JsBoolGen;
import jsonvalues.gen.JsDoubleGen;
import jsonvalues.gen.JsInstantGen;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsLongGen;
import jsonvalues.gen.JsStrGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestToPrimitive {

  @Test
  public void testIsPrimitive() {
    Assertions.assertTrue(JsBinaryGen.arbitrary(0,
                                                100)
                                     .sample(10000)
                                     .allMatch(JsPrimitive::isPrimitive));
    Assertions.assertTrue(JsLongGen.arbitrary()
                                   .sample(10000)
                                   .allMatch(JsPrimitive::isPrimitive));
    Assertions.assertTrue(JsIntGen.arbitrary()
                                  .sample(10000)
                                  .allMatch(JsPrimitive::isPrimitive));
    Assertions.assertTrue(JsStrGen.alphabetic()
                                  .sample(10000)
                                  .allMatch(JsPrimitive::isPrimitive));
    Assertions.assertTrue(JsInstantGen.arbitrary()
                                      .sample(10000)
                                      .allMatch(JsPrimitive::isPrimitive));
    Assertions.assertTrue(JsBigDecGen.arbitrary()
                                     .sample(10000)
                                     .allMatch(JsPrimitive::isPrimitive));
    Assertions.assertTrue(JsBigIntGen.arbitrary(BigInteger.ZERO,
                                                new BigInteger("1000000000000000000"))
                                     .sample(10000)
                                     .allMatch(JsPrimitive::isPrimitive));
    Assertions.assertTrue(JsDoubleGen.arbitrary()
                                     .sample(10000)
                                     .allMatch(JsPrimitive::isPrimitive));
    Assertions.assertTrue(JsBoolGen.arbitrary()
                                   .sample(10000)
                                   .allMatch(JsPrimitive::isPrimitive));
    Assertions.assertTrue(JsNull.NULL.isPrimitive());
  }

  @Test
  public void testToPrimitive() {
    Assertions.assertTrue(JsBinaryGen.arbitrary(0,
                                                100)
                                     .sample(10000)
                                     .allMatch(b -> b.equals(b.toJsPrimitive())));
    Assertions.assertTrue(JsLongGen.arbitrary()
                                   .sample(10000)
                                   .allMatch(b -> b.equals(b.toJsPrimitive())));
    Assertions.assertTrue(JsIntGen.arbitrary()
                                  .sample(10000)
                                  .allMatch(b -> b.equals(b.toJsPrimitive())));
    Assertions.assertTrue(JsStrGen.alphabetic()
                                  .sample(10000)
                                  .allMatch(b -> b.equals(b.toJsPrimitive())));
    Assertions.assertTrue(JsInstantGen.arbitrary()
                                      .sample(10000)
                                      .allMatch(b -> b.equals(b.toJsPrimitive())));
    Assertions.assertTrue(JsBigDecGen.arbitrary()
                                     .sample(10000)
                                     .allMatch(b -> b.equals(b.toJsPrimitive())));
    Assertions.assertTrue(JsBigIntGen.arbitrary(BigInteger.ZERO,
                                                new BigInteger("1000000000000000000"))
                                     .sample(10000)
                                     .allMatch(b -> b.equals(b.toJsPrimitive())));
    Assertions.assertTrue(JsDoubleGen.arbitrary()
                                     .sample(10000)
                                     .allMatch(b -> b.equals(b.toJsPrimitive())));
    Assertions.assertTrue(JsBoolGen.arbitrary()
                                   .sample(10000)
                                   .allMatch(b -> b.equals(b.toJsPrimitive())));
  }
}
