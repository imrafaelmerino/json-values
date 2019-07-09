package jsonvalues.specifications.immutable.jsobject

import jsonvalues.JsArray.TYPE.SET
import jsonvalues.JsArray.TYPE
import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class SetTheoryOpsSpec extends BasePropSpec
{

  property("union parse an object with itself returns the same object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.union(js).equals(js) && js.union(js).hashCode() == js.hashCode()
          }
          )
  }

  property("union_ parse an object with itself itself returns the same object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.union_(js, SET).equals(js) && js.union_(js, SET).hashCode() == js.hashCode()
          }
          )
  }

  property("intersection parse an object with itself returns the same object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.intersection(js).equals(js) && js.intersection(js).hashCode() == js.hashCode()
          }
          )
  }
  //
  property("union parse an object A with an empty object, returns A")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.union(JsObj.empty()).equals(js) && js.union(JsObj.empty()).equals(js)
          }
          )
  }
  //
  property("objects union is commutative (same fields but not necessarily same values)")
  {
    check(forAll(jsGen.jsObjGen,
                 jsGen.jsObjGen
                 )
          { (a,
             b
            ) =>
            a.union(b).fields().containsAll(b.union(a).fields()) &&
            b.union(a).fields().containsAll(a.union(b).fields())

          }
          )
  }
  //
  property("objects union_ is commutative (same fields but not necessarily same values)")
  {
    check(forAll(jsGen.jsObjGen,
                 jsGen.jsObjGen
                 )
          { (a,
             b
            ) =>
            a.union_(b, SET).fields().containsAll(b.union_(a, SET).fields()) &&
            b.union_(a, SET).fields().containsAll(a.union_(b, SET).fields())

          }
          )
  }
  property("objects intersection is commutative (same fields and values)")
  {
    check(forAll(jsGen.jsObjGen,
                 jsGen.jsObjGen
                 )
          { (a,
             b
            ) =>
            a.intersection(b).equals(b.intersection(a))
          }
          )
  }

  property("objects intersection_ is commutative (same fields and values)")
  {
    check(forAll(jsGen.jsObjGen,
                 jsGen.jsObjGen
                 )
          { (a,
             b
            ) =>
            println("------------")
            println("a: "+a)
            println("b: "+b)
            val obj = a.intersection_(b,
                                      SET
                                      )
            println(obj)
            val obj1 = b.intersection_(a,
                                      SET
                                      )
            println(obj1)
            val eq = obj.equals(obj1,
                                  SET
                                  )
            println(eq)
            eq
          }
          )
  }


  property("intersection parse an object with an empty object returns an empty object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.intersection(JsObj.empty()).equals(JsObj.empty())
          }
          )
  }

  property("intersection_ parse an object with an empty object returns an empty object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.intersection_(JsObj.empty(), SET).equals(JsObj.empty())
          }
          )
  }

  property("arrays as set, duplicated are removed")
  {
    val ONE = JsInt.of(1)
    val TWO = JsInt.of(2)
    val THREE = JsInt.of(3)
    val obj3 = JsObj.of("d",ONE)
    val obj4 = JsObj.of("e",TWO)

    val gen: Gen[JsObj] = Gen.const(JsObj.of("a",
                                             JsArray.of(ONE,
                                                        TWO,
                                                        obj3
                                                        )
                                             )
                                    )

    val b: JsObj = JsObj.of("a",
                            JsArray.of(ONE,
                                       THREE,
                                       obj4
                                       )
                            )

    check(forAll(gen)
          {
            obj =>
              obj.union_(obj,
                         SET
                         ).equals(obj)




              val result = obj.union_(b,
                                      TYPE.MULTISET
                                      )




              val result1 = obj.union_(b,
                                       SET
                                       )





              val result2 = obj.union_(b,
                                       TYPE.LIST
                                       )


              result.size("a").orElse(0) == 6 && result1.size("a").orElse(0) == 5 && result2.size("a").orElse(0) == 3




          }
          )

  }


}
