package jsonvalues.specifications.immutable.array

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


  property("inserted string in an empty array has to be returned by getStr function")
  {

    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[String]
                  )
           { (path,
              str
             ) =>
             JsArray.empty().put(path,
                                 JsStr.of(str)
                                 ).getStr(path).get() == str
           }
           )
  }

  property("inserted boolean is an empty array has to be returned only by getBool function")
  {
    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[Boolean]
                  )
           { (path,
              bool
             )
           =>

             val obj = JsArray.empty().put(path,
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

  property("inserted integer in an empty array has to be returned by getInt,getLong,getBigInt function")
  {
    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[Int]
                  )
           { (path,
              n
             )
           =>
             val arr = JsArray.empty().put(path,
                                           JsInt.of(n)
                                           )
             arr.getInt(path).getAsInt == n
             arr.getBool(path) == Optional.empty()
             arr.getLong(path).getAsLong == n
             arr.getBigInt(path).get() == BigInteger.valueOf(n)
             arr.getObj(path) == Optional.empty()
             arr.getArray(path) == Optional.empty()
             arr.getBigDecimal(path) == Optional.empty()
             arr.getDouble(path) == OptionalDouble.empty()
             arr.get(path) == JsInt.of(n)

           }
           )
  }


  property("inserted long in an empty array has to be returned by getInt(if fits),getLong,getBigInt and get function")
  {
    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[Long]
                  )
           { (path,
              n
             )
           =>


             val arr = JsArray.empty().put(path,
                                           JsLong.of(n)
                                           )

             arr.getInt(path) == Try.apply(OptionalInt.of(Math.toIntExact(n))).getOrElse(OptionalInt.empty())

             arr.getBool(path) == Optional.empty()
             arr.getLong(path).getAsLong == n
             arr.getBigInt(path).get() == BigInteger.valueOf(n)
             arr.getObj(path) == Optional.empty()
             arr.getArray(path) == Optional.empty()
             arr.getBigDecimal(path) == Optional.empty()
             arr.getDouble(path) == OptionalDouble.empty()
             arr.get(path) == JsLong.of(n)

           }
           )
  }


  property("inserted bigint in an empty array has to be returned by getInt(if fits),getLong(if fits),getBigInt and get functions")
  {
    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[BigInt]
                  )
           { (path,
              n
             )
           =>

             val arr = JsArray.empty().put(path,
                                           JsBigInt.of(n.bigInteger)
                                           )
             arr.getInt(path) == Try.apply(OptionalInt.of(n.bigInteger.intValueExact())).getOrElse(OptionalInt.empty())
             arr.getBool(path) == Optional.empty()
             arr.getLong(path) == Try.apply(OptionalLong.of(n.bigInteger.longValueExact())).getOrElse(OptionalLong.empty())
             arr.getBigInt(path).get() == n.bigInteger
             arr.getObj(path) == Optional.empty()
             arr.getArray(path) == Optional.empty()
             arr.getBigDecimal(path) == Optional.empty()
             arr.getDouble(path) == OptionalDouble.empty()
             arr.get(path) == JsBigInt.of(n.bigInteger)

           }
           )
  }


  property("inserts integer in an empty array with merge function")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 Arbitrary.arbitrary[Int]
                 )
          { (path,
             number
            ) =>

            JsArray.empty().merge(path,
                                  JsInt.of(number),
                                  doubleInt
                                  ).getInt(path).getAsInt == number
          }
          )
  }

  property("inserts integer and doubles it with merge function")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 Arbitrary.arbitrary[Int]
                 )
          { (path,
             number
            ) =>
            JsArray.empty().put(path,
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
    check(forAll(jsGen.jsArrGen)
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
    check(forAll(jsGen.jsArrGen)
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
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsValueGen
                 )
          { (path,
             elem
            ) =>
            JsArray.empty().putIfAbsent(path,
                                        ScalaToJava.supplier(() => elem)
                                        ).get(path) == elem
          }
          )
  }


  property("removes existing element and get function returns NOTHING")
  {
    check(forAll(jsPathGen.arrayPathGen,
                 jsGen.jsValueGen
                 )
          { (path,
             elem
            ) =>

            JsArray.empty().put(path,
                                elem
                                ).remove(path).get(path) == JsNothing.NOTHING

          }
          )
  }

  property("inserts and removes boolean")
  {
    check(
           forAll(jsPathGen.arrayPathGen,
                  Arbitrary.arbitrary[Boolean]
                  )
           { (path,
              value
             ) =>

             val json = JsArray.empty().put(path,
                                            JsBool.of(value)
                                            )
             (json.getBool(path).get() == value) &&
             (json.remove(path).get(path) == JsNothing.NOTHING)


           }
           )
  }


}
