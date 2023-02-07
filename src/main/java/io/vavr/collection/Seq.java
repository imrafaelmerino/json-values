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

import io.vavr.*;
import io.vavr.control.Option;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.*;

/**
 * Interface for immutable sequential data structures.
 * <p>
 * Basic operations:
 *
 * <ul>
 * <li>{@link #append(Object)}</li>
 * <li>{@link #insert(int, Object)}</li>
 * <li>{@link #prepend(Object)}</li>
 * <li>{@link #prependAll(Iterable)}</li>
 * </ul>
 *
 * Conversion:
 *
 * <ul>
 * </ul>
 *
 * Filtering:
 *
 * <ul>
 * <li>{@link #remove(Object)}</li>
 * <li>{@link #removeAll(Object)}</li>
 * <li>{@link #removeAll(Iterable)}</li>
 * <li>{@link #removeAt(int)}</li>
 * <li>{@link #removeFirst(Predicate)}</li>
 * <li>{@link #removeLast(Predicate)}</li>
 * </ul>
 *
 * Selection:
 *
 * <ul>
 * <li>{@link #get(int)}</li>
 * </ul>
 *
 * Transformation:
 *
 * <ul>
 * <li>{@link #reverse()}</li>
 * </ul>
 *
 * Traversal:
 *
 * <ul>
 * </ul>
 *
 * Views:
 *
 * <ul>
 * </ul>
 *
 * @param <T> Component type
 */
public interface Seq<T> extends Traversable<T>,  Serializable {

    long serialVersionUID = 1L;


    /**
     * Appends an element to this.
     *
     * @param element An element
     * @return A new Seq containing the given element appended to this elements
     */
    Seq<T> append(T element);



    /**
     * A {@code Seq} is a partial function which returns the element at the specified index if the
     * index is valid. It's up to the caller to make sure the index is valid (for instance through
     * {@code isDefinedAt}).
     * The behaviour is undefined if the index is out of bounds.
     * It may throw any {@code RuntimeException} or return an arbitrary value.
     *
     * @param index an index
     * @return the element at the given index
     */
    T apply(Integer index);
    


    /**
     * Returns the element at the specified index.
     *
     * @param index an index
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    default T get(int index) {
        if (isDefinedAt(index)) {
            return apply(index);
        }
        throw new IndexOutOfBoundsException("get(" + index + ")");
    }

    default boolean isDefinedAt(Integer index) {
        return 0 <= index && index < length();
    }





    /**
     * Inserts the given element at the specified index.
     *
     * @param index   an index
     * @param element an element
     * @return a new Seq, where the given element is inserted into this at the given index
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    Seq<T> insert(int index, T element);





    /**
     * Turns this sequence into a plain function returning an Option result.
     *
     * @return a function that takes an index i and returns the value of
     * this sequence in a Some if the index is within bounds, otherwise a None.
     * @deprecated Will be removed
     */
    @Deprecated
    default Function1<Integer, Option<T>> lift() {
        return i -> (i >= 0 && i < length()) ? Option.some(apply(i)) : Option.none();
    }




    /**
     * Prepends an element to this.
     *
     * @param element An element
     * @return A new Seq containing the given element prepended to this elements
     */
    Seq<T> prepend(T element);

    /**
     * Prepends all given elements to this.
     *
     * @param elements An Iterable of elements
     * @return A new Seq containing the given elements prepended to this elements
     */
    Seq<T> prependAll(Iterable<? extends T> elements);

    /**
     * Removes the first occurrence of the given element.
     *
     * @param element An element to be removed from this Seq.
     * @return a Seq containing all elements of this without the first occurrence of the given element.
     */
    Seq<T> remove(T element);

    /**
     * Removes all occurrences of the given element.
     *
     * @param element An element to be removed from this Seq.
     * @return a Seq containing all elements of this but not the given element.
     */
    Seq<T> removeAll(T element);

    /**
     * Removes all occurrences of the given elements.
     *
     * @param elements Elements to be removed from this Seq.
     * @return a Seq containing all elements of this but none of the given elements.
     * @throws NullPointerException if {@code elements} is null
     */
    Seq<T> removeAll(Iterable<? extends T> elements);

    /**
     * Removes the element at the specified position in this sequence. Shifts any subsequent elements to the left
     * (subtracts one from their indices).
     *
     * @param index position of element to remove
     * @return a sequence containing all elements of this without the element at the specified position.
     * @throws IndexOutOfBoundsException if this is empty, index &lt; 0 or index &gt;= length()
     */
    Seq<T> removeAt(int index);

    /**
     * Removes the first occurrence that satisfy predicate
     *
     * @param predicate an predicate
     * @return a new Seq
     */
    Seq<T> removeFirst(Predicate<T> predicate);

    /**
     * Removes the last occurrence that satisfy predicate
     *
     * @param predicate an predicate
     * @return a new Seq
     */
    Seq<T> removeLast(Predicate<T> predicate);

    /**
     * Reverses the order of elements.
     *
     * @return the reversed elements.
     */
    Seq<T> reverse();

    /**
     * An iterator yielding elements in reversed order.
     * <p>
     * Note: {@code xs.reverseIterator()} is the same as {@code xs.reverse().iterator()} but might
     * be more efficient.
     *
     * @return an iterator yielding the elements of this Seq in reversed order
     */
    Iterator<T> reverseIterator();




   




 




    @Override
    Seq<T> filter(Predicate<? super T> predicate);



    @Override
    default <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return reverse().foldLeft(zero, (xs, x) -> f.apply(x, xs));
    }

    /**
     * Groups subsequent equal elements.
     *
     * @return a new Seq of new instances containing the equal elements.
     */
    Seq<? extends Seq<T>> group();


    @Override
    <U> Seq<U> map(Function<? super T, ? extends U> mapper);

    @Override
    Seq<T> orElse(Iterable<? extends T> other);

    @Override
    Seq<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);



    @Override
    Seq<T> peek(Consumer<? super T> action);

    @Override
    Seq<T> replace(T currentElement, T newElement);







    @Override
    Tuple2<? extends Seq<T>, ? extends Seq<T>> span(Predicate<? super T> predicate);

    @Override
    Seq<T> tail();

    @Override
    Option<? extends Seq<T>> tailOption();



    @Override
    default boolean isSequential() {
        return true;
    }
}
