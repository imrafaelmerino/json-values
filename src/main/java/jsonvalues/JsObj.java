package jsonvalues;

import com.dslplatform.json.MyDslJson;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import fun.optic.Prism;
import fun.tuple.Pair;
import io.vavr.Tuple2;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import jsonvalues.JsArray.TYPE;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Stream;

import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static java.util.Objects.requireNonNull;
import static jsonvalues.JsArray.streamOfArr;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.MatchExp.ifNothingElse;

/**
 * Represents a json object, which is an unordered set of name/element pairs. Two implementations are
 * provided, an immutable which uses the persistent Scala HashMap, and a mutable which uses the conventional
 * Java HashMap.
 */
public class JsObj implements Json<JsObj>, Iterable<Tuple2<String, JsValue>> {
    public static final JsObj EMPTY = new JsObj(LinkedHashMap.empty());
    /**
     * lenses defined for a Json object
     */
    public static final JsOptics.JsObjLenses lens = JsOptics.obj.lens;
    /**
     * optionals defined for a Json object
     */
    public static final JsOptics.JsObjOptional optional = JsOptics.obj.optional;
    /**
     * prism between the sum type JsValue and JsObj
     */
    public static final Prism<JsValue, JsObj> prism =
            new Prism<>(s -> s.isObj() ?
                             Optional.of(s.toJsObj()) :
                             Optional.empty(),
                        o -> o
            );
    public static final int TYPE_ID = 3;
    @SuppressWarnings("squid:S3008")//EMPTY_PATH should be a valid name
    private static final JsPath EMPTY_PATH = JsPath.empty();
    private final Map<String, JsValue> map;
    private volatile int hascode;
    //squid:S3077: doesn't make any sense, volatile is perfectly valid here an as a matter of fact
    //is a recomendation from Efective Java to apply the idiom single check for lazy initialization
    @SuppressWarnings("squid:S3077")

    private volatile String str;

    public JsObj() {
        this.map = LinkedHashMap.empty();
    }

    JsObj(final Map<String, JsValue> myMap) {
        this.map = myMap;
    }

    public static JsObj empty() {
        return EMPTY;
    }

