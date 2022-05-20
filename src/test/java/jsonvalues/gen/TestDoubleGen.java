package jsonvalues.gen;

import fun.gen.DoubleGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDoubleGen {


    @Test
    public void test_interval() {

        double min = 0.0;
        double max = 100.0;
        Assertions.assertTrue(
                DoubleGen.arbitrary(min,
                                    max).then(n ->
                                                      JsDoubleGen.arbitrary(min,
                                                                            n))
                         .sample(100000).allMatch(it -> it.isDouble(d -> d >= min && d <= max))
        );


        Assertions.assertTrue(
                DoubleGen.arbitrary(min,
                                    max).then(n ->
                                                      JsDoubleGen.biased(0.0,
                                                                         n))
                         .sample(100000).allMatch(it -> it.isDouble(d -> d >= min && d <= max))
        );
    }
}
