package jsonvalues.specifications.mutable.jsobject

import java.util.Optional
import java.util.function.Predicate

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class FactoryMethodsSpec extends BasePropSpec
{


  property("of factory methods")
  {
    check(forAll(jsPairGen.pairOfValueGen)
          { p =>
            JsObj._of_("a",
                       p.elem
                       ).size() == 1 &&
            JsObj._of_("a",
                       p.elem,
                       "x",
                       p.elem
                       ).size() == 2 &&
            JsObj._of_("a",
                       p.elem,
                       "x",
                       p.elem,
                       "c",
                       p.elem
                       ).size() == 3 &&
            JsObj._of_("a",
                       p.elem,
                       "x",
                       p.elem,
                       "c",
                       p.elem,
                       "d",
                       p.elem
                       ).size() == 4 &&
            JsObj._of_("a",
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
            JsObj._of_("a",
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
    check(forAll(jsGen._jsObjGen_)
          { js =>
            val obj = JsObj._parse_(js.toString).orElseThrow()
            obj.equals(js)
          }
          )
  }

  property("json object parser: mapping keys")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = JsObj._parse_(js.toString,
                                       ParseOptions.builder().withKeyMap(it => it + "!")
                                       )
            val allKeysEndsWithExclamation: Predicate[_ >: JsPair] = p => p.path.stream().filter(pos => pos.isKey).allMatch(pos => pos.asKey().name.endsWith("!"))
            parsed.orElseThrow().stream_().allMatch(allKeysEndsWithExclamation)
          }
          )
  }

  //keyValueFilter doesn't change the structure of the json
  property("json object parser: filtering all x keys")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = JsObj._parse_(js.toString,
                                       ParseOptions.builder().withElemFilter(_ => false)
                                       )
            parsed.orElseThrow().stream_().filter(p => p.elem.isNotJson).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: ignoring null")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = JsObj._parse_(js.toString,
                                       ParseOptions.builder().withElemFilter(p => p.elem.isNotNull)
                                       )

            parsed.orElseThrow().stream_().filter(p => p.elem.isNull).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: filtering string values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val parsed = JsObj._parse_(js.toString,
                                       ParseOptions.builder().withElemFilter(p=> !p.elem.isStr)
                                       )

            parsed.orElseThrow().stream_().filter(p=>p.elem.isStr).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: filtering number values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val parsed = JsObj._parse_(js.toString,
                                       ParseOptions.builder().withElemFilter(p=> p.elem.isNotNumber)
                                       )

            parsed.orElseThrow().stream_().filter(p=> p.elem.isNumber).findFirst().equals(Optional.empty)
          }
          )
  }

  property("json object parser: mapping x values")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val parsed = JsObj._parse_(js.toString,
                                       ParseOptions.builder().withElemMap(Utils.mapIfStr(_ => "hi").andThen(_.elem))
                                       )

            parsed.orElseThrow().stream_().filter(p=> p.elem.isStr).allMatch(p=> p.elem.isStr(s => s.equals("hi")))
          }
          )
  }


}