    /**
     * Returns a one-element immutable object.
     *
     * @param key name of a key
     * @param el  JsValue to be associated to the key
     * @return an immutable one-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    public static JsObj of(final String key,
                           final JsValue el
    ) {

        return JsObj.EMPTY.set(JsPath.empty()
                                     .key(requireNonNull(key)),
                               el
        );
    }

    /**
     * Returns a two-element immutable object.
     *
     * @param key1 name of a key
     * @param el1  JsValue to be associated to the key1
     * @param key2 name of a key
     * @param el2  JsValue to be associated to the key2
     * @return an immutable two-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2
    ) {

        return of(key1,
                  el1
        ).set(JsPath.empty()
                    .key(requireNonNull(key2)),
              el2
        );
    }

    /**
     * Returns a three-element immutable object.
     *
     * @param key1 name of a key
     * @param el1  JsValue to be associated to the key1
     * @param key2 name of a key
     * @param el2  JsValue to be associated to the key2
     * @param key3 name of a key
     * @param el3  JsValue to be associated to the key3
     * @return an immutable three-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3
    ) {
        return of(key1,
                  el1,
                  key2,
                  el2
        ).set(JsPath.empty()
                    .key(requireNonNull(key3)),
              el3
        );
    }

    /**
     * Returns a four-element immutable object.
     *
     * @param key1 name of a key
     * @param el1  JsValue to be associated to the key1
     * @param key2 name of a key
     * @param el2  JsValue to be associated to the key2
     * @param key3 name of a key
     * @param el3  JsValue to be associated to the key3
     * @param key4 name of a key
     * @param el4  JsValue to be associated to the key4
     * @return an immutable four-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3
        ).set(JsPath.empty()
                    .key(requireNonNull(key4)),
              el4
        );
    }

    /**
     * Returns a five-element immutable object.
     *
     * @param key1 name of a key
     * @param el1  JsValue to be associated to the key1
     * @param key2 name of a key
     * @param el2  JsValue to be associated to the key2
     * @param key3 name of a key
     * @param el3  JsValue to be associated to the key3
     * @param key4 name of a key
     * @param el4  JsValue to be associated to the key4
     * @param key5 name of a key
     * @param el5  JsValue to be associated to the key5
     * @return an immutable five-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4
        ).set(JsPath.empty()
                    .key(requireNonNull(key5)),
              el5
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1 name of a key
     * @param el1  JsValue to be associated to the key1
     * @param key2 name of a key
     * @param el2  JsValue to be associated to the key2
     * @param key3 name of a key
     * @param el3  JsValue to be associated to the key3
     * @param key4 name of a key
     * @param el4  JsValue to be associated to the key4
     * @param key5 name of a key
     * @param el5  JsValue to be associated to the key5
     * @param key6 name of a key
     * @param el6  JsValue to be associated to the key6
     * @return an immutable six-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5
        ).set(JsPath.empty()
                    .key(requireNonNull(key6)),
              el6
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1 name of a key
     * @param el1  JsValue to be associated to the key1
     * @param key2 name of a key
     * @param el2  JsValue to be associated to the key2
     * @param key3 name of a key
     * @param el3  JsValue to be associated to the key3
     * @param key4 name of a key
     * @param el4  JsValue to be associated to the key4
     * @param key5 name of a key
     * @param el5  JsValue to be associated to the key5
     * @param key6 name of a key
     * @param el6  JsValue to be associated to the key6
     * @param key7 name of a key
     * @param el7  JsValue to be associated to the key7
     * @return an immutable seven-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6
        ).set(JsPath.empty()
                    .key(requireNonNull(key7)),
              el7
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1 name of a key
     * @param el1  JsValue to be associated to the key1
     * @param key2 name of a key
     * @param el2  JsValue to be associated to the key2
     * @param key3 name of a key
     * @param el3  JsValue to be associated to the key3
     * @param key4 name of a key
     * @param el4  JsValue to be associated to the key4
     * @param key5 name of a key
     * @param el5  JsValue to be associated to the key5
     * @param key6 name of a key
     * @param el6  JsValue to be associated to the key6
     * @param key7 name of a key
     * @param el7  JsValue to be associated to the key7
     * @param key8 name of a key
     * @param el8  JsValue to be associated to the key8
     * @return an immutable eight-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7
        ).set(JsPath.empty()
                    .key(requireNonNull(key8)),
              el8
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1 name of a key
     * @param el1  JsValue to be associated to the key1
     * @param key2 name of a key
     * @param el2  JsValue to be associated to the key2
     * @param key3 name of a key
     * @param el3  JsValue to be associated to the key3
     * @param key4 name of a key
     * @param el4  JsValue to be associated to the key4
     * @param key5 name of a key
     * @param el5  JsValue to be associated to the key5
     * @param key6 name of a key
     * @param el6  JsValue to be associated to the key6
     * @param key7 name of a key
     * @param el7  JsValue to be associated to the key7
     * @param key8 name of a key
     * @param el8  JsValue to be associated to the key8
     * @param key9 name of a key
     * @param el9  JsValue to be associated to the key9
     * @return an immutable nine-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8
        ).set(JsPath.empty()
                    .key(requireNonNull(key9)),
              el9
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1  name of a key
     * @param el1   JsValue to be associated to the key1
     * @param key2  name of a key
     * @param el2   JsValue to be associated to the key2
     * @param key3  name of a key
     * @param el3   JsValue to be associated to the key3
     * @param key4  name of a key
     * @param el4   JsValue to be associated to the key4
     * @param key5  name of a key
     * @param el5   JsValue to be associated to the key5
     * @param key6  name of a key
     * @param el6   JsValue to be associated to the key6
     * @param key7  name of a key
     * @param el7   JsValue to be associated to the key7
     * @param key8  name of a key
     * @param el8   JsValue to be associated to the key8
     * @param key9  name of a key
     * @param el9   JsValue to be associated to the key9
     * @param key10 name of a key
     * @param el10  JsValue to be associated to the key10
     * @return an immutable ten-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9
        ).set(JsPath.empty()
                    .key(requireNonNull(key10)),
              el10
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1  name of a key
     * @param el1   JsValue to be associated to the key1
     * @param key2  name of a key
     * @param el2   JsValue to be associated to the key2
     * @param key3  name of a key
     * @param el3   JsValue to be associated to the key3
     * @param key4  name of a key
     * @param el4   JsValue to be associated to the key4
     * @param key5  name of a key
     * @param el5   JsValue to be associated to the key5
     * @param key6  name of a key
     * @param el6   JsValue to be associated to the key6
     * @param key7  name of a key
     * @param el7   JsValue to be associated to the key7
     * @param key8  name of a key
     * @param el8   JsValue to be associated to the key8
     * @param key9  name of a key
     * @param el9   JsValue to be associated to the key9
     * @param key10 name of a key
     * @param el10  JsValue to be associated to the key10
     * @param key11 name of a key
     * @param el11  JsValue to be associated to the key11
     * @return an immutable eleven-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10
        ).set(JsPath.empty()
                    .key(requireNonNull(key11)),
              el11
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1  name of a key
     * @param el1   JsValue to be associated to the key1
     * @param key2  name of a key
     * @param el2   JsValue to be associated to the key2
     * @param key3  name of a key
     * @param el3   JsValue to be associated to the key3
     * @param key4  name of a key
     * @param el4   JsValue to be associated to the key4
     * @param key5  name of a key
     * @param el5   JsValue to be associated to the key5
     * @param key6  name of a key
     * @param el6   JsValue to be associated to the key6
     * @param key7  name of a key
     * @param el7   JsValue to be associated to the key7
     * @param key8  name of a key
     * @param el8   JsValue to be associated to the key8
     * @param key9  name of a key
     * @param el9   JsValue to be associated to the key9
     * @param key10 name of a key
     * @param el10  JsValue to be associated to the key10
     * @param key11 name of a key
     * @param el11  JsValue to be associated to the key11
     * @param key12 name of a key
     * @param el12  JsValue to be associated to the key12
     * @return an immutable twelve-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11
        ).set(JsPath.empty()
                    .key(requireNonNull(key12)),
              el12
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1  name of a key
     * @param el1   JsValue to be associated to the key1
     * @param key2  name of a key
     * @param el2   JsValue to be associated to the key2
     * @param key3  name of a key
     * @param el3   JsValue to be associated to the key3
     * @param key4  name of a key
     * @param el4   JsValue to be associated to the key4
     * @param key5  name of a key
     * @param el5   JsValue to be associated to the key5
     * @param key6  name of a key
     * @param el6   JsValue to be associated to the key6
     * @param key7  name of a key
     * @param el7   JsValue to be associated to the key7
     * @param key8  name of a key
     * @param el8   JsValue to be associated to the key8
     * @param key9  name of a key
     * @param el9   JsValue to be associated to the key9
     * @param key10 name of a key
     * @param el10  JsValue to be associated to the key10
     * @param key11 name of a key
     * @param el11  JsValue to be associated to the key11
     * @param key12 name of a key
     * @param el12  JsValue to be associated to the key12
     * @param key13 name of a key
     * @param el13  JsValue to be associated to the key13
     * @return an immutable twelve-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12
        ).set(JsPath.empty()
                    .key(requireNonNull(key13)),
              el13
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1  name of a key
     * @param el1   JsValue to be associated to the key1
     * @param key2  name of a key
     * @param el2   JsValue to be associated to the key2
     * @param key3  name of a key
     * @param el3   JsValue to be associated to the key3
     * @param key4  name of a key
     * @param el4   JsValue to be associated to the key4
     * @param key5  name of a key
     * @param el5   JsValue to be associated to the key5
     * @param key6  name of a key
     * @param el6   JsValue to be associated to the key6
     * @param key7  name of a key
     * @param el7   JsValue to be associated to the key7
     * @param key8  name of a key
     * @param el8   JsValue to be associated to the key8
     * @param key9  name of a key
     * @param el9   JsValue to be associated to the key9
     * @param key10 name of a key
     * @param el10  JsValue to be associated to the key10
     * @param key11 name of a key
     * @param el11  JsValue to be associated to the key11
     * @param key12 name of a key
     * @param el12  JsValue to be associated to the key12
     * @param key13 name of a key
     * @param el13  JsValue to be associated to the key13
     * @param key14 name of a key
     * @param el14  JsValue to be associated to the key14
     * @return an immutable twelve-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13
        ).set(JsPath.empty()
                    .key(requireNonNull(key14)),
              el14
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1  name of a key
     * @param el1   JsValue to be associated to the key1
     * @param key2  name of a key
     * @param el2   JsValue to be associated to the key2
     * @param key3  name of a key
     * @param el3   JsValue to be associated to the key3
     * @param key4  name of a key
     * @param el4   JsValue to be associated to the key4
     * @param key5  name of a key
     * @param el5   JsValue to be associated to the key5
     * @param key6  name of a key
     * @param el6   JsValue to be associated to the key6
     * @param key7  name of a key
     * @param el7   JsValue to be associated to the key7
     * @param key8  name of a key
     * @param el8   JsValue to be associated to the key8
     * @param key9  name of a key
     * @param el9   JsValue to be associated to the key9
     * @param key10 name of a key
     * @param el10  JsValue to be associated to the key10
     * @param key11 name of a key
     * @param el11  JsValue to be associated to the key11
     * @param key12 name of a key
     * @param el12  JsValue to be associated to the key12
     * @param key13 name of a key
     * @param el13  JsValue to be associated to the key13
     * @param key14 name of a key
     * @param el14  JsValue to be associated to the key14
     * @param key15 name of a key
     * @param el15  JsValue to be associated to the key15
     * @return an immutable twelve-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14
        ).set(JsPath.empty()
                    .key(requireNonNull(key15)),
              el15
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1  name of a key
     * @param el1   JsValue to be associated to the key1
     * @param key2  name of a key
     * @param el2   JsValue to be associated to the key2
     * @param key3  name of a key
     * @param el3   JsValue to be associated to the key3
     * @param key4  name of a key
     * @param el4   JsValue to be associated to the key4
     * @param key5  name of a key
     * @param el5   JsValue to be associated to the key5
     * @param key6  name of a key
     * @param el6   JsValue to be associated to the key6
     * @param key7  name of a key
     * @param el7   JsValue to be associated to the key7
     * @param key8  name of a key
     * @param el8   JsValue to be associated to the key8
     * @param key9  name of a key
     * @param el9   JsValue to be associated to the key9
     * @param key10 name of a key
     * @param el10  JsValue to be associated to the key10
     * @param key11 name of a key
     * @param el11  JsValue to be associated to the key11
     * @param key12 name of a key
     * @param el12  JsValue to be associated to the key12
     * @param key13 name of a key
     * @param el13  JsValue to be associated to the key13
     * @param key14 name of a key
     * @param el14  JsValue to be associated to the key14
     * @param key15 name of a key
     * @param el15  JsValue to be associated to the key16
     * @param key16 name of a key
     * @param el16  JsValue to be associated to the key16
     * @return an immutable twelve-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15,
                           final String key16,
                           final JsValue el16
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14,
                  key15,
                  el15
        ).set(JsPath.empty()
                    .key(requireNonNull(key16)),
              el16
        );
    }


    /**
     * Returns a six-element immutable object.
     *
     * @param key1  name of a key
     * @param el1   JsValue to be associated to the key1
     * @param key2  name of a key
     * @param el2   JsValue to be associated to the key2
     * @param key3  name of a key
     * @param el3   JsValue to be associated to the key3
     * @param key4  name of a key
     * @param el4   JsValue to be associated to the key4
     * @param key5  name of a key
     * @param el5   JsValue to be associated to the key5
     * @param key6  name of a key
     * @param el6   JsValue to be associated to the key6
     * @param key7  name of a key
     * @param el7   JsValue to be associated to the key7
     * @param key8  name of a key
     * @param el8   JsValue to be associated to the key8
     * @param key9  name of a key
     * @param el9   JsValue to be associated to the key9
     * @param key10 name of a key
     * @param el10  JsValue to be associated to the key10
     * @param key11 name of a key
     * @param el11  JsValue to be associated to the key11
     * @param key12 name of a key
     * @param el12  JsValue to be associated to the key12
     * @param key13 name of a key
     * @param el13  JsValue to be associated to the key13
     * @param key14 name of a key
     * @param el14  JsValue to be associated to the key14
     * @param key15 name of a key
     * @param el15  JsValue to be associated to the key15
     * @param key16 name of a key
     * @param el16  JsValue to be associated to the key16
     * @param key17 name of a key
     * @param el17  JsValue to be associated to the key17
     * @return an immutable twelve-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15,
                           final String key16,
                           final JsValue el16,
                           final String key17,
                           final JsValue el17
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14,
                  key15,
                  el15,
                  key16,
                  el16
        ).set(JsPath.empty()
                    .key(requireNonNull(key17)),
              el17
        );
    }


    /**
     * Returns a six-element immutable object.
     *
     * @param key1  name of a key
     * @param el1   JsValue to be associated to the key1
     * @param key2  name of a key
     * @param el2   JsValue to be associated to the key2
     * @param key3  name of a key
     * @param el3   JsValue to be associated to the key3
     * @param key4  name of a key
     * @param el4   JsValue to be associated to the key4
     * @param key5  name of a key
     * @param el5   JsValue to be associated to the key5
     * @param key6  name of a key
     * @param el6   JsValue to be associated to the key6
     * @param key7  name of a key
     * @param el7   JsValue to be associated to the key7
     * @param key8  name of a key
     * @param el8   JsValue to be associated to the key8
     * @param key9  name of a key
     * @param el9   JsValue to be associated to the key9
     * @param key10 name of a key
     * @param el10  JsValue to be associated to the key10
     * @param key11 name of a key
     * @param el11  JsValue to be associated to the key11
     * @param key12 name of a key
     * @param el12  JsValue to be associated to the key12
     * @param key13 name of a key
     * @param el13  JsValue to be associated to the key13
     * @param key14 name of a key
     * @param el14  JsValue to be associated to the key14
     * @param key15 name of a key
     * @param el15  JsValue to be associated to the key15
     * @param key16 name of a key
     * @param el16  JsValue to be associated to the key16
     * @param key17 name of a key
     * @param el17  JsValue to be associated to the key17
     * @param key18 name of a key
     * @param el18  JsValue to be associated to the key18
     * @return an immutable twelve-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15,
                           final String key16,
                           final JsValue el16,
                           final String key17,
                           final JsValue el17,
                           final String key18,
                           final JsValue el18
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14,
                  key15,
                  el15,
                  key16,
                  el16,
                  key17,
                  el17
        ).set(JsPath.empty()
                    .key(requireNonNull(key18)),
              el18
        );
    }


    /**
     * Returns a six-element immutable object.
     *
     * @param key1  name of a key
     * @param el1   JsValue to be associated to the key1
     * @param key2  name of a key
     * @param el2   JsValue to be associated to the key2
     * @param key3  name of a key
     * @param el3   JsValue to be associated to the key3
     * @param key4  name of a key
     * @param el4   JsValue to be associated to the key4
     * @param key5  name of a key
     * @param el5   JsValue to be associated to the key5
     * @param key6  name of a key
     * @param el6   JsValue to be associated to the key6
     * @param key7  name of a key
     * @param el7   JsValue to be associated to the key7
     * @param key8  name of a key
     * @param el8   JsValue to be associated to the key8
     * @param key9  name of a key
     * @param el9   JsValue to be associated to the key9
     * @param key10 name of a key
     * @param el10  JsValue to be associated to the key10
     * @param key11 name of a key
     * @param el11  JsValue to be associated to the key11
     * @param key12 name of a key
     * @param el12  JsValue to be associated to the key12
     * @param key13 name of a key
     * @param el13  JsValue to be associated to the key13
     * @param key14 name of a key
     * @param el14  JsValue to be associated to the key14
     * @param key15 name of a key
     * @param el15  JsValue to be associated to the key15
     * @param key16 name of a key
     * @param el16  JsValue to be associated to the key16
     * @param key17 name of a key
     * @param el17  JsValue to be associated to the key17
     * @param key18 name of a key
     * @param el18  JsValue to be associated to the key18
     * @param key19 name of a key
     * @param el19  JsValue to be associated to the key18
     * @return an immutable twelve-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15,
                           final String key16,
                           final JsValue el16,
                           final String key17,
                           final JsValue el17,
                           final String key18,
                           final JsValue el18,
                           final String key19,
                           final JsValue el19
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14,
                  key15,
                  el15,
                  key16,
                  el16,
                  key17,
                  el17,
                  key18,
                  el18
        ).set(JsPath.empty()
                    .key(requireNonNull(key19)),
              el19
        );
    }

    /**
     * Returns a six-element immutable object.
     *
     * @param key1  name of a key
     * @param el1   JsValue to be associated to the key1
     * @param key2  name of a key
     * @param el2   JsValue to be associated to the key2
     * @param key3  name of a key
     * @param el3   JsValue to be associated to the key3
     * @param key4  name of a key
     * @param el4   JsValue to be associated to the key4
     * @param key5  name of a key
     * @param el5   JsValue to be associated to the key5
     * @param key6  name of a key
     * @param el6   JsValue to be associated to the key6
     * @param key7  name of a key
     * @param el7   JsValue to be associated to the key7
     * @param key8  name of a key
     * @param el8   JsValue to be associated to the key8
     * @param key9  name of a key
     * @param el9   JsValue to be associated to the key9
     * @param key10 name of a key
     * @param el10  JsValue to be associated to the key10
     * @param key11 name of a key
     * @param el11  JsValue to be associated to the key11
     * @param key12 name of a key
     * @param el12  JsValue to be associated to the key12
     * @param key13 name of a key
     * @param el13  JsValue to be associated to the key13
     * @param key14 name of a key
     * @param el14  JsValue to be associated to the key14
     * @param key15 name of a key
     * @param el15  JsValue to be associated to the key15
     * @param key16 name of a key
     * @param el16  JsValue to be associated to the key16
     * @param key17 name of a key
     * @param el17  JsValue to be associated to the key17
     * @param key18 name of a key
     * @param el18  JsValue to be associated to the key18
     * @param key19 name of a key
     * @param el19  JsValue to be associated to the key19
     * @param key20 name of a key
     * @param el20  JsValue to be associated to the key20

     * @return an immutable twelve-element JsObj
     * @throws UserError if an elem is a mutable Json
     */
    // squid:S00107: static factory methods usually have more than 4 parameters, that's one their advantages precisely
    @SuppressWarnings("squid:S00107")
    public static JsObj of(final String key1,
                           final JsValue el1,
                           final String key2,
                           final JsValue el2,
                           final String key3,
                           final JsValue el3,
                           final String key4,
                           final JsValue el4,
                           final String key5,
                           final JsValue el5,
                           final String key6,
                           final JsValue el6,
                           final String key7,
                           final JsValue el7,
                           final String key8,
                           final JsValue el8,
                           final String key9,
                           final JsValue el9,
                           final String key10,
                           final JsValue el10,
                           final String key11,
                           final JsValue el11,
                           final String key12,
                           final JsValue el12,
                           final String key13,
                           final JsValue el13,
                           final String key14,
                           final JsValue el14,
                           final String key15,
                           final JsValue el15,
                           final String key16,
                           final JsValue el16,
                           final String key17,
                           final JsValue el17,
                           final String key18,
                           final JsValue el18,
                           final String key19,
                           final JsValue el19,
                           final String key20,
                           final JsValue el20
    ) {

        return of(key1,
                  el1,
                  key2,
                  el2,
                  key3,
                  el3,
                  key4,
                  el4,
                  key5,
                  el5,
                  key6,
                  el6,
                  key7,
                  el7,
                  key8,
                  el8,
                  key9,
                  el9,
                  key10,
                  el10,
                  key11,
                  el11,
                  key12,
                  el12,
                  key13,
                  el13,
                  key14,
                  el14,
                  key15,
                  el15,
                  key16,
                  el16,
                  key17,
                  el17,
                  key18,
                  el18,
                  key19,
                  el19
        ).set(JsPath.empty()
                    .key(requireNonNull(key20)),
              el20
        );
    }

