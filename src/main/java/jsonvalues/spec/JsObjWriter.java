package jsonvalues.spec;

import jsonvalues.JsObj;
import jsonvalues.JsValue;


import static java.util.Objects.requireNonNull;

final class JsObjWriter implements JsWriter.WriteObject<JsObj> {

    private final JsValueWritter valueSerializer;

    public JsObjWriter(final JsValueWritter valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void write(final JsWriter sw,
                      final JsObj value
    ) {
        int size = requireNonNull(value).size();
        sw.writeByte(JsWriter.OBJECT_START);
        if (size > 0) {
            var iterator = value.iterator();
            var kv = iterator.next();
            sw.writeString(kv.key());
            sw.writeByte(JsWriter.SEMI);
            final JsValue fist = kv.value();
            valueSerializer.serialize(sw,
                                      fist
            );

            for (int i = 1; i < size; i++) {
                sw.writeByte(JsWriter.COMMA);
                kv = iterator.next();
                sw.writeString(kv.key());
                sw.writeByte(JsWriter.SEMI);
                final JsValue keyValue = kv.value();
                valueSerializer.serialize(sw,
                                          keyValue
                );
            }
        }
        sw.writeByte(JsWriter.OBJECT_END);
    }
}
