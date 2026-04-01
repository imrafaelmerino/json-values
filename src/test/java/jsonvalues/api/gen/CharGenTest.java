package jsonvalues.api.gen;

import java.util.Map;
import jsonvalues.JsStr;
import jsonvalues.gen.JsStrGen;
import org.junit.jupiter.api.Test;

public class CharGenTest {


  @Test
  public void shouldCharGen() {

    Map<JsStr, Long> countsLetters = FunTest.generate(100000,
                                                      JsStrGen.letter());

    Map<JsStr, Long> countsDigits = FunTest.generate(100000,
                                                     JsStrGen.digit());

    Map<JsStr, Long> countAlpha = FunTest.generate(1000000,
                                                   JsStrGen.alphabetic());

    FunTest.assertGeneratedValuesHaveSameProbability(countsLetters,
                                                     countsLetters.keySet(),
                                                     0.1);
    FunTest.assertGeneratedValuesHaveSameProbability(countsDigits,
                                                     countsDigits.keySet(),
                                                     0.1);

    FunTest.assertGeneratedValuesHaveSameProbability(countAlpha,
                                                     countAlpha.keySet(),
                                                     0.1);

  }


}
