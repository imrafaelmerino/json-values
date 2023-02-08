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

/**
 * Internal class, containing helpers.
 */
final class Collections {

    // checks, if the *elements* of the given iterables are equal
    static boolean areEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
        final java.util.Iterator<?> iter1 = iterable1.iterator();
        final java.util.Iterator<?> iter2 = iterable2.iterator();
        while (iter1.hasNext() && iter2.hasNext()) {
            if (!Objects.equals(iter1.next(), iter2.next())) {
                return false;
            }
        }
        return iter1.hasNext() == iter2.hasNext();
    }

    static int hashUnordered(Iterable<?> iterable) {
        return hash(iterable, Integer::sum);
    }


    // hashes the elements respecting their order
    static int hashOrdered(Iterable<?> iterable) {
        return hash(iterable, (acc, hash) -> acc * 31 + hash);
    }


    private static int hash(Iterable<?> iterable, IntBinaryOperator accumulator) {

        int hashCode = 1;
        for (Object o : iterable) {
            hashCode = accumulator.applyAsInt(hashCode, Objects.hashCode(o));
        }
        return hashCode;

    }


    // @param iterable may not be null
    static boolean isEmpty(Iterable<?> iterable) {
        return !iterable.iterator().hasNext();
    }



    static <T> Iterator<T> reverseIterator(Iterable<T> iterable) {
        return ((Vector) iterable).reverseIterator();
    }


    static <T> IterableWithSize<T> withSize(Iterable<? extends T> iterable) {
        if(iterable instanceof java.util.List l) return new IterableWithSize<>(l, l.size());
        if(iterable instanceof Vector l) return new IterableWithSize<>(l, l.length());

        //TODO
        throw new RuntimeException();
    }


    static class IterableWithSize<T> {
        private final Iterable<? extends T> iterable;
        private final int size;

        IterableWithSize(Iterable<? extends T> iterable, int size) {
            this.iterable = iterable;
            this.size = size;
        }

        java.util.Iterator<? extends T> iterator() {
            return iterable.iterator();
        }

        java.util.Iterator<? extends T> reverseIterator() {
            return Collections.reverseIterator(iterable);
        }

        int size() {
            return size;
        }

        Object[] toArray() {

            return ArrayType.asArray(iterator(), size());

        }
    }

}