    /**
     * Returns an immutable object from one or more pairs.
     *
     * @param pair   a pair
     * @param others more optional pairs
     * @return an immutable JsObject
     * @throws UserError if an elem of a pair is mutable
     */
    @SafeVarargs
    public static JsObj of(final Pair<JsPath, JsValue> pair,
                           final Pair<JsPath, JsValue>... others
    ) {
        Objects.requireNonNull(pair);
        Objects.requireNonNull(others);
        JsObj obj = JsObj.EMPTY.set(pair.first(),
                                    pair.second()
        );
        for (Pair<JsPath, JsValue> p : others) {

            obj = obj.set(p.first(),
                          p.second()
            );
        }
        return obj;

    }

    public static JsObj ofIterable(final Iterable<java.util.Map.Entry<String, JsValue>> xs) {
        JsObj acc = JsObj.EMPTY;
        for (java.util.Map.Entry<String, JsValue> x : requireNonNull(xs)) {

            acc = acc.set(x.getKey(),
                          x.getValue()
            );
        }
        return acc;
    }

    /**
     * Tries to parse the string into an immutable object.
     *
     * @param str the string to be parsed
     * @return a JsOb object
     * @throws MalformedJson if the string doesnt represent a json object
     */
    public static JsObj parse(final String str) {

        try (JsonParser parser = JacksonFactory.INSTANCE.createParser(requireNonNull(str))) {
            JsonToken keyEvent = parser.nextToken();
            if (START_OBJECT != keyEvent) throw MalformedJson.expectedObj(str);
            return new JsObj(JsObj.parse(parser));
        } catch (IOException e) {
            throw new MalformedJson(e.getMessage());
        }
    }

