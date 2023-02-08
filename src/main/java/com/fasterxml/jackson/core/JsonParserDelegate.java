package com.fasterxml.jackson.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Helper class that implements
 * <a href="http://en.wikipedia.org/wiki/Delegation_pattern">delegation pattern</a> for {@link JsonParser},
 * to allow for simple overridability of basic parsing functionality.
 * The idea is that any functionality to be modified can be simply
 * overridden; and anything else will be delegated by default.
 */
 class JsonParserDelegate extends JsonParser
{
    /**
     * Delegate object that method calls are delegated to.
     */
    protected JsonParser delegate;

     JsonParserDelegate(JsonParser d) {
        delegate = d;
    }

    /*
    /**********************************************************************
    /*  API, configuration
    /**********************************************************************
     */

    @Override
    public void setCodec(ObjectCodec c) { delegate.setCodec(c); }
    @Override
    public ObjectCodec getCodec() { return delegate.getCodec(); }

    @Override
    public JsonParser enable(Feature f) {
        delegate.enable(f);
        return this;
    }

    @Override
    public JsonParser disable(Feature f) {
        delegate.disable(f);
        return this;
    }

    @Override
    public boolean isEnabled(Feature f) { return delegate.isEnabled(f); }
    @Override
    public int getFeatureMask() { return delegate.getFeatureMask(); }

    @Override
    @Deprecated
    public // since 2.7
     JsonParser setFeatureMask(int mask) {
        delegate.setFeatureMask(mask);
        return this;
    }

    @Override
    public JsonParser overrideStdFeatures(int values, int mask) {
        delegate.overrideStdFeatures(values, mask);
        return this;
    }

    @Override
    public JsonParser overrideFormatFeatures(int values, int mask) {
        delegate.overrideFormatFeatures(values, mask);
        return this;
    }

    @Override  FormatSchema getSchema() { return delegate.getSchema(); }
    @Override  void setSchema(FormatSchema schema) { delegate.setSchema(schema); }
    @Override  boolean canUseSchema(FormatSchema schema) {  return delegate.canUseSchema(schema); }
    @Override
    public Version version() { return delegate.version(); }
    @Override  Object getInputSource() { return delegate.getInputSource(); }

    /*
    /**********************************************************************
    /* Constraints violation checking (2.15)
    /**********************************************************************
     */

    @Override
     StreamReadConstraints streamReadConstraints() {
        return delegate.streamReadConstraints();
    }

    /*
    /**********************************************************************
    /* Capability introspection
    /**********************************************************************
     */

    @Override  boolean requiresCustomCodec() { return delegate.requiresCustomCodec(); }

    @Override  JacksonFeatureSet<StreamReadCapability> getReadCapabilities() { return delegate.getReadCapabilities(); }

    /*
    /**********************************************************************
    /* Closeable impl
    /**********************************************************************
     */

    @Override
    public void close() throws IOException { delegate.close(); }
    @Override  boolean isClosed() { return delegate.isClosed(); }

    /*
    /**********************************************************************
    /*  API, state change/override methods
    /**********************************************************************
     */

    @Override  void clearCurrentToken() { delegate.clearCurrentToken(); }
    @Override  JsonToken getLastClearedToken() { return delegate.getLastClearedToken(); }
    @Override  void overrideCurrentName(String name) { delegate.overrideCurrentName(name); }

    @Override // since 2.13
     void assignCurrentValue(Object v) { delegate.assignCurrentValue(v); }

    // TODO: deprecate in 2.14 or later
    @Override
     void setCurrentValue(Object v) { delegate.setCurrentValue(v); }

    /*
    /**********************************************************************
    /*  API, state/location accessors
    /**********************************************************************
     */

    @Override  JsonStreamContext getParsingContext() { return delegate.getParsingContext(); }

    @Override  JsonToken currentToken() { return delegate.currentToken(); }
    @Override  int currentTokenId() { return delegate.currentTokenId(); }
    @Override  String currentName() throws IOException { return delegate.currentName(); }
    @Override // since 2.13
     Object currentValue() { return delegate.currentValue(); }

    @Override // since 2.13
     JsonLocation currentLocation() { return delegate.getCurrentLocation(); }
    @Override // since 2.13
     JsonLocation currentTokenLocation() { return delegate.getTokenLocation(); }

    // TODO: deprecate in 2.14 or later
    @Override  JsonToken getCurrentToken() { return delegate.getCurrentToken(); }
    @Deprecated // since 2.12
    @Override  int getCurrentTokenId() { return delegate.getCurrentTokenId(); }
    // TODO: deprecate in 2.14 or later
    @Override  String getCurrentName() throws IOException { return delegate.getCurrentName(); }
    // TODO: deprecate in 2.14 or later
    @Override  Object getCurrentValue() { return delegate.getCurrentValue(); }

    // TODO: deprecate in 2.14 or later
    @Override  JsonLocation getCurrentLocation() { return delegate.getCurrentLocation(); }
    // TODO: deprecate in 2.14 or later
    @Override  JsonLocation getTokenLocation() { return delegate.getTokenLocation(); }

    /*
    /**********************************************************************
    /*  API, token accessors
    /**********************************************************************
     */

    @Override  boolean hasCurrentToken() { return delegate.hasCurrentToken(); }
    @Override  boolean hasTokenId(int id) { return delegate.hasTokenId(id); }
    @Override  boolean hasToken(JsonToken t) { return delegate.hasToken(t); }

