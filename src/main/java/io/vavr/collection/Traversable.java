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


import java.util.*;
import java.util.function.*;

/**
 * An interface for inherently recursive, multi-valued data structures. The order of elements is determined by
 * {@link Iterable#iterator()}, which may vary each time it is called.
 *
 * <p>
 * Basic operations:
 *
 * <ul>
 * <li>{@link #contains(Object)}</li>
 * <li>{@link #head()}</li>
 * <li>{@link #headOption()}</li>
 * <li>{@link #isEmpty()}</li>
 * <li>{@link #last()}</li>
 * <li>{@link #length()}</li>
 * <li>{@link #size()}</li>
 * <li>{@link #tail()}</li>
 * <li>{@link #tailOption()}</li>
 * </ul>
 *
 * Iteration:
 *
 * <ul>
 * <li>{@link #iterator()}</li>
 * </ul>
 *

 *
 * Reduction/Folding:
 *
 * <ul>
 * <li>{@link #foldLeft(Object, BiFunction)}</li>
 * <li>{@link #foldRight(Object, BiFunction)}</li>
 * <li>{@link #mkString(CharSequence, CharSequence, CharSequence)}</li>
 * </ul>
 *
 * Selection:
 *
 * <ul>
 * <li>{@link #filter(Predicate)}</li>
 * </ul>
 *
 * Tests:
 *
 * <ul>
 * <li>{@link #hasDefiniteSize()}</li>
 * <li>{@link #isDistinct()}</li>
 * <li>{@link #isOrdered()}</li>
 * <li>{@link #isSequential()}</li>
 * <li>{@link #isTraversableAgain()}</li>
 * </ul>
 *
 * Transformation:
 *
 * <ul>

 * <li>{@link #map(Function)}</li>
 * <li>{@link #replace(Object, Object)}</li>
 * <li>{@link #span(Predicate)}</li>
 * </ul>
 *
 * @param <T> Component type
 */
@SuppressWarnings("deprecation")
public interface Traversable<T> extends Iterable<T>,  Value<T> {







    /**
     * In Vavr there are four basic classes of collections:
     *
     * <ul>
     * <li>Seq (sequential elements)</li>
     * <li>Set (distinct elements)</li>
     * <li>Map (indexed elements)</li>
     * <li>Multimap (indexed collections)</li>
     * </ul>
     *
     * Two collection instances of these classes are equal if and only if both collections
     *
     * <ul>
     * <li>belong to the same basic collection class (Seq, Set, Map or Multimap)</li>
     * <li>contain the same elements</li>
     * <li>have the same element order, if the collections are of type Seq</li>
     * </ul>
     *
     * Two Map/Multimap elements, resp. entries, (key1, value1) and (key2, value2) are equal,
     * if the keys are equal and the values are equal.
     * <p>
     * <strong>Notes:</strong>
     *
     * <ul>
     * <li>No collection instance equals null, e.g. Queue(1) not equals null.</li>
     * <li>Nulls are allowed and handled as expected, e.g. List(null, 1) equals Stream(null, 1)
     * and HashMap((null, 1)) equals LinkedHashMap((null, 1)).
     * </li>
     * <li>The element order is taken into account for Seq only.
     * E.g. List(null, 1) not equals Stream(1, null)
     * and HashMap((null, 1), ("a", null)) equals LinkedHashMap(("a", null), (null, 1)).
     * The reason is, that we do not know which implementations we compare when having
     * two instances of type Map, Multimap or Set (see <a href="https://en.wikipedia.org/wiki/Liskov_substitution_principle">Liskov Substitution Principle</a>).</li>
     * <li>Other collection classes are equal if their types are equal and their elements are equal (in iteration order).</li>
     * <li>Iterator equality is defined to be object reference equality.</li>
     * </ul>
     *
     * @param obj an object, may be null
     * @return true, if this collection equals the given object according to the rules described above, false otherwise.
     */
    boolean equals(Object obj);


