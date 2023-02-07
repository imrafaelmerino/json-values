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
package io.vavr;


import io.vavr.collection.Iterator;
import io.vavr.collection.*;
import io.vavr.control.Try;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.StreamSupport;


/**
 * Functional programming is all about values and transformation of values using functions. The {@code Value}
 * type reflects the values in a functional setting. It can be seen as the result of a partial function application.
 * Hence the result may be undefined. If a value is undefined, we say it is empty.
 * <p>
 * How the empty state is interpreted depends on the context, i.e. it may be <em>undefined</em>, <em>failed</em>,
 * <em>no elements</em>, etc.
 * <p>
 * Basic operations:
 *
 * <ul>
 * <li>{@link #get()}</li>
 * <li>{@link #getOrElse(Object)}</li>
 * <li>{@link #getOrElse(Supplier)}</li>
 * <li>{@link #getOrElseThrow(Supplier)}</li>
 * <li>{@link #map(Function)}</li>
 * <li>{@link #stringPrefix()}</li>
 * </ul>
 *
 * Equality checks:
 *
 * <ul>
 * <li>{@link #corresponds(Iterable, BiPredicate)}</li>
 * <li>{@link #eq(Object)}</li>
 * </ul>
 *
 * Iterable extensions:
 *
 * <ul>
 * <li>{@link #contains(Object)}</li>
 * <li>{@link #exists(Predicate)}</li>
 * <li>{@link #forEach(Consumer)}</li>
 * <li>{@link #iterator()}</li>
 * </ul>
 *
 * Side-effects:
 *
 * <ul>
 * <li>{@link #out(PrintStream)}</li>
 * <li>{@link #out(PrintWriter)}</li>
 * <li>{@link #peek(Consumer)}</li>
 * </ul>
 *
 * Tests:
 *
 * <ul>
 * <li>{@link #isEmpty()}</li>
 * <li>{@link #isSingleValued()}</li>
 * </ul>
 *
 * Type conversion:
 *
 * <ul>
 * <li>{@link #collect(Collector)}</li>
 * <li>{@link #collect(Supplier, BiConsumer, BiConsumer)}</li>
 * <li>{@link #toJavaArray()}</li>
 * <li>{@link #toJavaArray(Class)}</li>
 * <li>{@link #toJavaList()}</li>
 * <li>{@link #toJavaSet()}</li>
 * <li>{@link #toJavaStream()}</li>
 * <li>{@link #toList()}</li>
 * <li>{@link #toString()}</li>
 * </ul>
 *
 * <strong>Please note:</strong> flatMap signatures are manifold and have to be declared by subclasses of Value.
 *
 * @param <T> The type of the wrapped value.
 * @deprecated Marked for removal
 */
@Deprecated
public interface Value<T> extends Iterable<T> {


    /**
     * Collects the underlying value(s) (if present) using the provided {@code collector}.
     *
     * @param <A>       the mutable accumulation type of the reduction operation
     * @param <R>       the result type of the reduction operation
     * @param collector Collector performing reduction
     * @return R reduction result
     */
    default <R, A> R collect(Collector<? super T, A, R> collector) {
        return StreamSupport.stream(spliterator(), false).collect(collector);
    }

