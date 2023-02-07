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


import io.vavr.Tuple2;
import io.vavr.control.Option;

import java.io.Serializable;
import java.util.Iterator;


/**
 * An immutable {@code HashMap} implementation based on a
 * <a href="https://en.wikipedia.org/wiki/Hash_array_mapped_trie">Hash array mapped trie (HAMT)</a>.
 */
public final class HashMap<K, V> implements Iterable<Tuple2<K,V>>  {

    private static final long serialVersionUID = 1L;

    private static final HashMap<?, ?> EMPTY = new HashMap<>(HashArrayMappedTrie.empty());

    private final HashArrayMappedTrie<K, V> trie;

    private HashMap(HashArrayMappedTrie<K, V> trie) {
        this.trie = trie;
    }
    
  

    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> empty() {
        return (HashMap<K, V>) EMPTY;
    }

   

  
    public boolean containsKey(K key) {
        return trie.containsKey(key);
    }


  
    public Option<V> get(K key) {
        return trie.get(key);
    }

  
    public V getOrElse(K key, V defaultValue) {
        return trie.getOrElse(key, defaultValue);
    }

   

  
    public boolean isEmpty() {
        return trie.isEmpty();
    }




  
    public HashMap<K, V> put(K key, V value) {
        return new HashMap<>(trie.put(key, value));
    }

    

  
    public HashMap<K, V> remove(K key) {
        final HashArrayMappedTrie<K, V> result = trie.remove(key);
        return result.size() == trie.size() ? this : wrap(result);
    }

   

  
    public int size() {
        return trie.size();
    }

    
    private static <K, V> HashMap<K, V> wrap(HashArrayMappedTrie<K, V> trie) {
        return trie.isEmpty() ? empty() : new HashMap<>(trie);
    }

    public java.util.Set<K> keySet() {
        return trie.keysIterator().toJavaSet();
    }


    public boolean containsValue(V value){
        for (V v : trie.valuesIterator()) {
            if(v.equals(value)) return true;
        }

        return false;
    }

    @Override
    public Iterator<Tuple2<K, V>> iterator() {
        return trie.iterator();
    }
}
