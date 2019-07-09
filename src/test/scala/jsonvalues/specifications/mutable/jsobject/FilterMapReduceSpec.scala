package jsonvalues.specifications.mutable.jsobject

import jsonvalues._
import jsonvalues.specifications.BasePropSpec

class FilterMapReduceSpec extends BasePropSpec
{


  //
  //  //  property("mapPairs_")
  //  //  {
  //  //    check(forAll(jsOnlyObjGen)
  //  //          { json =>
  //  //
  //  //            json.mapPairs_((p: Pair[JsPath, JsValue]) => neatjson.Pair.parse(p._1.last().name(),
  //  //                                                                          JsNull.NULL
  //  //                                                                         )
  //  //                          )
  //  //              .stream_()
  //  //              .allMatch((p: Pair[JsPath, JsElem]) => p._2.equals(JsNull.NULL))
  //  //
  //  //            json.mapPairs_(e => neatjson.Pair.parse("key_" + e._1,
  //  //                                                 JsNull.NULL
  //  //                                                )
  //  //                          )
  //  //              .stream_()
  //  //              .allMatch((p: Pair[JsPath, JsElem]) => p._1.last().toString.startsWith("key_") && p._2 ==
  //  JsNull.NULL)
  //  //
  //  //          }
  //  //         )
  //  //  }
  //

  //  //  property("filtering")
  //  //  {
  //  //    check(
  //  //           forAll(Gen.oneOf(jsOnlyStrGen.jsObjGen,
  //  //                            jsOnlyStrGen.jsArrGen
  //  //                           )
  //  //                 )
  //  //           { js =>
  //  //             js.filterElems_(p => p._2.isStr).stream_()
  //  //               .allMatch(p => p._2.isStr
  //  //                        )
  //  //           }
  //  //         )
  //  //  }
  //
}
