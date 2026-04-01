package jsonvalues.spec;

import static jsonvalues.spec.JsSpecs.oneSpecOf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jsonvalues.JsBinary;
import jsonvalues.JsNull;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BuildersValidationTest {

  private static String unique(String prefix) {
    return prefix + "_" + System.nanoTime();
  }

  @Test
  public void shouldFixedBuilderBuildsAndCachesSpec() {
    String name = unique("fixed");
    String namespace = "demo.ns";
    String alias1 = unique("alias1");
    String alias2 = unique("alias2");

    var spec = JsFixedBuilder.withName(name)
                             .withNamespace(namespace)
                             .withDoc("fixed binary")
                             .withAliases(List.of(alias1,
                                                  alias2))
                             .build(4);

    Assertions.assertTrue(spec.test(JsBinary.of(new byte[] {1, 2, 3, 4}))
                              .isEmpty());
    Assertions.assertFalse(spec.test(JsBinary.of(new byte[] {1, 2, 3}))
                               .isEmpty());

    var fixed = (JsFixedBinary) spec;
    Assertions.assertEquals("demo.ns." + name,
                            fixed.getMetaData()
                                 .getFullName());
    Assertions.assertSame(spec,
                          JsSpecCache.get("demo.ns." + name));
    Assertions.assertSame(spec,
                          JsSpecCache.get(alias1));
    Assertions.assertSame(spec,
                          JsSpecCache.get(alias2));
  }

  @Test
  public void shouldFixedBuilderValidationErrors() {
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsFixedBuilder.withName("1bad"));

    var builder = JsFixedBuilder.withName(unique("fixed"));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> builder.withNamespace("bad namespace"));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> builder.withAliases(List.of("ok.alias",
                                                              "bad alias")));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> builder.build(0));
  }

  @Test
  public void shouldObjBuilderValidatesAliasDefinitions() {
    var base = JsObjSpec.of("a",
                            JsSpecs.str(),
                            "b",
                            JsSpecs.str(),
                            "c",
                            JsSpecs.str());

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldAliases(Map.of("a",
                                                                           List.of("a")))
                                                  .build(base));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldAliases(Map.of("a",
                                                                           List.of("dup",
                                                                                   "dup")))
                                                  .build(base));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldAliases(Map.of("a",
                                                                           List.of("dup"),
                                                                           "b",
                                                                           List.of("dup")))
                                                  .build(base));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldAliases(Map.of("missing",
                                                                           List.of("alias")))
                                                  .build(base));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldAliases(Map.of("a",
                                                                           List.of("")))
                                                  .build(base));
  }

  @Test
  public void shouldObjBuilderValidatesDocsOrdersAndMinMax() {
    var base = JsObjSpec.of("a",
                            JsSpecs.str(),
                            "b",
                            JsSpecs.str(),
                            "c",
                            JsSpecs.str());

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldDocs(Map.of("missing",
                                                                        "doc"))
                                                  .build(base));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldOrders(Map.of("missing",
                                                                          JsObjSpecBuilder.ORDERS.ascending))
                                                  .build(base));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withMinProperties(-1));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withMaxProperties(-1));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withMinProperties(3)
                                                  .withMaxProperties(2)
                                                  .build(base));

    var valid = JsObjSpecBuilder.withName(unique("obj"))
                                .withFieldOrders(Map.of("a",
                                                        JsObjSpecBuilder.ORDERS.ascending,
                                                        "b",
                                                        JsObjSpecBuilder.ORDERS.descending,
                                                        "c",
                                                        JsObjSpecBuilder.ORDERS.ignore))
                                .build(base);

    Assertions.assertTrue(valid.test(jsonvalues.JsObj.of("a",
                                                          JsStr.of("x"),
                                                          "b",
                                                          JsStr.of("y"),
                                                          "c",
                                                          JsStr.of("z")))
                               .isEmpty());
  }

  @Test
  public void shouldObjBuilderValidatesDefaults() {
    var base = JsObjSpec.of("a",
                            JsSpecs.str(),
                            "b",
                            oneSpecOf(JsSpecs.str(),
                                      JsSpecs.integer()),
                            "ref",
                            JsSpecs.ofNamedSpec(unique("named")));

    Map<String, jsonvalues.JsValue> withNull = new HashMap<>();
    withNull.put("a",
                 null);
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldsDefaults(withNull)
                                                  .build(base));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldsDefaults(Map.of("missing",
                                                                             JsStr.of("x")))
                                                  .build(base));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldsDefaults(Map.of("a",
                                                                             JsNull.NULL))
                                                  .build(base));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldsDefaults(Map.of("b",
                                                                             jsonvalues.JsInt.of(1)))
                                                  .build(base));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldsDefaults(Map.of("ref",
                                                                             JsStr.of("x")))
                                                  .build(base));

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecBuilder.withName(unique("obj"))
                                                  .withFieldsDefaults(Map.of("ref",
                                                                             JsNull.NULL))
                                                  .build(base));
  }

  @Test
  public void shouldSerializerExceptionConstructors() {
    var oneArg = new JsSerializerException("reason");
    Assertions.assertEquals("reason",
                            oneArg.getMessage());

    var cause = new RuntimeException("boom");
    var twoArgs = new JsSerializerException("reason",
                                            cause);
    Assertions.assertEquals("reason",
                            twoArgs.getMessage());
    Assertions.assertSame(cause,
                          twoArgs.getCause());
  }
}
