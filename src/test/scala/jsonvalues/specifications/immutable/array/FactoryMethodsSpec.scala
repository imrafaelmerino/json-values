package jsonvalues.specifications.immutable.array

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
            Jsons.immutable.array.of(p.elem).size() == 1 &&
            Jsons.immutable.array.of(p.elem,
                                     p.elem
                                     ).size() == 2 &&
            Jsons.immutable.array.of(p.elem,
                                     p.elem,
                                     p.elem
                                     ).size() == 3 &&
            Jsons.immutable.array.of(p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem
                                     ).size() == 4 &&
            Jsons.immutable.array.of(p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem
                                     ).size() == 5 &&
            Jsons.immutable.array.of(p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem
                                     ).size() == 6 &&
            Jsons.immutable.array.of(p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem
                                     ).size() == 7 &&
            Jsons.immutable.array.of(p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem
                                     ).size() == 8 &&
            Jsons.immutable.array.of(p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem,
                                     p.elem
                                     ).size() == 9
          }
          )
  }

  property("json array parser")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            Jsons.immutable.array.parse(js.toString).get().equals(js)
          }
          )
  }

  property("json array parser: mapping keys")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = Jsons.immutable.array.parse(js.toString,
                                                     ParseBuilder.builder().withKeyMap(it => it + "!")
                                                     )
            val allKeysEndsWithExclamation: Predicate[_ >: JsPair] = p => p.path.stream().filter(pos => pos.isKey).allMatch(pos => pos.asKey().name.endsWith("!"))
            parsed.get().stream_().allMatch(allKeysEndsWithExclamation)
          }
          )
  }


  property("json array parser: filwithElemFiltertering all obj elements")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>

            val predicate: JsPair => Boolean = (pair: JsPair) => pair.path.last().isKey
            val parsed = Jsons.immutable.array.parse(js.toString,
                                                     ParseBuilder.builder().withElemFilter(ScalaToJava.predicate(predicate))
                                                     )
            parsed.get().stream_().filter(p => p.elem.isNotJson && p.path.last().isIndex).findFirst().equals(Optional.empty)

          }
          )
  }

  property("json array parser: ignoring null")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = Jsons.immutable.array.parse(js.toString,
                                                     ParseBuilder.builder().withElemFilter(p => p.elem.isNotNull)
                                                     )

            val value = parsed.get().stream_().filter(p => p.elem.isNull).findFirst()

            value.equals(Optional.empty)
          }

          )
  }

  property("json array parser: filtertering string values")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = Jsons.immutable.array.parse(js.toString,
                                                     ParseBuilder.builder().withElemFilter(p => !p.elem.isStr
                                                                                           )
                                                     )

            parsed.get().stream_().filter(p => p.elem.isStr).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json array parser: filwithElemFiltertering number values")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>

            val predicate: Predicate[JsPair] = (pair: JsPair) => pair.elem.isNotNumber
            val parsed = Jsons.immutable.array.parse(js.toString,
                                                     ParseBuilder.builder().withElemFilter(predicate)
                                                     )

            parsed.get().stream_().filter(p => p.elem.isNumber).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json array parser: mapping x values")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = Jsons.immutable.array.parse(js.toString,
                                                     ParseBuilder.builder().withElemMap(p => JsElems.mapIfStr(_ => "hi").apply(p.elem))
                                                     )

            parsed.get().stream_().filter(p => p.elem.isStr).allMatch(p => p.elem.isStr(a => a.equals("hi")))
          }
          )
  }


}
