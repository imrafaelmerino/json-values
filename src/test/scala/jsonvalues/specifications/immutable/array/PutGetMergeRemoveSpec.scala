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


  val doubleInt: BiFunction[JsValue, JsValue, JsInt] = ScalaToJava.bifunction((a,
                                                                               b
                                                                            ) => JsInt.of(a.toJsInt().value + b.toJsInt().value)
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
             val array = JsArray.empty().put(path,
                                             JsStr.of(str)
                                             )
             array.getStrOpt(path).get() == str && array.getStr(path) == str
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
             obj.getBool(path) == bool && obj.getBool(path) == bool &&
             obj.getIntOpt(path) == OptionalInt.empty() && obj.getInt(path) == null &&
             obj.getLongOpt(path) == OptionalLong.empty() && obj.getLong(path) == null &&
             obj.getStrOpt(path) == Optional.empty() && obj.getStr(path) == null &&
             obj.getObjOpt(path) == Optional.empty() && obj.getObj(path) == null &&
             obj.getArrayOpt(path) == Optional.empty() && obj.getArray(path) == null &&
             obj.getBigDecimalOpt(path) == Optional.empty() && obj.getBigDecimal(path) == null &&
             obj.getBigIntOpt(path) == Optional.empty() && obj.getBigInt(path) == null &&
             obj.getDoubleOpt(path) == OptionalDouble.empty() && obj.getDouble(path) == null &&
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
             arr.getIntOpt(path).getAsInt == n && arr.getInt(path) == n &&
             arr.getBoolOpt(path) == Optional.empty() &&
             arr.getLongOpt(path).getAsLong == n &&
             arr.getBigInt(path) == BigInteger.valueOf(n) &&
             arr.getObjOpt(path) == Optional.empty() &&
             arr.getArrayOpt(path) == Optional.empty() &&
             arr.getBigDecimalOpt(path) == Optional.empty() &&
             arr.getDoubleOpt(path) == OptionalDouble.empty() &&
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

             arr.getIntOpt(path) == Try.apply(OptionalInt.of(Math.toIntExact(n))).getOrElse(OptionalInt.empty()) &&
             arr.getBoolOpt(path) == Optional.empty() &&
             arr.getLongOpt(path).getAsLong == n &&
             arr.getBigInt(path) == BigInteger.valueOf(n) &&
             arr.getObjOpt(path) == Optional.empty() &&
             arr.getArrayOpt(path) == Optional.empty() &&
             arr.getBigDecimalOpt(path) == Optional.empty() &&
             arr.getDoubleOpt(path) == OptionalDouble.empty() &&
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
             arr.getIntOpt(path) == Try.apply(OptionalInt.of(n.bigInteger.intValueExact())).getOrElse(OptionalInt.empty()) &&
             arr.getBigInt(path) == n.bigInteger &&
             arr.getBoolOpt(path) == Optional.empty() &&
             arr.getLongOpt(path) == Try.apply(OptionalLong.of(n.bigInteger.longValueExact())).getOrElse(OptionalLong.empty()) &&
             arr.getBigInt(path) == n.bigInteger &&
             arr.getObjOpt(path) == Optional.empty() &&
             arr.getArrayOpt(path) == Optional.empty() &&
             arr.getBigDecimalOpt(path) == Optional.empty() &&
             arr.getDoubleOpt(path) == OptionalDouble.empty() &&
             arr.get(path) == JsBigInt.of(n.bigInteger)

           }
           )
  }




  property("put if present replaces elements with null")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>

            js.streamAll().allMatch(
                                   it =>
                                   {
                                     val elemToNull: function.Function[_ >: JsValue, _ <: JsValue] = _ => JsNull.NULL
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

            js.streamAll().allMatch(
                                   it => js.putIfAbsent(it.path,
                                                        ScalaToJava.supplier(() => JsNull.NULL)
                                                        ).get(it.path) == it.value
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
             (json.getBool(path) == value) &&
             (json.remove(path).get(path) == JsNothing.NOTHING)


           }
           )
  }


}
