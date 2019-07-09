package jsonvalues.specifications.mutable.jsobject

import java.math.BigInteger

import jsonvalues._
import jsonvalues.specifications.BasePropSpec
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
                json.stream_().
                  filter((t: jsonvalues.JsPair) => filter.apply(t)).
                  allMatch((t: jsonvalues.JsPair) => predicate.apply(t))
            }

            List(
                  testPredicateIf(pair => pair.elem.isStr,
                                  pair => js.getStr(pair.path).get == pair.elem.asInstanceOf[JsStr].x
                                  ),
                  testPredicateIf(pair => pair.elem.isInt,
                                  pair =>
                                  {
                                    val n = pair.elem.asInstanceOf[JsInt].x
                                    (js.getInt(pair.path).getAsInt == n) &&
                                    (js.getLong(pair.path).getAsLong == n) &&
                                    (js.getBigInt(pair.path).get() == BigInteger.valueOf(n))
                                  }
                                  ),
                  testPredicateIf(pair => pair.elem.isLong,
                                  pair =>
                                  {
                                    val n = pair.elem.asInstanceOf[JsLong].x
                                    (js.getLong(pair.path).getAsLong == n) &&
                                    (js.getBigInt(pair.path).get() == BigInteger.valueOf(n))
                                  }
                                  ),
                  testPredicateIf(pair =>  pair.elem.isDouble,
                                  pair =>
                                  {
                                    val n = pair.elem.asInstanceOf[JsDouble].x
                                    (js.getDouble(pair.path).getAsDouble == n) &&
                                    (js.getBigDecimal(pair.path).get() == java.math.BigDecimal.valueOf(n))
                                  }
                                  ),
                  testPredicateIf(pair => pair.elem.isBigInt,
                                  pair => js.getBigInt(pair.path).get == pair.elem.asInstanceOf[JsBigInt].x
                                  ),
                  testPredicateIf(pair => pair.elem.isBigDec,
                                  pair => js.getBigDecimal(pair.path).get == pair.elem.asInstanceOf[JsBigDec].x
                                  ),
                  testPredicateIf(pair => pair.elem.isBool,
                                  pair => js.getBool(pair.path).get == pair.elem.asInstanceOf[JsBool].x
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
}
