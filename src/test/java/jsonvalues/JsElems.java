package jsonvalues;


import java.math.BigDecimal;
import java.util.function.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public class JsElems
{

    private JsElems()
    {
    }

    /**
     Returns a function that maps a json element if it's a json double, returning the element otherwise
     @param fn the function to be applied to map the value of this JsElem it it's a JsDouble
     @return the same this JsElem or a new JsDouble with its value mapped
     */
    public static Function<JsElem, JsElem> mapIfDouble(DoubleUnaryOperator fn)
    {
        return element ->
        {
            if (element.isDouble()) return element.asJsDouble()
                                                  .map(requireNonNull(fn));
            return element;
        };
    }

    /**
     Returns a function that maps a json element if it's a big decimal (or a double), returning the element otherwise
     @param fn the function to be applied to map the value of this JsElem it it's a JsBigDecimal or JsDouble
     @return the same this JsElem or a new JsBigDecimal with its value mapped
     */
    public static Function<JsElem, JsElem> mapIfBigDecimal(UnaryOperator<BigDecimal> fn)
    {
        return element ->
        {
            if (element.isBigDec() || element.isDouble()) return JsBigDec.of(fn.apply(element.asJsBigDec().x));
            return element;
        };
    }

    /**
     Returns a function that maps a json element if it's a json integer, returning the element otherwise
     @param fn the function to be applied to map the value of this JsElem it it's a JsInt
     @return the same JsElem or a new JsInt with its value mapped
     */
    public static Function<JsElem, JsElem> mapIfInt(final IntUnaryOperator fn)
    {
        return element ->
        {
            if (element.isInt()) return element.asJsInt()
                                               .map(requireNonNull(fn));
            return element;
        };
    }


    /**
     Returns a function that maps a json element if it's a json string, returning the element otherwise
     @param fn the function to be applied to map the value of this JsElem it it's a JsStr
     @return the same JsElem or a new JsStr with its value mapped
     */
    public static Function<JsElem, JsElem> mapIfStr(final UnaryOperator<String> fn)
    {
        return element ->
        {
            if (element.isStr()) return element.asJsStr()
                                               .map(requireNonNull(fn));
            return element;
        };
    }

    /**
     Returns true if, and only if, the entire string matches the pattern
     @param pattern the pattern
     @return true if, and only if, the entire string matches the pattern
     */
    public static Predicate<JsElem> matches(Pattern pattern)
    {

        return elem -> elem.isStr(str -> requireNonNull(pattern).matcher(Matcher.quoteReplacement(str))
                                                                .matches());

    }

    public static BinaryOperator<JsElem> strOperator(final BinaryOperator<String> op,
                                                     final JsElem _default
                                                    )
    {

        requireNonNull(op);
        requireNonNull(_default);
        return (e1, e2) ->
        {
            requireNonNull(e1);
            requireNonNull(e2);
            if (e1.isStr() && e2.isStr()) return JsStr.of(op.apply(e1.asJsStr().x,
                                                                   e2.asJsStr().x
                                                                  ));
            return _default;
        };
    }

    public static BinaryOperator<JsElem> intOperator(final IntBinaryOperator op,
                                                     final JsElem _default
                                                    )
    {

        requireNonNull(op);
        requireNonNull(_default);
        return (e1, e2) ->
        {
            requireNonNull(e1);
            requireNonNull(e2);
            if (e1.isInt() && e2.isInt()) return JsInt.of(op.applyAsInt(e1.asJsInt().x,
                                                                        e2.asJsInt().x
                                                                       ));
            return _default;
        };
    }

    public static BinaryOperator<JsElem> longOperator(final LongBinaryOperator op,
                                                      final JsElem _default
                                                     )
    {

        requireNonNull(op);
        requireNonNull(_default);
        return (e1, e2) ->
        {
            requireNonNull(e1);
            requireNonNull(e2);
            if ((e1.isInt() || e1.isLong()) && (e2.isInt() || e2.isLong())) return JsLong.of(op.applyAsLong(e1.asJsLong().x,
                                                                                                            e2.asJsLong().x
                                                                                                           ));
            return _default;
        };
    }


}
