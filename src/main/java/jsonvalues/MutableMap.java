package jsonvalues;

/**
 Represents a mutable data structure where pairs of a JsObj are stored. Each mutable Json factory {@link MutableJsons} has an
 implementation of this interface, that can be defined using the method {@link jsonvalues.MutableJsons#withMap(Class)}.
 The default mutable implementation that {@link Jsons#mutable} uses is the Java {@link java.util.HashMap}
 */
public interface MutableMap extends MyMap<MutableMap>

{

    /**
     creates and returns a copy of this map
     @return a new instance
     */
    MutableMap copy();


    /**
     removes the key associated with this map. It will be called by the library only if the key exists
     @param key the given key
     */
    void remove(final String key);

    /**
     updates the element associated with the key with a new element. It will be called by the library only if the key exists,
     @param key the given key
     @param je the new element
     */
    void update(final String key,
                final JsElem je
               );
}

