package jsonvalues;

import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.immutable.HashMap;
import scala.runtime.AbstractFunction1;

import java.util.AbstractMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

final class ScalaImmutableMap implements ImmutableMap
{
    private static final HashMap<String, JsElem> EMPTY_HASH_MAP = new HashMap<>();
    private final scala.collection.immutable.Map<String, JsElem> persistentMap;


    ScalaImmutableMap()
    {
        this.persistentMap = EMPTY_HASH_MAP;
    }

    ScalaImmutableMap(final scala.collection.immutable.Map<String, JsElem> map)
    {
        this.persistentMap = map;
    }

    static AbstractFunction1<String, String> af1(UnaryOperator<String> uo)
    {
        return new AbstractFunction1<String, String>()
        {
            @Override
            public String apply(final String str)
            {
                return uo.apply(str);
            }
        };
    }

    @Override
    public boolean contains(final String key)
    {
        return persistentMap.contains(key);
    }


    @Override
    public final Set<String> keys()
    {
        return JavaConverters.setAsJavaSet(persistentMap.keys()
                                                        .toSet());


    }
    @Override
    public JsElem get(final String key)
    {
        try
        {
            return persistentMap.apply(key);
        }
        catch (NoSuchElementException e)
        {
            throw InternalError.keyNotFound(key);
        }
    }

    @Override
    public Optional<JsElem> getOptional(final String key)
    {
        return persistentMap.contains(key) ? Optional.of(persistentMap.get(key)
                                                                      .get()) : Optional.empty();
    }

    @Override
    public int hashCode()
    {
        return ((HashMap<String, JsElem>) persistentMap).hashCode();
    }

    @Override
    public java.util.Map.Entry<String, JsElem> head()
    {
        if (this.isEmpty()) throw UserError.headOfEmptyObj();

        final Tuple2<String, JsElem> head = persistentMap.head();

        return new AbstractMap.SimpleEntry<>(head._1,
                                             head._2
        );
    }

    @Override
    public boolean isEmpty()
    {
        return persistentMap.isEmpty();
    }

    @Override
    public java.util.Iterator<java.util.Map.Entry<String, JsElem>> iterator()
    {
        final Iterator<Tuple2<String, JsElem>> iterator = persistentMap.iterator();
        return JavaConverters.asJavaIterator(iterator.map(it -> new AbstractMap.SimpleEntry<>(it._1,
                                                                                              it._2
                                                          )
                                                         ));
    }

    @Override
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaImmutableMap remove(final String key)
    {
        return new ScalaImmutableMap(((HashMap<String, JsElem>) persistentMap).$minus(key));
    }

    @Override
    public int size()
    {
        return persistentMap.size();
    }

    @Override
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaImmutableMap tail(String head)
    {
        if (this.isEmpty()) throw UserError.tailOfEmptyObj();
        return new ScalaImmutableMap(((HashMap<String, JsElem>) persistentMap).$minus(head));
    }

    @Override
    public String toString()
    {
        if (persistentMap.isEmpty()) return "{}";


        return persistentMap.keysIterator()
                            .map(af1(key -> String.format("\"%s\":%s",
                                                          key,
                                                          persistentMap.apply(key)
                                                         )))
                            .mkString("{",
                                      ",",
                                      "}"
                                     );
    }

    @Override
    public ScalaImmutableMap update(final String key,
                                    final JsElem e
                                   )
    {
        return new ScalaImmutableMap(persistentMap.updated(key,
                                                           e
                                                          ));
    }
}
