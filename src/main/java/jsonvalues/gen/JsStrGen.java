package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.StrGen;
import jsonvalues.JsStr;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public final class JsStrGen implements Gen<JsStr> {

    /**
     * Generator tha produces an alphanumeric character
     */
    private static final Gen<JsStr> alphanumeric = new JsStrGen(StrGen.alphanumeric);
    private static final Gen<JsStr> digit = new JsStrGen(StrGen.digit);
    private static final Gen<JsStr> letter = new JsStrGen(StrGen.letter);
    private static final Gen<JsStr> alphabetic = new JsStrGen(StrGen.alphabetic);
    private final Gen<String> gen;

    /**
     *
     * @param gen
     */
    public JsStrGen(Gen<String> gen) {
        this.gen = requireNonNull(gen);
    }

    /**
     * Generator tha produces a digit from 0 to 9
     */
    public static Gen<JsStr> digit() {
        return digit;
    }

    /**
     * Generator tha produces a letter from a to z
     */
    public static Gen<JsStr> letter() {
        return letter;
    }

    /**
     * Generator tha produces an alphabetic character
     */
    public static Gen<JsStr> alphabetic() {
        return alphabetic;
    }

    /**
     * Generates a seq of digits
     *
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> digits(final int minLength,
                                    final int maxLength) {
        return new JsStrGen(StrGen.digits(minLength,
                                          maxLength));
    }

    /**
     * Generates a seq of letters
     *
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> letters(final int minLength,
                                     final int maxLength) {
        return new JsStrGen(StrGen.letters(minLength,
                                           maxLength));
    }

    /**
     * Generates a seq of alphabetic characters
     *
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> alphabetic(final int minLength,
                                        final int maxLength) {
        return new JsStrGen(StrGen.alphabetic(minLength,
                                              maxLength));
    }

    /**
     * Generates a seq of alphanumeric characters
     *
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> alphanumeric(final int minLength,
                                          final int maxLength) {
        return new JsStrGen(StrGen.alphanumeric(minLength,
                                                maxLength));
    }

    /**
     * Generates a seq of arbitrary characters
     *
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> arbitrary(final int minLength,
                                       final int maxLength) {
        return new JsStrGen(StrGen.arbitrary(minLength,
                                             maxLength));
    }

    /**
     * @param minLength minimum length of the string
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> biased(final int minLength,
                                    final int maxLength) {
        return new JsStrGen(StrGen.biased(minLength,
                                          maxLength));
    }

    /**
     *
     * @param seed the function argument
     * @return
     */
    @Override
    public Supplier<JsStr> apply(Random seed) {
        return gen.map(JsStr::of)
                  .apply(seed);
    }
}
