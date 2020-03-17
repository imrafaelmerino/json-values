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


  val doubleInt: BiFunction[JsValue, JsValue, JsInt] = ScalaToJava.bifunction((a,
                                                                               b
                                                                              ) => JsInt.of(a.toJsInt.value + b.toJsInt.value)
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
                          ).getStrOpt(path).get() == str
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
        obj.getBool(path) == bool
        obj.getIntOpt(path) == OptionalInt.empty()
        obj.getLongOpt(path) == OptionalLong.empty()
        obj.getStrOpt(path) == Optional.empty()
        obj.getObjOpt(path) == Optional.empty()
        obj.getArrayOpt(path) == Optional.empty()
        obj.getBigDecimalOpt(path) == Optional.empty()
        obj.getBigIntOpt(path) == Optional.empty()
        obj.getDoubleOpt(path) == OptionalDouble.empty()
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
        obj.getIntOpt(path).getAsInt == n
        obj.getBoolOpt(path) == Optional.empty()
        obj.getLongOpt(path).getAsLong == n
        obj.getBigIntOpt(path).get() == BigInteger.valueOf(n)
        obj.getObjOpt(path) == Optional.empty()
        obj.getArrayOpt(path) == Optional.empty()
        obj.getBigDecimalOpt(path) == Optional.empty()
        obj.getDoubleOpt(path) == OptionalDouble.empty()
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

        obj.getIntOpt(path) == Try.apply(OptionalInt.of(Math.toIntExact(n))).getOrElse(OptionalInt.empty())

        obj.getBoolOpt(path) == Optional.empty()
        obj.getLongOpt(path).getAsLong == n
        obj.getBigIntOpt(path).get() == BigInteger.valueOf(n)
        obj.getObjOpt(path) == Optional.empty()
        obj.getArrayOpt(path) == Optional.empty()
        obj.getBigDecimalOpt(path) == Optional.empty()
        obj.getDoubleOpt(path) == OptionalDouble.empty()
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

        obj.getIntOpt(path) == Try.apply(OptionalInt.of(n.bigInteger.intValueExact())).getOrElse(OptionalInt.empty()) &&
        obj.getBoolOpt(path) == Optional.empty() &&
        obj.getLongOpt(path) == Try.apply(OptionalLong.of(n.bigInteger.longValueExact())).getOrElse(OptionalLong.empty()) &&
        obj.getBigIntOpt(path).get() == n.bigInteger &&
        obj.getObjOpt(path) == Optional.empty() &&
        obj.getArrayOpt(path) == Optional.empty() &&
        obj.getBigDecimalOpt(path) == Optional.empty() &&
        obj.getDoubleOpt(path) == OptionalDouble.empty() &&
        obj.get(path) == JsBigInt.of(n.bigInteger)

      }
      )
  }


  property("put if present replaces elements with null")
  {
    check(forAll(jsGen.jsObjGen)
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
    check(forAll(jsGen.jsObjGen)
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
        (json.getBoolOpt(path).get() == value) &&
        (json.remove(path).get(path) == JsNothing.NOTHING)


      }
      )
  }


}
