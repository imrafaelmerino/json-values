package jsonvalues.spec;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsInt;
import jsonvalues.JsNull;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CriticalParsersAndCacheMutationTest {

  private static String unique(String prefix) {
    return prefix + "_" + System.nanoTime();
  }

  private static DslJsReader readerOf(String value) {
    DslJsReader reader = JsIO.INSTANCE.newReader(value.getBytes(StandardCharsets.UTF_8));
    reader.readNextToken();
    return reader;
  }

  @Test
  public void shouldCacheAndRejectDuplicatesForNamedSpecs() {
    String name = unique("cache_named");
    JsSpec spec = JsSpecs.integer();
    JsSpecCache.put(name,
                    spec);
    Assertions.assertSame(spec,
                          JsSpecCache.get(name));

    IllegalArgumentException duplicate =
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> JsSpecCache.put(name,
                                                      JsSpecs.longInteger()));
    Assertions.assertTrue(duplicate.getMessage()
                                   .contains("already been created"));
  }

  @Test
  public void shouldCacheAliasesAndRejectAliasCollisions() {
    String name = unique("cache_putall");
    String alias1 = unique("alias1");
    String alias2 = unique("alias2");
    JsSpec spec = JsSpecs.str();

    JsSpecCache.putAll(name,
                       List.of(alias1,
                               alias2),
                       spec);
    Assertions.assertSame(spec,
                          JsSpecCache.get(name));
    Assertions.assertSame(spec,
                          JsSpecCache.get(alias1));
    Assertions.assertSame(spec,
                          JsSpecCache.get(alias2));

    String another = unique("cache_putall_2");
    IllegalArgumentException aliasCollision =
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> JsSpecCache.putAll(another,
                                                         List.of(alias1),
                                                         JsSpecs.integer()));
    Assertions.assertTrue(aliasCollision.getMessage()
                                       .contains("already been created"));

    String duplicatePrimary = unique("cache_putall_primary");
    JsSpecCache.putAll(duplicatePrimary,
                       null,
                       JsSpecs.integer());
    IllegalArgumentException duplicatePrimaryName =
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> JsSpecCache.putAll(duplicatePrimary,
                                                         null,
                                                         JsSpecs.longInteger()));
    Assertions.assertTrue(duplicatePrimaryName.getMessage()
                                              .contains("already been created"));
  }

  @Test
  public void shouldThrowWhenGettingMissingSpec() {
    IllegalArgumentException missing =
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> JsSpecCache.get(unique("missing")));
    Assertions.assertTrue(missing.getMessage()
                                 .contains("doesn't exist"));
  }

  @Test
  public void shouldNamedSpecParserHandleNullabilityAndDelegateToCachedSpec() throws JsParserException {
    String name = unique("named_delegate");
    JsSpecs.ofNamedSpec(name,
                        JsSpecs.integer());

    NamedSpec nonNullable = (NamedSpec) JsSpecs.ofNamedSpec(name);
    JsParserException invalidNull =
        Assertions.assertThrows(JsParserException.class,
                                () -> nonNullable.parser()
                                                 .parse(readerOf("null")));
    Assertions.assertTrue(invalidNull.getMessage()
                                     .contains(ParserErrors.INVALID_NULL));

    NamedSpec nullable = (NamedSpec) nonNullable.nullable();
    Assertions.assertEquals(JsNull.NULL,
                            nullable.parser()
                                    .parse(readerOf("null")));
    Assertions.assertEquals(JsInt.of(7),
                            nullable.parser()
                                    .parse(readerOf("7")));
  }

  @Test
  public void shouldNamedSpecTestRespectNullabilityAndDelegateValidationErrors() {
    String name = unique("named_test_delegate");
    JsSpecs.ofNamedSpec(name,
                        JsSpecs.integer());

    NamedSpec nonNullable = (NamedSpec) JsSpecs.ofNamedSpec(name);
    Assertions.assertFalse(nonNullable.test(JsPath.empty(),
                                            JsNull.NULL)
                                      .isEmpty());
    Assertions.assertFalse(nonNullable.test(JsPath.empty(),
                                            JsStr.of("x"))
                                      .isEmpty());
    Assertions.assertTrue(nonNullable.test(JsPath.empty(),
                                           JsInt.of(1))
                                     .isEmpty());

    NamedSpec nullable = (NamedSpec) nonNullable.nullable();
    Assertions.assertTrue(nullable.test(JsPath.empty(),
                                        JsNull.NULL)
                                  .isEmpty());
    Assertions.assertFalse(nullable.test(JsPath.empty(),
                                         JsStr.of("x"))
                                   .isEmpty());
  }

  @Test
  public void shouldObjAndArraySpecParsersValidateAcceptedAndRejectedSpecTypes() {
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecParser.of(JsSpecs.integer()));
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsArraySpecParser.of(JsSpecs.integer()));

    JsSpec objectOneOf = JsSpecs.oneSpecOf(JsObjSpec.of("a",
                                                        JsSpecs.integer()),
                                           JsObjSpec.of("b",
                                                        JsSpecs.str()));
    JsSpec arrayOneOf = JsSpecs.oneSpecOf(JsSpecs.arrayOfInt(),
                                          JsSpecs.arrayOfStr());

    Assertions.assertDoesNotThrow(() -> JsObjSpecParser.of(objectOneOf));
    Assertions.assertDoesNotThrow(() -> JsArraySpecParser.of(arrayOneOf));

    JsSpec mixedObjOneOf = JsSpecs.oneSpecOf(JsObjSpec.of("a",
                                                          JsSpecs.integer()),
                                             JsSpecs.integer());
    JsSpec mixedArrayOneOf = JsSpecs.oneSpecOf(JsSpecs.arrayOfInt(),
                                               JsSpecs.integer());

    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecParser.of(mixedObjOneOf));
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsArraySpecParser.of(mixedArrayOneOf));
  }

  @Test
  public void shouldObjAndArraySpecParsersRejectNullInputsOnParseApis() {
    JsObjSpecParser objParser = JsObjSpecParser.of(JsObjSpec.of("a",
                                                                JsSpecs.integer()));
    JsArraySpecParser arrayParser = JsArraySpecParser.of(JsSpecs.arrayOfInt());

    Assertions.assertThrows(NullPointerException.class,
                            () -> objParser.parse((byte[]) null));
    Assertions.assertThrows(NullPointerException.class,
                            () -> objParser.parse((String) null));
    Assertions.assertThrows(NullPointerException.class,
                            () -> objParser.parse((ByteArrayInputStream) null));

    Assertions.assertThrows(NullPointerException.class,
                            () -> arrayParser.parse((byte[]) null));
    Assertions.assertThrows(NullPointerException.class,
                            () -> arrayParser.parse((String) null));
    Assertions.assertThrows(NullPointerException.class,
                            () -> arrayParser.parse((ByteArrayInputStream) null));
  }

  @Test
  public void shouldObjSpecParserParseFromBytesStringAndStream() {
    JsObjSpecParser parser = JsObjSpecParser.of(JsObjSpec.of("a",
                                                             JsSpecs.integer()));
    Assertions.assertNotNull(parser);
    Assertions.assertEquals(JsObj.of("a",
                                     JsInt.of(1)),
                            parser.parse("{\"a\":1}".getBytes(StandardCharsets.UTF_8)));
    Assertions.assertEquals(JsObj.of("a",
                                     JsInt.of(2)),
                            parser.parse("{\"a\":2}"));
    Assertions.assertEquals(JsObj.of("a",
                                     JsInt.of(3)),
                            parser.parse(new ByteArrayInputStream("{\"a\":3}".getBytes(StandardCharsets.UTF_8))));
  }

  @Test
  public void shouldArraySpecParserParseFromBytesStringAndStream() {
    JsArraySpecParser parser = JsArraySpecParser.of(JsSpecs.arrayOfInt());
    Assertions.assertNotNull(parser);
    Assertions.assertEquals(JsArray.of(1,
                                       2),
                            parser.parse("[1,2]".getBytes(StandardCharsets.UTF_8)));
    Assertions.assertEquals(JsArray.of(3,
                                       4),
                            parser.parse("[3,4]"));
    Assertions.assertEquals(JsArray.of(5,
                                       6),
                            parser.parse(new ByteArrayInputStream("[5,6]".getBytes(StandardCharsets.UTF_8))));
  }

  @Test
  public void shouldObjAndArraySpecParserFactoriesAcceptNamedSpecs() {
    String objName = unique("named_obj_parser");
    JsSpecs.ofNamedSpec(objName,
                        JsObjSpec.of("a",
                                     JsSpecs.integer()));
    JsObjSpecParser objParser = JsObjSpecParser.of(JsSpecs.ofNamedSpec(objName));
    Assertions.assertNotNull(objParser);
    Assertions.assertEquals(JsObj.of("a",
                                     JsInt.of(9)),
                            objParser.parse("{\"a\":9}"));

    String arrName = unique("named_arr_parser");
    JsSpecs.ofNamedSpec(arrName,
                        JsSpecs.arrayOfInt());
    JsArraySpecParser arrParser = JsArraySpecParser.of(JsSpecs.ofNamedSpec(arrName));
    Assertions.assertNotNull(arrParser);
    Assertions.assertEquals(JsArray.of(7,
                                       8),
                            arrParser.parse("[7,8]"));

    String badObjName = unique("named_bad_obj_parser");
    JsSpecs.ofNamedSpec(badObjName,
                        JsSpecs.integer());
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsObjSpecParser.of(JsSpecs.ofNamedSpec(badObjName)));

    String badArrName = unique("named_bad_arr_parser");
    JsSpecs.ofNamedSpec(badArrName,
                        JsSpecs.integer());
    Assertions.assertThrows(IllegalArgumentException.class,
                            () -> JsArraySpecParser.of(JsSpecs.ofNamedSpec(badArrName)));
  }
}
