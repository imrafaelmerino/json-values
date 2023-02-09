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
                NumberConverter.serialize(value.toJsBigDec().value,
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
                NumberConverter.serialize(value.toJsLong().value,
                                          writer
                                         );
                break;
            }
            case JsInt.TYPE_ID: {
                NumberConverter.serialize(value.toJsInt().value,
                                          writer
                                         );
                break;
            }

            case JsBinary.TYPE_ID: {
                byte[] xs = value.toJsBinary().value;
                if (xs.length == 0) writer.writeString("");
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
