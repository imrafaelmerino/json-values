package jsonvalues;

import jsonvalues.console.JsArrayIO;
import jsonvalues.console.JsIOs;
import jsonvalues.console.JsObjIO;
import jsonvalues.future.JsArrayFuture;
import jsonvalues.future.JsObjFuture;
import jsonvalues.gen.JsGens;
import jsonvalues.gen.JsObjGen;
import jsonvalues.spec.JsObjParser;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class TestExample {

    @Test
    public void test() {

        JsObjSpec spec = JsObjSpec.strict("name",
                                          JsSpecs.str,
                                          "surname",
                                          JsSpecs.str.optional(),
                                          "languages",
                                          JsSpecs.arrayOfStr,
                                          "age",
                                          JsSpecs.integer.optional(),
                                          "address",
                                          JsObjSpec.strict("street",
                                                           JsSpecs.str,
                                                           "number",
                                                           JsSpecs.any(it -> it.isInt() || it.isStr()),
                                                           "city",
                                                           JsSpecs.str.nullable(),
                                                           "coordinates",
                                                           JsSpecs.tuple(JsSpecs.decimal,
                                                                         JsSpecs.any,
                                                                         JsSpecs.decimal
                                                                        )
                                                          )
                                         );

        JsObj person = JsObj.of("name",
                                JsStr.of("Rafael"),
                                "surname",
                                JsStr.of("Merino García"),
                                "languages",
                                JsArray.of("Haskell",
                                           "Java",
                                           "Clojure",
                                           "Scala",
                                           "Erlang",
                                           "Prolog"),
                                "age",
                                JsInt.of(37),
                                "address",
                                JsObj.of("street",
                                         JsStr.of("Elm Street"),
                                         "number",
                                         JsInt.of(12),
                                         "city",
                                         JsStr.of("Madrid"),
                                         "coordinates",
                                         JsArray.of(45.9,
                                                    18.6
                                                   )
                                        )
                               );

        JsObjParser parser = new JsObjParser(spec);

        Assertions.assertTrue(spec.test(person)
                                  .isEmpty());

        Assertions.assertEquals(person,
                                parser.parse(person.toPrettyString()));


        JsStrLens<JsObj>       nameLens   = JsObj.optics.lens.str("name");

        Option<JsObj, String>  surnameOpt = JsObj.optics.optional.str("surname");

        Option<JsObj, Integer> ageOpt     = JsObj.optics.optional.intNum("age");
        JsStrLens<JsObj>       streetLens = JsObj.optics.lens.str(JsPath.path("/address/street"));
        JsValueLens<JsObj>     cityLens   = JsObj.optics.lens.value(JsPath.path("/address/city"));

        JsArrayLens<JsObj> languagesLens = JsObj.optics.lens.array("languages");

        JsValueLens<JsObj> numberLens = JsObj.optics.lens.value(JsPath.path("/address/number"));

        JsDoubleLens<JsObj> latitudeLens = JsObj.optics.lens.doubleNum(JsPath.path("/address/coordinates/0"));

        JsDoubleLens<JsObj> longitudeLens = JsObj.optics.lens.doubleNum(JsPath.path("/address/coordinates/1"));

        IntFunction<Function<JsObj, JsObj>> incAge = n -> ageOpt.modify.apply(i -> i + n);

        Function<JsObj, JsObj> surnameToUpperCase = surnameOpt.modify.apply(String::toUpperCase);

        Function<JsObj, JsObj> nameToUppercase = nameLens.modify.apply(String::toUpperCase);

        Function<String, Function<JsObj, JsObj>> addLanguage = language -> languagesLens.modify.apply(a -> a.append(JsStr.of(language)));

        Function<JsObj, JsObj> modifyPerson = incAge.apply(1)
                                                    .andThen(nameToUppercase)
                                                    .andThen(surnameToUpperCase)
                                                    .andThen(streetLens.set.apply("Las cruces"))
                                                    .andThen(numberLens.set.apply(JsStr.of("034CF")))
                                                    .andThen(cityLens.modify.apply(it -> it.ifNull(JsStr.of("Madrid"))))
                                                    .andThen(latitudeLens.modify.apply(it -> it + 2.0))
                                                    .andThen(longitudeLens.modify.apply(it -> it + 2.0))
                                                    .andThen(addLanguage.apply("Lisp"));

        JsObj newPerson = modifyPerson.apply(person);


        System.out.println(newPerson);
        Assertions.assertEquals(ageOpt.get.apply(newPerson),
                                ageOpt.get.apply(person)
                                          .map(i -> i + 1));


        Assertions.assertEquals(nameLens.get.apply(newPerson),"RAFAEL");
        Assertions.assertEquals(surnameOpt.get.apply(newPerson),
                                Optional.ofNullable("MERINO GARCÍA"));


        Assertions.assertEquals(latitudeLens.get.apply(newPerson),Double.valueOf(47.9));
        Assertions.assertEquals(longitudeLens.get.apply(newPerson),Double.valueOf(20.6));

        Assertions.assertEquals(cityLens.get.apply(newPerson),
                                JsStr.of("Madrid"));


        Assertions.assertEquals(streetLens.get.apply(newPerson),"Las cruces"
                                );


        Assertions.assertEquals(numberLens.get.apply(newPerson),
                                JsStr.of("034CF")
                               );
    }

    @Test
    public void test_gen() {
        JsObjGen gen = JsObjGen.of("name",
                                   JsGens.alphabetic,
                                   "surname",
                                   JsGens.alphabetic.optional(),
                                   "age",
                                   JsGens.choose(16,
                                                 100
                                                )
                                         .optional(),
                                   "address",
                                   JsObjGen.of("street",
                                               JsGens.alphabetic,
                                               "number",
                                               JsGens.oneOf(JsGens.choose(0,
                                                                          1000
                                                                         ),
                                                            JsGens.alphanumeric
                                                           ),
                                               "city",
                                               JsGens.alphabetic.nullable(),
                                               "coordinates",
                                               JsGens.tuple(JsGens.decimal,
                                                            JsGens.decimal
                                                           )
                                              )
                                  );

        JsObjFuture future = JsObjFuture.of("name",
                                            () -> completedFuture(JsStr.of("a")),
                                            "surname",
                                            () -> completedFuture(JsObj.empty()),
                                            "age",
                                            () -> completedFuture(JsInt.of(1)),
                                            "address",
                                            JsObjFuture.of("street",
                                                           () -> completedFuture(JsStr.of("b")),
                                                           "number",
                                                           () -> completedFuture(JsStr.of("1")),
                                                           "coordinates",
                                                           JsArrayFuture.of(() -> completedFuture(JsDouble.of(1.5)),
                                                                            () -> completedFuture(JsDouble.of(1.5))
                                                                           )
                                                          )
                                           );

        JsObjIO consoleIO = JsObjIO.of("name",
                                       JsIOs.read(JsSpecs.str),
                                       "surname",
                                       JsIOs.read(JsSpecs.str),
                                       "age",
                                       JsIOs.read(JsSpecs.integer),
                                       "address",
                                       JsObjIO.of("street",
                                                  JsIOs.read(JsSpecs.str),
                                                  "number",
                                                  JsIOs.read(JsSpecs.str),
                                                  "coordinates",
                                                  JsArrayIO.of(JsIOs.read(JsSpecs.decimal),
                                                               JsIOs.read(JsSpecs.decimal)
                                                              )
                                                 )
                                      );

    }


    public static void main(String[] args) {




        System.out.println(JsStr.prism.modify(String::toLowerCase)
                                      .apply(JsInt.of(1)));

    }
}
