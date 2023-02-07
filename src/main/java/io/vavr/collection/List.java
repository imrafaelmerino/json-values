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

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;



/**
 * An immutable {@code List} is an eager sequence of elements. Its immutability makes it suitable for concurrent programming.
 * <p>
 * A {@code List} is composed of a {@code head} element and a {@code tail} {@code List}.
 * <p>
 * There are two implementations of the {@code List} interface:
 *
 * <ul>
 * <li>{@link Nil}, which represents the empty {@code List}.</li>
 * <li>{@link Cons}, which represents a {@code List} containing one or more elements.</li>
 * </ul>
 *
 * A {@code List} is a {@code Stack} in the sense that it stores elements allowing a last-in-first-out (LIFO) retrieval.
 * <p>
 * Stack API:
 *
 * <ul>
 * <li>{@link #peek()}</li>
 * <li>{@link #peekOption()}</li>
 * <li>{@link #pop()}</li>
 * <li>{@link #popOption()}</li>
 * <li>{@link #pop2()}</li>
 * <li>{@link #pop2Option()}</li>
 * <li>{@link #push(Object)}</li>
 * <li>{@link #push(Object[])}</li>
 * <li>{@link #pushAll(Iterable)}</li>
 * </ul>
 *
 * Methods to obtain a {@code List}:
 *
 * <pre>
 * <code>
 * // factory methods
 * List.empty()                        // = List.of()
 * List.of(x)                          // e.g. List.of(1)
 * List.of(Object...)                  // e.g. List.of(1, 2, 3)
 * List.ofAll(Iterable)                // e.g. List.ofAll(Stream.of(1, 2, 3)) = 1, 2, 3
 * List.ofAll(&lt;primitive array&gt;) // e.g. List.of(new int[] {1, 2, 3}) = 1, 2, 3
 *
 * // int sequences
 * List.range(0, 3)              // = 0, 1, 2
 * List.rangeClosed(0, 3)        // = 0, 1, 2, 3
 * </code>
 * </pre>
 *
 * Note: A {@code List} is primarily a {@code Seq} and extends {@code Stack} for technical reasons (so {@code Stack} does not need to wrap {@code List}).
 * <p>
 * If operating on a {@code List}, please prefer
 *
 * <ul>
 * <li>{@link #prepend(Object)} over {@link #push(Object)}</li>
 * <li>{@link #prependAll(Iterable)} over {@link #pushAll(Iterable)}</li>
 * <li>{@link #tail()} over {@link #pop()}</li>
 * <li>{@link #tailOption()} over {@link #popOption()}</li>
 * </ul>
 *
 * Factory method applications:
 *
 * <pre>
 * <code>
 * List&lt;Integer&gt;       s1 = List.of(1);
 * List&lt;Integer&gt;       s2 = List.of(1, 2, 3);
 *                           // = List.of(new Integer[] {1, 2, 3});
 *
 * List&lt;int[]&gt;         s3 = List.ofAll(1, 2, 3);
 * List&lt;List&lt;Integer&gt;&gt; s4 = List.ofAll(List.of(1, 2, 3));
 *
 * List&lt;Integer&gt;       s5 = List.ofAll(1, 2, 3);
 * List&lt;Integer&gt;       s6 = List.ofAll(List.of(1, 2, 3));
 *
 * // cuckoo's egg
 * List&lt;Integer[]&gt;     s7 = List.&lt;Integer[]&gt; of(new Integer[] {1, 2, 3});
 * </code>
 * </pre>
 *
 * Example: Converting a String to digits
 *
 * <pre>
 * <code>
 * // = List(1, 2, 3)
 * List.of("123".toCharArray()).map(c -&gt; Character.digit(c, 10))
 * </code>
 * </pre>
 *
 * See Okasaki, Chris: <em>Purely Functional Data Structures</em> (p. 7 ff.). Cambridge, 2003.
 *
 * @param <T> Component type of the List
 */
public abstract class List<T> implements LinearSeq<T> {

    private static final long serialVersionUID = 1L;

    // sealed
    private List() {
    }

    /**
     * Returns a {@link Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(Collector)} to obtain a {@link List}.
     *
     * @param <T> Component type of the List.
     * @return a {@code Collector} which collects all the input elements into a
     * {@link List}, in encounter order
     */
    public static <T> Collector<T, ArrayList<T>, List<T>> collector() {
        return Collections.toListAndThen(List::ofAll);
    }

