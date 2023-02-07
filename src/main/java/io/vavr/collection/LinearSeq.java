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
    LinearSeq<T> filter(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> filterNot(Predicate<? super T> predicate);

    @Override
    <U> LinearSeq<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper);



    @Override
    LinearSeq<T> insert(int index, T element);

    @Override
    LinearSeq<T> insertAll(int index, Iterable<? extends T> elements);







    @Override
    <U> LinearSeq<U> map(Function<? super T, ? extends U> mapper);

    @Override
    LinearSeq<T> orElse(Iterable<? extends T> other);

    @Override
    LinearSeq<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);



    @Override
    Tuple2<? extends LinearSeq<T>, ? extends LinearSeq<T>> partition(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> peek(Consumer<? super T> action);



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
    LinearSeq<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation);

    @Override
    <U> LinearSeq<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation);

    @Override
    <U> LinearSeq<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation);



    @Override
    Iterator<? extends LinearSeq<T>> slideBy(Function<? super T, ?> classifier);

    @Override
    Iterator<? extends LinearSeq<T>> sliding(int size);

    @Override
    Iterator<? extends LinearSeq<T>> sliding(int size, int step);


    @Override
    LinearSeq<T> sorted(Comparator<? super T> comparator);


    @Override
    <U> LinearSeq<T> sortBy(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper);

    @Override
    Tuple2<? extends LinearSeq<T>, ? extends LinearSeq<T>> span(Predicate<? super T> predicate);




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
    <U> LinearSeq<Tuple2<T, U>> zip(Iterable<? extends U> that);

    @Override
    <U, R> LinearSeq<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper);

    @Override
    <U> LinearSeq<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem);

    @Override
    LinearSeq<Tuple2<T, Integer>> zipWithIndex();

    @Override
    <U> LinearSeq<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper);


}


