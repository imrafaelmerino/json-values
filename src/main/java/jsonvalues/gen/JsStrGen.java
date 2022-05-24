package jsonvalues.gen;

import fun.gen.Gen;
import fun.gen.StrGen;
import jsonvalues.JsStr;

import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Represents a JsStr generator. There are different static factory methods to create all kind of generators:
 * - One character generators like <code>alphanumeric</code>, <code>digit</code>, <code>letter</code> and <code>alphabetic</code>.
 * - String generators like <code>digits</code>, <code>letters</code>, <code>alphanumeric</code>, <code>alphabetic</code>, <code>arbitrary</code> and <code>biased</code>.
 * <p>
 * The length of the generated strings is distributed uniformly over a specified interval.
 *
 * The biased generator produces, with higher probability, potential problematic values that usually cause more
 * bugs (empty and blank strings for example).
 * <p>
 * If none of the previous factory methods suit your needs, you can still create a JsStr generator
 * from a string generator and the function map:
 *
 * <pre>{@code
 *      import fun.gen.Gen;
 *      import jsonvalues.JsStr;
 *
 *      Gen<String> strGen = seed -> () -> {...};
 *      Gen<JsStr> jsStrGen = gen.map(JsStr::of)
 *      }
 *  </pre>
 *  <p>
 */
public final class JsStrGen implements Gen<JsStr> {


    private static final Gen<JsStr> alphanumeric = new JsStrGen(StrGen.alphanumeric());
    private static final Gen<JsStr> digit = new JsStrGen(StrGen.digit());
    private static final Gen<JsStr> letter = new JsStrGen(StrGen.letter());
    private static final Gen<JsStr> alphabetic = new JsStrGen(StrGen.alphabetic());
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
     * Generator tha produces a digit from 0 to 9
     * @return a JsStr generator
     */
    public static Gen<JsStr> digit() {
        return digit;
    }

    /**
     * Generator tha produces a letter from a to z
     * @return a JsStr generator
     */
    public static Gen<JsStr> letter() {
        return letter;
    }

    /**
     * Generator tha produces an alphabetic character
     * @return a JsStr generator
     */
    public static Gen<JsStr> alphabetic() {
        return alphabetic;
    }

    /**
     * Generator tha produces an alphanumeric character
     * @return a JsStr generator
     */
    public static Gen<JsStr> alphanumeric() {
        return alphanumeric;
    }

    /**
     * Generates a string made up of digits
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
     * Generates a string made up of letters
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
     * Generates a string made up of alphabetic characters
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
     * Generates a string made up of alphanumeric characters
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
     * returns a generator that produces arbitrary strings
     *
     * @param minLength minimum length of the string (inclusive)
     * @param maxLength maximum length of the string (inclusive)
     *
     * @return a string generator
     */
    public static Gen<JsStr> arbitrary(final int minLength,
                                       final int maxLength) {
        return new JsStrGen(StrGen.arbitrary(minLength,
                                             maxLength));
    }


    /**
     *
     * returns a biased generators that produces, with higher probability, potential problematic values
     * that usually cause more bugs. These values are:
     * <pre>
     * - empty string  (if minLength is equal to zero)
     * - blank string of length minLength
     * - blank string of length maxLength
     * - arbitrary string of length minLength
     * - arbitrary string of length maxLength
     * </pre>
     *
     * @param minLength minimum length of the string (inclusive)
     * @param maxLength maximum length of the string (inclusive)
     * @return a string generator
     */
    public static Gen<JsStr> biased(final int minLength,
                                    final int maxLength) {
        return new JsStrGen(StrGen.biased(minLength,
                                          maxLength));
    }

    /**
     * Returns a supplier from the specified seed that generates a new JsStr each time it's called
     *
     * @param seed the generator seed
     * @return a JsStr supplier
     */
    @Override
    public Supplier<JsStr> apply(Random seed) {
        return gen.map(JsStr::of)
                  .apply(requireNonNull(seed));
    }
}
