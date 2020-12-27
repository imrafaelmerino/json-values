package jsonvalues;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 Represents a json element of any type. Every json type implements this interface. This interface
 implements two kind of methods:
 <ul>
 <li>Classificatory methods, which starts with the prefix <b>isXXX</b></li>
 <li>Accessory methods to convert the JsValue to the real implementation, which starts with the prefix asJsXXX</li>
 </ul>
 */
public interface JsValue {
    int id();

    /**
     @return this JsValue as a JsBool
     @throws UserError if this JsValue is not a JsBool
     */
    default JsBool toJsBool() {
        try {
            return ((JsBool) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsBool(this);

        }
    }

    JsPrimitive toJsPrimitive();

    /**
     @return true if this JsValue is a JsBinary or a JsString which value is an array of
     bytes encoded in base64
     */
    default boolean isBinary() {
        return false;
    }

    /**
     @return true if this JsValue is a JsInstant or a JsString which value is a
     date formatted in ISO-8601
     */
    default boolean isInstant() {
        return false;
    }


    /**
     Returns true if this elem is a JsInstant and satisfies the given predicate

     @param predicate the given predicate
     @return true if this JsValue is a JsInstant and satisfies the predicate
     */
    default boolean isInstant(Predicate<Instant> predicate) {
        return isInstant() && predicate.test(this.toJsInstant().value);
    }

    /**
     @return true if this JsValue is a JsBool
     */
    default boolean isBool() {
        return false;
    }

    /**
     returns true if this elem and the given have the same type

     @param that the given elem
     @return true if this JsValue and the given have the same type
     */
    default boolean isSameType(final JsValue that) {
        return this.getClass() == requireNonNull(that).getClass();
    }

    /**
     @return true if this JsValue is a JsBool and it's true
     */
    default boolean isTrue() {
        return false;
    }

    /**
     @return true if this JsValue is a JsBool and it's false
     */
    default boolean isFalse() {
        return false;
    }

    /**
     Returns true if this elem is a JsInt and satisfies the given predicate

     @param predicate the given predicate
     @return true if this JsValue is a JsInt and satisfies the predicate
     */
    default boolean isInt(IntPredicate predicate) {
        return isInt() && predicate.test(toJsInt().value);
    }

    /**
     @return true if this JsValue is a JsInt
     */
    default boolean isInt() {
        return false;
    }

    /**
     @return this JsValue as a JsInt
     @throws UserError if this JsValue is not a JsInt
     */
    default JsInt toJsInt() {
        try {
            return ((JsInt) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsInt(this);
        }
    }

    /**
     @return this JsValue as a JsInt
     @throws UserError if this JsValue is not a JsInt
     */
    default JsInstant toJsInstant() {
        if(this instanceof JsInstant) return ((JsInstant) this);
        if(this instanceof JsStr) {
            Optional<Instant> instant = JsStr.instantPrism.getOptional.apply(((JsStr) this).value);
            if(instant.isPresent()) return JsInstant.of(instant.get());
        }

        throw UserError.isNotAJsInstant(this);
    }


    /**
     @return this JsValue as a JsBinary
     @throws UserError if this JsValue is not a JsBinary
     */
    default JsBinary toJsBinary() {
            if(this instanceof JsBinary) return ((JsBinary) this);
            if(this instanceof JsStr) {
                Optional<byte[]> bytes = JsStr.base64Prism.getOptional.apply(((JsStr) this).value);
                if(bytes.isPresent()) return JsBinary.of(bytes.get());
            }

            throw UserError.isNotAJsBinary(this);
    }

    /**
     Returns true if this elem is a JsDouble and satisfies the given predicate

     @param predicate the given predicate
     @return true if this JsValue is a JsDouble and satisfies the predicate
     */
    default boolean isDouble(DoublePredicate predicate) {
        return isDouble() && predicate.test(toJsDouble().value);
    }

    /**
     @return true if this JsValue is a JsDouble
     */
    default boolean isDouble() {
        return false;
    }

    /**
     @return this JsValue as a JsDouble
     @throws UserError if this JsValue is not a JsDouble
     */
    default JsDouble toJsDouble() {
        try {
            return ((JsDouble) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsDouble(this);
        }
    }

    /**
     Returns true if this elem is a JsBigDec and satisfies the given predicate

     @param predicate the given predicate
     @return true if this JsValue is a JsBigDec and satisfies the predicate
     */
    default boolean isBigDec(Predicate<BigDecimal> predicate) {
        return isBigDec() && predicate.test(toJsBigDec().value);
    }

    /**
     @return true if this JsValue is a JsBigDec
     */
    default boolean isBigDec() {
        return false;
    }

    /**
     @return this JsValue as a JsBigDec
     @throws UserError if this JsValue is not a JsBigDec or a JsDouble
     */
    default JsBigDec toJsBigDec() {
        try {
            if (isDouble()) return JsBigDec.of(BigDecimal.valueOf(toJsDouble().value));
            else return ((JsBigDec) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsBigDec(this);
        }
    }

    /**
     Returns true if this elem is a JsLong and satisfies the given predicate

     @param predicate the given predicate
     @return true if this JsValue is a JsLong and satisfies the predicate
     */
    default boolean isLong(LongPredicate predicate) {
        return isLong() && predicate.test(toJsLong().value);
    }

    /**
     @return true if this JsValue is a JsLong
     */
    default boolean isLong() {
        return false;
    }

    /**
     @return this JsValue as a JsLong
     @throws UserError if this JsValue is not a JsLong or a JsInt
     */
    default JsLong toJsLong() {
        try {
            if (isInt()) return JsLong.of(toJsInt().value);
            else return ((JsLong) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsLong(this);
        }
    }

    /**
     @return true if this JsValue is a not a Json (neither JsObj nor JsArray)
     */
    default boolean isPrimitive() {
        return !isJson();
    }

    /**
     @return true if this JsValue is a Json (JsObj or JsArray)
     */
    default boolean isJson() {
        return isObj() || isArray();
    }

    /**
     @return true if this JsValue is a JsObj
     */
    default boolean isObj() {
        return false;
    }

    /**
     @return true if this JsValue is a JsArray
     */

    default boolean isArray() {
        return false;
    }

    /**
     Returns true if this elem is a Json and satisfies the given predicate

     @param predicate the given predicate
     @return true if this JsValue is a Json and satisfies the predicate
     */
    default boolean isJson(Predicate<Json<?>> predicate) {
        return isJson() && predicate.test(toJson());
    }

    /**
     @return this JsValue as a Json
     @throws UserError if this JsValue is not a JsObj or a JsArray
     */
    //S1452: Json<?> has only two possible types: JsObj or JsArr,
    @SuppressWarnings("squid:S1452")
    default Json<?> toJson() {

        if (isObj()) return toJsObj();
        else if (isArray()) return toJsArray();
        else throw UserError.isNotAJson(this);

    }

    /**
     @return this JsValue as a JsObj
     @throws UserError if this JsValue is not a JsObj
     */
    default JsObj toJsObj() {
        try {
            return ((JsObj) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsObj(this);
        }
    }

    /**
     @return this JsValue as a JsArray
     @throws UserError if this JsValue is not a JsArray
     */
    default JsArray toJsArray() {
        try {
            return ((JsArray) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsArray(this);
        }
    }

    /**
     Returns true if this elem is a JsObj and satisfies the given predicate

     @param predicate the given predicate
     @return true if this JsValue is a JsObj and satisfies the predicate
     */
    default boolean isObj(Predicate<JsObj> predicate) {
        return isObj() && predicate.test(toJsObj());
    }

    /**
     Returns true if this elem is a JsArray and satisfies the given predicate

     @param predicate the given predicate
     @return true if this JsValue is a JsArray and satisfies the predicate
     */
    default boolean isArray(Predicate<JsArray> predicate) {
        return isArray() && predicate.test(toJsArray());
    }

    /**
     @return this JsValue as a JsStr
     @throws UserError if this JsValue is not a JsStr
     */
    default JsNumber toJsNumber() {
        try {
            return ((JsNumber) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsNumber(this);
        }
    }

    /**
     Returns true if this elem is a JsBigInt and satisfies the given predicate

     @param predicate the given predicate
     @return true if this JsValue is a JsBigInt and satisfies the predicate
     */
    default boolean isBigInt(Predicate<BigInteger> predicate) {
        return isBigInt() && predicate.test(toJsBigInt().value);
    }

    /**
     @return true if this JsValue is a JsBigInt
     */
    default boolean isBigInt() {
        return false;
    }

    /**
     @return this JsValue as a JsBigInt
     @throws UserError if this JsValue is not a JsBigInt or JsLong or JsInt
     */
    default JsBigInt toJsBigInt() {
        try {
            if (isInt()) return JsBigInt.of(BigInteger.valueOf(toJsInt().value));
            if (isLong()) return JsBigInt.of(BigInteger.valueOf(toJsLong().value));
            return ((JsBigInt) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsBigInt(this);
        }
    }


    /**
     Returns true if this elem is a JsStr and satisfies the given predicate

     @param predicate the given predicate
     @return true if this JsValue is a JsStr and satisfies the predicate
     */
    default boolean isStr(Predicate<String> predicate) {
        return isStr() && predicate.test(toJsStr().value);
    }

    /**
     @return true if this JsValue is a JsStr
     */
    default boolean isStr() {
        return false;
    }

    /**
     @return this JsValue as a JsStr
     @throws UserError if this JsValue is not a JsStr
     */
    default JsStr toJsStr() {
        try {
            return ((JsStr) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsString(this);
        }
    }

    /**
     @return true if this element is an integral number (JsInt or JsLong or JsBigInt)
     */
    default boolean isIntegral() {
        return isInt() || isLong() || isBigInt();
    }

    /**
     @return true if this element is an decimal number (JsDouble or JsBigDec)
     */
    default boolean isDecimal() {
        return isDouble() || isBigDec();
    }

    /**
     @return true if this element is not null
     */
    default boolean isNotNull() {
        return !isNull();
    }

    /**
     @return true if this element is null
     */
    default boolean isNull() {
        return false;
    }

    /**
     @return true if this element is not JsNothing
     */
    default boolean isNotNothing() {
        return !isNothing();
    }

    /**
     @return true if this element is JsNothing
     */
    default boolean isNothing() {
        return false;
    }

    /**
     @return true if this element is  not a number
     */
    default boolean isNotNumber() {
        return !isNumber();
    }

    /**
     @return true if this element is a number
     */
    default boolean isNumber() {
        return isInt() || isLong() || isBigInt() || isDouble() || isBigDec();
    }

    /**
     returns the specified default value if null or the same this object

     @param value the default value
     @return a json value
     */
    default JsValue ifNull(final JsValue value) {
        if (isNull()) return requireNonNull(value);
        return this;
    }

    /**
     returns the specified default value if nothing or the same this object

     @param value the default value
     @return a json value
     */
    default JsValue ifNothing(final JsValue value) {
        if (isNothing()) return requireNonNull(value);
        return this;
    }
}