    /**
     * Returns the single instance of Nil.
     * <p>
     * Note: this method intentionally returns type {@code List} and not {@code Nil}. This comes handy when folding.
     *
     * @param <T> Component type of Nil, determined by type inference in the particular context.
     * @return The empty list.
     */
    public static <T> List<T> empty() {
        return Nil.instance();
    }



    @Override
    public abstract boolean isEmpty();



    /**
     * Narrows a widened {@code List<? extends T>} to {@code List<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param list A {@code List}.
     * @param <T>  Component type of the {@code List}.
     * @return the given {@code list} instance as narrowed type {@code List<T>}.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> narrow(List<? extends T> list) {
        return (List<T>) list;
    }

    /**
     * Returns a singleton {@code List}, i.e. a {@code List} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new List instance containing the given element
     */
    public static <T> List<T> of(T element) {
        return new Cons<>(element, Nil.instance());
    }

    /**
     * Creates a List of the given elements.
     *
     * <pre>{@code
     * List.of(1, 2, 3, 4)
     * }</pre>
     *
     * @param <T>      Component type of the List.
     * @param elements Zero or more elements.
     * @return A list containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    public static <T> List<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> result = Nil.instance();
        for (int i = elements.length - 1; i >= 0; i--) {
            result = result.prepend(elements[i]);
        }
        return result;
    }

    /**
     * Creates a List of the given elements.
     * <p>
     * The resulting list has the same iteration order as the given iterable of elements
     * if the iteration order of the elements is stable.
     *
     * @param <T>      Component type of the List.
     * @param elements An Iterable of elements.
     * @return A list containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> ofAll(Iterable<? extends T> elements) {

            List<T> result = Nil.instance();
            for (T element : elements) {
                result = result.prepend(element);
            }
            return result.reverse();

    }

    /**
     * Creates a List that contains the elements of the given {@link java.util.stream.Stream}.
     *
     * @param javaStream A {@link java.util.stream.Stream}
     * @param <T>        Component type of the Stream.
     * @return A List containing the given elements in the same order.
     */
    public static <T> List<T> ofAll(java.util.stream.Stream<? extends T> javaStream) {
        Objects.requireNonNull(javaStream, "javaStream is null");
        final java.util.Iterator<? extends T> iterator = javaStream.iterator();
        List<T> list = List.empty();
        while (iterator.hasNext()) {
            list = list.prepend(iterator.next());
        }
        return list.reverse();
    }

