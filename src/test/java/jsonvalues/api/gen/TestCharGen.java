package jsonvalues.api.gen;

import java.util.Map;
import jsonvalues.JsStr;
import jsonvalues.gen.JsStrGen;
import org.junit.jupiter.api.Test;

public class TestCharGen {


  @Test
  public void testCharGen() {

    Map<JsStr, Long> countsLetters = TestFun.generate(100000,
                                                      JsStrGen.letter());

    Map<JsStr, Long> countsDigits = TestFun.generate(100000,
                                                     JsStrGen.digit());

    Map<JsStr, Long> countAlpha = TestFun.generate(1000000,
                                                   JsStrGen.alphabetic());

    TestFun.assertGeneratedValuesHaveSameProbability(countsLetters,
                                                     countsLetters.keySet(),
                                                     0.1);
    TestFun.assertGeneratedValuesHaveSameProbability(countsDigits,
                                                     countsDigits.keySet(),
                                                     0.1);

    TestFun.assertGeneratedValuesHaveSameProbability(countAlpha,
                                                     countAlpha.keySet(),
                                                     0.1);

  }


}
