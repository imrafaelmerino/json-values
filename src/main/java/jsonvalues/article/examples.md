```code


   JsObj person1 = JsObj.of("name", JsStr.of("Rafael"),
                             "age", JsInt.of(10),
                             "isStudent", JsBool.TRUE,
                             "height", JsBigDec.of(new BigDecimal("1.2")),
                             "hobbies", JsArray.of("Reading", "Gaming", "Traveling"),
                             "contact", JsObj.of("address", JsObj.of("street", JsStr.of("123 Main St"),
                                                                     "city", JsStr.of("Cityville"),
                                                                     "zipCode", JsStr.of("12345")
                                                                    )
                                                )
                            );

    JsObj person2 = JsObj.of("name", JsStr.of("Rafael"),
                             "age", JsInt.of(10),
                             "isStudent", JsBool.TRUE,
                             "height", JsBigDec.of(new BigDecimal("1.2")),
                             "hobbies", JsArray.of("Reading", "Gaming", "Traveling"),
                             "contact", JsObj.of("email", JsObj.of("type", JsStr.of("work"),
                                                                   "address", JsStr.of("john@example.com")
                                                                  )
                                                )
                            );


    JsObj person3 = JsObj.of("name", JsStr.of("Rafael"),
                             "age", JsInt.of(10),
                             "isStudent", JsBool.TRUE,
                             "height", JsBigDec.of(new BigDecimal("1.2")),
                             "hobbies", JsArray.of("Reading", "Gaming", "Traveling"),
                             "contact", JsObj.of("phone", JsObj.of("type", JsStr.of("mobile"),
                                                                   "number", JsStr.of("555-1234")
                                                                  )
                                                )
                            );

```

```code 

    JsObjSpec phoneSpec = JsObjSpec.of("phone",
                                       JsObjSpec.of("type", oneStringOf("mobile", "fix"),
                                                    "number", str()
                                                   )
                                      );

    JsObjSpec emailSpec = JsObjSpec.of("email",
                                       JsObjSpec.of("type", oneStringOf("work", "home"),
                                                    "address", str()
                                                   )
                                      );

    JsObjSpec addressSpec = JsObjSpec.of("address",
                                         JsObjSpec.of("street", str(),
                                                      "city", str(),
                                                      "zipCode", str()
                                                     )
                                        );

    JsObjSpec personSpec = JsObjSpec.of("name", str(),
                                        "age", integer(),
                                        "isStudent", bool(),
                                        "height", decimal(),
                                        "hobbies", arrayOfStr(),
                                        "contact", JsSpecs.oneSpecOf(emailSpec,addressSpec,phoneSpec)
                                       );


```