package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import io.vavr.Tuple2;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import jsonvalues.JsArray.TYPE;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Stream;
import static com.dslplatform.json.MyDslJson.INSTANCE;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static java.util.Objects.requireNonNull;
import static jsonvalues.JsArray.streamOfArr;
import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNothing.NOTHING;
import static jsonvalues.JsNull.NULL;
import static jsonvalues.MatchExp.ifNothingElse;
import static jsonvalues.Trampoline.done;
import static jsonvalues.Trampoline.more;

/**
 Represents a json object, which is an unordered set of name/element pairs. Two implementations are
 provided, an immutable which uses the persistent Scala HashMap, and a mutable which uses the conventional
 Java HashMap.
 */
public class JsObj implements Json<JsObj>, Iterable<Tuple2<String, JsValue>> {
    public static final JsObj EMPTY = new JsObj(LinkedHashMap.empty());
    /**
     lenses defined for a Json object
     */
    public static final JsOptics.JsObjLenses lens = JsOptics.obj.lens;
    /**
     optionals defined for a Json object
     */
    public static final JsOptics.JsObjOptional optional = JsOptics.obj.optional;
    /**
     prism between the sum type JsValue and JsObj
     */
    public static final Prism<JsValue, JsObj> prism =
            new Prism<>(s -> s.isObj() ? Optional.of(s.toJsObj()) : Optional.empty(),
                        o -> o
            );
    @SuppressWarnings("squid:S3008")//EMPTY_PATH should be a valid name
    private static final JsPath EMPTY_PATH = JsPath.empty();
    public static final int TYPE_ID = 3;
    private final Map<String, JsValue> map;
    private volatile int hascode;
    //squid:S3077: doesn't make any sense, volatile is perfectly valid here an as a matter of fact
    //is a recomendation from Efective Java to apply the idiom single check for lazy initialization
    @SuppressWarnings("squid:S3077")

    private volatile String str;

    public JsObj(){
        this.map = LinkedHashMap.empty();
    }
    JsObj(final Map<String, JsValue> myMap) {
        this.map = myMap;
    }

    public static JsObj empty() {
        return EMPTY;
    }

