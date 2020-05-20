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


}
