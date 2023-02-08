package com.fasterxml.jackson.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;

 class JsonGeneratorDelegate extends JsonGenerator
{
    /**
     * Delegate object that method calls are delegated to.
     */
    protected JsonGenerator delegate;

    /**
     * Whether copy methods
     * ({@link #copyCurrentEvent}, {@link #copyCurrentStructure}, {@link #writeTree} and {@link #writeObject})
     * are to be called (true), or handled by this object (false).
     */
    protected boolean delegateCopyMethods;

    /*
    /**********************************************************************
    /* Construction, initialization
    /**********************************************************************
     */

     JsonGeneratorDelegate(JsonGenerator d) {
        this(d, true);
    }

    /**
     * @param d Underlying generator to delegate calls to
     * @param delegateCopyMethods Flag assigned to <code>delagateCopyMethod</code>
     *   and which defines whether copy methods are handled locally (false), or
     *   delegated to configured
     */
     JsonGeneratorDelegate(JsonGenerator d, boolean delegateCopyMethods) {
        delegate = d;
        this.delegateCopyMethods = delegateCopyMethods;
    }

    /*
    /**********************************************************************
    /*  API, metadata/state access
    /**********************************************************************
     */

    @Override  ObjectCodec getCodec() { return delegate.getCodec(); }

    @Override  JsonGenerator setCodec(ObjectCodec oc) {
        delegate.setCodec(oc);
        return this;
    }

    @Override  void setSchema(FormatSchema schema) { delegate.setSchema(schema); }
    @Override  FormatSchema getSchema() { return delegate.getSchema(); }
    @Override
    public Version version() { return delegate.version(); }
    @Override  Object getOutputTarget() { return delegate.getOutputTarget(); }
    @Override  int getOutputBuffered() { return delegate.getOutputBuffered(); }

    @Override  void assignCurrentValue(Object v) { delegate.assignCurrentValue(v); }
    @Override  Object currentValue() { return delegate.currentValue(); }

    // TODO: deprecate in 2.14 or later
    @Override
     void setCurrentValue(Object v) { delegate.setCurrentValue(v); }

    // TODO: deprecate in 2.14 or later
    @Override
     Object getCurrentValue() { return delegate.getCurrentValue(); }

    /*
    /**********************************************************************
    /*  API, capability introspection
    /**********************************************************************
     */

    @Override
     boolean canUseSchema(FormatSchema schema) { return delegate.canUseSchema(schema); }

    @Override
     boolean canWriteTypeId() { return delegate.canWriteTypeId(); }

    @Override
     boolean canWriteObjectId() { return delegate.canWriteObjectId(); }

    @Override
     boolean canWriteBinaryNatively() { return delegate.canWriteBinaryNatively(); }

    @Override
     boolean canOmitFields() { return delegate.canOmitFields(); }

    @Override
     boolean canWriteFormattedNumbers() { return delegate.canWriteFormattedNumbers(); }

    @Override
     JacksonFeatureSet<StreamWriteCapability> getWriteCapabilities() {
        return delegate.getWriteCapabilities();
    }

    /*
    /**********************************************************************
    /*  API, configuration
    /**********************************************************************
     */

    @Override
     JsonGenerator enable(Feature f) {
        delegate.enable(f);
        return this;
    }

    @Override
     JsonGenerator disable(Feature f) {
        delegate.disable(f);
        return this;
    }

    @Override
     boolean isEnabled(Feature f) { return delegate.isEnabled(f); }

    // final, can't override (and no need to)
    // final JsonGenerator configure(Feature f, boolean state)

    @Override
     int getFeatureMask() { return delegate.getFeatureMask(); }

    @Override
    @Deprecated
     JsonGenerator setFeatureMask(int mask) {
        delegate.setFeatureMask(mask);
        return this;
    }

    @Override
     JsonGenerator overrideStdFeatures(int values, int mask) {
        delegate.overrideStdFeatures(values, mask);
        return this;
    }

    @Override
     JsonGenerator overrideFormatFeatures(int values, int mask) {
        delegate.overrideFormatFeatures(values, mask);
        return this;
    }

    /*
    /**********************************************************************
    /* Configuring generator
    /**********************************************************************
      */

    @Override
     JsonGenerator setPrettyPrinter(PrettyPrinter pp) {
        delegate.setPrettyPrinter(pp);
        return this;
    }

    @Override
     PrettyPrinter getPrettyPrinter() { return delegate.getPrettyPrinter(); }

    @Override
     JsonGenerator useDefaultPrettyPrinter() { delegate.useDefaultPrettyPrinter();
        return this; }

    @Override
     JsonGenerator setHighestNonEscapedChar(int charCode) { delegate.setHighestNonEscapedChar(charCode);
        return this; }

    @Override
     int getHighestEscapedChar() { return delegate.getHighestEscapedChar(); }

