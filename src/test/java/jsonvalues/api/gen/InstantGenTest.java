package jsonvalues.api.gen;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jsonvalues.JsInstant;
import jsonvalues.gen.JsInstantGen;
import org.junit.jupiter.api.Test;

public class InstantGenTest {

  @Test
  public void shouldBiasedInstant() {

    Map<JsInstant, Long> counts = FunTest.generate(100000,
                                                   JsInstantGen.biased());

    List<JsInstant> problematic = Stream.of(0L,
                                            Instant.MAX.getEpochSecond(),
                                            Instant.MIN.getEpochSecond(),
                                            (long) Integer.MAX_VALUE,
                                            (long) Integer.MIN_VALUE,
                                            0L)
                                        .map(epochMilli -> JsInstant.of(Instant.ofEpochSecond(epochMilli)))
                                        .collect(Collectors.toList());

    FunTest.assertGeneratedValuesHaveSameProbability(counts,
                                                     problematic,
                                                     0.05);


  }

  @Test
  public void shouldBiasedInstantInterval() {

    Map<JsInstant, Long> counts = FunTest.generate(100000,
                                                   JsInstantGen.biased(Integer.MIN_VALUE - 2L,
                                                                       Integer.MAX_VALUE + 2L));

    List<JsInstant> problematic = Stream.of(0L,
                                            Integer.MIN_VALUE - 2L,
                                            Integer.MAX_VALUE + 2L,
                                            (long) Integer.MAX_VALUE,
                                            (long) Integer.MIN_VALUE,
                                            0L)
                                        .map(epochMilli -> JsInstant.of(Instant.ofEpochSecond(epochMilli)))
                                        .collect(Collectors.toList());

    FunTest.assertGeneratedValuesHaveSameProbability(counts,
                                                     problematic,
                                                     0.05);


  }
}
