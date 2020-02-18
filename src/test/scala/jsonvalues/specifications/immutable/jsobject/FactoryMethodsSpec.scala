package jsonvalues.specifications.immutable.jsobject


import java.util.Optional
import java.util.function.Predicate

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class FactoryMethodsSpec extends BasePropSpec
{


  property("parse factory methods")
  {
    check(forAll(jsPairGen.pairGen)
          { p =>
            Jsons.immutable.`object`
              .of("a",
                  p.elem
                  ).size() == 1 &&
            Jsons.immutable.`object`.of("a",
                                        p.elem,
                                        "x",
                                        p.elem
                                        ).size() == 2 &&
            Jsons.immutable.`object`.of("a",
                                        p.elem,
                                        "x",
                                        p.elem,
                                        "c",
                                        p.elem
                                        ).size() == 3 &&
            Jsons.immutable.`object`.of("a",
                                        p.elem,
                                        "x",
                                        p.elem,
                                        "c",
                                        p.elem,
                                        "d",
                                        p.elem
                                        ).size() == 4 &&
            Jsons.immutable.`object`.of("a",
                                        p.elem,
                                        "x",
                                        p.elem,
                                        "c",
                                        p.elem,
                                        "d",
                                        p.elem,
                                        "e",
                                        p.elem
                                        ).size() == 5 &&
            Jsons.immutable.`object`.of("a",
                                        p.elem,
                                        "x",
                                        p.elem,
                                        "c",
                                        p.elem,
                                        "d",
                                        p.elem,
                                        "e",
                                        p.elem,
                                        "f",
                                        p.elem
                                        ).size() == 6


          }
          )
  }

  property("json object parser")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val obj = Jsons.immutable.
              `object`
              .parse(js.toString).get()
            obj.equals(js) && obj.hashCode() == js.hashCode()
          }
          )
  }

  property("json object parser: mapping keys")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = Jsons.immutable.
              `object`
              .parse(js.toString,
                     ParseBuilder.builder().withKeyMap(it => it + "!")
                     )
            val allKeysEndsWithExclamation: Predicate[_ >: JsPair] = p => p.path.stream().filter(pos => pos.isKey).allMatch(pos => pos.asKey().name.endsWith("!"))
            parsed.get().stream_().allMatch(allKeysEndsWithExclamation)
          }
          )
  }

  property("json object parser: filtering all x keys")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = Jsons.immutable.
              `object`
              .parse(js.toString,
                     ParseBuilder.builder().withElemFilter(_ => false)
                     )
            parsed.get().stream_().filter(p => p.elem.isNotJson).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: ignoring null")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = Jsons.immutable.
              `object`
              .parse(js.toString,
                     ParseBuilder.builder().withElemFilter(p => p.elem.isNotNull)
                     )

            parsed.get().stream_().filter(p => p.elem.isNull).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: filtering string values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val parsed = Jsons.immutable.
              `object`
              .parse(js.toString,
                     ParseBuilder.builder().withElemFilter(p => !p.elem.isStr)
                     )

            parsed.get().stream_().filter(p => p.elem.isStr).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: filtering number values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val predicate: Predicate[JsPair] = (p: JsPair) => p.elem.isNotNumber

            val parsed = Jsons.immutable.
              `object`
              .parse(js.toString,
                     ParseBuilder.builder().withElemFilter(predicate)
                     )

            parsed.get().stream_().filter(p => p.elem.isNumber).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json `object` parser: mapping x values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val parsed = Jsons.immutable.
              `object`
              .parse(js.toString,
                     ParseBuilder.builder().withElemMap(p => JsElems.mapIfStr(_ => "hi")(p.elem))
                     )

            parsed.get().stream_().filter(p => p.elem.isStr).allMatch(p => p.elem.isStr(s => s.equals("hi")))
          }
          )
  }

}
