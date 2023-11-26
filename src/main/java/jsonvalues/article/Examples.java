package jsonvalues.article;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.IntGen;
import fun.gen.StrGen;
import jsonvalues.*;
import jsonvalues.gen.*;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static jsonvalues.spec.JsSpecs.*;

public class Examples {


    static JsObj person1 = JsObj.of("name", JsStr.of("Rafael"),
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

    static JsObj person2 = JsObj.of("name", JsStr.of("Rafael"),
                                    "age", JsInt.of(10),
                                    "isStudent", JsBool.TRUE,
                                    "height", JsBigDec.of(new BigDecimal("1.2")),
                                    "hobbies", JsArray.of("Reading", "Gaming", "Traveling"),
                                    "contact", JsObj.of("email", JsObj.of("type", JsStr.of("work"),
                                                                          "address", JsStr.of("john@example.com")
                                                                         )
                                                       )
                                   );


    static JsObj person3 = JsObj.of("name", JsStr.of("Rafael"),
                                    "age", JsInt.of(10),
                                    "isStudent", JsBool.TRUE,
                                    "height", JsBigDec.of(new BigDecimal("1.2")),
                                    "hobbies", JsArray.of("Reading", "Gaming", "Traveling"),
                                    "contact", JsObj.of("phone", JsObj.of("type", JsStr.of("mobile"),
                                                                          "number", JsStr.of("555-1234")
                                                                         )
                                                       )
                                   );


    static JsObjSpec phoneSpec = JsObjSpec.of("phone",
                                              JsObjSpec.of("type", oneStringOf("mobile", "landline"),
                                                           "number", str(6, 12)
                                                          )
                                             );

    static JsObjGen phoneGen = JsObjGen.of("phone",
                                           JsObjGen.of("type", Combinators.oneOf("mobile", "landline").map(JsStr::of),
                                                       "number", JsStrGen.alphanumeric(6, 12)
                                                      )
                                          );
    static Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    static JsObjSpec emailSpec =
            JsObjSpec.of("email",
                         JsObjSpec.of("type", oneStringOf("work", "home"),
                                      "address", str(EMAIL_PATTERN)
                                     )
                        );

    static JsObjSpec addressSpec =
            JsObjSpec.of("address",
                         JsObjSpec.of("street", str(5, 256),
                                      "city", str(1, 256),
                                      "zipCode", str(5, 5)
                                     )
                        );
    static JsObjGen addressGen =
            JsObjGen.of("address",
                        JsObjGen.of("street", JsStrGen.alphabetic(5, 256),
                                    "city", JsStrGen.alphabetic(1, 256),
                                    "zipCode", JsStrGen.alphabetic(5, 5)
                                   )
                       );
    static IntPredicate ageValidation = n -> n >= 16 && n <= 120;
    static JsObjSpec personSpec =
            JsObjSpec.of("name", str(2, 256),
                         "age", integer(ageValidation),
                         "isStudent", bool(),
                         "height", decimal(),
                         "hobbies", arrayOfStr().nullable(),
                         "contact", JsSpecs.oneSpecOf(emailSpec, addressSpec, phoneSpec)
                        );
    static Gen<JsStr> emailAddressGen =
            Combinators.oneOf("@gmail.com", "@hotmail.com")
                       .then(domain -> StrGen.alphabetic(3, 200)
                                             .map(address -> JsStr.of(address + domain))
                            );
    static JsObjGen emailGen =
            JsObjGen.of("email",
                        JsObjGen.of("type", Combinators.oneOf("work", "home")
                                                       .map(JsStr::of),
                                    "address", emailAddressGen
                                   )
                       );

    static JsObjGen personGen =
            JsObjGen.of("name", Combinators.oneOf(JsIntGen.arbitrary(), JsStrGen.alphabetic(2, 256)),
                        "age", JsIntGen.arbitrary(16, 120),
                        "isStudent", JsBoolGen.arbitrary(),
                        "height", JsBigDecGen.arbitrary(),
                        "hobbies", IntGen.arbitrary(0, 5)
                                         .then(size -> JsArrayGen.ofN(JsStrGen.alphabetic(3, 5), size)),
                        "contact", Combinators.oneOf(emailGen, addressGen, phoneGen)
                       )
                    .withAllOptKeys()
                    .withAllNullValues();

    static Predicate<JsObj> personHasEmail = it -> it.getObj("contact").containsKey("email");
    static Predicate<JsObj> personHasAddress = it -> it.getObj("contact").containsKey("address");
    static Predicate<JsObj> personHasPhone = it -> it.getObj("contact").containsKey("phone");
    static Gen<JsObj> validPersonGen = personGen.suchThat(personSpec);
    static Gen<JsObj> validPersonGenWithEmail = validPersonGen.suchThat(personHasEmail);
    static Gen<JsObj> validPersonGenWithAddress = validPersonGen.suchThat(personHasAddress);
    static Gen<JsObj> validPersonGenWithPhone = validPersonGen.suchThat(personHasPhone);
    static Gen<JsObj> invalidPersonGen = personGen.suchThatNo(personSpec);



    public static void main(String[] args) {

        System.out.println(personSpec.test(person1));
        System.out.println(personSpec.test(person2));
        System.out.println(personSpec.test(person3));


        System.out.println(validPersonGen.sample(1000)
                                         .allMatch(obj -> personSpec.test(obj)
                                                                    .isEmpty()));

        System.out.println(invalidPersonGen.sample(1000)
                                           .noneMatch(obj -> personSpec.test(obj)
                                                                       .isEmpty()));


        System.out.println(validPersonGen.classify(100000,
                                                   Map.of("email", personHasEmail,
                                                          "phone", personHasPhone,
                                                          "address", personHasAddress),
                                                   "no_contact"
                                                  )
                          );



    }


}
