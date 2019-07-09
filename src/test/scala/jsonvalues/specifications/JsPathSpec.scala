package jsonvalues.specifications

import jsonvalues.JsPath
import org.scalacheck.Prop.forAll

class JsPathSpec extends BasePropSpec
{

  property("path toStr ")
  {
    check(forAll(jsPathGen.pathGen)
          { path =>
            path.equals(JsPath.of(path.toString)) && path.hashCode() == JsPath.of(path.toString).hashCode()
          }
          )
  }

  property("path compare == 0")
  {
    check(forAll(jsPathGen.pathGen
                 )
          {
            path => path.compareTo(path) == 0
          }
          )
  }


}
