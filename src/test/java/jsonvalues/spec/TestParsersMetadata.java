package jsonvalues.spec;

import static jsonvalues.spec.JsSpecs.str;

import java.util.List;
import java.util.Map;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestParsersMetadata {

  @Test
  public void testAliasAndDefaults() {

    String phoneNumber = "phoneNumber";
    JsObjSpec personSpec =
        JsObjSpecBuilder.withName("personSpec")
                        .withFieldAliases(Map.of("name",
                                                 List.of("first_name"),
                                                 "surname",
                                                 List.of("last_name")
                                                )
                                         )
                        .withFieldsDefaults(Map.of(phoneNumber,
                                                   JsStr.of("6666666")
                                                  )
                                           )
                        .build(JsObjSpec.of("name",
                                            str(),
                                            "surname",
                                            str(),
                                            phoneNumber,
                                            str()
                                           )
                                        .withOptKeys(phoneNumber
                                                    )
                              );

    JsObjSpecParser parser = JsObjSpecParser.of(personSpec);

    String personStr = JsObj.of("first_name",
                                JsStr.of("John"),
                                "last_name",
                                JsStr.of("Doe")
                               )
                            .toString();

    JsObj person = parser.parse(personStr);

    Assertions.assertEquals(JsObj.parse("{\"name\":\"John\",\"phoneNumber\":\"6666666\",\"surname\":\"Doe\"}"),
                            person);


  }

  @Test
  public void testMaxAndMinProperties() {

    JsObjSpec personSpec =
        JsObjSpecBuilder.withName("maxminspec")
                        .withMaxProperties(3)
                        .withMinProperties(2)
                        .build(JsObjSpec.of("a",
                                            str(),
                                            "b",
                                            str(),
                                            "c",
                                            str(),
                                            "d",
                                            str()
                                           )
                                        .withAllOptKeys()
                              );

    JsObjSpecParser parser = JsObjSpecParser.of(personSpec);

    JsObj json1 = JsObj.of("a",
                           JsStr.of("John")
                          );
    String jsonStr1 = json1.toString();

    Assertions.assertThrows(JsParserException.class,
                            () -> {
                              try {
                                parser.parse(jsonStr1);
                              } catch (Exception e) {
                                System.out.println(e.getMessage());
                                throw e;
                              }
                            });

    Assertions.assertFalse(personSpec.test(json1)
                                     .isEmpty());

    JsObj json2 = JsObj.of("a",
                           JsStr.of("John"),
                           "b",
                           JsStr.of("John"),
                           "c",
                           JsStr.of("John"),
                           "d",
                           JsStr.of("John")
                          );
    String jsonStr2 = json2.toString();
    Assertions.assertThrows(JsParserException.class,
                            () -> {
                              try {
                                parser.parse(jsonStr2);
                              } catch (Exception e) {
                                System.out.println(e.getMessage());
                                throw e;
                              }
                            });
    Assertions.assertFalse(personSpec.test(json2)
                                     .isEmpty());
  }

  @Test
  public void testMaxAndMinItems() {

    JsObjSpec spec = JsObjSpecBuilder.withName("arrayitems")
                                     .build(JsObjSpec.of("a",
                                                         JsSpecs.arrayOfStr(ArraySchema.sizeBetween(1,
                                                                                                    3))
                                                        )
                                           );

    JsObjSpecParser parser = JsObjSpecParser.of(spec);

    String jsonStr1 = "{\"a\":[]}";

    Assertions.assertThrows(JsParserException.class,
                            () -> {
                              try {
                                parser.parse(jsonStr1);
                              } catch (Exception e) {
                                System.out.println(e.getMessage());
                                throw e;
                              }
                            });

    Assertions.assertFalse(spec.test(JsObj.parse(jsonStr1))
                               .isEmpty());

    String jsonStr2 = "{\"a\":[\"a\",\"b\",\"c\",\"d\"]}";

    Assertions.assertThrows(JsParserException.class,
                            () -> {
                              try {
                                parser.parse(jsonStr2);
                              } catch (Exception e) {
                                System.out.println(e.getMessage());
                                throw e;
                              }
                            });

    Assertions.assertFalse(spec.test(JsObj.parse(jsonStr2))
                               .isEmpty());
  }

}
