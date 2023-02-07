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

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.function.*;

/**
 * Interface for immutable, linear sequences.
 * <p>
 * Efficient {@code head()}, {@code tail()}, and {@code isEmpty()} methods are characteristic for linear sequences.
 *
 * @param <T> component type
 */
public interface LinearSeq<T> extends Seq<T> {

    long serialVersionUID = 1L;


    // -- Adjusted return types of Seq methods

    @Override
    LinearSeq<T> append(T element);

    @Override
    LinearSeq<T> appendAll(Iterable<? extends T> elements);

    @Override
    LinearSeq<T> asJava(Consumer<? super java.util.List<T>> action);

    @Override
    LinearSeq<T> asJavaMutable(Consumer<? super java.util.List<T>> action);

    @Override
    default PartialFunction<Integer, T> asPartialFunction() throws IndexOutOfBoundsException {
        return new PartialFunction<Integer, T>() {
            private static final long serialVersionUID = 1L;
            @Override
            public T apply(Integer index) {
                return get(index);
            }
            @Override
            public boolean isDefinedAt(Integer index) {
                // we can't use length() because of infinite long sequences
                return 0 <= index && drop(index).nonEmpty();
            }
        };
    }

    @Override
    <R> LinearSeq<R> collect(PartialFunction<? super T, ? extends R> partialFunction);

    @Override
    LinearSeq<? extends LinearSeq<T>> combinations();

    @Override
    LinearSeq<? extends LinearSeq<T>> combinations(int k);

    @Override
    Iterator<? extends LinearSeq<T>> crossProduct(int power);

    @Override
    LinearSeq<T> distinct();

    @Override
    LinearSeq<T> distinctBy(Comparator<? super T> comparator);

    @Override
    <U> LinearSeq<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    @Override
    LinearSeq<T> drop(int n);

