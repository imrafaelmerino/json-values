package jsonvalues.api.gen;


import java.util.Map;
import jsonvalues.gen.JsStrGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestStrGen {


  @Test
  public void testArbitrary() {

    Map<Integer, Long> count = TestFun.generate(10000000,
                                                JsStrGen.arbitrary(0,
                                                                   3)
                                                        .map(it -> it.value.length())
                                               );
    TestFun.assertGeneratedValuesHaveSameProbability(count,
                                                     count.keySet(),
                                                     0.15);
  }

  @Test
  public void testBiased() {

    Assertions.assertTrue(JsStrGen.biased(0)
                                  .sample(1000)
                                  .allMatch(t -> t.value.isEmpty()));

    Assertions.assertTrue(JsStrGen.biased(0,
                                          1)
                                  .sample(1000)
                                  .allMatch(it -> it.value.length() < 3));

    Map<Integer, Long> count = TestFun.generate(10000000,
                                                JsStrGen.biased(0,
                                                                3)
                                                        .map(it -> it.value.length()));

    Assertions.assertTrue(count.get(0) > count.get(1));
    Assertions.assertTrue(count.get(0) > count.get(2));
    Assertions.assertTrue(count.get(3) > count.get(1));
    Assertions.assertTrue(count.get(3) > count.get(2));


  }

  @Test
  public void testDigits_interval() {

    Assertions.assertTrue(JsStrGen.digits(0,
                                          2)
                                  .sample(100000)
                                  .allMatch(it -> it.value.isEmpty() ||
                                                  ((it.value.length() < 3) && it.value.chars()
                                                                                      .allMatch(Character::isDigit))));
  }

  @Test
  public void testDigits() {

    Assertions.assertTrue(JsStrGen.digits(20)
                                  .sample(100000)
                                  .allMatch(it -> it.value.isEmpty() ||
                                                  ((it.value.length() == 20) && it.value.chars()
                                                                                        .allMatch(Character::isDigit))));
  }

  @Test
  public void alphanumeric_interval() {
    Assertions.assertTrue(
        JsStrGen.alphanumeric(0,
                              2)
                .sample(100000)
                .allMatch(str ->
                              str.value.isEmpty() ||
                              (str.value.length() < 3 &&
                               str.value.chars()
                                        .allMatch(c -> Character.isDigit(c) || Character.isAlphabetic(c)))));
  }

  @Test
  public void alphanumeric() {
    Assertions.assertTrue(
        JsStrGen.alphanumeric(2)
                .sample(100000)
                .allMatch(str ->
                              str.value.isEmpty() ||
                              (str.value.length() == 2 &&
                               str.value.chars()
                                        .allMatch(c -> Character.isDigit(c) || Character.isAlphabetic(c)))));
  }

  @Test
  public void alphabetic_interval() {
    Assertions.assertTrue(
        JsStrGen.alphabetic(0,
                            2)
                .sample(100000)
                .allMatch(str ->
                              str.value.isEmpty() ||
                              (str.value.length() < 3 && str.value.chars()
                                                                  .allMatch(Character::isAlphabetic))));
  }

  @Test
  public void alphabetic() {
    Assertions.assertTrue(
        JsStrGen.alphabetic(10)
                .sample(100000)
                .allMatch(str ->
                              str.value.isEmpty() ||
                              (str.value.length() == 10 && str.value.chars()
                                                                    .allMatch(Character::isAlphabetic))));
  }

  @Test
  public void letters() {
    Assertions.assertTrue(JsStrGen.letters(0,
                                           2)
                                  .sample(100000)
                                  .allMatch(it -> it.value.isEmpty() ||
                                                  it.value.chars()
                                                          .allMatch(Character::isLetter)));
  }

  @Test
  public void testStringGen() {

    Map<String, Long> countsLetter = TestFun.generate(1000000,
                                                      JsStrGen.letter()
                                                              .map(it -> it.value));

    Map<String, Long> countsDigit = TestFun.generate(1000000,
                                                     JsStrGen.digit()
                                                             .map(it -> it.value));

    Map<String, Long> countAlpha = TestFun.generate(10000000,
                                                    JsStrGen.alphabetic()
                                                            .map(it -> it.value));

    TestFun.assertGeneratedValuesHaveSameProbability(countsLetter,
                                                     countsLetter.keySet(),
                                                     0.1);
    TestFun.assertGeneratedValuesHaveSameProbability(countsDigit,
                                                     countsDigit.keySet(),
                                                     0.1);
    TestFun.assertGeneratedValuesHaveSameProbability(countAlpha,
                                                     countAlpha.keySet(),
                                                     0.1);


  }
}