    /**
     Returns a one-element immutable object.

     @param key name of a key
     @param el  JsValue to be associated to the key
     @return an immutable one-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a two-element immutable object.

     @param key1 name of a key
     @param el1  JsValue to be associated to the key1
     @param key2 name of a key
     @param el2  JsValue to be associated to the key2
     @return an immutable two-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a three-element immutable object.

     @param key1 name of a key
     @param el1  JsValue to be associated to the key1
     @param key2 name of a key
     @param el2  JsValue to be associated to the key2
     @param key3 name of a key
     @param el3  JsValue to be associated to the key3
     @return an immutable three-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a four-element immutable object.

     @param key1 name of a key
     @param el1  JsValue to be associated to the key1
     @param key2 name of a key
     @param el2  JsValue to be associated to the key2
     @param key3 name of a key
     @param el3  JsValue to be associated to the key3
     @param key4 name of a key
     @param el4  JsValue to be associated to the key4
     @return an immutable four-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a five-element immutable object.

     @param key1 name of a key
     @param el1  JsValue to be associated to the key1
     @param key2 name of a key
     @param el2  JsValue to be associated to the key2
     @param key3 name of a key
     @param el3  JsValue to be associated to the key3
     @param key4 name of a key
     @param el4  JsValue to be associated to the key4
     @param key5 name of a key
     @param el5  JsValue to be associated to the key5
     @return an immutable five-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a six-element immutable object.

     @param key1 name of a key
     @param el1  JsValue to be associated to the key1
     @param key2 name of a key
     @param el2  JsValue to be associated to the key2
     @param key3 name of a key
     @param el3  JsValue to be associated to the key3
     @param key4 name of a key
     @param el4  JsValue to be associated to the key4
     @param key5 name of a key
     @param el5  JsValue to be associated to the key5
     @param key6 name of a key
     @param el6  JsValue to be associated to the key6
     @return an immutable six-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a six-element immutable object.

     @param key1 name of a key
     @param el1  JsValue to be associated to the key1
     @param key2 name of a key
     @param el2  JsValue to be associated to the key2
     @param key3 name of a key
     @param el3  JsValue to be associated to the key3
     @param key4 name of a key
     @param el4  JsValue to be associated to the key4
     @param key5 name of a key
     @param el5  JsValue to be associated to the key5
     @param key6 name of a key
     @param el6  JsValue to be associated to the key6
     @param key7 name of a key
     @param el7  JsValue to be associated to the key7
     @return an immutable seven-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a six-element immutable object.

     @param key1 name of a key
     @param el1  JsValue to be associated to the key1
     @param key2 name of a key
     @param el2  JsValue to be associated to the key2
     @param key3 name of a key
     @param el3  JsValue to be associated to the key3
     @param key4 name of a key
     @param el4  JsValue to be associated to the key4
     @param key5 name of a key
     @param el5  JsValue to be associated to the key5
     @param key6 name of a key
     @param el6  JsValue to be associated to the key6
     @param key7 name of a key
     @param el7  JsValue to be associated to the key7
     @param key8 name of a key
     @param el8  JsValue to be associated to the key8
     @return an immutable eight-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a six-element immutable object.

     @param key1 name of a key
     @param el1  JsValue to be associated to the key1
     @param key2 name of a key
     @param el2  JsValue to be associated to the key2
     @param key3 name of a key
     @param el3  JsValue to be associated to the key3
     @param key4 name of a key
     @param el4  JsValue to be associated to the key4
     @param key5 name of a key
     @param el5  JsValue to be associated to the key5
     @param key6 name of a key
     @param el6  JsValue to be associated to the key6
     @param key7 name of a key
     @param el7  JsValue to be associated to the key7
     @param key8 name of a key
     @param el8  JsValue to be associated to the key8
     @param key9 name of a key
     @param el9  JsValue to be associated to the key9
     @return an immutable nine-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a six-element immutable object.

     @param key1  name of a key
     @param el1   JsValue to be associated to the key1
     @param key2  name of a key
     @param el2   JsValue to be associated to the key2
     @param key3  name of a key
     @param el3   JsValue to be associated to the key3
     @param key4  name of a key
     @param el4   JsValue to be associated to the key4
     @param key5  name of a key
     @param el5   JsValue to be associated to the key5
     @param key6  name of a key
     @param el6   JsValue to be associated to the key6
     @param key7  name of a key
     @param el7   JsValue to be associated to the key7
     @param key8  name of a key
     @param el8   JsValue to be associated to the key8
     @param key9  name of a key
     @param el9   JsValue to be associated to the key9
     @param key10 name of a key
     @param el10  JsValue to be associated to the key10
     @return an immutable ten-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a six-element immutable object.

     @param key1  name of a key
     @param el1   JsValue to be associated to the key1
     @param key2  name of a key
     @param el2   JsValue to be associated to the key2
     @param key3  name of a key
     @param el3   JsValue to be associated to the key3
     @param key4  name of a key
     @param el4   JsValue to be associated to the key4
     @param key5  name of a key
     @param el5   JsValue to be associated to the key5
     @param key6  name of a key
     @param el6   JsValue to be associated to the key6
     @param key7  name of a key
     @param el7   JsValue to be associated to the key7
     @param key8  name of a key
     @param el8   JsValue to be associated to the key8
     @param key9  name of a key
     @param el9   JsValue to be associated to the key9
     @param key10 name of a key
     @param el10  JsValue to be associated to the key10
     @param key11 name of a key
     @param el11  JsValue to be associated to the key11
     @return an immutable eleven-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a six-element immutable object.

     @param key1  name of a key
     @param el1   JsValue to be associated to the key1
     @param key2  name of a key
     @param el2   JsValue to be associated to the key2
     @param key3  name of a key
     @param el3   JsValue to be associated to the key3
     @param key4  name of a key
     @param el4   JsValue to be associated to the key4
     @param key5  name of a key
     @param el5   JsValue to be associated to the key5
     @param key6  name of a key
     @param el6   JsValue to be associated to the key6
     @param key7  name of a key
     @param el7   JsValue to be associated to the key7
     @param key8  name of a key
     @param el8   JsValue to be associated to the key8
     @param key9  name of a key
     @param el9   JsValue to be associated to the key9
     @param key10 name of a key
     @param el10  JsValue to be associated to the key10
     @param key11 name of a key
     @param el11  JsValue to be associated to the key11
     @param key12 name of a key
     @param el12  JsValue to be associated to the key12
     @return an immutable twelve-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a six-element immutable object.

     @param key1  name of a key
     @param el1   JsValue to be associated to the key1
     @param key2  name of a key
     @param el2   JsValue to be associated to the key2
     @param key3  name of a key
     @param el3   JsValue to be associated to the key3
     @param key4  name of a key
     @param el4   JsValue to be associated to the key4
     @param key5  name of a key
     @param el5   JsValue to be associated to the key5
     @param key6  name of a key
     @param el6   JsValue to be associated to the key6
     @param key7  name of a key
     @param el7   JsValue to be associated to the key7
     @param key8  name of a key
     @param el8   JsValue to be associated to the key8
     @param key9  name of a key
     @param el9   JsValue to be associated to the key9
     @param key10 name of a key
     @param el10  JsValue to be associated to the key10
     @param key11 name of a key
     @param el11  JsValue to be associated to the key11
     @param key12 name of a key
     @param el12  JsValue to be associated to the key12
     @param key13 name of a key
     @param el13  JsValue to be associated to the key13
     @return an immutable twelve-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a six-element immutable object.

     @param key1  name of a key
     @param el1   JsValue to be associated to the key1
     @param key2  name of a key
     @param el2   JsValue to be associated to the key2
     @param key3  name of a key
     @param el3   JsValue to be associated to the key3
     @param key4  name of a key
     @param el4   JsValue to be associated to the key4
     @param key5  name of a key
     @param el5   JsValue to be associated to the key5
     @param key6  name of a key
     @param el6   JsValue to be associated to the key6
     @param key7  name of a key
     @param el7   JsValue to be associated to the key7
     @param key8  name of a key
     @param el8   JsValue to be associated to the key8
     @param key9  name of a key
     @param el9   JsValue to be associated to the key9
     @param key10 name of a key
     @param el10  JsValue to be associated to the key10
     @param key11 name of a key
     @param el11  JsValue to be associated to the key11
     @param key12 name of a key
     @param el12  JsValue to be associated to the key12
     @param key13 name of a key
     @param el13  JsValue to be associated to the key13
     @param key14 name of a key
     @param el14  JsValue to be associated to the key14
     @return an immutable twelve-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns a six-element immutable object.

     @param key1  name of a key
     @param el1   JsValue to be associated to the key1
     @param key2  name of a key
     @param el2   JsValue to be associated to the key2
     @param key3  name of a key
     @param el3   JsValue to be associated to the key3
     @param key4  name of a key
     @param el4   JsValue to be associated to the key4
     @param key5  name of a key
     @param el5   JsValue to be associated to the key5
     @param key6  name of a key
     @param el6   JsValue to be associated to the key6
     @param key7  name of a key
     @param el7   JsValue to be associated to the key7
     @param key8  name of a key
     @param el8   JsValue to be associated to the key8
     @param key9  name of a key
     @param el9   JsValue to be associated to the key9
     @param key10 name of a key
     @param el10  JsValue to be associated to the key10
     @param key11 name of a key
     @param el11  JsValue to be associated to the key11
     @param key12 name of a key
     @param el12  JsValue to be associated to the key12
     @param key13 name of a key
     @param el13  JsValue to be associated to the key13
     @param key14 name of a key
     @param el14  JsValue to be associated to the key14
     @param key15 name of a key
     @param el15  JsValue to be associated to the key15
     @return an immutable twelve-element JsObj
     @throws UserError if an elem is a mutable Json
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
     Returns an immutable object from one or more pairs.

     @param pair   a pair
     @param others more optional pairs
     @return an immutable JsObject
     @throws UserError if an elem of a pair is mutable
     */
    public static JsObj of(final JsPair pair,
                           final JsPair... others
                          ) {
        JsObj obj = JsObj.EMPTY.set(pair.path,
                                    pair.value
                                   );
        for (JsPair p : others) {

            obj = obj.set(p.path,
                          p.value
                         );
        }
        return obj;

    }

