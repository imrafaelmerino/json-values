package jsonvalues.spec;

/**
 * Configuration for DslJson options.
 * By default, key cache is enabled. Everything else is not configured.
 * To load `META-INF/services` call `includeServiceLoader()`
 */
class Settings {
    final StringCache keyCache = new SimpleStringCache();
    StringCache valuesCache;
    JsReader.DoublePrecision doublePrecision = JsReader.DoublePrecision.DEFAULT;
    int maxNumberDigits = 512;
    int maxStringBuffer = 128 * 1024 * 1024;


    /**
     * Use specific string values cache implementation.
     * By default, string values cache is disabled.
     * <p>
     * To support memory restricted scenarios where there is limited number of string values,
     * values cache can be used.
     * <p>
     * Not every "JSON string" will use this cache... eg UUID, LocalDate don't create an instance of string
     * and therefore don't use this cache.
     *
     * @return itself
     */
    Settings useStringValuesCache() {
        this.valuesCache = new SimpleStringCache();
        return this;
    }

    /**
     * By default doubles are not deserialized into an exact value in some rare edge cases.
     *
     * @param precision type of double deserialization
     * @return itself
     */
    Settings doublePrecision(JsReader.DoublePrecision precision) {
        this.doublePrecision = precision;
        return this;
    }


    /**
     * Specify maximum allowed size for digits buffer. Default is 512.
     * Digits buffer is used when processing strange/large input numbers.
     *
     * @param size maximum allowed size for digit buffer
     * @return itself
     */
    Settings limitDigitsBuffer(int size) {
        if (size < 1) throw new IllegalArgumentException("size can't be smaller than 1");
        this.maxNumberDigits = size;
        return this;
    }

    /**
     * Specify maximum allowed size for string buffer. Default is 128MB
     * To protect against malicious inputs, maximum allowed string buffer can be reduced.
     *
     * @param size maximum size of buffer in bytes
     * @return itself
     */
    Settings limitStringBuffer(int size) {
        if (size < 1) throw new IllegalArgumentException("size can't be smaller than 1");
        this.maxStringBuffer = size;
        return this;
    }


}