    @Override
     CharacterEscapes getCharacterEscapes() {  return delegate.getCharacterEscapes(); }

    @Override
     JsonGenerator setCharacterEscapes(CharacterEscapes esc) { delegate.setCharacterEscapes(esc);
        return this; }

    @Override
     JsonGenerator setRootValueSeparator(SerializableString sep) { delegate.setRootValueSeparator(sep);
        return this; }

    /*
    /**********************************************************************
    /*  API, write methods, structural
    /**********************************************************************
     */

    @Override
     void writeStartArray() throws IOException { delegate.writeStartArray(); }

    @SuppressWarnings("deprecation")
    @Override
     void writeStartArray(int size) throws IOException { delegate.writeStartArray(size); }

    @Override
     void writeStartArray(Object forValue) throws IOException { delegate.writeStartArray(forValue); }

    @Override
     void writeStartArray(Object forValue, int size) throws IOException { delegate.writeStartArray(forValue, size); }

    @Override
     void writeEndArray() throws IOException { delegate.writeEndArray(); }

    @Override
     void writeStartObject() throws IOException { delegate.writeStartObject(); }

    @Override
     void writeStartObject(Object forValue) throws IOException { delegate.writeStartObject(forValue); }

    @Override
     void writeStartObject(Object forValue, int size) throws IOException {
        delegate.writeStartObject(forValue, size);
    }

    @Override
     void writeEndObject() throws IOException { delegate.writeEndObject(); }

    @Override
     void writeFieldName(String name) throws IOException {
        delegate.writeFieldName(name);
    }

    @Override
     void writeFieldName(SerializableString name) throws IOException {
        delegate.writeFieldName(name);
    }

    @Override
     void writeFieldId(long id) throws IOException {
        delegate.writeFieldId(id);
    }

    @Override
     void writeArray(int[] array, int offset, int length) throws IOException {
        delegate.writeArray(array, offset, length);
    }

    @Override
     void writeArray(long[] array, int offset, int length) throws IOException {
        delegate.writeArray(array, offset, length);
    }

    @Override
     void writeArray(double[] array, int offset, int length) throws IOException {
        delegate.writeArray(array, offset, length);
    }

    @Override
     void writeArray(String[] array, int offset, int length) throws IOException {
        delegate.writeArray(array, offset, length);
    }

    /*
    /**********************************************************************
    /*  API, write methods, text/String values
    /**********************************************************************
     */

    @Override
     void writeString(String text) throws IOException { delegate.writeString(text); }

    @Override
     void writeString(Reader reader, int len) throws IOException {
        delegate.writeString(reader, len);
    }

    @Override
     void writeString(char[] text, int offset, int len) throws IOException { delegate.writeString(text, offset, len); }

    @Override
     void writeString(SerializableString text) throws IOException { delegate.writeString(text); }

    @Override
     void writeRawUTF8String(byte[] text, int offset, int length) throws IOException { delegate.writeRawUTF8String(text, offset, length); }

    @Override
     void writeUTF8String(byte[] text, int offset, int length) throws IOException { delegate.writeUTF8String(text, offset, length); }

    /*
    /**********************************************************************
    /*  API, write methods, binary/raw content
    /**********************************************************************
     */

    @Override
     void writeRaw(String text) throws IOException { delegate.writeRaw(text); }

    @Override
     void writeRaw(String text, int offset, int len) throws IOException { delegate.writeRaw(text, offset, len); }

    @Override
     void writeRaw(SerializableString raw) throws IOException { delegate.writeRaw(raw); }

    @Override
     void writeRaw(char[] text, int offset, int len) throws IOException { delegate.writeRaw(text, offset, len); }

    @Override
     void writeRaw(char c) throws IOException { delegate.writeRaw(c); }

    @Override
     void writeRawValue(String text) throws IOException { delegate.writeRawValue(text); }

    @Override
     void writeRawValue(String text, int offset, int len) throws IOException { delegate.writeRawValue(text, offset, len); }

    @Override
     void writeRawValue(char[] text, int offset, int len) throws IOException { delegate.writeRawValue(text, offset, len); }

    @Override
     void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException { delegate.writeBinary(b64variant, data, offset, len); }

