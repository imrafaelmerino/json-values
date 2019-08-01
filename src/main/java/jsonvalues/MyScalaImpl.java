package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.generic.CanBuildFrom;
import scala.collection.immutable.HashMap;
import scala.collection.mutable.Builder;
import scala.runtime.AbstractFunction1;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

class MyScalaImpl
{
    private MyScalaImpl()
    {
    }

    static final class Map implements MyMap<Map>
    {
        static final HashMap<String, JsElem> EMPTY_HASH_MAP = new HashMap<>();
        static final Map EMPTY = new Map();
        private final scala.collection.immutable.Map<String, JsElem> map;

        Map()
        {
            this.map = EMPTY_HASH_MAP;
        }

        Map(final scala.collection.immutable.Map<String, JsElem> map)
        {
            this.map = map;
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
        public java.util.Iterator<java.util.Map.Entry<String, JsElem>> iterator()
        {
            final Iterator<Tuple2<String, JsElem>> iterator = map.iterator();
            return JavaConverters.asJavaIterator(iterator.map(it -> new AbstractMap.SimpleEntry<>(it._1,
                                                                                                  it._2
                                                              )
                                                             ));
        }

        @Override
        public boolean contains(final String key)
        {
            return map.contains(key);
        }


        @Override
        public final Set<String> fields()
        {
            return JavaConverters.setAsJavaSet(map.keys()
                                                  .toSet());

        }

        @Override
        public JsElem get(final String key)
        {

            return map.apply(key);
        }

        @Override
        public Optional<JsElem> getOptional(final String key)
        {
            return map.contains(key) ? Optional.of(map.get(key)
                                                      .get()) : Optional.empty();
        }

        @Override
        public int hashCode()
        {
            return ((HashMap<String, JsElem>) map).hashCode();
        }

        @Override
        public java.util.Map.Entry<String, JsElem> head()
        {
            if (this.isEmpty()) throw new UnsupportedOperationException("head parse empty map");

            final Tuple2<String, JsElem> head = map.head();

            return new AbstractMap.SimpleEntry<>(head._1,
                                                 head._2
            );
        }

        @Override
        public boolean isEmpty()
        {
            return map.isEmpty();
        }

        @Override
        @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
        public Map remove(final String key)
        {
            return new Map(((HashMap<String, JsElem>) map).$minus(key));
        }

        @Override
        public int size()
        {
            return map.size();
        }

        @Override
        @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
        public Map tail(String head)
        {
            if (this.isEmpty()) throw new UnsupportedOperationException("tail of empty map");

            return new Map(((HashMap<String, JsElem>) map).$minus(head));

        }

        @Override
        public String toString()
        {
            if (map.isEmpty()) return EMPTY_OBJ_AS_STR;


            return map.keysIterator()
                      .map(af1(key -> MAP_PAIR_TO_STR.apply(key,
                                                            map.apply(key)
                                                           )))
                      .mkString(OPEN_BRACKET,
                                COMMA,
                                CLOSE_BRACKET
                               );
        }

        @Override
        public Map update(final String key,
                          final JsElem je
                         )
        {
            return new Map(map.updated(key,
                                       je
                                      ));
        }



        @Override
        public Map updateAll(final java.util.Map<String, JsElem> map)
        {
            scala.collection.immutable.Map<String, JsElem> immap = this.map;
            for (java.util.Map.Entry<String, JsElem> entry : map.entrySet())
                immap = immap.updated(entry.getKey(),
                                      entry.getValue()
                                     );
            return new Map(immap);
        }

        @Override
        public boolean equals(@Nullable Object obj)
        {
            return this.eq(obj);
        }
    }

    static final class Vector implements MyVector<Vector>
    {

        static final scala.collection.immutable.Vector<JsElem> EMPTY_VECTOR = new scala.collection.immutable.Vector<>(0,
                                                                                                                      0,
                                                                                                                      0
        );
        static final CanBuildFrom<scala.collection.immutable.Vector<JsElem>, JsElem, scala.collection.immutable.Vector<JsElem>> bf = new CanBuildFrom<scala.collection.immutable.Vector<JsElem>, JsElem, scala.collection.immutable.Vector<JsElem>>()
        {
            @Override
            public Builder<JsElem, scala.collection.immutable.Vector<JsElem>> apply()
            {
                return scala.collection.immutable.Vector.<JsElem>canBuildFrom().apply();
            }

            @Override
            public Builder<JsElem, scala.collection.immutable.Vector<JsElem>> apply(final scala.collection.immutable.Vector<JsElem> v)
            {
                return scala.collection.immutable.Vector.<JsElem>canBuildFrom().apply();
            }
        };

        static final Vector EMPTY = new Vector(EMPTY_VECTOR);
        private final scala.collection.immutable.Vector<JsElem> vector;


        Vector(final scala.collection.immutable.Vector<JsElem> vector)
        {
            this.vector = vector;
        }

        @Override
        public Vector add(final Collection<? extends JsElem> list)
        {
            scala.collection.immutable.Vector<JsElem> r = this.vector;
            for (final JsElem jsElem : list) r = r.appendBack(jsElem);
            return new Vector(r);
        }


        @Override
        @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
        public Vector add(final Vector that)
        {
            return new Vector(vector.$plus$plus(that.vector,
                                                bf
                                               ));
        }

        @Override
        public Vector appendBack(final JsElem elem)
        {
            return new Vector(vector.appendBack(elem));
        }

        @Override
        public Vector appendFront(final JsElem elem)
        {
            return new Vector(vector.appendFront(elem));
        }

        @Override
        public boolean contains(final JsElem e)
        {
            return vector.contains(e);
        }

        @Override
        public JsElem get(final int index)
        {
            return vector.apply(index);
        }

        @Override
        public int hashCode()
        {
            return vector.hashCode();
        }

        @Override
        public JsElem head()
        {
            return vector.head();
        }

        @Override
        public Vector init()
        {
            return new Vector(vector.init());
        }

        @Override
        public boolean isEmpty()
        {
            return vector.isEmpty();
        }

        @Override
        public java.util.Iterator<JsElem> iterator()
        {
            return JavaConverters.asJavaIterator(vector.iterator()
                                                       .toIterator());
        }

        @Override
        public JsElem last()
        {
            return vector.last();
        }

        @Override
        public String toString()
        {
            if (vector.isEmpty()) return EMPTY_ARR_AS_STR;

            return vector.mkString(OPEN_BRACKET,
                                   COMMA,
                                   CLOSE_BRACKET
                                  );
        }

        @Override
        @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
        public Vector remove(final int index)
        {


            if (index == 0) return new Vector(vector.tail());
            if (index == vector.size() - 1) return new Vector(vector.init());

            Tuple2<scala.collection.immutable.Vector<JsElem>, scala.collection.immutable.Vector<JsElem>> tuple = vector.splitAt(index);


            return new Vector(tuple._1.init()
                                      .$plus$plus(tuple._2,
                                                  bf
                                                 ));
        }

        @Override
        public int size()
        {
            return vector.size();
        }

        @Override
        public Stream<JsElem> stream()
        {
            return JavaConverters.seqAsJavaList(vector)
                                 .stream();
        }

        @Override
        public Vector tail()
        {
            return new Vector(vector.tail());
        }

        @Override
        public Vector update(final int index,
                             final JsElem ele
                            )
        {
            return new Vector(vector.updateAt(index,
                                              ele
                                             ));
        }

        @Override
        public boolean equals(final @Nullable Object that)
        {
            return this.eq(that);
        }
    }
}
