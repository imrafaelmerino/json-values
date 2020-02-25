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
            JsObj
              .of("a",
                  p.value
                  ).size() == 1 &&
            JsObj.of("a",
                     p.value,
                     "x",
                     p.value
                     ).size() == 2 &&
            JsObj.of("a",
                     p.value,
                     "x",
                     p.value,
                     "c",
                     p.value
                     ).size() == 3 &&
            JsObj.of("a",
                     p.value,
                     "x",
                     p.value,
                     "c",
                     p.value,
                     "d",
                     p.value
                     ).size() == 4 &&
            JsObj.of("a",
                     p.value,
                     "x",
                     p.value,
                     "c",
                     p.value,
                     "d",
                     p.value,
                     "e",
                     p.value
                     ).size() == 5 &&
            JsObj.of("a",
                     p.value,
                     "x",
                     p.value,
                     "c",
                     p.value,
                     "d",
                     p.value,
                     "e",
                     p.value,
                     "f",
                     p.value
                     ).size() == 6


          }
          )
  }

  property("json object parser")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val obj = JsObj.parse(js.toString)

            obj.equals(js) && obj.hashCode() == js.hashCode()
          }
          )
  }

  property("json object parser: mapping keys")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = JsObj.parse(js.toString,
                                     ParseBuilder.builder().withKeyMap(it => it + "!")
                                     )
            val allKeysEndsWithExclamation: Predicate[_ >: JsPair] = p => p.path.stream().filter(pos => pos.isKey).allMatch(pos => pos.asKey().name.endsWith("!"))
            parsed.streamAll().allMatch(allKeysEndsWithExclamation)
          }
          )
  }

  property("json object parser: filtering all x keys")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = JsObj.parse(js.toString,
                                     ParseBuilder.builder().withElemFilter(_ => false)
                                     )
            parsed.streamAll().filter(p => p.value.isNotJson).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: ignoring null")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = JsObj
              .parse(js.toString,
                     ParseBuilder.builder().withElemFilter(p => p.value.isNotNull)
                     )

            parsed.streamAll().filter(p => p.value.isNull).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: filtering string values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val parsed = JsObj.parse(js.toString,
                                     ParseBuilder.builder().withElemFilter(p => !p.value.isStr)
                                     )

            parsed.streamAll().filter(p => p.value.isStr).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: filtering number values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val predicate: Predicate[JsPair] = (p: JsPair) => p.value.isNotNumber

            val parsed = JsObj.parse(js.toString,
                                     ParseBuilder.builder().withElemFilter(predicate)
                                     )

            parsed.streamAll().filter(p => p.value.isNumber).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: mapping x values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val parsed = JsObj.parse(js.toString,
                                     ParseBuilder.builder().withElemMap(p => JsElems.mapIfStr(_ => "hi")(p.value))
                                     )

            parsed.streamAll().filter(p => p.value.isStr).allMatch(p => p.value.isStr(s => s.equals("hi")))
          }
          )
  }

}
