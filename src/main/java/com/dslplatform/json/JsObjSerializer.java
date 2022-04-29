package com.dslplatform.json;

import io.vavr.Tuple2;
import jsonvalues.JsObj;
import jsonvalues.JsValue;

import java.util.Iterator;

import static java.util.Objects.requireNonNull;

final class JsObjSerializer implements JsonWriter.WriteObject<JsObj> {

    private final JsValueSerializer valueSerializer;

    public JsObjSerializer(final JsValueSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void write(final JsonWriter sw,
                      final JsObj value
                     ) {
        int size = requireNonNull(value).size();
        sw.writeByte(JsonWriter.OBJECT_START);
        if (size > 0) {
            final Iterator<Tuple2<String, JsValue>> iterator = value.iterator();
            Tuple2<String, JsValue>                 kv       = iterator.next();
            sw.writeString(kv._1);
            sw.writeByte(JsonWriter.SEMI);
            final JsValue fist = kv._2;
            valueSerializer.serialize(sw,
                                      fist
                                     );

            for (int i = 1; i < size; i++) {
                sw.writeByte(JsonWriter.COMMA);
                kv = iterator.next();
                sw.writeString(kv._1);
                sw.writeByte(JsonWriter.SEMI);
                final JsValue keyValue = kv._2;
                valueSerializer.serialize(sw,
                                          keyValue
                                         );
            }
        }
        sw.writeByte(JsonWriter.OBJECT_END);
    }
}
