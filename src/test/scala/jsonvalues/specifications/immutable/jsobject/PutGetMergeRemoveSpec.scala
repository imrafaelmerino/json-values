package jsonvalues.specifications.immutable.jsobject

import java.math.BigInteger
import java.util._
import java.util.function.BiFunction

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll

import scala.util.Try

class PutGetMergeRemoveSpec extends BasePropSpec
{





  val doubleInt: BiFunction[JsElem, JsElem, JsInt] = ScalaToJava.bifunction((a,
                                                                             b
                                                                            ) => JsInt.of(a.asJsInt().x + b.asJsInt().x)
                                                                            )

  property("inserted string in an empty object has to be returned by getStr function")
  {

    check(
           forAll(jsPathGen.objectPathGen,
                  Arbitrary.arbitrary[String]
                  )
           { (path,
              str
             ) =>
             JsObj.empty().put(path,
                               JsStr.of(str)
                               ).getStr(path).get() == str
           }
           )
  }

  property("inserted boolean in an empty object has to be returned only by getBool function")
  {
    check(
           forAll(jsPathGen.objectPathGen,
                  Arbitrary.arbitrary[Boolean]
                  )
           { (path,
              bool
             )
           =>

             val obj = JsObj.empty().put(path,
                                         JsBool.of(bool)
                                         )
             obj.getBool(path).get() == bool
             obj.getInt(path) == OptionalInt.empty()
             obj.getLong(path) == OptionalLong.empty()
             obj.getStr(path) == Optional.empty()
             obj.getObj(path) == Optional.empty()
             obj.getArray(path) == Optional.empty()
             obj.getBigDecimal(path) == Optional.empty()
             obj.getBigInt(path) == Optional.empty()
             obj.getDouble(path) == OptionalDouble.empty()
             obj.get(path) == JsBool.of(bool)

           }
           )
  }


  property("inserted integer in an empty object has to be returned by getInt,getLong,getBigInt function")
  {
    check(
           forAll(jsPathGen.objectPathGen,
                  Arbitrary.arbitrary[Int]
                  )
           { (path,
              n
             )
           =>
             val obj = JsObj.empty().put(path,
                                         JsInt.of(n)
                                         )
             obj.getInt(path).getAsInt == n
             obj.getBool(path) == Optional.empty()
             obj.getLong(path).getAsLong == n
             obj.getBigInt(path).get() == BigInteger.valueOf(n)
             obj.getObj(path) == Optional.empty()
             obj.getArray(path) == Optional.empty()
             obj.getBigDecimal(path) == Optional.empty()
             obj.getDouble(path) == OptionalDouble.empty()
             obj.get(path) == JsInt.of(n)

           }
           )
  }

  property("inserted long in an empty object has to be returned by getInt(if fits),getLong,getBigInt and get function")
  {
    check(
           forAll(jsPathGen.objectPathGen,
                  Arbitrary.arbitrary[Long]
                  )
           { (path,
              n
             )
           =>


             val obj = JsObj.empty().put(path,
                                         JsLong.of(n)
                                         )

             obj.getInt(path) == Try.apply(OptionalInt.of(Math.toIntExact(n))).getOrElse(OptionalInt.empty())

             obj.getBool(path) == Optional.empty()
             obj.getLong(path).getAsLong == n
             obj.getBigInt(path).get() == BigInteger.valueOf(n)
             obj.getObj(path) == Optional.empty()
             obj.getArray(path) == Optional.empty()
             obj.getBigDecimal(path) == Optional.empty()
             obj.getDouble(path) == OptionalDouble.empty()
             obj.get(path) == JsLong.of(n)

           }
           )
  }

  property("inserted bigint in an empty object has to be returned by getInt(if fits),getLong(if fits),getBigInt and get functions")
  {
    check(
           forAll(jsPathGen.objectPathGen,
                  Arbitrary.arbitrary[BigInt]
                  )
           { (path,
              n
             )
           =>

             val obj = JsObj.empty().put(path,
                                         JsBigInt.of(n.bigInteger)
                                         )

             obj.getInt(path) == Try.apply(OptionalInt.of(n.bigInteger.intValueExact())).getOrElse(OptionalInt.empty()) &&
             obj.getBool(path) == Optional.empty() &&
             obj.getLong(path) == Try.apply(OptionalLong.of(n.bigInteger.longValueExact())).getOrElse(OptionalLong.empty()) &&
             obj.getBigInt(path).get() == n.bigInteger &&
             obj.getObj(path) == Optional.empty() &&
             obj.getArray(path) == Optional.empty() &&
             obj.getBigDecimal(path) == Optional.empty()  &&
             obj.getDouble(path) == OptionalDouble.empty() &&
             obj.get(path) == JsBigInt.of(n.bigInteger)

           }
           )
  }


  property("inserts integer in an empty object with merge function")
  {
    check(forAll(jsPathGen.objectPathGen,
                 Arbitrary.arbitrary[Int]
                 )
          { (path,
             number
            ) =>

            JsObj.empty().merge(path,
                                JsInt.of(number),
                                doubleInt
                                ).getInt(path).getAsInt == number
          }
          )
  }

  property("inserts integer and doubles it with merge function")
  {
    check(forAll(jsPathGen.objectPathGen,
                 Arbitrary.arbitrary[Int]
                 )
          { (path,
             number
            ) =>
            JsObj.empty().put(path,
                              JsInt.of(number)
                              ).merge(path,
                                      JsInt.of(number),
                                      doubleInt
                                      ).getInt(path).getAsInt == 2 * number
          }
          )
  }


  property("put if present replaces elements with null")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            js.stream_().allMatch(
                                   it =>
                                   {
                                     val elemToNull: function.Function[_ >: JsElem, _ <: JsElem] = _ => JsNull.NULL
                                     js.putIfPresent(it.path,
                                                     elemToNull
                                                     ).get(it.path).equals(JsNull.NULL)
                                   }
                                   )


          }
          )
  }

  property("put if absent never inserts when element containsPath")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            js.stream_().allMatch(
                                   it => js.putIfAbsent(it.path,
                                                        ScalaToJava.supplier(() => JsNull.NULL)
                                                        ).get(it.path) == it.elem
                                   )


          }
          )
  }

  property("put if absent inserts when element doesnt exist")
  {
    check(forAll(jsPathGen.objectPathGen,
                 jsGen.jsValueGen
                 )
          { (path,
             elem
            ) =>
            JsObj.empty().putIfAbsent(path,
                                      ScalaToJava.supplier(() => elem)
                                      ).get(path) == elem
          }
          )
  }


  property("removes existing element and get function returns NOTHING")
  {
    check(forAll(jsPathGen.objectPathGen,
                 jsGen.jsValueGen
                 )
          { (path,
             elem
            ) =>

            JsObj.empty().put(path,
                              elem
                              ).remove(path).get(path) == JsNothing.NOTHING

          }
          )
  }

  property("inserts and removes boolean")
  {
    check(
           forAll(jsPathGen.objectPathGen,
                  Arbitrary.arbitrary[Boolean]
                  )
           { (path,
              value
             ) =>

             val json = JsObj.empty().put(path,
                                          JsBool.of(value)
                                          )
             (json.getBool(path).get() == value) &&
             (json.remove(path).get(path) == JsNothing.NOTHING)


           }
           )
  }


}
