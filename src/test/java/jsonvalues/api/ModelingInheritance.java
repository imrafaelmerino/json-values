package jsonvalues.api;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.NamedGen;

import jsonvalues.JsStr;
import jsonvalues.gen.*;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsObjSpecParser;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static jsonvalues.spec.JsSpecs.oneSpecOf;


public class ModelingInheritance {
    String NAME_FIELD = "name";
    String TYPE_FIELD = "type";
    String BUTTON_COUNT_FIELD = "buttonCount";
    String WHEEL_COUNT_FIELD = "wheelCount";
    String TRACKING_TYPE_FIELD = "trackingType";
    String KEY_COUNT_FIELD = "keyCount";
    String MEDIA_BUTTONS_FIELD = "mediaButtons";
    String CONNECTED_DEVICES_FIELD = "connectedDevices";
    String PERIPHERAL_FIELD = "peripheral";
    List<String> TRACKING_TYPE_ENUM = List.of("ball", "optical");

    @Test
    public void test() {

        var baseSpec = JsObjSpec.of(NAME_FIELD, JsSpecs.str(),
                                    TYPE_FIELD, JsSpecs.oneStringOf("mouse", "keyboard", "usb_hub"));

        var baseGen = JsObjGen.of(NAME_FIELD, JsStrGen.alphabetic());

        var mouseSpec =
                JsObjSpec.of(BUTTON_COUNT_FIELD, JsSpecs.integer(),
                             WHEEL_COUNT_FIELD, JsSpecs.integer(),
                             TRACKING_TYPE_FIELD, JsSpecs.oneStringOf(TRACKING_TYPE_ENUM)
                            )
                         .concat(baseSpec);

        var mouseGen =
                JsObjGen.of(BUTTON_COUNT_FIELD, JsIntGen.arbitrary(0, 10),
                            WHEEL_COUNT_FIELD, JsIntGen.arbitrary(0, 10),
                            TRACKING_TYPE_FIELD, Combinators.oneOf(TRACKING_TYPE_ENUM).map(JsStr::of),
                            TYPE_FIELD, Gen.cons(JsStr.of("mouse"))
                           )
                        .concat(baseGen);

        var keyboardSpec =
                JsObjSpec.of(KEY_COUNT_FIELD, JsSpecs.integer(),
                             MEDIA_BUTTONS_FIELD, JsSpecs.bool()
                            )
                         .concat(baseSpec);

        var keyboardGen =
                JsObjGen.of(KEY_COUNT_FIELD, JsIntGen.arbitrary(0, 10),
                            MEDIA_BUTTONS_FIELD, JsBoolGen.arbitrary(),
                            TYPE_FIELD, Gen.cons(JsStr.of("keyboard"))
                           )
                        .concat(baseGen);


        var usbHubSpec =
                JsObjSpec.of(CONNECTED_DEVICES_FIELD,
                             JsSpecs.arrayOfSpec(JsSpecs.ofNamedSpec(PERIPHERAL_FIELD))
                            )
                         .withOptKeys(CONNECTED_DEVICES_FIELD)
                         .concat(baseSpec);

        var usbHubGen =
                JsObjGen.of(CONNECTED_DEVICES_FIELD,
                            JsArrayGen.biased(NamedGen.of(PERIPHERAL_FIELD), 2, 10),
                            TYPE_FIELD, Gen.cons(JsStr.of("usb_hub"))
                           )
                        .withOptKeys(CONNECTED_DEVICES_FIELD)
                        .concat(baseGen);


        var peripheralSpec =
                JsSpecs.ofNamedSpec(PERIPHERAL_FIELD,
                                    oneSpecOf(mouseSpec, keyboardSpec, usbHubSpec));

        var peripheralGen =
                NamedGen.of(PERIPHERAL_FIELD,
                            Combinators.oneOf(mouseGen, keyboardGen, usbHubGen));


        var parser = JsObjSpecParser.of(peripheralSpec);


        peripheralGen.sample(10).peek(System.out::println)
                     .forEach(obj -> {
                                  System.out.println(obj);
                                  System.out.println(obj.getStr(TYPE_FIELD));

                                  Assertions.assertEquals(obj,
                                                          parser.parse(obj.toString())
                                                         );

                                  Assertions.assertTrue(peripheralSpec.test(obj).isEmpty());
                              }
                             );


    }



}