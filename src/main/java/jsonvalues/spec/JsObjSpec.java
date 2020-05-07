package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import com.dslplatform.json.parsers.JsSpecParsers;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import jsonvalues.*;

import java.util.*;

import static jsonvalues.spec.ERROR_CODE.*;


public class JsObjSpec implements JsSpec
{

  @Override
  public boolean isRequired()
  {
    return required;
  }

  final boolean strict;
  /**
   When this spec is associated to a key in another JsObjSpec, the required flag indicates whether or
   not the key is optional. If this JsObjSpec is the root of the spec, the flag doesn't have
   any meaning
   */
  private final boolean required;
  private final boolean nullable;

  @Override
  public JsObjSpec optional()
  {
    return new JsObjSpec(bindings,
                         false,
                         nullable,
                         strict
    );
  }

  @Override
  public JsSpecParser parser()
  {
    Map<String, JsSpecParser> parsers = HashMap.empty();
    Vector<String> required = Vector.empty();
    for (final String key : bindings.keySet())
    {

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

  public Set<JsErrorPair> test(final JsObj obj)
  {
    return test(JsPath.empty(),
                obj);
  }

  @Override
  public JsObjSpec nullable()
  {
    return new JsObjSpec(bindings,
                         required,
                         true,
                         strict
    );
  }

  public JsObjSpec(final Map<String, JsSpec> bindings,
                   boolean required,
                   boolean nullable,
                   boolean strict
                  )
  {
    this.bindings = bindings;
    this.required = required;
    this.nullable = nullable;
    this.strict = strict;
  }

  Map<String, JsSpec> bindings = HashMap.empty();


  private Set<JsErrorPair> test(final JsPath parent,
                                final JsObjSpec parentObjSpec,
                                final Set<JsErrorPair> errors,
                                final JsValue parentValue
                               )
  {

    if (parentValue.isNull() && nullable) return errors;
    if (!parentValue.isObj())
    {
      errors.add(JsErrorPair.of(parent,
                                new Error(parentValue,
                                          OBJ_EXPECTED)));
      return errors;
    }
    JsObj json = parentValue.toJsObj();
    for (final Tuple2<String, JsValue> next : json)
    {
      final String key = next._1;
      final JsValue value = next._2;
      final JsPath keyPath = JsPath.fromKey(key);
      final JsPath currentPath = parent.append(keyPath);
      final JsSpec spec = parentObjSpec.bindings.getOrElse(key,
                                                           null
                                                          );
      if (spec == null)
      {
        if (parentObjSpec.strict)
        {
          errors.add(JsErrorPair.of(currentPath,
                                    new Error(value,
                                              SPEC_MISSING
                                    )
                                   ));
        }
      } else errors.addAll(spec.test(currentPath,
                                     value));

    }
    final Seq<String> requiredFields = parentObjSpec.bindings.filter((key, spec) -> spec.isRequired())
                                                             .map(p -> p._1);
    for (final String requiredField : requiredFields)
    {
      if (!json.containsKey(requiredField)) errors.add(JsErrorPair.of(parent.key(requiredField),
                                                                      new Error(JsNothing.NOTHING,
                                                                                REQUIRED
                                                                      )
                                                                     )
                                                      );
    }


    return errors;
  }

  @Override
  public Set<JsErrorPair> test(final JsPath parentPath,
                               final JsValue value
                              )
  {
    return test(parentPath,
                this,
                new HashSet<>(),
                value);
  }

  @SafeVarargs
  public static JsObjSpec strict(final Tuple2<String,JsSpec> pair,
                                 final Tuple2<String,JsSpec>... others
                                )
  {
    return new JsObjSpec(true,
                         true,
                         false,
                         pair,
                         others
    );
  }

  @SafeVarargs
  public static JsObjSpec lenient(final Tuple2<String,JsSpec> pair,
                                  final Tuple2<String,JsSpec>... others
                                 )
  {
    return new JsObjSpec(false,
                         true,
                         false,
                         pair,
                         others
    );
  }

  @SafeVarargs
  private JsObjSpec(final boolean strict,
                    final boolean required,
                    final boolean nullable,
                    final Tuple2<String,JsSpec> pair,
                    final Tuple2<String,JsSpec>... others
                   )
  {
    bindings = bindings.put(pair._1,
                            pair._2
                           );
    for (Tuple2<String,JsSpec> p : others)
      bindings = bindings.put(p._1,
                              p._2
                             );
    this.strict = strict;
    this.required = required;
    this.nullable = nullable;

  }

  public static JsObjSpec strict(final String key,
                                 final JsSpec spec
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         true,
                         true,
                         false
    );
  }

  public static JsObjSpec lenient(final String key,
                                  final JsSpec spec
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         false,
                         true,
                         false
    );
  }

  private JsObjSpec(final String key,
                    final JsSpec spec,
                    final boolean strict,
                    final boolean required,
                    final boolean nullable
                   )
  {
    bindings = bindings.put(key,
                            spec
                           );
    this.strict = strict;
    this.required = required;
    this.nullable = nullable;
  }