    /**
     * Collects the underlying value(s) (if present) using the given {@code supplier}, {@code accumulator} and
     * {@code combiner}.
     *
     * @param <R>         type of the result
     * @param supplier    provide unit value for reduction
     * @param accumulator perform reduction with unit value
     * @param combiner    function for combining two values, which must be
     *                    compatible with the accumulator.
     * @return R reduction result
     */
    default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
        return StreamSupport.stream(spliterator(), false).collect(supplier, accumulator, combiner);
    }

    /**
     * Shortcut for {@code exists(e -> Objects.equals(e, element))}, tests if the given {@code element} is contained.
     *
     * @param element An Object of type A, may be null.
     * @return true, if element is contained, false otherwise.
     */
    default boolean contains(T element) {
        return exists(e -> Objects.equals(e, element));
    }

    /**
     * Tests whether every element of this iterable relates to the corresponding element of another iterable by
     * satisfying a test predicate.
     *
     * @param <U>       Component type of that iterable
     * @param that      the other iterable
     * @param predicate the test predicate, which relates elements from both iterables
     * @return {@code true} if both iterables have the same length and {@code predicate(x, y)}
     * is {@code true} for all corresponding elements {@code x} of this iterable and {@code y} of {@code that},
     * otherwise {@code false}.
     */
    default <U> boolean corresponds(Iterable<U> that, BiPredicate<? super T, ? super U> predicate) {
        final java.util.Iterator<T> it1 = iterator();
        final java.util.Iterator<U> it2 = that.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            if (!predicate.test(it1.next(), it2.next())) {
                return false;
            }
        }
        return !it1.hasNext() && !it2.hasNext();
    }

    /**
     * A <em>smoothing</em> replacement for {@code equals}. It is similar to Scala's {@code ==} but better in the way
     * that it is not limited to collection types, e.g. {@code Some(1) eq List(1)}, {@code None eq Failure(x)} etc.
     * <p>
     * In a nutshell: eq checks <strong>congruence of structures</strong> and <strong>equality of contained values</strong>.
     * <p>
     * Example:
     *
     * <pre><code>
     * // ((1, 2), ((3))) =&gt; structure: (()(())) values: 1, 2, 3
     * final Value&lt;?&gt; i1 = List.of(List.of(1, 2), Arrays.asList(List.of(3)));
     * final Value&lt;?&gt; i2 = Queue.of(Stream.of(1, 2), List.of(Lazy.of(() -&gt; 3)));
     * assertThat(i1.eq(i2)).isTrue();
     * </code></pre>
     * <p>
     * Semantics:
     *
     * <pre><code>
     * o == this             : true
     * o instanceof Value    : iterable elements are eq, non-iterable elements equals, for all (o1, o2) in (this, o)
     * o instanceof Iterable : this eq Iterator.of((Iterable&lt;?&gt;) o);
     * otherwise             : false
     * </code></pre>
     *
     * @param o An object
     * @return true, if this equals o according to the rules defined above, otherwise false.
     */
    default boolean eq(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Value) {
            final Value<?> that = (Value<?>) o;
            return this.iterator().corresponds(that.iterator(), (o1, o2) -> {
                if (o1 instanceof Value) {
                    return ((Value<?>) o1).eq(o2);
                } else if (o2 instanceof Value) {
                    return ((Value<?>) o2).eq(o1);
                } else {
                    return Objects.equals(o1, o2);
                }
            });
        } else if (o instanceof Iterable) {
            final Value<?> that = Iterator.ofAll((Iterable<?>) o);
            return this.eq(that);
        } else {
            return false;
        }
    }

    /**
     * Checks, if an element exists such that the predicate holds.
     *
     * @param predicate A Predicate
     * @return true, if predicate holds for one or more elements, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean exists(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (T t : this) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }



    /**
     * Performs an action on each element.
     *
     * @param action A {@code Consumer}
     * @throws NullPointerException if {@code action} is null
     */
    @Override
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * Gets the underlying value or throws if no value is present.
     * <p>
     * <strong>IMPORTANT! This method will throw an undeclared {@link Throwable} if {@code isEmpty() == true} is true.</strong>
     * <p>
     * Because the 'empty' state indicates that there is no value present that can be returned,
     * {@code get()} has to throw in such a case. Generally, implementing classes should throw a
     * {@link NoSuchElementException} if {@code isEmpty()} returns true.
     * <p>
     * However, there exist use-cases, where implementations may throw other exceptions. See {@link Try#get()}.
     * <p>
     * <strong>Additional note:</strong> Dynamic proxies will wrap an undeclared exception in a {@link java.lang.reflect.UndeclaredThrowableException}.
     *
     * @return the underlying value if this is not empty, otherwise {@code get()} throws a {@code Throwable}
     */
    T get();
    
    /**
     * Returns the underlying value if present, otherwise {@code other}.
     *
     * @param other An alternative value.
     * @return A value of type {@code T}
     */
    default T getOrElse(T other) {
        return isEmpty() ? other : get();
    }

    /**
     * Returns the underlying value if present, otherwise {@code other}.
     *
     * @param supplier An alternative value supplier.
     * @return A value of type {@code T}
     * @throws NullPointerException if supplier is null
     */
    default T getOrElse(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isEmpty() ? supplier.get() : get();
    }

    /**
     * Returns the underlying value if present, otherwise throws {@code supplier.get()}.
     *
     * @param <X>      a Throwable type
     * @param supplier An exception supplier.
     * @return A value of type {@code T}.
     * @throws NullPointerException if supplier is null
     * @throws X                    if no value is present
     */
    default <X extends Throwable> T getOrElseThrow(Supplier<X> supplier) throws X {
        Objects.requireNonNull(supplier, "supplier is null");
        if (isEmpty()) {
            throw supplier.get();
        } else {
            return get();
        }
    }




    /**
     * Checks, this {@code Value} is empty, i.e. if the underlying value is absent.
     *
     * @return false, if no underlying value is present, true otherwise.
     */
    boolean isEmpty();



    /**
     * States whether this is a single-valued type.
     *
     * @return {@code true} if this is single-valued, {@code false} otherwise.
     */
    boolean isSingleValued();

    /**
     * Maps the underlying value to a different component type.
     *
     * @param mapper A mapper
     * @param <U>    The new component type
     * @return A new value
     */
    <U> Value<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Performs the given {@code action} on the first element if this is an <em>eager</em> implementation.
     * Performs the given {@code action} on all elements (the first immediately, successive deferred),
     * if this is a <em>lazy</em> implementation.
     *
     * @param action The action that will be performed on the element(s).
     * @return this instance
     */
    Value<T> peek(Consumer<? super T> action);

    /**
     * Returns the name of this Value type, which is used by toString().
     *
     * @return This type name.
     */
    String stringPrefix();

    // -- output

    /**
     * Sends the string representations of this to the {@link PrintStream}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @param out The PrintStream to write to
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stream.
     */
    default void out(PrintStream out) {
        for (T t : this) {
            out.println(t);
            if (out.checkError()) {
                throw new IllegalStateException("Error writing to PrintStream");
            }
        }
    }

    /**
     * Sends the string representations of this to the {@link PrintWriter}.
     * If this value consists of multiple elements, each element is displayed in a new line.
     *
     * @param writer The PrintWriter to write to
     * @throws IllegalStateException if {@code PrintWriter.checkError()} is true after writing to writer.
     */
    default void out(PrintWriter writer) {
        for (T t : this) {
            writer.println(t);
            if (writer.checkError()) {
                throw new IllegalStateException("Error writing to PrintWriter");
            }
        }
    }


    /**
     * Returns a rich {@code io.vavr.collection.Iterator}.
     *
     * @return A new Iterator
     */
    @Override
    Iterator<T> iterator();





    /**
     * Converts this to a Java array with component type {@code Object}
     *
     * <pre>{@code
     * // = [] of type Object[]
     * Future.<String> of(() -> { throw new Error(); })
     *       .toJavaArray()
     *
     * // = [ok] of type Object[]
     * Try.of(() -> "ok")
     *    .toJavaArray()
     *
     * // = [1, 2, 3] of type Object[]
     * List.of(1, 2, 3)
     *     .toJavaArray()
     * }</pre>
     *
     * @return A new Java array.
     */
    default Object[] toJavaArray() {
        if ((this instanceof Traversable<?>) && ((Traversable<?>) this).isTraversableAgain()) {
            final Object[] results = new Object[((Traversable<T>) this).size()];
            final Iterator<T> iter = iterator();
            Arrays.setAll(results, i -> iter.next());
            return results;

        } else {
            return toJavaList().toArray();
        }
    }

    /**
     * Converts this to a Java array having an accurate component type.
     *
     * <pre>{@code
     * // = [] of type String[]
     * Future.<String> of(() -> { throw new Error(); })
     *       .toJavaArray(String.class)
     *
     * // = [ok] of type String[]
     * Try.of(() -> "ok")
     *    .toJavaArray(String.class)
     *
     * // = [1, 2, 3] of type Integer[]
     * List.of(1, 2, 3)
     *     .toJavaArray(Integer.class)
     * }</pre>
     *
     * @param componentType Component type of the array
     * @return A new Java array.
     * @throws NullPointerException if componentType is null
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    default T[] toJavaArray(Class<T> componentType) {
        Objects.requireNonNull(componentType, "componentType is null");
        if (componentType.isPrimitive()) {
            final Class<?> boxedType =
                    componentType == boolean.class ? Boolean.class :
                    componentType == byte.class ? Byte.class :
                    componentType == char.class ? Character.class :
                    componentType == double.class ? Double.class :
                    componentType == float.class ? Float.class :
                    componentType == int.class ? Integer.class :
                    componentType == long.class ? Long.class :
                    componentType == short.class ? Short.class :
                    componentType == void.class ? Void.class : null;
            componentType = (Class<T>) boxedType;
        }
        final java.util.List<T> list = toJavaList();
        return list.toArray((T[]) java.lang.reflect.Array.newInstance(componentType, 0)); // toArray(new T[0]) is preferred over toArray(new T[size]) source: https://shipilev.net/blog/2016/arrays-wisdom-ancients/
    }


    /**
     * Converts this to a mutable {@link java.util.List}.
     * Elements are added by calling {@link java.util.List#add(Object)}.
     *
     * <pre>{@code
     * // = []
     * Future.<String> of(() -> { throw new Error(); })
     *       .toJavaList()
     * 
     * // = [ok]
     * Try.of(() -> "ok")
     *    .toJavaList()
     *
     * // = [1, 2, 3]
     * List.of(1, 2, 3)
     *     .toJavaList()
     * }</pre>
     *
     * @return A new {@link ArrayList}.
     */
    default java.util.List<T> toJavaList() {
        return ValueModule.toJavaCollection(this, ArrayList::new, 10);
    }




    /**
     * Converts this to a mutable {@link java.util.Set}.
     * Elements are added by calling {@link java.util.Set#add(Object)}.
     *
     * <pre>{@code
     * // = []
     * Future.of(() -> { throw new Error(); })
     *       .toJavaSet()
     * 
     * // = [ok]
     * Try.of(() -> "ok")
     *     .toJavaSet()
     *
     * // = [1, 2, 3]
     * List.of(1, 2, 3)
     *     .toJavaSet()
     * }</pre>
     *
     * @return A new {@link java.util.HashSet}.
     */
    default java.util.Set<T> toJavaSet() {
        return ValueModule.toJavaCollection(this, java.util.HashSet::new, 16);
    }


    /**
     * Converts this to a sequential {@link java.util.stream.Stream} by calling
     * {@code StreamSupport.stream(this.spliterator(), false)}.
     *
     * <pre>{@code
     * // empty Stream
     * Future.of(() -> { throw new Error(); })
     *       .toJavaStream()
     *
     * // Stream containing "ok"
     * Try.of(() -> "ok")
     *    .toJavaStream()
     *
     * // Stream containing 1, 2, 3
     * List.of(1, 2, 3)
     *     .toJavaStream()
     * }</pre>
     *
     * @return A new sequential {@link java.util.stream.Stream}.
     * @see Value#spliterator()
     */
    default java.util.stream.Stream<T> toJavaStream() {
        return StreamSupport.stream(spliterator(), false);
    }













    @Override
    default Spliterator<T> spliterator() {
        return Spliterators.spliterator(iterator(), isEmpty() ? 0 : 1,
                Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED);
    }

    // -- Object

    /**
     * Clarifies that values have a proper equals() method implemented.
     * <p>
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-">Object.equals(Object)</a>.
     *
     * @param o An object
     * @return true, if this equals o, false otherwise
     */
    @Override
    boolean equals(Object o);

    /**
     * Clarifies that values have a proper hashCode() method implemented.
     * <p>
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#hashCode--">Object.hashCode()</a>.
     *
     * @return The hashcode of this object
     */
    @Override
    int hashCode();

    /**
     * Clarifies that values have a proper toString() method implemented.
     * <p>
     * See <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#toString--">Object.toString()</a>.
     *
     * @return A String representation of this object
     */
    @Override
    String toString();

}

@SuppressWarnings("deprecation")
interface ValueModule {
    
    static <T, R extends Traversable<T>> R toTraversable(
            Value<T> value, R empty, Function<T, R> ofElement, Function<Iterable<T>, R> ofAll) {
        if (value.isEmpty()) {
            return empty;
        } else if (value.isSingleValued()) {
            return ofElement.apply(value.get());
        } else {
            return ofAll.apply(value);
        }
    }



    static <T, R extends Collection<T>> R toJavaCollection(
            Value<T> value, Function<Integer, R> containerSupplier, int defaultInitialCapacity) {
        final int size;
        if (value instanceof Traversable && ((Traversable) value).isTraversableAgain()) {
            size = ((Traversable) value).size();
        } else {
            size = defaultInitialCapacity;
        }
        final R container = containerSupplier.apply(size);
        value.forEach(container::add);
        return container;
    }
}
