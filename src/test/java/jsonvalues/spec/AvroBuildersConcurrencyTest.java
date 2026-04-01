package jsonvalues.spec;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import jsonvalues.JsBinary;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("stress")
public class AvroBuildersConcurrencyTest {

  private static final int THREADS = 8;
  private static final int PER_THREAD = 20;

  @Test
  public void shouldConcurrentlyRegisterUniqueNamedObjSpecsInCache() throws Exception {
    JsObjSpec base = JsObjSpec.of("id",
                                  JsSpecs.integer(),
                                  "name",
                                  JsSpecs.str());

    ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    CountDownLatch startGate = new CountDownLatch(1);
    try {
      List<Future<List<String>>> futures = new ArrayList<>();
      for (int t = 0; t < THREADS; t++) {
        final int thread = t;
        futures.add(executor.submit(() -> {
          startGate.await();
          List<String> fullNames = new ArrayList<>();
          for (int i = 0; i < PER_THREAD; i++) {
            String specName = "obj_" + thread + "_" + i + "_" + System.nanoTime();
            String namespace = "concurrency.avro.obj";
            String fullName = namespace + "." + specName;

            JsObjSpec built = JsObjSpecBuilder.withName(specName)
                                              .withNamespace(namespace)
                                              .build(base);
            Assertions.assertSame(built,
                                  JsSpecCache.get(fullName));
            Assertions.assertTrue(built.test(JsObj.of("id",
                                                      jsonvalues.JsInt.of(1),
                                                      "name",
                                                      JsStr.of("ok")))
                                      .isEmpty());

            // Ensure named spec indirection resolves from static cache under contention.
            JsSpec named = JsSpecs.ofNamedSpec(fullName);
            Assertions.assertTrue(named.parse("{\"id\":1,\"name\":\"ok\"}")
                                      .equals(JsObj.of("id",
                                                       jsonvalues.JsInt.of(1),
                                                       "name",
                                                       JsStr.of("ok"))));
            fullNames.add(fullName);
          }
          return fullNames;
        }));
      }
      startGate.countDown();
      for (Future<List<String>> future : futures) {
        for (String fullName : future.get(60,
                                          TimeUnit.SECONDS)) {
          Assertions.assertNotNull(JsSpecCache.get(fullName));
        }
      }
    } finally {
      executor.shutdownNow();
    }
  }

  @Test
  public void shouldConcurrentlyBuildEnumAndFixedSpecsAndCacheThem() throws Exception {
    ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    CountDownLatch startGate = new CountDownLatch(1);
    try {
      List<Callable<Void>> tasks = new ArrayList<>();
      for (int t = 0; t < THREADS; t++) {
        final int thread = t;
        tasks.add(() -> {
          startGate.await();
          for (int i = 0; i < PER_THREAD; i++) {
            String enumName = "enum_" + thread + "_" + i + "_" + System.nanoTime();
            String fixedName = "fixed_" + thread + "_" + i + "_" + System.nanoTime();
            String namespace = "concurrency.avro.misc";
            String enumFullName = namespace + "." + enumName;
            String fixedFullName = namespace + "." + fixedName;

            JsSpec enumSpec = JsEnumBuilder.withName(enumName)
                                           .withNamespace(namespace)
                                           .withDefaultSymbol("A")
                                           .build("A",
                                                  "B",
                                                  "C");
            Assertions.assertSame(enumSpec,
                                  JsSpecCache.get(enumFullName));
            Assertions.assertTrue(enumSpec.test(JsStr.of("A"))
                                          .isEmpty());
            Assertions.assertFalse(enumSpec.test(JsStr.of("Z"))
                                           .isEmpty());

            JsSpec fixedSpec = JsFixedBuilder.withName(fixedName)
                                             .withNamespace(namespace)
                                             .build(4);
            Assertions.assertSame(fixedSpec,
                                  JsSpecCache.get(fixedFullName));
            Assertions.assertTrue(fixedSpec.test(JsBinary.of(new byte[] {1, 2, 3, 4}))
                                           .isEmpty());
            Assertions.assertFalse(fixedSpec.test(JsBinary.of(new byte[] {1, 2, 3}))
                                            .isEmpty());

            // Parse paths from named refs to exercise parser resolution from shared cache.
            Assertions.assertEquals(JsStr.of("A"),
                                    JsSpecs.ofNamedSpec(enumFullName)
                                           .parse("\"A\""));
            String base64 = Base64.getEncoder()
                                  .encodeToString(new byte[] {1, 2, 3, 4});
            Assertions.assertEquals(JsBinary.of(new byte[] {1, 2, 3, 4}),
                                    JsSpecs.ofNamedSpec(fixedFullName)
                                           .parse("\"" + base64 + "\""));
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

  @Test
  public void shouldAllowOnlyOneRegistrationForSameNameUnderConcurrency() throws Exception {
    String sharedName = "shared_named_" + System.nanoTime();
    ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    CountDownLatch startGate = new CountDownLatch(1);
    AtomicInteger success = new AtomicInteger(0);
    AtomicInteger duplicateFailures = new AtomicInteger(0);
    try {
      List<Future<Void>> futures = new ArrayList<>();
      for (int t = 0; t < THREADS; t++) {
        futures.add(executor.submit(() -> {
          startGate.await();
          try {
            JsSpecs.ofNamedSpec(sharedName,
                                JsSpecs.integer());
            success.incrementAndGet();
          } catch (IllegalArgumentException e) {
            if (e.getMessage()
                 .contains("already been created")) {
              duplicateFailures.incrementAndGet();
            } else {
              throw e;
            }
          }
          return null;
        }));
      }
      startGate.countDown();
      waitAll(futures);
    } finally {
      executor.shutdownNow();
    }

    Assertions.assertEquals(1,
                            success.get());
    Assertions.assertEquals(THREADS - 1,
                            duplicateFailures.get());
    Assertions.assertNotNull(JsSpecCache.get(sharedName));
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
}
