package jsonvalues.api.gen;

import jsonvalues.JsBool;
import jsonvalues.gen.JsBoolGen;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestBoolGen {



    @Test
    public void testBooleanGen() {

        Map<JsBool, Long> counts = TestFun.generate(100000,
                                                    JsBoolGen.arbitrary());

        List<JsBool> values = TestFun.list(JsBool.TRUE,
                                           JsBool.FALSE);

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         values,
                                                         0.05);

    }
}
