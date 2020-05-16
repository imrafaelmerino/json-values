package jsonvalues.specifications.immutable.jsarray


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


}
