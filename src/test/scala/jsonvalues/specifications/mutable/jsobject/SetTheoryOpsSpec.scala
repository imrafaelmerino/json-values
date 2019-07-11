package jsonvalues.specifications.mutable.jsobject

import jsonvalues.JsArray.TYPE
import jsonvalues.JsArray.TYPE.SET
import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class SetTheoryOpsSpec extends BasePropSpec
{



  property("union of an object with itself returns the same object")
  {
    check(forAll(jsGen._jsObjGen_)
          { js =>
            js.union(js).equals(js) && js.union(js).hashCode() == js.hashCode()
          }
         )
  }

  property("union_ of an object with itself itself returns the same object")
  {
    check(forAll(jsGen._jsObjGen_)
          { js =>
            js.union_(js, SET).equals(js) && js.union_(js, SET).hashCode() == js.hashCode()
          }
         )
  }

  property("intersection of an object with itself returns the same object")
  {
    check(forAll(jsGen._jsObjGen_)
          { js =>
            js.intersection(js, TYPE.LIST).equals(js) && js.intersection(js, TYPE.LIST).hashCode() == js.hashCode()
          }
         )
  }
  //
  property("union of an object A with an empty object, returns A")
  {
    check(forAll(jsGen._jsObjGen_)
          { js =>
            js.union(JsObj.empty()).equals(js) && js.union(JsObj.empty()).equals(js)
          }
         )
  }
  //
  property("objects union is commutative (same fields but not necessarily same values)")
  {
    check(forAll(jsGen._jsObjGen_,
                 jsGen._jsObjGen_
                )
          { (a,
             b
            ) =>

            val aCopy = JsObj._parse_(a.toString).orElseThrow()
            val c = a.union(b)
            val d = b.union(aCopy)

            c.fields().containsAll(d.fields()) &&
            d.fields().containsAll(c.fields())

          }
         )
  }

  property("objects union_ is commutative (same fields but not necessarily same values)")
  {
    check(forAll(jsGen._jsObjGen_,
                 jsGen._jsObjGen_
                )
          { (a,
             b
            ) =>
            val aCopy = JsObj._parse_(a.toString).orElseThrow()
            val c = a.union_(b, SET)
            val d = b.union_(aCopy, SET)
            c.fields().containsAll(d.fields()) && d.fields().containsAll(c.fields())

          }
         )
  }

  property("objects intersection is commutative (same fields and values)")
  {
    check(forAll(jsGen._jsObjGen_,
                 jsGen._jsObjGen_
                )
          { (a,
             b
            ) =>
            val aCopy = JsObj._parse_(a.toString).orElseThrow()
            val c = a.intersection(b, TYPE.LIST)
            val d = b.intersection(aCopy, TYPE.LIST)
            c.equals(d)
          }
         )
  }

  property("objects intersection_ is commutative (same fields and values)")
  {
    check(forAll(jsGen._jsObjGen_,
                 jsGen._jsObjGen_
                )
          { (a,
             b
            ) =>
            a.intersection_(b, SET).equals(b.intersection_(a, SET), SET)
          }
         )
  }


  property("intersection of an object with an empty object returns an empty object")
  {
    check(forAll(jsGen._jsObjGen_)
          { js =>
            js.intersection(JsObj.empty(), TYPE.LIST).equals(JsObj.empty())
          }
         )
  }

  property("intersection_ of an object with an empty object returns an empty object")
  {
    check(forAll(jsGen._jsObjGen_)
          { js =>
            js.intersection_(JsObj.empty(), SET).equals(JsObj.empty())
          }
         )
  }


}
