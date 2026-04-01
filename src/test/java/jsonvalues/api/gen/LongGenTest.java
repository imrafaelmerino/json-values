package jsonvalues.api.gen;

import fun.gen.LongGen;
import jsonvalues.gen.JsLongGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LongGenTest {


  @Test
  public void shouldGenerateValuesWithinInterval() {

    long min = 0;
    long max = Integer.MAX_VALUE;
    Assertions.assertTrue(LongGen.arbitrary(min,
                                            max)
                                 .then(n -> JsLongGen.arbitrary(min,
                                                                n))
                                 .sample(100000)
                                 .allMatch(it -> it.isLong(d -> d >= min && d <= max))
                         );

    Assertions.assertTrue(LongGen.arbitrary(min,
                                            max)
                                 .then(n ->
                                           JsLongGen.biased(min,
                                                            n))
                                 .sample(100000)
                                 .allMatch(it -> it.isLong(d -> d >= min && d <= max))
                         );
  }
}
