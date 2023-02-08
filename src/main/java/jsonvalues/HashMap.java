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
package jsonvalues;


import java.util.Iterator;
import java.util.Optional;


/**
 * An immutable {@code HashMap} implementation based on a
 * <a href="https://en.wikipedia.org/wiki/Hash_array_mapped_trie">Hash array mapped trie (HAMT)</a>.
 */
 final class HashMap implements Iterable<HashArrayMappedTrieModule.LeafNode>  {

    private static final HashMap EMPTY = new HashMap(HashArrayMappedTrie.empty());

    private final HashArrayMappedTrie trie;

    private HashMap(HashArrayMappedTrie trie) {
        this.trie = trie;
    }

    public static  HashMap empty() {
        return  EMPTY;
    }
  
    public boolean containsKey(String key) {
        return trie.containsKey(key);
    }


  
    public Optional<JsValue> get(String key) {
        return trie.get(key);
    }

  
    public JsValue getOrElse(String key, JsValue defaultValue) {
        return trie.getOrElse(key, defaultValue);
    }

  
    public boolean isEmpty() {
        return trie.isEmpty();
    }

  
    public HashMap put(String key, JsValue value) {
        return new HashMap(trie.put(key, value));
    }

  
    public HashMap remove(String key) {
        final HashArrayMappedTrie result = trie.remove(key);
        return result.size() == trie.size() ? this : wrap(result);
    }

    public int size() {
        return trie.size();
    }

    
    private static HashMap wrap(HashArrayMappedTrie trie) {
        return trie.isEmpty() ? empty() : new HashMap(trie);
    }

    public Iterator<String> keySet() {
        return trie.keysIterator();
    }


    public boolean containsValue(JsValue value){
        for (Iterator<JsValue> it = trie.valuesIterator(); it.hasNext(); ) {
            JsValue v = it.next();
            if(v.equals(value)) return true;
        }

        return false;
    }

    @Override
    public Iterator<HashArrayMappedTrieModule.LeafNode> iterator() {
        return trie.iterator();
    }
}
