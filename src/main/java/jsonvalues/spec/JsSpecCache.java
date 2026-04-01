package jsonvalues.spec;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class JsSpecCache {

  private static final Map<String, JsSpec> cache = new ConcurrentHashMap<>();

  private JsSpecCache() {
  }

  static void put(String name,
                  JsSpec spec) {
    String safeName = requireNonNull(name);
    JsSpec safeSpec = requireNonNull(spec);
    synchronized (JsSpecCache.class) {
      validateDoesntExist(safeName);
      cache.put(safeName,
                safeSpec);
    }
  }

  static void putAll(String name,
                     List<String> aliases,
                     JsSpec spec) {
    String safeName = requireNonNull(name);
    JsSpec safeSpec = requireNonNull(spec);
    synchronized (JsSpecCache.class) {
      validateDoesntExist(safeName);
      if (aliases != null) {
        for (String alias : aliases) {
          validateDoesntExist(alias);
        }
      }
      cache.put(safeName,
                safeSpec);
      if (aliases != null) {
        for (String alias : aliases) {
          cache.put(alias,
                    safeSpec);
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
    String safeName = requireNonNull(name);
    JsSpec spec = cache.get(safeName);
    if (spec == null) {
      throw new IllegalArgumentException(("The spec `%s` doesn't exist. Use the builder like `JsObjSpecBuilder` or `JsSpecs.ofNamedSpec(name,spec)` to"
                                          +
                                          " create specs that can be gotten by its name"
                                         ).formatted(safeName));
    }
    return spec;
  }


}
