package jsonvalues;

import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

public class Utils
{
    /**
     Declarative way of implementing {@code  if(pair.elem.isInt()) return Pair.parse(pair.path, pair.elem.asJsInt().map(operator)) else return pair}
     <p>
     Examples:
     <pre>
     {@code
     JsPair pair = JsPair.parse(JsPath.parse("a.b"),JsInt.parse(1))
     pair.mapIfLong(l->l+10) // ('a'.'b', 11)

     JsPair pair1 = JsPair.parse(JsPath.parse("a.b"),JsStr.parse("a"))
     pair1.mapIfLong(l->l+10).equals(pair1) // true, same pair is returned
     }
     </pre>
     @param operator the function to be applied to map the integer
     @return the same this instance if the JsElem is not a JsInt or a new pair
     */
    public static Function<JsPair, JsPair> mapIfInt(IntUnaryOperator operator)
    {

        return pair ->
        {
            if (pair.elem.isInt()) return JsPair.of(pair.path,
                                                    pair.elem.asJsInt()
                                                             .map(operator)
                                                   );


            return pair;
        };

    }

    /**
     Declarative way of implementing {@code  if(pair.elem.isStr()) return Pair.parse(pair.path, pair.elem.asJsStr().map(mapFn)) else return pair}
     <p>
     Examples:
     <pre>
     {@code
     JsPair pair = JsPair.parse(JsPath.parse("a.b"),JsStr.parse("a"))
     pair.mapIfStr(String::toUpperCase) // ('a'.'b', "A")

     JsPair pair1 = JsPair.parse(JsPath.parse("a.b"),JsInt.parse(1))
     pair1.mapIfStr(String::toUpperCase).equals(pair1) // true, same pair is returned
     }
     </pre>
     @param fn the function to be applied to map the string of the JsStr
     @return the same this instance if the JsElem is not a JsStr or a new pair
     */
    public static Function<JsPair, JsPair> mapIfStr(UnaryOperator<String> fn)
    {

        return pair ->
        {
            if (pair.elem.isStr()) return JsPair.of(pair.path,
                                                    pair.elem.asJsStr()
                                                             .map(fn)
                                                   );


            return pair;
        };

    }


}