    @Override
     int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) throws IOException { return delegate.writeBinary(b64variant, data, dataLength); }

    /*
    /**********************************************************************
    /*  API, write methods, other value types
    /**********************************************************************
     */

    @Override
     void writeNumber(short v) throws IOException { delegate.writeNumber(v); }

    @Override
     void writeNumber(int v) throws IOException { delegate.writeNumber(v); }

    @Override
     void writeNumber(long v) throws IOException { delegate.writeNumber(v); }

    @Override
     void writeNumber(BigInteger v) throws IOException { delegate.writeNumber(v); }

    @Override
     void writeNumber(double v) throws IOException { delegate.writeNumber(v); }

    @Override
     void writeNumber(float v) throws IOException { delegate.writeNumber(v); }

    @Override
     void writeNumber(BigDecimal v) throws IOException { delegate.writeNumber(v); }

    @Override
     void writeNumber(String encodedValue) throws IOException, UnsupportedOperationException { delegate.writeNumber(encodedValue); }

    @Override
     void writeNumber(char[] encodedValueBuffer, int offset, int length) throws IOException, UnsupportedOperationException { delegate.writeNumber(encodedValueBuffer, offset, length); }

    @Override
     void writeBoolean(boolean state) throws IOException { delegate.writeBoolean(state); }

    @Override
     void writeNull() throws IOException { delegate.writeNull(); }

    /*
    /**********************************************************************
    /*  API, convenience field-write methods
    /**********************************************************************
     */

    // 04-Oct-2019, tatu: Reminder: these should NOT be delegated, unless matching
    //    methods in `FilteringGeneratorDelegate` are re-defined to "split" calls again

//     void writeBinaryField(String fieldName, byte[] data) throws IOException {
//     void writeBooleanField(String fieldName, boolean value) throws IOException {
//     void writeNullField(String fieldName) throws IOException {
//     void writeStringField(String fieldName, String value) throws IOException {
//     void writeNumberField(String fieldName, short value) throws IOException {

//     void writeArrayFieldStart(String fieldName) throws IOException {
//     void writeObjectFieldStart(String fieldName) throws IOException {
//     void writeObjectField(String fieldName, Object pojo) throws IOException {
//     void writePOJOField(String fieldName, Object pojo) throws IOException {

    // Sole exception being this method as it is not a "combo" method

    @Override
     void writeOmittedField(String fieldName) throws IOException {
        delegate.writeOmittedField(fieldName);
    }

    /*
    /**********************************************************************
    /*  API, write methods, Native Ids
    /**********************************************************************
     */

    @Override
     void writeObjectId(Object id) throws IOException { delegate.writeObjectId(id); }

    @Override
     void writeObjectRef(Object id) throws IOException { delegate.writeObjectRef(id); }

    @Override
     void writeTypeId(Object id) throws IOException { delegate.writeTypeId(id); }

    @Override
     void writeEmbeddedObject(Object object) throws IOException { delegate.writeEmbeddedObject(object); }

    /*
    /**********************************************************************
    /*  API, write methods, serializing Java objects
    /**********************************************************************
     */

    @Override // since 2.13
     void writePOJO(Object pojo) throws IOException {
        writeObject(pojo);
    }

    @Override
     void writeObject(Object pojo) throws IOException {
        if (delegateCopyMethods) {
            delegate.writeObject(pojo);
            return;
        }
        if (pojo == null) {
            writeNull();
        } else {
            ObjectCodec c = getCodec();
            if (c != null) {
                c.writeValue(this, pojo);
                return;
            }
            _writeSimpleObject(pojo);
        }
    }

    @Override
     void writeTree(TreeNode tree) throws IOException {
        if (delegateCopyMethods) {
            delegate.writeTree(tree);
            return;
        }
        // As with 'writeObject()', we are not check if write would work
        if (tree == null) {
            writeNull();
        } else {
            ObjectCodec c = getCodec();
            if (c == null) {
                throw new IllegalStateException("No ObjectCodec defined");
            }
            c.writeTree(this, tree);
        }
    }

    /*
    /**********************************************************************
    /*  API, convenience field write methods
    /**********************************************************************
     */

    // // These are fine, just delegate to other methods...

    /*
    /**********************************************************************
    /*  API, copy-through methods
    /**********************************************************************
     */

    @Override
     void copyCurrentEvent(JsonParser p) throws IOException {
        if (delegateCopyMethods) delegate.copyCurrentEvent(p);
        else super.copyCurrentEvent(p);
    }

    @Override
     void copyCurrentStructure(JsonParser p) throws IOException {
        if (delegateCopyMethods) delegate.copyCurrentStructure(p);
        else super.copyCurrentStructure(p);
    }

    /*
    /**********************************************************************
    /*  API, context access
    /**********************************************************************
     */

    @Override  JsonStreamContext getOutputContext() { return delegate.getOutputContext(); }

    /*
    /**********************************************************************
    /*  API, buffer handling
    /**********************************************************************
     */

    @Override
    public void flush() throws IOException { delegate.flush(); }
    @Override
    public void close() throws IOException { delegate.close(); }

    @Override  boolean isClosed() { return delegate.isClosed(); }

    /*
    /**********************************************************************
    /* Extended API
    /**********************************************************************
     */

    @Deprecated // since 2.11
     JsonGenerator getDelegate() { return delegate; }

    /**
     * @return Underlying generator that calls are delegated to
     *
     * @since 2.11
     */
     JsonGenerator delegate() { return delegate; }
}
