package jsonvalues.spec;

import static jsonvalues.spec.JsSpecs.oneSpecOf;

import fun.gen.Combinators;
import fun.gen.Gen;
import java.util.List;
import java.util.Map;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.gen.JsArrayGen;
import jsonvalues.gen.JsObjGen;
import jsonvalues.gen.JsStrGen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsSpecToJsonSchemaTests {


  @Test
  public void test() {
    JsObjSpec objSpec =
        JsObjSpecBuilder.withName("person_with_embedded_address")
                        .build(JsObjSpec.of("name",
                                            JsSpecs.str(StrSchema.withLength(3,
                                                                             10)
                                                                 .setPattern("[a-z]+")
                                                                 .setFormat("email")
                                                       ),
                                            "age",
                                            JsSpecs.integer(IntegerSchema.between(0,
                                                                                  100)
                                                           ),
                                            "address",
                                            JsObjSpec.of("street",
                                                         JsSpecs.str(StrSchema.withLength(5,
                                                                                          10)
                                                                              .setPattern("[a-z]+")
                                                                              .setFormat("email")),
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
                                           )
                              );

    Assertions.assertEquals("""
                                {
                                  "$schema": "https://json-schema.org/draft/2019-09/schema",
                                  "properties": {
                                    "height": {
                                      "type": "number"
                                    },
                                    "name": {
                                      "maxLength": 10,
                                      "pattern": "[a-z]+",
                                      "format": "email",
                                      "minLength": 3,
                                      "type": "string"
                                    },
                                    "birthDate": {
                                      "format": "date-time",
                                      "type": "string"
                                    },
                                    "address": {
                                      "properties": {
                                        "zip": {
                                          "type": "integer"
                                        },
                                        "street": {
                                          "maxLength": 10,
                                          "pattern": "[a-z]+",
                                          "format": "email",
                                          "minLength": 5,
                                          "type": "string"
                                        },
                                        "city": {
                                          "type": "string"
                                        }
                                      },
                                      "additionalProperties": false,
                                      "type": "object",
                                      "required": [
                                        "street",
                                        "city",
                                        "zip"
                                      ]
                                    },
                                    "distance": {
                                      "type": "integer"
                                    },
                                    "image": {
                                      "contentEncoding": "base64",
                                      "type": "string"
                                    },
                                    "vip": {
                                      "type": "boolean"
                                    },
                                    "age": {
                                      "maximum": 100,
                                      "minimum": 0,
                                      "type": "integer"
                                    }
                                  },
                                  "additionalProperties": false,
                                  "type": "object",
                                  "$id": "person_with_embedded_address",
                                  "required": [
                                    "name",
                                    "age",
                                    "address",
                                    "vip",
                                    "height",
                                    "distance",
                                    "image",
                                    "birthDate"
                                  ]
                                }""",
                            SpecToJsonSchema.convert(objSpec)
                                            .toPrettyString());

  }

  @Test
  public void testRecursiveOneOfSpec() {
    String NAME_FIELD = "name";
    String TYPE_FIELD = "type";
    String BUTTON_COUNT_FIELD = "buttonCount";
    String WHEEL_COUNT_FIELD = "wheelCount";
    String TRACKING_TYPE_FIELD = "trackingType";
    String KEY_COUNT_FIELD = "keyCount";
    String MEDIA_BUTTONS_FIELD = "mediaButtons";
    String CONNECTED_DEVICES_FIELD = "connectedDevices";
    String PERIPHERAL_FIELD = "peripheral_device";
    JsObjSpec baseSpec =
        JsObjSpec.of(NAME_FIELD,
                     JsSpecs.str(),
                     TYPE_FIELD,
                     JsEnumBuilder.withName("type")
                                  .build("mouse",
                                         "keyboard",
                                         "usb_hub"));

    JsObjSpec mouseSpec =
        JsObjSpecBuilder.withName("mouse_device")
                        .build(JsObjSpec.of(BUTTON_COUNT_FIELD,
                                            JsSpecs.integer(),
                                            WHEEL_COUNT_FIELD,
                                            JsSpecs.integer(),
                                            TRACKING_TYPE_FIELD,
                                            JsEnumBuilder.withName("tracking_type")
                                                         .build(TRACKING_TYPE_FIELD)
                                           ))
                        .concat(baseSpec);

    JsObjSpec keyboardSpec =
        JsObjSpecBuilder.withName("keyboard_device")
                        .build(JsObjSpec.of(KEY_COUNT_FIELD,
                                            JsSpecs.integer(),
                                            MEDIA_BUTTONS_FIELD,
                                            JsSpecs.bool()
                                           ))
                        .concat(baseSpec);

    JsObjSpec usbHubSpec =
        JsObjSpecBuilder.withName("usb_hub_device")
                        .withFieldsDefaults(Map.of(CONNECTED_DEVICES_FIELD,
                                                   JsNull.NULL))
                        .build(JsObjSpec.of(CONNECTED_DEVICES_FIELD,
                                            JsSpecs.arrayOfSpec(JsSpecs.ofNamedSpec(PERIPHERAL_FIELD))
                                                   .nullable()
                                           )
                                        .withOptKeys(CONNECTED_DEVICES_FIELD)
                                        .concat(baseSpec));

    JsSpec peripheralSpec =
        JsSpecs.ofNamedSpec(PERIPHERAL_FIELD,
                            oneSpecOf(mouseSpec,
                                      keyboardSpec,
                                      usbHubSpec
                                     )
                           );

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "defs": {
                                                "peripheral_device": {
                                                  "oneOf": [
                                                    {
                                                      "properties": {
                                                        "name": {
                                                          "type": "string"
                                                        },
                                                        "trackingType": {
                                                          "enum": [
                                                            "trackingType"
                                                          ],
                                                          "type": "string"
                                                        },
                                                        "wheelCount": {
                                                          "type": "integer"
                                                        },
                                                        "type": {
                                                          "enum": [
                                                            "mouse",
                                                            "keyboard",
                                                            "usb_hub"
                                                          ],
                                                          "type": "string"
                                                        },
                                                        "buttonCount": {
                                                          "type": "integer"
                                                        }
                                                      },
                                                      "additionalProperties": false,
                                                      "type": "object",
                                                      "$id": "mouse_device",
                                                      "required": [
                                                        "buttonCount",
                                                        "wheelCount",
                                                        "trackingType",
                                                        "name",
                                                        "type"
                                                      ]
                                                    },
                                                    {
                                                      "properties": {
                                                        "name": {
                                                          "type": "string"
                                                        },
                                                        "keyCount": {
                                                          "type": "integer"
                                                        },
                                                        "type": {
                                                          "enum": [
                                                            "mouse",
                                                            "keyboard",
                                                            "usb_hub"
                                                          ],
                                                          "type": "string"
                                                        },
                                                        "mediaButtons": {
                                                          "type": "boolean"
                                                        }
                                                      },
                                                      "additionalProperties": false,
                                                      "type": "object",
                                                      "$id": "keyboard_device",
                                                      "required": [
                                                        "keyCount",
                                                        "mediaButtons",
                                                        "name",
                                                        "type"
                                                      ]
                                                    },
                                                    {
                                                      "properties": {
                                                        "name": {
                                                          "type": "string"
                                                        },
                                                        "connectedDevices": {
                                                          "items": {
                                                            "$ref": "#/defs/peripheral_device"
                                                          },
                                                          "default": null,
                                                          "type": [
                                                            "array",
                                                            "null"
                                                          ]
                                                        },
                                                        "type": {
                                                          "enum": [
                                                            "mouse",
                                                            "keyboard",
                                                            "usb_hub"
                                                          ],
                                                          "type": "string"
                                                        }
                                                      },
                                                      "additionalProperties": false,
                                                      "type": "object",
                                                      "$id": "usb_hub_device",
                                                      "required": [
                                                        "name",
                                                        "type"
                                                      ]
                                                    }
                                                  ]
                                                }
                                              },
                                              "$ref": "#/defs/peripheral_device",
                                              "$id": "peripheral_device"
                                            }"""),
                            SpecToJsonSchema.convert(peripheralSpec));
  }

  @Test
  public void testRecursiveNamedOneOfSpec() {

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
        JsSpecs.ofNamedSpec("mouse",
                            JsObjSpec.of(BUTTON_COUNT_FIELD,
                                         JsSpecs.integer(),
                                         WHEEL_COUNT_FIELD,
                                         JsSpecs.integer(),
                                         TRACKING_TYPE_FIELD,
                                         JsSpecs.oneStringOf(TRACKING_TYPE_ENUM)
                                        )
                                     .concat(baseSpec));

    var keyboardSpec =
        JsSpecs.ofNamedSpec("keyboard",
                            JsObjSpec.of(KEY_COUNT_FIELD,
                                         JsSpecs.integer(),
                                         MEDIA_BUTTONS_FIELD,
                                         JsSpecs.bool()
                                        )
                                     .concat(baseSpec));

    var usbHubSpec =
        JsSpecs.ofNamedSpec("usbHub",
                            JsObjSpec.of(CONNECTED_DEVICES_FIELD,
                                         JsSpecs.arrayOfSpec(JsSpecs.ofNamedSpec(PERIPHERAL_FIELD))
                                        )
                                     .withOptKeys(CONNECTED_DEVICES_FIELD)
                                     .concat(baseSpec));

    var peripheralSpec =
        JsSpecs.ofNamedSpec(PERIPHERAL_FIELD,
                            oneSpecOf(mouseSpec,
                                      keyboardSpec,
                                      usbHubSpec)
                           );

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "defs": {
                                                "mouse": {
                                                  "properties": {
                                                    "name": {
                                                      "type": "string"
                                                    },
                                                    "trackingType": {
                                                      "enum": [
                                                        "ball",
                                                        "optical"
                                                      ],
                                                      "type": "string"
                                                    },
                                                    "wheelCount": {
                                                      "type": "integer"
                                                    },
                                                    "type": {
                                                      "enum": [
                                                        "mouse",
                                                        "keyboard",
                                                        "usb_hub"
                                                      ],
                                                      "type": "string"
                                                    },
                                                    "buttonCount": {
                                                      "type": "integer"
                                                    }
                                                  },
                                                  "additionalProperties": false,
                                                  "type": "object",
                                                  "$id": "mouse",
                                                  "required": [
                                                    "buttonCount",
                                                    "wheelCount",
                                                    "trackingType",
                                                    "name",
                                                    "type"
                                                  ]
                                                },
                                                "keyboard": {
                                                  "properties": {
                                                    "name": {
                                                      "type": "string"
                                                    },
                                                    "keyCount": {
                                                      "type": "integer"
                                                    },
                                                    "type": {
                                                      "enum": [
                                                        "mouse",
                                                        "keyboard",
                                                        "usb_hub"
                                                      ],
                                                      "type": "string"
                                                    },
                                                    "mediaButtons": {
                                                      "type": "boolean"
                                                    }
                                                  },
                                                  "additionalProperties": false,
                                                  "type": "object",
                                                  "$id": "keyboard",
                                                  "required": [
                                                    "keyCount",
                                                    "mediaButtons",
                                                    "name",
                                                    "type"
                                                  ]
                                                },
                                                "usbHub": {
                                                  "properties": {
                                                    "name": {
                                                      "type": "string"
                                                    },
                                                    "connectedDevices": {
                                                      "items": {
                                                        "$ref": "#/defs/peripheral"
                                                      },
                                                      "type": "array"
                                                    },
                                                    "type": {
                                                      "enum": [
                                                        "mouse",
                                                        "keyboard",
                                                        "usb_hub"
                                                      ],
                                                      "type": "string"
                                                    }
                                                  },
                                                  "additionalProperties": false,
                                                  "type": "object",
                                                  "$id": "usbHub",
                                                  "required": [
                                                    "name",
                                                    "type"
                                                  ]
                                                },
                                                "peripheral": {
                                                  "oneOf": [
                                                    {
                                                      "$ref": "#/defs/mouse"
                                                    },
                                                    {
                                                      "$ref": "#/defs/keyboard"
                                                    },
                                                    {
                                                      "$ref": "#/defs/usbHub"
                                                    }
                                                  ]
                                                }
                                              },
                                              "$ref": "#/defs/peripheral",
                                              "$id": "peripheral"
                                            }
                                            """),
                            SpecToJsonSchema.convert(peripheralSpec));

  }

  @Test
  public void testNamedSpecs() {

    var unused =
        JsSpecs.ofNamedSpec("address",
                            JsObjSpec.of("street",
                                         JsSpecs.str(),
                                         "city",
                                         JsSpecs.str(),
                                         "zip",
                                         JsSpecs.integer()
                                        ));

    var email =
        JsSpecs.ofNamedSpec("email",
                            JsObjSpec.of("address",
                                         JsSpecs.str()
                                        ));

    var person = JsObjSpecBuilder.withName("person")
                                 .build(JsObjSpec.of("name",
                                                     JsSpecs.str(),
                                                     "age",
                                                     JsSpecs.integer(),
                                                     "addresses",
                                                     JsSpecs.arrayOfSpec(JsSpecs.ofNamedSpec("address")),
                                                     "email",
                                                     email
                                                    )
                                       );

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "defs": {
                                                "address": {
                                                  "properties": {
                                                    "zip": {
                                                      "type": "integer"
                                                    },
                                                    "street": {
                                                      "type": "string"
                                                    },
                                                    "city": {
                                                      "type": "string"
                                                    }
                                                  },
                                                  "additionalProperties": false,
                                                  "type": "object",
                                                  "$id": "address",
                                                  "required": [
                                                    "street",
                                                    "city",
                                                    "zip"
                                                  ]
                                                },
                                                "email": {
                                                  "properties": {
                                                    "address": {
                                                      "type": "string"
                                                    }
                                                  },
                                                  "additionalProperties": false,
                                                  "type": "object",
                                                  "$id": "email",
                                                  "required": [
                                                    "address"
                                                  ]
                                                }
                                              },
                                              "properties": {
                                                "addresses": {
                                                  "items": {
                                                    "$ref": "#/defs/address"
                                                  },
                                                  "type": "array"
                                                },
                                                "name": {
                                                  "type": "string"
                                                },
                                                "email": {
                                                  "$ref": "#/defs/email"
                                                },
                                                "age": {
                                                  "type": "integer"
                                                }
                                              },
                                              "additionalProperties": false,
                                              "type": "object",
                                              "$id": "person",
                                              "required": [
                                                "name",
                                                "age",
                                                "addresses",
                                                "email"
                                              ]
                                            }"""),
                            SpecToJsonSchema.convert(person));


  }

  @Test
  public void testStrSchema() {

    StrSchema strSchema = StrSchema.withLength(1,
                                               2)
                                   .setFormat("email")
                                   .setPattern(".*");
    var spec = JsObjSpec.of("a",
                            JsSpecs.str(strSchema),
                            "b",
                            JsSpecs.arrayOfStr(strSchema),
                            "c",
                            JsSpecs.mapOfStr(strSchema)
                           );

    Assertions.assertEquals("""
                                {
                                  "$schema": "https://json-schema.org/draft/2019-09/schema",
                                  "properties": {
                                    "a": {
                                      "maxLength": 2,
                                      "pattern": ".*",
                                      "format": "email",
                                      "minLength": 1,
                                      "type": "string"
                                    },
                                    "b": {
                                      "items": {
                                        "maxLength": 2,
                                        "pattern": ".*",
                                        "format": "email",
                                        "minLength": 1,
                                        "type": "string"
                                      },
                                      "type": "array"
                                    },
                                    "c": {
                                      "additionalProperties": {
                                        "maxLength": 2,
                                        "pattern": ".*",
                                        "format": "email",
                                        "minLength": 1,
                                        "type": "string"
                                      },
                                      "type": "object"
                                    }
                                  },
                                  "additionalProperties": false,
                                  "type": "object",
                                  "required": [
                                    "a",
                                    "b",
                                    "c"
                                  ]
                                }""",
                            SpecToJsonSchema.convert(spec)
                                            .toPrettyString());
  }

  @Test
  public void testArrays() {
    JsObjSpec spec = JsObjSpec.of("a",
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

    Assertions.assertEquals("""
                                {
                                  "$schema": "https://json-schema.org/draft/2019-09/schema",
                                  "properties": {
                                    "a": {
                                      "items": {
                                        "type": "string"
                                      },
                                      "type": "array"
                                    },
                                    "b": {
                                      "items": {
                                        "type": "integer"
                                      },
                                      "type": "array"
                                    },
                                    "c": {
                                      "items": {
                                        "type": "boolean"
                                      },
                                      "type": "array"
                                    },
                                    "d": {
                                      "items": {
                                        "type": "number"
                                      },
                                      "type": "array"
                                    },
                                    "e": {
                                      "items": {
                                        "type": "integer"
                                      },
                                      "type": "array"
                                    },
                                    "f": {
                                      "items": {
                                        "type": "object"
                                      },
                                      "type": "array"
                                    },
                                    "g": {
                                      "items": {
                                        "type": "number"
                                      },
                                      "type": "array"
                                    },
                                    "h": {
                                      "items": {
                                        "type": "integer"
                                      },
                                      "type": "array"
                                    }
                                  },
                                  "additionalProperties": false,
                                  "type": "object",
                                  "required": [
                                    "a",
                                    "b",
                                    "d",
                                    "e",
                                    "g",
                                    "h",
                                    "c",
                                    "f"
                                  ]
                                }""",
                            SpecToJsonSchema.convert(spec)
                                            .toPrettyString());

  }

  @Test
  public void testStringConstraints() {

    StrSchema strSchema = StrSchema.withLength(3,
                                               5)
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
                                     throw e;
                                   }
                                 }
                                );
       });
  }

  @Test
  public void testArrayOfStringConstraints() {

    StrSchema strSchema = StrSchema.withLength(3,
                                               5)
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
                                     throw e;
                                   }
                                 }
                                );
       });
  }
}
