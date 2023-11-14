package jsonvalues.spec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

final class JsSpecCache {

    static Map<String, JsSpec> cache = new HashMap<>();

    private JsSpecCache() {
    }

    static synchronized void put(String name, JsSpec spec) {
        validateDoesntExist(name);
        cache.put(name, requireNonNull(spec));
    }

    static synchronized void putAll(String name,List<String> aliases, JsSpec spec) {
        validateDoesntExist(name);
        if(aliases!=null)for (String alias : aliases) validateDoesntExist(alias);
        cache.put(name, requireNonNull(spec));
        if(aliases!=null)for (String alias : aliases) cache.put(alias, requireNonNull(spec));

    }

    private static void validateDoesntExist(String name) {
        if (cache.containsKey(requireNonNull(name)))
            throw new IllegalArgumentException("The spec `%s` has already been created. Choose another namespace and/or name".formatted(name));
    }

    static JsSpec get(String name) {
        JsSpec spec = cache.get(requireNonNull(name));
        if (spec == null)
            throw new IllegalArgumentException(("The spec `%s` doesn't exist. Use a builder like `JsObjSpecBuilder` to" +
                                                " create specs that can be gotten by its name"
                                               ).formatted(name));
        return spec;
    }


}
