package jsonvalues.readme;

import jsonvalues.JsArray;
import jsonvalues.JsInstant;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.gen.*;
import jsonvalues.spec.JsObjParser;
import jsonvalues.spec.JsObjSpec;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static jsonvalues.spec.JsSpecs.*;

public class Readme {

    @Test
    public void test() {


        JsObj person =
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
                                             "zipCode",
                                             JsStr.of("45920"),
                                             "tags",
                                             JsArray.of("workAddress")
                                    ),
                                    JsObj.of("coordinates",
                                             JsArray.of(39.8581,
                                                        -4.02263),
                                             "city",
                                             JsStr.of("Madrid"),
                                             "zipCode",
                                             JsStr.of("28029"),
                                             "tags",
                                             JsArray.of("homeAddress",
                                                        "amazon")
                                    )
                         )
                );

        JsObjSpec personSpec =
                JsObjSpec.strict("name",
                                 str(),
                                 "surname",
                                 str(),
                                 "phoneNumber",
                                 str(),
                                 "registrationDate",
                                 instant(),
                                 "addresses",
                                 arrayOfObjSpec(JsObjSpec.strict("coordinates",
                                                                 tuple(decimal(),
                                                                       decimal()
                                                                 ),
                                                                 "city",
                                                                 str(),
                                                                 "tags",
                                                                 arrayOfStr(),
                                                                 "zipCode",
                                                                 str()
                                                )
                                 )
                );


        BiFunction<Integer, Integer, Predicate<String>> lengthBetween =
                (min, max) -> string -> string.length() <= max &&
                        string.length() >= min;


        BiFunction<Instant, Instant, Predicate<Instant>> instantBetween =
                (min, max) -> instant -> min.isBefore(instant) &&
                        max.isAfter(instant);

        BiFunction<Long, Long, Predicate<BigDecimal>> decBetween =
                (min, max) -> n -> BigDecimal.valueOf(min).compareTo(n) < 0 &&
                        BigDecimal.valueOf(max).compareTo(n) > 0;


        int MAX_NAME_LENGTH = 10;
        int MAX_SURNAME_LENGTH = 10;
        int MAX_PHONE_LENGTH = 10;
        int MAX_CITY_LENGTH = 20;
        int MAX_TAG_LENGTH = 20;
        int MAX_ZIPCODE_LENGTH = 30;

        Predicate<String> nameSpec = lengthBetween.apply(0,
                                                         MAX_NAME_LENGTH);

        Predicate<String> surnameSpec = lengthBetween.apply(0,
                                                            MAX_SURNAME_LENGTH);

        Predicate<String> phoneSpec = lengthBetween.apply(0,
                                                          MAX_PHONE_LENGTH);

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


        JsObjSpec personSpec1 =
                JsObjSpec.strict("name",
                                 str(nameSpec),
                                 "surname",
                                 str(surnameSpec),
                                 "phoneNumber",
                                 str(phoneSpec).nullable(),
                                 "registrationDate",
                                 instant(registrationDateSpec),
                                 "addresses",
                                 arrayOfObjSpec(JsObjSpec.lenient("coordinates",
                                                                  tuple(decimal(latitudeSpec),
                                                                        decimal(longitudeSpec)
                                                                  ),
                                                                  "city",
                                                                  str(citySpec),
                                                                  "tags",
                                                                  arrayOfStr(tagSpec),
                                                                  "zipCode",
                                                                  str(zipCodeSpec)
                                                         )
                                                         .setOptionals("tags",
                                                                       "zipCode",
                                                                       "city")
                                 )
                         )
                         .setOptionals("surname",
                                       "phoneNumber",
                                       "addresses");


        JsObjParser personParser = new JsObjParser(personSpec);

        String string = "...";

        //JsObj personObj = personParser.parse(string);


        JsObjGen personGen =
                JsObjGen.of("name",
                            JsStrGen.biased(0,
                                            MAX_NAME_LENGTH + 1),
                            "surname",
                            JsStrGen.biased(0,
                                            MAX_NAME_LENGTH + 1),
                            "phoneNumber",
                            JsStrGen.biased(0,
                                            MAX_PHONE_LENGTH + 1),
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
                                                                            100),
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
