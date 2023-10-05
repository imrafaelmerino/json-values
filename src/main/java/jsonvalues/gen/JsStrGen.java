package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.StrGen;
import jsonvalues.JsStr;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a JsStr generator. There are different static factory methods to create all kinds of generators:
 * - One character generators like {@code alphanumeric}, {@code digit}, {@code letter}, and {@code alphabetic}.
 * - String generators like {@code digits}, {@code letters}, {@code alphanumeric}, {@code alphabetic}, {@code arbitrary}, and {@code biased}.
 * <p>
 * The length of the generated strings is distributed uniformly over a specified interval.
 * <p>
 * The biased generator produces, with higher probability, potential problematic values that usually cause more
 * bugs (empty and blank strings, for example).
 * <p>
 * If none of the previous factory methods suit your needs, you can still create a JsStr generator
 * from a string generator and the function map:
 *
 * <pre>{@code
 * import fun.gen.Gen;
 * import jsonvalues.JsStr;
 *
 * Gen<String> strGen = seed -> () -> {...};
 * Gen<JsStr> jsStrGen = gen.map(JsStr::of);
 * }
 * </pre>
 */
public final class JsStrGen implements Gen<JsStr> {


    private static final Gen<JsStr> alphanumeric = new JsStrGen(StrGen.alphanumeric(1, 1));
    private static final Gen<JsStr> digit = new JsStrGen(StrGen.digits(1, 1));
    private static final Gen<JsStr> letter = new JsStrGen(StrGen.letters(1, 1));
    private static final Gen<JsStr> alphabetic = new JsStrGen(StrGen.alphabetic(1, 1));
    private static final Gen<JsStr> ascii = new JsStrGen(StrGen.ascii(1, 1));
    private final Gen<String> gen;

