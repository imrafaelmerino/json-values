package jsonvalues;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * Represents a json element of any type. Every json type implements this interface. This interface
 * implements two kind of methods:
 * <ul>
 * <li>Classificatory methods, which starts with the prefix <b>isXXX</b></li>
 * <li>Accessory methods to convert the JsElem to the real implementation, which starts with the prefix asJsXXX</li>
 </ul>
 */
public interface JsElem
{

    /**
     @return this JsElem as a JsBool
     @throws UserError if this JsElem is not a JsBool
     */
    default JsBool asJsBool()
    {
        try
        {
            return ((JsBool) this);
        }
        catch (ClassCastException e)
        {
            throw UserError.isNotAJsBool(this);

        }
    }

    /**
     @return true if this JsElem is a JsBool
     */
    default boolean isBool() {
        return false;
    }

    /**
     returns true if this elem and the given have the same type
     @param that the given elem
     @return true if this JsElem and the given have the same type
     */
    default boolean isSameType(final JsElem that)
    {
        return this.getClass() == requireNonNull(that).getClass();
    }

    /**
     @return true if this JsElem is a JsBool and it's true
     */
    default boolean isTrue() {
        return false;
    }

    /**
     @return true if this JsElem is a JsBool and it's false
     */
    default boolean isFalse() {
        return false;
    }

    /**
     @return this JsElem as a JsInt
     @throws UserError if this JsElem is not a JsInt
     */
    default JsInt asJsInt()
    {
        try
        {
            return ((JsInt) this);
        }
        catch (ClassCastException e)
        {
            throw UserError.isNotAJsInt(this);
        }
    }

    /**
     @return true if this JsElem is a JsInt
     */
    default boolean isInt() {
        return false;
    }

    /**
     Returns true if this elem is a JsInt and satisfies the given predicate
     @param predicate the given predicate
     @return true if this JsElem is a JsInt and satisfies the predicate
     */
    default boolean isInt(IntPredicate predicate)
    {
        return isInt() && predicate.test(asJsInt().x);
    }

    /**
     @return this JsElem as a JsDouble
     @throws UserError if this JsElem is not a JsDouble
     */
    default JsDouble asJsDouble()
    {
        try
        {
            return ((JsDouble) this);
        }
        catch (ClassCastException e)
        {
            throw UserError.isNotAJsDouble(this);
        }
    }

    /**
     @return true if this JsElem is a JsDouble
     */
    default boolean isDouble() {
        return false;
    }

    /**
     Returns true if this elem is a JsDouble and satisfies the given predicate
     @param predicate the given predicate
     @return true if this JsElem is a JsDouble and satisfies the predicate
     */
    default boolean isDouble(DoublePredicate predicate)
    {
        return isDouble() && predicate.test(asJsDouble().x);
    }

    /**
     @return this JsElem as a JsBigDec
     @throws UserError if this JsElem is not a JsBigDec or a JsDouble
     */
    default JsBigDec asJsBigDec()
    {
        try
        {
            if (isDouble()) return JsBigDec.of(BigDecimal.valueOf(asJsDouble().x));
            else return ((JsBigDec) this);
        }
        catch (ClassCastException e)
        {
            throw UserError.isNotAJsBigDec(this);
        }
    }

    /**
     @return true if this JsElem is a JsBigDec
     */
    default boolean isBigDec()
    {
        return false;
    }

    /**
     Returns true if this elem is a JsBigDec and satisfies the given predicate
     @param predicate the given predicate
     @return true if this JsElem is a JsBigDec and satisfies the predicate
     */
    default boolean isBigDec(Predicate<BigDecimal> predicate)
    {
        return isBigDec() && predicate.test(asJsBigDec().x);
    }

    /**
     @return this JsElem as a JsLong
     @throws UserError if this JsElem is not a JsLong or a JsInt
     */
    default JsLong asJsLong()
    {
        try
        {
            if (isInt()) return JsLong.of(asJsInt().x);
            else return ((JsLong) this);
        }
        catch (ClassCastException e)
        {
            throw UserError.isNotAJsLong(this);
        }
    }

    /**
     @return true if this JsElem is a JsLong
     */
    default boolean isLong() {
        return false;
    }

    /**
     Returns true if this elem is a JsLong and satisfies the given predicate
     @param predicate the given predicate
     @return true if this JsElem is a JsLong and satisfies the predicate
     */
    default boolean isLong(LongPredicate predicate)
    {
        return isLong() && predicate.test(asJsLong().x);
    }