    /**
     * Tries to parse a YAML string into an immutable object.
     *
     * @param str the YAML to be parsed
     * @return a JsOb object
     * @throws MalformedJson if the string doesnt represent a json object
     */
    public static JsObj parseYaml(final String str) {

        try (JsonParser parser = JacksonFactory.YAML_FACTORY.createParser(requireNonNull(str))) {
            JsonToken keyEvent = parser.nextToken();
            if (START_OBJECT != keyEvent) throw MalformedJson.expectedObj(str);
            return new JsObj(JsObj.parse(parser));
        } catch (IOException e) {
            throw new MalformedJson(e.getMessage());
        }
    }

    static Map<String, JsValue> parse(final JsonParser parser) throws IOException {
        Map<String, JsValue> map = LinkedHashMap.empty();
        String key = parser.nextFieldName();
        for (; key != null; key = parser.nextFieldName()) {
            JsValue elem;
            switch (parser.nextToken()
                          .id()) {
                case JsonTokenId.ID_STRING:
                    elem = JsStr.of(parser.getValueAsString());
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    elem = JsNumber.of(parser);
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    elem = JsBigDec.of(parser.getDecimalValue());
                    break;
                case JsonTokenId.ID_FALSE:
                    elem = FALSE;
                    break;
                case JsonTokenId.ID_TRUE:
                    elem = TRUE;
                    break;
                case JsonTokenId.ID_NULL:
                    elem = NULL;
                    break;
                case JsonTokenId.ID_START_OBJECT:
                    elem = new JsObj(parse(parser));
                    break;
                case JsonTokenId.ID_START_ARRAY:
                    elem = new JsArray(JsArray.parse(parser));
                    break;
                default:
                    throw JsValuesInternalError.tokenNotExpected(parser.currentToken()
                                                                       .name());
            }
            map = map.put(key,
                          elem
            );
        }

        return map;

    }

