package jsonvalues.spec;

import java.util.Base64;
import java.util.Objects;
import jsonvalues.JsArray;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsBinary;
import jsonvalues.JsBool;
import jsonvalues.JsDouble;
import jsonvalues.JsInstant;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsNothing;
import jsonvalues.JsNull;
import jsonvalues.JsObj;
import jsonvalues.JsStr;
import jsonvalues.JsValue;


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

    if (value instanceof JsBool bool) {
      writer.writeAscii(Boolean.toString(bool.value));
    } else if (value instanceof JsNull) {
      writer.writeNull();
    } else if (value instanceof JsStr str) {
      writer.writeString(str.value);
    } else if (value instanceof JsObj obj) {
      objectSerializer.write(writer,
                             obj
                            );
    } else if (value instanceof JsArray arr) {
      arraySerializer.write(writer,
                            arr
                           );
    } else if (value instanceof JsDouble d) {
      NumberConverter.serialize(d.toJsBigDec().value,
                                writer
                               );
    } else if (value instanceof JsBigDec bd) {
      NumberConverter.serialize(bd.value,
                                writer
                               );
    } else if (value instanceof JsBigInt bi) {
      writer.writeAscii(bi.value.toString());
    } else if (value instanceof JsLong l) {
      NumberConverter.serialize(l.value,
                                writer
                               );
    } else if (value instanceof JsInt i) {
      NumberConverter.serialize(i.value,
                                writer
                               );
    } else if (value instanceof JsBinary b) {
      byte[] xs = b.value;
      if (xs.length == 0) {
        writer.writeString("");
      } else {
        //writer.writeString(Base64.getEncoder().encodeToString(xs));
        writer.writeBinary(xs);
      }
    } else if (value instanceof JsInstant instant) {
      writer.writeString(instant.value.toString());
    } else if (value instanceof JsNothing) {
      writer.writeString("");
    }

  }
}