    /**
     @return this JsElem as a Json
     @throws UserError if this JsElem is not a JsObj or a JsArray
     */
    //S1452: Json<?> has only two possible types: JsObj or JsArr,
    @SuppressWarnings("squid:S1452")
    default Json<?> asJson()
    {

        if (isObj()) return asJsObj();
        else if (isArray()) return asJsArray();
        else throw UserError.isNotAJson(this);

    }

    /**
     @return true if this JsElem is a Json (JsObj or JsArray)
     */
    default boolean isJson()
    {
        return isObj() || isArray();
    }

    /**
     @return true if this JsElem is a not a Json (neither JsObj nor JsArray)
     */
    default boolean isNotJson()
    {
        return !isJson();
    }

    /**
     Returns true if this elem is a Json and satisfies the given predicate
     @param predicate the given predicate
     @return true if this JsElem is a Json and satisfies the predicate
     */
    default boolean isJson(Predicate<Json<?>> predicate)
    {
        return isJson() && predicate.test(asJson());
    }

    /**
     @return this JsElem as a JsObj
     @throws UserError if this JsElem is not a JsObj
     */
    default JsObj asJsObj()
    {
        try
        {
            return ((JsObj) this);
        }
        catch (ClassCastException e)
        {
            throw UserError.isNotAJsObj(this);
        }
    }

    /**
     @return true if this JsElem is a JsObj
     */
    default boolean isObj() {
        return false;
    }

    /**
     Returns true if this elem is a JsObj and satisfies the given predicate
     @param predicate the given predicate
     @return true if this JsElem is a JsObj and satisfies the predicate
     */
    default boolean isObj(Predicate<JsObj> predicate)
    {
        return isObj() && predicate.test(asJsObj());
    }

    /**
     @return this JsElem as a JsArray
     @throws UserError if this JsElem is not a JsArray
     */
    default JsArray asJsArray()
    {
        try
        {
            return ((JsArray) this);
        }
        catch (ClassCastException e)
        {
            throw UserError.isNotAJsArray(this);
        }
    }

    /**
     @return true if this JsElem is a JsArray
     */

    default boolean isArray()
    {
        return false;
    }

    /**
     Returns true if this elem is a JsArray and satisfies the given predicate
     @param predicate the given predicate
     @return true if this JsElem is a JsArray and satisfies the predicate
     */
    default boolean isArray(Predicate<JsArray> predicate)
    {
        return isArray() && predicate.test(asJsArray());
    }

    /**
     @return this JsElem as a JsStr
     @throws UserError if this JsElem is not a JsStr
     */
    default JsStr asJsStr()
    {
        try
        {
            return ((JsStr) this);
        }
        catch (ClassCastException e)
        {
            throw UserError.isNotAJsString(this);
        }
    }

    /**
     @return true if this JsElem is a JsStr
     */
    default boolean isStr() {
        return false;
    }

    /**
     Returns true if this elem is a JsStr and satisfies the given predicate
     @param predicate the given predicate
     @return true if this JsElem is a JsStr and satisfies the predicate
     */
    default boolean isStr(Predicate<String> predicate)
    {
        return isStr() && predicate.test(asJsStr().x);
    }

    /**
     @return this JsElem as a JsBigInt
     @throws UserError if this JsElem is not a JsBigInt or JsLong or JsInt
     */
    default JsBigInt asJsBigInt()
    {
        try
        {
            if (isInt()) return JsBigInt.of(BigInteger.valueOf(asJsInt().x));
            if (isLong()) return JsBigInt.of(BigInteger.valueOf(asJsLong().x));
            return ((JsBigInt) this);
        }
        catch (ClassCastException e)
        {
            throw UserError.isNotAJsBigInt(this);
        }
    }

    /**
     @return true if this JsElem is a JsBigInt
     */
    default boolean isBigInt()
    {
        return false;
    }

    /**
     Returns true if this elem is a JsBigInt and satisfies the given predicate
     @param predicate the given predicate
     @return true if this JsElem is a JsBigInt and satisfies the predicate
     */
    default boolean isBigInt(Predicate<BigInteger> predicate)
    {
        return isBigInt() && predicate.test(asJsBigInt().x);
    }

    /**
     @return true if this JsElem is a JsStr that contains an instant in the format {@link DateTimeFormatter#ISO_INSTANT}
     */
    default boolean isInstant()
    {
        return isStr(str ->
                     {
                         try
                         {
                             Instant.parse(str);
                             return true;
                         }
                         catch (Exception e)
                         {
                             return false;
                         }
                     });
    }