    @Override  boolean isExpectedStartArrayToken() { return delegate.isExpectedStartArrayToken(); }
    @Override  boolean isExpectedStartObjectToken() { return delegate.isExpectedStartObjectToken(); }
    @Override  boolean isExpectedNumberIntToken() { return delegate.isExpectedNumberIntToken(); }

    @Override  boolean isNaN() throws IOException { return delegate.isNaN(); }

    /*
    /**********************************************************************
    /*  API, access to token textual content
    /**********************************************************************
     */

    @Override  String getText() throws IOException { return delegate.getText();  }
    @Override  boolean hasTextCharacters() { return delegate.hasTextCharacters(); }
    @Override  char[] getTextCharacters() throws IOException { return delegate.getTextCharacters(); }
    @Override  int getTextLength() throws IOException { return delegate.getTextLength(); }
    @Override  int getTextOffset() throws IOException { return delegate.getTextOffset(); }
    @Override  int getText(Writer writer) throws IOException, UnsupportedOperationException { return delegate.getText(writer);  }

    /*
    /**********************************************************************
    /*  API, access to token numeric values
    /**********************************************************************
     */

    @Override
    public BigInteger getBigIntegerValue() throws IOException { return delegate.getBigIntegerValue(); }

    @Override
     boolean getBooleanValue() throws IOException { return delegate.getBooleanValue(); }

    @Override
     byte getByteValue() throws IOException { return delegate.getByteValue(); }

    @Override
     short getShortValue() throws IOException { return delegate.getShortValue(); }

    @Override
    public BigDecimal getDecimalValue() throws IOException { return delegate.getDecimalValue(); }

    @Override
     double getDoubleValue() throws IOException { return delegate.getDoubleValue(); }

    @Override
     float getFloatValue() throws IOException { return delegate.getFloatValue(); }

    @Override
    public int getIntValue() throws IOException { return delegate.getIntValue(); }

    @Override
    public long getLongValue() throws IOException { return delegate.getLongValue(); }

    @Override
     NumberType getNumberType() throws IOException { return delegate.getNumberType(); }

    @Override
     Number getNumberValue() throws IOException { return delegate.getNumberValue(); }

    @Override
     Number getNumberValueExact() throws IOException { return delegate.getNumberValueExact(); }

    @Override
     Object getNumberValueDeferred() throws IOException { return delegate.getNumberValueDeferred(); }

    /*
    /**********************************************************************
    /*  API, access to token information, coercion/conversion
    /**********************************************************************
     */

    @Override  int getValueAsInt() throws IOException { return delegate.getValueAsInt(); }
    @Override  int getValueAsInt(int defaultValue) throws IOException { return delegate.getValueAsInt(defaultValue); }
    @Override  long getValueAsLong() throws IOException { return delegate.getValueAsLong(); }
    @Override  long getValueAsLong(long defaultValue) throws IOException { return delegate.getValueAsLong(defaultValue); }
    @Override  double getValueAsDouble() throws IOException { return delegate.getValueAsDouble(); }
    @Override  double getValueAsDouble(double defaultValue) throws IOException { return delegate.getValueAsDouble(defaultValue); }
    @Override  boolean getValueAsBoolean() throws IOException { return delegate.getValueAsBoolean(); }
    @Override  boolean getValueAsBoolean(boolean defaultValue) throws IOException { return delegate.getValueAsBoolean(defaultValue); }
    @Override
    public String getValueAsString() throws IOException { return delegate.getValueAsString(); }
    @Override  String getValueAsString(String defaultValue) throws IOException { return delegate.getValueAsString(defaultValue); }

    /*
    /**********************************************************************
    /*  API, access to token values, other
    /**********************************************************************
     */

    @Override  Object getEmbeddedObject() throws IOException { return delegate.getEmbeddedObject(); }
    @Override  byte[] getBinaryValue(Base64Variant b64variant) throws IOException { return delegate.getBinaryValue(b64variant); }
    @Override  int readBinaryValue(Base64Variant b64variant, OutputStream out) throws IOException { return delegate.readBinaryValue(b64variant, out); }

    @Override
    public JsonToken nextToken() throws IOException { return delegate.nextToken(); }

    @Override  JsonToken nextValue() throws IOException { return delegate.nextValue(); }

    @Override  void finishToken() throws IOException { delegate.finishToken(); }

    @Override
     JsonParser skipChildren() throws IOException {
        delegate.skipChildren();
        // NOTE: must NOT delegate this method to delegate, needs to be self-reference for chaining
        return this;
    }

    /*
    /**********************************************************************
    /*  API, Native Ids (type, object)
    /**********************************************************************
     */

    @Override  boolean canReadObjectId() { return delegate.canReadObjectId(); }
    @Override  boolean canReadTypeId() { return delegate.canReadTypeId(); }
    @Override  Object getObjectId() throws IOException { return delegate.getObjectId(); }
    @Override  Object getTypeId() throws IOException { return delegate.getTypeId(); }

    /*
    /**********************************************************************
    /* Extended API
    /**********************************************************************
     */

    /**
     * Accessor for getting the immediate {@link JsonParser} this parser delegates calls to.
     *
     * @return Underlying parser calls are delegated to
     *
     * @since 2.10
     */
     JsonParser delegate() { return delegate; }
}
