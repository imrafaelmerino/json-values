package jsonvalues.optics;

import jsonvalues.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public class JsPrisms
{


  public static JsPrism<String> str =
    new JsPrism<>(s -> s.isStr() ? Optional.of(s.toJsStr().value) : Optional.empty(),
                  JsStr::of
    );


  public static JsPrism<JsObj> obj =
    new JsPrism<>(s -> s.isObj() ? Optional.of(s.toJsObj()) : Optional.empty(),
                o -> o
    );


  public static JsPrism<JsArray> array =
    new JsPrism<>(
      s -> s.isArray() ? Optional.of(s.toJsArray()) : Optional.empty(),
      a -> a
    );

  public static JsPrism<Boolean> bool =
    new JsPrism<>(s -> s.isBool() ? Optional.of(s.toJsBool().value) : Optional.empty(),
                  JsBool::of
    );

  public static JsPrism<Integer> intNum =
    new JsPrism<>(s -> s.isInt() ? Optional.of(s.toJsInt().value) : Optional.empty(),
                  JsInt::of
    );

  public static JsPrism<BigInteger> bigIntNum =
    new JsPrism<>(s ->
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

  public static JsPrism<Long> longNum = new JsPrism<>(s ->
                                                  {
                                                    if (s.isLong())
                                                      return Optional.of(s.toJsLong().value);
                                                    if (s.isInt())
                                                      return Optional.of((long) s.toJsInt().value);
                                                    return Optional.empty();
                                                  },
                                                      JsLong::of
  );


  public static JsPrism<Double> doubleNum = new JsPrism<>(s ->
                                                      {
                                                        if (s.isLong())
                                                          return Optional.of((double) s.toJsLong().value);
                                                        if (s.isInt())
                                                          return Optional.of((double) s.toJsInt().value);
                                                        if (s.isDouble())
                                                          return Optional.of(s.toJsDouble().value);
                                                        if(s.isDecimal()) return Optional.of(s.toJsBigDec().value.doubleValue());
                                                        return Optional.empty();
                                                      },
                                                          JsDouble::of
  );


  public static JsPrism<BigDecimal> decimalNum = new JsPrism<>(s ->
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
