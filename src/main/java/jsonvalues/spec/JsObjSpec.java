package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import com.dslplatform.json.JsSpecParsers;
import fun.tuple.Pair;
import io.vavr.Tuple2;
import jsonvalues.JsNothing;
import jsonvalues.JsObj;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.*;
import java.util.function.Predicate;
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
    private final List<String> requiredFields;
    Map<String, JsSpec> bindings;
    Predicate<JsObj> predicate;

    public JsObjSpec lenient(){
        return new JsObjSpec(bindings,nullable,false,predicate,requiredFields);
    }

    private JsObjSpec(final Map<String, JsSpec> bindings,
                      boolean nullable,
                      boolean strict,
                      Predicate<JsObj> predicate,
                      List<String> requiredFields
    ) {
        for(String key: requiredFields){
            if(!bindings.containsKey(key))
                throw new IllegalArgumentException("required '"+key+"' not defined in spec");
        }

        this.bindings = bindings;
        this.nullable = nullable;
        this.strict = strict;
        this.predicate = predicate;
        this.requiredFields = requiredFields;
        this.parsers = new LinkedHashMap<>();
        for (Map.Entry<String, JsSpec> entry : bindings.entrySet())
            parsers.put(entry.getKey(),
                        entry.getValue().parser());
    }

    public static JsObjSpec of(final String key,
                               final JsSpec spec
    ) {
        Map<String, JsSpec> bindings = new LinkedHashMap<>();
        bindings.put(requireNonNull(key),
                     requireNonNull(spec));
        return new JsObjSpec(bindings,
                             false,
                             true,
                             null,
                             new ArrayList<>(bindings.keySet())
        );
    }



    public static JsObjSpec of(final String key1,
                               final JsSpec spec1,
                               final String key2,
                               final JsSpec spec2
    ) {

        return of(key1,
                  spec1).set(requireNonNull(key2),
                                 requireNonNull(spec2));

    }



    @SuppressWarnings("squid:S00107")
    public static JsObjSpec of(final String key1,
                               final JsSpec spec1,
                               final String key2,
                               final JsSpec spec2,
                               final String key3,
                               final JsSpec spec3
    ) {
        return of(key1,
                  spec1,
                  key2,
                  spec2).set(requireNonNull(key3),
                                 requireNonNull(spec3));

    }

    @SuppressWarnings("squid:S00107")
    public static JsObjSpec of(
            final String key1,
            final JsSpec spec1,
            final String key2,
            final JsSpec spec2,
            final String key3,
            final JsSpec spec3,
            final String key4,
            final JsSpec spec4
    ) {
        return of(key1,
                  spec1,
                  key2,
                  spec2,
                  key3,
                  spec3).set(requireNonNull(key4),
                                 requireNonNull(spec4));
    }


    @SuppressWarnings("squid:S00107")
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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
    public static JsObjSpec of(
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
        return of(key1,
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

    public JsObjSpec suchThat(final Predicate<JsObj> predicate) {
        return new JsObjSpec(bindings,
                             nullable,
                             strict,
                             predicate,
                             requiredFields);
    }

    public List<String> getRequiredFields() {
        return requiredFields;
    }

    public JsObjSpec withAllOptKeys() {
        return new JsObjSpec(bindings,
                             nullable,
                             strict,
                             predicate,
                             new ArrayList<>());
    }

    public JsObjSpec withOptKeys(final String field,
                                 final String... fields) {

        List<String> optionalFields = new ArrayList<>();
        optionalFields.add(field);
        optionalFields
                .addAll(Arrays.stream(requireNonNull(fields))
                              .collect(Collectors.toList()));
        return new JsObjSpec(bindings,
                             nullable,
                             strict,
                             predicate,
                             getRequiredFields(bindings.keySet(),
                                               optionalFields));
    }

    private List<String> getRequiredFields(Set<String> fields,
                                           List<String> optionals) {
        return fields.stream()
                     .filter(key -> !optionals.contains(key))
                     .collect(Collectors.toList());
    }

    public JsObjSpec withOptKeys(final List<String> optionals) {
        return new JsObjSpec(bindings,
                             nullable,
                             strict,
                             predicate,
                             getRequiredFields(bindings.keySet(),
                                               optionals));

    }

    @Override
    public JsObjSpec nullable() {
        return new JsObjSpec(bindings,
                             true,
                             strict,
                             predicate,
                             requiredFields
        );
    }


    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofObjSpec(requiredFields,
                                                parsers,
                                                predicate,
                                                nullable,
                                                strict
        );
    }

    @Override
    public Set<SpecError> test(final JsPath parentPath,
                               final JsValue value
    ) {
        return test(parentPath,
                    this,
                    new HashSet<>(),
                    value
        );
    }


    private Set<SpecError> test(final JsPath parent,
                                final JsObjSpec parentObjSpec,
                                final Set<SpecError> errors,
                                final JsValue parentValue
    ) {

        if (parentValue.isNull() && nullable) return errors;
        if (!parentValue.isObj()) {
            errors.add(SpecError.of(parent,
                                    Pair.of(parentValue,
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
                    errors.add(SpecError.of(currentPath,
                                            Pair.of(value,
                                                    SPEC_MISSING
                                            )
                    ));
                }
            } else errors.addAll(spec.test(currentPath,
                                           value));

        }

        for (final String requiredField : requiredFields) {
            if (!json.containsKey(requiredField))
                errors.add(SpecError.of(parent.key(requiredField),
                                        Pair.of(JsNothing.NOTHING,
                                                REQUIRED
                                        )
                           )
                );
        }

        if (predicate != null && !predicate.test(json))
            errors.add(SpecError.of(JsPath.empty(),
                                    Pair.of(json,
                                            OBJ_CONDITION)));


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
        LinkedHashMap<String, JsSpec> newBindings = new LinkedHashMap<>(this.bindings);
        newBindings.put(requireNonNull(key),
                        requireNonNull(spec)
        );
        return new JsObjSpec(newBindings,
                             this.nullable,
                             this.strict,
                             this.predicate,
                             new ArrayList<>(newBindings.keySet()));

    }
}
