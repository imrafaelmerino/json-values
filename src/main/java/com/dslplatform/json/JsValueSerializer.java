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

        switch (value.id()) {
            case JsBool.TYPE_ID: {
                writer.writeAscii(Boolean.toString(value.toJsBool().value));
                break;
            }
            case JsNull.TYPE_ID: {
                writer.writeNull();
                break;
            }
            case JsStr.TYPE_ID: {
                writer.writeString(value.toJsStr().value);
                break;
            }
            case JsObj.TYPE_ID: {
                objectSerializer.write(writer,
                                       value.toJsObj()
                );
                break;
            }
            case JsArray.TYPE_ID: {
                arraySerializer.write(writer,
                                      value.toJsArray()
                );
                break;
            }
            case JsDouble.TYPE_ID:
            case JsBigDec.TYPE_ID: {
                MyNumberConverter.serialize(value.toJsBigDec().value,
                                            writer
                );
                break;
            }
            case JsBigInt.TYPE_ID: {
                writer.writeAscii(value.toJsBigInt().value
                                          .toString());

                break;
            }
            case JsLong.TYPE_ID: {
                MyNumberConverter.serialize(value.toJsLong().value,
                                            writer
                );
                break;
            }
            case JsInt.TYPE_ID: {
                MyNumberConverter.serialize(value.toJsInt().value,
                                            writer
                );
                break;
            }

            case JsBinary.TYPE_ID: {
                byte[] xs = value.toJsBinary().value;
                if(xs.length == 0) writer.writeString("");
                else writer.writeBinary(xs);
                break;
            }
            case JsInstant.TYPE_ID: {
                writer.writeString(value.toJsInstant().value.toString());
                break;
            }
            default:
                throw new IllegalStateException("JsValue.id() not considered. Default branch of a switch statement was executed.");

        }

    }
}
