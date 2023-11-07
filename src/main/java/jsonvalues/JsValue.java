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
 * Represents a JSON element of any type. Every JSON type implements this interface.
 * This interface implements two kinds of methods:
 * <p>
 * 1. Classificatory methods, which start with the prefix <b>isXXX</b>.
 * 2. Accessory methods to convert the JsValue to the real implementation, which start with the prefix toJsXXX.
 */
public sealed interface JsValue permits JsNothing, JsPrimitive, Json {

    /**
     * Returns this JsValue as a JsBool.
     *
     * @return This JsValue as a JsBool.
     * @throws UserError if this JsValue is not a JsBool or cannot be converted to JsBool.
     */
    default JsBool toJsBool() {
        try {
            return ((JsBool) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsBool(this);
        }
    }

    /**
     * Returns this JsValue as a JsPrimitive.
     *
     * @return This JsValue as a JsPrimitive.
     * @throws UserError if this JsValue is a Json container (object or array).
     */
    JsPrimitive toJsPrimitive();

    /**
     * Returns true if this JsValue is a JsBinary or a JsString with a value that is an array of bytes encoded in base64.
     *
     * @return True if this JsValue is a JsBinary or a JsString with a value that is an array of bytes encoded in base64.
     */
    default boolean isBinary() {
        return false;
    }

    /**
     * Returns true if this JsValue is a JsInstant or a JsString with a value that is a date formatted in ISO-8601.
     *
     * @return True if this JsValue is a JsInstant or a JsString with a value that is a date formatted in ISO-8601.
     */
    default boolean isInstant() {
        return false;
    }

    /**
     * Returns true if this element is a JsInstant and satisfies the given predicate.
     *
     * @param predicate The given predicate.
     * @return True if this JsValue is a JsInstant and satisfies the predicate.
     */
    default boolean isInstant(Predicate<Instant> predicate) {
        return isInstant() && predicate.test(this.toJsInstant().value);
    }

    /**
     * Returns true if this JsValue is a JsBool.
     *
     * @return True if this JsValue is a JsBool.
     */
    default boolean isBool() {
        return false;
    }

    /**
     * Returns true if this JsValue is of the same type as the given JsValue.
     *
     * @param that The given JsValue.
     * @return True if this JsValue and the given JsValue have the same type.
     */
    default boolean isSameType(final JsValue that) {
        return this.getClass() == requireNonNull(that).getClass();
    }

    /**
     * Returns true if this JsValue is a JsBool and its value is true.
     *
     * @return True if this JsValue is a JsBool and its value is true.
     */
    default boolean isTrue() {
        return false;
    }

    /**
     * Returns true if this JsValue is a JsBool and its value is false.
     *
     * @return True if this JsValue is a JsBool and its value is false.
     */
    default boolean isFalse() {
        return false;
    }

    /**
     * Returns true if this element is a JsInt and satisfies the given predicate.
     *
     * @param predicate The given predicate.
     * @return True if this JsValue is a JsInt and satisfies the predicate.
     */
    default boolean isInt(IntPredicate predicate) {
        return isInt() && predicate.test(toJsInt().value);
    }

    /**
     * Returns true if this JsValue is a JsInt.
     *
     * @return True if this JsValue is a JsInt.
     */
    default boolean isInt() {
        return false;
    }

    /**
     * Returns this JsValue as a JsInt.
     *
     * @return This JsValue as a JsInt.
     * @throws UserError if this JsValue is not a JsInt or cannot be converted to JsInt.
     */
    default JsInt toJsInt() {
        try {
            return ((JsInt) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsInt(this);
        }
    }

    /**
     * Returns this JsValue as a JsInstant.
     *
     * @return This JsValue as a JsInstant.
     * @throws UserError if this JsValue is not a JsInstant or cannot be converted to JsInstant,
     *                   e.g., if it's not a JsInstant or a JsString with a date formatted in ISO-8601.
     */
    default JsInstant toJsInstant() {
        if (this instanceof JsInstant) return ((JsInstant) this);
        if (this instanceof JsStr) {
            var instant = JsStr.instantPrism.getOptional.apply(((JsStr) this).value);
            if (instant.isPresent()) return JsInstant.of(instant.get());
        }
        throw UserError.isNotAJsInstant(this);
    }

    /**
     * Returns this JsValue as a JsBinary.
     *
     * @return This JsValue as a JsBinary.
     * @throws UserError if this JsValue is not a JsBinary or cannot be converted to JsBinary,
     *                   e.g., if it's not a JsBinary or a JsString with a value encoded in base64.
     */
    default JsBinary toJsBinary() {
        if (this instanceof JsBinary) return ((JsBinary) this);
        if (this instanceof JsStr) {
            Optional<byte[]> bytes = JsStr.base64Prism.getOptional.apply(((JsStr) this).value);
            if (bytes.isPresent()) return JsBinary.of(bytes.get());
        }
        throw UserError.isNotAJsBinary(this);
    }

    /**
     * Returns true if this element is a JsDouble and satisfies the given predicate.
     *
     * @param predicate The given predicate.
     * @return True if this JsValue is a JsDouble and satisfies the predicate.
     */
    default boolean isDouble(DoublePredicate predicate) {
        return isDouble() && predicate.test(toJsDouble().value);
    }

    /**
     * Returns true if this JsValue is a JsDouble.
     *
     * @return True if this JsValue is a JsDouble.
     */
    default boolean isDouble() {
        return false;
    }

    /**
     * Returns this JsValue as a JsDouble.
     *
     * @return This JsValue as a JsDouble.
     * @throws UserError if this JsValue is not a JsDouble or cannot be converted to JsDouble.
     */
    default JsDouble toJsDouble() {
        try {
            return ((JsDouble) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsDouble(this);
        }
    }

    /**
     * Returns true if this element is a JsBigDec and satisfies the given predicate.
     *
     * @param predicate The given predicate.
     * @return True if this JsValue is a JsBigDec and satisfies the predicate.
     */
    default boolean isBigDec(Predicate<BigDecimal> predicate) {
        return isBigDec() && predicate.test(toJsBigDec().value);
    }

    /**
     * Returns true if this JsValue is a JsBigDec.
     *
     * @return True if this JsValue is a JsBigDec.
     */
    default boolean isBigDec() {
        return false;
    }

    /**
     * Returns this JsValue as a JsBigDec.
     *
     * @return This JsValue as a JsBigDec.
     * @throws UserError if this JsValue is not a JsBigDec or cannot be converted to JsBigDec.
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
     * Returns true if this element is a JsLong and satisfies the given predicate.
     *
     * @param predicate The given predicate.
     * @return True if this JsValue is a JsLong and satisfies the predicate.
     */
    default boolean isLong(LongPredicate predicate) {
        return isLong() && predicate.test(toJsLong().value);
    }

    /**
     * Returns this JsValue as a JsLong.
     *
     * @return This JsValue as a JsLong.
     * @throws UserError if this JsValue is not a JsLong, JsInt, or cannot be converted to JsLong.
     */
    default boolean isLong() {
        return false;
    }

    /**
     * Returns this JsValue as a JsLong.
     *
     * @return This JsValue as a JsLong.
     * @throws UserError if this JsValue is not a JsLong, JsInt, or cannot be converted to JsLong.
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
     * Returns true if this JsValue is not a Json (neither JsObj nor JsArray).
     *
     * @return True if this JsValue is not a Json (neither JsObj nor JsArray).
     */
    default boolean isPrimitive() {
        return !isJson();
    }

    /**
     * Returns true if this JsValue is a Json (JsObj or JsArray).
     *
     * @return True if this JsValue is a Json (JsObj or JsArray).
     */
    default boolean isJson() {
        return isObj() || isArray();
    }

    /**
     * Returns true if this JsValue is a JsObj.
     *
     * @return True if this JsValue is a JsObj.
     */
    default boolean isObj() {
        return false;
    }

    /**
     * Returns true if this JsValue is a JsArray.
     *
     * @return True if this JsValue is a JsArray.
     */
    default boolean isArray() {
        return false;
    }

    /**
     * Returns true if this element is a Json and satisfies the given predicate.
     *
     * @param predicate The given predicate.
     * @return True if this JsValue is a Json and satisfies the predicate.
     */
    default boolean isJson(Predicate<Json<?>> predicate) {
        return isJson() && predicate.test(toJson());
    }

    /**
     * Returns this JsValue as a Json.
     *
     * @return This JsValue as a Json.
     * @throws UserError if this JsValue is not a JsObj or JsArray or cannot be converted to Json.
     */
    //S1452: Json<?> has only two possible types: JsObj or JsArr,
    default Json<?> toJson() {

        if (isObj()) return toJsObj();
        else if (isArray()) return toJsArray();
        else throw UserError.isNotAJson(this);

    }

    /**
     * Returns this JsValue as a JsObj.
     *
     * @return This JsValue as a JsObj.
     * @throws UserError if this JsValue is not a JsObj or cannot be converted to JsObj.
     */
    default JsObj toJsObj() {
        try {
            return ((JsObj) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsObj(this);
        }
    }

    /**
     * Returns this JsValue as a JsArray.
     *
     * @return This JsValue as a JsArray.
     * @throws UserError if this JsValue is not a JsArray or cannot be converted to JsArray.
     */
    default JsArray toJsArray() {
        try {
            return ((JsArray) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsArray(this);
        }
    }

    /**
     * Returns true if this element is a JsObj and satisfies the given predicate.
     *
     * @param predicate The given predicate.
     * @return True if this JsValue is a JsObj and satisfies the predicate.
     */
    default boolean isObj(Predicate<JsObj> predicate) {
        return isObj() && predicate.test(toJsObj());
    }

    /**
     * Returns true if this element is a JsArray and satisfies the given predicate.
     *
     * @param predicate The given predicate.
     * @return True if this JsValue is a JsArray and satisfies the predicate.
     */
    default boolean isArray(Predicate<JsArray> predicate) {
        return isArray() && predicate.test(toJsArray());
    }

    /**
     * Returns this JsValue as a JsStr.
     *
     * @return This JsValue as a JsStr.
     * @throws UserError if this JsValue is not a JsStr or cannot be converted to JsStr.
     */
    default JsNumber toJsNumber() {
        try {
            return ((JsNumber) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsNumber(this);
        }
    }

    /**
     * Returns true if this element is a JsBigInt and satisfies the given predicate.
     *
     * @param predicate The given predicate.
     * @return True if this JsValue is a JsBigInt and satisfies the predicate.
     */
    default boolean isBigInt(Predicate<BigInteger> predicate) {
        return isBigInt() && predicate.test(toJsBigInt().value);
    }

    /**
     * Returns true if this JsValue is a JsBigInt.
     *
     * @return True if this JsValue is a JsBigInt.
     */
    default boolean isBigInt() {
        return false;
    }

    /**
     * Returns this JsValue as a JsBigInt.
     *
     * @return This JsValue as a JsBigInt.
     * @throws UserError if this JsValue is not a JsBigInt, JsLong, JsInt, or cannot be converted to JsBigInt.
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
     * Returns true if this element is a JsStr and satisfies the given predicate.
     *
     * @param predicate The given predicate.
     * @return True if this JsValue is a JsStr and satisfies the predicate.
     */
    default boolean isStr(Predicate<String> predicate) {
        return isStr() && predicate.test(toJsStr().value);
    }

    /**
     * Returns true if this JsValue is a JsStr.
     *
     * @return True if this JsValue is a JsStr.
     */
    default boolean isStr() {
        return false;
    }

    /**
     * Returns this JsValue as a JsStr.
     *
     * @return This JsValue as a JsStr.
     * @throws UserError if this JsValue is not a JsStr or cannot be converted to JsStr.
     */
    default JsStr toJsStr() {
        try {
            return ((JsStr) this);
        } catch (ClassCastException e) {
            throw UserError.isNotAJsString(this);
        }
    }

    /**
     * Returns true if this element is an integral number (JsInt, JsLong, or JsBigInt).
     *
     * @return True if this element is an integral number (JsInt, JsLong, or JsBigInt).
     */
    default boolean isIntegral() {
        return isInt() || isLong() || isBigInt();
    }

    /**
     * Returns true if this element is a decimal number (JsDouble or JsBigDec).
     *
     * @return True if this element is a decimal number (JsDouble or JsBigDec).
     */
    default boolean isDecimal() {
        return isDouble() || isBigDec();
    }

    /**
     * Return true if this element is not null.
     *
     * @return True if this element is not null.
     */
    default boolean isNotNull() {
        return !isNull();
    }

    /**
     * Returns true if this element is null.
     *
     * @return True if this element is null.
     */
    default boolean isNull() {
        return false;
    }

    /**
     * Returns true if this element is not JsNothing.
     *
     * @return True if this element is not JsNothing.
     */
    default boolean isNotNothing() {
        return !isNothing();
    }

    /**
     * Returns true if this element is JsNothing.
     *
     * @return True if this element is JsNothing.
     */
    default boolean isNothing() {
        return false;
    }

    /**
     * Returns true if this element is not a number.
     *
     * @return True if this element is not a number.
     */
    default boolean isNotNumber() {
        return !isNumber();
    }

    /**
     * Returns true if this element is a number.
     *
     * @return True if this element is a number.
     */
    default boolean isNumber() {
        return isInt() || isLong() || isBigInt() || isDouble() || isBigDec();
    }

    /**
     * Returns the specified default value if null or the same this object.
     *
     * @param value The default value.
     * @return A JSON value.
     */
    default JsValue ifNull(final JsValue value) {
        if (isNull()) return requireNonNull(value);
        return this;
    }

    /**
     * Returns the specified default value if nothing or the same this object.
     *
     * @param value The default value.
     * @return A JSON value.
     */
    default JsValue ifNothing(final JsValue value) {
        if (isNothing()) return requireNonNull(value);
        return this;
    }
}
