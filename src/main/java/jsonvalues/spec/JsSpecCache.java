package jsonvalues.spec;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class JsSpecCache {

  static final Map<String, JsSpec> cache = new HashMap<>();

  private JsSpecCache() {
  }

  static void put(String name,
                  JsSpec spec) {
    synchronized (JsSpecCache.class) {
      validateDoesntExist(requireNonNull(name));
      cache.put(requireNonNull(name),
                requireNonNull(spec));
    }
  }

  static void putAll(String name,
                     List<String> aliases,
                     JsSpec spec) {
    synchronized (JsSpecCache.class) {
      validateDoesntExist(requireNonNull(name));
      if (aliases != null) {
        for (String alias : aliases) {
          validateDoesntExist(alias);
        }
      }
      cache.put(name,
                requireNonNull(spec));
      if (aliases != null) {
        for (String alias : aliases) {
          cache.put(alias,
                    requireNonNull(spec));
        }
      }
    }

  }

  private static void validateDoesntExist(String name) {
    if (cache.containsKey(requireNonNull(name))) {
      throw new IllegalArgumentException("The spec `%s` has already been created. Choose another name".formatted(name));
    }
  }

  static JsSpec get(String name) {
    JsSpec spec = cache.get(requireNonNull(name));
    if (spec == null) {
      throw new IllegalArgumentException(("The spec `%s` doesn't exist. Use the builder like `JsObjSpecBuilder` or `JsSpecs.ofNamedSpec(name,spec)` to"
                                          +
                                          " create specs that can be gotten by its name"
                                         ).formatted(name));
    }
    return spec;
  }


}
