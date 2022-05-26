package jsonvalues;

import jsonvalues.gen.*;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static jsonvalues.JsPath.path;

public class Readme {


    public static void main(String[] args) {

        JsPath path =  path("/a/b/0");

        Position head = path.head();

        Assertions.assertEquals(head,
                                Key.of("a")
        );

        JsPath tail = path.tail();
        Assertions.assertEquals(tail.head(),
                                Key.of("b")
        );

        Assertions.assertEquals(tail.last(),
                                Index.of(0)
        );


        JsObj a1 = JsObj.of("name",
                            JsStr.of("Rafael"),
                            "surname",
                            JsStr.of("Merino"),
                            "phoneNumber",
                            JsStr.of("6666666"),
                            "registrationDate",
                            JsInstant.of("2019-01-21T05:47:26.853Z"),
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
                                                JsArray.of(40.4168,
                                                           3.7038),
                                                "city",
                                                JsStr.of("Madrid"),
                                                "tags",
                                                JsArray.of("homeAddress")
                                       )
                            )
        );

        JsObj a2 = JsObj.of(path("/name"),
                            JsStr.of("Rafael"),
                            path("/surname"),
                            JsStr.of("Merino"),
                            path("/phoneNumber"),
                            JsStr.of("6666666"),
                            path("/registrationDate"),
                            JsInstant.of("2019-01-21T05:47:26.853Z"),
                            path("/addresses/0/coordinates/0"),
                            JsDouble.of(39.8581),
                            path("/addresses/0/coordinates/1"),
                            JsDouble.of(-4.02263),
                            path("/addresses/0/city"),
                            JsStr.of("Toledo"),
                            path("/addresses/0/tags"),
                            JsArray.of("workAddress"),
                            path("/addresses/1/coordinates/0"),
                            JsDouble.of(40.4168),
                            path("/addresses/1/coordinates/1"),
                            JsDouble.of(3.7038),
                            path("/addresses/1/city"),
                            JsStr.of("Madrid"),
                            path("/addresses/1/tags"),
                            JsArray.of("homeAddress")
        );

        Assertions.assertEquals(a1,a2);


                JsObj.empty().set(path("/name"), JsStr.of("Rafael"))
                             .set(path("/surname"), JsStr.of("Merino"))
                             .set(path("/phoneNumber"), JsStr.of("6666666"));




        JsArray a12 = JsArray.of("apple", "orange", "pear");

        JsArray b1 = JsArray.of(1, 2, 3, 4);

        JsArray c = JsArray.of(JsStr.of("hi"), JsInt.of(1), JsBool.TRUE, JsNull.NULL);

        List<JsValue> list = new ArrayList<>();
        Set<JsValue> set = new HashSet<>();

        JsArray.ofIterable(list);
        JsArray.ofIterable(set);



        JsArray f = JsArray.empty().append(JsInt.of(1))
                                   .prepend(JsInt.of(0));

        JsArray g = JsArray.empty().append(JsInt.of(3))
                           .prepend(JsInt.of(2));

        System.out.println(f.appendAll(g));






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