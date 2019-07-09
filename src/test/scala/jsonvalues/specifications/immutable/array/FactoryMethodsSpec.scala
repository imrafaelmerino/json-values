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
            JsArray.of(p.elem).size() == 1 &&
            JsArray.of(p.elem,
                       p.elem
                       ).size() == 2 &&
            JsArray.of(p.elem,
                       p.elem,
                       p.elem
                       ).size() == 3 &&
            JsArray.of(p.elem,
                       p.elem,
                       p.elem,
                       p.elem
                       ).size() == 4 &&
            JsArray.of(p.elem,
                       p.elem,
                       p.elem,
                       p.elem,
                       p.elem
                       ).size() == 5 &&
            JsArray.of(p.elem,
                       p.elem,
                       p.elem,
                       p.elem,
                       p.elem,
                       p.elem
                       ).size() == 6 &&
            JsArray.of(p.elem,
                       p.elem,
                       p.elem,
                       p.elem,
                       p.elem,
                       p.elem,
                       p.elem
                       ).size() == 7 &&
            JsArray.of(p.elem,
                       p.elem,
                       p.elem,
                       p.elem,
                       p.elem,
                       p.elem,
                       p.elem,
                       p.elem
                       ).size() == 8 &&
            JsArray.of(p.elem,
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
            JsArray.parse(js.toString).orElseThrow().equals(js)
          }
          )
  }

  property("json array parser: mapping keys")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = JsArray.parse(js.toString,
                                       ParseOptions.builder().withKeyMap(it => it + "!")
                                       )
            val allKeysEndsWithExclamation: Predicate[_ >: JsPair] = p => p.path.stream().filter(pos => pos.isKey).allMatch(pos => pos.asKey().name.endsWith("!"))
            parsed.orElseThrow().stream_().allMatch(allKeysEndsWithExclamation)
          }
          )
  }


  property("json array parser: filwithElemFiltertering all obj elements")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>

            val predicate: JsPair => Boolean = (pair: JsPair) => pair.path.last().isKey
            val parsed = JsArray.parse(js.toString,
                                       ParseOptions.builder().withElemFilter(ScalaToJava.predicate(predicate))
                                       )
            parsed.orElseThrow().stream_().filter(p => p.elem.isNotJson && p.path.last().isIndex).findFirst().equals(Optional.empty)

          }
          )
  }

  property("json array parser: ignoring null")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = JsArray.parse(js.toString,
                                       ParseOptions.builder().withElemFilter(p => p.elem.isNotNull)
                                       )

            val value = parsed.orElseThrow().stream_().filter(p => p.elem.isNull).findFirst()

            value.equals(Optional.empty)
          }

          )
  }

  property("json array parser: filtertering string values")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = JsArray.parse(js.toString,
                                       ParseOptions.builder().withElemFilter(p => !p.elem.isStr
                                                                             )
                                       )

            parsed.orElseThrow().stream_().filter(p => p.elem.isStr).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json array parser: filwithElemFiltertering number values")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>

            val predicate: Predicate[JsPair] = (pair: JsPair) => pair.elem.isNotNumber
            val parsed = JsArray.parse(js.toString,
                                       ParseOptions.builder().withElemFilter(predicate)
                                       )

            parsed.orElseThrow().stream_().filter(p => p.elem.isNumber).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json array parser: mapping x values")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            val parsed = JsArray.parse(js.toString,
                                       ParseOptions.builder().withElemMap(p => JsElems.mapIfStr(_ => "hi").apply(p.elem))
                                       )

            parsed.orElseThrow().stream_().filter(p => p.elem.isStr).allMatch(p => p.elem.isStr(a => a.equals("hi")))
          }
          )
  }


}
