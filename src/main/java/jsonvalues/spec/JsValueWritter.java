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

        switch (value) {
            case JsBool bool -> writer.writeAscii(Boolean.toString(bool.value));
            case JsNull ignored -> writer.writeNull();
            case JsStr str -> writer.writeString(str.value);
            case JsObj obj -> objectSerializer.write(writer,
                                                     obj
                                                    );
            case JsArray arr -> arraySerializer.write(writer,
                                                      arr
                                                     );
            case JsDouble d -> NumberConverter.serialize(d.toJsBigDec().value,
                                                         writer
                                                        );
            case JsBigDec bd -> NumberConverter.serialize(bd.value,
                                                          writer
                                                         );
            case JsBigInt bi -> writer.writeAscii(bi.value.toString());
            case JsLong l -> NumberConverter.serialize(l.value,
                                                       writer
                                                      );
            case JsInt i -> NumberConverter.serialize(i.value,
                                                      writer
                                                     );
            case JsBinary b -> {
                byte[] xs = b.value;
                if (xs.length == 0) writer.writeString("");
                else writer.writeBinary(xs);
            }
            case JsInstant instant -> writer.writeString(instant.value.toString());
            case JsNothing ignored -> writer.writeString("");
        }

    }
}
