package jsonvalues;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class JsOptics {

  public static JsObjOptics obj = new JsObjOptics();
  public static JsArrayOptics array = new JsArrayOptics();

  public static class JsArrayOptics {
    public final JsArrayOptionals optional = new JsArrayOptionals();
    public final JsArrayLenses lens = new JsArrayLenses();
  }

  public static class JsArrayLenses {

    public JsValueLens<JsArray> value(final JsPath path) {
      return new JsValueLens<>(Objects.requireNonNull(path));
    }

    public JsValueLens<JsArray> value(final int index) {
      return new JsValueLens<>(Objects.requireNonNull(JsPath.fromIndex(index)));
    }

    public JsStrLens<JsArray> str(final JsPath path) {
      return new JsStrLens<>(Objects.requireNonNull(path));
    }

    public JsStrLens<JsArray> str(final int index) {
      return new JsStrLens<>(Objects.requireNonNull(JsPath.fromIndex(index)));
    }

    public JsBoolLens<JsArray> bool(final JsPath path) {
      return new JsBoolLens<>(Objects.requireNonNull(path));
    }

    public JsBoolLens<JsArray> bool(final int index) {
      return new JsBoolLens<>(Objects.requireNonNull(JsPath.fromIndex(index)));
    }

    public JsLongLens<JsArray> longNum(final JsPath path) {
      return new JsLongLens<>(Objects.requireNonNull(path));
    }

    public JsLongLens<JsArray> longNum(final int index) {
      return new JsLongLens<>(Objects.requireNonNull(JsPath.fromIndex(index)));
    }

    public JsIntLens<JsArray> intNum(final JsPath path) {
      return new JsIntLens<>(Objects.requireNonNull(path));
    }

    public JsIntLens<JsArray> intNum(final int index) {
      return new JsIntLens<>(Objects.requireNonNull(JsPath.fromIndex(index)));
    }

    public JsDoubleLens<JsArray> doubleNum(final JsPath path) {
      return new JsDoubleLens<>(Objects.requireNonNull(path));
    }

    public JsDoubleLens<JsArray> doubleNum(final int index) {
      return new JsDoubleLens<>(Objects.requireNonNull(JsPath.fromIndex(index)));
    }

    public JsDecimalLens<JsArray> decimalNum(final JsPath path) {
      return new JsDecimalLens<>(Objects.requireNonNull(path));
    }

    public JsDecimalLens<JsArray> decimalNum(final int index) {
      return new JsDecimalLens<>(Objects.requireNonNull(JsPath.fromIndex(index)));
    }

    public JsBigIntLens<JsArray> integralNum(final JsPath path) {
      return new JsBigIntLens<>(Objects.requireNonNull(path));
    }

    public JsBigIntLens<JsArray> integralNum(final int index) {
      return new JsBigIntLens<>(Objects.requireNonNull(JsPath.fromIndex(index)));
    }

    public JsObjLens<JsArray> obj(final JsPath path) {
      return new JsObjLens<>(Objects.requireNonNull(path));
    }

    public JsObjLens<JsArray> obj(final int index) {
      return new JsObjLens<>(Objects.requireNonNull(JsPath.fromIndex(index)));
    }

    public JsArrayLens<JsArray> array(final JsPath path) {
      return new JsArrayLens<>(Objects.requireNonNull(path));
    }

    public JsArrayLens<JsArray> array(final int index) {
      return new JsArrayLens<>(Objects.requireNonNull(JsPath.fromIndex(index)));
    }
  }

  public static class JsObjOptics {
    public final JsObjOptionals optional = new JsObjOptionals();
    public final JsObjLenses lens = new JsObjLenses();
  }

  public static class JsArrayOptionals {

    public Option<JsArray, String> str(final JsPath path) {
      return JsOptics.array.lens.value(Objects.requireNonNull(path)).compose(JsStr.prism);
    }

    public Option<JsArray, String> str(final int index) {
      return JsOptics.array.lens.value(index).compose(JsStr.prism);
    }

    public Option<JsArray, Boolean> bool(final JsPath path) {
      return JsOptics.array.lens.value(Objects.requireNonNull(path)).compose(JsBool.prism);
    }

    public Option<JsArray, Boolean> bool(final int index) {
      return JsOptics.array.lens.value(index).compose(JsBool.prism);
    }

    public Option<JsArray, Long> longNum(final JsPath path) {
      return JsOptics.array.lens.value(Objects.requireNonNull(path)).compose(JsLong.prism);
    }

    public Option<JsArray, Long> longNum(final int index) {
      return JsOptics.array.lens.value(index).compose(JsLong.prism);
    }


    public Option<JsArray, Integer> intNum(final JsPath path) {
      return JsOptics.array.lens.value(Objects.requireNonNull(path)).compose(JsInt.prism);
    }

    public Option<JsArray, Integer> intNum(final int index) {
      return JsOptics.array.lens.value(index).compose(JsInt.prism);
    }

    public Option<JsArray, Double> doubleNum(final JsPath path) {
      return JsOptics.array.lens.value(Objects.requireNonNull(path)).compose(JsDouble.prism);
    }

    public Option<JsArray, Double> doubleNum(final int index) {
      return JsOptics.array.lens.value(index).compose(JsDouble.prism);
    }

    public Option<JsArray, BigDecimal> decimalNum(final JsPath path) {
      return JsOptics.array.lens.value(Objects.requireNonNull(path)).compose(JsBigDec.prism);
    }

    public Option<JsArray, BigDecimal> decimalNum(final int index) {
      return JsOptics.array.lens.value(index).compose(JsBigDec.prism);
    }

    public Option<JsArray, BigInteger> integralNum(final JsPath path) {
      return JsOptics.array.lens.value(Objects.requireNonNull(path)).compose(JsBigInt.prism);
    }

    public Option<JsArray, BigInteger> integralNum(final int index) {
      return JsOptics.array.lens.value(index).compose(JsBigInt.prism);
    }

    public Option<JsArray, JsObj> obj(final JsPath path) {
      return JsOptics.array.lens.value(Objects.requireNonNull(path)).compose(JsObj.prism);
    }

    public Option<JsArray, JsObj> obj(final int index) {
      return JsOptics.array.lens.value(index).compose(JsObj.prism);
    }

    public Option<JsArray, JsArray> array(final JsPath path) {
      return JsOptics.array.lens.value(Objects.requireNonNull(path)).compose(JsArray.prism);
    }

    public Option<JsArray, JsArray> array(final int index) {
      return JsOptics.array.lens.value(index).compose(JsArray.prism);
    }

  }

  public static class JsObjOptionals {

    public Option<JsObj, String> str(final JsPath path) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(path)).compose(JsStr.prism);
    }

    public Option<JsObj, String> str(final String key) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(key)).compose(JsStr.prism);
    }

    public Option<JsObj, Boolean> bool(final JsPath path) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(path)).compose(JsBool.prism);
    }

    public Option<JsObj, Boolean> bool(final String key) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(key)).compose(JsBool.prism);
    }

    public Option<JsObj, Long> longNum(final JsPath path) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(path)).compose(JsLong.prism);
    }

    public Option<JsObj, Long> longNum(final String key) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(key)).compose(JsLong.prism);
    }

    public Option<JsObj, Integer> intNum(final JsPath path) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(path)).compose(JsInt.prism);
    }

    public Option<JsObj, Integer> intNum(final String key) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(key)).compose(JsInt.prism);
    }

    public Option<JsObj, Double> doubleNum(final JsPath path) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(path)).compose(JsDouble.prism);
    }

    public Option<JsObj, Double> doubleNum(final String key) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(key)).compose(JsDouble.prism);
    }

    public Option<JsObj, BigDecimal> decimalNum(final JsPath path) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(path)).compose(JsBigDec.prism);
    }

    public Option<JsObj, BigDecimal> decimalNum(final String key) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(key)).compose(JsBigDec.prism);
    }

    public Option<JsObj, BigInteger> integralNum(final JsPath path) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(path)).compose(JsBigInt.prism);
    }

    public Option<JsObj, BigInteger> integralNum(final String key) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(key)).compose(JsBigInt.prism);
    }

    public Option<JsObj, JsObj> obj(final JsPath path) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(path)).compose(JsObj.prism);
    }

    public Option<JsObj, JsObj> obj(final String key) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(key)).compose(JsObj.prism);
    }

    public Option<JsObj, JsArray> array(final JsPath path) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(path)).compose(JsArray.prism);
    }

    public Option<JsObj, JsArray> array(final String key) {
      return JsOptics.obj.lens.value(Objects.requireNonNull(key)).compose(JsArray.prism);
    }

  }

  public static class JsObjLenses {

    public JsValueLens<JsObj> value(final JsPath path) {
      return new JsValueLens<>(Objects.requireNonNull(path));
    }

    public JsValueLens<JsObj> value(final String key) {
      return new JsValueLens<>(Objects.requireNonNull(JsPath.fromKey(key)));
    }

    public JsStrLens<JsObj> str(final JsPath path) {
      return new JsStrLens<>(Objects.requireNonNull(path));
    }

    public JsStrLens<JsObj> str(final String key) {
      return new JsStrLens<>(Objects.requireNonNull(JsPath.fromKey(key)));
    }

    public JsBoolLens<JsObj> bool(final JsPath path) {
      return new JsBoolLens<>(Objects.requireNonNull(path));
    }

    public JsBoolLens<JsObj> bool(final String key) {
      return new JsBoolLens<>(Objects.requireNonNull(JsPath.fromKey(key)));
    }

    public JsLongLens<JsObj> longNum(final JsPath path) {
      return new JsLongLens<>(Objects.requireNonNull(path));
    }

    public JsLongLens<JsObj> longNum(final String key) {
      return new JsLongLens<>(Objects.requireNonNull(JsPath.fromKey(key)));
    }

    public JsIntLens<JsObj> intNum(final JsPath path) {
      return new JsIntLens<>(Objects.requireNonNull(path));
    }

    public JsIntLens<JsObj> intNum(final String key) {
      return new JsIntLens<>(Objects.requireNonNull(JsPath.fromKey(key)));
    }

    public JsDoubleLens<JsObj> doubleNum(final JsPath path) {
      return new JsDoubleLens<>(Objects.requireNonNull(path));
    }

    public JsDoubleLens<JsObj> doubleNum(final String key) {
      return new JsDoubleLens<>(Objects.requireNonNull(JsPath.fromKey(key)));
    }

    public JsDecimalLens<JsObj> decimalNum(final JsPath path) {
      return new JsDecimalLens<>(Objects.requireNonNull(path));
    }

    public JsDecimalLens<JsObj> decimalNum(final String key) {
      return new JsDecimalLens<>(Objects.requireNonNull(JsPath.fromKey(key)));
    }

    public JsBigIntLens<JsObj> integralNum(final JsPath path) {
      return new JsBigIntLens<>(Objects.requireNonNull(path));
    }

    public JsBigIntLens<JsObj> integralNum(final String key) {
      return new JsBigIntLens<>(Objects.requireNonNull(JsPath.fromKey(key)));
    }

    public JsObjLens<JsObj> obj(final JsPath path) {
      return new JsObjLens<>(Objects.requireNonNull(path));
    }

    public JsObjLens<JsObj> obj(final String key) {
      return new JsObjLens<>(Objects.requireNonNull(JsPath.fromKey(key)));
    }

    public JsArrayLens<JsObj> array(final JsPath path) {
      return new JsArrayLens<>(Objects.requireNonNull(path));
    }

    public JsArrayLens<JsObj> array(final String key) {
      return new JsArrayLens<>(Objects.requireNonNull(JsPath.fromKey(key)));
    }
  }
}
