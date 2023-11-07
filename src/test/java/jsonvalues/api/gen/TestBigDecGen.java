package jsonvalues.api.gen;

import jsonvalues.JsBigDec;
import jsonvalues.gen.JsBigDecGen;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestBigDecGen {


    @Test
    public void biasedBigDecimalInterval() {

        Map<JsBigDec, Long> counts =
                TestFun.generate(100000,
                                 JsBigDecGen.biased(BigDecimal.valueOf(-100000000000000000L),
                                                    BigDecimal.valueOf(100000000000000000L)));

        List<JsBigDec> problematic = TestFun.list(
                                                    BigDecimal.valueOf(100000000000000000L),
                                                    BigDecimal.valueOf(-100000000000000000L),
                                                    BigDecimal.valueOf(Integer.MAX_VALUE),
                                                    BigDecimal.valueOf(Short.MAX_VALUE),
                                                    BigDecimal.valueOf(Byte.MAX_VALUE),
                                                    BigDecimal.valueOf(Integer.MIN_VALUE),
                                                    BigDecimal.valueOf(Short.MIN_VALUE),
                                                    BigDecimal.valueOf(Byte.MIN_VALUE),
                                                    BigDecimal.ZERO)
                                            .stream()
                                            .map(JsBigDec::of)
                                            .collect(Collectors.toList());

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);
    }


    @Test
    public void biasedBigDecimal() {

        Map<JsBigDec, Long> counts = TestFun.generate(100000,
                                                      JsBigDecGen.biased());

        List<JsBigDec> problematic = TestFun.list(JsBigDec.of(BigDecimal.valueOf(Long.MAX_VALUE)),
                                                  JsBigDec.of(BigDecimal.valueOf(Long.MAX_VALUE)),
                                                  JsBigDec.of(BigDecimal.valueOf(Integer.MAX_VALUE)),
                                                  JsBigDec.of(BigDecimal.valueOf(Integer.MIN_VALUE)),
                                                  JsBigDec.of(BigDecimal.valueOf(Short.MAX_VALUE)),
                                                  JsBigDec.of(BigDecimal.valueOf(Short.MIN_VALUE)),
                                                  JsBigDec.of(BigDecimal.valueOf(Byte.MAX_VALUE)),
                                                  JsBigDec.of(BigDecimal.valueOf(Byte.MIN_VALUE)),
                                                  JsBigDec.of(BigDecimal.ZERO));

        TestFun.assertGeneratedValuesHaveSameProbability(counts,
                                                         problematic,
                                                         0.05);


    }


}
