package jsonvalues.specifications

import jsonvalues.JsPair
import org.scalacheck.Prop.forAll

class JsPairSpec extends BasePropSpec
{

  property("appends elements to the back after creating a new array in an empty object")
  {
    check(forAll(jsPairGen.pairGen)
          { a =>
            val b = JsPair.of(a.path.toString,
                              a.elem
                              )
            a.equals(b
                     ) &&
            a.hashCode().equals(b.hashCode())
          }
          )
  }


}