    /**
     * Creates a JsStr generator from a specified string generator
     *
     * @param gen the string generator
     */
    private JsStrGen(Gen<String> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     * Returns a generator that produces a single digit from 0 to 9.
     *
     * @return A JsStr generator.
     */
    public static Gen<JsStr> digit() {
        return digit;
    }

    /**
     * Returns a generator that produces a single letter from 'a' to 'z'.
     *
     * @return A JsStr generator.
     */
    public static Gen<JsStr> letter() {
        return letter;
    }

    /**
     * Returns a generator that produces a single alphabetic character.
     *
     * @return A JsStr generator.
     */
    public static Gen<JsStr> alphabetic() {
        return alphabetic;
    }

    /**
     * Returns a generator that produces a single ASCII character.
     *
     * @return A JsStr generator.
     */
    public static Gen<JsStr> ascii() {
        return ascii;
    }

    /**
     * Returns a generator that produces a single alphanumeric character.
     *
     * @return A JsStr generator.
     */
    public static Gen<JsStr> alphanumeric() {
        return alphanumeric;
    }

    /**
     * Generates a string made up of digits with a length between {@code minLength} and {@code maxLength}.
     *
     * @param minLength Minimum length of the string (inclusive).
     * @param maxLength Maximum length of the string (inclusive).
     * @return A string generator.
     */
    public static Gen<JsStr> digits(final int minLength,
                                    final int maxLength
                                   ) {
        return new JsStrGen(StrGen.digits(minLength,
                                          maxLength));
    }

    /**
     * Generates a string made up of digits with a length of {@code length}.
     *
     * @param length Length of the string.
     * @return A string generator.
     */
    public static Gen<JsStr> digits(final int length) {
        return digits(length, length);
    }

    /**
     * Generates a string made up of letters with a length between {@code minLength} and {@code maxLength}.
     *
     * @param minLength Minimum length of the string (inclusive).
     * @param maxLength Maximum length of the string (inclusive).
     * @return A string generator.
     */
    public static Gen<JsStr> letters(final int minLength,
                                     final int maxLength
                                    ) {
        return new JsStrGen(StrGen.letters(minLength,
                                           maxLength));
    }

    /**
     * Generates a string made up of letters with a length of {@code length}.
     *
     * @param length Length of the string.
     * @return A string generator.
     */
    public static Gen<JsStr> letters(final int length) {
        return letters(length, length);
    }

    /**
     * Generates a string made up of alphabetic characters with a length between {@code minLength} and {@code maxLength}.
     *
     * @param minLength Minimum length of the string (inclusive).
     * @param maxLength Maximum length of the string (inclusive).
     * @return A string generator.
     */
    public static Gen<JsStr> alphabetic(final int minLength,
                                        final int maxLength
                                       ) {
        return new JsStrGen(StrGen.alphabetic(minLength,
                                              maxLength));
    }

    /**
     * Generates a string made up of ASCII characters with a length between {@code minLength} and {@code maxLength}.
     *
     * @param minLength Minimum length of the string (inclusive).
     * @param maxLength Maximum length of the string (inclusive).
     * @return A string generator.
     */
    public static Gen<JsStr> ascii(final int minLength,
                                   final int maxLength
                                  ) {
        return new JsStrGen(StrGen.ascii(minLength,
                                         maxLength));
    }

    /**
     * Generates a string made up of alphabetic characters
     *
     * @param length length of the string
     * @return a string generator
     */
    public static Gen<JsStr> alphabetic(final int length) {
        return alphabetic(length, length);
    }

    /**
     * Generates a string made up of alphabetic characters with a length of {@code length}.
     *
     * @param length Length of the string.
     * @return A string generator.
     */
    public static Gen<JsStr> ascii(final int length) {
        return ascii(length, length);
    }

    /**
     * Generates a string made up of alphanumeric characters with a length between {@code minLength} and {@code maxLength}.
     *
     * @param minLength Minimum length of the string (inclusive).
     * @param maxLength Maximum length of the string (inclusive).
     * @return A string generator.
     */
    public static Gen<JsStr> alphanumeric(final int minLength,
                                          final int maxLength
                                         ) {
        return new JsStrGen(StrGen.alphanumeric(minLength,
                                                maxLength));
    }

    /**
     * Generates a string made up of alphanumeric characters with a length of {@code length}.
     *
     * @param length Length of the string.
     * @return A string generator.
     */
    public static Gen<JsStr> alphanumeric(final int length) {
        return alphanumeric(length, length);
    }

    /**
     * Returns a generator that produces arbitrary strings with a length between {@code minLength} and {@code maxLength}.
     *
     * @param minLength Minimum length of the string (inclusive).
     * @param maxLength Maximum length of the string (inclusive).
     * @return A string generator.
     */
    public static Gen<JsStr> arbitrary(final int minLength,
                                       final int maxLength
                                      ) {
        return new JsStrGen(StrGen.arbitrary(minLength,
                                             maxLength));
    }


    /**
     * Returns a generator that produces arbitrary strings with a length of {@code length}.
     *
     * @param length Length of the string.
     * @return A string generator.
     */
    public static Gen<JsStr> arbitrary(final int length) {
        return arbitrary(length, length);
    }


    /**
     * Returns a biased generator that produces, with higher probability, potential problematic values
     * that usually cause more bugs. These values include empty strings (if {@code minLength} is equal to zero),
     * blank strings of length {@code minLength}, blank strings of length {@code maxLength}, arbitrary strings of length
     * {@code minLength}, and arbitrary strings of length {@code maxLength}.
     *
     * @param minLength Minimum length of the string (inclusive).
     * @param maxLength Maximum length of the string (inclusive).
     * @return A string generator.
     */
    public static Gen<JsStr> biased(final int minLength,
                                    final int maxLength
                                   ) {
        return new JsStrGen(StrGen.biased(minLength,
                                          maxLength));
    }

    /**
     * Returns a biased generator that produces, with higher probability, potential problematic values
     * that usually cause more bugs. These values include blank strings.
     *
     * @param length Minimum length of the string (inclusive).
     * @return A string generator.
     */
    public static Gen<JsStr> biased(final int length) {
        return biased(length, length);
    }


    @Override
    public Supplier<JsStr> apply(Random seed) {
        return gen.map(JsStr::of)
                  .apply(requireNonNull(seed));
    }
}
