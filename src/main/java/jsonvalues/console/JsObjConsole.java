package jsonvalues.console;

import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.future.JsFuture;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;


/**
 represents a supplier of a completable future than composes a json object from the user inputs on
 the console. It has the same recursive structure as a json object. Each key of the object
 has a supplier of a completable future associated that, when executed, prints out its path on the
 console and waits for the user to type in a value and press Enter. When the user fills out
 all the values, all the futures are completed and a json object is composed.
 */
public class JsObjConsole implements JsConsole<JsObj>, Program<JsObj> {

    private Map<String, JsConsole<?>> bindings = new LinkedHashMap<>();

    /**
     static factory method to create a JsObjIO of sixteen mappings

     @param key1  the first key
     @param io1   the mapping associated to the first key
     @param key2  the second key
     @param io2   the mapping associated to the second key
     @param key3  the third key
     @param io3   the mapping associated to the third key
     @param key4  the forth key
     @param io4   the mapping associated to the forth key
     @param key5  the fifth key
     @param io5   the mapping associated to the fifth key
     @param key6  the sixth key
     @param io6   the mapping associated to the sixth key
     @param key7  the seventh key
     @param io7   the mapping associated to the seventh key
     @param key8  the eight key
     @param io8   the mapping associated to the eight key
     @param key9  the ninth key
     @param io9   the mapping associated to the ninth key
     @param key10 the tenth key
     @param io10  the mapping associated to the tenth key
     @param key11 the eleventh key
     @param io11  the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param io12  the mapping associated to the twelfth key
     @param key13 the thirteenth key
     @param io13  the mapping associated to the thirteenth key
     @param key14 the fourteenth key
     @param io14  the mapping associated to the fourteenth key
     @param key15 the fifteenth key
     @param io15  the mapping associated to the fifteenth key
     @param key16 the sixteenth key
     @param io16  the mapping associated to the sixteenth key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5,
                                  final String key6,
                                  final JsConsole<?> io6,
                                  final String key7,
                                  final JsConsole<?> io7,
                                  final String key8,
                                  final JsConsole<?> io8,
                                  final String key9,
                                  final JsConsole<?> io9,
                                  final String key10,
                                  final JsConsole<?> io10,
                                  final String key11,
                                  final JsConsole<?> io11,
                                  final String key12,
                                  final JsConsole<?> io12,
                                  final String key13,
                                  final JsConsole<?> io13,
                                  final String key14,
                                  final JsConsole<?> io14,
                                  final String key15,
                                  final JsConsole<?> io15,
                                  final String key16,
                                  final JsConsole<?> io16
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4,
                                                     key5,
                                                     io5,
                                                     key6,
                                                     io6,
                                                     key7,
                                                     io7,
                                                     key8,
                                                     io8,
                                                     key9,
                                                     io9,
                                                     key10,
                                                     io10,
                                                     key11,
                                                     io11,
                                                     key12,
                                                     io12,
                                                     key13,
                                                     io13,
                                                     key14,
                                                     io14,
                                                     key15,
                                                     io15
                                                    );

        console.bindings.put(requireNonNull(key16),
                             requireNonNull(io16)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of fifteen mappings

     @param key1  the first key
     @param io1   the mapping associated to the first key
     @param key2  the second key
     @param io2   the mapping associated to the second key
     @param key3  the third key
     @param io3   the mapping associated to the third key
     @param key4  the forth key
     @param io4   the mapping associated to the forth key
     @param key5  the fifth key
     @param io5   the mapping associated to the fifth key
     @param key6  the sixth key
     @param io6   the mapping associated to the sixth key
     @param key7  the seventh key
     @param io7   the mapping associated to the seventh key
     @param key8  the eight key
     @param io8   the mapping associated to the eight key
     @param key9  the ninth key
     @param io9   the mapping associated to the ninth key
     @param key10 the tenth key
     @param io10  the mapping associated to the tenth key
     @param key11 the eleventh key
     @param io11  the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param io12  the mapping associated to the twelfth key
     @param key13 the thirteenth key
     @param io13  the mapping associated to the thirteenth key
     @param key14 the fourteenth key
     @param io14  the mapping associated to the fourteenth key
     @param key15 the fifteenth key
     @param io15  the mapping associated to the fifteenth key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5,
                                  final String key6,
                                  final JsConsole<?> io6,
                                  final String key7,
                                  final JsConsole<?> io7,
                                  final String key8,
                                  final JsConsole<?> io8,
                                  final String key9,
                                  final JsConsole<?> io9,
                                  final String key10,
                                  final JsConsole<?> io10,
                                  final String key11,
                                  final JsConsole<?> io11,
                                  final String key12,
                                  final JsConsole<?> io12,
                                  final String key13,
                                  final JsConsole<?> io13,
                                  final String key14,
                                  final JsConsole<?> io14,
                                  final String key15,
                                  final JsConsole<?> io15
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4,
                                                     key5,
                                                     io5,
                                                     key6,
                                                     io6,
                                                     key7,
                                                     io7,
                                                     key8,
                                                     io8,
                                                     key9,
                                                     io9,
                                                     key10,
                                                     io10,
                                                     key11,
                                                     io11,
                                                     key12,
                                                     io12,
                                                     key13,
                                                     io13,
                                                     key14,
                                                     io14
                                                    );

        console.bindings.put(requireNonNull(key15),
                             requireNonNull(io15)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of fourteen mappings

     @param key1  the first key
     @param io1   the mapping associated to the first key
     @param key2  the second key
     @param io2   the mapping associated to the second key
     @param key3  the third key
     @param io3   the mapping associated to the third key
     @param key4  the forth key
     @param io4   the mapping associated to the forth key
     @param key5  the fifth key
     @param io5   the mapping associated to the fifth key
     @param key6  the sixth key
     @param io6   the mapping associated to the sixth key
     @param key7  the seventh key
     @param io7   the mapping associated to the seventh key
     @param key8  the eight key
     @param io8   the mapping associated to the eight key
     @param key9  the ninth key
     @param io9   the mapping associated to the ninth key
     @param key10 the tenth key
     @param io10  the mapping associated to the tenth key
     @param key11 the eleventh key
     @param io11  the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param io12  the mapping associated to the twelfth key
     @param key13 the thirteenth key
     @param io13  the mapping associated to the thirteenth key
     @param key14 the fourteenth key
     @param io14  the mapping associated to the fourteenth key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5,
                                  final String key6,
                                  final JsConsole<?> io6,
                                  final String key7,
                                  final JsConsole<?> io7,
                                  final String key8,
                                  final JsConsole<?> io8,
                                  final String key9,
                                  final JsConsole<?> io9,
                                  final String key10,
                                  final JsConsole<?> io10,
                                  final String key11,
                                  final JsConsole<?> io11,
                                  final String key12,
                                  final JsConsole<?> io12,
                                  final String key13,
                                  final JsConsole<?> io13,
                                  final String key14,
                                  final JsConsole<?> io14
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4,
                                                     key5,
                                                     io5,
                                                     key6,
                                                     io6,
                                                     key7,
                                                     io7,
                                                     key8,
                                                     io8,
                                                     key9,
                                                     io9,
                                                     key10,
                                                     io10,
                                                     key11,
                                                     io11,
                                                     key12,
                                                     io12,
                                                     key13,
                                                     io13
                                                    );

        console.bindings.put(requireNonNull(key14),
                             requireNonNull(io14)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of thirteen mappings

     @param key1  the first key
     @param io1   the mapping associated to the first key
     @param key2  the second key
     @param io2   the mapping associated to the second key
     @param key3  the third key
     @param io3   the mapping associated to the third key
     @param key4  the forth key
     @param io4   the mapping associated to the forth key
     @param key5  the fifth key
     @param io5   the mapping associated to the fifth key
     @param key6  the sixth key
     @param io6   the mapping associated to the sixth key
     @param key7  the seventh key
     @param io7   the mapping associated to the seventh key
     @param key8  the eight key
     @param io8   the mapping associated to the eight key
     @param key9  the ninth key
     @param io9   the mapping associated to the ninth key
     @param key10 the tenth key
     @param io10  the mapping associated to the tenth key
     @param key11 the eleventh key
     @param io11  the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param io12  the mapping associated to the twelfth key
     @param key13 the thirteenth key
     @param io13  the mapping associated to the thirteenth key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5,
                                  final String key6,
                                  final JsConsole<?> io6,
                                  final String key7,
                                  final JsConsole<?> io7,
                                  final String key8,
                                  final JsConsole<?> io8,
                                  final String key9,
                                  final JsConsole<?> io9,
                                  final String key10,
                                  final JsConsole<?> io10,
                                  final String key11,
                                  final JsConsole<?> io11,
                                  final String key12,
                                  final JsConsole<?> io12,
                                  final String key13,
                                  final JsConsole<?> io13
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4,
                                                     key5,
                                                     io5,
                                                     key6,
                                                     io6,
                                                     key7,
                                                     io7,
                                                     key8,
                                                     io8,
                                                     key9,
                                                     io9,
                                                     key10,
                                                     io10,
                                                     key11,
                                                     io11,
                                                     key12,
                                                     io12
                                                    );

        console.bindings.put(requireNonNull(key13),
                             requireNonNull(io13)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of twelve mappings

     @param key1  the first key
     @param io1   the mapping associated to the first key
     @param key2  the second key
     @param io2   the mapping associated to the second key
     @param key3  the third key
     @param io3   the mapping associated to the third key
     @param key4  the forth key
     @param io4   the mapping associated to the forth key
     @param key5  the fifth key
     @param io5   the mapping associated to the fifth key
     @param key6  the sixth key
     @param io6   the mapping associated to the sixth key
     @param key7  the seventh key
     @param io7   the mapping associated to the seventh key
     @param key8  the eight key
     @param io8   the mapping associated to the eight key
     @param key9  the ninth key
     @param io9   the mapping associated to the ninth key
     @param key10 the tenth key
     @param io10  the mapping associated to the tenth key
     @param key11 the eleventh key
     @param io11  the mapping associated to the eleventh key
     @param key12 the twelfth key
     @param io12  the mapping associated to the twelfth key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5,
                                  final String key6,
                                  final JsConsole<?> io6,
                                  final String key7,
                                  final JsConsole<?> io7,
                                  final String key8,
                                  final JsConsole<?> io8,
                                  final String key9,
                                  final JsConsole<?> io9,
                                  final String key10,
                                  final JsConsole<?> io10,
                                  final String key11,
                                  final JsConsole<?> io11,
                                  final String key12,
                                  final JsConsole<?> io12
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4,
                                                     key5,
                                                     io5,
                                                     key6,
                                                     io6,
                                                     key7,
                                                     io7,
                                                     key8,
                                                     io8,
                                                     key9,
                                                     io9,
                                                     key10,
                                                     io10,
                                                     key11,
                                                     io11
                                                    );

        console.bindings.put(requireNonNull(key12),
                             requireNonNull(io12)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of eleven mappings

     @param key1  the first key
     @param io1   the mapping associated to the first key
     @param key2  the second key
     @param io2   the mapping associated to the second key
     @param key3  the third key
     @param io3   the mapping associated to the third key
     @param key4  the forth key
     @param io4   the mapping associated to the forth key
     @param key5  the fifth key
     @param io5   the mapping associated to the fifth key
     @param key6  the sixth key
     @param io6   the mapping associated to the sixth key
     @param key7  the seventh key
     @param io7   the mapping associated to the seventh key
     @param key8  the eight key
     @param io8   the mapping associated to the eight key
     @param key9  the ninth key
     @param io9   the mapping associated to the ninth key
     @param key10 the tenth key
     @param io10  the mapping associated to the tenth key
     @param key11 the eleventh key
     @param io11  the mapping associated to the eleventh key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5,
                                  final String key6,
                                  final JsConsole<?> io6,
                                  final String key7,
                                  final JsConsole<?> io7,
                                  final String key8,
                                  final JsConsole<?> io8,
                                  final String key9,
                                  final JsConsole<?> io9,
                                  final String key10,
                                  final JsConsole<?> io10,
                                  final String key11,
                                  final JsConsole<?> io11
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4,
                                                     key5,
                                                     io5,
                                                     key6,
                                                     io6,
                                                     key7,
                                                     io7,
                                                     key8,
                                                     io8,
                                                     key9,
                                                     io9,
                                                     key10,
                                                     io10
                                                    );

        console.bindings.put(requireNonNull(key11),
                             requireNonNull(io11)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of ten mappings

     @param key1  the first key
     @param io1   the mapping associated to the first key
     @param key2  the second key
     @param io2   the mapping associated to the second key
     @param key3  the third key
     @param io3   the mapping associated to the third key
     @param key4  the forth key
     @param io4   the mapping associated to the forth key
     @param key5  the fifth key
     @param io5   the mapping associated to the fifth key
     @param key6  the sixth key
     @param io6   the mapping associated to the sixth key
     @param key7  the seventh key
     @param io7   the mapping associated to the seventh key
     @param key8  the eight key
     @param io8   the mapping associated to the eight key
     @param key9  the ninth key
     @param io9   the mapping associated to the ninth key
     @param key10 the tenth key
     @param io10  the mapping associated to the tenth key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5,
                                  final String key6,
                                  final JsConsole<?> io6,
                                  final String key7,
                                  final JsConsole<?> io7,
                                  final String key8,
                                  final JsConsole<?> io8,
                                  final String key9,
                                  final JsConsole<?> io9,
                                  final String key10,
                                  final JsConsole<?> io10
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4,
                                                     key5,
                                                     io5,
                                                     key6,
                                                     io6,
                                                     key7,
                                                     io7,
                                                     key8,
                                                     io8,
                                                     key9,
                                                     io9
                                                    );

        console.bindings.put(requireNonNull(key10),
                             requireNonNull(io10)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of nine mappings

     @param key1 the first key
     @param io1  the mapping associated to the first key
     @param key2 the second key
     @param io2  the mapping associated to the second key
     @param key3 the third key
     @param io3  the mapping associated to the third key
     @param key4 the forth key
     @param io4  the mapping associated to the forth key
     @param key5 the fifth key
     @param io5  the mapping associated to the fifth key
     @param key6 the sixth key
     @param io6  the mapping associated to the sixth key
     @param key7 the seventh key
     @param io7  the mapping associated to the seventh key
     @param key8 the eight key
     @param io8  the mapping associated to the eight key
     @param key9 the ninth key
     @param io9  the mapping associated to the ninth key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5,
                                  final String key6,
                                  final JsConsole<?> io6,
                                  final String key7,
                                  final JsConsole<?> io7,
                                  final String key8,
                                  final JsConsole<?> io8,
                                  final String key9,
                                  final JsConsole<?> io9
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4,
                                                     key5,
                                                     io5,
                                                     key6,
                                                     io6,
                                                     key7,
                                                     io7,
                                                     key8,
                                                     io8
                                                    );

        console.bindings.put(requireNonNull(key9),
                             requireNonNull(io9)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of eight mappings

     @param key1 the first key
     @param io1  the mapping associated to the first key
     @param key2 the second key
     @param io2  the mapping associated to the second key
     @param key3 the third key
     @param io3  the mapping associated to the third key
     @param key4 the forth key
     @param io4  the mapping associated to the forth key
     @param key5 the fifth key
     @param io5  the mapping associated to the fifth key
     @param key6 the sixth key
     @param io6  the mapping associated to the sixth key
     @param key7 the seventh key
     @param io7  the mapping associated to the seventh key
     @param key8 the eight key
     @param io8  the mapping associated to the eight key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5,
                                  final String key6,
                                  final JsConsole<?> io6,
                                  final String key7,
                                  final JsConsole<?> io7,
                                  final String key8,
                                  final JsConsole<?> io8
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4,
                                                     key5,
                                                     io5,
                                                     key6,
                                                     io6,
                                                     key7,
                                                     io7
                                                    );

        console.bindings.put(requireNonNull(key8),
                             requireNonNull(io8)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of seven mappings

     @param key1 the first key
     @param io1  the mapping associated to the first key
     @param key2 the second key
     @param io2  the mapping associated to the second key
     @param key3 the third key
     @param io3  the mapping associated to the third key
     @param key4 the forth key
     @param io4  the mapping associated to the forth key
     @param key5 the fifth key
     @param io5  the mapping associated to the fifth key
     @param key6 the sixth key
     @param io6  the mapping associated to the sixth key
     @param key7 the seventh key
     @param io7  the mapping associated to the seventh key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5,
                                  final String key6,
                                  final JsConsole<?> io6,
                                  final String key7,
                                  final JsConsole<?> io7
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4,
                                                     key5,
                                                     io5,
                                                     key6,
                                                     io6
                                                    );

        console.bindings.put(requireNonNull(key7),
                             requireNonNull(io7)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of six mappings

     @param key1 the first key
     @param io1  the mapping associated to the first key
     @param key2 the second key
     @param io2  the mapping associated to the second key
     @param key3 the third key
     @param io3  the mapping associated to the third key
     @param key4 the forth key
     @param io4  the mapping associated to the forth key
     @param key5 the fifth key
     @param io5  the mapping associated to the fifth key
     @param key6 the sixth key
     @param io6  the mapping associated to the sixth key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5,
                                  final String key6,
                                  final JsConsole<?> io6
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4,
                                                     key5,
                                                     io5
                                                    );

        console.bindings.put(requireNonNull(key6),
                             requireNonNull(io6)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of five mappings

     @param key1 the first key
     @param io1  the mapping associated to the first key
     @param key2 the second key
     @param io2  the mapping associated to the second key
     @param key3 the third key
     @param io3  the mapping associated to the third key
     @param key4 the forth key
     @param io4  the mapping associated to the forth key
     @param key5 the fifth key
     @param io5  the mapping associated to the fifth key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4,
                                  final String key5,
                                  final JsConsole<?> io5
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3,
                                                     key4,
                                                     io4
                                                    );

        console.bindings.put(requireNonNull(key5),
                             requireNonNull(io5)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of four mappings

     @param key1 the first key
     @param io1  the mapping associated to the first key
     @param key2 the second key
     @param io2  the mapping associated to the second key
     @param key3 the third key
     @param io3  the mapping associated to the third key
     @param key4 the forth key
     @param io4  the mapping associated to the forth key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3,
                                  final String key4,
                                  final JsConsole<?> io4
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2,
                                                     key3,
                                                     io3
                                                    );

        console.bindings.put(requireNonNull(key4),
                             requireNonNull(io4)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of three mappings

     @param key1 the first key
     @param io1  the mapping associated to the first key
     @param key2 the second key
     @param io2  the mapping associated to the second key
     @param key3 the third key
     @param io3  the mapping associated to the third key
     @return a JsObjIO
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjConsole of(final String key1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2,
                                  final String key3,
                                  final JsConsole<?> io3
                                 ) {

        final JsObjConsole console = JsObjConsole.of(key1,
                                                     io1,
                                                     key2,
                                                     io2
                                                    );

        console.bindings.put(requireNonNull(key3),
                             requireNonNull(io3)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of two mappings

     @param ke1  the first key
     @param io1  the mapping associated to the first key
     @param key2 the second key
     @param io2  the mapping associated to the second key
     @return a JsObjIO
     */
    public static JsObjConsole of(final String ke1,
                                  final JsConsole<?> io1,
                                  final String key2,
                                  final JsConsole<?> io2
                                 ) {

        final JsObjConsole console = JsObjConsole.of(ke1,
                                                     io1
                                                    );

        console.bindings.put(requireNonNull(key2),
                             requireNonNull(io2)
                            );

        return console;

    }

