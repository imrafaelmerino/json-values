package jsonvalues.spec;

import jsonvalues.*;

import java.util.Objects;


final class JsValueWritter {

    private JsWriter.WriteObject<JsObj> objectSerializer;
    private JsWriter.WriteObject<JsArray> arraySerializer;

    public void setObjectSerializer(final JsWriter.WriteObject<JsObj> objectSerializer) {
        this.objectSerializer = Objects.requireNonNull(objectSerializer);
    }

    public void setArraySerializer(final JsWriter.WriteObject<JsArray> arraySerializer) {
        this.arraySerializer = Objects.requireNonNull(arraySerializer);
    }

    void serialize(final JsWriter writer,
                   final JsValue value
                  ) {

        if (Objects.requireNonNull(value) instanceof JsBool) {
            JsBool bool = (JsBool) Objects.requireNonNull(value);
            writer.writeAscii(Boolean.toString(bool.value));
        } else if (value instanceof JsNull) {
            writer.writeNull();
        } else if (value instanceof JsStr) {
            JsStr str = (JsStr) value;
            writer.writeString(str.value);
        } else if (value instanceof JsObj) {
            JsObj obj = (JsObj) value;
            objectSerializer.write(writer,
                                   obj
                                  );
        } else if (value instanceof JsArray) {
            JsArray arr = (JsArray) value;
            arraySerializer.write(writer,
                                  arr
                                 );
        } else if (value instanceof JsDouble) {
            JsDouble d = (JsDouble) value;
            NumberConverter.serialize(d.toJsBigDec().value,
                                      writer
                                     );
        } else if (value instanceof JsBigDec) {
            JsBigDec bd = (JsBigDec) value;
            NumberConverter.serialize(bd.value,
                                      writer
                                     );
        } else if (value instanceof JsBigInt) {
            JsBigInt bi = (JsBigInt) value;
            writer.writeAscii(bi.value.toString());
        } else if (value instanceof JsLong) {
            JsLong l = (JsLong) value;
            NumberConverter.serialize(l.value,
                                      writer
                                     );
        } else if (value instanceof JsInt) {
            JsInt i = (JsInt) value;
            NumberConverter.serialize(i.value,
                                      writer
                                     );
        } else if (value instanceof JsBinary) {
            JsBinary b = (JsBinary) value;
            byte[] xs = b.value;
            if (xs.length == 0) writer.writeString("");
            else writer.writeBinary(xs);
        } else if (value instanceof JsInstant) {
            JsInstant instant = (JsInstant) value;
            writer.writeString(instant.value.toString());
        } else if (value instanceof JsNothing) {
            writer.writeString("");
        }

    }
}
