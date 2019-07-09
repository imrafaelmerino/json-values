package jsonvalues.specifications

import jsonvalues.{JsElemGens, JsPairGens, JsPathGens}
import org.scalatest.PropSpec
import org.scalatestplus.scalacheck.Checkers

class BasePropSpec extends PropSpec with Checkers
{

  val jsGen = JsElemGens()
  val jsPathGen = JsPathGens()
  val jsPairGen = JsPairGens()

  implicit override val generatorDrivenConfig = PropertyCheckConfiguration(minSuccessful = 100,
                                                                           workers = 1

                                                                           )
}
