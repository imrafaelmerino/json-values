package jsonvalues.spec;

import static jsonvalues.spec.JsSpecs.oneSpecOf;

import fun.gen.Combinators;
import fun.gen.Gen;
import java.math.BigDecimal;
import java.math.BigInteger;
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

public class JsSpecToJsonSchemaTest {


  @Test
  public void shouldConvertComplexObjSpecToJsonSchema() {
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
  public void shouldRecursiveOneOfSpec() {
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
  public void shouldRecursiveNamedOneOfSpec() {

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
  public void shouldNamedSpecs() {

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
  public void shouldStrSchema() {

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
  public void shouldArrays() {
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
  public void shouldStringConstraints() {

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
  public void shouldArrayOfStringConstraints() {

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

  @Test
  public void shouldConvertNamedSpecAsRoot() {
    String rootName = "root_named_person";
    JsSpec namedRoot = JsSpecs.ofNamedSpec(rootName,
                                           JsObjSpec.of("name",
                                                        JsSpecs.str()));

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "defs": {
                                                "root_named_person": {
                                                  "properties": {
                                                    "name": {
                                                      "type": "string"
                                                    }
                                                  },
                                                  "additionalProperties": false,
                                                  "type": "object",
                                                  "$id": "root_named_person",
                                                  "required": [
                                                    "name"
                                                  ]
                                                }
                                              },
                                              "$id": "root_named_person",
                                              "$ref": "#/defs/root_named_person"
                                            }"""),
                            SpecToJsonSchema.convert(namedRoot));
  }

  @Test
  public void shouldTupleAndArraySchemaConstraints() {
    JsObjSpec spec = JsObjSpec.of("coords",
                                  JsSpecs.tuple(JsSpecs.decimal(),
                                                JsSpecs.decimal()
                                               ).nullable(),
                                  "tags",
                                  JsSpecs.arrayOfStr(StrSchema.withLength(1,
                                                                          3),
                                                     ArraySchema.sizeBetween(1,
                                                                             2)
                                                                .setUniqueItems()
                                                    )
                                 );

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "properties": {
                                                "coords": {
                                                  "items": {
                                                    "items": [
                                                      {
                                                        "type": "number"
                                                      },
                                                      {
                                                        "type": "number"
                                                      }
                                                    ],
                                                    "additionalItems": false,
                                                    "type": [
                                                      "array",
                                                      "null"
                                                    ]
                                                  },
                                                  "type": [
                                                    "array",
                                                    "null"
                                                  ]
                                                },
                                                "tags": {
                                                  "items": {
                                                    "maxLength": 3,
                                                    "minLength": 1,
                                                    "type": "string",
                                                    "minItems": 1,
                                                    "maxItems": 2,
                                                    "uniqueItems": true
                                                  },
                                                  "type": "array"
                                                }
                                              },
                                              "additionalProperties": false,
                                              "type": "object",
                                              "required": [
                                                "coords",
                                                "tags"
                                              ]
                                            }"""),
                            SpecToJsonSchema.convert(spec));
  }

  @Test
  public void shouldConvertMapOfLongWithConstraints() {
    JsSpec spec = JsSpecs.mapOfDouble(DoubleSchema.between(2.0,
                                                           5.0));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "additionalProperties": {
                                                "maximum": 5.0,
                                                "minimum": 2.0,
                                                "type": "number"
                                              },
                                              "type": "object"
                                            }"""),
                            SpecToJsonSchema.convert(spec));
  }

  @Test
  public void shouldConvertPrimitiveDirectSpecs() {
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "const": "x"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.cons(JsStr.of("x"))));

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "object"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.obj()));

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "*"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.any()));
  }

  @Test
  public void shouldConvertArraySpecDirect() {
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "array",
                                              "items": {
                                                "type": "*"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.array()));

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "array",
                                              "items": {
                                                "type": "string"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.arrayOfStr(s -> true)));
  }

  @Test
  public void shouldConvertInternalMapSpecsWithConstraints() {
    JsSpec mapOfInt = new JsMapOfInt(false,
                                     new IntegerSchemaConstraints(1,
                                                                  3));
    JsSpec mapOfLong = new JsMapOfLong(false,
                                       new LongSchemaConstraints(2,
                                                                 4));
    JsSpec mapOfDec = new JsMapOfDec(false,
                                     new DecimalSchemaConstraints(BigDecimal.ONE,
                                                                  BigDecimal.TEN));
    JsSpec mapOfBigInt = new JsMapOfBigInt(false,
                                           new BigIntSchemaConstraints(BigInteger.ONE,
                                                                       BigInteger.TEN));

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "additionalProperties": {
                                                "maximum": 3,
                                                "minimum": 1,
                                                "type": "integer"
                                              },
                                              "type": "object"
                                            }"""),
                            SpecToJsonSchema.convert(mapOfInt));

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "additionalProperties": {
                                                "maximum": 4,
                                                "minimum": 2,
                                                "type": "integer"
                                              },
                                              "type": "object"
                                            }"""),
                            SpecToJsonSchema.convert(mapOfLong));

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "additionalProperties": {
                                                "maximum": 10,
                                                "minimum": 1,
                                                "type": "number"
                                              },
                                              "type": "object"
                                            }"""),
                            SpecToJsonSchema.convert(mapOfDec));

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "additionalProperties": {
                                                "maximum": 10,
                                                "minimum": 1,
                                                "type": "integer"
                                              },
                                              "type": "object"
                                            }"""),
                            SpecToJsonSchema.convert(mapOfBigInt));

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "additionalProperties": {
                                                "format": "date-time",
                                                "type": "string"
                                              },
                                              "type": "object"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.mapOfInstant()));

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "additionalProperties": {
                                                "contentEncoding": "base64",
                                                "type": "string"
                                              },
                                              "type": "object"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.mapOfBinary()));

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "additionalProperties": {
                                                "type": "boolean"
                                              },
                                              "type": "object"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.mapOfBool()));
  }

  @Test
  public void shouldConvertPredicateSpecs() {
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "integer"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.integer(i -> i >= 0)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "integer"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.longInteger(l -> l >= 0)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "number"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.doubleNumber(d -> d >= 0)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "number"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.decimal(d -> d.signum() >= 0)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "integer"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.bigInteger(b -> b.signum() >= 0)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "string"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.str(s -> !s.isEmpty())));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "contentEncoding": "base64",
                                              "type": "string"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.binary(bytes -> bytes.length > 0)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "format": "date-time",
                                              "type": "string"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.instant(i -> true)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "object"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.obj(o -> true)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "*"
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.any(v -> true)));
  }

  @Test
  public void shouldConvertArraySuchThatSpecs() {
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "array",
                                              "items": {
                                                "type": "integer"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.arrayOfIntSuchThat(a -> true)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "array",
                                              "items": {
                                                "type": "number"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.arrayOfDoubleSuchThat(a -> true)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "array",
                                              "items": {
                                                "type": "string"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.arrayOfStrSuchThat(a -> true)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "array",
                                              "items": {
                                                "type": "*"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.arraySuchThat(a -> true)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "array",
                                              "items": {
                                                "type": "object"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.arrayOfObjSuchThat(a -> true)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "array",
                                              "items": {
                                                "type": "integer"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.arrayOfLongSuchThat(a -> true)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "array",
                                              "items": {
                                                "type": "integer"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.arrayOfBigIntSuchThat(a -> true)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "array",
                                              "items": {
                                                "type": "number"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.arrayOfDecSuchThat(a -> true)));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "array",
                                              "items": {
                                                "type": "boolean"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.arrayOfBoolSuchThat(a -> true)));
  }

  @Test
  public void shouldConvertMapWithoutConstraints() {
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "object",
                                              "additionalProperties": {
                                                "type": "integer"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.mapOfInteger()));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "object",
                                              "additionalProperties": {
                                                "type": "integer"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.mapOfLong()));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "object",
                                              "additionalProperties": {
                                                "type": "integer"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.mapOfBigInteger()));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "object",
                                              "additionalProperties": {
                                                "type": "number"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.mapOfDecimal()));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "object",
                                              "additionalProperties": {
                                                "type": "number"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.mapOfDouble()));
    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": "object",
                                              "additionalProperties": {
                                                "type": "string"
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(JsSpecs.mapOfStr()));
  }

  @Test
  public void shouldMapAliasesConsistency() {
    Assertions.assertEquals(SpecToJsonSchema.convert(JsSpecs.mapOfInteger()),
                            SpecToJsonSchema.convert(JsSpecs.mapOfInt()));
    Assertions.assertEquals(SpecToJsonSchema.convert(JsSpecs.mapOfBigInteger()),
                            SpecToJsonSchema.convert(JsSpecs.mapOfBigInt()));
    Assertions.assertEquals(SpecToJsonSchema.convert(JsSpecs.mapOfInt(IntegerSchema.between(1,
                                                                                             3))),
                            SpecToJsonSchema.convert(new JsMapOfInt(false,
                                                                    new IntegerSchemaConstraints(1,
                                                                                                 3))));
    Assertions.assertEquals(SpecToJsonSchema.convert(JsSpecs.mapOfBigInt(BigIntSchema.between(BigInteger.ONE,
                                                                                                BigInteger.TEN))),
                            SpecToJsonSchema.convert(JsSpecs.mapOfBigInteger(BigIntSchema.between(BigInteger.ONE,
                                                                                                   BigInteger.TEN))));
  }

  @Test
  public void shouldNullableMapOfBigIntKeepsConstraints() {
    JsSpec nonNullable =
        JsSpecs.mapOfBigInt(BigIntSchema.between(BigInteger.ONE,
                                                 BigInteger.TEN));
    JsSpec nullable = nonNullable.nullable();

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "type": [
                                                "object",
                                                "null"
                                              ],
                                              "additionalProperties": {
                                                "type": "integer",
                                                "minimum": 1,
                                                "maximum": 10
                                              }
                                            }"""),
                            SpecToJsonSchema.convert(nullable));
  }

  @Test
  public void shouldNullableNamedSpecAsRoot() {
    String rootName = "nullable_root_named_person";
    JsSpec namedRoot = JsSpecs.ofNamedSpec(rootName,
                                           JsObjSpec.of("name",
                                                        JsSpecs.str()))
                              .nullable();

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "defs": {
                                                "nullable_root_named_person": {
                                                  "properties": {
                                                    "name": {
                                                      "type": "string"
                                                    }
                                                  },
                                                  "additionalProperties": false,
                                                  "type": "object",
                                                  "$id": "nullable_root_named_person",
                                                  "required": [
                                                    "name"
                                                  ]
                                                }
                                              },
                                              "$id": "nullable_root_named_person",
                                              "oneOf": [
                                                {
                                                  "$ref": "#/defs/nullable_root_named_person"
                                                },
                                                {
                                                  "type": "null"
                                                }
                                              ]
                                            }"""),
                            SpecToJsonSchema.convert(namedRoot));
  }

  @Test
  public void shouldNullableNamedSpecAsField() {
    JsSpecs.ofNamedSpec("address_named",
                        JsObjSpec.of("street",
                                     JsSpecs.str()));

    JsObjSpec spec = JsObjSpec.of("address",
                                  JsSpecs.ofNamedSpec("address_named")
                                         .nullable());

    Assertions.assertEquals(JsObj.parse("""
                                            {
                                              "$schema": "https://json-schema.org/draft/2019-09/schema",
                                              "defs": {
                                                "address_named": {
                                                  "properties": {
                                                "street": {
                                                      "type": "string"
                                                    }
                                                  },
                                                  "additionalProperties": false,
                                                  "type": "object",
                                                  "$id": "address_named",
                                                  "required": [
                                                    "street"
                                                  ]
                                                }
                                              },
                                              "properties": {
                                                "address": {
                                                  "oneOf": [
                                                    {
                                                      "$ref": "#/defs/address_named"
                                                    },
                                                    {
                                                      "type": "null"
                                                    }
                                                  ]
                                                }
                                              },
                                              "additionalProperties": false,
                                              "type": "object",
                                              "required": [
                                                "address"
                                              ]
                                            }"""),
                            SpecToJsonSchema.convert(spec));
  }
}
