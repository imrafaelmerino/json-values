package jsonvalues;

import static java.util.Objects.requireNonNull;

import fun.optic.Lens;
import fun.optic.Option;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

/**
 * Contains the optics defined for JSON objects and arrays
 */
public final class JsOptics {

  public static final JsObjOptics obj = new JsObjOptics();
  public static final JsArrayOptics array = new JsArrayOptics();

  private JsOptics() {
  }

  /**
   * Contains all the optics defined for a Json array
   */
  public static class JsArrayOptics {

    public final JsArrayOptionals optional = new JsArrayOptionals();
    public final JsArrayLenses lens = new JsArrayLenses();
  }

  /**
   * Contains all the lenses defined for a Json array
   */
  public static class JsArrayLenses {

    /**
     * lens that focus on the value located at a path in an array
     *
     * @param path the path where the value is located at
     * @return a lens
     */
    public Lens<JsArray, JsValue> value(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsValueLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on the value located at an index in an array
     *
     * @param index the index where the value is located at
     * @return a lens
     */
    public Lens<JsArray, JsValue> value(final int index) {
      return new JsValueLens<>(requireNonNull(JsPath.fromIndex(index)));
    }

    /**
     * lens that focus on the string located at a path in an array
     *
     * @param path the path where the string is located at
     * @return a lens
     */
    public Lens<JsArray, String> str(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsStrLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on the string located at an index in an array
     *
     * @param index the index where the string is located at
     * @return a lens
     */
    public Lens<JsArray, String> str(final int index) {
      return new JsStrLens<>(requireNonNull(JsPath.fromIndex(index)));
    }

    /**
     * lens that focus on the boolean located at a path in an array
     *
     * @param path the path where the boolean is located at
     * @return a lens
     */
    public Lens<JsArray, Boolean> bool(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsBoolLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on the boolean located at an index in an array
     *
     * @param index the index where the boolean is located at
     * @return a lens
     */
    public Lens<JsArray, Boolean> bool(final int index) {
      return new JsBoolLens<>(requireNonNull(JsPath.fromIndex(index)));
    }

    /**
     * lens that focus on the long number located at a path in an array
     *
     * @param path the path where the long number is located at
     * @return a lens
     */
    public Lens<JsArray, Long> longNum(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsLongLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on the long number located at an index in an array
     *
     * @param index the index where the long number is located at
     * @return a lens
     */
    public Lens<JsArray, Long> longNum(final int index) {
      return new JsLongLens<>(requireNonNull(JsPath.fromIndex(index)));
    }

    /**
     * lens that focus on the integer number located at a path in an array
     *
     * @param path the path where the integer number is located at
     * @return a lens
     */
    public Lens<JsArray, Integer> intNum(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsIntLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on the integer number located at an index in an array
     *
     * @param index the index where the integer number is located at
     * @return a lens
     */
    public Lens<JsArray, Integer> intNum(final int index) {
      return new JsIntLens<>(requireNonNull(JsPath.fromIndex(index)));
    }

    /**
     * lens that focus on the double number located at a path in an array
     *
     * @param path the path where the double number is located at
     * @return a lens
     */
    public Lens<JsArray, Double> doubleNum(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsDoubleLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on the double number located at an index in an array
     *
     * @param index the index where the double number is located at
     * @return a lens
     */
    public Lens<JsArray, Double> doubleNum(final int index) {
      return new JsDoubleLens<>(requireNonNull(JsPath.fromIndex(index)));
    }

    /**
     * lens that focus on the decimal number located at a path in an array
     *
     * @param path the path where the decimal number is located at
     * @return a lens
     */
    public Lens<JsArray, BigDecimal> decimalNum(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsDecimalLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on the decimal number located at an index in an array
     *
     * @param index the index where the decimal number is located at
     * @return a lens
     */
    public Lens<JsArray, BigDecimal> decimalNum(final int index) {
      return new JsDecimalLens<>(requireNonNull(JsPath.fromIndex(index)));
    }

    /**
     * lens that focus on the integral number located at a path in an array
     *
     * @param path the path where the integral number is located at
     * @return a lens
     */
    public Lens<JsArray, BigInteger> integralNum(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsBigIntLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on the integral number located at an index in an array
     *
     * @param index the index where the integral number is located at
     * @return a lens
     */
    public Lens<JsArray, BigInteger> integralNum(final int index) {
      return new JsBigIntLens<>(requireNonNull(JsPath.fromIndex(index)));
    }

    /**
     * lens that focus on the JSON object located at a path in an array
     *
     * @param path the path where the JSON object is located at
     * @return a lens
     */
    public Lens<JsArray, JsObj> obj(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsObjLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on the json object located at an index in an array
     *
     * @param index the index where the json object is located at
     * @return a lens
     */
    public Lens<JsArray, JsObj> obj(final int index) {
      return new JsObjLens<>(requireNonNull(JsPath.fromIndex(index)));
    }

    /**
     * lens that focus on the json array located at a path in an array
     *
     * @param path the path where the json array is located at
     * @return a lens
     */
    public Lens<JsArray, JsArray> array(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsArrayLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on the json array located at an index in an array
     *
     * @param index the index where the json array is located at
     * @return a lens
     */
    public Lens<JsArray, JsArray> array(final int index) {
      return new JsArrayLens<>(requireNonNull(JsPath.fromIndex(index)));
    }


    /**
     * lens that focus on an array of bytes located at a path in an array
     *
     * @param path the path where the bytes are located at
     * @return a lens
     */
    public Lens<JsArray, byte[]> binary(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsBinaryLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on an array of bytes located at an index in an array
     *
     * @param index the index where the bytes are located at
     * @return a lens
     */
    public Lens<JsArray, byte[]> binary(final int index) {
      return new JsBinaryLens<>(requireNonNull(JsPath.fromIndex(index)));
    }


    /**
     * lens that focus on the array of bytes located at a path in an array
     *
     * @param path the path where the bytes are located at
     * @return a lens
     */
    public Lens<JsArray, Instant> instant(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return new JsInstantLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on the array of bytes at an index in an array
     *
     * @param index the index where the bytes are located at
     * @return a lens
     */
    public Lens<JsArray, Instant> instant(final int index) {
      return new JsInstantLens<>(requireNonNull(JsPath.fromIndex(index)));
    }
  }

  /**
   * Contains all the optics defined for a Json object
   */
  public static class JsObjOptics {

    public final JsObjOptional optional = new JsObjOptional();
    public final JsObjLenses lens = new JsObjLenses();
  }

  /**
   * Contains all the optionals defined for a Json array
   */
  public static class JsArrayOptionals {

    /**
     * optional that focus on the string located at a path in an array
     *
     * @param path the path where the string is located at
     * @return an optional
     */
    public Option<JsArray, String> str(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return JsOptics.array.lens.value(requireNonNull(path))
                                .compose(JsStr.prism);
    }

    /**
     * optional that focus on the string located at an index in an array
     *
     * @param index the index
     * @return an optional
     */
    public Option<JsArray, String> str(final int index) {
      return JsOptics.array.lens.value(index)
                                .compose(JsStr.prism);
    }

    /**
     * optional that focus on the boolean located at a path in an array
     *
     * @param path the path where the boolean number is located at
     * @return an optional
     */
    public Option<JsArray, Boolean> bool(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }
      return JsOptics.array.lens.value(requireNonNull(path))
                                .compose(JsBool.prism);
    }

    /**
     * optional that focus on the boolean located at an index in an array
     *
     * @param index the index
     * @return an optional
     */
    public Option<JsArray, Boolean> bool(final int index) {
      return JsOptics.array.lens.value(index)
                                .compose(JsBool.prism);
    }

    /**
     * optional that focus on the long number located at a path in an array
     *
     * @param path the path where the long number is located at
     * @return an optional
     */
    public Option<JsArray, Long> longNum(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }

      return JsOptics.array.lens.value(requireNonNull(path))
                                .compose(JsLong.prism);
    }

    /**
     * optional that focus on the long number located at an index in an array
     *
     * @param index the index
     * @return an optional
     */
    public Option<JsArray, Long> longNum(final int index) {
      return JsOptics.array.lens.value(index)
                                .compose(JsLong.prism);
    }

    /**
     * optional that focus on the integer number located at a path in an array
     *
     * @param path the path where the integer number is located at
     * @return an optional
     */
    public Option<JsArray, Integer> intNum(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }

      return JsOptics.array.lens.value(requireNonNull(path))
                                .compose(JsInt.prism);
    }

    /**
     * optional that focus on the integer number located at an index in an array
     *
     * @param index the index
     * @return an optional
     */
    public Option<JsArray, Integer> intNum(final int index) {
      return JsOptics.array.lens.value(index)
                                .compose(JsInt.prism);
    }

    /**
     * optional that focus on the double number located at a path in an array
     *
     * @param path the path where the double number is located at
     * @return an optional
     */
    public Option<JsArray, Double> doubleNum(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }

      return JsOptics.array.lens.value(requireNonNull(path))
                                .compose(JsDouble.prism);
    }

    /**
     * optional that focus on the double number located at an index in an array
     *
     * @param index the index
     * @return an optional
     */
    public Option<JsArray, Double> doubleNum(final int index) {
      return JsOptics.array.lens.value(index)
                                .compose(JsDouble.prism);
    }

    /**
     * optional that focus on the decimal number located at a path in an array
     *
     * @param path the path where the decimal number is located at
     * @return an optional
     */
    public Option<JsArray, BigDecimal> decimalNum(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }

      return JsOptics.array.lens.value(requireNonNull(path))
                                .compose(JsBigDec.prism);
    }

    /**
     * optional that focus on the decimal number located at an index in an array
     *
     * @param index the index
     * @return an optional
     */
    public Option<JsArray, BigDecimal> decimalNum(final int index) {
      return JsOptics.array.lens.value(index)
                                .compose(JsBigDec.prism);
    }

    /**
     * optional that focus on the integral number located at a path in an array
     *
     * @param path the path where the integral number is located at
     * @return an optional
     */
    public Option<JsArray, BigInteger> integralNum(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }

      return JsOptics.array.lens.value(requireNonNull(path))
                                .compose(JsBigInt.prism);
    }

    /**
     * optional that focus on the integral number located at an index in an array
     *
     * @param index the index
     * @return an optional
     */
    public Option<JsArray, BigInteger> integralNum(final int index) {
      return JsOptics.array.lens.value(index)
                                .compose(JsBigInt.prism);
    }

    /**
     * optional that focus on the object located at a path in an array
     *
     * @param path the path where the obj is located at
     * @return an optional
     */
    public Option<JsArray, JsObj> obj(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }

      return JsOptics.array.lens.value(requireNonNull(path))
                                .compose(JsObj.prism);
    }

    /**
     * optional that focus on the json object located at an index in an array
     *
     * @param index the index
     * @return an optional
     */
    public Option<JsArray, JsObj> obj(final int index) {
      return JsOptics.array.lens.value(index)
                                .compose(JsObj.prism);
    }

    /**
     * optional that focus on the array located at a path in an array
     *
     * @param path the path where the array is located at
     * @return an optional
     */
    public Option<JsArray, JsArray> array(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }

      return JsOptics.array.lens.value(requireNonNull(path))
                                .compose(JsArray.prism);
    }

    /**
     * optional that focus on the json array located at an index in an array
     *
     * @param index the index
     * @return an optional
     */
    public Option<JsArray, JsArray> array(final int index) {
      return JsOptics.array.lens.value(index)
                                .compose(JsArray.prism);
    }

    /**
     * optional that focus on the instant located at a path in an array
     *
     * @param path the path where the instant is located at
     * @return an optional
     */
    public Option<JsArray, Instant> instant(final JsPath path) {
      if (path.head()
              .isKey()) {
        throw UserError.pathHeadIsNotAnIndex(path);
      }

      return JsOptics.array.lens.value(requireNonNull(path))
                                .compose(JsInstant.prism);
    }

    /**
     * optional that focus on the instant located at an index in an array
     *
     * @param index the index where the instant is located at
     * @return an optional
     */
    public Option<JsArray, Instant> instant(final int index) {
      return JsOptics.array.lens.value(index)
                                .compose(JsInstant.prism);
    }
  }

  /**
   * Contains all the optionals defined for a Json object
   */
  public static class JsObjOptional {

    /**
     * optional that focus on the string located at a path in an object
     *
     * @param path the path where the string is located at
     * @return an optional
     */
    public Option<JsObj, String> str(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }
      return JsOptics.obj.lens.value(requireNonNull(path))
                              .compose(JsStr.prism);
    }

    /**
     * optional that focus on the string located at a key in an object
     *
     * @param key the key where the string is located at
     * @return an optional
     */
    public Option<JsObj, String> str(final String key) {
      return JsOptics.obj.lens.value(requireNonNull(key))
                              .compose(JsStr.prism);
    }

    /**
     * optional that focus on the boolean located at a path in an object
     *
     * @param path the path where the boolean is located at
     * @return an optional
     */
    public Option<JsObj, Boolean> bool(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return JsOptics.obj.lens.value(requireNonNull(path))
                              .compose(JsBool.prism);
    }

    /**
     * optional that focus on the boolean located at a key in an object
     *
     * @param key the key where the boolean is located at
     * @return an optional
     */
    public Option<JsObj, Boolean> bool(final String key) {
      return JsOptics.obj.lens.value(requireNonNull(key))
                              .compose(JsBool.prism);
    }

    /**
     * optional that focus on the long number located at a path in an object
     *
     * @param path the path where the long number is located at
     * @return an optional
     */
    public Option<JsObj, Long> longNum(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return JsOptics.obj.lens.value(requireNonNull(path))
                              .compose(JsLong.prism);
    }

    /**
     * optional that focus on the long number located at a key in an object
     *
     * @param key the key where the long number is located at
     * @return an optional
     */
    public Option<JsObj, Long> longNum(final String key) {
      return JsOptics.obj.lens.value(requireNonNull(key))
                              .compose(JsLong.prism);
    }

    /**
     * optional that focus on the integer number located at a path in an object
     *
     * @param path the path where the integer number is located at
     * @return an optional
     */
    public Option<JsObj, Integer> intNum(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return JsOptics.obj.lens.value(requireNonNull(path))
                              .compose(JsInt.prism);
    }

    /**
     * optional that focus on the integer number located at a key in an object
     *
     * @param key the key where the integer number is located at
     * @return an optional
     */
    public Option<JsObj, Integer> intNum(final String key) {
      return JsOptics.obj.lens.value(requireNonNull(key))
                              .compose(JsInt.prism);
    }

    /**
     * optional that focus on the double number located at a path in an object
     *
     * @param path the path where the double number is located at
     * @return an optional
     */
    public Option<JsObj, Double> doubleNum(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return JsOptics.obj.lens.value(requireNonNull(path))
                              .compose(JsDouble.prism);
    }

    /**
     * optional that focus on the double number located at a key in an object
     *
     * @param key the key where the double number is located at
     * @return an optional
     */
    public Option<JsObj, Double> doubleNum(final String key) {
      return JsOptics.obj.lens.value(requireNonNull(key))
                              .compose(JsDouble.prism);
    }

    /**
     * optional that focus on the decimal number located at a path in an object
     *
     * @param path the path where the decimal number is located at
     * @return an optional
     */
    public Option<JsObj, BigDecimal> decimalNum(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return JsOptics.obj.lens.value(requireNonNull(path))
                              .compose(JsBigDec.prism);
    }

    /**
     * optional that focus on the decimal number located at a key in an object
     *
     * @param key the key where the decimal number is located at
     * @return an optional
     */
    public Option<JsObj, BigDecimal> decimalNum(final String key) {
      return JsOptics.obj.lens.value(requireNonNull(key))
                              .compose(JsBigDec.prism);
    }

    /**
     * optional that focus on the integral number located at a path in an object
     *
     * @param path the path where the integral number is located at
     * @return an optional
     */
    public Option<JsObj, BigInteger> integralNum(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return JsOptics.obj.lens.value(requireNonNull(path))
                              .compose(JsBigInt.prism);
    }

    /**
     * optional that focus on the bigint number located at a path in an object
     *
     * @param key the path where the bigint number is located at
     * @return an optional
     */
    public Option<JsObj, BigInteger> integralNum(final String key) {
      return JsOptics.obj.lens.value(requireNonNull(key))
                              .compose(JsBigInt.prism);
    }

    /**
     * optional that focus on the object located at a path in an object
     *
     * @param path the path where the object is located at
     * @return an optional
     */
    public Option<JsObj, JsObj> obj(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return JsOptics.obj.lens.value(requireNonNull(path))
                              .compose(JsObj.prism);
    }

    /**
     * optional that focus on the object located at a key in an object
     *
     * @param key the key where the object is located at
     * @return an optional
     */
    public Option<JsObj, JsObj> obj(final String key) {
      return JsOptics.obj.lens.value(requireNonNull(key))
                              .compose(JsObj.prism);
    }

    /**
     * optional that focus on the array located at a path in an object
     *
     * @param path the path where the array is located at
     * @return an optional
     */
    public Option<JsObj, JsArray> array(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return JsOptics.obj.lens.value(requireNonNull(path))
                              .compose(JsArray.prism);
    }

    /**
     * optional that focus on the array located at a key in an object
     *
     * @param key the path where the array is located at
     * @return an optional
     */
    public Option<JsObj, JsArray> array(final String key) {
      return JsOptics.obj.lens.value(requireNonNull(key))
                              .compose(JsArray.prism);
    }

    /**
     * optional that focus on the array of bytes located at a path in an object
     *
     * @param path the path where the bytes are located at
     * @return an optional
     */
    public Option<JsObj, byte[]> binary(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return JsOptics.obj.lens.value(requireNonNull(path))
                              .compose(JsBinary.prism);
    }

    /**
     * optional that focus on the array of bytes located at a key in an object
     *
     * @param key the path where the bytes are located at
     * @return an optional
     */
    public Option<JsObj, byte[]> binary(final String key) {
      return JsOptics.obj.lens.value(requireNonNull(key))
                              .compose(JsBinary.prism);
    }

    /**
     * optional that focus on the instant located at a path in an object
     *
     * @param path the path where the instant is located at
     * @return an optional
     */
    public Option<JsObj, Instant> instant(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return JsOptics.obj.lens.value(requireNonNull(path))
                              .compose(JsInstant.prism);
    }

    /**
     * optional that focus on the instant located at a key in an object
     *
     * @param key the path where the instant is located at
     * @return an optional
     */
    public Option<JsObj, Instant> instant(final String key) {
      return JsOptics.obj.lens.value(requireNonNull(key))
                              .compose(JsInstant.prism);
    }
  }

  /**
   * Represents all the lenses defined for a Json object
   */
  public static class JsObjLenses {

    /**
     * lens that focus on a value located at a path in an object.
     *
     * @param path the path where the value is located at
     * @return an optional
     */
    public Lens<JsObj, JsValue> value(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsValueLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on a value located at a key in an object.
     *
     * @param key the key where the value is located at
     * @return an optional
     */
    public Lens<JsObj, JsValue> value(final String key) {
      return new JsValueLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }

    /**
     * lens that focus on a string located at a path in an object.
     *
     * @param path the path where the string is located at
     * @return an optional
     */
    public Lens<JsObj, String> str(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsStrLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on a string located at a key in an object.
     *
     * @param key the key where the string is located at
     * @return an optional
     */
    public Lens<JsObj, String> str(final String key) {
      return new JsStrLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }

    /**
     * lens that focus on a boolean located at a path in an object.
     *
     * @param path the path where the boolean is located at
     * @return an optional
     */
    public Lens<JsObj, Boolean> bool(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsBoolLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on a boolean located at a path in an object.
     *
     * @param key the key where the boolean is located at
     * @return an optional
     */
    public Lens<JsObj, Boolean> bool(final String key) {
      return new JsBoolLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }

    /**
     * lens that focus on a long number located at a path in an object.
     *
     * @param path the path where the long number is located at
     * @return an optional
     */
    public Lens<JsObj, Long> longNum(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsLongLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on a long number located at a key in an object.
     *
     * @param key the key where the long number is located at
     * @return an optional
     */
    public Lens<JsObj, Long> longNum(final String key) {
      return new JsLongLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }

    /**
     * lens that focus on an integer number located at a path in an object.
     *
     * @param path the path where the integer number is located at
     * @return an optional
     */
    public Lens<JsObj, Integer> intNum(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsIntLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on an integer number located at a key in an object.
     *
     * @param key the key where the integer number is located at
     * @return an optional
     */
    public Lens<JsObj, Integer> intNum(final String key) {
      return new JsIntLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }

    /**
     * lens that focus on a double number located at a path in an object.
     *
     * @param path the path where the double number is located at
     * @return an optional
     */
    public Lens<JsObj, Double> doubleNum(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsDoubleLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on a double number located at a key in an object.
     *
     * @param key the key where the double number is located at
     * @return an optional
     */
    public Lens<JsObj, Double> doubleNum(final String key) {
      return new JsDoubleLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }

    /**
     * lens that focus on a decimal number located at a path in an object.
     *
     * @param path the path where the decimal number is located at
     * @return an optional
     */
    public Lens<JsObj, BigDecimal> decimalNum(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsDecimalLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on a decimal number located at a key in an object.
     *
     * @param key the key where the decimal number is located at
     * @return an optional
     */
    public Lens<JsObj, BigDecimal> decimalNum(final String key) {
      return new JsDecimalLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }

    /**
     * lens that focus on an integral number located at a path in an object.
     *
     * @param path the path where the integral number is located at
     * @return an optional
     */
    public Lens<JsObj, BigInteger> integralNum(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsBigIntLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on an integral number located at a key in an object.
     *
     * @param key the key where the integral number is located at
     * @return an optional
     */
    public Lens<JsObj, BigInteger> integralNum(final String key) {
      return new JsBigIntLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }

    /**
     * lens that focus on a json object located at a path in an object.
     *
     * @param path the path where the json object is located at
     * @return an optional
     */
    public Lens<JsObj, JsObj> obj(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsObjLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on a json object located at a key in an object.
     *
     * @param key the key where the json object is located at
     * @return an optional
     */
    public Lens<JsObj, JsObj> obj(final String key) {
      return new JsObjLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }

    /**
     * lens that focus on a json object located at a path in an object.
     *
     * @param path the path where the json array is located at
     * @return an optional
     */
    public Lens<JsObj, JsArray> array(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsArrayLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on a json array located at a key in an object.
     *
     * @param key the key where the json array is located at
     * @return an optional
     */
    public Lens<JsObj, JsArray> array(final String key) {
      return new JsArrayLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }


    /**
     * lens that focus on an array of bytes located at a path in an object.
     *
     * @param path the path where the array of bytes is located at
     * @return an optional
     */
    public Lens<JsObj, byte[]> binary(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsBinaryLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on an array of bytes located at a key in an object.
     *
     * @param key the key where the array of bytes is located at
     * @return an optional
     */
    public Lens<JsObj, byte[]> binary(final String key) {
      return new JsBinaryLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }


    /**
     * lens that focus on an instant located at a path in an object.
     *
     * @param path the path where the instant is located at
     * @return an optional
     */
    public Lens<JsObj, Instant> instant(final JsPath path) {
      if (path.head()
              .isIndex()) {
        throw UserError.pathHeadIsNotAKey(requireNonNull(path));
      }

      return new JsInstantLens<>(requireNonNull(path));
    }

    /**
     * lens that focus on an instant located at a key in an object.
     *
     * @param key the key where the instant is located at
     * @return an optional
     */
    public Lens<JsObj, Instant> instant(final String key) {
      return new JsInstantLens<>(requireNonNull(JsPath.fromKey(requireNonNull(key))));
    }

  }
}
