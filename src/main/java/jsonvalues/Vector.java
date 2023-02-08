/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jsonvalues;


import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * Vector is the default Seq implementation that provides effectively constant time access to any element.
 * Many other operations (e.g. `tail`, `drop`, `slice`) are also effectively constant.
 * <p>
 * The implementation is based on a `bit-mapped trie`, a very wide and shallow tree (i.e. depth ≤ 6).
 *
 * @param <T> Component type of the Vector.
 */
final class Vector<T> implements Iterable<T> {

    @SuppressWarnings("unchecked")
    private static final Vector<?> EMPTY = new Vector<>(BitMappedTrie.empty());

    final BitMappedTrie<T> trie;

    private Vector(BitMappedTrie<T> trie) {
        this.trie = trie;
    }

    @SuppressWarnings("ObjectEquality")
    private Vector<T> wrap(BitMappedTrie<T> trie) {
        return (trie == this.trie)
                ? this
                : ofAll(trie);
    }

    private static <T> Vector<T> ofAll(BitMappedTrie<T> trie) {
        return (trie.length() == 0)
                ? empty()
                : new Vector<>(trie);
    }

    /**
     * Returns the empty Vector.
     *
     * @param <T> Component type.
     * @return The empty Vector.
     */
    @SuppressWarnings("unchecked")
    public static <T> Vector<T> empty() {
        return (Vector<T>) EMPTY;
    }


    public Vector<T> append(T element) {
        return new Vector<>(trie.appendAll(Vector.of(element)));
    }


    public Vector<T> appendAll(Vector<T> vector) {
        return new Vector<>(trie.appendAll(vector));
    }


    public Vector<T> drop(int n) {
        return wrap(trie.drop(n));
    }


    public Vector<T> dropRight(int n) {
        return take(length() - n);
    }


    public T apply(Integer index) {
        return trie.get(index);
    }


    public T head() {
        return get(0);
    }


    /**
     * Returns the element at the specified index.
     *
     * @param index an index
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    public T get(int index) {
        return apply(index);
    }


    public Vector<T> init() {
            return dropRight(1);
    }


    public boolean isEmpty() {
        return length() == 0;
    }


    @Override
    public Iterator<T> iterator() {
        return trie.iterator();
    }


    public int length() {
        return trie.length();
    }


    public Vector<T> prepend(T element) {
        return prependAll(Vector.of(element));
    }

    /**
     * Returns a singleton {@code Vector}, i.e. a {@code Vector} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new Vector instance containing the given element
     */
    public static <T> Vector<T> of(T element) {
        return ofAll(BitMappedTrie.ofAll(new Object[]{element}));
    }

    public Vector<T> prependAll(Vector<T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (isEmpty()) {
            return iterable;
        }
        if (iterable.isEmpty()) {
            return this;
        }
        return new Vector<>(trie.prependAll(iterable));
    }

    Vector<T> reverse() {
        return (length() <= 1) ? this : foldLeft(empty(), Vector::prepend);
    }

    <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> combine) {
        Objects.requireNonNull(combine, "combine is null");
        U xs = zero;
        for (T x : this) {
            xs = combine.apply(xs, x);
        }
        return xs;
    }

    Iterator<T> reverseIterator() {
        return reverse().iterator();
    }


    public Vector<T> removeAt(int index) {
        final Vector<T> begin = take(index);
        final Vector<T> end = drop(index + 1);
        return (begin.length() > end.length())
                ? begin.appendAll(end)
                : end.prependAll(begin);

    }


    public Vector<T> tail() {
        return drop(1);

    }


    public Vector<T> take(int n) {
        return wrap(trie.take(n));
    }

    public Vector<T> update(int index, T element) {
        return wrap(trie.update(index, element));
    }


    @Override
    public int hashCode() {
        return hash(this, (acc, hash) -> acc * 31 + hash);
    }


    private int hash(Vector<T> iterable, IntBinaryOperator accumulator) {

        int hashCode = 1;
        for (var o : iterable) {
            hashCode = accumulator.applyAsInt(hashCode, Objects.hashCode(o));
        }
        return hashCode;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector<?> vector = (Vector<?>) o;
        final java.util.Iterator<?> iter1 = this.iterator();
        final java.util.Iterator<?> iter2 = vector.iterator();
        while (iter1.hasNext() && iter2.hasNext()) {
            if (!Objects.equals(iter1.next(), iter2.next())) {
                return false;
            }
        }
        return iter1.hasNext() == iter2.hasNext();
    }

    public int count(Predicate<T> o) {
        int n = 0;
        for (T t : this) {
            if (o.test(t)) {
                n += 1;
            }
        }
        return n;
    }

    public Stream<T> toJavaStream() {
        return StreamSupport.stream(spliterator(), false);
    }


    public T last() {
        return get(length() - 1);
    }

    public boolean contains(T element) {
        return exists(e -> Objects.equals(e, element));
    }

    public boolean exists(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (T t : this) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

    public <U> Vector<U> map(Function<T, U> map) {
        return new Vector<>(trie.map(map));
    }


}


