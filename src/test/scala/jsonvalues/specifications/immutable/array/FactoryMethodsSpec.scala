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
            JsArray.of(p.value).size() == 1 &&
            JsArray.of(p.value,
                                     p.value
                                     ).size() == 2 &&
            JsArray.of(p.value,
                                     p.value,
                                     p.value
                                     ).size() == 3 &&
            JsArray.of(p.value,
                                     p.value,
                                     p.value,
                                     p.value
                                     ).size() == 4 &&
            JsArray.of(p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value
                                     ).size() == 5 &&
            JsArray.of(p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value
                                     ).size() == 6 &&
            JsArray.of(p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value
                                     ).size() == 7 &&
            JsArray.of(p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value
                                     ).size() == 8 &&
            JsArray.of(p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value,
                                     p.value
                                     ).size() == 9
          }
          )
  }

  property("json array parser")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            JsArray.parse(js.toString).equals(js)
          }
          )
  }

  property("json array parser: mapping keys")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = JsArray.parse(js.toString,
                                                     ParseBuilder.builder().withKeyMap(it => it + "!")
                                                     )
            val allKeysEndsWithExclamation: Predicate[_ >: JsPair] = p => p.path.stream().filter(pos => pos.isKey).allMatch(pos => pos.asKey().name.endsWith("!"))
            parsed.streamAll().allMatch(allKeysEndsWithExclamation)
          }
          )
  }


  property("json array parser: filwithElemFiltertering all obj elements")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>

            val predicate: JsPair => Boolean = (pair: JsPair) => pair.path.last().isKey
            val parsed = JsArray.parse(js.toString,
                                                     ParseBuilder.builder().withElemFilter(ScalaToJava.predicate(predicate))
                                                     )
            parsed.streamAll().filter(p => p.value.isNotJson && p.path.last().isIndex).findFirst().equals(Optional.empty)

          }
          )
  }

  property("json array parser: ignoring null")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = JsArray.parse(js.toString,
                                                     ParseBuilder.builder().withElemFilter(p => p.value.isNotNull)
                                                     )

            val value = parsed.streamAll().filter(p => p.value.isNull).findFirst()

            value.equals(Optional.empty)
          }

          )
  }

  property("json array parser: filtertering string values")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = JsArray.parse(js.toString,
                                                     ParseBuilder.builder().withElemFilter(p => !p.value.isStr
                                                                                           )
                                                     )

            parsed.streamAll().filter(p => p.value.isStr).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json array parser: filwithElemFiltertering number values")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>

            val predicate: Predicate[JsPair] = (pair: JsPair) => pair.value.isNotNumber
            val parsed = JsArray.parse(js.toString,
                                                     ParseBuilder.builder().withElemFilter(predicate)
                                                     )

            parsed.streamAll().filter(p => p.value.isNumber).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json array parser: mapping x values")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = JsArray.parse(js.toString,
                                                     ParseBuilder.builder().withElemMap(p => JsElems.mapIfStr(_ => "hi").apply(p.value))
                                                     )

            parsed.streamAll().filter(p => p.value.isStr).allMatch(p => p.value.isStr(a => a.equals("hi")))
          }
          )
  }


}
