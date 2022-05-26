package jsonvalues.readme;

import fun.optic.Lens;
import fun.optic.Option;
import jsonvalues.*;
import jsonvalues.gen.*;
import jsonvalues.spec.JsErrorPair;
import jsonvalues.spec.JsObjParser;
import jsonvalues.spec.JsObjSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import static jsonvalues.JsPath.path;
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


        System.out.println(JsObj.empty().set(path("/food/fruits/2"), JsStr.of("apple"), JsStr.of("")).toPrettyString());


    }


    static void lenses(){
        Lens<JsObj, JsValue> nameLens = JsObj.lens.value("name");

        Lens<JsObj, JsValue> ageOpt = JsObj.lens.value("age");

        Lens<JsObj, JsValue> cityLens = JsObj.lens.value(path("/address/city"));

        Lens<JsObj, JsValue> lanLens = JsObj.lens.value("languages");

        JsPath latPath = path("/address/coordinates/0");
        Lens<JsObj, JsValue> latLens = JsObj.lens.value(latPath);

        Function<IntFunction<Integer>, Function<JsObj, JsObj>> modifyAge =
                fn -> ageOpt.modify.apply(JsInt.prism.modify.apply(fn::apply));

        Function<Function<String,String>,Function<JsObj, JsObj>> modifyName =
                fn -> nameLens.modify.apply(JsStr.prism.modify.apply(fn::apply));

        Function<String, Function<JsObj, JsObj>> addLanguage =
                language -> {
                    Function<JsArray,JsArray> addLanToArr = a -> a.append(JsStr.of(language));
                    return lanLens.modify.apply(JsArray.prism.modify.apply(addLanToArr));
                };

        Function<String, Function<JsObj, JsObj>> setCity = city -> cityLens.set.apply(JsStr.of(city));

        Function<Function<Double, Double>, Function<JsObj,JsObj>> modifyLatitude =
                fn -> latLens.modify.apply(JsDouble.prism.modify.apply(fn));

//And finally:

        Function<JsObj, JsObj> modifyPerson =
                modifyAge.apply(n -> n + 1)
                         .andThen(modifyName.apply(String::trim))
                         .andThen(setCity.apply("Paris"))
                         .andThen(modifyLatitude.apply(lat -> -lat))
                         .andThen(addLanguage.apply("Lisp"));



        Assertions.assertEquals(Optional.of("hi!"),
                                 JsStr.prism.getOptional.apply(JsStr.of("hi!")));

// 1 is not a string, empty is returned
        Assertions.assertEquals(Optional.empty(),
                                JsStr.prism.getOptional.apply(JsInt.of(1)));

        Assertions.assertEquals(JsStr.of("HI!"),
                                JsStr.prism.modify.apply(String::toUpperCase)
                                                  .apply(JsStr.of("hi!")));

// 1 is not a string, the same value is returned
        Assertions.assertEquals(JsInt.of(1),
                                JsStr.prism.modify.apply(String::toUpperCase)
                                                  .apply(JsInt.of(1)));

        Assertions.assertEquals(Optional.of(2),
                                JsInt.prism.getOptional.apply(JsInt.of(2)));

        Assertions.assertEquals(Optional.empty(),
                                JsInt.prism.getOptional.apply(JsStr.of("hi!")));

        Assertions.assertEquals(JsInt.of(2),
                                JsInt.prism.modify.apply(n -> n  + 1)
                                                  .apply(JsInt.of(1)));

        Assertions.assertEquals(JsNull.NULL,
                                JsInt.prism.modify.apply(n -> n  + 1)
                                                  .apply(JsNull.NULL));

        Assertions.assertEquals(Optional.empty(),
                                JsInt.prism.modifyOpt.apply(n -> n  + 1)
                                                          .apply(JsNull.NULL));

    }



    static void optic(){

        Option<JsObj, String> nameOpt = JsObj.lens.value("name").compose(JsStr.prism);

        Option<JsObj, Integer> ageOpt = JsObj.lens.value("age").compose(JsInt.prism);

    }



}
