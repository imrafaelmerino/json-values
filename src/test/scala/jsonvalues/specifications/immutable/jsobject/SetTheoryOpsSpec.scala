package jsonvalues.specifications.immutable.jsobject

import jsonvalues.JsArray.TYPE
import jsonvalues.JsArray.TYPE.SET
import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class SetTheoryOpsSpec extends BasePropSpec
{

  property("union of an object with itself returns the same object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.union(js).equals(js) && js.union(js).hashCode() == js.hashCode()
          }
          )
  }

  property("unionAll of an object with itself itself returns the same object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.unionAll(js,
                        SET
                        ).equals(js) && js.unionAll(js,
                                                    SET
                                                    ).hashCode() == js.hashCode()
          }
          )
  }

  property("intersection of an object with itself returns the same object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.intersection(js,
                            TYPE.LIST
                            ).equals(js) && js.intersection(js,
                                                            TYPE.LIST
                                                            ).hashCode() == js.hashCode()
          }
          )
  }
  //
  property("union of an object A with an empty object, returns A")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.union(JsObj.empty()
                     ).equals(js) && js.union(JsObj.empty()).equals(js)
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
            a.union(b).keySet().containsAll(b.union(a).keySet()) &&
            b.union(a).keySet().containsAll(a.union(b).keySet())

          }
          )
  }
  //
  property("objects unionAll is commutative (same fields but not necessarily same values)")
  {
    check(forAll(jsGen.jsObjGen,
                 jsGen.jsObjGen
                 )
          { (a,
             b
            ) =>
            a.unionAll(b,
                       SET
                       ).keySet().containsAll(b.unionAll(a,
                                                         SET
                                                         ).keySet()
                                            ) &&
            b.unionAll(a,
                       SET
                       ).keySet().containsAll(a.unionAll(b,
                                                         SET
                                                         ).keySet()
                                            )

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
            a.intersection(b,
                           TYPE.LIST
                           ).equals(b.intersection(a,
                                                   TYPE.LIST
                                                   )
                                    )
          }
          )
  }

  property("objects intersectionAll is commutative (same fields and values)")
  {
    check(forAll(jsGen.jsObjGen,
                 jsGen.jsObjGen
                 )
          { (a,
             b
            ) =>
            val obj = a.intersectionAll(b,
                                        SET
                                        )
            val obj1 = b.intersectionAll(a,
                                         SET
                                         )
            obj.equals(obj1,
                       SET
                       )

          }
          )
  }


  property("intersection of an object with an empty object returns an empty object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.intersection(JsObj.empty(),
                            TYPE.LIST
                            ).equals(JsObj.empty()
                                     )
          }
          )
  }

  property("intersectionAll of an object with an empty object returns an empty object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            js.intersectionAll(JsObj.empty(),
                               SET
                               ).equals(JsObj.empty()
                                      )
          }
          )
  }

  property("arrays as set, duplicated are removed")
  {
    val ONE = JsInt.of(1)
    val TWO = JsInt.of(2)
    val THREE = JsInt.of(3)
    val obj3 = JsObj.of("d",
                                           ONE
                                           )
    val obj4 = JsObj.of("e",
                                           TWO
                                           )

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
              obj.unionAll(obj,
                           SET
                           ).equals(obj)

              val result = obj.unionAll(b,
                                        TYPE.MULTISET
                                        )

              val result1 = obj.unionAll(b,
                                         SET
                                         )

              val result2 = obj.unionAll(b,
                                         TYPE.LIST
                                         )

              val a = JsPath.fromKey("a")
              result.size(a).orElse(0) == 6 && result1.size(a).orElse(0) == 5 && result2.size(a).orElse(0) == 3


          }
          )

  }


}