    @Override
    LinearSeq<T> dropUntil(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> dropWhile(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> dropRight(int n);

    @Override
    LinearSeq<T> dropRightUntil(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> dropRightWhile(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> filter(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> filterNot(Predicate<? super T> predicate);

    @Override
    <U> LinearSeq<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);



    @Override
    Iterator<? extends LinearSeq<T>> grouped(int size);



    @Override
    default int indexWhere(Predicate<? super T> predicate, int from) {
        Objects.requireNonNull(predicate, "predicate is null");
        int i = from;
        LinearSeq<T> these = drop(from);
        while (!these.isEmpty()) {
            if (predicate.test(these.head())) {
                return i;
            }
            i++;
            these = these.tail();
        }
        return -1;
    }

    @Override
    LinearSeq<T> init();

    @Override
    Option<? extends LinearSeq<T>> initOption();

    @Override
    LinearSeq<T> insert(int index, T element);

    @Override
    LinearSeq<T> insertAll(int index, Iterable<? extends T> elements);

    @Override
    LinearSeq<T> intersperse(T element);





    @Override
    <U> LinearSeq<U> map(Function<? super T, ? extends U> mapper);

    @Override
    LinearSeq<T> orElse(Iterable<? extends T> other);

    @Override
    LinearSeq<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);

    @Override
    LinearSeq<T> padTo(int length, T element);

    @Override
    LinearSeq<T> patch(int from, Iterable<? extends T> that, int replaced);

    @Override
    Tuple2<? extends LinearSeq<T>, ? extends LinearSeq<T>> partition(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> peek(Consumer<? super T> action);

    @Override
    LinearSeq<? extends LinearSeq<T>> permutations();

    @Override
    LinearSeq<T> prepend(T element);

    @Override
    LinearSeq<T> prependAll(Iterable<? extends T> elements);

    @Override
    LinearSeq<T> remove(T element);

    @Override
    LinearSeq<T> removeFirst(Predicate<T> predicate);

    @Override
    LinearSeq<T> removeLast(Predicate<T> predicate);

    @Override
    LinearSeq<T> removeAt(int index);

    @Override
    LinearSeq<T> removeAll(T element);

    @Override
    LinearSeq<T> removeAll(Iterable<? extends T> elements);

    @Override
    LinearSeq<T> replace(T currentElement, T newElement);

    @Override
    LinearSeq<T> replaceAll(T currentElement, T newElement);

    @Override
    LinearSeq<T> retainAll(Iterable<? extends T> elements);

    @Override
    LinearSeq<T> reverse();

    @Override
    default Iterator<T> reverseIterator() {
        return reverse().iterator();
    }

    @Override
    LinearSeq<T> rotateLeft(int n);

    @Override
    LinearSeq<T> rotateRight(int n);

    @Override
    LinearSeq<T> shuffle();

    @Override
    LinearSeq<T> shuffle(Random random);

    @Override
    LinearSeq<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation);

    @Override
    <U> LinearSeq<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation);

    @Override
    <U> LinearSeq<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation);

    @Override
    default int segmentLength(Predicate<? super T> predicate, int from) {
        Objects.requireNonNull(predicate, "predicate is null");
        int i = 0;
        LinearSeq<T> these = this.drop(from);
        while (!these.isEmpty() && predicate.test(these.head())) {
            i++;
            these = these.tail();
        }
        return i;
    }

    @Override
    LinearSeq<T> slice(int beginIndex, int endIndex);

    @Override
    Iterator<? extends LinearSeq<T>> slideBy(Function<? super T, ?> classifier);

    @Override
    Iterator<? extends LinearSeq<T>> sliding(int size);

    @Override
    Iterator<? extends LinearSeq<T>> sliding(int size, int step);

    @Override
    LinearSeq<T> sorted();

    @Override
    LinearSeq<T> sorted(Comparator<? super T> comparator);

    @Override
    <U extends Comparable<? super U>> LinearSeq<T> sortBy(Function<? super T, ? extends U> mapper);

    @Override
    <U> LinearSeq<T> sortBy(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends LinearSeq<T>, ? extends LinearSeq<T>> span(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> subSequence(int beginIndex);

    @Override
    LinearSeq<T> subSequence(int beginIndex, int endIndex);

    @Override
    LinearSeq<T> tail();

    @Override
    Option<? extends LinearSeq<T>> tailOption();

    @Override
    LinearSeq<T> take(int n);

    @Override
    LinearSeq<T> takeUntil(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> takeWhile(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> takeRight(int n);

    @Override
    LinearSeq<T> takeRightUntil(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> takeRightWhile(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> update(int index, T element);

    @Override
    LinearSeq<T> update(int index, Function<? super T, ? extends T> updater);

    @Override
    <U> LinearSeq<Tuple2<T, U>> zip(Iterable<? extends U> that);

    @Override
    <U, R> LinearSeq<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper);

    @Override
    <U> LinearSeq<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem);

    @Override
    LinearSeq<Tuple2<T, Integer>> zipWithIndex();

    @Override
    <U> LinearSeq<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper);

    /**
     * Searches this sequence for a specific element using a linear search. The sequence must already be sorted into
     * ascending natural order. If it is not sorted, the results are undefined.
     *
     * @param element the element to find
     * @return the index of the search element, if it is contained in the sequence;
     * otherwise, <code>(-(<i>insertion point</i>) - 1)</code>. The
     * <i>insertion point</i> is defined as the point at which the
     * element would be inserted into the sequence. Note that this guarantees that
     * the return value will be &gt;= 0 if and only if the element is found.
     * @throws ClassCastException if T cannot be cast to {@code Comparable<? super T>}
     */
    @Override
    @SuppressWarnings("unchecked")
    default int search(T element) {
        final ToIntFunction<T> comparison = ((Comparable<T>) element)::compareTo;
        return LinearSeqModule.Search.linearSearch(this, comparison);
    }

    /**
     * Searches this sequence for a specific element using a linear search. The sequence must already be sorted into
     * ascending order according to the specified comparator. If it is not sorted, the results are undefined.
     *
     * @param element    the element to find
     * @param comparator the comparator by which this sequence is ordered
     * @return the index of the search element, if it is contained in the sequence;
     * otherwise, <code>(-(<i>insertion point</i>) - 1)</code>. The
     * <i>insertion point</i> is defined as the point at which the
     * element would be inserted into the sequence. Note that this guarantees that
     * the return value will be &gt;= 0 if and only if the element is found.
     */
    @Override
    default int search(T element, Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final ToIntFunction<T> comparison = current -> comparator.compare(element, current);
        return LinearSeqModule.Search.linearSearch(this, comparison);
    }

}

interface LinearSeqModule {


    interface Search {
        
        static <T> int linearSearch(LinearSeq<T> seq, ToIntFunction<T> comparison) {
            int idx = 0;
            for (T current : seq) {
                final int cmp = comparison.applyAsInt(current);
                if (cmp == 0) {
                    return idx;
                } else if (cmp < 0) {
                    return -(idx + 1);
                }
                idx += 1;
            }
            return -(idx + 1);
        }
    }

}
