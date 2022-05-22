package jsonvalues.readme;


import jsonvalues.spec.JsObjSpec;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.function.*;

import static jsonvalues.spec.JsSpecs.*;


public class A {

    public static void main(String[] args) {



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
        long LAT_MIN = -90;
        long LAT_MAX = 90;
        long LON_MIN = -180;
        long LON_MAX = 180;
        int MIN_ADDRESSES_SIZE = 1;
        int MAX_ADDRESSES_SIZE = 100;
        int MAX_TAGS_SIZE = 10;


        Predicate<String> nameSpec = lengthBetween.apply(0, MAX_NAME_LENGTH);

        Predicate<String> surnameSpec = lengthBetween.apply(0, MAX_SURNAME_LENGTH);

        Predicate<String> phoneSpec = lengthBetween.apply(0, MAX_PHONE_LENGTH);

        Predicate<Instant> registrationDateSpec = instantBetween.apply(Instant.EPOCH, Instant.MAX);

        Predicate<BigDecimal> latitudeSpec = decBetween.apply(LAT_MIN, LAT_MAX);

        Predicate<BigDecimal> longitudeSpec = decBetween.apply(LON_MIN, LON_MAX);

        Predicate<String> citySpec = lengthBetween.apply(0, MAX_CITY_LENGTH);

        Predicate<String> tagSpec = lengthBetween.apply(0, MAX_TAG_LENGTH);

        Predicate<String> zipCodeSpec = lengthBetween.apply(0, MAX_ZIPCODE_LENGTH);


        JsObjSpec personSpec =
                JsObjSpec.strict("name", str(nameSpec),
                                 "surname", str(surnameSpec),
                                 "phoneNumber", str(phoneSpec).nullable(),
                                 "registrationDate", instant(registrationDateSpec),
                                 "addresses", arrayOfObjSpec(JsObjSpec.lenient("coordinates",
                                                                               tuple(decimal(latitudeSpec),
                                                                                     decimal(longitudeSpec)
                                                                               ),
                                                                               "city", str(citySpec),
                                                                               "tags", arrayOfStr(tagSpec),
                                                                               "zipCode", str(zipCodeSpec)
                                                                      )
                                                                      .setOptionals("tags", "zipCode", "city"),
                                                             MIN_ADDRESSES_SIZE,
                                                             MAX_ADDRESSES_SIZE
                                 )
                         )
                         .setOptionals("surname", "phoneNumber", "addresses");
    }
}
