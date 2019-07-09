package jsonvalues.specifications.immutable.array

import jsonvalues.JsArray.TYPE
import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class SetTheoryOpsSpec extends BasePropSpec
{


  property("union parse an array with itself returns the same array (TYPE.SET)")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.union(js, TYPE.SET).equals(js) && js.union(js, TYPE.SET).hashCode() == js.hashCode()
          }
         )
  }

  property("union parse an array with itself returns the same array (TYPE.LIST)")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.union(js, TYPE.LIST).equals(js) && js.union(js, TYPE.LIST).hashCode() == js.hashCode()
          }
         )
  }

  property("union parse an array with itself returns the same array duplicated (TYPE.MULTISET)")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.union(js, TYPE.MULTISET).size() == 2 * js.size()
          }
         )
  }

  property("union_ parse an array with itself itself returns the same array (TYPE.SET)")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.union_(js, TYPE.SET).equals(js) && js.union_(js, TYPE.SET).hashCode() == js.hashCode()
          }
         )
  }

  property("union_ parse an array with itself returns the same array (TYPE.LIST)")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.union_(js, TYPE.LIST).equals(js) && js.union_(js, TYPE.LIST).hashCode() == js.hashCode()
          }
         )
  }


  property("union_ parse an array with itself returns the same array duplicated (TYPE.MULTISET)")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.union_(js, TYPE.MULTISET).size() == 2 * js.size()
          }
         )
  }

  property("intersection parse an array with itself returns the same array (MULTISET AND LIST)")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.intersection(js, TYPE.LIST).equals(js) &&
            js.intersection(js, TYPE.MULTISET).equals(js)
          }
         )
  }
//
  property("union parse an array A with an empty array, returns A")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.union(JsArray.empty(), TYPE.SET).equals(js) && js.union_(JsArray.empty(), TYPE.SET).equals(js) &&
            js.union(JsArray.empty(), TYPE.LIST).equals(js) && js.union_(JsArray.empty(), TYPE.LIST).equals(js) &&
            js.union(JsArray.empty(), TYPE.MULTISET).equals(js) && js.union_(JsArray.empty(), TYPE.MULTISET).equals(js)
          }
         )
  }
//
  property("array union is commutative (MULTISET AND LIST)")
  {
    check(forAll(jsGen.jsArrGen,
                 jsGen.jsArrGen
                )
          { (a,
             b
            ) =>
            a.union(b, TYPE.MULTISET).size() == b.union(a, TYPE.MULTISET).size() &&
            a.union(b, TYPE.LIST).size() == b.union(a, TYPE.LIST).size()
          }
         )
  }
//
  property("array union_ is commutative (MULTISET AND LIST)")
  {
    check(forAll(jsGen.jsArrGen,
                 jsGen.jsArrGen
                )
          { (a,
             b
            ) =>
            a.union_(b, TYPE.MULTISET).size() == b.union_(a, TYPE.MULTISET).size() &&
            a.union_(b, TYPE.LIST).size() == b.union_(a, TYPE.LIST).size()


          }
         )
  }
  property("arrays intersection is commutative (ORDERED LIST)")
  {
    check(forAll(jsGen.jsArrGen,
                 jsGen.jsArrGen
                )
          { (a,
             b
            ) =>
            a.intersection(b, TYPE.LIST).equals(b.intersection(a, TYPE.LIST))
          }
         )
  }

  property("arrays intersection_ is commutative (ORDERED LIST)")
  {
    check(forAll(jsGen.jsArrGen,
                 jsGen.jsArrGen
                )
          { (a,
             b
            ) =>
            a.intersection_(b).equals(b.intersection_(a))
          }
         )
  }


  property("intersection parse an array with an empty array returns an empty array")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.intersection(JsArray.empty(), TYPE.LIST).equals(JsArray.empty()) &&
            js.intersection(JsArray.empty(), TYPE.SET).equals(JsArray.empty()) &&
            js.intersection(JsArray.empty(), TYPE.LIST).equals(JsArray.empty())
          }
         )
  }

  property("intersection_ parse an array with an empty array returns an empty array")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            js.intersection_(JsArray.empty()).equals(JsArray.empty())
          }
         )
  }


}
