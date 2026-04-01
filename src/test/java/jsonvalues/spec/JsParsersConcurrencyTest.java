package jsonvalues.spec;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("stress")
public class JsParsersConcurrencyTest {

  private static final int THREADS = 8;
  private static final int ITERATIONS_PER_THREAD = 250;

  @Test
  public void shouldKeepConsistentResultsForNumericEdgeCasesUnderConcurrency() throws Exception {
    JsObjSpecParser intParser = JsObjSpecParser.of(JsObjSpec.of("x",
                                                                JsSpecs.integer()));
    JsObjSpecParser longParser = JsObjSpecParser.of(JsObjSpec.of("x",
                                                                 JsSpecs.longInteger()));
    JsObjSpecParser doubleParser = JsObjSpecParser.of(JsObjSpec.of("x",
                                                                   JsSpecs.doubleNumber()));
    JsObjSpecParser decimalParser = JsObjSpecParser.of(JsObjSpec.of("x",
                                                                    JsSpecs.decimal()));

    List<String> numbers = List.of("0",
                                   "-0",
                                   "1",
                                   "-1",
                                   "2147483647",
                                   "2147483648",
                                   "-2147483648",
                                   "-2147483649",
                                   "9223372036854775807",
                                   "9223372036854775808",
                                   "-9223372036854775808",
                                   "-9223372036854775809",
                                   "1.0",
                                   "-1.0",
                                   "1e10",
                                   "1E-10",
                                   "1.7976931348623157e308",
                                   "4.9406564584124654e-324",
                                   ".1",
                                   "1.",
                                   "1e",
                                   "1e-",
                                   "+1",
                                   "01",
                                   "-01");

    List<ParserWorkload> workloads = List.of(new ParserWorkload("int",
                                                                 intParser,
                                                                 numbers),
                                             new ParserWorkload("long",
                                                                 longParser,
                                                                 numbers),
                                             new ParserWorkload("double",
                                                                 doubleParser,
                                                                 numbers),
                                             new ParserWorkload("decimal",
                                                                 decimalParser,
                                                                 numbers));

    for (ParserWorkload workload : workloads) {
      assertConcurrentConsistency(workload);
    }
  }

