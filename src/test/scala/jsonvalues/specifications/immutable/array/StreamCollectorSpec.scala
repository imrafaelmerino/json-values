package jsonvalues.specifications.immutable.array

import java.math.BigInteger

import jsonvalues.JsArray
import jsonvalues.specifications.BasePropSpec
import org.scalacheck.Prop.forAll

class StreamCollectorSpec extends BasePropSpec
{

  property("pairs<name,x> returned by stream_ function are returned by get function.")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>
            def testPredicateIf(filter   : jsonvalues.JsPair => Boolean,
                                predicate: jsonvalues.JsPair => Boolean
                               ): JsArray => Boolean =
            {
              json =>
                json.stream_().
                  filter((t: jsonvalues.JsPair) => filter.apply(t)).
                  allMatch((t: jsonvalues.JsPair) => predicate.apply(t))
            }

            List(
                  testPredicateIf(pair => pair.elem.isStr,
                                  pair => js.getStr(pair.path).get == pair.elem.asJsStr.x
                                  ),
                  testPredicateIf(pair => pair.elem.isInt,
                                  pair =>
                                  {
                                    val n = pair.elem.asJsInt.x
                                    (js.getInt(pair.path).getAsInt == n) &&
                                    (js.getLong(pair.path).getAsLong == n) &&
                                    (js.getBigInt(pair.path).get() == BigInteger.valueOf(n))
                                  }
                                  ),
                  testPredicateIf(pair => pair.elem.isLong,
                                  pair =>
                                  {
                                    val n = pair.elem.asJsLong.x
                                    (js.getLong(pair.path).getAsLong == n) &&
                                    (js.getBigInt(pair.path).get() == BigInteger.valueOf(n))
                                  }
                                  ),
                  testPredicateIf(pair => pair.elem.isDouble,
                                  pair =>
                                  {
                                    val n = pair.elem.asJsDouble.x
                                    (js.getDouble(pair.path).getAsDouble == n) &&
                                    (js.getBigDecimal(pair.path).get() == java.math.BigDecimal.valueOf(n))
                                  }
                                  ),
                  testPredicateIf(pair => pair.elem.isBigInt,
                                  pair => js.getBigInt(pair.path).get == pair.elem.asJsBigInt.x
                                  ),
                  testPredicateIf(pair => pair.elem.isBigDec,
                                  pair => js.getBigDecimal(pair.path).get == pair.elem.asJsBigDec().x
                                  ),
                  testPredicateIf(pair => pair.elem.isBool,
                                  pair => js.getBool(pair.path).get == pair.elem.asJsBool.x
                                  )
                  ).map(f => f(js))
              .reduce(_ && _)
          }
          )
  }


  property("array collector reduces an stream_ back to the same object")
  {
    check(forAll(jsGen.jsArrGen)
          { js =>

            val array = js.stream_().collect(JsArray.collector())
            array.equals(js) && array.hashCode() == array.hashCode()

          }
          )
  }

}