    /**
     * Creates a List from boolean values.
     *
     * @param elements boolean values
     * @return A new List of Boolean values
     * @throws NullPointerException if elements is null
     */
    public static List<Boolean> ofAll(boolean... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from byte values.
     *
     * @param elements byte values
     * @return A new List of Byte values
     * @throws NullPointerException if elements is null
     */
    public static List<Byte> ofAll(byte... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from char values.
     *
     * @param elements char values
     * @return A new List of Character values
     * @throws NullPointerException if elements is null
     */
    public static List<Character> ofAll(char... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from double values.
     *
     * @param elements double values
     * @return A new List of Double values
     * @throws NullPointerException if elements is null
     */
    public static List<Double> ofAll(double... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from float values.
     *
     * @param elements a float values
     * @return A new List of Float values
     * @throws NullPointerException if elements is null
     */
    public static List<Float> ofAll(float... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from int values.
     *
     * @param elements int values
     * @return A new List of Integer values
     * @throws NullPointerException if elements is null
     */
    public static List<Integer> ofAll(int... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from long values.
     *
     * @param elements long values
     * @return A new List of Long values
     * @throws NullPointerException if elements is null
     */
    public static List<Long> ofAll(long... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from short values.
     *
     * @param elements short values
     * @return A new List of Short values
     * @throws NullPointerException if elements is null
     */
    public static List<Short> ofAll(short... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Returns a List containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <T> Component type of the List
     * @param n   The number of elements in the List
     * @param f   The Function computing element values
     * @return A List consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    public static <T> List<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return Collections.tabulate(n, f, empty(), List::of);
    }

    /**
     * Returns a List containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param <T> Component type of the List
     * @param n   The number of elements in the List
     * @param s   The Supplier computing element values
     * @return A List of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    public static <T> List<T> fill(int n, Supplier<? extends T> s) {
        Objects.requireNonNull(s, "s is null");
        return Collections.fill(n, s, empty(), List::of);
    }

    /**
     * Returns a List containing {@code n} times the given {@code element}
     *
     * @param <T>     Component type of the List
     * @param n       The number of elements in the List
     * @param element The element
     * @return A List of size {@code n}, where each element is the given {@code element}.
     */
    public static <T> List<T> fill(int n, T element) {
        return Collections.fillObject(n, element, empty(), List::of);
    }

    public static List<Character> range(char from, char toExclusive) {
        return ofAll(Iterator.range(from, toExclusive));
    }





    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.range(0, 0)  // = List()
     * List.range(2, 0)  // = List()
     * List.range(-2, 2) // = List(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    public static List<Integer> range(int from, int toExclusive) {
        return ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeBy(1, 3, 1)  // = List(1, 2)
     * List.rangeBy(1, 4, 2)  // = List(1, 3)
     * List.rangeBy(4, 1, -2) // = List(4, 2)
     * List.rangeBy(4, 1, 2)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static List<Integer> rangeBy(int from, int toExclusive, int step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.range(0L, 0L)  // = List()
     * List.range(2L, 0L)  // = List()
     * List.range(-2L, 2L) // = List(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or the empty range if {@code from >= toExclusive}
     */
    public static List<Long> range(long from, long toExclusive) {
        return ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeBy(1L, 3L, 1L)  // = List(1L, 2L)
     * List.rangeBy(1L, 4L, 2L)  // = List(1L, 3L)
     * List.rangeBy(4L, 1L, -2L) // = List(4L, 2L)
     * List.rangeBy(4L, 1L, 2L)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static List<Long> rangeBy(long from, long toExclusive, long step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    public static List<Character> rangeClosed(char from, char toInclusive) {
        return ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    public static List<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }


    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosed(0, 0)  // = List(0)
     * List.rangeClosed(2, 0)  // = List()
     * List.rangeClosed(-2, 2) // = List(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    public static List<Integer> rangeClosed(int from, int toInclusive) {
        return ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosedBy(1, 3, 1)  // = List(1, 2, 3)
     * List.rangeClosedBy(1, 4, 2)  // = List(1, 3)
     * List.rangeClosedBy(4, 1, -2) // = List(4, 2)
     * List.rangeClosedBy(4, 1, 2)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static List<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosed(0L, 0L)  // = List(0L)
     * List.rangeClosed(2L, 0L)  // = List()
     * List.rangeClosed(-2L, 2L) // = List(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or the empty range if {@code from > toInclusive}
     */
    public static List<Long> rangeClosed(long from, long toInclusive) {
        return ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosedBy(1L, 3L, 1L)  // = List(1L, 2L, 3L)
     * List.rangeClosedBy(1L, 4L, 2L)  // = List(1L, 3L)
     * List.rangeClosedBy(4L, 1L, -2L) // = List(4L, 2L)
     * List.rangeClosedBy(4L, 1L, 2L)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static List<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Transposes the rows and columns of a {@link List} matrix.
     *
     * @param <T> matrix element type
     * @param matrix to be transposed.
     * @return a transposed {@link List} matrix.
     * @throws IllegalArgumentException if the row lengths of {@code matrix} differ.
     * <p>
     * ex: {@code
     * List.transpose(List(List(1,2,3), List(4,5,6))) → List(List(1,4), List(2,5), List(3,6))
     * }
     */
    public static <T> List<List<T>> transpose(List<List<T>> matrix) {
        return Collections.transpose(matrix, List::ofAll, List::of);
    }




    /**
     * Creates a list from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the list, otherwise {@code Some} {@code Tuple}
     * of the value to add to the resulting list and
     * the element for the next call.
     * <p>
     * Example:
     * <pre>
     * <code>
     * List.unfold(10, x -&gt; x == 0
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;&gt;(x-1, x)));
     * // List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
     * </code>
     * </pre>
     *
     * @param <T>  type of seeds and unfolded values
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return a list with the values built up by the iteration
     * @throws NullPointerException if {@code f} is null
     */
    public static <T> List<T> unfold(T seed, Function<? super T, Option<Tuple2<? extends T, ? extends T>>> f) {
        return Iterator.unfoldRight(seed, f.andThen(tupleOpt -> tupleOpt.map(Tuple2::swap)))
                .foldLeft(List.empty(), List::prepend);
    }

    @Override
    public final List<T> append(T element) {
        return foldRight(of(element), (x, xs) -> xs.prepend(x));
    }










    @Override
    public final List<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return this;
        } else {
            final List<T> filtered = foldLeft(empty(), (xs, x) -> predicate.test(x) ? xs.prepend(x) : xs);
            if (filtered.isEmpty()) {
                return empty();
            } else if (filtered.length() == length()) {
                return this;
            } else {
                return filtered.reverse();
            }
        }
    }



    @Override
    public final T apply(Integer index) {
        List<T> list = this;
        for (int i = index - 1; i >= 0; i--) {
            list = list.tail();
        }
        return list.head();
    }


    @Override
    public final boolean hasDefiniteSize() {
        return true;
    }



    @Override
    public abstract int length();

    @Deprecated
    @Override
    public boolean isDefinedAt(Integer index) {
        return index >= 0 && index < length();
    }

    @Override
    public final List<T> insert(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        List<T> preceding = Nil.instance();
        List<T> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("insert(" + index + ", e) on List of length " + length());
            }
            preceding = preceding.prepend(tail.head());
        }
        List<T> result = tail.prepend(element);
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }



    @Override
    public final boolean isTraversableAgain() {
        return true;
    }

    @Override
    public final T last() {
        return Collections.last(this);
    }



    @Override
    public final <U> List<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        List<U> list = empty();
        for (T t : this) {
            list = list.prepend(mapper.apply(t));
        }
        return list.reverse();
    }

    @Override
    public final List<T> orElse(Iterable<? extends T> other) {
        return isEmpty() ? ofAll(other) : this;
    }

    @Override
    public final List<T> orElse(Supplier<? extends Iterable<? extends T>> supplier) {
        return isEmpty() ? ofAll(supplier.get()) : this;
    }






    /**
     * Returns the head element without modifying the List.
     *
     * @return the first element
     * @throws NoSuchElementException if this List is empty
     * @deprecated use head() instead
     */
    @Deprecated
    public final T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("peek of empty list");
        }
        return head();
    }

    /**
     * Returns the head element without modifying the List.
     *
     * @return {@code None} if this List is empty, otherwise a {@code Some} containing the head element
     * @deprecated use headOption() instead
     */
    @Deprecated
    public final Option<T> peekOption() {
        return headOption();
    }

    /**
     * Performs an action on the head element of this {@code List}.
     *
     * @param action A {@code Consumer}
     * @return this {@code List}
     */
    @Override
    public final List<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return this;
    }


    /**
     * Removes the head element from this List.
     *
     * @return the elements of this List without the head element
     * @throws NoSuchElementException if this List is empty
     * @deprecated use tail() instead
     */
    @Deprecated
    public final List<T> pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("pop of empty list");
        }
        return tail();
    }

    /**
     * Removes the head element from this List.
     *
     * @return {@code None} if this List is empty, otherwise a {@code Some} containing the elements of this List without the head element
     * @deprecated use tailOption() instead
     */
    @Deprecated
    public final Option<List<T>> popOption() {
        return tailOption();
    }

    /**
     * Removes the head element from this List.
     *
     * @return a tuple containing the head element and the remaining elements of this List
     * @throws NoSuchElementException if this List is empty
     * @deprecated use Tuple.of(list.head(), list.tail()) instead
     */
    @Deprecated
    public final Tuple2<T, List<T>> pop2() {
        if (isEmpty()) {
            throw new NoSuchElementException("pop2 of empty list");
        }
        return Tuple.of(head(), tail());
    }

    /**
     * Removes the head element from this List.
     *
     * @return {@code None} if this List is empty, otherwise {@code Some} {@code Tuple} containing the head element and the remaining elements of this List
     * @deprecated use list.isEmpty() ? Option.none() : Option.some(Tuple.of(list.head(), list.tail())) instead
     */
    @Deprecated
    public final Option<Tuple2<T, List<T>>> pop2Option() {
        return Option.when(nonEmpty(), this::pop2);
    }

    @Override
    public final List<T> prepend(T element) {
        return new Cons<>(element, this);
    }

    @Override
    public final List<T> prependAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return isEmpty() ? ofAll(elements) : ofAll(elements).reverse().foldLeft(this, List::prepend);
    }

