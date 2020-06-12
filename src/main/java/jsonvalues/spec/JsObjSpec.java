package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import jsonvalues.JsNothing;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsValue;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.ERROR_CODE.*;

/**
 Represents a specification of a Json object
 */
public class JsObjSpec implements JsSpec {

    final boolean strict;
    /**
     When this spec is associated to a key in another JsObjSpec, the required flag indicates whether or
     not the key is optional. If this JsObjSpec is the root of the spec, the flag doesn't have
     any meaning
     */
    private final boolean required;
    private final boolean nullable;
    Map<String, JsSpec> bindings = HashMap.empty();

    private JsObjSpec(final Map<String, JsSpec> bindings,
                      boolean required,
                      boolean nullable,
                      boolean strict
                     ) {
        this.bindings = bindings;
        this.required = required;
        this.nullable = nullable;
        this.strict = strict;
    }


    @SuppressWarnings("squid:S00107")
    private JsObjSpec(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final boolean strict
                     ) {
        this(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                strict
            );
        bindings = bindings.put(key15,
                                spec15
                               );
    }
    @SuppressWarnings("squid:S00107")
    private JsObjSpec(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final boolean strict
                     ) {
        this(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                strict
            );
        bindings = bindings.put(key16,
                                spec16
                               );
    }
    @SuppressWarnings("squid:S00107")
    private JsObjSpec(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17,
            final boolean strict
                     ) {
        this(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                strict
            );
        bindings = bindings.put(key17,
                                spec17
                               );
    }
    @SuppressWarnings("squid:S00107")
    private JsObjSpec(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17,
            final String key18,
            final JsSpec spec18,
            final boolean strict
                     ) {
        this(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                key17,
                spec17,
                strict
            );
        bindings = bindings.put(key18,
                                spec18
                               );
    }

    @SuppressWarnings("squid:S00107")
    private JsObjSpec(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17,
            final String key18,
            final JsSpec spec18,
            final String key19,
            final JsSpec spec19,
            final boolean strict
                     ) {
        this(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                key17,
                spec17,
                key18,
                spec18,
                strict
            );
        bindings = bindings.put(key19,
                                spec19
                               );
    }

    @SuppressWarnings("squid:S00107")
    private JsObjSpec(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17,
            final String key18,
            final JsSpec spec18,
            final String key19,
            final JsSpec spec19,
            final String key20,
            final JsSpec spec20,
            final boolean strict
                     ) {
        this(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                key17,
                spec17,
                key18,
                spec18,
                key19,
                spec19,
                strict
            );
        bindings = bindings.put(key20,
                                spec20
                               );
    }
    @SuppressWarnings("squid:S00107")
    private JsObjSpec(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final boolean strict
                     ) {
        this(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                strict
            );
        bindings = bindings.put(key14,
                                spec14
                               );
    }
    @SuppressWarnings("squid:S00107")
    private JsObjSpec(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final boolean strict
                     ) {
        this(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                strict
            );
        bindings = bindings.put(key13,
                                spec13
                               );
    }


    @SuppressWarnings("squid:S00107")
    private JsObjSpec(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final boolean strict
                     ) {
        this(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                strict
            );
        bindings = bindings.put(key12,
                                spec12
                               );
    }

    @SuppressWarnings("squid:S00107")
    private JsObjSpec(final String key,
                      final JsSpec spec,
                      final String key1,
                      final JsSpec spec1,
                      final String key2,
                      final JsSpec spec2,
                      final String key3,
                      final JsSpec spec3,
                      final String key4,
                      final JsSpec spec4,
                      final String key5,
                      final JsSpec spec5,
                      final String key6,
                      final JsSpec spec6,
                      final String key7,
                      final JsSpec spec7,
                      final String key8,
                      final JsSpec spec8,
                      final String key9,
                      final JsSpec spec9,
                      final String key10,
                      final JsSpec spec10,
                      final boolean strict
                     ) {
        this(key,
             spec,
             key1,
             spec1,
             key2,
             spec2,
             key3,
             spec3,
             key4,
             spec4,
             key5,
             spec5,
             key6,
             spec6,
             key7,
             spec7,
             key8,
             spec8,
             key9,
             spec9,
             strict
            );
        bindings = bindings.put(key10,
                                spec10
                               );
    }
    @SuppressWarnings("squid:S00107")
    private JsObjSpec(String key,
                      JsSpec spec,
                      String key1,
                      JsSpec spec1,
                      String key2,
                      JsSpec spec2,
                      String key3,
                      JsSpec spec3,
                      String key4,
                      JsSpec spec4,
                      String key5,
                      JsSpec spec5,
                      String key6,
                      JsSpec spec6,
                      String key7,
                      JsSpec spec7,
                      String key8,
                      JsSpec spec8,
                      String key9,
                      JsSpec spec9,
                      boolean strict
                     ) {
        this(key,
             spec,
             key1,
             spec1,
             key2,
             spec2,
             key3,
             spec3,
             key4,
             spec4,
             key5,
             spec5,
             key6,
             spec6,
             key7,
             spec7,
             key8,
             spec8,
             strict
            );
        bindings = bindings.put(key9,
                                spec9
                               );

    }

