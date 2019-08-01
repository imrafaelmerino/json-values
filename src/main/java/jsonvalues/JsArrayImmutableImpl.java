package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static jsonvalues.Functions.accept;

class JsArrayImmutableImpl extends AbstractJsArray<MyScalaImpl.Vector, JsObj>
{
    public static final long serialVersionUID = 1L;

    static JsArrayImmutableImpl EMPTY = new JsArrayImmutableImpl(MyScalaImpl.Vector.EMPTY);
    private volatile int hascode;
    private volatile @Nullable String str;

    JsArrayImmutableImpl(final MyScalaImpl.Vector array)
    {
        super(array);
    }

    @Override
    AbstractJsArray<MyScalaImpl.Vector, JsObj> emptyArray()
    {
        return EMPTY;
    }

    @Override
    JsObj emptyObject()
    {
        return JsObjImmutableImpl.EMPTY;
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
    JsArray of(final MyScalaImpl.Vector vector)
    {
        return new JsArrayImmutableImpl(vector);
    }

    @Override
    public JsArray toImmutable()
    {
        return this;
    }

    public JsArray toMutable()
    {
        List<JsElem> acc = new ArrayList<>();
        array.forEach(accept(acc::add,
                            obj -> acc.add(obj.toMutable()),
                            arr -> acc.add(arr.toMutable())
                           ));
        return new JsArrayMutableImpl(new MyJavaImpl.Vector(acc));

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

    @Override
    public final String toString()
    {
        if (str != null) return str;
        str = super.toString();
        return str;

    }

    @Override
    public final JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return Functions.mapValues(this,
                                   requireNonNull(fn),
                                   it -> true,
                                   JsPath.empty()
                                         .index(-1)
                                  )
                        .get();

    }

    @Override
    public JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn,
                            final Predicate<? super JsPair> predicate
                           )
    {
        return Functions.mapValues(this,
                                   requireNonNull(fn),
                                   predicate,
                                   Functions.MINUS_ONE_INDEX
                                  )
                        .get();
    }

    @Override
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return Functions.mapValues_(this,
                                    requireNonNull(fn),
                                    it -> true,
                                    JsPath.empty()
                                          .index(-1)
                                   )
                        .get();
    }

    @Override
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return Functions.mapValues_(this,
                                    requireNonNull(fn),
                                    predicate,
                                    JsPath.empty()
                                          .index(-1)
                                   )
                        .get();
    }


    @Override
    public final JsArray mapKeys(final Function<? super JsPair, String> fn)
    {
        return this;
    }

    @Override
    public final JsArray mapKeys(final Function<? super JsPair, String> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return this;
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn)
    {
        return Functions.mapKeys_(this,
                                  requireNonNull(fn),
                                  it -> true,
                                  JsPath.empty()
                                        .index(-1)
                                 )
                        .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn,
                                  final Predicate<? super JsPair> predicate
                                 )
    {
        return Functions.mapKeys_(this,
                                  requireNonNull(fn),
                                  requireNonNull(predicate),
                                  JsPath.empty()
                                        .index(-1)
                                 )
                        .get();

    }

    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                 final BiPredicate<? super JsPath, ? super JsObj> predicate
                                )
    {

        return Functions.mapJsObj(this,
                                  requireNonNull(fn),
                                  requireNonNull(predicate),
                                  JsPath.empty()
                                        .index(-1)
                                 )
                        .get();

    }

    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return Functions.mapJsObj(this,
                                  requireNonNull(fn),
                                  (path, obj) -> true,
                                  JsPath.empty()
                                        .index(-1)
                                 )
                        .get();

    }

    @Override
    public final JsArray mapObjs_(final BiFunction<? super JsPath,? super  JsObj, JsObj> fn,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 )
    {

        return Functions.mapJsObj_(this,
                                   requireNonNull(fn),
                                   requireNonNull(predicate),
                                   JsPath.empty()
                                         .index(-1)
                                  )
                        .get();

    }

    @Override
    public final JsArray mapObjs_(final BiFunction<? super JsPath,? super JsObj, JsObj> fn)
    {
        return Functions.mapJsObj_(this,
                                   requireNonNull(fn),
                                   (p, obj) -> true,
                                   JsPath.empty()
                                         .index(-1)
                                  )
                        .get();

    }

    @Override
    public final JsArray filterElems(final Predicate<? super JsPair> filter)
    {
        return Functions.filterValues(this,
                                      requireNonNull(filter),
                                      JsPath.empty()
                                            .index(-1)
                                     )
                        .get();
    }

    @Override
    public final JsArray filterElems_(final Predicate<? super JsPair> filter)
    {


        return Functions.filterValues_(this,
                                       requireNonNull(filter),
                                       JsPath.empty()
                                             .index(-1)
                                      )
                        .get();

    }

    @Override
    public final JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return Functions.filterJsObjs(this,
                                      requireNonNull(filter),
                                      JsPath.empty()
                                            .index(-1)
                                     )
                        .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return Functions.filterJsObjs_(this,
                                       requireNonNull(filter),
                                       JsPath.empty()
                                             .index(-1)
                                      )
                        .get();

    }

    @Override
    public final JsArray filterKeys(final Predicate<? super JsPair> filter)
    {
        return this;
    }

    @Override
    public final JsArray filterKeys_(final Predicate<? super JsPair> filter)
    {
        return Functions.filterKeys_(this,
                                     requireNonNull(filter),
                                     JsPath.empty()
                                           .index(-1)
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
            array = ((JsArrayImmutableImpl) JsArray.parse(json)
                                                   .orElseThrow()).array;
        }
        catch (MalformedJson malformedJson)
        {
            throw new NotSerializableException(String.format("Error deserializing a string into the class %s: %s",
                                                             JsArray.class.getName(),
                                                             malformedJson.getMessage()
                                                            ));
        }

    }


}
