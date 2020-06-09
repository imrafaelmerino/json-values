package jsonvalues;

import jsonvalues.console.JsArrayConsole;
import jsonvalues.console.JsIOs;
import jsonvalues.console.JsObjConsole;
import jsonvalues.gen.JsGens;
import jsonvalues.gen.JsObjGen;
import jsonvalues.spec.JsErrorPair;
import jsonvalues.spec.JsObjParser;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.IntFunction;

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
                                                                         JsSpecs.decimal
                                                                        )
                                                          )
                                         );

        JsObj person = JsObj.of("name", JsStr.of("Rafael"),
                                "surname", JsStr.of("Merino García"),
                                "languages", JsArray.of("Java", "Clojure", "Scala"),
                                "age", JsInt.of(37),
                                "address", JsObj.of("street",
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

        Set<JsErrorPair> test = spec.test(person);
        Assertions.assertTrue(test
                                  .isEmpty());

        Assertions.assertEquals(person,
                                parser.parse(person.toPrettyString()));


        JsStrLens<JsObj>       nameLens   = JsObj.lens.str("name");

        Option<JsObj, String>  surnameOpt = JsObj.optional.str("surname");

        Option<JsObj, Integer> ageOpt     = JsObj.optional.intNum("age");
        JsStrLens<JsObj>       streetLens = JsObj.lens.str(JsPath.path("/address/street"));
        JsValueLens<JsObj>     cityLens   = JsObj.lens.value(JsPath.path("/address/city"));

        JsArrayLens<JsObj> languagesLens = JsObj.lens.array("languages");

        JsValueLens<JsObj> numberLens = JsObj.lens.value(JsPath.path("/address/number"));

        JsDoubleLens<JsObj> latitudeLens = JsObj.lens.doubleNum(JsPath.path("/address/coordinates/0"));

        JsDoubleLens<JsObj> longitudeLens = JsObj.lens.doubleNum(JsPath.path("/address/coordinates/1"));

        IntFunction<Function<JsObj, JsObj>> incAge = n -> ageOpt.modify.apply(i -> i + n);

        Function<JsObj, JsObj> surnameToUpperCase = surnameOpt.modify.apply(String::toUpperCase);

        Function<JsObj, JsObj> nameToUppercase = nameLens.modify.apply(String::toUpperCase);

        Function<String, Function<JsObj, JsObj>> addLanguage =
                language -> languagesLens.modify.apply(a -> a.append(JsStr.of(language)));

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
                                   "languages",
                                   JsGens.choose(1,10).flatMap(n -> JsGens.array(JsGens.alphabetic, n.value)),
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



        JsObjConsole consoleIO = JsObjConsole.of("name",
                                                 JsIOs.read(JsSpecs.str),
                                                 "surname",
                                                 JsIOs.read(JsSpecs.str),
                                                 "age",
                                                 JsIOs.read(JsSpecs.integer),
                                                 "address",
                                                 JsObjConsole.of("street",
                                                                 JsIOs.read(JsSpecs.str),
                                                                 "number",
                                                                 JsIOs.read(JsSpecs.str),
                                                                 "coordinates",
                                                                 JsArrayConsole.tuple(JsIOs.read(JsSpecs.decimal),
                                                                                      JsIOs.read(JsSpecs.decimal)
                                                                                     )
                                                                )
                                                );

    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {




        JsObjConsole consoleIO = JsObjConsole.of("name",
                                                 JsIOs.read(JsSpecs.str),
                                                 "surname",
                                                 JsIOs.read(JsSpecs.str),
                                                 "age",
                                                 JsIOs.read(JsSpecs.integer),
                                                 "address",
                                                 JsObjConsole.of("street",
                                                                 JsIOs.read(JsSpecs.str),
                                                                 "number",
                                                                 JsIOs.read(JsSpecs.str),
                                                                 "coordinates",
                                                                 JsArrayConsole.tuple(JsIOs.read(JsSpecs.decimal),
                                                                                      JsIOs.read(JsSpecs.decimal)
                                                                                     )
                                                                )
                                                );

        System.out.println(consoleIO.exec());

    }
}
