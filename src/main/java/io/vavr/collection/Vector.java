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
package io.vavr.collection;


import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.vavr.collection.Collections.withSize;


/**
 * Vector is the default Seq implementation that provides effectively constant time access to any element.
 * Many other operations (e.g. `tail`, `drop`, `slice`) are also effectively constant.
 * <p>
 * The implementation is based on a `bit-mapped trie`, a very wide and shallow tree (i.e. depth ≤ 6).
 *
 * @param <T> Component type of the Vector.
 */
public final class Vector<T> implements Iterable<T> {

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


    /**
     * Creates a Vector of the given elements.
     * <p>
     * The resulting vector has the same iteration order as the given iterable of elements
     * if the iteration order of the elements is stable.
     *
     * @param <T>      Component type of the Vector.
     * @param iterable An Iterable of elements.
     * @return A vector containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    public static <T> Vector<T> ofAll(Iterable<? extends T> iterable) {
        final Object[] values = withSize(iterable).toArray();
        return ofAll(BitMappedTrie.ofAll(values));
    }


    public Vector<T> append(T element) {
        return appendAll(List.of(element));
    }


    public Vector<T> appendAll(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (isEmpty()) {
            return ofAll(iterable);
        }
        if (Collections.isEmpty(iterable)) {
            return this;
        }
        return new Vector<>(trie.appendAll(iterable));
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
        if (!isEmpty()) {
            return get(0);
        } else {
            throw new NoSuchElementException("head of empty Vector");
        }
    }


    /**
     * Returns the element at the specified index.
     *
     * @param index an index
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    public T get(int index) {
        if (isDefinedAt(index)) {
            return apply(index);
        }
        throw new IndexOutOfBoundsException("get(" + index + ")");
    }


    public Vector<T> init() {
        if (!isEmpty()) {
            return dropRight(1);
        } else {
            throw new UnsupportedOperationException("init of empty Vector");
        }
    }


    public boolean isEmpty() {
        return length() == 0;
    }


    public boolean isTraversableAgain() {
        return true;
    }


    public Iterator<T> iterator() {
        return isEmpty() ? Iterator.empty()
                : trie.iterator();
    }


    public int length() {
        return trie.length();
    }


    public Vector<T> prepend(T element) {
        return prependAll(List.of(element));
    }


    public Vector<T> prependAll(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (isEmpty()) {
            return ofAll(iterable);
        }
        if (Collections.isEmpty(iterable)) {
            return this;
        }
        return new Vector<>(trie.prependAll(iterable));
    }


    public Vector<T> removeAt(int index) {
        if (isDefinedAt(index)) {
            final Vector<T> begin = take(index);
            final Vector<T> end = drop(index + 1);
            return (begin.length() > end.length())
                    ? begin.appendAll(end)
                    : end.prependAll(begin);
        } else {
            throw new IndexOutOfBoundsException("removeAt(" + index + ")");
        }
    }


    public Vector<T> tail() {
        if (!isEmpty()) {
            return drop(1);
        } else {
            throw new UnsupportedOperationException("tail of empty Vector");
        }
    }


    public Vector<T> take(int n) {
        return wrap(trie.take(n));
    }


    boolean isDefinedAt(Integer index) {
        return 0 <= index && index < length();
    }

    public Vector<T> update(int index, T element) {
        if (isDefinedAt(index)) {
            return wrap(trie.update(index, element));
        } else {
            throw new IndexOutOfBoundsException("update(" + index + ")");
        }
    }


    @Override
    public int hashCode() {
        return Collections.hashOrdered(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector<?> vector = (Vector<?>) o;
        return Collections.areEqual(this,vector);
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
        if (isEmpty()) {
            throw new NoSuchElementException("last of empty IndexedSeq");
        } else {
            return get(length() - 1);
        }
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
}