    @SuppressWarnings("squid:S00107")
    private JsObjSpec(String key,
                      JsSpec spec,
                      String key1,
                      JsSpec spec1,
                      String key2,
                      JsSpec spec2,
                      String key3,
                      JsSpec spec3,
                      String key4,
                      JsSpec spec4,
                      String key5,
                      JsSpec spec5,
                      String key6,
                      JsSpec spec6,
                      String key7,
                      JsSpec spec7,
                      String key8,
                      JsSpec spec8,
                      boolean strict
                     ) {
        this(key,
             spec,
             key1,
             spec1,
             key2,
             spec2,
             key3,
             spec3,
             key4,
             spec4,
             key5,
             spec5,
             key6,
             spec6,
             key7,
             spec7,
             strict
            );
        bindings = bindings.put(key8,
                                spec8
                               );
    }

    @SuppressWarnings("squid:S00107")
    private JsObjSpec(String key,
                      JsSpec spec,
                      String key1,
                      JsSpec spec1,
                      String key2,
                      JsSpec spec2,
                      String key3,
                      JsSpec spec3,
                      String key4,
                      JsSpec spec4,
                      String key5,
                      JsSpec spec5,
                      String key6,
                      JsSpec spec6,
                      String key7,
                      JsSpec spec7,
                      boolean strict
                     ) {
        this(key,
             spec,
             key1,
             spec1,
             key2,
             spec2,
             key3,
             spec3,
             key4,
             spec4,
             key5,
             spec5,
             key6,
             spec6,
             strict
            );
        bindings = bindings.put(key7,
                                spec7
                               );
    }
    @SuppressWarnings("squid:S00107")
    private JsObjSpec(String key,
                      JsSpec spec,
                      String key1,
                      JsSpec spec1,
                      String key2,
                      JsSpec spec2,
                      String key3,
                      JsSpec spec3,
                      String key4,
                      JsSpec spec4,
                      String key5,
                      JsSpec spec5,
                      String key6,
                      JsSpec spec6,
                      boolean strict
                     ) {
        this(key,
             spec,
             key1,
             spec1,
             key2,
             spec2,
             key3,
             spec3,
             key4,
             spec4,
             key5,
             spec5,
             strict
            );
        bindings = bindings.put(key6,
                                spec6
                               );
    }
    @SuppressWarnings("squid:S00107")
    private JsObjSpec(String key,
                      JsSpec spec,
                      String key1,
                      JsSpec spec1,
                      String key2,
                      JsSpec spec2,
                      String key3,
                      JsSpec spec3,
                      String key4,
                      JsSpec spec4,
                      String key5,
                      JsSpec spec5,
                      boolean strict
                     ) {
        this(key,
             spec,
             key1,
             spec1,
             key2,
             spec2,
             key3,
             spec3,
             key4,
             spec4,
             strict
            );
        bindings = bindings.put(key5,
                                spec5
                               );
    }
    @SuppressWarnings("squid:S00107")
    private JsObjSpec(String key,
                      JsSpec spec,
                      String key1,
                      JsSpec spec1,
                      String key2,
                      JsSpec spec2,
                      String key3,
                      JsSpec spec3,
                      String key4,
                      JsSpec spec4,
                      boolean strict
                     ) {
        this(key,
             spec,
             key1,
             spec1,
             key2,
             spec2,
             key3,
             spec3,
             strict
            );
        bindings = bindings.put(key4,
                                spec4
                               );
    }
    @SuppressWarnings("squid:S00107")
    private JsObjSpec(final String key,
                      final JsSpec spec,
                      final String key1,
                      final JsSpec spec1,
                      final String key2,
                      final JsSpec spec2,
                      final String key3,
                      final JsSpec spec3,
                      final boolean strict
                     ) {
        this(key,
             spec,
             key1,
             spec1,
             key2,
             spec2,
             strict
            );
        bindings = bindings.put(key3,
                                spec3
                               );
    }
    @SuppressWarnings("squid:S00107")
    private JsObjSpec(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            boolean strict
                     ) {
        this(
                key1,
                spec1,
                key2,
                spec2,
                strict
            );
        bindings = bindings.put(key3,
                                spec3
                               );
    }

