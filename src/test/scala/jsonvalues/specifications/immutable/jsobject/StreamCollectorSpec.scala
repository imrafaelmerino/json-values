package jsonvalues.specifications.immutable.jsobject

import java.math.BigInteger
import java.util.function.Function
import java.util.stream

import jsonvalues.specifications.BasePropSpec
import jsonvalues.{JsObj, JsPair, Utils}
import org.scalacheck.Prop.forAll

class StreamCollectorSpec extends BasePropSpec
{

  property("pairs<name,x> returned by stream_ function are returned by get function.")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            def testPredicateIf(filter   : jsonvalues.JsPair => Boolean,
                                predicate: jsonvalues.JsPair => Boolean
                               ): JsObj => Boolean =
            {
              json =>
                json.stream_().
                  filter((t: jsonvalues.JsPair) => filter.apply(t)).
                  allMatch((t: jsonvalues.JsPair) => predicate.apply(t))
            }

            List(
                  testPredicateIf(p => p.elem.isStr,
                                  pair => js.getStr(pair.path).get == pair.elem.asJsStr.x
                                  ),
                  testPredicateIf(p => p.elem.isInt,
                                  pair =>
                                  {
                                    val n = pair.elem.asJsInt.x
                                    (js.getInt(pair.path).getAsInt == n) &&
                                    (js.getLong(pair.path).getAsLong == n) &&
                                    (js.getBigInt(pair.path).get() == BigInteger.valueOf(n))
                                  }
                                  ),
                  testPredicateIf(p => p.elem.isLong,
                                  pair =>
                                  {
                                    val n = pair.elem.asJsLong.x
                                    (js.getLong(pair.path).getAsLong == n) &&
                                    (js.getBigInt(pair.path).get() == BigInteger.valueOf(n))
                                  }
                                  ),
                  testPredicateIf(p => p.elem.isDouble,
                                  pair =>
                                  {
                                    val n = pair.elem.asJsDouble.x
                                    (js.getDouble(pair.path).getAsDouble == n) &&
                                    (js.getBigDecimal(pair.path).get() == java.math.BigDecimal.valueOf(n))
                                  }
                                  ),
                  testPredicateIf(p => p.elem.isBigInt,
                                  pair => js.getBigInt(pair.path).get == pair.elem.asJsBigInt.x
                                  ),
                  testPredicateIf(pair => pair.elem.isBigDec,
                                  pair => js.getBigDecimal(pair.path).get == pair.elem.asJsBigDec.x
                                  ),
                  testPredicateIf(pair => pair.elem.isBool,
                                  pair => js.getBool(pair.path).get == pair.elem.asJsBool.x
                                  )
                  ).map(f => f(js))
              .reduce(_ && _)
          }
          )
  }


  property("object collector reduces an stream_ back to the same object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>
            val obj = js.stream_().collect(JsObj.collector())
            obj.equals(js) && obj.hashCode() == js.hashCode()

          }
          )
  }


  property("maps a stream in parallel and not in parallel and returns the same json object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val function = Utils.mapIfStr(_.toUpperCase)

            val a: stream.Stream[JsPair] = js.stream_().map(function)

            val b: stream.Stream[JsPair] = js.stream_().parallel().map(function)

            a.collect(JsObj.collector()).equals(b.collect(JsObj.collector()))

          }
          )
  }

  property("filters a stream in parallel and not in parallel and returns the same json object")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val a: stream.Stream[JsPair] = js.stream_().filter(p => p.elem.isNotNull && !p.elem.isBool())

            val b: stream.Stream[JsPair] = js.stream_().parallel().filter(p => p.elem.isNotNull && !p.elem.isBool())

            a.collect(JsObj.collector()).equals(b.collect(JsObj.collector()))

          }
          )
  }

  property("reduces a stream in parallel and not in parallel and returns the same result")
  {
    check(forAll(jsGen.jsObjGen)
          { js =>

            val function: Function[JsPair, Long] = pair => pair.elem.asJsLong().x

            val value: stream.Stream[Long] = js.stream_().filter(p => p.elem.isLong || p.elem.isInt).map(function)
            val a = value.reduce((a: Long,
                                  b: Long
                                 ) => a + b
                                 ).orElse(-1)

            val value1: stream.Stream[Long] = js.stream_().parallel().filter(p => p.elem.isLong || p.elem.isInt).map(function)
            val b = value1.reduce((a: Long,
                                   b: Long
                                  ) => a + b
                                  ).orElse(-1)
            a.equals(b)
          }
          )
  }
}
