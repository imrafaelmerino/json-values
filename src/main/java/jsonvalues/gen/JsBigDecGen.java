package jsonvalues.gen;

import fun.gen.BigDecGen;
import fun.gen.Gen;
import jsonvalues.JsBigDec;

import java.math.BigDecimal;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class JsBigDecGen implements Gen<JsBigDec> {

    public static final Gen<JsBigDec> biased = JsBigDecGen.of(BigDecGen.biased);
    public static final Gen<JsBigDec> arbitrary = JsBigDecGen.of(BigDecGen.arbitrary);
    private final Gen<BigDecimal> gen;

    private JsBigDecGen(Gen<BigDecimal> gen) {
        this.gen = gen;
    }

    public static Gen<JsBigDec> arbitrary(final BigDecimal min,
                                          final BigDecimal max) {
        return JsBigDecGen.of(BigDecGen.arbitrary(min,
                                                  max));
    }

    public static Gen<JsBigDec> biased(final BigDecimal min,
                                       final BigDecimal max) {
        return JsBigDecGen.of(BigDecGen.biased(min,
                                               max));
    }

    public static Gen<JsBigDec> biased(final long min,
                                       final long max) {
        return biased(BigDecimal.valueOf(min),BigDecimal.valueOf(max));
    }

    public static Gen<JsBigDec> of(final Gen<BigDecimal> gen) {
        return new JsBigDecGen(requireNonNull(gen));
    }

    @Override
    public Supplier<JsBigDec> apply(Random seed) {
        return gen.map(JsBigDec::of).apply(seed);
    }

}
