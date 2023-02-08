package com.fasterxml.jackson.core;

/**
 * Since 2.10, Builder class is offered for creating token stream factories
 * with difference configurations: with 3.x they will be fully immutable.
 *
 * @since 2.10
 */
 abstract class TSFBuilder<F extends JsonFactory,
    B extends TSFBuilder<F,B>>
{
    /*
    /**********************************************************************
    /* Constants
    /**********************************************************************
     */

    /**
     * Bitfield (set of flags) of all factory features that are enabled by default.
     */
    protected final static int DEFAULT_FACTORY_FEATURE_FLAGS = JsonFactory.Feature.collectDefaults();

    /**
     * Bitfield (set of flags) of all parser features that are enabled
     * by default.
     */
    protected final static int DEFAULT_PARSER_FEATURE_FLAGS = JsonParser.Feature.collectDefaults();

    /**
     * Bitfield (set of flags) of all generator features that are enabled
     * by default.
     */
    protected final static int DEFAULT_GENERATOR_FEATURE_FLAGS = JsonGenerator.Feature.collectDefaults();

    /*
    /**********************************************************************
    /* Configured features
    /**********************************************************************
     */

    /**
     * Set of {@link JsonFactory.Feature}s enabled,
     * as bitmask.
     */
    protected int _factoryFeatures;

    /**
     * Set of {@link JsonParser.Feature}s enabled, as bitmask.
     */
    protected int _streamReadFeatures;

    /**
     * Set of {@link JsonGenerator.Feature}s enabled, as bitmask.
     */
    protected int _streamWriteFeatures;

    /*
    /**********************************************************************
    /* Other configuration
    /**********************************************************************
     */

    /**
     * Optional helper object that may decorate input sources, to do
     * additional processing on input during parsing.
     */
    protected InputDecorator _inputDecorator;

    /**
     * Optional helper object that may decorate output object, to do
     * additional processing on output during content generation.
     */
    protected OutputDecorator _outputDecorator;

    /**
     * Optional StreamReadConfig.
     *
     * @since 2.15
     */
    protected StreamReadConstraints _streamReadConstraints;

    /*
    /**********************************************************************
    /* Construction
    /**********************************************************************
     */

    protected TSFBuilder() {
        _factoryFeatures = DEFAULT_FACTORY_FEATURE_FLAGS;
        _streamReadFeatures = DEFAULT_PARSER_FEATURE_FLAGS;
        _streamWriteFeatures = DEFAULT_GENERATOR_FEATURE_FLAGS;
        _inputDecorator = null;
        _outputDecorator = null;
    }

    protected TSFBuilder(JsonFactory base)
    {
        this(base._factoryFeatures,
                base._parserFeatures, base._generatorFeatures);
        _streamReadConstraints = base._streamReadConstraints;
    }

    protected TSFBuilder(int factoryFeatures,
            int parserFeatures, int generatorFeatures)
    {
        _factoryFeatures = factoryFeatures;
        _streamReadFeatures = parserFeatures;
        _streamWriteFeatures = generatorFeatures;
    }

    // // // Accessors

     int factoryFeaturesMask() { return _factoryFeatures; }
     int streamReadFeatures() { return _streamReadFeatures; }
     int streamWriteFeatures() { return _streamWriteFeatures; }

     InputDecorator inputDecorator() { return _inputDecorator; }
     OutputDecorator outputDecorator() { return _outputDecorator; }

    // // // Factory features

     B enable(JsonFactory.Feature f) {
        _factoryFeatures |= f.getMask();
        return _this();
    }

     B disable(JsonFactory.Feature f) {
        _factoryFeatures &= ~f.getMask();
        return _this();
    }

     B configure(JsonFactory.Feature f, boolean state) {
        return state ? enable(f) : disable(f);
    }

    // // // StreamReadFeatures (replacement of non-json-specific parser features)

     B enable(StreamReadFeature f) {
        _streamReadFeatures |= f.mappedFeature().getMask();
        return _this();
    }

     B enable(StreamReadFeature first, StreamReadFeature... other) {
        _streamReadFeatures |= first.mappedFeature().getMask();
        for (StreamReadFeature f : other) {
            _streamReadFeatures |= f.mappedFeature().getMask();
        }
        return _this();
    }

     B disable(StreamReadFeature f) {
        _streamReadFeatures &= ~f.mappedFeature().getMask();
        return _this();
    }

     B disable(StreamReadFeature first, StreamReadFeature... other) {
        _streamReadFeatures &= ~first.mappedFeature().getMask();
        for (StreamReadFeature f : other) {
            _streamReadFeatures &= ~f.mappedFeature().getMask();
        }
        return _this();
    }

     B configure(StreamReadFeature f, boolean state) {
        return state ? enable(f) : disable(f);
    }

    // // // StreamWriteFeatures (replacement of non-json-specific generator features)

     B enable(StreamWriteFeature f) {
        _streamWriteFeatures |= f.mappedFeature().getMask();
        return _this();
    }

     B enable(StreamWriteFeature first, StreamWriteFeature... other) {
        _streamWriteFeatures |= first.mappedFeature().getMask();
        for (StreamWriteFeature f : other) {
            _streamWriteFeatures |= f.mappedFeature().getMask();
        }
        return _this();
    }

     B disable(StreamWriteFeature f) {
        _streamWriteFeatures &= ~f.mappedFeature().getMask();
        return _this();
    }

     B disable(StreamWriteFeature first, StreamWriteFeature... other) {
        _streamWriteFeatures &= ~first.mappedFeature().getMask();
        for (StreamWriteFeature f : other) {
            _streamWriteFeatures &= ~f.mappedFeature().getMask();
        }
        return _this();
    }

     B configure(StreamWriteFeature f, boolean state) {
        return state ? enable(f) : disable(f);
    }

    /* 26-Jun-2018, tatu: This should not be needed here, but due to 2.x limitations,
     *   we do need to include it or require casting.
     *   Specifically: since `JsonFactory` (and not `TokenStreamFactory`) is base class
     *   for all backends, it can not expose JSON-specific builder, but this.
     *   So let's select lesser evil(s).
     */

    // // // JSON-specific, reads

     B enable(JsonReadFeature f) {
        return _failNonJSON(f);
    }

     B enable(JsonReadFeature first, JsonReadFeature... other) {
        return _failNonJSON(first);
    }

     B disable(JsonReadFeature f) {
        return _failNonJSON(f);
    }

     B disable(JsonReadFeature first, JsonReadFeature... other) {
        return _failNonJSON(first);
    }

     B configure(JsonReadFeature f, boolean state) {
        return _failNonJSON(f);
    }

    private B _failNonJSON(Object feature) {
        throw new IllegalArgumentException("Feature "+feature.getClass().getName()
                +"#"+feature.toString()+" not supported for non-JSON backend");
    }

    // // // JSON-specific, writes

     B enable(JsonWriteFeature f) {
        return _failNonJSON(f);
    }

     B enable(JsonWriteFeature first, JsonWriteFeature... other) {
        return _failNonJSON(first);
    }

     B disable(JsonWriteFeature f) {
        return _failNonJSON(f);
    }

     B disable(JsonWriteFeature first, JsonWriteFeature... other) {
        return _failNonJSON(first);
    }

     B configure(JsonWriteFeature f, boolean state) {
        return _failNonJSON(f);
    }

    // // // Other configuration

     B inputDecorator(InputDecorator dec) {
        _inputDecorator = dec;
        return _this();
    }

     B outputDecorator(OutputDecorator dec) {
        _outputDecorator = dec;
        return _this();
    }

    /**
     * Sets the constraints for streaming reads.
     *
     * @param streamReadConstraints constraints for streaming reads
     * @return this factory
     * @since 2.15
     */
     B streamReadConstraints(StreamReadConstraints streamReadConstraints) {
        _streamReadConstraints = streamReadConstraints;
        return _this();
    }

    // // // Other methods

    /**
     * Method for constructing actual {@link TokenStreamFactory} instance, given
     * configuration.
     *
     * @return {@link TokenStreamFactory} build based on current configuration
     */
     abstract F build();

    // silly convenience cast method we need
    @SuppressWarnings("unchecked")
    protected final B _this() { return (B) this; }

    // // // Support for subtypes

    protected void _legacyEnable(JsonParser.Feature f) {
        if (f != null) {
            _streamReadFeatures |= f.getMask();
        }
    }

    protected void _legacyDisable(JsonParser.Feature f) {
        if (f != null) {
            _streamReadFeatures &= ~f.getMask();
        }
    }

    protected void _legacyEnable(JsonGenerator.Feature f) {
        if (f != null) {
            _streamWriteFeatures |= f.getMask();
        }
    }
    protected void _legacyDisable(JsonGenerator.Feature f) {
        if (f != null) {
            _streamWriteFeatures &= ~f.getMask();
        }
    }
}
