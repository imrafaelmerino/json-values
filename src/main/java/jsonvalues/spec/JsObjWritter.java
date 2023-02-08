package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsValue;


import static java.util.Objects.requireNonNull;

final class JsObjWritter implements JsonWriter.WriteObject<JsObj> {

    private final JsValueWritter valueSerializer;

    public JsObjWritter(final JsValueWritter valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void write(final JsonWriter sw,
                      final JsObj value
    ) {
        int size = requireNonNull(value).size();
        sw.writeByte(JsonWriter.OBJECT_START);
        if (size > 0) {
            var iterator = value.iterator();
            var kv = iterator.next();
            sw.writeString(kv.key());
            sw.writeByte(JsonWriter.SEMI);
            final JsValue fist = kv.value();
            valueSerializer.serialize(sw,
                                      fist
            );

            for (int i = 1; i < size; i++) {
                sw.writeByte(JsonWriter.COMMA);
                kv = iterator.next();
                sw.writeString(kv.key());
                sw.writeByte(JsonWriter.SEMI);
                final JsValue keyValue = kv.value();
                valueSerializer.serialize(sw,
                                          keyValue
                );
            }
        }
        sw.writeByte(JsonWriter.OBJECT_END);
    }
}
