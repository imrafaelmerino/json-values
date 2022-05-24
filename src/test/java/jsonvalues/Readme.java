package jsonvalues;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.tuple.Pair;
import jsonvalues.gen.*;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class Readme {


    public static void main(String[] args) {

        JsObj.of("name",
                 JsStr.of("Rafael"),
                 "surname",
                 JsStr.of("Merino"),
                 "phoneNumber",
                 JsStr.of("6666666"),
                 "registrationDate",
                 JsInstant.of(Instant.parse("2019-01-21T05:47:26.853Z")),
                 "addresses",
                 JsArray.of(JsObj.of("coordinates",
                                     JsArray.of(39.8581,
                                                -4.02263),
                                     "city",
                                     JsStr.of("Toledo"),
                                     "tags",
                                     JsArray.of("workAddress")
                            ),
                            JsObj.of("coordinates",
                                     JsArray.of(39.8581,
                                                -4.02263),
                                     "city",
                                     JsStr.of("Madrid"),
                                     "tags",
                                     JsArray.of("homeAddress")
                            )
                 )
        );

        JsObjSpec.strict("name",
                         JsSpecs.str(),
                         "surname",
                         JsSpecs.str(),
                         "phoneNumber",
                         JsSpecs.str(),
                         "registrationDate",
                         JsSpecs.instant(),
                         "addresses",
                         JsSpecs.arrayOfObjSpec(JsObjSpec.strict("coordinates",
                                                                 JsSpecs.tuple(JsSpecs.decimal(),
                                                                               JsSpecs.decimal()),
                                                                 "city",
                                                                 JsSpecs.str(),
                                                                 "tags",
                                                                 JsSpecs.arrayOfStr(),
                                                                 "zipCode",
                                                                 JsSpecs.str()
                                                )
                         )
        );


        int MAX_NAME_LENGTH = 10;
        int MAX_NAME_SURNAME = 10;
        int MAX_NAME_PHONE = 10;
        int MAX_CITY_LENGTH = 20;
        int MAX_TAG_LENGTH = 20;
        int MAX_ZIPCODE_LENGTH = 30;

        BiFunction<Integer, Integer, Predicate<String>> lengthBetween =
                (min, max) -> string -> string.length() <= max && string.length() >= min;


        BiFunction<Instant, Instant, Predicate<Instant>> instantBetween =
                (min, max) -> instant -> min.isBefore(instant) && max.isAfter(instant);

        BiFunction<Long, Long, Predicate<BigDecimal>> decBetween =
                (min, max) -> n -> BigDecimal.valueOf(min).compareTo(n) < 0 &&
                        BigDecimal.valueOf(max).compareTo(n) > 0;


        Predicate<String> nameSpec = lengthBetween.apply(0,
                                                         MAX_NAME_LENGTH);
        Predicate<String> surnameSpec = lengthBetween.apply(0,
                                                            MAX_NAME_SURNAME);
        Predicate<String> phoneSpec = lengthBetween.apply(0,
                                                          MAX_NAME_PHONE);
        Predicate<Instant> registrationDateSpec = instantBetween.apply(Instant.EPOCH,
                                                                       Instant.MAX);

        Predicate<BigDecimal> latitudeSpec = decBetween.apply(-90L,
                                                              90L);

        Predicate<BigDecimal> longitudeSpec = decBetween.apply(-180L,
                                                               180L);

        Predicate<String> citySpec = lengthBetween.apply(0,
                                                         MAX_CITY_LENGTH);
        Predicate<String> tagSpec = lengthBetween.apply(0,
                                                        MAX_TAG_LENGTH);
        Predicate<String> zipCodeSpec = lengthBetween.apply(0,
                                                            MAX_ZIPCODE_LENGTH);


        JsObjSpec.strict("name",
                         JsSpecs.str(nameSpec),
                         "surname",
                         JsSpecs.str(surnameSpec),
                         "phoneNumber",
                         JsSpecs.str(phoneSpec),
                         "registrationDate",
                         JsSpecs.instant(registrationDateSpec),
                         "addresses",
                         JsSpecs.arrayOfObjSpec(JsObjSpec.strict("coordinates",
                                                                 JsSpecs.tuple(JsSpecs.decimal(latitudeSpec),
                                                                               JsSpecs.decimal(longitudeSpec)),
                                                                 "city",
                                                                 JsSpecs.str(citySpec),
                                                                 "tags",
                                                                 JsSpecs.arrayOfStr(tagSpec),
                                                                 "zipCode",
                                                                 JsSpecs.str(zipCodeSpec)
                                                )
                         )
        );


        JsObjGen.of("name",
                    JsStrGen.biased(0,
                                    100),
                    "surname",
                    JsStrGen.biased(0,
                                    100),
                    "phoneNumber",
                    JsStrGen.biased(0,
                                    10),
                    "registrationDate",
                    JsInstantGen.biased(0,
                                        Instant.MAX.getEpochSecond()),
                    "addresses",
                    JsArrayGen.biased(JsObjGen.of("coordinates",
                                                  JsTupleGen.of(JsBigDecGen.biased(-90,
                                                                                   90),
                                                                JsBigDecGen.biased(-180,
                                                                                   180)
                                                  ),
                                                  "city",
                                                  JsStrGen.biased(0,
                                                                  100),
                                                  "tags",
                                                  JsArrayGen.biased(JsStrGen.biased(0,
                                                                                    20),
                                                                    0,
                                                                    100)
                                                      ,
                                                  "zipCode",
                                                  JsStrGen.biased(0,
                                                                  10)
                                              )
                                              .setOptionals("tags",
                                                            "zipCode",
                                                            "city"),
                                      0,
                                      1)

                )
                .setOptionals("surname",
                              "phoneNumber",
                              "addresses");




    }

}