package jsonvalues.spec;

import static jsonvalues.spec.JsSpecs.oneSpecOf;

import fun.gen.Combinators;
import fun.gen.Gen;
import java.util.List;
import jsonvalues.JsStr;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsSpecToSchemaTests {


  @Test
  public void test() {
    JsObjSpec objSpec = JsObjSpec.of("name",
                                     JsSpecs.str(),
                                     "age",
                                     JsSpecs.integer(),
                                     "address",
                                     JsObjSpec.of("street",
                                                  JsSpecs.str(),
                                                  "city",
                                                  JsSpecs.str(),
                                                  "zip",
                                                  JsSpecs.integer()
                                                 ),
                                     "vip",
                                     JsSpecs.bool(),
                                     "height",
                                     JsSpecs.decimal(),
                                     "distance",
                                     JsSpecs.longInteger(),
                                     "image",
                                     JsSpecs.binary(),
                                     "birthDate",
                                     JsSpecs.instant()
                                    );

    System.out.println(SpecToSchema.toJsSchema(objSpec)
                                   .toPrettyString());

  }

  @Test
  public void testNamedSpec() {

    String NAME_FIELD = "name";
    String TYPE_FIELD = "type";
    String BUTTON_COUNT_FIELD = "buttonCount";
    String WHEEL_COUNT_FIELD = "wheelCount";
    String TRACKING_TYPE_FIELD = "trackingType";
    String KEY_COUNT_FIELD = "keyCount";
    String MEDIA_BUTTONS_FIELD = "mediaButtons";
    String CONNECTED_DEVICES_FIELD = "connectedDevices";
    String PERIPHERAL_FIELD = "peripheral";
    List<String> TRACKING_TYPE_ENUM = List.of("ball",
                                              "optical");

    var baseSpec =
        JsObjSpec.of(NAME_FIELD,
                     JsSpecs.str(),
                     TYPE_FIELD,
                     JsSpecs.oneStringOf("mouse",
                                         "keyboard",
                                         "usb_hub"));

    var mouseSpec =
        JsObjSpec.of(BUTTON_COUNT_FIELD,
                     JsSpecs.integer(),
                     WHEEL_COUNT_FIELD,
                     JsSpecs.integer(),
                     TRACKING_TYPE_FIELD,
                     JsSpecs.oneStringOf(TRACKING_TYPE_ENUM)
                    )
                 .concat(baseSpec);

    var keyboardSpec =
        JsObjSpec.of(KEY_COUNT_FIELD,
                     JsSpecs.integer(),
                     MEDIA_BUTTONS_FIELD,
                     JsSpecs.bool()
                    )
                 .concat(baseSpec);

    var usbHubSpec =
        JsObjSpec.of(CONNECTED_DEVICES_FIELD,
                     JsSpecs.arrayOfSpec(JsSpecs.ofNamedSpec(PERIPHERAL_FIELD))
                    )
                 .withOptKeys(CONNECTED_DEVICES_FIELD)
                 .concat(baseSpec);

    var peripheralSpec =
        JsSpecs.ofNamedSpec(PERIPHERAL_FIELD,
                            oneSpecOf(mouseSpec,
                                      keyboardSpec,
                                      usbHubSpec));


/*    System.out.println(JsObjSpecToSchema.convert(peripheralSpec)
                                        .toPrettyString());*/

    System.out.println(SpecToSchema.toJsSchema(usbHubSpec)
                                   .toPrettyString());


  }

  @Test
  public void testStrSchema() {

    StrSchema strSchema = new StrSchema().setMinLength(1)
                                         .setMaxLength(2)
                                         .setFormat("email")
                                         .setPattern(".*");
    var spec = JsObjSpec.of("a",
                            JsSpecs.str(strSchema),
                            "b",
                            JsSpecs.arrayOfStr(strSchema),
                            "c",
                            JsSpecs.mapOfStr(strSchema)
                           );

    System.out.println(SpecToSchema.toJsSchema(spec)
                                   .toPrettyString());
  }

  @Test
  public void testArrays() {
    JsObjSpec objSpec = JsObjSpec.of("a",
                                     JsSpecs.arrayOfStr(),
                                     "b",
                                     JsSpecs.arrayOfInt(),
                                     "d",
                                     JsSpecs.arrayOfDec(),
                                     "e",
                                     JsSpecs.arrayOfLong(),
                                     "g",
                                     JsSpecs.arrayOfDouble(),
                                     "h",
                                     JsSpecs.arrayOfBigInt(),
                                     "c",
                                     JsSpecs.arrayOfBool(),
                                     "f",
                                     JsSpecs.arrayOfObj()
                                    );

    System.out.println(SpecToSchema.toJsSchema(objSpec)
                                   .toPrettyString());

  }

  @Test
  public void testStringConstraints() {

    StrSchema strSchema = new StrSchema()
        .setMinLength(3)
        .setMaxLength(5)
        .setFormat("digits")
        .setPattern("\\[a-z]+");
    JsSpec strSpec = JsSpecs.str(strSchema
                                );
    JsObjSpec objSpec = JsObjSpec.of("a",
                                     strSpec
                                    );
    Gen<JsStr> strGen = Combinators.oneOf(JsStrGen.alphabetic(0,
                                                              2),
                                          JsStrGen.alphabetic(6,
                                                              10),
                                          JsStrGen.digits(3,
                                                          5)
                                         );
    JsObjGen gen = JsObjGen.of("a",
                               strGen
                              );

    var parser = JsObjSpecParser.of(objSpec);

    gen.sample(100)
       .forEach(obj -> {
         Assertions.assertThrows(JsParserException.class,
                                 () -> {
                                   try {
                                     parser.parse(obj.toString());
                                   } catch (Exception e) {
                                     System.out.println(obj);
                                     System.out.println(e.getMessage());
                                     throw e;
                                   }
                                 }
                                );
       });
  }

  @Test
  public void testArrayOfStringConstraints() {

    StrSchema strSchema = new StrSchema()
        .setMinLength(3)
        .setMaxLength(5)
        .setFormat("digits")
        .setPattern("\\[a-z]+");

    JsObjSpec objSpec = JsObjSpec.of("b",
                                     JsSpecs.arrayOfStr(strSchema)
                                    );
    Gen<JsStr> strGen = Combinators.oneOf(JsStrGen.alphabetic(0,
                                                              2),
                                          JsStrGen.alphabetic(6,
                                                              10),
                                          JsStrGen.digits(3,
                                                          5)
                                         );
    JsObjGen gen = JsObjGen.of(
        "b",
        JsArrayGen.biased(strGen,
                          1,
                          10)
                              );

    var parser = JsObjSpecParser.of(objSpec);

    gen.sample(100)
       .forEach(obj -> {
         Assertions.assertThrows(JsParserException.class,
                                 () -> {
                                   try {
                                     parser.parse(obj.toString());
                                   } catch (Exception e) {
                                     System.out.println(obj);
                                     System.out.println(e.getMessage());
                                     throw e;
                                   }
                                 }
                                );
       });
  }
}
