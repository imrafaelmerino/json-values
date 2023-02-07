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


import java.util.function.*;

/**
 * Interface for immutable, linear sequences.
 * <p>
 * Efficient {@code head()}, {@code tail()}, and {@code isEmpty()} methods are characteristic for linear sequences.
 *
 * @param <T> component type
 */
 interface LinearSeq<T> extends Traversable<T> {



    // -- Adjusted return types of Seq methods

    LinearSeq<T> append(T element);







    @Override
    LinearSeq<T> filter(Predicate<? super T> predicate);





    LinearSeq<T> insert(int index, T element);







    @Override
    <U> LinearSeq<U> map(Function<? super T, ? extends U> mapper);

    @Override
    LinearSeq<T> orElse(Iterable<? extends T> other);

    @Override
    LinearSeq<T> orElse(Supplier<? extends Iterable<? extends T>> supplier);




    @Override
    LinearSeq<T> peek(Consumer<? super T> action);



    LinearSeq<T> prepend(T element);

    LinearSeq<T> prependAll(Iterable<? extends T> elements);



    @Override
    LinearSeq<T> replace(T currentElement, T newElement);


    LinearSeq<T> reverse();

    default Iterator<T> reverseIterator() {
        return reverse().iterator();
    }








    @Override
    Tuple2<? extends LinearSeq<T>, ? extends LinearSeq<T>> span(Predicate<? super T> predicate);

    @Override
    LinearSeq<T> tail();

    @Override
    Option<? extends LinearSeq<T>> tailOption();




}


