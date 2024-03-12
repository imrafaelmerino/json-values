package jsonvalues.api;

import static jsonvalues.spec.JsSpecs.oneSpecOf;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.NamedGen;
import java.util.List;
import jsonvalues.JsStr;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsBoolGen;
import jsonvalues.gen.JsIntGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsSpecs;
import jsonvalues.spec.SpecToJsonSchema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public final class ModelingInheritance {

  String NAME_FIELD = "name";
  String TYPE_FIELD = "type";
  String BUTTON_COUNT_FIELD = "buttonCount";
  String WHEEL_COUNT_FIELD = "wheelCount";
  String TRACKING_TYPE_FIELD = "trackingType";
  String KEY_COUNT_FIELD = "keyCount";
  String MEDIA_BUTTONS_FIELD = "mediaButtons";
  String CONNECTED_DEVICES_FIELD = "connectedDevices";
  String PERIPHERAL_FIELD = "any_peripheral";
  List<String> TRACKING_TYPE_ENUM = List.of("ball",
                                            "optical");

  @Test
  public void test() {

    var baseSpec = JsObjSpec.of(NAME_FIELD,
                                JsSpecs.str());

    var baseGen = JsObjGen.of(NAME_FIELD,
                              JsStrGen.alphabetic()
                                      .distinct());

    var mouseSpec =
        JsObjSpec.of(TYPE_FIELD,
                     JsSpecs.cons(JsStr.of("mouse")),
                     BUTTON_COUNT_FIELD,
                     JsSpecs.integer(),
                     WHEEL_COUNT_FIELD,
                     JsSpecs.integer(),
                     TRACKING_TYPE_FIELD,
                     JsSpecs.oneStringOf(TRACKING_TYPE_ENUM)
                    )
                 .concat(baseSpec);

    var mouseGen =
        JsObjGen.of(BUTTON_COUNT_FIELD,
                    JsIntGen.arbitrary(0,
                                       10),
                    WHEEL_COUNT_FIELD,
                    JsIntGen.arbitrary(0,
                                       10),
                    TRACKING_TYPE_FIELD,
                    Combinators.oneOf(TRACKING_TYPE_ENUM)
                               .map(JsStr::of),
                    TYPE_FIELD,
                    Gen.cons(JsStr.of("mouse"))
                   )
                .concat(baseGen);

    var keyboardSpec =
        JsObjSpec.of(TYPE_FIELD,
                     JsSpecs.cons(JsStr.of("keyboard")),
                     KEY_COUNT_FIELD,
                     JsSpecs.integer(),
                     MEDIA_BUTTONS_FIELD,
                     JsSpecs.bool()
                    )
                 .concat(baseSpec);

    var keyboardGen =
        JsObjGen.of(KEY_COUNT_FIELD,
                    JsIntGen.arbitrary(0,
                                       10),
                    MEDIA_BUTTONS_FIELD,
                    JsBoolGen.arbitrary(),
                    TYPE_FIELD,
                    Gen.cons(JsStr.of("keyboard"))
                   )
                .concat(baseGen);

    var usbHubSpec =
        JsObjSpec.of(TYPE_FIELD,
                     JsSpecs.cons(JsStr.of("usb_hub")),
                     CONNECTED_DEVICES_FIELD,
                     JsSpecs.arrayOfSpec(JsSpecs.ofNamedSpec(PERIPHERAL_FIELD))
                            .nullable()
                    )
                 .withOptKeys(CONNECTED_DEVICES_FIELD)
                 .concat(baseSpec);

    var usbHubGen =
        JsObjGen.of(CONNECTED_DEVICES_FIELD,
                    JsArrayGen.biased(NamedGen.of(PERIPHERAL_FIELD),
                                      2,
                                      10),
                    TYPE_FIELD,
                    Gen.cons(JsStr.of("usb_hub"))
                   )
                .withNullValues(CONNECTED_DEVICES_FIELD)
                .withOptKeys(CONNECTED_DEVICES_FIELD)
                .concat(baseGen);

    var peripheralSpec =
        JsSpecs.ofNamedSpec(PERIPHERAL_FIELD,
                            oneSpecOf(JsSpecs.ofNamedSpec("mouse_spec",
                                                          mouseSpec),
                                      JsSpecs.ofNamedSpec("keyboard_spec",
                                                          keyboardSpec),
                                      JsSpecs.ofNamedSpec("usb_hub_spec",
                                                          usbHubSpec)));

    var peripheralGen =
        NamedGen.of(PERIPHERAL_FIELD,
                    Combinators.oneOf(mouseGen,
                                      keyboardGen,
                                      usbHubGen));

    var parser = JsObjSpecParser.of(peripheralSpec);

    var schema = SpecToJsonSchema.convert(peripheralSpec);

    System.out.println(schema);

    peripheralGen.sample(150)
                 .forEach(obj -> {

                            Assertions.assertEquals(obj,
                                                    parser.parse(obj.toString())
                                                   );

                            Assertions.assertTrue(peripheralSpec.test(obj)
                                                                .isEmpty());
                          }
                         );
  }

}