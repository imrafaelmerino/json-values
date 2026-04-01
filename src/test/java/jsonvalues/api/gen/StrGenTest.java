package jsonvalues.api.gen;


import java.util.Map;
import jsonvalues.gen.JsStrGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StrGenTest {


  @Test
  public void shouldArbitrary() {

    Map<Integer, Long> count = FunTest.generate(10000000,
                                                JsStrGen.arbitrary(0,
                                                                   3)
                                                        .map(it -> it.value.length())
                                               );
    FunTest.assertGeneratedValuesHaveSameProbability(count,
                                                     count.keySet(),
                                                     0.15);
  }

  @Test
  public void shouldBiased() {

    Assertions.assertTrue(JsStrGen.biased(0)
                                  .sample(1000)
                                  .allMatch(t -> t.value.isEmpty()));

    Assertions.assertTrue(JsStrGen.biased(0,
                                          1)
                                  .sample(1000)
                                  .allMatch(it -> it.value.length() < 3));

    Map<Integer, Long> count = FunTest.generate(10000000,
                                                JsStrGen.biased(0,
                                                                3)
                                                        .map(it -> it.value.length()));

    Assertions.assertTrue(count.get(0) > count.get(1));
    Assertions.assertTrue(count.get(0) > count.get(2));
    Assertions.assertTrue(count.get(3) > count.get(1));
    Assertions.assertTrue(count.get(3) > count.get(2));


  }

  @Test
  public void shouldGenerateDigitsWithinInterval() {

    Assertions.assertTrue(JsStrGen.digits(0,
                                          2)
                                  .sample(100000)
                                  .allMatch(it -> it.value.isEmpty() ||
                                                  ((it.value.length() < 3) && it.value.chars()
                                                                                      .allMatch(Character::isDigit))));
  }

  @Test
  public void shouldDigits() {

    Assertions.assertTrue(JsStrGen.digits(20)
                                  .sample(100000)
                                  .allMatch(it -> it.value.isEmpty() ||
                                                  ((it.value.length() == 20) && it.value.chars()
                                                                                        .allMatch(Character::isDigit))));
  }

  @Test
  public void shouldGenerateAlphanumericWithinInterval() {
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
  public void shouldAlphanumeric() {
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
  public void shouldGenerateAlphabeticWithinInterval() {
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
  public void shouldAlphabetic() {
    Assertions.assertTrue(
        JsStrGen.alphabetic(10)
                .sample(100000)
                .allMatch(str ->
                              str.value.isEmpty() ||
                              (str.value.length() == 10 && str.value.chars()
                                                                    .allMatch(Character::isAlphabetic))));
  }

  @Test
  public void shouldLetters() {
    Assertions.assertTrue(JsStrGen.letters(0,
                                           2)
                                  .sample(100000)
                                  .allMatch(it -> it.value.isEmpty() ||
                                                  it.value.chars()
                                                          .allMatch(Character::isLetter)));
  }

  @Test
  public void shouldStringGen() {

    Map<String, Long> countsLetter = FunTest.generate(1000000,
                                                      JsStrGen.letter()
                                                              .map(it -> it.value));

    Map<String, Long> countsDigit = FunTest.generate(1000000,
                                                     JsStrGen.digit()
                                                             .map(it -> it.value));

    Map<String, Long> countAlpha = FunTest.generate(10000000,
                                                    JsStrGen.alphabetic()
                                                            .map(it -> it.value));

    FunTest.assertGeneratedValuesHaveSameProbability(countsLetter,
                                                     countsLetter.keySet(),
                                                     0.1);
    FunTest.assertGeneratedValuesHaveSameProbability(countsDigit,
                                                     countsDigit.keySet(),
                                                     0.1);
    FunTest.assertGeneratedValuesHaveSameProbability(countAlpha,
                                                     countAlpha.keySet(),
                                                     0.1);


  }
}
