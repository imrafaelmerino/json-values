package jsonvalues;



import fun.optic.Prism;
import jsonvalues.spec.JsonIO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.IntStream.range;
import static jsonvalues.JsArray.TYPE.LIST;
import static jsonvalues.JsArray.TYPE.MULTISET;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.JsObj.streamOfObj;
import static jsonvalues.MatchExp.ifJsonElse;
import static jsonvalues.MatchExp.ifNothingElse;


/**
 * Represents a json array, which is an ordered list of elements.
 */
public final class JsArray implements Json<JsArray>, Iterable<JsValue> {
    public static final int TYPE_ID = 4;
    /**
     * lenses defined for a Json array
     */
    public static final JsOptics.JsArrayLenses lens = JsOptics.array.lens;
    /**
     * optionals defined for a Json array
     */
    public static final JsOptics.JsArrayOptionals optional = JsOptics.array.optional;

    public static final JsArray EMPTY = new JsArray(Vector.empty());
    /**
     * prism between the sum type JsValue and JsArray
     */
    public static final Prism<JsValue, JsArray> prism =
            new Prism<>(
                    s -> s.isArray() ?
                         Optional.of(s.toJsArray()) :
                         Optional.empty(),
                    a -> a
            );
    private final Vector<JsValue> seq;
    private volatile int hashcode;
    private volatile String str;


    JsArray(Vector<JsValue> seq) {
        this.seq = seq;
    }

    public JsArray() {
        this.seq = Vector.empty();
    }

    public static JsArray empty() {
        return EMPTY;
    }


