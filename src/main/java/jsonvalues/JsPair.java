package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.*;

import static java.util.Objects.requireNonNull;

/**
 Immutable pair which represents a JsElem of a Json and its JsPath location: (path, element).
 */
public final class JsPair
{

    /**
     the json element.
     */
    public final JsValue value;


    /**
     the location of the element.
     */
    public final JsPath path;


    private JsPair(final JsPath path,
                   final JsValue value
                  )
    {
        this.path = path;
        this.value = value;
    }

    /**
     Declarative way of implementing {@code  if(pair.elem.isInt()) return Pair.of(pair.path, pair.elem.asJsInt().map(operator)) else return pair}
     @param operator the function to be applied to map the integer
     @return the same this instance if the JsElem is not a JsInt or a new pair
     */
    public JsPair mapIfInt(IntUnaryOperator operator)
    {

        if (this.value.isInt()) return of(path,
                                          value.toJsInt()
                                               .map(operator)
                                         );


        return this;
    }

    /**
     Declarative way of implementing {@code  if(pair.elem.isStr()) return Pair.of(pair.path, pair.elem.asJsStr().map(mapFn)) else return pair}
     @param fn the function to be applied to map the string of the JsStr
     @return the same this instance if the JsElem is not a JsStr or a new pair
     */
    public JsPair mapIfStr(final UnaryOperator<String> fn)
    {

        if (this.value.isStr()) return of(path,
                                          value.toJsStr()
                                               .map(requireNonNull(fn))
                                         );
        return this;
    }


    /**
     Returns a json pair from the path and the json element.
     @param path the JsPath object
     @param elem the JsElem
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final JsValue elem
                           )
    {
        return new JsPair(requireNonNull(requireNonNull(path)),
                          requireNonNull(requireNonNull(elem))
        );
    }

    /**
     Returns a json pair from the path and the integer.
     @param path the JsPath
     @param i the integer
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final int i
                           )
    {
        return new JsPair(requireNonNull(path),
                          JsInt.of(i)
        );
    }

    /**
     Returns a json pair from the path and the double.
     @param path the JsPath
     @param d the double
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final double d
                           )
    {
        return new JsPair(requireNonNull(path),
                          JsDouble.of(d)
        );
    }

    /**
     Returns a json pair from the path and the long.
     @param path the JsPath
     @param l the long
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final long l
                           )
    {
        return new JsPair(requireNonNull(path),
                          JsLong.of(l)
        );
    }

    /**
     Returns a json pair from the path and the boolean.
     @param path the JsPath
     @param b the boolean
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final boolean b
                           )
    {
        return new JsPair(requireNonNull(path),
                          JsBool.of(b)
        );
    }

    /**
     Returns a json pair from the path and the string.
     @param path the JsPath
     @param s the string
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final String s
                           )
    {
        return new JsPair(requireNonNull(path),
                          JsStr.of(requireNonNull(s))
        );
    }

    /**
     Returns a json pair from the path and the big decimal.
     @param path the JsPath
     @param bd the big decimal
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final BigDecimal bd
                           )
    {
        return new JsPair(requireNonNull(path),
                          JsBigDec.of(requireNonNull(bd))
        );
    }

    /**
     Returns a json pair from the path and the big integer.
     @param path the JsPath
     @param bi the big integer
     @return an immutable JsPair
     */
    public static JsPair of(final JsPath path,
                            final BigInteger bi
                           )
    {
        return new JsPair(requireNonNull(path),
                          JsBigInt.of(requireNonNull(bi))
        );
    }


    /**

     @return string representation of this pair: (path, elem)
     */
    @Override
    public String toString()
    {
        return String.format("(%s, %s)",
                             path,
                             value
                            );
    }

    /**
     Returns true if that is a pair and both represents the same element at the same location.
     @param that the reference object with which to compare.
     @return true if this.element.equals(that.element) and this.path.equals(that.path)
     */
    @Override
    public boolean equals(final @Nullable Object that)
    {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        final JsPair thatPair = (JsPair) that;
        return Objects.equals(value,
                              thatPair.value
                             ) &&
        Objects.equals(path,
                       thatPair.path
                      );
    }