    /**
     * Pushes a new element on top of this List.
     *
     * @param element The new element
     * @return a new {@code List} instance, containing the new element on top of this List
     * @deprecated use prepend(T) instead
     */
    @Deprecated
    public final List<T> push(T element) {
        return prepend(element);
    }

    /**
     * Pushes the given elements on top of this List. A List has LIFO order, i.e. the last of the given elements is
     * the first which will be retrieved.
     *
     * @param elements Elements, may be empty
     * @return a new {@code List} instance, containing the new elements on top of this List
     * @throws NullPointerException if elements is null
     * @deprecated use prependAll(List.of(elements).reverse()) instead
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public final List<T> push(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> result = this;
        for (T element : elements) {
            result = result.prepend(element);
        }
        return result;
    }

    /**
     * Pushes the given elements on top of this List. A List has LIFO order, i.e. the last of the given elements is
     * the first which will be retrieved.
     *
     * @param elements An Iterable of elements, may be empty
     * @return a new {@code List} instance, containing the new elements on top of this List
     * @throws NullPointerException if elements is null
     * @deprecated use prependAll(List.of(elements).reverse()) instead
     */
    @Deprecated
    public final List<T> pushAll(Iterable<T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> result = this;
        for (T element : elements) {
            result = result.prepend(element);
        }
        return result;
    }