    private JsObjSpec(final String key1,
                      final JsSpec spec1,
                      final String key2,
                      final JsSpec spec2,
                      final boolean strict
                     ) {
        this(key1,
             spec1,
             strict,
             true,
             false
            );
        bindings = bindings.put(key2,
                                spec2
                               );
    }

    private JsObjSpec(final String key,
                      final JsSpec spec,
                      final boolean strict,
                      final boolean required,
                      final boolean nullable
                     ) {
        bindings = bindings.put(key,
                                spec
                               );
        this.strict = strict;
        this.required = required;
        this.nullable = nullable;
    }

    /**
     static factory method to create a strict JsObjSpec of one mappings. Strict means that different
     keys than the defined are not allowed

     @param key  the  key
     @param spec the mapping associated to the  key
     @return a JsObjSpec
     */
    public static JsObjSpec strict(final String key,
                                   final JsSpec spec
                                  ) {
        return new JsObjSpec(key,
                             spec,
                             true,
                             true,
                             false
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of one mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key  the key
     @param spec the mapping associated to the key
     @return a JsObjSpec
     */
    public static JsObjSpec lenient(final String key,
                                    final JsSpec spec
                                   ) {
        return new JsObjSpec(key,
                             spec,
                             false,
                             true,
                             false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of two mappings. Strict means that different
     keys than the defined are not allowed

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @return a JsObjSpec
     */
    public static JsObjSpec strict(final String key1,
                                   final JsSpec spec1,
                                   final String key2,
                                   final JsSpec spec2
                                  ) {
        return new JsObjSpec(key1,
                             spec1,
                             key2,
                             spec2,
                             true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of two mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @return a JsObjSpec
     */
    public static JsObjSpec lenient(final String key1,
                                    final JsSpec spec1,
                                    final String key2,
                                    final JsSpec spec2
                                   ) {
        return new JsObjSpec(key1,
                             spec1,
                             key2,
                             spec2,
                             false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of three mappings. Strict means that different
     keys than the defined are not allowed

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(final String key1,
                                   final JsSpec spec1,
                                   final String key2,
                                   final JsSpec spec2,
                                   final String key3,
                                   final JsSpec spec3
                                  ) {
        return new JsObjSpec(key1,
                             spec1,
                             key2,
                             spec2,
                             key3,
                             spec3,
                             true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of three mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of four mappings. Strict means that different
     keys than the defined are not allowed

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of four mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of five mappings. Strict means that different
     keys than the defined are not allowed

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @param key5  the fifth key
     @param spec5 the mapping associated to the fifth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of five mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @param key5  the fifth key
     @param spec5 the mapping associated to the fifth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                true
        );
    }

    /**
     static factory method to create a strict JsObjSpec of six mappings. Strict means that different
     keys than the defined are not allowed

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @param key5  the fifth key
     @param spec5 the mapping associated to the fifth key
     @param key6  the sixth key
     @param spec6 the mapping associated to the sixth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of six  mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @param key5  the fifth key
     @param spec5 the mapping associated to the fifth key
     @param key6  the sixth key
     @param spec6 the mapping associated to the sixth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of seven mappings. Strict means that different
     keys than the defined are not allowed

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @param key5  the fifth key
     @param spec5 the mapping associated to the fifth key
     @param key6  the sixth key
     @param spec6 the mapping associated to the sixth key
     @param key7  the seventh key
     @param spec7 the mapping associated to the seventh key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of seven  mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @param key5  the fifth key
     @param spec5 the mapping associated to the fifth key
     @param key6  the sixth key
     @param spec6 the mapping associated to the sixth key
     @param key7  the seventh key
     @param spec7 the mapping associated to the seventh key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of eight mappings. Strict means that different
     keys than the defined are not allowed

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @param key5  the fifth key
     @param spec5 the mapping associated to the fifth key
     @param key6  the sixth key
     @param spec6 the mapping associated to the sixth key
     @param key7  the seventh key
     @param spec7 the mapping associated to the seventh key
     @param key8  the eighth key
     @param spec8 the mapping associated to the eighth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of eight  mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @param key5  the fifth key
     @param spec5 the mapping associated to the fifth key
     @param key6  the sixth key
     @param spec6 the mapping associated to the sixth key
     @param key7  the seventh key
     @param spec7 the mapping associated to the seventh key
     @param key8  the eighth key
     @param spec8 the mapping associated to the eighth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of nine mappings. Strict means that different
     keys than the defined are not allowed

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @param key5  the fifth key
     @param spec5 the mapping associated to the fifth key
     @param key6  the sixth key
     @param spec6 the mapping associated to the sixth key
     @param key7  the seventh key
     @param spec7 the mapping associated to the seventh key
     @param key8  the eighth key
     @param spec8 the mapping associated to the eighth key
     @param key9  the ninth key
     @param spec9 the mapping associated to the ninth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8,
            String key9,
            JsSpec spec9
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of nine  mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1  the first key
     @param spec1 the mapping associated to the first key
     @param key2  the second key
     @param spec2 the mapping associated to the second key
     @param key3  the third key
     @param spec3 the mapping associated to the third key
     @param key4  the fourth key
     @param spec4 the mapping associated to the fourth key
     @param key5  the fifth key
     @param spec5 the mapping associated to the fifth key
     @param key6  the sixth key
     @param spec6 the mapping associated to the sixth key
     @param key7  the seventh key
     @param spec7 the mapping associated to the seventh key
     @param key8  the eighth key
     @param spec8 the mapping associated to the eighth key
     @param key9  the ninth key
     @param spec9 the mapping associated to the ninth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8,
            String key9,
            JsSpec spec9
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                false
        );
    }
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8,
            String key9,
            JsSpec spec9,
            String key10,
            JsSpec spec10
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of ten mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8,
            String key9,
            JsSpec spec9,
            String key10,
            JsSpec spec10
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of eleven mappings. Strict means that different
     keys than the defined are not allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8,
            String key9,
            JsSpec spec9,
            String key10,
            JsSpec spec10,
            String key11,
            JsSpec spec11
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of eleven mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            String key1,
            JsSpec spec1,
            String key2,
            JsSpec spec2,
            String key3,
            JsSpec spec3,
            String key4,
            JsSpec spec4,
            String key5,
            JsSpec spec5,
            String key6,
            JsSpec spec6,
            String key7,
            JsSpec spec7,
            String key8,
            JsSpec spec8,
            String key9,
            JsSpec spec9,
            String key10,
            JsSpec spec10,
            String key11,
            JsSpec spec11
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of twelve mappings. Strict means that different
     keys than the defined are not allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of twelve mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                false
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of thirteen mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                false
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of fourteen mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of fourteen mappings. Strict means that different
     keys than the defined are not allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of fifteen mappings. Lenient means that different
     keys than the defined are allowed, being valid any value associated to them

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15
                                   ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of fifteen mappings. Strict means that different
     keys than the defined are not allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                true
        );
    }

    /**
     static factory method to create a strict JsObjSpec of sixteen mappings. Strict means that different
     keys than the defined are not allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @param key16  the sixteenth key
     @param spec16 the mapping associated to the sixteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
             final String key16,
            final JsSpec spec16
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of sixteen mappings. Strict means that different
     keys than the defined are allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @param key16  the sixteenth key
     @param spec16 the mapping associated to the sixteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                false
        );
    }


    /**
     static factory method to create a strict JsObjSpec of seventeen mappings. Strict means that different
     keys than the defined are not allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @param key16  the sixteenth key
     @param spec16 the mapping associated to the sixteenth key
     @param key17  the seventeenth key
     @param spec17 the mapping associated to the seventeenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                key17,
                spec17,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of seventeen mappings. Lenient means that different
     keys than the defined are allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @param key16  the sixteenth key
     @param spec16 the mapping associated to the sixteenth key
     @param key17  the seventeenth key
     @param spec17 the mapping associated to the seventeenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                key17,
                spec17,
                false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of eighteen mappings. Strict means that different
     keys than the defined are not allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @param key16  the sixteenth key
     @param spec16 the mapping associated to the sixteenth key
     @param key17  the seventeenth key
     @param spec17 the mapping associated to the seventeenth key
     @param key18  the eighteenth key
     @param spec18 the mapping associated to the eighteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17,
            final String key18,
            final JsSpec spec18
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                key17,
                spec17,
                key18,
                spec18,
                true
        );
    }
    /**
     static factory method to create a lenient JsObjSpec of eighteen mappings. Lenient means that different
     keys than the defined are allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @param key16  the sixteenth key
     @param spec16 the mapping associated to the sixteenth key
     @param key17  the seventeenth key
     @param spec17 the mapping associated to the seventeenth key
     @param key18  the eighteenth key
     @param spec18 the mapping associated to the eighteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17,
            final String key18,
            final JsSpec spec18
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                key17,
                spec17,
                key18,
                spec18,
                false
        );
    }
    /**
     static factory method to create a strict JsObjSpec of nineteen mappings. Strict means that different
     keys than the defined are not allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @param key16  the sixteenth key
     @param spec16 the mapping associated to the sixteenth key
     @param key17  the seventeenth key
     @param spec17 the mapping associated to the seventeenth key
     @param key18  the eighteenth key
     @param spec18 the mapping associated to the eighteenth key
     @param key19  the nineteenth key
     @param spec19 the mapping associated to the nineteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17,
            final String key18,
            final JsSpec spec18,
            final String key19,
            final JsSpec spec19
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                key17,
                spec17,
                key18,
                spec18,
                key19,
                spec19,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of nineteen mappings. Lenient means that different
     keys than the defined are allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @param key16  the sixteenth key
     @param spec16 the mapping associated to the sixteenth key
     @param key17  the seventeenth key
     @param spec17 the mapping associated to the seventeenth key
     @param key18  the eighteenth key
     @param spec18 the mapping associated to the eighteenth key
     @param key19  the nineteenth key
     @param spec19 the mapping associated to the nineteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17,
            final String key18,
            final JsSpec spec18,
            final String key19,
            final JsSpec spec19
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                key17,
                spec17,
                key18,
                spec18,
                key19,
                spec19,
                false
        );
    }

    /**
     static factory method to create a strict JsObjSpec of twenty mappings. Strict means that different
     keys than the defined are not allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @param key16  the sixteenth key
     @param spec16 the mapping associated to the sixteenth key
     @param key17  the seventeenth key
     @param spec17 the mapping associated to the seventeenth key
     @param key18  the eighteenth key
     @param spec18 the mapping associated to the eighteenth key
     @param key19  the nineteenth key
     @param spec19 the mapping associated to the nineteenth key
     @param key20  the twentieth key
     @param spec20 the mapping associated to the twentieth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17,
            final String key18,
            final JsSpec spec18,
            final String key19,
            final JsSpec spec19,
            final String key20,
            final JsSpec spec20
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                key17,
                spec17,
                key18,
                spec18,
                key19,
                spec19,
                key20,
                spec20,
                true
        );
    }

    /**
     static factory method to create a lenient JsObjSpec of twenty mappings. Lenient means that different
     keys than the defined are allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @param key14  the fourteenth key
     @param spec14 the mapping associated to the fourteenth key
     @param key15  the fifteenth key
     @param spec15 the mapping associated to the fifteenth key
     @param key16  the sixteenth key
     @param spec16 the mapping associated to the sixteenth key
     @param key17  the seventeenth key
     @param spec17 the mapping associated to the seventeenth key
     @param key18  the eighteenth key
     @param spec18 the mapping associated to the eighteenth key
     @param key19  the nineteenth key
     @param spec19 the mapping associated to the nineteenth key
     @param key20  the twentieth key
     @param spec20 the mapping associated to the twentieth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13,
            final String key14,
            final JsSpec spec14,
            final String key15,
            final JsSpec spec15,
            final String key16,
            final JsSpec spec16,
            final String key17,
            final JsSpec spec17,
            final String key18,
            final JsSpec spec18,
            final String key19,
            final JsSpec spec19,
            final String key20,
            final JsSpec spec20
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                key14,
                spec14,
                key15,
                spec15,
                key16,
                spec16,
                key17,
                spec17,
                key18,
                spec18,
                key19,
                spec19,
                key20,
                spec20,
                false
        );
    }
    /**
     static factory method to create a strict JsObjSpec of thirteen mappings. Strict means that different
     keys than the defined are not allowed

     @param key1   the first key
     @param spec1  the mapping associated to the first key
     @param key2   the second key
     @param spec2  the mapping associated to the second key
     @param key3   the third key
     @param spec3  the mapping associated to the third key
     @param key4   the fourth key
     @param spec4  the mapping associated to the fourth key
     @param key5   the fifth key
     @param spec5  the mapping associated to the fifth key
     @param key6   the sixth key
     @param spec6  the mapping associated to the sixth key
     @param key7   the seventh key
     @param spec7  the mapping associated to the seventh key
     @param key8   the eighth key
     @param spec8  the mapping associated to the eighth key
     @param key9   the ninth key
     @param spec9  the mapping associated to the ninth key
     @param key10  the tenth key
     @param spec10 the mapping associated to the eleventh key
     @param key11  the eleventh key
     @param spec11 the mapping associated to the eleventh key
     @param key12  the twelfth key
     @param spec12 the mapping associated to the twelfth key,
     @param key13  the thirteenth key
     @param spec13 the mapping associated to the thirteenth key
     @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4,
            final String key5,
            final JsSpec spec5,
            final String key6,
            final JsSpec spec6,
            final String key7,
            final JsSpec spec7,
            final String key8,
            final JsSpec spec8,
            final String key9,
            final JsSpec spec9,
            final String key10,
            final JsSpec spec10,
            final String key11,
            final JsSpec spec11,
            final String key12,
            final JsSpec spec12,
            final String key13,
            final JsSpec spec13
                                  ) {
        return new JsObjSpec(
                key1,
                spec1,
                key2,
                spec2,
                key3,
                spec3,
                key4,
                spec4,
                key5,
                spec5,
                key6,
                spec6,
                key7,
                spec7,
                key8,
                spec8,
                key9,
                spec9,
                key10,
                spec10,
                key11,
                spec11,
                key12,
                spec12,
                key13,
                spec13,
                true
        );
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public JsObjSpec nullable() {
        return new JsObjSpec(bindings,
                             required,
                             true,
                             strict
        );
    }

    @Override
    public JsObjSpec optional() {
        return new JsObjSpec(bindings,
                             false,
                             nullable,
                             strict
        );
    }

    @Override
    public JsSpecParser parser() {
        Map<String, JsSpecParser> parsers  = HashMap.empty();
        Vector<String>            required = Vector.empty();
        for (final String key : bindings.keySet()) {

            final JsSpec spec = bindings.get(key)
                                        .get();
            if (spec.isRequired()) required = required.append(key);
            parsers = parsers.put(key,
                                  spec.parser()
                                 );
        }


        return JsSpecParsers.INSTANCE.ofObjSpec(required,
                                                parsers,
                                                nullable,
                                                strict
                                               );
    }

    @Override
    public Set<JsErrorPair> test(final JsPath parentPath,
                                 final JsValue value
                                ) {
        return test(parentPath,
                    this,
                    new HashSet<>(),
                    value
                   );
    }

    public Set<JsErrorPair> test(final JsObj obj) {
        return test(JsPath.empty(),
                    obj
                   );
    }

    private Set<JsErrorPair> test(final JsPath parent,
                                  final JsObjSpec parentObjSpec,
                                  final Set<JsErrorPair> errors,
                                  final JsValue parentValue
                                 ) {

        if (parentValue.isNull() && nullable) return errors;
        if (!parentValue.isObj()) {
            errors.add(JsErrorPair.of(parent,
                                      new Error(parentValue,
                                                OBJ_EXPECTED
                                      )
                                     ));
            return errors;
        }
        JsObj json = parentValue.toJsObj();
        for (final Tuple2<String, JsValue> next : json) {
            final String  key         = next._1;
            final JsValue value       = next._2;
            final JsPath  keyPath     = JsPath.fromKey(key);
            final JsPath  currentPath = parent.append(keyPath);
            final JsSpec spec = parentObjSpec.bindings.getOrElse(key,
                                                                 null
                                                                );
            if (spec == null) {
                if (parentObjSpec.strict) {
                    errors.add(JsErrorPair.of(currentPath,
                                              new Error(value,
                                                        SPEC_MISSING
                                              )
                                             ));
                }
            }
            else errors.addAll(spec.test(currentPath,
                                         value
                                        ));

        }
        final Seq<String> requiredFields = parentObjSpec.bindings.filter((key, spec) -> spec.isRequired())
                                                                 .map(p -> p._1);
        for (final String requiredField : requiredFields) {
            if (!json.containsKey(requiredField)) errors.add(JsErrorPair.of(parent.key(requiredField),
                                                                            new Error(JsNothing.NOTHING,
                                                                                      REQUIRED
                                                                            )
                                                                           )
                                                            );
        }


        return errors;
    }

    /**
     add the given key spec to this
     @param key the key
     @param spec the spec
     @return a new object spec
     */
    public JsObjSpec set(final String key,
                         final JsSpec spec) {
        return new JsObjSpec(bindings.put(requireNonNull(key),
                                          requireNonNull(spec)
                                         ),
                             this.required,
                             this.nullable,
                             this.strict
        );
    }
}
