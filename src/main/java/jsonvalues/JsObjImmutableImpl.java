package jsonvalues;

import org.checkerframework.checker.nullness.qual.KeyFor;
import org.checkerframework.checker.nullness.qual.Nullable;

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

class JsObjImmutableImpl extends AbstractJsObj<MyScalaImpl.Map, JsArray>
{
    public static final long serialVersionUID = 1L;
    static JsObjImmutableImpl EMPTY = new JsObjImmutableImpl(MyScalaImpl.Map.EMPTY);
    private transient volatile int hascode;
    private transient volatile @Nullable String str;


    JsObjImmutableImpl(final MyScalaImpl.Map myMap)
    {
        super(myMap);
    }

    @Override
    JsArray emptyArray()
    {
        return JsArrayImmutableImpl.EMPTY;
    }

    @Override
    AbstractJsObj<MyScalaImpl.Map, JsArray> emptyObject()
    {
        return EMPTY;
    }

    //equals method is inherited, so it's implemented. The purpose of this method is to cache
    //the hashcode once calculated. the object is immutable and it won't change
    @SuppressWarnings("squid:S1206")
    @Override
    public final int hashCode()
    {
        if (hascode != 0) return hascode;
        hascode = super.hashCode();
        return hascode;

    }

    @Override
    public Iterator<Map.Entry<String, JsElem>> iterator()
    {
        return map.iterator();
    }

    @Override
    AbstractJsObj<MyScalaImpl.Map, JsArray> of(final MyScalaImpl.Map map)
    {
        return new JsObjImmutableImpl(map);
    }

    @Override
    public JsObj toImmutable()
    {
        return this;
    }

    @Override
    public JsObj toMutable()
    {
        Map<String, JsElem> acc = new HashMap<>();
        final Set<@KeyFor("map") String> keys = (Set<@KeyFor("map") String>) map.fields();
        keys.forEach(key -> accept(val -> acc.put(key,
                                                  val
                                                 ),
                                   obj -> acc.put(key,
                                                  obj.toMutable()
                                                 ),
                                   arr -> acc.put(key,
                                                  arr.toMutable()
                                                 )
                                  ).accept(map.get(key))
                    );
        return new JsObjMutableImpl(new MyJavaImpl.Map(acc));

    }



    @Override
    public final String toString()
    {
        if (str != null) return str;
        str = super.toString();
        return str;

    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {

        return Functions.mapValues(this,
                                   requireNonNull(fn),
                                   p -> true,
                                   JsPath.empty()
                                  )
                        .get();
    }

    @Override
    public final JsObj mapElems(final Function<? super JsPair, ? extends JsElem> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return Functions.mapValues(this,
                                   requireNonNull(fn),
                                   predicate,
                                   JsPath.empty()
                                  )
                        .get();
    }

    @Override
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return Functions.mapValues_(this,
                                    requireNonNull(fn),
                                    p -> true,
                                    JsPath.empty()
                                   )
                        .get();
    }

    @Override
    public final JsObj mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return Functions.mapValues_(this,
                                    requireNonNull(fn),
                                    requireNonNull(predicate),
                                    JsPath.empty()
                                   )
                        .get();
    }

    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn)
    {
        return Functions.mapKeys(this,
                                 requireNonNull(fn),
                                 p -> true,
                                 JsPath.empty()
                                )
                        .get();
    }

    @Override
    public final JsObj mapKeys(final Function<? super JsPair, String> fn,
                               final Predicate<? super JsPair> predicate
                              )
    {
        return Functions.mapKeys(this,
                                 requireNonNull(fn),
                                 requireNonNull(predicate),
                                 JsPath.empty()
                                )
                        .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn)
    {
        return Functions.mapKeys_(this,
                                  requireNonNull(fn),
                                  p -> true,
                                  JsPath.empty()
                                 )
                        .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") // xx_ traverses the whole json
    public final JsObj mapKeys_(final Function<? super JsPair, String> fn,
                                final Predicate<? super JsPair> predicate
                               )
    {
        return Functions.mapKeys_(this,
                                  requireNonNull(fn),
                                  requireNonNull(predicate),
                                  JsPath.empty()
                                 )
                        .get();
    }

    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath,? super  JsObj, JsObj> fn,
                               final BiPredicate<? super JsPath, ? super JsObj> predicate
                              )
    {

        return Functions.mapJsObj(this,
                                  requireNonNull(fn),
                                  requireNonNull(predicate),
                                  JsPath.empty()
                                 )
                        .get();
    }

    @Override
    public final JsObj mapObjs(final BiFunction<? super JsPath,? super JsObj, JsObj> fn)
    {
        return Functions.mapJsObj(this,
                                  requireNonNull(fn),
                                  (path, obj) -> true,
                                  JsPath.empty()
                                 )
                        .get();
    }

    @Override
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                final BiPredicate<? super JsPath, ? super JsObj> predicate
                               )
    {


        return Functions.mapJsObj_(this,
                                   requireNonNull(fn),
                                   requireNonNull(predicate),
                                   JsPath.empty()
                                  )
                        .get();
    }

    @Override
    public final JsObj mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return Functions.mapJsObj_(this,
                                   requireNonNull(fn),
                                   (path, obj) -> true,
                                   JsPath.empty()
                                  )
                        .get();
    }

    @Override
    public final JsObj filterElems(final Predicate<? super JsPair> filter)
    {
        return Functions.filterValues(this,
                                      requireNonNull(filter),
                                      JsPath.empty()
                                     )
                        .get();
    }

    @Override
    public final JsObj filterElems_(final Predicate<? super JsPair> filter)
    {


        return Functions.filterValues_(this,
                                       requireNonNull(filter),
                                       JsPath.empty()
                                      )
                        .get();

    }

    @Override
    public final JsObj filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return Functions.filterJsObjs(this,
                                      requireNonNull(filter),
                                      JsPath.empty()
                                     )
                        .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsObj filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return Functions.filterJsObjs_(this,
                                       requireNonNull(filter),
                                       JsPath.empty()
                                      )
                        .get();

    }

    @Override
    public final JsObj filterKeys(final Predicate<? super JsPair> filter)
    {
        return Functions.filterKeys(this,
                                    requireNonNull(filter),
                                    JsPath.empty()
                                   )
                        .get();
    }

    @Override
    public final JsObj filterKeys_(final Predicate<? super JsPair> filter)
    {
        return Functions.filterKeys_(this,
                                     requireNonNull(filter),
                                     JsPath.empty()
                                    )
                        .get();

    }

    /**
     * Serialize this {@code ScalaJsObj} instance.
     *
     * @serialData The {@code String}) representation of this json object.
     */
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
            map = ((JsObjImmutableImpl) JsObj.parse(json)
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
        return false;
    }

    @Override
    public boolean isImmutable()
    {
        return true;
    }

}
