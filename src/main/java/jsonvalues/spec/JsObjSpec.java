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
    private final boolean nullable;
    Map<String, JsSpec> bindings;
    private List<String> requiredFields;

    final Map<String, JsSpecParser> parsers;

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
            parsers.put(entry.getKey(), entry.getValue().parser());
    }


    /**
     * static factory method to create a strict JsObjSpec of one mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key  the  key
     * @param spec the mapping associated to the  key
     * @return a JsObjSpec
     */
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

    /**
     * static factory method to create a lenient JsObjSpec of one mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key  the key
     * @param spec the mapping associated to the key
     * @return a JsObjSpec
     */
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

    /**
     * static factory method to create a strict JsObjSpec of two mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @return a JsObjSpec
     */
    public static JsObjSpec strict(final String key1,
                                   final JsSpec spec1,
                                   final String key2,
                                   final JsSpec spec2
    ) {
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of two mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @return a JsObjSpec
     */
    public static JsObjSpec lenient(final String key1,
                                    final JsSpec spec1,
                                    final String key2,
                                    final JsSpec spec2
    ) {
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of three mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @return a JsObjSpec
     */
    @SuppressWarnings("squid:S00107")
    public static JsObjSpec strict(final String key1,
                                   final JsSpec spec1,
                                   final String key2,
                                   final JsSpec spec2,
                                   final String key3,
                                   final JsSpec spec3
    ) {
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of three mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of four mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of four mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of five mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param spec5 the mapping associated to the fifth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of five mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param spec5 the mapping associated to the fifth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of six mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param spec5 the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param spec6 the mapping associated to the sixth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of six  mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param spec5 the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param spec6 the mapping associated to the sixth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of seven mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param spec5 the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param spec6 the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param spec7 the mapping associated to the seventh key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of seven  mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param spec5 the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param spec6 the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param spec7 the mapping associated to the seventh key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of eight mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param spec5 the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param spec6 the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param spec7 the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param spec8 the mapping associated to the eighth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of eight  mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param spec5 the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param spec6 the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param spec7 the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param spec8 the mapping associated to the eighth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of nine mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param spec5 the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param spec6 the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param spec7 the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param spec8 the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param spec9 the mapping associated to the ninth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of nine  mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1  the first key
     * @param spec1 the mapping associated to the first key
     * @param key2  the second key
     * @param spec2 the mapping associated to the second key
     * @param key3  the third key
     * @param spec3 the mapping associated to the third key
     * @param key4  the fourth key
     * @param spec4 the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param spec5 the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param spec6 the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param spec7 the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param spec8 the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param spec9 the mapping associated to the ninth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        return new JsObjSpec(bindings,
                             false,
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of ten mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of eleven mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of eleven mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of twelve mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of twelve mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of thirteen mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of fourteen mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of fourteen mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of fifteen mappings. Lenient means that different
     * keys than the defined are allowed, being valid any value associated to them
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of fifteen mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of sixteen mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @param key16  the sixteenth key
     * @param spec16 the mapping associated to the sixteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        bindings.put(requireNonNull(key16),
                     requireNonNull(spec16));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of sixteen mappings. Strict means that different
     * keys than the defined are allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @param key16  the sixteenth key
     * @param spec16 the mapping associated to the sixteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        bindings.put(requireNonNull(key16),
                     requireNonNull(spec16));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of seventeen mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @param key16  the sixteenth key
     * @param spec16 the mapping associated to the sixteenth key
     * @param key17  the seventeenth key
     * @param spec17 the mapping associated to the seventeenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        bindings.put(requireNonNull(key16),
                     requireNonNull(spec16));
        bindings.put(requireNonNull(key17),
                     requireNonNull(spec17));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of seventeen mappings. Lenient means that different
     * keys than the defined are allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @param key16  the sixteenth key
     * @param spec16 the mapping associated to the sixteenth key
     * @param key17  the seventeenth key
     * @param spec17 the mapping associated to the seventeenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        bindings.put(requireNonNull(key16),
                     requireNonNull(spec16));
        bindings.put(requireNonNull(key17),
                     requireNonNull(spec17));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of eighteen mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @param key16  the sixteenth key
     * @param spec16 the mapping associated to the sixteenth key
     * @param key17  the seventeenth key
     * @param spec17 the mapping associated to the seventeenth key
     * @param key18  the eighteenth key
     * @param spec18 the mapping associated to the eighteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        bindings.put(requireNonNull(key16),
                     requireNonNull(spec16));
        bindings.put(requireNonNull(key17),
                     requireNonNull(spec17));
        bindings.put(requireNonNull(key18),
                     requireNonNull(spec18));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of eighteen mappings. Lenient means that different
     * keys than the defined are allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @param key16  the sixteenth key
     * @param spec16 the mapping associated to the sixteenth key
     * @param key17  the seventeenth key
     * @param spec17 the mapping associated to the seventeenth key
     * @param key18  the eighteenth key
     * @param spec18 the mapping associated to the eighteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        bindings.put(requireNonNull(key16),
                     requireNonNull(spec16));
        bindings.put(requireNonNull(key17),
                     requireNonNull(spec17));
        bindings.put(requireNonNull(key18),
                     requireNonNull(spec18));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of nineteen mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @param key16  the sixteenth key
     * @param spec16 the mapping associated to the sixteenth key
     * @param key17  the seventeenth key
     * @param spec17 the mapping associated to the seventeenth key
     * @param key18  the eighteenth key
     * @param spec18 the mapping associated to the eighteenth key
     * @param key19  the nineteenth key
     * @param spec19 the mapping associated to the nineteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        bindings.put(requireNonNull(key16),
                     requireNonNull(spec16));
        bindings.put(requireNonNull(key17),
                     requireNonNull(spec17));
        bindings.put(requireNonNull(key18),
                     requireNonNull(spec18));
        bindings.put(requireNonNull(key19),
                     requireNonNull(spec19));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of nineteen mappings. Lenient means that different
     * keys than the defined are allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @param key16  the sixteenth key
     * @param spec16 the mapping associated to the sixteenth key
     * @param key17  the seventeenth key
     * @param spec17 the mapping associated to the seventeenth key
     * @param key18  the eighteenth key
     * @param spec18 the mapping associated to the eighteenth key
     * @param key19  the nineteenth key
     * @param spec19 the mapping associated to the nineteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        bindings.put(requireNonNull(key16),
                     requireNonNull(spec16));
        bindings.put(requireNonNull(key17),
                     requireNonNull(spec17));
        bindings.put(requireNonNull(key18),
                     requireNonNull(spec18));
        bindings.put(requireNonNull(key19),
                     requireNonNull(spec19));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of twenty mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @param key16  the sixteenth key
     * @param spec16 the mapping associated to the sixteenth key
     * @param key17  the seventeenth key
     * @param spec17 the mapping associated to the seventeenth key
     * @param key18  the eighteenth key
     * @param spec18 the mapping associated to the eighteenth key
     * @param key19  the nineteenth key
     * @param spec19 the mapping associated to the nineteenth key
     * @param key20  the twentieth key
     * @param spec20 the mapping associated to the twentieth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        bindings.put(requireNonNull(key16),
                     requireNonNull(spec16));
        bindings.put(requireNonNull(key17),
                     requireNonNull(spec17));
        bindings.put(requireNonNull(key18),
                     requireNonNull(spec18));
        bindings.put(requireNonNull(key19),
                     requireNonNull(spec19));
        bindings.put(requireNonNull(key20),
                     requireNonNull(spec20));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
    }

    /**
     * static factory method to create a lenient JsObjSpec of twenty mappings. Lenient means that different
     * keys than the defined are allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @param key14  the fourteenth key
     * @param spec14 the mapping associated to the fourteenth key
     * @param key15  the fifteenth key
     * @param spec15 the mapping associated to the fifteenth key
     * @param key16  the sixteenth key
     * @param spec16 the mapping associated to the sixteenth key
     * @param key17  the seventeenth key
     * @param spec17 the mapping associated to the seventeenth key
     * @param key18  the eighteenth key
     * @param spec18 the mapping associated to the eighteenth key
     * @param key19  the nineteenth key
     * @param spec19 the mapping associated to the nineteenth key
     * @param key20  the twentieth key
     * @param spec20 the mapping associated to the twentieth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        bindings.put(requireNonNull(key14),
                     requireNonNull(spec14));
        bindings.put(requireNonNull(key15),
                     requireNonNull(spec15));
        bindings.put(requireNonNull(key16),
                     requireNonNull(spec16));
        bindings.put(requireNonNull(key17),
                     requireNonNull(spec17));
        bindings.put(requireNonNull(key18),
                     requireNonNull(spec18));
        bindings.put(requireNonNull(key19),
                     requireNonNull(spec19));
        bindings.put(requireNonNull(key20),
                     requireNonNull(spec20));
        return new JsObjSpec(bindings,
                             false,
                             false
        );
    }

    /**
     * static factory method to create a strict JsObjSpec of thirteen mappings. Strict means that different
     * keys than the defined are not allowed
     *
     * @param key1   the first key
     * @param spec1  the mapping associated to the first key
     * @param key2   the second key
     * @param spec2  the mapping associated to the second key
     * @param key3   the third key
     * @param spec3  the mapping associated to the third key
     * @param key4   the fourth key
     * @param spec4  the mapping associated to the fourth key
     * @param key5   the fifth key
     * @param spec5  the mapping associated to the fifth key
     * @param key6   the sixth key
     * @param spec6  the mapping associated to the sixth key
     * @param key7   the seventh key
     * @param spec7  the mapping associated to the seventh key
     * @param key8   the eighth key
     * @param spec8  the mapping associated to the eighth key
     * @param key9   the ninth key
     * @param spec9  the mapping associated to the ninth key
     * @param key10  the tenth key
     * @param spec10 the mapping associated to the eleventh key
     * @param key11  the eleventh key
     * @param spec11 the mapping associated to the eleventh key
     * @param key12  the twelfth key
     * @param spec12 the mapping associated to the twelfth key,
     * @param key13  the thirteenth key
     * @param spec13 the mapping associated to the thirteenth key
     * @return a JsObjSpec
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
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key1),
                     requireNonNull(spec1));
        bindings.put(requireNonNull(key2),
                     requireNonNull(spec2));
        bindings.put(requireNonNull(key3),
                     requireNonNull(spec3));
        bindings.put(requireNonNull(key4),
                     requireNonNull(spec4));
        bindings.put(requireNonNull(key5),
                     requireNonNull(spec5));
        bindings.put(requireNonNull(key6),
                     requireNonNull(spec6));
        bindings.put(requireNonNull(key7),
                     requireNonNull(spec7));
        bindings.put(requireNonNull(key8),
                     requireNonNull(spec8));
        bindings.put(requireNonNull(key9),
                     requireNonNull(spec9));
        bindings.put(requireNonNull(key10),
                     requireNonNull(spec10));
        bindings.put(requireNonNull(key11),
                     requireNonNull(spec11));
        bindings.put(requireNonNull(key12),
                     requireNonNull(spec12));
        bindings.put(requireNonNull(key13),
                     requireNonNull(spec13));
        return new JsObjSpec(bindings,
                             false,
                             true
        );
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
