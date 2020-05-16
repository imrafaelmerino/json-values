package jsonvalues.specifications.immutable.jsarray

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
             JsArray.empty().set(path,
                                 JsStr.of(str)
                                 ).getStr(path) == str
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

             val obj = JsArray.empty().set(path,
                                           JsBool.of(bool)
                                           )
             obj.getBool(path) == bool
             obj.getInt(path) == null
             obj.getLong(path) == null
             obj.getStr(path) == null
             obj.getObj(path) == null
             obj.getArray(path) == null
             obj.getBigDec(path) == null
             obj.getBigInt(path) == null
             obj.getDouble(path) == null
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
             val arr = JsArray.empty().set(path,
                                           JsInt.of(n)
                                           )
             arr.getInt(path) == n
             arr.getBool(path) == null
             arr.getLong(path) == n
             arr.getBigInt(path) == BigInteger.valueOf(n)
             arr.getObj(path) == null
             arr.getArray(path) == null
             arr.getBigDec(path) == null
             arr.getDouble(path) == null
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


             val arr = JsArray.empty().set(path,
                                           JsLong.of(n)
                                           )

             arr.getInt(path) == Try.apply(Math.toIntExact(n)).getOrElse(null)

             arr.getBool(path) == null
             arr.getLong(path) == n
             arr.getBigInt(path) == BigInteger.valueOf(n)
             arr.getObj(path) == null
             arr.getArray(path) == null
             arr.getBigDec(path) == null
             arr.getDouble(path) == null
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

             val arr = JsArray.empty().set(path,
                                           JsBigInt.of(n.bigInteger)
                                           )
             arr.getInt(path) == Try.apply(n.bigInteger.intValueExact()).getOrElse(null)
             arr.getBool(path) == null
             arr.getLong(path) == Try.apply(n.bigInteger.longValueExact()).getOrElse(null)
             arr.getBigInt(path) == n.bigInteger
             arr.getObj(path) == null
             arr.getArray(path) == null
             arr.getBigDec(path) == null
             arr.getDouble(path) == null
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
                                     JsArray.optics.lens.value(it.path)
                                       .setIfPresent
                                       .apply(js,ScalaToJava.supplier.apply(()=>JsNull.NULL))
                                       .get(it.path)
                                       .equals(JsNull.NULL)

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
                                   it =>
                                     JsArray.optics.lens.value(it.path)
                                       .setIfAbsent
                                       .apply(js,ScalaToJava.supplier.apply(()=>JsNull.NULL))
                                       .get(it.path)
                                       .equals(it.value)
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
            JsArray.optics.lens.value(path)
              .setIfAbsent
              .apply(JsArray.empty(),ScalaToJava.supplier.apply(()=>elem))
              .get(path)
              .equals(elem)

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

            JsArray.empty().set(path,
                                elem
                                ).delete(path).get(path) == JsNothing.NOTHING

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

             val json = JsArray.empty().set(path,
                                            JsBool.of(value)
                                            )
             (json.getBool(path) == value) &&
             (json.delete(path).get(path) == JsNothing.NOTHING)


           }
           )
  }


}