  public static JsObjSpec strict(final String key,
                                 final JsSpec spec,
                                 final String key1,
                                 final JsSpec spec1
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         true
    );
  }

  public static JsObjSpec lenient(final String key,
                                  final JsSpec spec,
                                  final String key1,
                                  final JsSpec spec1
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         false
    );
  }

  private JsObjSpec(final String key,
                    final JsSpec spec,
                    final String key1,
                    final JsSpec spec1,
                    final boolean strict
                   )
  {
    this(key,
         spec,
         strict,
         true,
         false
        );
    bindings = bindings.put(key1,
                            spec1
                           );
  }

  public static JsObjSpec strict(final String key,
                                 final JsSpec spec,
                                 final String key1,
                                 final JsSpec spec1,
                                 final String key2,
                                 final JsSpec spec2
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         true
    );
  }

  public static JsObjSpec lenient(final String key,
                                  final JsSpec spec,
                                  final String key1,
                                  final JsSpec spec1,
                                  final String key2,
                                  final JsSpec spec2
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         false
    );
  }

  private JsObjSpec(String key,
                    JsSpec spec,
                    String key1,
                    JsSpec spec1,
                    String key2,
                    JsSpec spec2,
                    boolean strict
                   )
  {
    this(key,
         spec,
         key1,
         spec1,
         strict
        );
    bindings = bindings.put(key2,
                            spec2
                           );
  }

  public static JsObjSpec strict(final String key,
                                 final JsSpec spec,
                                 final String key1,
                                 final JsSpec spec1,
                                 final String key2,
                                 final JsSpec spec2,
                                 final String key3,
                                 final JsSpec spec3
                                )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         true
    );
  }

  public static JsObjSpec lenient(final String key,
                                  final JsSpec spec,
                                  final String key1,
                                  final JsSpec spec1,
                                  final String key2,
                                  final JsSpec spec2,
                                  final String key3,
                                  final JsSpec spec3
                                 )
  {
    return new JsObjSpec(key,
                         spec,
                         key1,
                         spec1,
                         key2,
                         spec2,
                         key3,
                         spec3,
                         false
    );
  }

  private JsObjSpec(final String key,
                    final JsSpec spec,
                    final String key1,
                    final JsSpec spec1,
                    final String key2,
                    final JsSpec spec2,
                    final String key3,
                    final JsSpec spec3,
                    final boolean strict
                   )
  {
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

  public static JsObjSpec strict(final String key,
                                 final JsSpec spec,
                                 final String key1,
                                 final JsSpec spec1,
                                 final String key2,
                                 final JsSpec spec2,
                                 final String key3,
                                 final JsSpec spec3,
                                 final String key4,
                                 final JsSpec spec4
                                )
  {
    return new JsObjSpec(key,
                         spec,
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

  public static JsObjSpec lenient(String key,
                                  JsSpec spec,
                                  String key1,
                                  JsSpec spec1,
                                  String key2,
                                  JsSpec spec2,
                                  String key3,
                                  JsSpec spec3,
                                  String key4,
                                  JsSpec spec4
                                 )
  {
    return new JsObjSpec(key,
                         spec,
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
                   )
  {
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

  public static JsObjSpec strict(String key,
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
                                 JsSpec spec5
                                )
  {
    return new JsObjSpec(key,
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
                         true
    );
  }

  public static JsObjSpec lenient(String key,
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
                                  JsSpec spec5
                                 )
  {
    return new JsObjSpec(key,
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
                         false
    );
  }

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
                   )
  {
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

  public static JsObjSpec strict(String key,
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
                                 JsSpec spec6
                                )
  {
    return new JsObjSpec(key,
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
                         true
    );
  }

  public static JsObjSpec lenient(String key,
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
                                  JsSpec spec6
                                 )
  {
    return new JsObjSpec(key,
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
                         false
    );
  }

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
                   )
  {
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

  public static JsObjSpec strict(String key,
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
                                 JsSpec spec7
                                )
  {
    return new JsObjSpec(key,
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
                         true
    );
  }

  public static JsObjSpec lenient(String key,
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
                                  JsSpec spec7
                                 )
  {
    return new JsObjSpec(key,
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
                         false
    );
  }

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
                   )
  {
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

  public static JsObjSpec strict(String key,
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
                                 JsSpec spec8
                                )
  {
    return new JsObjSpec(key,
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
                         true
    );
  }

  public static JsObjSpec lenient(String key,
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
                                  JsSpec spec8
                                 )
  {
    return new JsObjSpec(key,
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
                         false
    );
  }

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
                   )
  {
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

  public static JsObjSpec strict(String key,
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
                                 JsSpec spec9
                                )
  {
    return new JsObjSpec(key,
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
                         true
    );
  }

  public static JsObjSpec lenient(String key,
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
                                  JsSpec spec9
                                 )
  {
    return new JsObjSpec(key,
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
                         false
    );
  }

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
                   )
  {
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

  public static JsObjSpec strict(String key,
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
                                 String key10,
                                 JsSpec spec10
                                )
  {
    return new JsObjSpec(key,
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
                         key10,
                         spec10,
                         true
    );
  }

  public static JsObjSpec lenient(String key,
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
                                  String key10,
                                  JsSpec spec10
                                 )
  {
    return new JsObjSpec(key,
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
                         key10,
                         spec10,
                         false
    );
  }

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
                   )
  {
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


  public static JsObjSpec strict(final String key,
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
                                 final String key11,
                                 final JsSpec spec11
                                )
  {
    return new JsObjSpec(key,
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
                         key10,
                         spec10,
                         key11,
                         spec11,
                         true
    );
  }


  public static JsObjSpec lenient(final String key,
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
                                  final String key11,
                                  final JsSpec spec11
                                 )
  {
    return new JsObjSpec(key,
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
                         key10,
                         spec10,
                         key11,
                         spec11,
                         false
    );
  }

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
                    final String key11,
                    final JsSpec spec11,
                    final boolean strict
                   )
  {
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
         key10,
         spec10,
         strict
        );
    bindings = bindings.put(key11,
                            spec11
                           );
  }


}