    static Stream<Pair<JsPath, JsValue>> streamOfObj(final JsObj obj,
                                                     final JsPath path
    ) {

        requireNonNull(path);
        return requireNonNull(obj).ifEmptyElse(() -> Stream.of(new Pair<>(path,
                                                                          obj
                                               )),
                                               () -> obj.keySet()
                                                        .stream()
                                                        .map(key -> new Pair<>(path.key(key),
                                                                               get(obj,
                                                                                   Key.of(key)
                                                                               )
                                                        ))
                                                        .flatMap(pair -> MatchExp.ifJsonElse(o -> streamOfObj(o,
                                                                                                              pair.first()
                                                                                             ),
                                                                                             a -> streamOfArr(a,
                                                                                                              pair.first()
                                                                                             ),
                                                                                             e -> Stream.of(pair)
                                                                                 )
                                                                                 .apply(pair.second()))
        );

    }

    private static JsValue get(final JsObj obj,
                               final Position position
    ) {
        return requireNonNull(position).match(key -> obj.map.getOrElse(key,
                                                                       NOTHING
                                              ),
                                              index -> NOTHING
        );
    }

    /**
     * Inserts the element at the key in this json, replacing any existing element.
     *
     * @param key   the key
     * @param value the element
     * @return a new json object
     */
    public JsObj set(final String key,
                     final JsValue value
    ) {
        requireNonNull(key);
        return ifNothingElse(() -> this.delete(key),
                             elem -> new JsObj(map.put(key,
                                                       elem
                             )
                             )
        )
                .apply(requireNonNull(value));
    }

    public JsObj delete(final String key) {
        if (!map.containsKey(requireNonNull(key))) return this;
        return new JsObj(map.remove(key));
    }

    @Override
    public boolean containsValue(final JsValue el) {
        return stream().anyMatch(p -> p.second().equals(requireNonNull(el)));
    }

    /**
     * Returns a set containing each key fo this object.
     *
     * @return a Set containing each key of this JsObj
     */
    public Set<String> keySet() {
        return map.keySet()
                  .toJavaSet();
    }

    @Override
    public JsValue get(final JsPath path) {
        if (path.isEmpty()) return this;
        final JsValue e = get(this,
                              path.head()
        );
        final JsPath tail = path.tail();
        if (tail.isEmpty()) return e;
        if (e.isPrimitive()) return NOTHING;
        return e.toJson()
                .get(tail);
    }

    @Override
    public JsObj filterValues(final Predicate<? super JsPrimitive> filter) {
        return OpFilterObjElems.filter(this,
                                       requireNonNull(filter)
        );
    }

    @Override
    public JsObj filterAllValues(final BiPredicate<? super JsPath, ? super JsPrimitive> filter) {
        return OpFilterObjElems.filterAll(this,
                                          JsPath.empty(),
                                          requireNonNull(filter)
        );

    }

    @Override
    public JsObj filterAllValues(final Predicate<? super JsPrimitive> filter) {
        return OpFilterObjElems.filterAll(this,
                                          requireNonNull(filter)
        );
    }

    @Override
    public JsObj filterKeys(final Predicate<? super String> filter) {

        return OpFilterObjKeys.filter(this,
                                      filter
        );
    }

    @Override
    public JsObj filterAllKeys(final BiPredicate<? super JsPath, ? super JsValue> filter) {
        return OpFilterObjKeys.filterAll(this,
                                         JsPath.empty(),
                                         filter
        );
    }

    @Override
    public JsObj filterAllKeys(final Predicate<? super String> filter) {
        return OpFilterObjKeys.filterAll(this,
                                         filter
        );
    }

    @Override
    public JsObj filterObjs(final Predicate<? super JsObj> filter) {
        return OpFilterObjObjs.filter(this,
                                      requireNonNull(filter)
        );
    }

    @Override
    public JsObj filterAllObjs(final BiPredicate<? super JsPath, ? super JsObj> filter) {
        return OpFilterObjObjs.filterAll(this,
                                         JsPath.empty(),
                                         requireNonNull(filter)
        );

    }

