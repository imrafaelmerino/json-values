package jsonvalues.optics;

import com.sun.org.apache.xpath.internal.operations.Bool;
import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public class Prisms
{


  public static Prism<String> str =
    new Prism<>(s -> s.isStr() ? Optional.of(s.toJsStr().value) : Optional.empty(),
                JsStr::of
    );


  public static Prism<JsObj> obj =
    new Prism<>(s -> s.isObj() ? Optional.of(s.toJsObj()) : Optional.empty(),
                o -> o
    );


  public static Prism<JsArray> array =
    new Prism<>(
      s -> s.isArray() ? Optional.of(s.toJsArray()) : Optional.empty(),
      a -> a
    );

  public static Prism<Boolean> bool =
    new Prism<>(s -> s.isBool() ? Optional.of(s.toJsBool().value) : Optional.empty(),
                JsBool::of
    );

  public static Prism<Integer> intNum =
    new Prism<>(s -> s.isInt() ? Optional.of(s.toJsInt().value) : Optional.empty(),
                JsInt::of
    );

  public static Prism<BigInteger> bigIntNum =
    new Prism<>(s ->
                {
                  if (s.isLong())
                    return Optional.of(BigInteger.valueOf(s.toJsLong().value));
                  if (s.isInt())
                    return Optional.of(BigInteger.valueOf(s.toJsInt().value));
                  if (s.isBigInt())
                    return Optional.of(s.toJsBigInt().value);
                  return Optional.empty();
                },
                JsBigInt::of
    );

  public static Prism<Long> longNum = new Prism<>(s ->
                                                  {
                                                    if (s.isLong())
                                                      return Optional.of(s.toJsLong().value);
                                                    if (s.isInt())
                                                      return Optional.of((long) s.toJsInt().value);
                                                    return Optional.empty();
                                                  },
                                                  JsLong::of
  );


  public static Prism<Double> doubleNum = new Prism<>(s ->
                                                      {
                                                        if (s.isLong())
                                                          return Optional.of((double) s.toJsLong().value);
                                                        if (s.isInt())
                                                          return Optional.of((double) s.toJsInt().value);
                                                        if (s.isDouble())
                                                          return Optional.of(s.toJsDouble().value);
                                                        return Optional.empty();
                                                      },
                                                      JsDouble::of
  );


  public static Prism<BigDecimal> decimalNum = new Prism<>(s ->
                                                           {
                                                             if (s.isLong())
                                                               return Optional.of(BigDecimal.valueOf(s.toJsLong().value));
                                                             if (s.isInt())
                                                               return Optional.of(BigDecimal.valueOf(s.toJsInt().value));
                                                             if (s.isBigInt())
                                                               return Optional.of(new BigDecimal(s.toJsBigInt().value));
                                                             if (s.isBigDec())
                                                               return Optional.of(s.toJsBigDec().value);
                                                             return Optional.empty();
                                                           },
                                                           JsBigDec::of
  );


}
