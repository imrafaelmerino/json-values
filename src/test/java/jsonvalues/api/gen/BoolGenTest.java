package jsonvalues.api.gen;

import java.util.List;
import java.util.Map;
import jsonvalues.JsBool;
import jsonvalues.gen.JsBoolGen;
import org.junit.jupiter.api.Test;

public class BoolGenTest {


  @Test
  public void shouldBooleanGen() {

    Map<JsBool, Long> counts = FunTest.generate(100000,
                                                JsBoolGen.arbitrary());

    List<JsBool> values = FunTest.list(JsBool.TRUE,
                                       JsBool.FALSE);

    FunTest.assertGeneratedValuesHaveSameProbability(counts,
                                                     values,
                                                     0.05);

  }
}
