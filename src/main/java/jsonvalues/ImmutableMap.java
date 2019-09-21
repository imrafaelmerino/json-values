package jsonvalues;

/**
 Represents an immutable data structure where pairs of a JsObj are stored. Each immutable Json factory {@link ImmutableJsons} has an
 implementation of this interface, that can be defined using the method {@link jsonvalues.ImmutableJsons#withMap(Class)}.
 The default immutable implementation that {@link Jsons#immutable} uses is the <a href="https://scala-lang.org/files/archive/api/2.12.0/scala/collection/seq/HashMap.html">immutable Scala HashMap</a>.
 */
public interface ImmutableMap extends MyMap<ImmutableMap>
{

    /**
     removes the key associated with this map. It will be called by the library only if the key exists
     @param key the given key
     @return a map with the key removed
     */
    ImmutableMap remove(final String key);

    /**
     updates the element associated with the key with a new element. It will be called by the library only if the key exists,
     @param key the given key
     @param je the new element
     @return a map with the key element updated
     */
    ImmutableMap update(final String key,
                        final JsElem je
                       );
}
