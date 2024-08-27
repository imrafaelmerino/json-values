package jsonvalues.api.gen;

import fun.gen.IntGen;
import jsonvalues.gen.JsIntGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestIntegerGen {


  @Test
  public void test_interval() {

    int min = 0;
    int max = Integer.MAX_VALUE - 1000000;
    Assertions.assertTrue(IntGen.arbitrary(min,
                                           max)
                                .then(n ->
                                          JsIntGen.arbitrary(min,
                                                             n))
                                .sample(100000)
                                .allMatch(it -> it.isInt(d -> d >= min && d <= max))
                         );

    Assertions.assertTrue(IntGen.arbitrary(min,
                                           max)
                                .then(n ->
                                          JsIntGen.biased(min,
                                                          n))
                                .sample(100000)
                                .allMatch(it -> it.isInt(d -> d >= min && d <= max))
                         );
  }
}