    @Override
    public JsObj filterAllObjs(final Predicate<? super JsObj> filter) {
        return OpFilterObjObjs.filterAll(this,
                                         requireNonNull(filter)
        );
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public JsObj mapValues(final Function<? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapObjElems.map(this,
                                 requireNonNull(fn)
        );
    }

    @Override
    public JsObj mapAllValues(final BiFunction<? super JsPath, ? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapObjElems.mapAll(this,
                                    requireNonNull(fn),
                                    EMPTY_PATH
        );
    }

    @Override
    public JsObj mapAllValues(final Function<? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapObjElems.mapAll(this,
                                    requireNonNull(fn)
        );
    }

    @Override
    public JsObj mapKeys(final Function<? super String, String> fn) {
        return OpMapObjKeys.map(this,
                                requireNonNull(fn)
        );
    }

    @Override
    public JsObj mapAllKeys(final BiFunction<? super JsPath, ? super JsValue, String> fn) {
        return OpMapObjKeys.mapAll(this,
                                   requireNonNull(fn),
                                   EMPTY_PATH
        );
    }

    @Override
    public JsObj mapAllKeys(final Function<? super String, String> fn) {
        return OpMapObjKeys.mapAll(this,
                                   requireNonNull(fn)
        );
    }

    @Override
    public JsObj mapObjs(final Function<? super JsObj, JsValue> fn) {
        return OpMapObjObjs.map(this,
                                requireNonNull(fn)
        );
    }

    @Override
    public JsObj mapAllObjs(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn) {
        return OpMapObjObjs.mapAll(this,
                                   requireNonNull(fn),
                                   JsPath.empty()
        );
    }

    @Override
    public JsObj mapAllObjs(final Function<? super JsObj, JsValue> fn) {
        return OpMapObjObjs.mapAll(this,
                                   requireNonNull(fn)
        );
    }

    @Override
    public JsObj set(final JsPath path,
                     final JsValue value,
                     final JsValue padElement

    ) {
        requireNonNull(value);
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(head ->
                          {
                              final JsPath tail = path.tail();

                              return tail.isEmpty() ?
                                     ifNothingElse(() -> this.delete(head),
                                                   elem -> new JsObj(map.put(head,
                                                                             elem
                                                   ))
                                     )
                                             .apply(value) :
                                     isReplaceWithEmptyJson(map).test(head,
                                                                      tail
                                     ) ?
                                     new JsObj(map.put(head,
                                                       tail.head()
                                                           .match(key -> JsObj.EMPTY
                                                                          .set(tail,
                                                                               value,
                                                                               padElement
                                                                          ),
                                                                  index -> JsArray.EMPTY
                                                                          .set(tail,
                                                                               value,
                                                                               padElement
                                                                          )
                                                           )
                                     )) :
                                     new JsObj(map.put(head,
                                                       map.get(head)
                                                          .get()
                                                          .toJson()
                                                          .set(tail,
                                                               value,
                                                               padElement
                                                          )
                                     ));
                          },
                          index -> this

                   );

    }

    @Override
    public JsObj set(final JsPath path,
                     final JsValue element) {
        return ifNothingElse(() -> this.delete(path),
                             e -> set(path,
                                      e,
                                      NULL
                             )).apply(requireNonNull(element));
    }

    @Override
    public <R> Optional<R> reduce(final BinaryOperator<R> op,
                                  final Function<? super JsPrimitive, R> map,
                                  final Predicate<? super JsPrimitive> predicate) {
        return OpMapReduce.reduceObj(this,
                                     requireNonNull(predicate),
                                     map,
                                     op,
                                     Optional.empty()
        );
    }

    @Override
    public <R> Optional<R> reduceAll(final BinaryOperator<R> op,
                                     final BiFunction<? super JsPath, ? super JsPrimitive, R> map,
                                     final BiPredicate<? super JsPath, ? super JsPrimitive> predicate
    ) {
        return OpMapReduce.reduceAllObj(this,
                                        JsPath.empty(),
                                        requireNonNull(predicate),
                                        map,
                                        op,
                                        Optional.empty()
        );

    }

    @Override
    public <R> Optional<R> reduceAll(final BinaryOperator<R> op,
                                     final Function<? super JsPrimitive, R> map,
                                     final Predicate<? super JsPrimitive> predicate) {
        return OpMapReduce.reduceAllObj(this,
                                        requireNonNull(predicate),
                                        map,
                                        op,
                                        Optional.empty()
        );
    }

    @Override
    public JsObj delete(final JsPath path) {
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(key ->
                          {
                              if (!map.containsKey(key)) return this;
                              final JsPath tail = path.tail();
                              return tail.isEmpty() ?
                                     new JsObj(map.remove(key)) :
                                     MatchExp.ifJsonElse(json -> new JsObj(map.put(key,
                                                                                   json.delete(tail)
                                                         )),
                                                         e -> this
                                             )
                                             .apply(map.get(key)
                                                       .get());
                          },
                          index -> this
                   );


    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Stream<Pair<JsPath, JsValue>> streamAll() {
        return streamOfObj(this,
                           JsPath.empty()
        );
    }

    @Override
    public Stream<JsValue> streamValues() {
        return map.values().toJavaStream();
    }


    @Override
    public Stream<Pair<JsPath, JsValue>> stream() {
        return this.keySet()
                   .stream()
                   .map(f ->
                        {
                            final JsPath key = JsPath.fromKey(f);
                            return new Pair<>(key,
                                              this.get(key)
                            );
                        }

                   );
    }

    public JsObj filterValues(final BiPredicate<? super String, ? super JsPrimitive> filter) {
        return OpFilterObjElems.filter(this,
                                       requireNonNull(filter)
        );
    }

    public JsObj filterKeys(final BiPredicate<? super String, ? super JsValue> filter) {
        return OpFilterObjKeys.filter(this,
                                      filter
        );

    }

    public JsObj filterObjs(final BiPredicate<? super String, ? super JsObj> filter) {
        return OpFilterObjObjs.filter(this,
                                      requireNonNull(filter)
        );
    }

    public JsObj mapValues(final BiFunction<? super String, ? super JsPrimitive, ? extends JsValue> fn) {
        return OpMapObjElems.map(this,
                                 requireNonNull(fn)
        );
    }

    public JsObj mapKeys(final BiFunction<? super String, ? super JsValue, String> fn) {
        return OpMapObjKeys.map(this,
                                requireNonNull(fn)
        );
    }

    public JsObj mapObjs(final BiFunction<? super String, ? super JsObj, JsValue> fn) {
        return OpMapObjObjs.map(this,
                                requireNonNull(fn)
        );
    }

    /**
     * Performs a reduction on the values that satisfy the predicate in the first level of this json. The reduction is performed mapping
     * each value with the mapping function and then applying the operator
     *
     * @param op        the operator upon two objects of type R
     * @param map       the mapping function which produces an object of type R from a JsValue
     * @param predicate the predicate that determines what JsValue will be mapped and reduced
     * @param <R>       the type of the operands of the operator
     * @return an {@link Optional} describing the of of the reduction
     * @see #reduceAll(BinaryOperator, BiFunction, BiPredicate) to apply the reduction in all the Json and not only in the first level
     */
    public <R> Optional<R> reduce(final BinaryOperator<R> op,
                                  final BiFunction<? super String, ? super JsPrimitive, R> map,
                                  final BiPredicate<? super String, ? super JsPrimitive> predicate
    ) {
        return OpMapReduce.reduceObj(this,
                                     requireNonNull(predicate),
                                     map,
                                     op
        );
    }

    /**
     * return true if this obj is equal to the given as a parameter. In the case of ARRAY_AS=LIST, this
     * method is equivalent to JsObj.equals(Object).
     *
     * @param that     the given array
     * @param ARRAY_AS enum to specify if arrays are considered as lists or sets or multisets
     * @return true if both objs are equals
     */
    @SuppressWarnings("squid:S00117")
    public boolean equals(final JsObj that,
                          final TYPE ARRAY_AS
    ) {
        if (isEmpty()) return that.isEmpty();
        if (that.isEmpty()) return isEmpty();
        return keySet().stream()
                       .allMatch(field ->
                                 {
                                     final boolean exists = that.containsKey(field);
                                     if (!exists) return false;
                                     final JsValue elem = get(field);
                                     final JsValue thatElem = that.get(field);
                                     if (elem.isJson() && thatElem.isJson())
                                         return elem.toJson()
                                                    .equals(thatElem,
                                                            ARRAY_AS
                                                    );
                                     return elem.equals(thatElem);
                                 }) && that.keySet()
                                           .stream()
                                           .allMatch(this::containsKey);
    }

    /**
     * return true if the key is present
     *
     * @param key the key
     * @return true if the specified key exists
     */
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public JsValue get(final String key) {
        return map.getOrElse(requireNonNull(key),
                             NOTHING
        );
    }

    /**
     * Returns the array located at the given key or null if it doesn't exist or it's not an array.
     *
     * @param key the key
     * @return the JsArray located at the given key or null
     */
    public JsArray getArray(final String key) {
        return JsArray.prism.getOptional.apply(get(requireNonNull(key)))
                                        .orElse(null);

    }

    /**
     * Returns the array located at the given key or the default value provided if it
     * doesn't exist or it's not an array.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the JsArray located at the given key or the default value provided
     */
    public JsArray getArray(final String key,
                            final Supplier<JsArray> orElse) {
        return JsArray.prism.getOptional.apply(get(requireNonNull(key)))
                                        .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the number located at the given key as a big decimal or null if it doesn't exist or it's
     * not a decimal number.
     *
     * @param key the key
     * @return the BigDecimal located at the given key or null
     */
    public BigDecimal getBigDec(final String key) {
        return JsBigDec.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);


    }

    /**
     * Returns the number located at the given key as a big decimal or the default value provided
     * if it doesn't exist or it's not a decimal number.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the BigDecimal located at the given key or the default value provided
     */
    public BigDecimal getBigDec(final String key,
                                final Supplier<BigDecimal> orElse) {
        return JsBigDec.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElseGet(requireNonNull(orElse));


    }

    /**
     * Returns the bytes located at the given key  or null if it doesn't exist or it's
     * not an array of bytes.
     *
     * @param key the key
     * @return the bytes located at the given key or null
     */
    public byte[] getBinary(final String key) {
        return JsBinary.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);


    }

    /**
     * Returns the bytes located at the given key  or the default value provided if it doesn't exist
     * or it's not an array of bytes.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the bytes located at the given key or the default value provided
     */
    public byte[] getBinary(final String key,
                            final Supplier<byte[]> orElse) {
        return JsBinary.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElseGet(requireNonNull(orElse));


    }

    /**
     * Returns the big integer located at the given key as a big integer or null if it doesn't
     * exist or it's not an integral number.
     *
     * @param key the key
     * @return the BigInteger located at the given key or null
     */
    public BigInteger getBigInt(final String key) {
        return JsBigInt.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);

    }

