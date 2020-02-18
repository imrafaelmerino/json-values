package jsonvalues.specifications

import jsonvalues._
import org.scalacheck.Prop.forAll
import org.scalatest.PropSpec
import org.scalatestplus.scalacheck.Checkers

class HashCodeEqualsSpec extends PropSpec with Checkers
{
  val jsGen = JsElemGens(freqTypeOfPair = FreqTypeOfPair(arrFreq = 0,objFreq = 0))
  val jsPathGen = JsPathGens()
  val jsPairGen = JsPairGens()


  property("object collector reduces an stream_ back to the same object")
  {
    check(forAll(jsGen.jsObjGen)
          { json =>
            val obj = JsObj.parse(json.toString)
            val h1 = json.hashCode()
            val h2 = obj.hashCode()
            h1==h2
          }
         )
  }

}
