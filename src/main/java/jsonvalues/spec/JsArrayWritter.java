package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsValue;

import static java.util.Objects.requireNonNull;


final class JsArrayWritter implements JsWriter.WriteObject<JsArray> {
    private final JsValueWritter valueSerializer;

    JsArrayWritter(final JsValueWritter valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void write(final JsWriter writer,
                      final JsArray list
                     ) {
        int size = requireNonNull(list).size();
        writer.writeByte(JsWriter.ARRAY_START);
        if (size != 0) {
            final JsValue first = list.get(0);
            valueSerializer.serialize(writer,
                                      first
                                     );
            for (int i = 1; i < size; i++) {
                writer.writeByte(JsWriter.COMMA);
                final JsValue value = list.get(i);
                valueSerializer.serialize(writer,
                                          value
                                         );
            }
        }
        writer.writeByte(JsWriter.ARRAY_END);
    }


}
