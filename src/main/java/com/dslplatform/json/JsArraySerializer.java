package com.dslplatform.json;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import static java.util.Objects.requireNonNull;


final class JsArraySerializer implements JsonWriter.WriteObject<JsArray> {
    private final JsValueSerializer valueSerializer;

    JsArraySerializer(final JsValueSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void write(final JsonWriter writer,
                      final JsArray list
    ) {
        int size = requireNonNull(list).size();
        writer.writeByte(JsonWriter.ARRAY_START);
        if (size != 0) {
            final JsValue first = list.get(0);
            valueSerializer.serialize(writer,
                                      first
            );
            for (int i = 1; i < size; i++) {
                writer.writeByte(JsonWriter.COMMA);
                final JsValue value = list.get(i);
                valueSerializer.serialize(writer,
                                          value
                );
            }
        }
        writer.writeByte(JsonWriter.ARRAY_END);
    }


}