    /**
     Returns the hashcode of this pair.
     @return the hashcode of this pair
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(value,
                            path
                           );
    }


    /**
     Returns a new pair with the same path and a new element result of applying the mapping function
     @param map the mapping function which maps the JsElem
     @return a new JsPair
     */
    public JsPair mapElem(final UnaryOperator<JsValue> map)
    {
        return JsPair.of(this.path,
                         requireNonNull(map).apply(this.value)
                        );
    }

    /**
     Returns a new pair with the same element and a new path result of applying the mapping function
     @param map the mapping function which maps the JsPath
     @return a new JsPair
     */
    public JsPair mapPath(final UnaryOperator<JsPath> map)
    {
        return JsPair.of(requireNonNull(map).apply(this.path),
                         this.value
                        );
    }

    /**
     Declarative way of implementing an if(json)return T; else return T; where T is computed by the
     given functions
     @param ifJson function that returns a T and is invoked if the element of this pair is a json
     @param ifNotJson function that returns a T and is invoked if the element of this pair is not a json
     @param <T> type of the result
     @return object of type T
     */
    public <T> T ifJsonElse(final BiFunction<JsPath, Json<?>, T> ifJson,
                            final BiFunction<JsPath, JsValue, T> ifNotJson
                           )
    {

        return value.isJson() ? requireNonNull(ifJson).apply(path,
                                                             value.toJson()
                                                            ) : requireNonNull(ifNotJson).apply(path,
                                                                                                value
                                                                                              );
    }

    /**
     Declarative way of implementing an if(obj)return T; else if(array) return T;  else return T; where
     T is computed by the given functions
     @param ifJsOb function that returns a T and is invoked if the element of this pair is a json object
     @param ifJsArr function that returns a T and is invoked if the element of this pair is not a json array
     @param ifNotJson function that returns a T and is invoked if the element of this pair is not a json
     @param <T> type of the result
     @return object of type T
     */
    public <T> T ifJsonElse(final BiFunction<JsPath, JsObj, T> ifJsOb,
                            final BiFunction<JsPath, JsArray, T> ifJsArr,
                            final BiFunction<JsPath, JsValue, T> ifNotJson
                           )
    {
        if (value.isObj()) return requireNonNull(ifJsOb).apply(path,
                                                               value.toJsObj()
                                                              );
        if (value.isArray()) return requireNonNull(ifJsArr).apply(path,
                                                                  value.toJsArray()
                                                                 );
        return requireNonNull(ifNotJson).apply(path,
                                               value
                                              );
    }

    /**
     Declarative way of implementing an if-else. This pair is tested on a given predicate, executing
     the ifTrue function when true and the ifFalse function otherwise
     @param predicate the given predicate
     @param ifTrue function to invoked when the predicate is evaluated to true, taking this pair as a parameter
     @param ifFalse function to invoked when the predicate is evaluated to false, taking this pair as a parameter
     @param <R> the type of the returned value
     @return an object of type R
     */
    public <R> R ifElse(final Predicate<? super JsPair> predicate,
                        final Function<? super JsPair, R> ifTrue,
                        final Function<? super JsPair, R> ifFalse
                       )
    {
        return requireNonNull(predicate).test(this) ? requireNonNull(ifTrue).apply(this) : requireNonNull(ifFalse).apply(this);
    }

    /**
     Consumes this pair if it's evaluated to true o a given predicate
     @param predicate the given predicate
     @param consumer the consumer that it's invoked if the predicate is evaluated to true
     */
    public void consumeIf(final Predicate<JsPair> predicate,
                          final Consumer<JsPair> consumer
                         )
    {
        if (requireNonNull(predicate).test(this)) requireNonNull(consumer).accept(this);
    }

}
