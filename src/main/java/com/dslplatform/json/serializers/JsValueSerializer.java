package com.dslplatform.json.serializers;

import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.NumberConverter;
import jsonvalues.*;

import java.util.Objects;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;


public final class JsValueSerializer {

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
            case JsBool.ID: {
                writer.writeAscii(Boolean.toString(value.toJsBool().value));
                break;
            }
            case JsNull.ID: {
                writer.writeNull();
                break;
            }
            case JsStr.ID: {
                writer.writeString(value.toJsStr().value);
                break;
            }
            case JsObj.ID: {
                objectSerializer.write(writer,
                                       value.toJsObj()
                                      );
                break;
            }
            case JsArray.ID: {
                arraySerializer.write(writer,
                                      value.toJsArray()
                                     );
                break;
            }
            case JsDouble.ID:
            case JsBigDec.ID: {
                NumberConverter.serialize(value.toJsBigDec().value,
                                          writer
                                         );
                break;
            }
            case JsBigInt.ID: {
                writer.writeAscii(value.toJsBigInt().value
                                          .toString());

                break;
            }
            case JsLong.ID: {
                NumberConverter.serialize(value.toJsLong().value,
                                          writer
                                         );
                break;
            }
            case JsInt.ID: {
                NumberConverter.serialize(value.toJsInt().value,
                                          writer
                                         );
                break;
            }

            case JsBinary.ID: {
                writer.writeBinary(value.toJsBinary().value);
                break;
            }
            case JsInstant.ID: {
                writer.writeString(ISO_INSTANT.format(value.toJsInstant().value));
                break;
            }
            default:
                throw new IllegalStateException("JsValue.id() not considered. Default branch of a switch statement was executed.");

        }

    }
}