    public static JsObj ofIterable(final Iterable<java.util.Map.Entry<String, JsValue>> xs) {
        JsObj acc = JsObj.EMPTY;
        for (java.util.Map.Entry<String, JsValue> x : requireNonNull(xs)) {

            acc = acc.set(JsPath.fromKey(x.getKey()),
                          x.getValue()
                         );
        }
        return acc;
    }

    /**
     Tries to parse the string into an immutable object.

     @param str the string to be parsed
     @return a JsOb object
     @throws MalformedJson if the string doesnt represent a json object
     */
    public static JsObj parse(final String str){

        try (JsonParser parser = JacksonFactory.INSTANCE.createParser(requireNonNull(str))) {
            JsonToken keyEvent = parser.nextToken();
            if (START_OBJECT != keyEvent) throw MalformedJson.expectedObj(str);
            return new JsObj(JsObj.parse(parser));
        } catch (IOException e) {
            throw new MalformedJson(e.getMessage());
        }
    }

    static Map<String, JsValue> parse(final JsonParser parser) throws IOException {
        Map<String, JsValue> map = LinkedHashMap.empty();
        String                   key = parser.nextFieldName();
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
                    throw InternalError.tokenNotExpected(parser.currentToken()
                                                               .name());
            }
            map = map.put(key,
                          elem
                         );
        }

        return map;

    }

    static Stream<JsPair> streamOfObj(final JsObj obj,
                                      final JsPath path
                                     ) {

        requireNonNull(path);
        return requireNonNull(obj).ifEmptyElse(() -> Stream.of(JsPair.of(path,
                                                                         obj
                                                                        )),
                                               () -> obj.keySet()
                                                        .stream()
                                                        .map(key -> JsPair.of(path.key(key),
                                                                              get(obj,
                                                                                  Key.of(key)
                                                                                 )
                                                                             ))
                                                        .flatMap(pair -> MatchExp.ifJsonElse(o -> streamOfObj(o,
                                                                                                              pair.path
                                                                                                             ),
                                                                                             a -> streamOfArr(a,
                                                                                                              pair.path
                                                                                                             ),
                                                                                             e -> Stream.of(pair)
                                                                                            )
                                                                                 .apply(pair.value))
                                              );

    }

    /**
     return true if the key is present

     @param key the key
     @return true if the specified key exists
     */
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public final boolean containsValue(final JsValue el) {
        return stream().anyMatch(p -> p.value.equals(Objects.requireNonNull(el)));
    }

    /**
     Returns a set containing each key fo this object.

     @return a Set containing each key of this JsObj
     */
    public final Set<String> keySet() {
        return map.keySet()
                  .toJavaSet();
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

    @Override
    public JsValue get(final JsPath path) {
        if (path.isEmpty()) return this;
        final JsValue e = get(this,
                              path.head()
                             );
        final JsPath tail = path.tail();
        if (tail.isEmpty()) return e;
        if (e.isNotJson()) return NOTHING;
        return e.toJson()
                .get(tail);
    }

    public final JsObj filterValues(final Predicate<? super JsPair> filter) {
        return new OpFilterObjElems(this).filter(JsPath.empty(),
                                                 requireNonNull(filter)
                                                )

                                         .get();
    }

    public final JsObj filterAllValues(final Predicate<? super JsPair> filter) {
        return new OpFilterObjElems(this).filterAll(JsPath.empty(),
                                                    requireNonNull(filter)
                                                   )

                                         .get();

    }

    public final JsObj filterKeys(final Predicate<? super JsPair> filter) {
        return new OpFilterObjKeys(this).filter(filter)
                                        .get();

    }

    public JsObj filterAllKeys(final Predicate<? super JsPair> filter) {
        return new OpFilterObjKeys(this).filterAll(JsPath.empty(),
                                                   filter
                                                  )
                                        .get();
    }

    public final JsObj filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter) {
        return new OpFilterObjObjs(this).filter(JsPath.empty(),
                                                requireNonNull(filter)
                                               )

                                        .get();
    }

    public final JsObj filterAllObjs(final BiPredicate<? super JsPath, ? super JsObj> filter) {
        return new OpFilterObjObjs(this).filterAll(JsPath.empty(),
                                                   requireNonNull(filter)
                                                  )

                                        .get();

    }

    public final boolean isEmpty() {
        return map.isEmpty();
    }

    public final JsObj mapValues(final Function<? super JsPair, ? extends JsValue> fn) {
        return new OpMapObjElems(this).map(requireNonNull(fn),
                                           EMPTY_PATH
                                          )
                                      .get();
    }

    public final JsObj mapAllValues(final Function<? super JsPair, ? extends JsValue> fn) {
        return new OpMapObjElems(this).mapAll(requireNonNull(fn),
                                              EMPTY_PATH
                                             )
                                      .get();
    }

    public final JsObj mapKeys(final Function<? super JsPair, String> fn) {
        return new OpMapObjKeys(this).map(requireNonNull(fn),
                                          EMPTY_PATH
                                         )
                                     .get();
    }

    public final JsObj mapAllKeys(final Function<? super JsPair, String> fn) {
        return new OpMapObjKeys(this).mapAll(requireNonNull(fn),
                                             EMPTY_PATH
                                            )
                                     .get();
    }

    public final JsObj mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn) {
        return new OpMapObjObjs(this).map(requireNonNull(fn),
                                          JsPath.empty()
                                         )
                                     .get();
    }

    public final JsObj mapAllObjs(final BiFunction<? super JsPath, ? super JsObj, JsValue> fn) {
        return new OpMapObjObjs(this).mapAll(requireNonNull(fn),
                                             JsPath.empty()
                                            )
                                     .get();
    }

    @Override
    public JsObj set(final JsPath path,
                     final JsValue element) {
        return set(path, element,
                   NULL);
    }

    public final JsObj set(final JsPath path,
                           final JsValue value,
                           final JsValue padElement

                          ) {
        requireNonNull(value);
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(head ->
                          {
                              final JsPath tail = path.tail();

                              return tail.isEmpty() ? ifNothingElse(() -> this,
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

    public final <R> Optional<R> reduce(final BinaryOperator<R> op,
                                        final Function<? super JsPair, R> map,
                                        final Predicate<? super JsPair> predicate
                                       ) {
        return new OpMapReduce<>(requireNonNull(predicate),
                                 map,
                                 op
        ).reduce(this);
    }

    public final <R> Optional<R> reduceAll(final BinaryOperator<R> op,
                                           final Function<? super JsPair, R> map,
                                           final Predicate<? super JsPair> predicate
                                          ) {
        return new OpMapReduce<>(requireNonNull(predicate),
                                 map,
                                 op
        ).reduceAll(this);

    }

    public final JsObj delete(final JsPath path) {
        if (requireNonNull(path).isEmpty()) return this;
        return path.head()
                   .match(key ->
                          {
                              if (!map.containsKey(key)) return this;
                              final JsPath tail = path.tail();
                              return tail.isEmpty() ? new JsObj(map.remove(key)) :
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

    public final int size() {
        return map.size();
    }

    public final Stream<JsPair> streamAll() {
        return streamOfObj(this,
                           JsPath.empty()
                          );
    }

    public final Stream<JsPair> stream() {
        return this.keySet()
                   .stream()
                   .map(f ->
                        {
                            final JsPath key = JsPath.fromKey(f);
                            return JsPair.of(key,
                                             this.get(key)
                                            );
                        }

                       );
    }

    /**
     return true if this obj is equal to the given as a parameter. In the case of ARRAY_AS=LIST, this
     method is equivalent to JsObj.equals(Object).

     @param that     the given array
     @param ARRAY_AS enum to specify if arrays are considered as lists or sets or multisets
     @return true if both objs are equals
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
                                     final boolean exists = that.containsPath(JsPath.fromKey(field));
                                     if (!exists) return false;
                                     final JsValue elem     = get(JsPath.fromKey(field));
                                     final JsValue thatElem = that.get(JsPath.fromKey(field));
                                     if (elem.isJson() && thatElem.isJson())
                                         return elem.toJson()
                                                    .equals(thatElem,
                                                            ARRAY_AS
                                                           );
                                     return elem.equals(thatElem);
                                 }) && that.keySet()
                                           .stream()
                                           .allMatch(f -> this.containsPath(JsPath.fromKey(f)));
    }

    /**
     Returns the array located at the given key or null if it doesn't exist or it's not an array.

     @param key the key
     @return the JsArray located at the given key or null
     */
    public JsArray getArray(final String key) {
        return JsArray.prism.getOptional.apply(get(requireNonNull(key)))
                                        .orElse(null);

    }

    public JsValue get(final String key) {
        return get(JsPath.fromKey(Objects.requireNonNull(key)));
    }

    /**
     Returns the number located at the given key as a big decimal or null if it doesn't exist or it's
     not a decimal number.

     @param key the key
     @return the BigDecimal located at the given key or null
     */
    public BigDecimal getBigDec(final String key) {
        return JsBigDec.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);


    }

    /**
     Returns the bytes located at the given key  or null if it doesn't exist or it's
     not an array of bytes.

     @param key the key
     @return the bytes located at the given key or null
     */
    public byte[] getBinary(final String key) {
        return JsBinary.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);


    }



    /**
     Returns the big integer located at the given key as a big integer or null if it doesn't
     exist or it's not an integral number.

     @param key the key
     @return the BigInteger located at the given key or null
     */
    public BigInteger getBigInt(final String key) {
        return JsBigInt.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);

    }


    /**
     Returns the instant located at the given key or null if it doesn't exist or it's
     not an instant.

     @param key the key
     @return the instant located at the given key or null
     */
    public Instant getInstant(final String key) {
        return JsInstant.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);


    }

    /**
     Returns the boolean located at the given key or null if it doesn't exist.

     @param key the key
     @return the Boolean located at the given key or null
     */
    public Boolean getBool(final String key) {
        return JsBool.prism.getOptional.apply(get(requireNonNull(key)))
                                       .orElse(null);

    }

    /**
     Returns the number located at the given key as a double or null if it
     doesn't exist or it's not a decimal number. If the number is a BigDecimal, the conversion is identical
     to the specified in {@link BigDecimal#doubleValue()} and in some cases it can lose information about
     the precision of the BigDecimal

     @param key the key
     @return the decimal number located at the given key or null
     */
    public Double getDouble(final String key) {
        return JsDouble.prism.getOptional.apply(get(requireNonNull(key)))
                                         .orElse(null);

    }

    /**
     Returns the integral number located at the given key as an integer or null if it
     doesn't exist or it's not an integral number or it's an integral number but doesn't fit in an integer.

     @param key the key
     @return the integral number located at the given key or null
     */
    public Integer getInt(final String key) {
        return JsInt.prism.getOptional.apply(get(requireNonNull(key)))
                                      .orElse(null);

    }

    /**
     Returns the integral number located at the given key as a long or null if it
     doesn't exist or it's not an integral number or it's an integral number but doesn't fit in a long.

     @param key the key
     @return the integral number located at the given key or null
     */
    public Long getLong(final String key) {
        return JsLong.prism.getOptional.apply(get(Objects.requireNonNull(key)))
                                       .orElse(null);

    }

    /**
     Returns the json object located at the given key or null if it doesn't exist or it's not an object.

     @param key the key
     @return the json object located at the given key or null
     */
    public JsObj getObj(final String key) {
        return JsObj.prism.getOptional.apply(get(key))
                                      .orElse(null);

    }

    /**
     Returns the string located at the given key or null if it doesn't exist or it's not an string.

     @param key the key
     @return the string located at the given key or null
     */
    public String getStr(final String key) {
        return JsStr.prism.getOptional.apply(get(key))
                                      .orElse(null);
    }

    /**
     equals method is inherited, so it's implemented. The purpose of this method is to cache
     the hashcode once calculated. the object is immutable and it won't change
     Single-check idiom  Item 83 from Effective Java
     */
    @SuppressWarnings("squid:S1206")

    public final int hashCode() {
        int result = hascode;
        if (result == 0)
            hascode = result = map.hashCode();
        return result;
    }

    public final boolean equals(final Object that) {
        if (!(that instanceof JsObj)) return false;
        if (this == that) return true;
        if (getClass() != that.getClass()) return false;
        final JsObj   thatMap   = (JsObj) that;
        final boolean thisEmpty = isEmpty();
        final boolean thatEmpty = thatMap.isEmpty();
        if (thisEmpty && thatEmpty) return true;
        if (thisEmpty != thatEmpty) return false;

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
     // Single-check idiom Item 83 from effective java
     */
    public final String toString() {
        String result = str;
        if (result == null)
            str = result = new String(INSTANCE.serialize(this));
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

    <T> Trampoline<T> ifEmptyElse(final Trampoline<T> empty,
                                  final BiFunction<Tuple2<String, JsValue>, JsObj, Trampoline<T>> fn
                                 ) {


        if (this.isEmpty()) return empty;

        final Tuple2<String, JsValue> head = this.head();

        final JsObj tail = this.tail();

        return fn.apply(head,
                        tail
                       );

    }

    /**
     Returns a pair with an arbitrary key of this object and its associated element. When using head
     and tail to process a JsObj, the key of the pair returned must be passed in to get the tail using
     the method {@link #tail()}.

     @return an arbitrary {@code Map.Entry<String,JsValue>} of this JsObj
     @throws UserError if this json object is empty
     */
    public final Tuple2<String, JsValue> head() {
        return map.head();
    }

    /**
     Returns a new object with all the entries of this json object except the one with the given key.

     @return a new JsObj
     @throws UserError if this json object is empty
     */
    public final JsObj tail() {
        return new JsObj(map.tail());
    }

    /**
     {@code this.intersection(that, SET)} returns an array with the elements that exist in both {@code this} and {@code that}
     {@code this.intersection(that, MULTISET)} returns an array with the elements that exist in both {@code this} and {@code that},
     being duplicates allowed.
     {@code this.intersection(that, LIST)} returns an array with the elements that exist in both {@code this} and {@code that},
     and are located at the same position.

     @param that     the other obj
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
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
                           ).get();
    }

    /**
     {@code this.intersectionAll(that)} behaves as {@code this.intersection(that, LIST)}, but for those elements
     that are containers of the same type and are located at the same position, the result is their
     intersection.  So this operation is kind of a 'recursive' intersection.

     @param that     the other object
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    // squid:S00117 ARRAY_AS should be a valid name
    @SuppressWarnings({"squid:S00117"})
    public JsObj intersectionAll(final JsObj that,
                                 final TYPE ARRAY_AS
                                ) {
        requireNonNull(that);
        requireNonNull(ARRAY_AS);
        return intersectionAll(this,
                               that,
                               ARRAY_AS
                              ).get();
    }

    @SuppressWarnings("squid:S1602")
    private
        // curly braces makes IntelliJ to format the code in a more legible way
    BiPredicate<String, JsPath> isReplaceWithEmptyJson(final Map<String, JsValue> pmap) {
        return (head, tail) ->
        {
            return (!pmap.containsKey(head) || !pmap.get(head)
                                                    .filter(JsValue::isNotJson)
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
        };
    }

    @Override
    public Iterator<Tuple2<String, JsValue>> iterator() {
        return map.iterator();
    }

    /**
     Inserts the element at the key in this json, replacing any existing element.

     @param key   the key
     @param value the element
     @return a new json object
     */
    public JsObj set(final String key,
                     final JsValue value
                    ) {
        return set(JsPath.fromKey(key),
                   value
                  );
    }

    public final JsObj delete(final String key) {
        return delete(JsPath.fromKey(requireNonNull(key)));
    }

    /**
     returns {@code this} json object plus those pairs from the given json object {@code that} which
     keys don't exist in {@code this}. Taking that into account, it's not a commutative operation unless
     the elements associated with the keys that exist in both json objects are equals.

     @param that the given json object
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    public final JsObj union(final JsObj that
                            ) {
        return union(this,
                     requireNonNull(that)
                    ).get();

    }

    /**
     behaves like the {@link JsObj#union(JsObj)} but, for those keys that exit in both {@code this}
     and {@code that} json objects,
     which associated elements are **containers of the same type**, the result is their union. In this
     case, we can specify if arrays are considered Sets, Lists, or MultiSets. So this operation is kind of a
     'recursive' union.

     @param that     the given json object
     @param ARRAY_AS option to define if arrays are considered SETS, LISTS OR MULTISET
     @return a new JsObj of the same type as the inputs (mutable or immutable)
     */
    @SuppressWarnings("squid:S3008")//ARRAY_AS should be a valid name
    public final JsObj unionAll(final JsObj that,
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
                                                          .get()
                                                 )
                          );

    }

    @SuppressWarnings("squid:S00117") //  ARRAY_AS is a perfectly fine name
    private Trampoline<JsObj> intersection(final JsObj a,
                                           final JsObj b,
                                           final JsArray.TYPE ARRAY_AS
                                          ) {
        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);
        Tuple2<String, JsValue> head = a.head();
        JsObj                   tail = a.tail();
        final Trampoline<Trampoline<JsObj>> tailCall = () -> intersection(tail,
                                                                          b,
                                                                          ARRAY_AS
                                                                         );
        final JsValue bElem = b.get(JsPath.fromKey(head._1));

        return ((bElem.isJson() && bElem.toJson()
                                        .equals(head._2,
                                                ARRAY_AS
                                               )) || bElem.equals(head._2)) ?
               more(tailCall).map(it -> it.set(JsPath.fromKey(head._1),
                                               head._2
                                              )) :
               more(tailCall);
    }

    @SuppressWarnings({"squid:S00117",}) // ARRAY_AS should be a valid name for an enum constant
    private Trampoline<JsObj> intersectionAll(final JsObj a,
                                              final JsObj b,
                                              final JsArray.TYPE ARRAY_AS
                                             ) {
        if (a.isEmpty()) return done(a);
        if (b.isEmpty()) return done(b);
        Tuple2<String, JsValue> head = a.head();

        JsObj tail = a.tail();

        final Trampoline<JsObj> tailCall = more(() -> intersectionAll(tail,
                                                                      b,
                                                                      ARRAY_AS
                                                                     ));
        if (b.containsPath(JsPath.fromKey(head._1))) {

            final JsValue headOtherElement = b.get(JsPath.fromKey(head._1));
            if (headOtherElement.equals(head._2)) {
                return more(() -> intersectionAll(tail,
                                                  b.tail(),
                                                  ARRAY_AS
                                                 )).map(it -> it.set(JsPath.fromKey(head._1),
                                                                     head._2
                                                                    ));

            }
            else if (head._2
                    .isJson() && head._2
                    .isSameType(headOtherElement)) {//different but same container
                Json<?> obj = head._2
                        .toJson();
                Json<?> obj1 = headOtherElement.toJson();

                Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpIntersectionJsons().intersectionAll(obj,
                                                                                                                    obj1,
                                                                                                                    ARRAY_AS
                                                                                                                   )
                                                             );
                return more(() -> tailCall).flatMap(json -> headCall
                                                            .map(it -> json.set(JsPath.fromKey(head._1),
                                                                                it
                                                                               )
                                                                )
                                                   );
            }

        }
        return tailCall;
    }

    private Trampoline<JsObj> union(JsObj a,
                                    JsObj b
                                   ) {
        if (b.isEmpty()) return done(a);
        Tuple2<String, JsValue> head = b.head();
        JsObj                   tail = b.tail();
        return union(a,
                     tail
                    ).map(it ->
                                  JsValueLens.of(head._1).modify.apply(c-> c.isNothing() ? head._2 : c).apply(it)

                         );

    }

    //squid:S00117 ARRAY_AS should be a valid name
    private Trampoline<JsObj> unionAll(final JsObj a,
                                       final JsObj b,
                                       final JsArray.TYPE ARRAY_AS
                                      ) {

        if (b.isEmpty()) return done(a);
        Tuple2<String, JsValue> head = b.head();
        JsObj                   tail = b.tail();
        Trampoline<JsObj> tailCall = more(() -> unionAll(a,
                                                         tail,
                                                         ARRAY_AS
                                                        ));
        return ifNothingElse(() -> more(() -> tailCall).map(it -> it.set(JsPath.fromKey(head._1),
                                                                         head._2
                                                                        )),
                             MatchExp.ifPredicateElse(e -> e.isJson() && e.isSameType(head._2),
                                                      it ->
                                                      {
                                                          Json<?> obj = a.get(JsPath.empty()
                                                                                    .key(head._1))
                                                                         .toJson();
                                                          Json<?> obj1 = head._2
                                                                  .toJson();

                                                          Trampoline<? extends Json<?>> headCall = more(() -> () -> new OpUnionJsons().unionAll(obj,
                                                                                                                                                obj1,
                                                                                                                                                ARRAY_AS
                                                                                                                                               )
                                                                                                       );
                                                          return more(() -> tailCall).flatMap(tailResult -> headCall.map(headUnion_ ->
                                                                                                                                 tailResult.set(JsPath.fromKey(head._1),
                                                                                                                                                headUnion_
                                                                                                                                               )
                                                                                                                        )
                                                                                             );
                                                      },
                                                      it -> tailCall
                                                     )
                            )
                .apply(a.get(JsPath.empty()
                                   .key(head._1)));


    }




}

