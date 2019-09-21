package jsonvalues;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

interface MyMap<M extends MyMap<M>>
{
    /**
     returns true if the map contains the key
     @param key the given key
     @return true if the map contains the key, false if not
     */
    boolean contains(final String key);

    /**
     returns the keys of this map
     @return set of keys
     */
    Set<String> keys();

    /**
     return the element associated with the key. It will be called by the library only if the key exists, so
     an error can be thrown if it doesn't
     @param key the given key
     @return the element associated with the key or JsNothing if it doesn't exist
     */
    JsElem get(final String key);

    /**
     return the element associated with the key wrapped into an optional. It's called by the library without
     checking the existence of the key.
     @param key the given key
     @return the element associated with the key or Optional.empty() if it doesn't exist
     */
    Optional<JsElem> getOptional(final String key);

    /**
     an entry of this map. A map is unordered, so any element could be the head. It's only called
     by the library if the map is not empty.
     @return an entry of this map
     */
    Map.Entry<String, JsElem> head();

    /**
     returns true if this map is empty
     @return true if empty, false otherwise
     */
    boolean isEmpty();

    /**
     returns an iterator of this map.
     @return an iterator of this map
     */
    Iterator<Map.Entry<String, JsElem>> iterator();

    /**
     returns the size of the map
     @return the size of the map
     */
    int size();

    /**
     the tail of the map given a head. It's only called by the library if the map is not empty.
     @param head the element returned by {@link #head()}
     @return the tail of the map given a head
     */
    M tail(final String head);


}
