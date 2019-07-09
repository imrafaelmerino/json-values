package jsonvalues;

import org.checkerframework.checker.nullness.qual.KeyFor;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Functions.accept;

class JsObjMutableImpl extends AbstractJsObj<MyJavaImpl.Map, JsArrayMutableImpl>
{
    public static final long serialVersionUID = 1L;

    JsObjMutableImpl(final MyJavaImpl.Map map)
    {
        super(map);
    }

    JsObjMutableImpl()
    {
        super(new MyJavaImpl.Map());
    }

    @Override
    JsArrayMutableImpl emptyArray()
    {
        return new JsArrayMutableImpl(new MyJavaImpl.Vector());
    }

    @Override
    JsObj emptyObject()
    {
        return new JsObjMutableImpl(new MyJavaImpl.Map());
    }

    @Override
    public Iterator<Map.Entry<String, JsElem>> iterator()
    {
        return map.iterator();
    }

    @Override
    JsObj of(final MyJavaImpl.Map map)
    {
        return new JsObjMutableImpl(map);
    }

    @Override
    public JsObj toImmutable()
    {
        Map<String, JsElem> acc = new HashMap<>();
        final Set<@KeyFor("map") String> keys = (Set<@KeyFor("map") String>) map.fields();
        keys.forEach(key -> accept(val -> acc.put(key,
                                                  val
                                                 ),
                                   obj -> acc.put(key,
                                                  obj.toImmutable()
                                                 ),
                                   arr -> acc.put(key,
                                                  arr.toImmutable()
                                                 )
                                  ).accept(map.get(key))
                    );
        return new JsObjImmutableImpl(MyScalaImpl.Map.EMPTY.updateAll(acc));

    }

    @Override
    public JsObj toMutable()
    {
        return this;
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    @Override
    public final JsObj mapElems(final Function<JsPair, ? extends JsElem> fn)
    {

        return Functions._mapValues_(this,
                                     this,
                                     requireNonNull(fn),
                                     p -> true,
                                     JsPath.empty()
                                    )
                        .get();
    }

    @Override
    public final JsObj mapElems(final Function<JsPair, ? extends JsElem> fn,
                                final Predicate<JsPair> predicate
                               )
    {
        return Functions._mapValues_(this,
                                     this,
                                     requireNonNull(fn),
                                     predicate,
                                     JsPath.empty()
                                    )
                        .get();
    }

    @Override
    public final JsObj mapElems_(final Function<JsPair, ? extends JsElem> fn)
    {
        return Functions._mapValues__(this,
                                      this,
                                      requireNonNull(fn),
                                      p -> true,
                                      JsPath.empty()
                                     )
                        .get();
    }

    @Override
    public final JsObj mapElems_(final Function<JsPair, ? extends JsElem> fn,
                                 final Predicate<JsPair> predicate
                                )
    {
        return Functions._mapValues__(this,
                                      this,
                                      requireNonNull(fn),
                                      requireNonNull(predicate),
                                      JsPath.empty()
                                     )
                        .get();
    }


    @Override
    public final JsObj mapKeys(final Function<JsPair, String> fn)
    {
        return Functions._mapKeys_(this,
                                   this,
                                   requireNonNull(fn),
                                   p -> true,
                                   JsPath.empty()
                                  )
                        .get();
    }

    @Override
    public final JsObj mapKeys(final Function<JsPair, String> fn,
                               final Predicate<JsPair> predicate
                              )
    {
        return Functions._mapKeys_(this,
                                   this,
                                   requireNonNull(fn),
                                   requireNonNull(predicate),
                                   JsPath.empty()
                                  )
                        .get();
    }

    @Override
    public final JsObj mapKeys_(final Function<JsPair, String> fn)
    {
        return Functions._mapKeys__(this,
                                    this,
                                    requireNonNull(fn),
                                    p -> true,
                                    JsPath.empty()
                                   )
                        .get();

    }

    @Override
    public final JsObj mapKeys_(final Function<JsPair, String> fn,
                                final Predicate<JsPair> predicate
                               )
    {
        return Functions._mapKeys__(this,
                                    this,
                                    requireNonNull(fn),
                                    requireNonNull(predicate),
                                    JsPath.empty()
                                   )
                        .get();
    }

    @Override
    public final JsObj mapObjs(final BiFunction<JsPath, JsObj, JsObj> fn,
                               final BiPredicate<JsPath, JsObj> predicate
                              )
    {

        return Functions._mapJsObj_(this,
                                    this,
                                    requireNonNull(fn),
                                    requireNonNull(predicate),
                                    JsPath.empty()
                                   )
                        .get();
    }

    @Override
    public final JsObj mapObjs(final BiFunction<JsPath, JsObj, JsObj> fn)
    {
        return Functions._mapJsObj_(this,
                                    this,
                                    requireNonNull(fn),
                                    (p, o) -> true,
                                    JsPath.empty()
                                   )
                        .get();
    }

    @Override
    public final JsObj mapObjs_(final BiFunction<JsPath, JsObj, JsObj> fn,
                                final BiPredicate<JsPath, JsObj> predicate
                               )
    {
        return Functions._mapJsObj__(this,
                                     this,
                                     requireNonNull(fn),
                                     requireNonNull(predicate),
                                     JsPath.empty()
                                    )
                        .get();
    }

    @Override
    public final JsObj mapObjs_(final BiFunction<JsPath, JsObj, JsObj> fn)
    {
        return Functions._mapJsObj__(this,
                                     this,
                                     requireNonNull(fn),
                                     (p, o) -> true,
                                     JsPath.empty()
                                    )
                        .get();
    }

    @Override
    public JsObj filterElems(final Predicate<JsPair> filter)
    {
        return Functions._filterValues_(this,
                                        requireNonNull(filter),
                                        JsPath.empty()
                                       );

    }

    @Override
    public JsObj filterElems_(final Predicate<JsPair> filter)
    {
        return Functions._filterValues__(this,
                                         requireNonNull(filter),
                                         JsPath.empty()
                                        );
    }

    @Override
    public JsObj filterObjs(final BiPredicate<JsPath, JsObj> filter)
    {
        return Functions._filterJsObj_(this,
                                       requireNonNull(filter),
                                       JsPath.empty()
                                      );
    }

    @Override
    public JsObj filterObjs_(final BiPredicate<JsPath, JsObj> filter)
    {
        return Functions._filterJsObj__(this,
                                        requireNonNull(filter),
                                        JsPath.empty()
                                       );
    }

    @Override
    public final JsObj filterKeys(final Predicate<JsPair> filter)
    {
        return Functions._filterKeys_(this,
                                      requireNonNull(filter),
                                      JsPath.empty()
                                     );
    }

    @Override
    public final JsObj filterKeys_(final Predicate<JsPair> filter)
    {
        return Functions._filterKeys__(this,
                                       requireNonNull(filter),
                                       JsPath.empty()
                                      );

    }


    private void writeObject(ObjectOutputStream s) throws IOException
    {
        s.defaultWriteObject();
        s.writeObject(toString());

    }


    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
    {
        s.defaultReadObject();
        final String json = (String) s.readObject();
        try
        {
            map = ((JsObjMutableImpl) JsObj._parse_(json)
                                           .orElseThrow()).map;
        }
        catch (MalformedJson malformedJson)
        {
            throw new NotSerializableException(String.format("Error deserializing a string into the class %s: %s",
                                                             JsObj.class.getName(),
                                                             malformedJson.getMessage()
                                                            ));
        }

    }

    @Override
    public boolean isMutable()
    {
        return true;
    }

    @Override
    public boolean isImmutable()
    {
        return false;
    }

}
