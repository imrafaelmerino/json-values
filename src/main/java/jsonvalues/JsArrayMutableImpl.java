package jsonvalues;

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

class JsArrayMutableImpl extends AbstractJsArray<MyJavaImpl.Vector, JsObj>
{
    public static final long serialVersionUID = 1L;

    protected JsArrayMutableImpl()
    {
        super(new MyJavaImpl.Vector());
    }


    protected JsArrayMutableImpl(final MyJavaImpl.Vector array)
    {
        super(array);
    }

    @Override
    JsArray emptyArray()
    {
        return new JsArrayMutableImpl(new MyJavaImpl.Vector());
    }

    @Override
    JsObj emptyObject()
    {
        return new JsObjMutableImpl(new MyJavaImpl.Map());
    }

    @Override
    JsArray of(final MyJavaImpl.Vector vector)
    {
        return new JsArrayMutableImpl(vector);
    }

    @Override
    public JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return Functions._mapValues_(this,
                                     this,
                                     fn,
                                     p -> true,
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
        return Functions._mapValues_(this,
                                     this,
                                     requireNonNull(fn),
                                     predicate,
                                     JsPath.empty()
                                           .index(-1)
                                    )
                        .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return Functions._mapValues__(this,
                                      this,
                                      requireNonNull(fn),
                                      it -> true,
                                      JsPath.empty()
                                            .index(-1)
                                     )
                        .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return Functions._mapValues__(this,
                                      this,
                                      requireNonNull(fn),
                                      predicate,
                                      JsPath.empty()
                                            .index(-1)
                                     )
                        .get();
    }

    @Override
    public JsArray toImmutable()
    {
        List<JsElem> acc = new ArrayList<>();
        array.forEach(accept(acc::add,
                             obj -> acc.add(obj.toImmutable()),
                             arr -> acc.add(arr.toImmutable())
                            ));
        return new JsArrayImmutableImpl(MyScalaImpl.Vector.EMPTY.add(acc));

    }

    public JsArray toMutable(){
        return this;
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
        return Functions._mapKeys__(this,
                                    this,
                                    requireNonNull(fn),
                                    it -> true,
                                    JsPath.empty()
                                          .index(-1)
                                   )
                        .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn,
                                  final Predicate<? super JsPair> predicate
                                 )
    {
        return Functions._mapKeys__(this,
                                    this,
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

        return Functions._mapJsObj_(this,
                                    this,
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
        return Functions._mapJsObj_(this,
                                    this,
                                    requireNonNull(fn),
                                    (p, o) -> true,
                                    JsPath.empty()
                                          .index(-1)
                                   )
                        .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 )
    {

        return Functions._mapJsObj__(this,
                                     this,
                                     requireNonNull(fn),
                                     requireNonNull(predicate),
                                     JsPath.empty()
                                           .index(-1)
                                    )
                        .get();

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return Functions._mapJsObj__(this,
                                     this,
                                     requireNonNull(fn),
                                     (p, o) -> true,
                                     JsPath.empty()
                                           .index(-1)
                                    )
                        .get();

    }


    @Override
    public JsArray filterElems(final Predicate<? super JsPair> filter)
    {
        return Functions._filterValues_(this,
                                        requireNonNull(filter),
                                        JsPath.empty()
                                              .index(-1)

                                       );

    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterElems_(final Predicate<? super JsPair> filter)
    {
        return Functions._filterValues__(this,
                                         requireNonNull(filter),
                                         JsPath.empty()
                                               .index(-1)
                                        );
    }

    @Override
    public final JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return Functions._filterJsObj_(this,
                                       requireNonNull(filter),
                                       JsPath.empty()
                                             .index(-1)
                                      )
        ;
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return Functions._filterJsObj__(this,
                                        requireNonNull(filter),
                                        JsPath.empty()
                                              .index(-1)
                                       );


    }

    @Override
    public final JsArray filterKeys(final Predicate<? super JsPair> filter)
    {
        return this;
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterKeys_(final Predicate<? super JsPair> filter)
    {
        return Functions._filterKeys__(this,
                                       requireNonNull(filter),
                                       JsPath.empty()
                                             .index(-1)

                                      );

    }

    /**
     * Serialize this {@code JsArray} instance.
     *
     * @serialData The {@code String}) representation of this json array.
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
            array = ((JsArrayMutableImpl) JsArray._parse_(json)
                                                 .orElseThrow()).array;
        }
        catch (MalformedJson malformedJson)
        {
            throw new NotSerializableException(String.format("Error deserializing a string into the class %s: %s",
                                                             JsArrayMutableImpl.class.getName(),
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
