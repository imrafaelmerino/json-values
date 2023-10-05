package jsonvalues.gen;

import fun.gen.Gen;
import org.junit.jupiter.api.Assertions;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TestFun {
    static <I> void assertGeneratedValuesHaveSameProbability(Map<I, Long> counts,
                                                             Collection<I> values,
                                                             double errorMargin) {
        if (errorMargin < 0.0) throw new IllegalArgumentException("errorMargin < 0");
        if (errorMargin > 1.0) throw new IllegalArgumentException("errorMargin > 1");

        List<Long> valueCounts = values.stream().map(key -> {
            if (!counts.containsKey(key))
                throw new RuntimeException(key + " was not generated");
            return counts.get(key);
        }).collect(Collectors.toList());
        long expected = avg(valueCounts);

        final Predicate<Long> isOk = isInMargin(expected,
                                                errorMargin);
        values.forEach(val -> {
            Assertions.assertTrue(isOk.test(counts.get(val)));
        });
    }


    static Predicate<Long> isInMargin(long expected,
                                      double margin) {
        return times -> {
            long abs = Math.abs(times - expected);
            double v = margin * expected;
            return abs < v;
        };
    }

    static <I> Map<I, Long> generate(int times,
                                     Gen<I> generator) {
        HashMap<I, Long> results = new HashMap<>();

        generator.sample(times)
                 .forEach(countOccurrences(results));

        return results;
    }


    private static <I> Consumer<I> countOccurrences(Map<I, Long> results) {
        return value -> results.compute(value,
                                        (k, old) -> old == null ?
                                                    1L :
                                                    old + 1L);
    }

    private static long avg(List<Long> xs) {
        return xs.stream()
                 .reduce(Long::sum)
                 .get() / xs.size();
    }

    static <I> long countKeys(Map<I, Long> map,
                              Predicate<I> predicate) {
        return map.keySet()
                  .stream()
                  .filter(predicate)
                  .count();
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    static <T> List<T> list(T... elems) {
        List<T> result = new ArrayList<>();
        Collections.addAll(result,
                           elems);
        return result;
    }
}
