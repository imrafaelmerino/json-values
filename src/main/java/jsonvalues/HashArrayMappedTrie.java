/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 vavr, https://vavr.io
 *
 * Licensed under the Apache License, version 2.0 (the "License");
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


import jsonvalues.HashArrayMappedTrieModule.EmptyNode;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;


/**
 * An immutable <a href="https://en.wikipedia.org/wiki/Hash_array_mapped_trie">Hash array mapped trie (HAMT)</a>.
 */
interface HashArrayMappedTrie extends Iterable<HashArrayMappedTrieModule.LeafNode> {

     static  <T, U> Iterator<U> map(Iterator<T> iter,
                                    Function<? super T, ? extends U> mapper) {
        return new Iterator<U>() {

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public U next() {
                return mapper.apply(iter.next());
            }


        };

    }

    static HashArrayMappedTrie empty() {
        return EmptyNode.instance();
    }

    boolean isEmpty();

    int size();

    Optional<JsValue> get(String key);

    JsValue getOrElse(String key, JsValue defaultValue);

    boolean containsKey(String key);

    HashArrayMappedTrie put(String key, JsValue value);

    HashArrayMappedTrie remove(String key);

    @Override
    Iterator<HashArrayMappedTrieModule.LeafNode> iterator();


    Iterator<String> keysIterator();


    Iterator<JsValue> valuesIterator();
}

