package jsonvalues;

import org.checkerframework.checker.nullness.qual.KeyFor;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class MyJavaImpl
{
    private MyJavaImpl()
    {
    }

    static class Map implements MyMap<Map>
    {


        private java.util.Map<String, JsElem> elements;

        Map(final java.util.Map<String, JsElem> elements)
        {
            this.elements = elements;
        }

        Map()
        {
            this.elements = new HashMap<>();
        }

        @Override
        public Iterator<java.util.Map.Entry<String, JsElem>> iterator()
        {
            return elements.entrySet()
                           .iterator();
        }

        @Override
        public boolean contains(final String key)
        {
            return elements.containsKey(key);
        }

        @Override
        @SuppressWarnings("cast")
        //according checkerframework, the return type is keyfor(map), but the type of the interface can't be change
        public Set<String> fields()
        {
            return (Set<String>) elements.keySet();
        }

        @Override
        public JsElem get(final String key)
        {
            if (!elements.containsKey(key)) throw new NoSuchElementException("key " + key + " not found");
            return elements.get(key);
        }

        @Override
        public Optional<JsElem> getOptional(final String key)
        {
            return Optional.ofNullable(elements.get(key));
        }

        @Override
        public java.util.Map.Entry<String, JsElem> head()
        {
            if (this.isEmpty()) throw new UnsupportedOperationException("head of empty map");
            return elements.keySet()
                           .stream()
                           .findFirst()
                           .map(key -> new AbstractMap.SimpleEntry<>(key,
                                                                     elements.get(key)
                           ))
                           .orElseThrow(() -> new Error("Map not empty without a key!"));
        }

        @Override
        public boolean isEmpty()
        {
            return elements.isEmpty();
        }


        @Override
        public Map remove(final String key)
        {
            elements.remove(key);
            return this;
        }

        @Override
        public int size()
        {
            return elements.size();
        }

        @Override
        public String toString()
        {
            if (elements.isEmpty()) return EMPTY_OBJ_AS_STR;

            return elements.keySet()
                           .stream()
                           .map(key -> MAP_PAIR_TO_STR.apply(key,
                                                             elements.get(key)
                                                            )
                               )
                           .collect(Collectors.joining(COMMA,
                                                       OPEN_BRACKET,
                                                       CLOSE_BRACKET
                                                      )
                                   );

        }

        @Override
        public Map tail(final String head)
        {
            if (this.isEmpty()) throw new UnsupportedOperationException("tail of empty map");
            final java.util.Map<String, JsElem> tail = elements.keySet()
                                                               .stream()
                                                               .filter(key -> !key.equals(head))
                                                               .collect(Collectors.toMap(Function.identity(),
                                                                                         (@KeyFor("elements") String key) -> elements.get(key)
                                                                                        )
                                                                       );
            return new Map(tail);

        }

        @Override
        public Map update(final String key,
                          final JsElem je
                         )
        {
            elements.put(key,
                         je
                        );
            return this;
        }

        @Override
        public Map updateAll(final java.util.Map<String, JsElem> p_map)
        {
            elements.putAll(p_map);
            return this;
        }

        @Override
        public int hashCode()
        {
            return elements.hashCode();

        }

        @Override
        public boolean equals(final @Nullable Object obj)
        {
            return this.eq(obj);
        }
    }

    static class Vector implements MyVector<Vector>
    {

        private List<JsElem> elements;


        Vector(final List<JsElem> p_vector)
        {
            elements = p_vector;

        }

        Vector()
        {
            this.elements = new ArrayList<>();
        }


        @Override
        public Vector add(final Collection<? extends JsElem> list)
        {
            elements.addAll(list);
            return this;
        }

        @Override
        public Vector add(final Vector list)
        {
            elements.addAll(list.elements);
            return this;
        }

        @Override
        public Vector appendBack(final JsElem elem)
        {
            elements.add(elem);
            return this;
        }

        @Override
        public Vector appendFront(final JsElem elem)
        {
            elements.add(0,
                         elem
                        );
            return this;
        }

        @Override
        public boolean contains(final JsElem e)
        {
            return elements.contains(e);
        }

        @Override
        public JsElem get(final int index)
        {
            return elements.get(index);
        }

        @Override
        public int hashCode()
        {
            return elements.hashCode();

        }

        @Override
        public JsElem head()
        {
            if (isEmpty()) throw new UnsupportedOperationException("head of empty vector");
            return elements.get(0);
        }

        @Override
        public Vector init()
        {

            if (isEmpty()) throw new UnsupportedOperationException("init of empty vector");
            return new Vector(IntStream.range(0,
                                              elements.size() - 1
                                             )
                                       .mapToObj(i -> elements.get(i))
                                       .collect(Collectors.toList()));

        }

        @Override
        public boolean isEmpty()
        {
            return elements.isEmpty();
        }

        @Override
        public Iterator<JsElem> iterator()
        {
            return elements.iterator();
        }

        @Override
        public JsElem last()
        {
            if (isEmpty()) throw new UnsupportedOperationException("last of empty vector");
            return elements.get(size() - 1);
        }

        @Override
        public String toString()
        {
            if (elements.isEmpty()) return EMPTY_ARR_AS_STR;

            return elements.stream()
                           .map(JsElem::toString)
                           .collect(Collectors.joining(COMMA,
                                                       OPEN_BRACKET,
                                                       CLOSE_BRACKET
                                                      ));
        }

        @Override
        public Vector remove(final int index)
        {
            elements.remove(index);
            return this;
        }

        @Override
        public int size()
        {
            return elements.size();
        }

        @Override
        public Stream<JsElem> stream()
        {
            return elements.stream();
        }

        @Override
        public Vector tail()
        {
            if (isEmpty()) throw new UnsupportedOperationException("tail of empty vector");

            return new Vector(elements.stream()
                                      .skip(1)
                                      .collect(Collectors.toList()));

        }

        @Override
        public Vector update(final int index,
                             final JsElem ele
                            )
        {
            elements.set(index,
                         ele
                        );
            return this;
        }

        @Override
        public boolean equals(final @Nullable Object that)
        {
            return this.eq(that);
        }


    }
}