    /**
     * Returns the big integer located at the given key as a big integer or the default value provided
     * if it doesn't exist or it's not an integral number.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the BigInteger located at the given key or null
     */
    public BigInteger getBigInt(final String key,
                                final Supplier<BigInteger> orElse) {
        return JsBigInt.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the instant located at the given key or null if it doesn't exist or it's
     * not an instant.
     *
     * @param key the key
     * @return the instant located at the given key or null
     */
    public Instant getInstant(final String key) {
        return JsInstant.prism.getOptional.apply(get(requireNonNull(key)))
                                          .orElse(null);


    }

    /**
     * Returns the instant located at the given key or the default value provided if it doesn't
     * exist or it's not an instant.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the instant located at the given key or null
     */
    public Instant getInstant(final String key,
                              final Supplier<Instant> orElse) {
        return JsInstant.prism.getOptional.apply(get(requireNonNull(key)))
                                          .orElseGet(requireNonNull(orElse));


    }

    /**
     * Returns the boolean located at the given key or null if it doesn't exist.
     *
     * @param key the key
     * @return the Boolean located at the given key or null
     */
    public Boolean getBool(final String key) {
        return JsBool.prism.getOptional.apply(get(requireNonNull(key)))
                                       .orElse(null);

    }

    /**
     * Returns the boolean located at the given key or the default value provided if it doesn't exist.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the Boolean located at the given key or null
     */
    public Boolean getBool(final String key,
                           final Supplier<Boolean> orElse) {
        return JsBool.prism.getOptional.apply(get(requireNonNull(key)))
                                       .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the number located at the given key as a double or null if it
     * doesn't exist or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
     * to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
     * the precision of the BigDecimal
     *
     * @param key the key
     * @return the decimal number located at the given key or null
     */
    public Double getDouble(final String key) {
        return JsDouble.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);

    }

    /**
     * Returns the number located at the given key as a double or the default value provided if it
     * doesn't exist or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
     * to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
     * the precision of the BigDecimal
     *
     * @param key    the key
     * @param orElse the default value
     * @return the decimal number located at the given key or null
     */
    public Double getDouble(final String key,
                            final Supplier<Double> orElse) {
        return JsDouble.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the integral number located at the given key as an integer or null if it
     * doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an integer.
     *
     * @param key the key
     * @return the integral number located at the given key or null
     */
    public Integer getInt(final String key) {
        return JsInt.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElse(null);

    }

    /**
     * Returns the integral number located at the given key as an integer or the default value provided if it
     * doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an integer.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the integral number located at the given key or null
     */
    public Integer getInt(final String key,
                          final Supplier<Integer> orElse) {
        return JsInt.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the integral number located at the given key as a long or null if it
     * doesn't exist or it's not an integral number or it's an integral number but doesn't fit in a long.
     *
     * @param key the key
     * @return the integral number located at the given key or null
     */
    public Long getLong(final String key) {
        return JsLong.prism.getOptional.apply(get(requireNonNull(key)))
                                       .orElse(null);

    }

    /**
     * Returns the integral number located at the given key as a long or the default value provided
     * if it doesn't exist or it's not an integral number or it's an integral number but doesn't fit
     * in a long.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the integral number located at the given key or the default value provided
     */
    public Long getLong(final String key,
                        final Supplier<Long> orElse) {
        return JsLong.prism.getOptional.apply(get(requireNonNull(key)))
                                       .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the json object located at the given key or null if it doesn't exist or it's not an object.
     *
     * @param key the key
     * @return the json object located at the given key or null
     */
    public JsObj getObj(final String key) {
        return JsObj.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElse(null);

    }

    /**
     * Returns the json object located at the given key or the default value provided
     * if it doesn't exist or it's not an object.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the json object located at the given key or the default value
     */
    public JsObj getObj(final String key,
                        final Supplier<JsObj> orElse) {
        return JsObj.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElseGet(requireNonNull(orElse));

    }

    /**
     * Returns the string located at the given key or null if it doesn't exist or it's not an string.
     *
     * @param key the key
     * @return the string located at the given key or null
     */
    public String getStr(final String key) {
        return JsStr.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElse(null);
    }

    /**
     * Returns the string located at the given key or the default value provided if it doesn't
     * exist or it's not an string.
     *
     * @param key    the key
     * @param orElse the default value
     * @return the string located at the given key or null
     */
    public String getStr(final String key,
                         final Supplier<String> orElse) {
        return JsStr.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElseGet(requireNonNull(orElse));
    }

    /**
     * equals method is inherited, so it's implemented. The purpose of this method is to cache
     * the hashcode once calculated. the object is immutable and it won't change
     * Single-check idiom  Item 83 from Effective Java
     */
    @Override
    @SuppressWarnings("squid:S1206")
    public int hashCode() {
        int result = hascode;
        if (result == 0)
            hascode = result = map.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object that) {
        if (!(that instanceof JsObj)) return false;
        if (this == that) return true;
        final JsObj thatMap = (JsObj) that;
        if (isEmpty()) return thatMap.isEmpty();

        return keySet().stream()
                       .allMatch(f -> thatMap.map.get(f)
                                                 .map(it -> it.equals(map.get(f)
                                                                         .get())
                                                 )
                                                 .getOrElse(false) && thatMap.keySet()
                                                                             .stream()
                                                                             .allMatch(map::containsKey));
    }

    /**
     * // Single-check idiom Item 83 from effective java
     */
    @Override
    public String toString() {
        String result = str;
        if (result == null)
            str = result = new String(MyDslJson.INSTANCE.serialize(this),
                                      StandardCharsets.UTF_8);
        return result;
    }

    @Override
    public int id() {
        return TYPE_ID;
    }

    @Override
    public boolean isObj() {
        return true;
    }

    /**
     * Returns a pair with an arbitrary key of this object and its associated element. When using head
     * and tail to process a JsObj, the key of the pair returned must be passed in to get the tail using
     * the method {@link #tail()}.
     *
     * @return an arbitrary {@code Map.Entry<String,JsValue>} of this JsObj
     * @throws UserError if this json object is empty
     */
    public Tuple2<String, JsValue> head() {
        return map.head();
    }

    /**
     * Returns a new object with all the entries of this json object except the one with the given key.
     *
     * @return a new JsObj
     * @throws UserError if this json object is empty
     */
    public JsObj tail() {
        return new JsObj(map.tail());
    }

    /**
     * {@code this.intersection(that, SET)} returns an array with the elements that exist in both {@code this} and {@code that}
     * {@code this.intersection(that, MULTISET)} returns an array with the elements that exist in both {@code this} and {@code that},
     * being duplicates allowed.
     * {@code this.intersection(that, LIST)} returns an array with the elements that exist in both {@code this} and {@code that},
     * and are located at the same position.
     *
     * @param that     the other obj
     * @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     * @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S00117")
    //  ARRAY_AS  should be a valid name
    public JsObj intersection(final JsObj that,
                              final TYPE ARRAY_AS
    ) {
        requireNonNull(that);
        return intersection(this,
                            that,
                            ARRAY_AS
        );
    }

    /**
     * {@code this.intersectionAll(that)} behaves as {@code this.intersection(that, LIST)}, but for those elements
     * that are containers of the same type and are located at the same position, the result is their
     * intersection.  So this operation is kind of a 'recursive' intersection.
     *
     * @param that     the other object
     * @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     * @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    // squid:S00117 ARRAY_AS should be a valid name
    @SuppressWarnings({"squid:S00117"})
    public JsObj intersectionAll(final JsObj that,
                                 final TYPE ARRAY_AS
    ) {

        return intersectionAll(this,
                               requireNonNull(that),
                               requireNonNull(ARRAY_AS)
        );
    }

    @SuppressWarnings("squid:S1602")
    private
        // curly braces makes IntelliJ to format the code in a more legible way
    BiPredicate<String, JsPath> isReplaceWithEmptyJson(final Map<String, JsValue> pmap) {
        return (head, tail) ->
                (!pmap.containsKey(head) || !pmap.get(head)
                                                 .filter(JsValue::isPrimitive)
                                                 .isEmpty())
                        ||
                        (
                                tail.head()
                                    .isKey() && !pmap.get(head)
                                                     .filter(JsValue::isArray)
                                                     .isEmpty()
                        )
                        ||
                        (tail.head()
                             .isIndex() && !pmap.get(head)
                                                .filter(JsValue::isObj)
                                                .isEmpty());
    }

    @Override
    public Iterator<Tuple2<String, JsValue>> iterator() {
        return map.iterator();
    }

    /**
     * returns {@code this} json object plus those pairs from the given json object {@code that} which
     * keys don't exist in {@code this}. Taking that into account, it's not a commutative operation unless
     * the elements associated with the keys that exist in both json objects are equals.
     *
     * @param that the given json object
     * @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    public JsObj union(final JsObj that
    ) {
        return union(this,
                     requireNonNull(that)
        );

    }

    /**
     * behaves like the {@link JsObj#union(JsObj)} but, for those keys that exit in both {@code this}
     * and {@code that} json objects,
     * which associated elements are **containers of the same type**, the result is their union. In this
     * case, we can specify if arrays are considered Sets, Lists, or MultiSets. So this operation is kind of a
     * 'recursive' union.
     *
     * @param that     the given json object
     * @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     * @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S3008")//ARRAY_AS should be a valid name
    public JsObj unionAll(final JsObj that,
                          final TYPE ARRAY_AS
    ) {
        requireNonNull(that);
        requireNonNull(ARRAY_AS);
        return ifEmptyElse(() -> that,
                           () -> that.ifEmptyElse(() -> this,
                                                  () -> unionAll(this,
                                                                 that,
                                                                 ARRAY_AS
                                                  )
                           )
        );

    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    private JsObj intersection(final JsObj a,
                               final JsObj b,
                               final JsArray.TYPE ARRAY_AS
    ) {
        if (a.isEmpty()) return a;
        if (b.isEmpty()) return b;

        JsObj result = JsObj.empty();
        for (final Tuple2<String, JsValue> head : a) {
            final JsValue bElem = b.get(head._1);
            if ((bElem.isJson() && bElem.toJson()
                                        .equals(head._2,
                                                ARRAY_AS
                                        )) || bElem.equals(head._2))
                result = result.set(head._1,
                                    head._2
                );
        }
        return result;
    }

    @SuppressWarnings({"squid:S00117",}) // ARRAY_AS should be a valid name for an enum constant
    private JsObj intersectionAll(final JsObj a,
                                  final JsObj b,
                                  final JsArray.TYPE ARRAY_AS
    ) {
        if (a.isEmpty()) return a;
        if (b.isEmpty()) return b;
        JsObj result = JsObj.empty();
        for (final Tuple2<String, JsValue> aVal : a) {

            if (b.containsKey(aVal._1)) {
                final JsValue bVal = b.get(aVal._1);

                if (bVal.equals(aVal._2)) result = result.set(aVal._1,
                                                              aVal._2);
                else if (bVal.isJson() && bVal.isSameType(aVal._2)) {
                    result = result.set(aVal._1,
                                        OpIntersectionJsons.intersectionAll(aVal._2.toJson(),
                                                                            bVal.toJson(),
                                                                            ARRAY_AS
                                        )
                    );
                }
            }

        }

        return result;

    }

    private JsObj union(JsObj a,
                        JsObj b
    ) {
        if (b.isEmpty()) return a;
        JsObj result = a;
        for (final Tuple2<String, JsValue> head : b) {
            if (!a.containsKey(head._1)) result = result.set(head._1,
                                                             head._2
            );
        }

        return result;

    }

    //squid:S00117 ARRAY_AS should be a valid name
    private JsObj unionAll(final JsObj a,
                           final JsObj b,
                           final JsArray.TYPE ARRAY_AS
    ) {

        if (b.isEmpty()) return a;
        JsObj result = a;
        for (final Tuple2<String, JsValue> bVal : b) {
            if (!a.containsKey(bVal._1))
                result = result.set(bVal._1,
                                    bVal._2
                );
            JsValue aVal = a.get(bVal._1);
            if (aVal.isJson() && aVal.isSameType(bVal._2)) {
                Json<?> aJson = aVal.toJson();
                Json<?> bJson = bVal._2.toJson();

                result = result.set(bVal._1,
                                    OpUnionJsons.unionAll(aJson,
                                                          bJson,
                                                          ARRAY_AS
                                    )
                );
            }
        }

        return result;

    }


}

