package jsonvalues.specifications.immutable.jsobject

import java.math.BigInteger
import java.util.function.Function
import java.util.stream

import jsonvalues.specifications.BasePropSpec
import jsonvalues.{JsObj, JsPair}
import org.scalacheck.Prop.forAll

class StreamCollectorSpec extends BasePropSpec
{

  property("pairs<name,x> returned by stream_ function are returned by get function.")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            def testPredicateIf(filter: jsonvalues.JsPair => Boolean,
                                predicate: jsonvalues.JsPair => Boolean
                               ): JsObj => Boolean =
            {
              json =>
                json.streamAll().
                  filter((t: jsonvalues.JsPair) => filter.apply(t)).
                  allMatch((t: jsonvalues.JsPair) => predicate.apply(t))
            }

            List(
              testPredicateIf(p => p.elem.isStr,
                              pair => js.getStr(pair.path).get == pair.elem.toJsStr.value
                              ),
              testPredicateIf(p => p.elem.isInt,
                              pair =>
                              {
                                val n = pair.elem.toJsInt.value
                                (js.getInt(pair.path).getAsInt == n) &&
                                (js.getLong(pair.path).getAsLong == n) &&
                                (js.getBigInt(pair.path).get() == BigInteger.valueOf(n))
                              }
                              ),
              testPredicateIf(p => p.elem.isLong,
                              pair =>
                              {
                                val n = pair.elem.toJsLong.value
                                (js.getLong(pair.path).getAsLong == n) &&
                                (js.getBigInt(pair.path).get() == BigInteger.valueOf(n))
                              }
                              ),
              testPredicateIf(p => p.elem.isDouble,
                              pair =>
                              {
                                val n = pair.elem.toJsDouble.value
                                (js.getDouble(pair.path).getAsDouble == n) &&
                                (js.getBigDecimal(pair.path).get() == java.math.BigDecimal.valueOf(n))
                              }
                              ),
              testPredicateIf(p => p.elem.isBigInt,
                              pair => js.getBigInt(pair.path).get == pair.elem.toJsBigInt.value
                              ),
              testPredicateIf(pair => pair.elem.isBigDec,
                              pair => js.getBigDecimal(pair.path).get == pair.elem.toJsBigDec.value
                              ),
              testPredicateIf(pair => pair.elem.isBool,
                              pair => js.getBool(pair.path).get == pair.elem.toJsBool.value
                              )
              ).map(f => f(js))
              .reduce(_ && _)
          }
          )
  }





  property("reduces a stream in parallel and not in parallel and returns the same result")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val function: Function[JsPair, Long] = pair => pair.elem.toJsLong().value

            val value: stream.Stream[Long] = js.streamAll().filter(p => p.elem.isLong || p.elem.isInt).map(function)
            val a = value.reduce((a: Long,
                                  b: Long
                                 ) => a + b
                                 ).orElse(-1)

            val value1: stream.Stream[Long] = js.streamAll().parallel().filter(p => p.elem.isLong || p.elem.isInt).map(function)
            val b = value1.reduce((a: Long,
                                   b: Long
                                  ) => a + b
                                  ).orElse(-1)
            a.equals(b)
          }
          )
  }
}