    @Override
    public final List<T> removeFirst(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        List<T> init = empty();
        List<T> tail = this;
        while (!tail.isEmpty() && !predicate.test(tail.head())) {
            init = init.prepend(tail.head());
            tail = tail.tail();
        }
        if (tail.isEmpty()) {
            return this;
        } else {
            return init.foldLeft(tail.tail(), List::prepend);
        }
    }




    @Override
    public final List<T> replace(T currentElement, T newElement) {
        List<T> preceding = Nil.instance();
        List<T> tail = this;
        while (!tail.isEmpty() && !Objects.equals(tail.head(), currentElement)) {
            preceding = preceding.prepend(tail.head());
            tail = tail.tail();
        }
        if (tail.isEmpty()) {
            return this;
        }
        // skip the current head element because it is replaced
        List<T> result = tail.tail().prepend(newElement);
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }




    @Override
    public final List<T> reverse() {
        return (length() <= 1) ? this : foldLeft(empty(), List::prepend);
    }









    @Override
    public final Tuple2<List<T>, List<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<T>, Iterator<T>> itt = iterator().span(predicate);
        return Tuple.of(ofAll(itt._1), ofAll(itt._2));
    }



    @Override
    public final String stringPrefix() {
        return "List";
    }





    @Override
    public abstract List<T> tail();

    @Override
    public final Option<List<T>> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }







    /**
     * Representation of the singleton empty {@code List}.
     *
     * @param <T> Component type of the List.
     * @deprecated will be removed from the public API
     */
    @Deprecated
    public static final class Nil<T> extends List<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private static final Nil<?> INSTANCE = new Nil<>();

        // hidden
        private Nil() {
        }

        /**
         * Returns the singleton instance of the linked list.
         *
         * @param <T> Component type of the List
         * @return the singleton instance of the linked list.
         */
        @SuppressWarnings("unchecked")
        public static <T> Nil<T> instance() {
            return (Nil<T>) INSTANCE;
        }

        @Override
        public T head() {
            throw new NoSuchElementException("head of empty list");
        }

        @Override
        public int length() {
            return 0;
        }




        @Override
        public LinearSeq<T> remove(T element) {
            return null;
        }

        @Override
        public LinearSeq<T> removeLast(Predicate<T> predicate) {
            return null;
        }

        @Override
        public Seq<? extends Seq<T>> group() {
            return null;
        }

        @Override
        public LinearSeq<T> removeAt(int index) {
            return null;
        }

        @Override
        public LinearSeq<T> removeAll(T element) {
            return null;
        }

        @Override
        public LinearSeq<T> removeAll(Iterable<? extends T> elements) {
            return null;
        }


        @Override
        public List<T> tail() {
            throw new UnsupportedOperationException("tail of empty list");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean equals(Object o) {
            return Collections.equals(this, o);
        }

        @Override
        public int hashCode() {
            return Collections.hashOrdered(this);
        }

        @Override
        public String toString() {
            return stringPrefix() + "()";
        }

        /**
         * Instance control for object serialization.
         *
         * @return The singleton instance of Nil.
         * @see Serializable
         */
        private Object readResolve() {
            return INSTANCE;
        }
    }

    /**
     * Non-empty {@code List}, consisting of a {@code head} and a {@code tail}.
     *
     * @param <T> Component type of the List.
     * @deprecated will be removed from the public API
     */
    @Deprecated
    // DEV NOTE: class declared final because of serialization proxy pattern (see Effective Java, 2nd ed., p. 315)
    public static final class Cons<T> extends List<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final T head;
        private final List<T> tail;
        private final int length;

        /**
         * Creates a List consisting of a head value and a trailing List.
         *
         * @param head The head
         * @param tail The tail
         */
        private Cons(T head, List<T> tail) {
            this.head = head;
            this.tail = tail;
            this.length = 1 + tail.length();
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public int length() {
            return length;
        }




        @Override
        public LinearSeq<T> remove(T element) {
            return null;
        }

        @Override
        public LinearSeq<T> removeLast(Predicate<T> predicate) {
            return null;
        }

        @Override
        public Seq<? extends Seq<T>> group() {
            return null;
        }

        @Override
        public LinearSeq<T> removeAt(int index) {
            return null;
        }

        @Override
        public LinearSeq<T> removeAll(T element) {
            return null;
        }

        @Override
        public LinearSeq<T> removeAll(Iterable<? extends T> elements) {
            return null;
        }


        @Override
        public List<T> tail() {
            return tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            return Collections.equals(this, o);
        }

        @Override
        public int hashCode() {
            return Collections.hashOrdered(this);
        }

        @Override
        public String toString() {
            return mkString(stringPrefix() + "(", ", ", ")");
        }

        /**
         * {@code writeReplace} method for the serialization proxy pattern.
         * <p>
         * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
         * an instance of the enclosing class.
         *
         * @return A SerializationProxy for this enclosing class.
         */
        private Object writeReplace() {
            return new SerializationProxy<>(this);
        }

        /**
         * {@code readObject} method for the serialization proxy pattern.
         * <p>
         * Guarantees that the serialization system will never generate a serialized instance of the enclosing class.
         *
         * @param stream An object serialization stream.
         * @throws InvalidObjectException This method will throw with the message "Proxy required".
         */
        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        /**
         * A serialization proxy which, in this context, is used to deserialize immutable, linked Lists with final
         * instance fields.
         *
         * @param <T> The component type of the underlying list.
         */
        // DEV NOTE: The serialization proxy pattern is not compatible with non-final, i.e. extendable,
        // classes. Also, it may not be compatible with circular object graphs.
        private static final class SerializationProxy<T> implements Serializable {

            private static final long serialVersionUID = 1L;

            // the instance to be serialized/deserialized
            private transient Cons<T> list;

            /**
             * Constructor for the case of serialization, called by {@link Cons#writeReplace()}.
             * <p/>
             * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
             * an instance of the enclosing class.
             *
             * @param list a Cons
             */
            SerializationProxy(Cons<T> list) {
                this.list = list;
            }

            /**
             * Write an object to a serialization stream.
             *
             * @param s An object serialization stream.
             * @throws IOException If an error occurs writing to the stream.
             */
            private void writeObject(ObjectOutputStream s) throws IOException {
                s.defaultWriteObject();
                s.writeInt(list.length());
                for (List<T> l = list; !l.isEmpty(); l = l.tail()) {
                    s.writeObject(l.head());
                }
            }

            /**
             * Read an object from a deserialization stream.
             *
             * @param s An object deserialization stream.
             * @throws ClassNotFoundException If the object's class read from the stream cannot be found.
             * @throws InvalidObjectException If the stream contains no list elements.
             * @throws IOException            If an error occurs reading from the stream.
             */
            private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
                s.defaultReadObject();
                final int size = s.readInt();
                if (size <= 0) {
                    throw new InvalidObjectException("No elements");
                }
                List<T> temp = Nil.instance();
                for (int i = 0; i < size; i++) {
                    @SuppressWarnings("unchecked")
                    final T element = (T) s.readObject();
                    temp = temp.prepend(element);
                }
                list = (Cons<T>) temp.reverse();
            }

            /**
             * {@code readResolve} method for the serialization proxy pattern.
             * <p>
             * Returns a logically equivalent instance of the enclosing class. The presence of this method causes the
             * serialization system to translate the serialization proxy back into an instance of the enclosing class
             * upon deserialization.
             *
             * @return A deserialized instance of the enclosing class.
             */
            private Object readResolve() {
                return list;
            }
        }
    }


    private interface SplitAt {

        static <T> Tuple2<List<T>, List<T>> splitByPredicateReversed(List<T> source, Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            List<T> init = Nil.instance();
            List<T> tail = source;
            while (!tail.isEmpty() && !predicate.test(tail.head())) {
                init = init.prepend(tail.head());
                tail = tail.tail();
            }
            return Tuple.of(init, tail);
        }
    }
}