    /**
     * Returns an immutable array.
     *
     * @param e    a JsValue
     * @param e1   a JsValue
     * @param e2   a JsValue
     * @param e3   a JsValue
     * @param e4   a JsValue
     * @param rest more optional JsValue
     * @return an immutable JsArray
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsArray of(final JsValue e,
                             final JsValue e1,
                             final JsValue e2,
                             final JsValue e3,
                             final JsValue e4,
                             final JsValue... rest
    ) {
        JsArray result = of(e,
                            e1,
                            e2,
                            e3,
                            e4
        );
        for (JsValue other : requireNonNull(rest)) {
            result = result.append(other);
        }
        return result;


    }

    /**
     * Returns an immutable five-element array.
     *
     * @param e  a JsValue
     * @param e1 a JsValue
     * @param e2 a JsValue
     * @param e3 a JsValue
     * @param e4 a JsValue
     * @return an immutable five-element JsArray
     */
    //squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsArray of(final JsValue e,
                             final JsValue e1,
                             final JsValue e2,
                             final JsValue e3,
                             final JsValue e4
    ) {

        return of(e,
                  e1,
                  e2,
                  e3
        ).append(e4);
    }

    /**
     * Returns an immutable four-element array.
     *
     * @param e  a JsValue
     * @param e1 a JsValue
     * @param e2 a JsValue
     * @param e3 a JsValue
     * @return an immutable four-element JsArray
     */
    public static JsArray of(final JsValue e,
                             final JsValue e1,
                             final JsValue e2,
                             final JsValue e3
    ) {
        return of(e,
                  e1,
                  e2
        ).append(e3);
    }

    /**
     * Returns an immutable three-element array.
     *
     * @param e  a JsValue
     * @param e1 a JsValue
     * @param e2 a JsValue
     * @return an immutable three-element JsArray
     */
    public static JsArray of(final JsValue e,
                             final JsValue e1,
                             final JsValue e2
    ) {

        return of(e,
                  e1
        ).append(e2);
    }

    /**
     * Returns an immutable two-element array.
     *
     * @param e  a JsValue
     * @param e1 a JsValue
     * @return an immutable two-element JsArray
     */
    public static JsArray of(final JsValue e,
                             final JsValue e1
    ) {
        return of(e).append(e1);
    }

    public static JsArray of(JsValue e) {

        return JsArray.EMPTY.append(e);
    }

    /**
     * Returns an immutable array from one or more strings.
     *
     * @param str    a string
     * @param others more optional strings
     * @return an immutable JsArray
     */
    public static JsArray of(String str,
                             String... others
    ) {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsStr.of(str));
        for (String a : others) {
            vector = vector.append(JsStr.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     * Returns an immutable array from one or more integers.
     *
     * @param number an integer
     * @param others more optional integers
     * @return an immutable JsArray
     */
    public static JsArray of(int number,
                             int... others
    ) {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsInt.of(number));
        for (int a : others) {
            vector = vector.append(JsInt.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     * Returns an immutable array from one or more booleans.
     *
     * @param bool   an boolean
     * @param others more optional booleans
     * @return an immutable JsArray
     */
    public static JsArray of(final boolean bool,
                             final boolean... others
    ) {
        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsBool.of(bool));
        for (boolean a : others) {
            vector = vector.append(JsBool.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     * Returns an immutable array from one or more longs.
     *
     * @param number a long
     * @param others more optional longs
     * @return an immutable JsArray
     */
    public static JsArray of(final long number,
                             final long... others
    ) {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsLong.of(number));
        for (long a : others) {
            vector = vector.append(JsLong.of(a));
        }
        return new JsArray(vector

        );
    }

    /**
     * Returns an immutable array from one or more big integers.
     *
     * @param number a big integer
     * @param others more optional big integers
     * @return an immutable JsArray
     */
    public static JsArray of(final BigInteger number,
                             final BigInteger... others
    ) {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsBigInt.of(number));
        for (BigInteger a : others) {
            vector = vector.append(JsBigInt.of(a));
        }
        return new JsArray(vector);
    }

    /**
     * Returns an immutable array from one or more doubles.
     *
     * @param number a double
     * @param others more optional doubles
     * @return an immutable JsArray
     */
    public static JsArray of(final double number,
                             final double... others
    ) {

        Vector<JsValue> vector = Vector.<JsValue>empty().append(JsDouble.of(number));
        for (double a : others) {
            vector = vector.append(JsDouble.of(a));
        }
        return new JsArray(vector
        );
    }

    /**
     * returns an immutable json array from an iterable of json elements
     *
     * @param iterable the iterable of json elements
     * @return an immutable json array
     */
    public static JsArray ofIterable(final Iterable<? extends JsValue> iterable) {
        Vector<JsValue> vector = Vector.empty();
        for (JsValue e : requireNonNull(iterable)) {
            vector = vector.append(e);
        }
        return new JsArray(vector

        );
    }

    /**
     * Tries to parse the string into an immutable json array.
     *
     * @param str the string to be parsed
     * @return a JsArray
     * @throws JsParserException if the string doesnt represent a json array
     */
    public static JsArray parse(final String str) {

      return JsonIO.INSTANCE.parseToJsArray(str.getBytes(StandardCharsets.UTF_8));

    }


    static Stream<JsPair> streamOfArr(final JsArray array,
                                                     final JsPath path) {


        requireNonNull(path);
        return requireNonNull(array).ifEmptyElse(() -> Stream.of(new JsPair(path,
                                                                         array
                                                 )),
                                                 () -> range(0,
                                                             array.size()
                                                 ).mapToObj(pair -> new JsPair(path.index(pair),
                                                                            array.get(Index.of(pair))
                                                  ))
                                                  .flatMap(pair -> MatchExp.ifJsonElse(o -> streamOfObj(o,
                                                                                                        pair.path()
                                                                                       ),
                                                                                       a -> streamOfArr(a,
                                                                                                        pair.path()
                                                                                       ),
                                                                                       e -> Stream.of(pair)
                                                                           )
                                                                           .apply(pair.value())
                                                  )
        );


    }


    /**
     * Adds one or more elements, starting from the first, to the back of this array.
     *
     * @param e      the JsValue to be added to the back.
     * @param others more optional JsValue to be added to the back
     * @return a new JsArray
     */
    public JsArray append(final JsValue e,
                          final JsValue... others
    ) {
        Vector<JsValue> acc = this.seq.append(requireNonNull(e));
        for (JsValue other : requireNonNull(others)) acc = acc.append(requireNonNull(other));
        return new JsArray(acc);
    }

    /**
     * Adds all the elements of the given array, starting from the head, to the back of this array.
     *
     * @param array the JsArray of elements to be added to the back
     * @return a new JsArray
     */
    public JsArray appendAll(final JsArray array) {
        return appendAllBack(this,
                             requireNonNull(array)
        );


    }

    private JsArray appendAllBack(JsArray arr1,
                                  final JsArray arr2
    ) {
        assert arr1 != null;
        assert arr2 != null;
        if (arr2.isEmpty()) return arr1;
        if (arr1.isEmpty()) return arr2;
        for (final JsValue value : arr2) {
            arr1 = arr1.append(value);
        }
        return arr1;
    }

    private JsArray appendAllFront(JsArray arr1,
                                   JsArray arr2
    ) {
        assert arr1 != null;
        assert arr2 != null;
        if (arr2.isEmpty()) return arr1;
        if (arr1.isEmpty()) return arr2;
        for (final JsValue value : arr1) {
            arr2 = arr2.append(value);
        }
        return arr2;
    }

    /**
     * Returns true if this array is equal to the given as a parameter. In the case of ARRAY_AS=LIST,
     * this method is equivalent to JsArray.equals(Object).
     *
     * @param array    the given array
     * @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     * @return true if both arrays are equals according to ARRAY_AS parameter
     */
    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    public boolean equals(final JsArray array,
                          final TYPE ARRAY_AS
    ) {
        if (ARRAY_AS == LIST) return this.equals(array);
        if (isEmpty()) return array.isEmpty();
        if (array.isEmpty()) return false;
        return IntStream.range(0,
                               size()
                        )
                        .mapToObj(i -> get(Index.of(i)))
                        .allMatch(elem ->
                                  {
                                      if (!array.containsValue(elem))
                                          return false;
                                      if (ARRAY_AS == MULTISET)
                                          return seq.count(it -> it.equals(elem)) == array.seq.count(it -> it.equals(elem));
                                      return true;
                                  })
                && IntStream.range(0,
                                   array.size()
                            )
                            .mapToObj(i -> array.get(Index.of(i)))
                            .allMatch(this::containsValue);
    }

    /**
     * Returns the integral number located at the given index as an integer or null if it
     * doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an integer.
     *
     * @param index the index
     * @return the integral number located at the given index or null
     */
    public Integer getInt(final int index) {
        return getInt(index,
                      () -> null);

    }

    /**
     * Returns the integral number located at the given index as an integer or the default value provided if it
     * doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an integer.
     *
     * @param index  the index
     * @param orElse the default value
     * @return the integral number located at the given index or null
     */
    public Integer getInt(final int index,
                          final Supplier<Integer> orElse) {
        return (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
               requireNonNull(orElse).get() :
               JsInt.prism.getOptional.apply(seq.get(index))
                                      .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the long number located at the given index as an long or null if it
     * doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an long.
     *
     * @param index the index
     * @return the long number located at the given index or null
     */
    public Long getLong(final int index) {
        return getLong(index,
                       () -> null);

    }

    /**
     * Returns the long number located at the given index as an long or the default value provided if it
     * doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an long.
     *
     * @param index  the index
     * @param orElse the default value
     * @return the long number located at the given index or the default value provided
     */
    public Long getLong(final int index,
                        final Supplier<Long> orElse) {
        return (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
               requireNonNull(orElse).get() :
               JsLong.prism.getOptional.apply(seq.get(index))
                                       .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the string located at the given index  or null if it doesn't exist.
     *
     * @param index the index
     * @return the string located at the given index or null
     */
    public String getStr(final int index) {
        return getStr(index,
                      () -> null);

    }


    /**
     * Returns the string located at the given index or the default value provided
     * if it doesn't exist.
     *
     * @param index  the index
     * @param orElse the default value
     * @return the string located at the given index or the default value
     */
    public String getStr(final int index,
                         final Supplier<String> orElse) {
        return (seq.isEmpty() || index < 0 || index > seq.length() - 1) ?
               requireNonNull(orElse).get() :
               JsStr.prism.getOptional.apply(seq.get(index))
                                      .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the instant located at the given index or null if it doesn't exist or it's not an instant.
     * If the element is an instant formatted as a string, it's returned as an instant as well.
     *
     * @param index the given index
     * @return an instant
     */
    public Instant getInstant(final int index) {
        return getInstant(index,
                          () -> null);

    }

    /**
     * Returns the instant located at the given index or the default value provided if it doesn't exist
     * or it's not an instant. If the element is an instant formatted as a string, it's returned as an
     * instant as well.
     *
     * @param index  the given index
     * @param orElse the default value
     * @return an instant
     */
    public Instant getInstant(final int index,
                              final Supplier<Instant> orElse) {
        return (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
               requireNonNull(orElse).get() :
               JsInstant.prism.getOptional.apply(seq.get(index))
                                          .orElse(requireNonNull(orElse).get());

    }

    /**
     * Returns the array of bytes located at the given index or null if it doesn't exist or it's not an array of bytes.
     * If the element is a string in base64, it's returned as an array of bytes as well.
     *
     * @param index the given index
     * @return an array of bytes
     */
    public byte[] getBinary(final int index) {
        return getBinary(index,
                         () -> null);

    }

    /**
     * Returns the array of bytes located at the given index or the default value provided if it doesn't exist or it's not an array of bytes.
     * If the element is a string in base64, it's returned as an array of bytes as well.
     *
     * @param index  the given index
     * @param orElse the default value
     * @return an array of bytes
     */
    public byte[] getBinary(final int index,
                            final Supplier<byte[]> orElse) {
        return (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
               requireNonNull(orElse).get() :
               JsBinary.prism.getOptional.apply(seq.get(index))
                                         .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the boolean located at the given index  or null if it doesn't exist.
     *
     * @param index the index
     * @return the boolean located at the given index or null
     */
    public Boolean getBool(final int index) {
        return getBool(index,
                       () -> null);

    }

    /**
     * Returns the boolean located at the given index  or the default value provided if it doesn't exist.
     *
     * @param index  the index
     * @param orElse the default value
     * @return the boolean located at the given index or the default value provided
     */
    public Boolean getBool(final int index,
                           final Supplier<Boolean> orElse) {
        return (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
               requireNonNull(orElse).get() :
               JsBool.prism.getOptional.apply(seq.get(index))
                                       .orElseGet(requireNonNull(orElse));

    }


    /**
     * Returns the number located at the given index as a double or null if it
     * doesn't exist or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
     * to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
     * the precision of the BigDecimal
     *
     * @param index the index
     * @return the double number located at the given index or null
     */
    public Double getDouble(final int index) {
        return getDouble(index,
                         () -> null);

    }

    /**
     * Returns the number located at the given index as a double or the default value provided if it
     * doesn't exist or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
     * to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
     * the precision of the BigDecimal
     *
     * @param index  the index
     * @param orElse the default value
     * @return the double number located at the given index or null
     */
    public Double getDouble(final int index,
                            final Supplier<Double> orElse) {
        return (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
               requireNonNull(orElse).get() :
               JsDouble.prism.getOptional.apply(seq.get(index))
                                         .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the number located at the given index as a big decimal or null if
     * it doesn't exist or it's not a decimal number.
     *
     * @param index the index
     * @return the decimal number located at the given index or null
     */
    public BigDecimal getBigDec(final int index) {
        return getBigDec(index,
                         () -> null);
    }

    /**
     * Returns the number located at the given index as a big decimal or the default value provided if
     * it doesn't exist or it's not a decimal number.
     *
     * @param index  the index
     * @param orElse the default value
     * @return the decimal number located at the given index or the default value provided
     */
    public BigDecimal getBigDec(final int index,
                                final Supplier<BigDecimal> orElse) {
        return (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
               requireNonNull(orElse).get() :
               JsBigDec.prism.getOptional.apply(seq.get(index))
                                         .orElseGet(requireNonNull(orElse));
    }

    /**
     * Returns the number located at the given index as a big integer or null if
     * it doesn't exist or it's not an integral number.
     *
     * @param index the index
     * @return the integral number located at the given index or null
     */
    public BigInteger getBigInt(final int index) {
        return getBigInt(index,
                         () -> null);
    }

    /**
     * Returns the number located at the given index as a big integer or the default value provided
     * if  it doesn't exist or it's not an integral number.
     *
     * @param index  the index
     * @param orElse the default value provided
     * @return the integral number located at the given index or null
     */
    public BigInteger getBigInt(final int index,
                                final Supplier<BigInteger> orElse) {
        return (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
               requireNonNull(orElse).get() :
               JsBigInt.prism.getOptional.apply(seq.get(index))
                                         .orElseGet(requireNonNull(orElse));
    }

    /**
     * Returns the object located at the given index  or null if it doesn't exist or it's not a json object.
     *
     * @param index the index
     * @return the object located at the given index or null
     */
    public JsObj getObj(final int index) {
        return getObj(index,
                      () -> null);
    }

    /**
     * Returns the object located at the given index  or the default value provided if it doesn't exist or
     * it's not a json object.
     *
     * @param index  the index
     * @param orElse the default value
     * @return the object located at the given index or the default value provided
     */
    public JsObj getObj(final int index,
                        final Supplier<JsObj> orElse) {
        return (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
               requireNonNull(orElse).get() :
               JsObj.prism.getOptional.apply(seq.get(index))
                                      .orElseGet(requireNonNull(orElse));
    }

    /**
     * Returns the array located at the given index or null if it doesn't exist or it's not a json array.
     *
     * @param index the index
     * @return the array located at the given index or null
     */
    public JsArray getArray(final int index) {
        return getArray(index,
                        () -> null);
    }

    /**
     * Returns the array located at the given index or the default value provided if it doesn't exist
     * or it's not a json array.
     *
     * @param index  the index
     * @param orElse the default value
     * @return the array located at the given index or the default value provided
     */
    public JsArray getArray(final int index,
                            final Supplier<JsArray> orElse) {
        return (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
               requireNonNull(orElse).get() :
               JsArray.prism.getOptional.apply(seq.get(index))
                                        .orElseGet(requireNonNull(orElse));
    }

    private JsValue get(final Position pos) {
        return requireNonNull(pos).match(key -> JsNothing.NOTHING,
                                         index ->
                                                 (this.seq.isEmpty() || index < 0 || index > this.seq.length() - 1) ?
                                                 JsNothing.NOTHING :
                                                 this.seq.get(index)
        );
    }

    @Override
    public boolean containsValue(final JsValue el) {
        return seq.contains(requireNonNull(el));
    }

    @Override
    public JsValue get(final JsPath path) {
        if (path.isEmpty()) return this;
        JsValue e = get(path.head());
        JsPath tail = path.tail();
        if (tail.isEmpty()) return e;
        if (e.isPrimitive()) return NOTHING;
        return e.toJson()
                .get(tail);
    }


    @Override
    public JsArray filterValues(final BiPredicate<? super JsPath, ? super JsPrimitive> filter) {
        return OpFilterArrElems.filter(this,
                                       JsPath.empty(),
                                       requireNonNull(filter)
        );
    }

    @Override
    public JsArray filterValues(final Predicate<? super JsPrimitive> filter) {
        return OpFilterArrElems.filter(this,
                                       requireNonNull(filter)
        );
    }


    @Override
    public JsArray filterKeys(final BiPredicate<? super JsPath, ? super JsValue> filter) {
        return OpFilterArrKeys.filter(this,
                                      JsPath.empty(),
                                      filter
        );
    }

    @Override
    public JsArray filterKeys(final Predicate<? super String> filter) {
        return OpFilterArrKeys.filter(this,
                                      filter
        );
    }


    @Override
    public JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter) {
        return OpFilterArrObjs.filter(this,
                                      JsPath.empty(),
                                      requireNonNull(filter)
        );
    }

    @Override
    public JsArray filterObjs(final Predicate<? super JsObj> filter) {
        return OpFilterArrObjs.filter(this,
                                      requireNonNull(filter)
        );
    }

    @Override
    public boolean isEmpty() {
        return seq.isEmpty();
    }


    @Override
    public JsArray mapValues(final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapArrElems.map(this,
                                 requireNonNull(fn),
                                 JsPath.empty()
                                       .index(-1)
        );
    }

    @Override
    public JsArray mapValues(final Function<? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapArrElems.map(this,
                                 requireNonNull(fn)
        );
    }


    @Override
    public JsArray mapKeys(final BiFunction<? super JsPath, ? super JsValue, String> fn) {
        return OpMapArrKeys.map(this,
                                requireNonNull(fn),
                                JsPath.empty()
                                      .index(-1)
        );
    }

    @Override
    public JsArray mapKeys(final Function<? super String, String> fn) {
        return OpMapArrKeys.map(this,
                                requireNonNull(fn)
        );
    }

    @Override
    public JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, ? extends JsValue> fn) {
        return OpMapArrObjs.map(this,
                                requireNonNull(fn),
                                JsPath.empty().index(-1)

        );
    }

    @Override
    public JsArray mapObjs(final Function<? super JsObj, ? extends JsValue> fn) {
        return OpMapArrObjs.map(this,
                                requireNonNull(fn)
        );
    }

    @Override
    public JsArray set(final JsPath path,
                       final JsValue element) {
        return set(path,
                   element,
                   NULL
        );
    }

    public JsArray set(final int index,
                       final JsValue element) {
        return set(index,
                   element,
                   NULL
        );
    }

    public JsArray set(final int index,
                       final JsValue value,
                       final JsValue padElement
    ) {

        requireNonNull(value);

        return ifNothingElse(() -> this.delete(index),
                             elem -> new JsArray(nullPadding(index,
                                                             seq,
                                                             elem,
                                                             padElement
                             ))
        )
                .apply(value);

    }

    @Override
    public JsArray set(final JsPath path,
                       final JsValue value,
                       final JsValue padElement
    ) {

        requireNonNull(value);
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(head -> this,
                          index ->
                          {
                              JsPath tail = path.tail();

                              return tail.isEmpty() ?
                                     ifNothingElse(() -> this.delete(index),
                                                   elem -> new JsArray(nullPadding(index,
                                                                                   seq,
                                                                                   elem,
                                                                                   padElement
                                                   ))
                                     )
                                             .apply(value) :
                                     putEmptyJson(seq).test(index,
                                                            tail
                                     ) ?
                                     new JsArray(nullPadding(index,
                                                             seq,
                                                             tail.head()
                                                                 .match(key -> JsObj.EMPTY
                                                                                .set(tail,
                                                                                     value,
                                                                                     padElement

                                                                                ),
                                                                        i -> JsArray.EMPTY
                                                                                .set(tail,
                                                                                     value,
                                                                                     padElement
                                                                                )
                                                                 ),
                                                             padElement
                                     )) :

                                     new JsArray(seq.update(index,
                                                            seq.get(index)
                                                               .toJson()
                                                               .set(tail,
                                                                    value,
                                                                    padElement
                                                               )
                                     ));

                          }

                   );

    }


    @Override
    public <R> Optional<R> reduce(final BinaryOperator<R> op,
                                  final BiFunction<? super JsPath, ? super JsPrimitive, R> map,
                                  final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
    ) {
        return OpMapReduce.reduceArr(this,
                                     JsPath.fromIndex(-1),
                                     requireNonNull(predicate),
                                     map,
                                     op,
                                     Optional.empty()
        );

    }

    @Override
    public <R> Optional<R> reduce(final BinaryOperator<R> op,
                                  final Function<? super JsPrimitive, R> map,
                                  final Predicate<? super JsPrimitive> predicate) {
        return OpMapReduce.reduceArr(this,
                                     requireNonNull(predicate),
                                     map,
                                     op,
                                     Optional.empty()
        );
    }

    @Override
    public JsArray delete(final JsPath path) {
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(head -> this,
                          index ->
                          {
                              int maxIndex = seq.length() - 1;
                              if (index < 0 || index > maxIndex) return this;
                              JsPath tail = path.tail();
                              return tail.isEmpty() ?
                                     new JsArray(seq.removeAt(index)) :
                                     ifJsonElse(json -> new JsArray(seq.update(index,
                                                                               json.delete(tail)
                                                )),
                                                e -> this
                                     )
                                             .apply(seq.get(index));
                          }

                   );


    }

    @Override
    public int size() {
        return seq.length();
    }

    @Override
    public Stream<JsPair> stream() {
        return streamOfArr(this,
                           JsPath.empty()
        );
    }


    private boolean yContainsX(final Vector<JsValue> x,
                               final Vector<JsValue> y
    ) {
        for (int i = 0; i < x.length(); i++) {
            if (!Objects.equals(x.get(i),
                                y.get(i)
            ))
                return false;

        }
        return true;

    }

    /**
     * returns the element located at the specified index or JsNothing if it doesn't exist. It never throws
     * an exception, it's a total function.
     *
     * @param i the index
     * @return a JsValue
     */
    public JsValue get(final int i) {
        try {
            return seq.get(i);
        } catch (IndexOutOfBoundsException e) {
            return NOTHING;
        }
    }

    /**
     * equals method is inherited, so it's implemented. The purpose of this method is to cache
     * the hashcode once calculated. the object is immutable and it won't change
     * Single-check idiom  Item 83 from Effective Java
     */
    @Override
    @SuppressWarnings("squid:S1206")
    public int hashCode() {
        int result = hashcode;
        if (result == 0)
            hashcode = result = seq.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object that) {
        if (!(that instanceof JsArray)) return false;
        if (this == that) return true;
        Vector<JsValue> thatSeq = ((JsArray) that).seq;
        boolean thatEmpty = thatSeq.isEmpty();
        boolean thisEmpty = isEmpty();
        if (thatEmpty && thisEmpty) return true;
        if (this.size() != thatSeq.length()) return false;
        return yContainsX(seq,
                          thatSeq
        ) && yContainsX(thatSeq,
                        seq
        );

    }

    /**
     * // Single-check idiom  Item 83 from effective java
     */
    @Override
    public String toString() {
        String result = str;
        if (result == null)
            str = result = new String(JsonIO.INSTANCE.serialize(this),
                                      StandardCharsets.UTF_8);

        return result;
    }

    /**
     * Returns the first element of this array.
     *
     * @return the first JsValue of this JsArray
     * @throws UserError if this JsArray is empty
     */
    public JsValue head() {
        return seq.head();
    }

    /**
     * Returns a json array consisting of all elements of this array except the first one.
     *
     * @return a JsArray consisting of all the elements of this JsArray except the head
     * @throws UserError if this JsArray is empty.
     */
    public JsArray tail() {
        return new JsArray(seq.tail());
    }

    /**
     * Returns all the elements of this array except the last one.
     *
     * @return JsArray with all the JsValue except the last one
     * @throws UserError if this JsArray is empty
     */
    public JsArray init() {
        return new JsArray(seq.init());
    }


    /**
     * {@code this.union(that, SET)} returns {@code this} plus those elements from {@code that} that
     * don't exist in {@code this}.
     * {@code this.union(that, MULTISET)} returns {@code this} plus those elements from {@code that}
     * appended to the back.
     * {@code this.union(that, LIST)} returns {@code this} plus those elements from {@code that} which
     * position is {@code >= this.size()}. For those
     * elements that are containers of the same type and are located at the same position, the result
     * is their intersection. So this operation is kind of a recursive intersection
     *
     * @param that the other array
     * @return a JsArray of the same type as the inputs
     */
    @Override
    public JsArray intersection(final JsArray that,
                                final JsArray.TYPE ARRAY_AS) {
        return intersection(this,
                            requireNonNull(that),
                            ARRAY_AS
        );
    }

    @Override
    public int id() {
        return TYPE_ID;
    }
    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public Iterator<JsValue> iterator() {
        return seq.iterator();
    }

    /**
     * Returns the last element of this array.
     *
     * @return the last JsValue of this JsArray
     * @throws UserError if this JsArray is empty
     */
    public JsValue last() {
        return seq.last();
    }

    /**
     * Adds one or more elements, starting from the last, to the front of this array.
     *
     * @param e      the JsValue to be added to the front.
     * @param others more optional JsValue to be added to the front
     * @return a new JsArray
     */
    public JsArray prepend(final JsValue e,
                           final JsValue... others
    ) {
        Vector<JsValue> acc = seq;
        for (int i = 0, othersLength = requireNonNull(others).length; i < othersLength; i++) {
            JsValue other = others[othersLength - 1 - i];
            acc = acc.prepend(requireNonNull(other));
        }
        return new JsArray(acc.prepend(requireNonNull(e)));
    }

    /**
     * Adds all the elements of the array, starting from the last, to the front of this array.
     *
     * @param array the JsArray of elements to be added to the front
     * @return a new JsArray
     */
    public JsArray prependAll(final JsArray array) {
        return appendAllFront(this,
                              requireNonNull(array)
        );

    }

    @SuppressWarnings("squid:S1602")
    // curly braces makes IntelliJ to format the code in a more legible way
    private BiPredicate<Integer, JsPath> putEmptyJson(final Vector<JsValue> pseq) {
        return (index, tail) ->
                index > pseq.length() - 1 || pseq.isEmpty() || pseq.get(index)
                                                                 .isPrimitive()
                        ||
                        (tail.head()
                             .isKey() && pseq.get(index)
                                             .isArray()
                        )
                        ||
                        (tail.head()
                             .isIndex() && pseq.get(index)
                                               .isObj()
                        );
    }

    public JsArray delete(final int index) {
        if (index < -0) throw new IllegalArgumentException("index must be >= 0");
        int maxIndex = seq.length() - 1;
        if (index > maxIndex) return this;
        return new JsArray(seq.removeAt(index));
    }


    /**
     * returns {@code this} plus those elements from {@code that} which position is  {@code >= this.size()},
     * and, at the positions where a container of the same type exists in both {@code this} and {@code that},
     * the result is their union. This operations doesn't make any sense if arrays are not considered lists,
     * because there is no notion of order.
     *
     * @param that the other array
     * @return a new JsArray of the same type as the inputs
     */
    @SuppressWarnings("squid:S00100")
    @Override
    public JsArray union(final JsArray that,
                         final TYPE ARRAY_AS) {
        return union(this,
                     requireNonNull(that),
                     ARRAY_AS
        );
    }

    private JsArray intersection(final JsArray a,
                                 final JsArray b,
                                 final JsArray.TYPE ARRAY_AS
    ) {
        if (a.isEmpty()) return a;
        if (b.isEmpty()) return b;

        JsArray result = JsArray.empty();

        for (int i = 0; i < a.size(); i++) {
            JsValue head = a.get(i);
            JsValue otherHead = b.get(i);
            if (head.isJson() && head.isSameType(otherHead)) {
                Json<?> obj = head.toJson();
                Json<?> obj1 = otherHead.toJson();
                result = result.set(i,
                                    OpIntersectionJsons.intersectionAll(obj,
                                                                        obj1,
                                                                        ARRAY_AS
                                    )
                );


            } else if (head.equals(otherHead))
                result = result.set(i,
                                    head
                );

        }

        return result;
    }

    private Vector<JsValue> nullPadding(final int index,
                                        Vector<JsValue> arr,
                                        final JsValue e,
                                        final JsValue pad
    ) {
        assert arr != null;
        assert e != null;

        if (index == arr.length()) return arr.append(e);


        if (index < arr.length()) return arr.update(index,
                                                  e);
        for (int j = arr.length(); j < index; j++) {
            arr = arr.append(pad);
        }
        return arr.append(e);
    }


    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name


    private JsArray union(final JsArray a,
                          final JsArray b,
                          final TYPE ARRAY_AS
    ) {
        if (b.isEmpty()) return a;
        if (a.isEmpty()) return b;
        JsArray result = a;
        for (int i = 0; i < b.size(); i++) {
            JsValue head = a.get(i);
            JsValue otherHead = b.get(i);
            if (head.isJson() && head.isSameType(otherHead)) {
                Json<?> obj = head.toJson();
                Json<?> obj1 = otherHead.toJson();
                result = result.set(i,
                                    OpUnionJsons.unionAll(obj,
                                                          obj1,
                                                          ARRAY_AS
                                    )
                );

            } else if (!otherHead.isNothing() && head.isNothing()) result = result.append(otherHead);
        }

        return result;


    }


    /**
     * Type of arrays: SET, MULTISET or LIST.
     */
    public enum TYPE {
        /**
         * The order of data items does not matter (or is undefined) but duplicate data items are not
         * permitted.
         */
        SET,
        /**
         * The order of data matters and duplicate data items are permitted.
         */
        LIST,
        /**
         * The order of data items does not matter, but in this
         * case duplicate data items are permitted.
         */
        MULTISET
    }


}