    /**
     * Returns a new traversable consisting of all elements which satisfy the given predicate.
     *
     * @param predicate A predicate
     * @return a new traversable
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> filter(Predicate<? super T> predicate);








    /**
     * Folds this elements from the left, starting with {@code zero} and successively calling {@code combine}.
     * <p>
     * Example:
     *
     * <pre> {@code
     * // = "cba!"
     * List("a", "b", "c").foldLeft("!", (xs, x) -> x + xs)
     * } </pre>
     *
     * @param <U>     the type to fold over
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    default <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> combine) {
        Objects.requireNonNull(combine, "combine is null");
        U xs = zero;
        for (T x : this) {
            xs = combine.apply(xs, x);
        }
        return xs;
    }

    /**
     * Folds this elements from the right, starting with {@code zero} and successively calling {@code combine}.
     * <p>
     * Example:
     *
     * <pre> {@code
     * // = "!cba"
     * List("a", "b", "c").foldRight("!", (x, xs) -> xs + x)
     * } </pre>
     *
     * @param <U>     the type of the folded value
     * @param zero    A zero element to start with.
     * @param combine A function which combines elements.
     * @return a folded value
     * @throws NullPointerException if {@code combine} is null
     */
    <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> combine);



    /**
     * Gets the first value in iteration order if this {@code Traversable} is not empty, otherwise throws.
     *
     * @return the first value
     * @throws NoSuchElementException if this {@code Traversable} is empty.
     */
    @Override
    default T get() {
        return head();
    }




    /**
     * Checks if this Traversable is known to have a finite size.
     * <p>
     * This method should be implemented by classes only, i.e. not by interfaces.
     *
     * @return true, if this Traversable is known to have a finite size, false otherwise.
     */
    boolean hasDefiniteSize();

    /**
     * Returns the first element of a non-empty Traversable.
     *
     * @return The first element of this Traversable.
     * @throws NoSuchElementException if this is empty
     */
    T head();

    /**
     * Returns the first element of a non-empty Traversable as {@code Option}.
     *
     * @return {@code Some(element)} or {@code None} if this is empty.
     */
    default Option<T> headOption() {
        return isEmpty() ? Option.none() : Option.some(head());
    }

    /**
     * Returns the hash code of this collection.
     * <br>
     * We distinguish between two types of hashes, those for collections with predictable iteration order (like Seq) and those with arbitrary iteration order (like Set, Map and Multimap).
     * <br>
     * In all cases the hash of an empty collection is defined to be 1.
     * <br>
     * Collections with predictable iteration order are hashed as follows:
     *
     * <pre>{@code
     * int hash = 1;
     * for (T t : this) { hash = hash * 31 + Objects.hashCode(t); }
     * }</pre>
     *
     * Collections with arbitrary iteration order are hashed in a way such that the hash of a fixed number of elements is independent of their iteration order.
     *
     * <pre>{@code
     * int hash = 1;
     * for (T t : this) { hash += Objects.hashCode(t); }
     * }</pre>
     *
     * Please note that the particular hashing algorithms may change in a future version of Vavr.
     * <br>
     * Generally, hash codes of collections aren't cached in Vavr (opposed to the size/length).
     * Storing hash codes in order to reduce the time complexity would increase the memory footprint.
     * Persistent collections are built upon tree structures, it allows us to implement efficient memory sharing.
     * A drawback of tree structures is that they make it necessary to store collection attributes at each tree node (read: element).
     * <br>
     * The computation of the hash code is linear in time, i.e. O(n). If the hash code of a collection is re-calculated often,
     * e.g. when using a List as HashMap key, we might want to cache the hash code.
     * This can be achieved by simply using a wrapper class, which is not included in Vavr but could be implemented like this:
     *
     * <pre>{@code
     * public final class Hashed<K> {
     *
     *     private final K key;
     *     private final Lazy<Integer> hashCode;
     *
     *     public Hashed(K key) {
     *         this.key = key;
     *         this.hashCode = Lazy.of(() -> Objects.hashCode(key));
     *     }
     *
     *     public K key() {
     *         return key;
     *     }
     *
     *     &#64;Override
     *     public boolean equals(Object o) {
     *         if (o == key) {
     *             return true;
     *         } else if (key != null && o instanceof Hashed) {
     *             final Hashed that = (Hashed) o;
     *             return key.equals(that.key);
     *         } else {
     *             return false;
     *         }
     *     }
     *
     *     &#64;Override
     *     public int hashCode() {
     *         return hashCode.get();
     *     }
     *
     *     &#64;Override
     *     public String toString() {
     *         return "Hashed(" + (key == null ? "null" : key.toString()) + ")";
     *     }
     * }
     * }</pre>
     *
     * @return The hash code of this collection
     */
    int hashCode();



    /**
     * Checks if this Traversable may consist of distinct elements only.
     *
     * @return true if this Traversable may consist of distinct elements only, false otherwise.
     */
    default boolean isDistinct() {
        return false;
    }

    /**
     * Checks if this Traversable is empty.
     *
     * @return true, if this Traversable contains no elements, false otherwise.
     */
    @Override
    default boolean isEmpty() {
        return length() == 0;
    }

    /**
     * Checks if this Traversable is ordered
     *
     * @return true, if this Traversable is ordered, false otherwise.
     */
    default boolean isOrdered() {
        return false;
    }

    /**
     * Checks if the elements of this Traversable appear in encounter order.
     *
     * @return true, if the insertion order of elements is preserved, false otherwise.
     */
    default boolean isSequential() {
        return false;
    }

    /**
     * Each of Vavr's collections may contain more than one element.
     *
     * @return {@code false}
     */
    @Override
    default boolean isSingleValued() {
        return false;
    }

    /**
     * Checks if this Traversable can be repeatedly traversed.
     * <p>
     * This method should be implemented by classes only, i.e. not by interfaces.
     *
     * @return true, if this Traversable is known to be traversable repeatedly, false otherwise.
     */
    boolean isTraversableAgain();

    /**
     * An iterator by means of head() and tail(). Subclasses may want to override this method.
     *
     * @return A new Iterator of this Traversable elements.
     */
    @Override
    default Iterator<T> iterator() {
        final Traversable<T> that = this;
        return new Iterator<>() {

            Traversable<T> traversable = that;

            @Override
            public boolean hasNext() {
                return !traversable.isEmpty();
            }

            @Override
            public T next() {
                final T result = traversable.head();
                traversable = traversable.tail();
                return result;
            }
        };
    }

    /**
     * Dual of {@linkplain #head()}, returning the last element.
     *
     * @return the last element.
     * @throws NoSuchElementException if this is empty
     */
    T last();


    /**
     * Computes the number of elements of this Traversable.
     * <p>
     * Same as {@link #size()}.
     *
     * @return the number of elements
     */
    int length();

    /**
     * Maps the elements of this {@code Traversable} to elements of a new type preserving their order, if any.
     *
     * @param mapper A mapper.
     * @param <U>    Component type of the target Traversable
     * @return a mapped Traversable
     * @throws NullPointerException if {@code mapper} is null
     */
    @Override
    <U> Traversable<U> map(Function<? super T, ? extends U> mapper);



    /**
     * Joins the string representations of this elements using a specific delimiter, prefix and suffix.
     * <p>
     * Example: {@code List.of("a", "b", "c").mkString("Chars(", ", ", ")") = "Chars(a, b, c)"}
     *
     * @param prefix    prefix of the resulting string
     * @param delimiter A delimiter string put between string representations of elements of this
     * @param suffix    suffix of the resulting string
     * @return a new String
     */
    default String mkString(CharSequence prefix, CharSequence delimiter, CharSequence suffix) {
        final StringBuilder builder = new StringBuilder(prefix);
        iterator().map(String::valueOf).intersperse(String.valueOf(delimiter)).forEach(builder::append);
        return builder.append(suffix).toString();
    }


    /**
     * Returns this {@code Traversable} if it is nonempty, otherwise return the alternative.
     *
     * @param other An alternative {@code Traversable}
     * @return this {@code Traversable} if it is nonempty, otherwise return the alternative.
     */
    Traversable<T> orElse(Iterable<? extends T> other);

    /**
     * Returns this {@code Traversable} if it is nonempty, otherwise return the result of evaluating supplier.
     *
     * @param supplier An alternative {@code Traversable} supplier
     * @return this {@code Traversable} if it is nonempty, otherwise return the result of evaluating supplier.
     */
    Traversable<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);


    @Override
    Traversable<T> peek(Consumer<? super T> action);







    /**
     * Replaces the first occurrence (if exists) of the given currentElement with newElement.
     *
     * @param currentElement An element to be substituted.
     * @param newElement     A replacement for currentElement.
     * @return a Traversable containing all elements of this where the first occurrence of currentElement is replaced with newElement.
     */
    Traversable<T> replace(T currentElement, T newElement);




    /**
     * Computes the number of elements of this Traversable.
     * <p>
     * Same as {@link #length()}.
     *
     * @return the number of elements
     */
    default int size() {
        return length();
    }


    /**
     * Returns a tuple where the first element is the longest prefix of elements that satisfy the given
     * {@code predicate} and the second element is the remainder.
     *
     * @param predicate A predicate.
     * @return a {@code Tuple} containing the longest prefix of elements that satisfy p and the remainder.
     * @throws NullPointerException if {@code predicate} is null
     */
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> span(Predicate<? super T> predicate);

    @Override
    default Spliterator<T> spliterator() {
        int characteristics = Spliterator.IMMUTABLE;
        if (isDistinct()) {
            characteristics |= Spliterator.DISTINCT;
        }
        if (isOrdered()) {
            characteristics |= (Spliterator.SORTED | Spliterator.ORDERED);
        }
        if (isSequential()) {
            characteristics |= Spliterator.ORDERED;
        }
        if (hasDefiniteSize()) {
            characteristics |= (Spliterator.SIZED | Spliterator.SUBSIZED);
            return Spliterators.spliterator(iterator(), length(), characteristics);
        } else {
            return Spliterators.spliteratorUnknownSize(iterator(), characteristics);
        }
    }



    /**
     * Drops the first element of a non-empty Traversable.
     *
     * @return A new instance of Traversable containing all elements except the first.
     * @throws UnsupportedOperationException if this is empty
     */
    Traversable<T> tail();

    /**
     * Drops the first element of a non-empty Traversable and returns an {@code Option}.
     *
     * @return {@code Some(traversable)} or {@code None} if this is empty.
     */
    Option<? extends Traversable<T>> tailOption();






}

