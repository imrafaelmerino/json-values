package com.dslplatform.json;

import jsonvalues.*;

import java.util.Objects;


final class JsValueSerializer {

    private JsonWriter.WriteObject<JsObj> objectSerializer;
    private JsonWriter.WriteObject<JsArray> arraySerializer;

    public void setObjectSerializer(final JsonWriter.WriteObject<JsObj> objectSerializer) {
        this.objectSerializer = Objects.requireNonNull(objectSerializer);
    }

    public void setArraySerializer(final JsonWriter.WriteObject<JsArray> arraySerializer) {
        this.arraySerializer = Objects.requireNonNull(arraySerializer);
    }

    void serialize(final JsonWriter writer,
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
            case JsDouble d -> MyNumberConverter.serialize(d.toJsBigDec().value,
                                                           writer
                                                          );
            case JsBigDec bd -> MyNumberConverter.serialize(bd.value,
                                                            writer
                                                           );
            case JsBigInt bi -> writer.writeAscii(bi.value.toString());
            case JsLong l -> MyNumberConverter.serialize(l.value,
                                                         writer
                                                        );
            case JsInt i -> MyNumberConverter.serialize(i.value,
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