    /**
     Returns true if this elem is a JsStr that contains an Instant and  satisfies the given predicate
     @param predicate the given predicate
     @return true if this JsElem is a JsStr that contains an Instant and satisfies the predicate
     */
    default boolean isInstant(Predicate<Instant> predicate)
    {
        return isStr(str ->
                     {
                         try
                         {
                             final Instant instant = Instant.parse(str);
                             return predicate.test(instant);
                         }
                         catch (DateTimeParseException e)
                         {
                             return false;
                         }


                     });
    }

    /**
     return true if this JsElem is a JsStr that contains a local date that can be parsed with the given formatter
     @param formatter the given formatter
     @return true if this JsElem is a JsStr that contains a local date that can be parsed with the given formatter
     */
    default boolean isLocalDate(DateTimeFormatter formatter)
    {
        requireNonNull(formatter);
        return isStr(str ->
                     {
                         try
                         {
                             LocalDate.parse(str,
                                             formatter
                                            );
                             return true;
                         }
                         catch (Exception e)
                         {
                             return false;
                         }
                     }
                    );
    }

    /**
     return true if this JsElem is a JsStr that contains a local date that can be parsed with the given formatter
     and satisfies the given predicate
     @param formatter the given formatter
     @param predicate the given predicate
     @return true if this JsElem is a JsStr that contains a local date that can be parsed with the formatter
     and satisfies the  predicate
     */
    default boolean isLocalDate(DateTimeFormatter formatter,
                                Predicate<LocalDate> predicate
                               )
    {
        requireNonNull(formatter);
        requireNonNull(predicate);
        return isStr(str ->
                     {
                         try
                         {
                             final LocalDate localDate = LocalDate.parse(str,
                                                                         formatter
                                                                        );
                             return predicate.test(localDate);
                         }
                         catch (DateTimeParseException e)
                         {
                             return false;
                         }


                     });

    }

    /**
     return true if this JsElem is a JsStr that contains a local date-time that can be parsed with the given formatter
     @param formatter the given formatter
     @return true if this JsElem is a JsStr that contains a local date-time that can be parsed with the given formatter
     */
    default boolean isLocalDateTime(DateTimeFormatter formatter)
    {
        requireNonNull(formatter);
        return isStr(str ->
                     {
                         try
                         {
                             LocalDateTime.parse(str,
                                                 formatter
                                                );
                             return true;
                         }
                         catch (Exception e)
                         {
                             return false;
                         }

                     });
    }

    /**
     return true if this JsElem is a JsStr that contains a local date-time that can be parsed with the given formatter
     and satisfies the given predicate
     @param formatter the given formatter
     @param predicate the given predicate
     @return true if this JsElem is a JsStr that contains a local date-time that can be parsed with the formatter
     and satisfies the  predicate
     */
    default boolean isLocalDateTime(DateTimeFormatter formatter,
                                    Predicate<LocalDateTime> predicate
                                   )
    {
        requireNonNull(formatter);
        requireNonNull(predicate);
        return isStr(str ->
                     {
                         try
                         {
                             final LocalDateTime ldt = LocalDateTime.parse(str,
                                                                           formatter
                                                                          );
                             return predicate.test(ldt);
                         }
                         catch (DateTimeParseException e)
                         {
                             return false;
                         }


                     });
    }

    /**

     @return true if this element is an integral number (JsInt or JsLong or JsBigInt)
     */
    default boolean isIntegral()
    {
        return isInt() || isLong() || isBigInt();
    }

    /**

     @return true if this element is an decimal number (JsDouble or JsBigDec)
     */
    default boolean isDecimal()
    {
        return isDouble() || isBigDec();
    }

    /**
     @return true if this element is null
     */
    default boolean isNull() {
        return false;
    }

    /**
     @return true if this element is JsNothing
     */
    default boolean isNothing() {
        return false;
    }

    /**
     @return true if this element is not null
     */
    default boolean isNotNull()
    {
        return !isNull();
    }

    /**
     @return true if this element is not JsNothing
     */
    default boolean isNotNothing()
    {
        return !isNothing();
    }

    /**
     @return true if this element is a number
     */
    default boolean isNumber()
    {
        return isInt() || isLong() || isBigInt() || isDouble() || isBigDec();
    }

    /**
     @return true if this element is  not a number
     */
    default boolean isNotNumber()
    {
        return !isNumber();
    }

}
