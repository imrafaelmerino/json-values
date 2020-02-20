package jsonvalues;


import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;

public class JsElems
{

    private JsElems()
    {
    }

    /**
     Returns a function that maps a json element if it's a json integer, returning the element otherwise
     @param fn the function to be applied to map the value of this JsElem it it's a JsInt
     @return the same JsElem or a new JsInt with its value mapped
     */
    public static Function<JsValue, JsValue> mapIfInt(final IntUnaryOperator fn)
    {
        return element ->
        {
            if (element.isInt()) return element.toJsInt()
                                               .map(requireNonNull(fn));
            return element;
        };
    }


    /**
     Returns a function that maps a json element if it's a json string, returning the element otherwise
     @param fn the function to be applied to map the value of this JsElem it it's a JsStr
     @return the same JsElem or a new JsStr with its value mapped
     */
    public static Function<JsValue, JsValue> mapIfStr(final UnaryOperator<String> fn)
    {
        return element ->
        {
            if (element.isStr()) return element.toJsStr()
                                               .map(requireNonNull(fn));
            return element;
        };
    }


}