    /**
     static factory method to create a JsObjIO of one mapping

     @param key the key
     @param io  the mapping associated to the key
     @return a JsObjIO
     */
    public static JsObjConsole of(final String key,
                                  final JsConsole<?> io
                                 ) {

        final JsObjConsole console = new JsObjConsole();

        console.bindings.put(requireNonNull(key),
                             requireNonNull(io)
                            );

        return console;

    }

    @Override
    public JsObj exec() throws ExecutionException, InterruptedException {
        return apply(JsPath.empty()).get()
                                    .get();
    }

    @Override
    public JsFuture<JsObj> apply(final JsPath path) {
        requireNonNull(path);
        return () ->
        {
            CompletableFuture<JsObj> result = CompletableFuture.completedFuture(JsObj.empty());

            for (Map.Entry<String, JsConsole<?>> entry : bindings.entrySet()) {
                JsPath             currentPath = path.append(JsPath.fromKey(entry.getKey()));
                final JsConsole<?> nextValue   = entry.getValue();
                result = result.thenApply(o ->
                                          {
                                              nextValue
                                                      .promptMessage()
                                                      .accept(currentPath);

                                              return o;
                                          })
                               .thenCombine(nextValue
                                                    .apply(currentPath)
                                                    .get(),
                                            (obj, value) -> obj.set(entry.getKey(),
                                                                    value
                                                                   )
                                           );
            }
            return result;
        };


    }

    @Override
    public Consumer<JsPath> promptMessage() {
        return JsIOs.printlnIndentedPath();
    }
}