  @Test
  public void shouldParseNestedStructuresConcurrentlyForBytesAndStream() throws Exception {
    JsObjSpec nestedSpec = JsObjSpec.of("id",
                                        JsSpecs.longInteger(),
                                        "score",
                                        JsSpecs.doubleNumber(),
                                        "name",
                                        JsSpecs.str(),
                                        "tags",
                                        JsSpecs.arrayOfStr());
    JsObjSpecParser objParser = JsObjSpecParser.of(nestedSpec);
    JsArraySpecParser arrayParser = JsArraySpecParser.of(JsSpecs.arrayOfDouble());

    List<String> objectPayloads = List.of("{\"id\":1,\"score\":1250.0,\"name\":\"alpha\",\"tags\":[\"x\",\"y\"]}",
                                          "{\"id\":-9223372036854775808,\"score\":4.9406564584124654e-324,\"name\":\"beta\",\"tags\":[]}",
                                          "{\"id\":9223372036854775807,\"score\":1.7976931348623157e308,\"name\":\"gamma\",\"tags\":[\"a\",\"b\",\"c\"]}");

    List<String> arrayPayloads = List.of("[0.0,1.0,-1.0,1.5e100]",
                                         "[4.9406564584124654e-324,2.2250738585072014e-308]",
                                         "[1.7976931348623157e308,-1.7976931348623157e308]");

    List<Outcome> expectedObjects = new ArrayList<>();
    for (String payload : objectPayloads) {
      expectedObjects.add(objOutcome(objParser,
                                     payload,
                                     false));
    }
    List<Outcome> expectedArrays = new ArrayList<>();
    for (String payload : arrayPayloads) {
      expectedArrays.add(arrayOutcome(arrayParser,
                                      payload,
                                      false));
    }

    ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    CountDownLatch startGate = new CountDownLatch(1);
    try {
      List<Future<Void>> futures = new ArrayList<>();
      for (int thread = 0; thread < THREADS; thread++) {
        final int threadIndex = thread;
        futures.add(executor.submit(() -> {
          startGate.await();
          int cursor = threadIndex * 37 + 11;
          for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
            cursor = nextCursor(cursor);
            int objIndex = Math.floorMod(cursor,
                                         objectPayloads.size());
            Outcome expectedObj = expectedObjects.get(objIndex);
            Outcome fromObjBytes = objOutcome(objParser,
                                              objectPayloads.get(objIndex),
                                              false);
            Outcome fromObjStream = objOutcome(objParser,
                                               objectPayloads.get(objIndex),
                                               true);
            Assertions.assertEquals(expectedObj,
                                    fromObjBytes);
            Assertions.assertEquals(expectedObj,
                                    fromObjStream);

            cursor = nextCursor(cursor);
            int arrIndex = Math.floorMod(cursor,
                                         arrayPayloads.size());
            Outcome expectedArr = expectedArrays.get(arrIndex);
            Outcome fromArrBytes = arrayOutcome(arrayParser,
                                               arrayPayloads.get(arrIndex),
                                               false);
            Outcome fromArrStream = arrayOutcome(arrayParser,
                                                arrayPayloads.get(arrIndex),
                                                true);
            Assertions.assertEquals(expectedArr,
                                    fromArrBytes);
            Assertions.assertEquals(expectedArr,
                                    fromArrStream);
          }
          return null;
        }));
      }
      startGate.countDown();
      waitAll(futures);
    } finally {
      executor.shutdownNow();
    }
  }

  @Test
  public void shouldUseSharedJsIoInstanceConcurrentlyWithoutCrossThreadLeaks() throws Exception {
    List<String> objPayloads = List.of("{\"a\":1,\"b\":\"x\"}",
                                       "{\"a\":-1,\"b\":\"y\"}",
                                       "{\"a\":9223372036854775807,\"b\":\"z\"}");
    List<String> arrPayloads = List.of("[1,2,3]",
                                       "[0,-1,9223372036854775807]",
                                       "[10,20,30,40]");

    List<Outcome> expectedObj = new ArrayList<>();
    for (String json : objPayloads) {
      expectedObj.add(ioObjOutcome(json,
                                   false));
    }
    List<Outcome> expectedArr = new ArrayList<>();
    for (String json : arrPayloads) {
      expectedArr.add(ioArrayOutcome(json,
                                     false));
    }

    ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    CountDownLatch startGate = new CountDownLatch(1);
    try {
      List<Future<Void>> futures = new ArrayList<>();
      for (int thread = 0; thread < THREADS; thread++) {
        final int threadIndex = thread;
        futures.add(executor.submit(() -> {
          startGate.await();
          int cursor = threadIndex * 101 + 7;
          for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
            cursor = nextCursor(cursor);
            int objIndex = Math.floorMod(cursor,
                                         objPayloads.size());
            Assertions.assertEquals(expectedObj.get(objIndex),
                                    ioObjOutcome(objPayloads.get(objIndex),
                                                 false));
            Assertions.assertEquals(expectedObj.get(objIndex),
                                    ioObjOutcome(objPayloads.get(objIndex),
                                                 true));

            cursor = nextCursor(cursor);
            int arrIndex = Math.floorMod(cursor,
                                         arrPayloads.size());
            Assertions.assertEquals(expectedArr.get(arrIndex),
                                    ioArrayOutcome(arrPayloads.get(arrIndex),
                                                   false));
            Assertions.assertEquals(expectedArr.get(arrIndex),
                                    ioArrayOutcome(arrPayloads.get(arrIndex),
                                                   true));
          }
          return null;
        }));
      }
      startGate.countDown();
      waitAll(futures);
    } finally {
      executor.shutdownNow();
    }
  }

  @Test
  public void shouldParseWithSharedStaticSpecsConcurrently() throws Exception {
    JsSpec intSpec = JsSpecs.integer();
    JsSpec longSpec = JsSpecs.longInteger();
    JsSpec doubleSpec = JsSpecs.doubleNumber();
    JsSpec decimalSpec = JsSpecs.decimal();

    List<SpecWorkload> workloads = List.of(new SpecWorkload("int",
                                                             intSpec,
                                                             List.of("0",
                                                                     "1",
                                                                     "-1",
                                                                     "2147483647",
                                                                     "2147483648",
                                                                     "01",
                                                                     "+1")),
                                           new SpecWorkload("long",
                                                             longSpec,
                                                             List.of("0",
                                                                     "1",
                                                                     "-1",
                                                                     "9223372036854775807",
                                                                     "9223372036854775808",
                                                                     "-9223372036854775809")),
                                           new SpecWorkload("double",
                                                             doubleSpec,
                                                             List.of("0.0",
                                                                     "1e-100",
                                                                     "1.7976931348623157e308",
                                                                     "1.",
                                                                     ".1",
                                                                     "+1")),
                                           new SpecWorkload("decimal",
                                                             decimalSpec,
                                                             List.of("0",
                                                                     "1.25",
                                                                     "1e2",
                                                                     "1e",
                                                                     "01",
                                                                     "+1")));

    for (SpecWorkload workload : workloads) {
      assertSharedSpecConcurrentConsistency(workload);
    }
  }

  private static void assertConcurrentConsistency(ParserWorkload workload) throws Exception {
    List<Outcome> expected = new ArrayList<>();
    for (String number : workload.numbers) {
      expected.add(objOutcome(workload.parser,
                              "{\"x\":" + number + "}",
                              false));
    }

    ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    CountDownLatch startGate = new CountDownLatch(1);
    try {
      List<Callable<Void>> tasks = new ArrayList<>();
      for (int thread = 0; thread < THREADS; thread++) {
        final int threadIndex = thread;
        tasks.add(() -> {
          startGate.await();
          int cursor = threadIndex * 17 + 3;
          for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
            cursor = nextCursor(cursor);
            int index = Math.floorMod(cursor,
                                      workload.numbers.size());
            String json = "{\"x\":" + workload.numbers.get(index) + "}";
            Outcome expectedOutcome = expected.get(index);
            Outcome bytesOutcome = objOutcome(workload.parser,
                                              json,
                                              false);
            Outcome streamOutcome = objOutcome(workload.parser,
                                               json,
                                               true);

            Assertions.assertEquals(expectedOutcome,
                                    bytesOutcome,
                                    () -> mismatchMessage(workload.name,
                                                          workload.numbers.get(index),
                                                          "bytes",
                                                          expectedOutcome,
                                                          bytesOutcome));
            Assertions.assertEquals(expectedOutcome,
                                    streamOutcome,
                                    () -> mismatchMessage(workload.name,
                                                          workload.numbers.get(index),
                                                          "stream",
                                                          expectedOutcome,
                                                          streamOutcome));
          }
          return null;
        });
      }
      List<Future<Void>> futures = new ArrayList<>();
      for (Callable<Void> task : tasks) {
        futures.add(executor.submit(task));
      }
      startGate.countDown();
      waitAll(futures);
    } finally {
      executor.shutdownNow();
    }
  }

  private static void assertSharedSpecConcurrentConsistency(SpecWorkload workload) throws Exception {
    List<Outcome> expected = new ArrayList<>();
    for (String value : workload.values) {
      expected.add(specOutcome(workload.spec,
                               value));
    }

    ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    CountDownLatch startGate = new CountDownLatch(1);
    try {
      List<Future<Void>> futures = new ArrayList<>();
      for (int thread = 0; thread < THREADS; thread++) {
        final int threadIndex = thread;
        futures.add(executor.submit(() -> {
          startGate.await();
          int cursor = threadIndex * 31 + 5;
          for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
            cursor = nextCursor(cursor);
            int index = Math.floorMod(cursor,
                                      workload.values.size());
            Outcome actual = specOutcome(workload.spec,
                                         workload.values.get(index));
            Assertions.assertEquals(expected.get(index),
                                    actual,
                                    () -> "Mismatch for shared spec `%s` value `%s`".formatted(workload.name,
                                                                                                workload.values.get(index)));
          }
          return null;
        }));
      }
      startGate.countDown();
      waitAll(futures);
    } finally {
      executor.shutdownNow();
    }
  }

  private static int nextCursor(int cursor) {
    return cursor * 1103515245 + 12345;
  }

  private static void waitAll(List<Future<Void>> futures) throws Exception {
    for (Future<Void> future : futures) {
      try {
        future.get(60,
                   TimeUnit.SECONDS);
      } catch (ExecutionException e) {
        Throwable cause = e.getCause();
        if (cause instanceof Exception exception) {
          throw exception;
        }
        throw new RuntimeException(cause);
      }
    }
  }

  private static String mismatchMessage(String parserName,
                                        String number,
                                        String mode,
                                        Outcome expected,
                                        Outcome actual
                                       ) {
    return "Mismatch for parser `%s` and value `%s` in `%s`. expected=%s actual=%s".formatted(parserName,
                                                                                              number,
                                                                                              mode,
                                                                                              expected,
                                                                                              actual);
  }

  private static Outcome objOutcome(JsObjSpecParser parser,
                                    String json,
                                    boolean fromStream
                                   ) {
    byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
    try {
      if (fromStream) {
        return new Outcome(true,
                           parser.parse(new ByteArrayInputStream(bytes))
                                 .toString(),
                           null);
      }
      return new Outcome(true,
                         parser.parse(bytes)
                               .toString(),
                         null);
    } catch (JsParserException e) {
      return new Outcome(false,
                         null,
                         withoutPosition(e.getMessage()));
    }
  }

  private static Outcome ioObjOutcome(String json,
                                      boolean fromStream
                                     ) {
    byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
    try {
      if (fromStream) {
        DslJsReader reader = JsIO.INSTANCE.createReader(new ByteArrayInputStream(bytes));
        try {
          reader.readNextToken();
          return new Outcome(true,
                             JsReaders.READERS.objReader.value(reader)
                                              .toString(),
                             null);
        } finally {
          reader.reset();
        }
      }
      return new Outcome(true,
                         JsIO.INSTANCE.parseToJsObj(bytes)
                                      .toString(),
                         null);
    } catch (JsParserException e) {
      return new Outcome(false,
                         null,
                         withoutPosition(e.getMessage()));
    }
  }

  private static Outcome ioArrayOutcome(String json,
                                        boolean fromStream
                                       ) {
    byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
    try {
      if (fromStream) {
        DslJsReader reader = JsIO.INSTANCE.createReader(new ByteArrayInputStream(bytes));
        try {
          reader.readNextToken();
          return new Outcome(true,
                             JsReaders.READERS.arrayOfValueReader.value(reader)
                                                       .toString(),
                             null);
        } finally {
          reader.reset();
        }
      }
      return new Outcome(true,
                         JsIO.INSTANCE.parseToJsArray(bytes)
                                      .toString(),
                         null);
    } catch (JsParserException e) {
      return new Outcome(false,
                         null,
                         withoutPosition(e.getMessage()));
    }
  }

  private static Outcome specOutcome(JsSpec spec,
                                     String value
                                    ) {
    try {
      return new Outcome(true,
                         spec.parse(value)
                             .toString(),
                         null);
    } catch (JsParserException e) {
      return new Outcome(false,
                         null,
                         withoutPosition(e.getMessage()));
    }
  }

  private static Outcome arrayOutcome(JsArraySpecParser parser,
                                      String json,
                                      boolean fromStream
                                     ) {
    byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
    try {
      if (fromStream) {
        return new Outcome(true,
                           parser.parse(new ByteArrayInputStream(bytes))
                                 .toString(),
                           null);
      }
      return new Outcome(true,
                         parser.parse(bytes)
                               .toString(),
                         null);
    } catch (JsParserException e) {
      return new Outcome(false,
                         null,
                         withoutPosition(e.getMessage()));
    }
  }

  private static String withoutPosition(String message) {
    return message.replaceAll("\\. Current parser position is \\d+$",
                              "");
  }

  private record Outcome(boolean ok,
                         String value,
                         String error) {
  }

  private record ParserWorkload(String name,
                                JsObjSpecParser parser,
                                List<String> numbers) {
  }

  private record SpecWorkload(String name,
                              JsSpec spec,
                              List<String> values) {
  }
}
