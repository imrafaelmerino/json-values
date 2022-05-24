package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import io.vavr.Tuple2;
import jsonvalues.JsNothing;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.ERROR_CODE.*;

/**
 * Represents a specification of a Json object
 */
public final class JsObjSpec implements JsSpec {

    final boolean strict;
    final Map<String, JsSpecParser> parsers;
    private final boolean nullable;
    Map<String, JsSpec> bindings;
    private List<String> requiredFields;

    private JsObjSpec(final Map<String, JsSpec> bindings,
                      boolean nullable,
                      boolean strict
    ) {
        this.bindings = bindings;
        this.nullable = nullable;
        this.strict = strict;
        this.requiredFields = new ArrayList<>(bindings.keySet());
        this.parsers = new LinkedHashMap<>();
        for (Map.Entry<String, JsSpec> entry : bindings.entrySet())
            parsers.put(entry.getKey(),
                        entry.getValue().parser());
    }



    public static JsObjSpec strict(final String key,
                                   final JsSpec spec
    ) {
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key),
                     requireNonNull(spec));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }


    public static JsObjSpec lenient(final String key,
                                    final JsSpec spec
    ) {

        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key),
                     requireNonNull(spec));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }


    public static JsObjSpec strict(final String key1,
                                   final JsSpec spec1,
                                   final String key2,
                                   final JsSpec spec2
    ) {

        return strict(key1,
                      spec1).set(requireNonNull(key2),
                                 requireNonNull(spec2));

    }


    public static JsObjSpec lenient(final String key1,
                                    final JsSpec spec1,
                                    final String key2,
                                    final JsSpec spec2
    ) {
        return lenient(key1,
                       spec1).set(requireNonNull(key2),
                                  requireNonNull(spec2));

    }

    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(final String key1,
                                   final JsSpec spec1,
                                   final String key2,
                                   final JsSpec spec2,
                                   final String key3,
                                   final JsSpec spec3
    ) {
        return strict(key1,
                      spec1,
                      key2,
                      spec2).set(requireNonNull(key3),
                                 requireNonNull(spec3));

    }


    @SuppressWarnings("squid:S00107")
    public static JsObjSpec lenient(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3
    ) {
        return lenient(key1,
                       spec1,
                       key2,
                       spec2).set(requireNonNull(key3),
                                  requireNonNull(spec3));
    }


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
        return strict(key1,
                      spec1,
                      key2,
                      spec2,
                      key3,
                      spec3).set(requireNonNull(key4),
                                 requireNonNull(spec4));
    }


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
        return lenient(key1,
                       spec1,
                       key2,
                       spec2,
                       key3,
                       spec3).set(requireNonNull(key4),
                                  requireNonNull(spec4));
    }


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
        return strict(key1,
                      spec1,
                      key2,
                      spec2,
                      key3,
                      spec3,
                      key4,
                      spec4).set(requireNonNull(key5),
                                 requireNonNull(spec5));
    }


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
        return lenient(key1,
                       spec1,
                       key2,
                       spec2,
                       key3,
                       spec3,
                       key4,
                       spec4).set(requireNonNull(key5),
                                  requireNonNull(spec5));
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
            JsSpec spec6
    ) {
        return strict(key1,
                      spec1,
                      key2,
                      spec2,
                      key3,
                      spec3,
                      key4,
                      spec4,
                      key5,
                      spec5).set(requireNonNull(key6),
                                 requireNonNull(spec6));

    }


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
        return lenient(key1,
                       spec1,
                       key2,
                       spec2,
                       key3,
                       spec3,
                       key4,
                       spec4,
                       key5,
                       spec5).set(requireNonNull(key6),
                                  requireNonNull(spec6));
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
            JsSpec spec7
    ) {
        return strict(key1,
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
                      spec6).set(requireNonNull(key7),
                                 requireNonNull(spec7));
    }


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
        return lenient(key1,
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
                       spec6).set(requireNonNull(key7),
                                  requireNonNull(spec7));
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
            JsSpec spec8
    ) {
        return strict(key1,
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
                      spec7).set(requireNonNull(key8),
                                 requireNonNull(spec8));

    }


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
        return lenient(key1,
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
                       spec7).set(requireNonNull(key8),
                                  requireNonNull(spec8));
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
            JsSpec spec9
    ) {
        return strict(key1,
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
                      spec8).set(requireNonNull(key9),
                                 requireNonNull(spec9));
    }


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
        return lenient(key1,
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
                       spec8).set(requireNonNull(key9),
                                  requireNonNull(spec9));
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
        return strict(key1,
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
                      spec9).set(requireNonNull(key10),
                                 requireNonNull(spec10));
    }


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
        return lenient(key1,
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
                       spec9).set(requireNonNull(key10),
                                  requireNonNull(spec10));
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
            JsSpec spec10,
            String key11,
            JsSpec spec11
    ) {
        return strict(key1,
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
                      spec10).set(requireNonNull(key11),
                                  requireNonNull(spec11));
    }


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
        return lenient(key1,
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
                       spec10).set(requireNonNull(key11),
                                   requireNonNull(spec11));
    }


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
        return strict(key1,
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
                      spec11).set(requireNonNull(key12),
                                  requireNonNull(spec12));
    }


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
        return lenient(key1,
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
                       spec11).set(requireNonNull(key12),
                                   requireNonNull(spec12));
    }


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
        return lenient(key1,
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
                       spec12).set(requireNonNull(key13),
                                   requireNonNull(spec13));
    }


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
        return lenient(key1,
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
                       spec13).set(requireNonNull(key14),
                                   requireNonNull(spec14));
    }


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
        return strict(key1,
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
                      spec13).set(requireNonNull(key14),
                                  requireNonNull(spec14));
    }


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
        return lenient(key1,
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
                       spec14).set(requireNonNull(key15),
                                   requireNonNull(spec15));
    }


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
        return strict(key1,
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
                      spec14).set(requireNonNull(key15),
                                  requireNonNull(spec15));
    }


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
        return strict(key1,
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
                      spec15).set(requireNonNull(key16),
                                  requireNonNull(spec16));
    }


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
        return lenient(key1,
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
                       spec15).set(requireNonNull(key16),
                                   requireNonNull(spec16));
    }


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
        return strict(key1,
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
                      spec16
        ).set(requireNonNull(key17),
              requireNonNull(spec17));
    }

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
        return lenient(key1,
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
                       spec16
        ).set(requireNonNull(key17),
              requireNonNull(spec17));
    }


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
        return strict(key1,
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
                      spec17
        ).set(requireNonNull(key18),
              requireNonNull(spec18));
    }


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
        return lenient(key1,
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
                       spec17
        ).set(requireNonNull(key18),
              requireNonNull(spec18));

    }


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
        return strict(key1,
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
                      spec18
        ).set(requireNonNull(key19),
              requireNonNull(spec19));


    }


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
        return lenient(key1,
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
                       spec18
        ).set(requireNonNull(key19),
              requireNonNull(spec19));
    }


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
        return strict(key1,
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
                      spec19
        ).set(requireNonNull(key20),
              requireNonNull(spec20));
    }


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
        return lenient(key1,
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
                       spec19
        ).set(requireNonNull(key20),
              requireNonNull(spec20));
    }


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
        return strict(key1,
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
                      spec12
        ).set(requireNonNull(key13),
              requireNonNull(spec13));
    }

    public List<String> getRequiredFields() {
        return requiredFields;
    }

    public JsObjSpec setOptionals(final String field,
                                  final String... fields) {
        JsObjSpec spec = new JsObjSpec(bindings,
                                       nullable,
                                       strict);
        List<String> optionalFields = new ArrayList<>();
        optionalFields.add(field);
        optionalFields
                .addAll(Arrays.stream(requireNonNull(fields))
                              .collect(Collectors.toList()));
        spec.requiredFields = getRequiredFields(spec.bindings.keySet(),
                                                optionalFields);
        return spec;
    }

    private List<String> getRequiredFields(Set<String> fields,
                                           List<String> optionals) {
        return fields.stream().filter(key -> !optionals.contains(key)).collect(Collectors.toList());
    }

    public JsObjSpec setOptionals(final List<String> optionals) {
        JsObjSpec spec = new JsObjSpec(bindings,
                                       nullable,
                                       strict);
        spec.requiredFields = getRequiredFields(spec.bindings.keySet(),
                                                optionals);
        return spec;

    }

    @Override
    public JsObjSpec nullable() {
        return new JsObjSpec(bindings,
                             true,
                             strict
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofObjSpec(requiredFields,
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
                                      new JsError(parentValue,
                                                  OBJ_EXPECTED
                                      )
            ));
            return errors;
        }
        JsObj json = parentValue.toJsObj();
        for (final Tuple2<String, JsValue> next : json) {
            final String key = next._1;
            final JsValue value = next._2;
            final JsPath keyPath = JsPath.fromKey(key);
            final JsPath currentPath = parent.append(keyPath);
            final JsSpec spec = parentObjSpec.bindings.get(key);
            if (spec == null) {
                if (parentObjSpec.strict) {
                    errors.add(JsErrorPair.of(currentPath,
                                              new JsError(value,
                                                          SPEC_MISSING
                                              )
                    ));
                }
            } else errors.addAll(spec.test(currentPath,
                                           value));

        }

        for (final String requiredField : requiredFields) {
            if (!json.containsKey(requiredField))
                errors.add(JsErrorPair.of(parent.key(requiredField),
                                          new JsError(JsNothing.NOTHING,
                                                      REQUIRED
                                          )
                           )
                );
        }


        return errors;
    }

    /**
     * add the given key spec to this
     *
     * @param key  the key
     * @param spec the spec
     * @return a new object spec
     */
    public JsObjSpec set(final String key,
                         final JsSpec spec) {
        LinkedHashMap<String, JsSpec> newBindings = new LinkedHashMap<>(bindings);
        newBindings.put(requireNonNull(key),
                        requireNonNull(spec)
        );
        return new JsObjSpec(newBindings,
                             this.nullable,
                             this.strict
        );
    }
}
