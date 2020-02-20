package com.dslplatform.json.serializers;

import com.dslplatform.json.JsonWriter;
import jsonvalues.JsArray;
import jsonvalues.JsValue;


public final class JsArraySerializer<T extends JsArray> implements JsonWriter.WriteObject<T>
{
    private JsValueSerializer valueSerializer;

    public JsArraySerializer(final JsValueSerializer valueSerializer)
    {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void write(final JsonWriter writer,
                      final T list
                     )
    {
        writer.writeByte(JsonWriter.ARRAY_START);
        final int size = list.size();
        if (size != 0)
        {
            final JsValue first = list.get(0);
            valueSerializer.serialize(writer,
                                      first
                                     );
            for (int i = 1; i < size; i++)
            {
                writer.writeByte(JsonWriter.COMMA);
                final JsValue value = list.get(i);
                valueSerializer.serialize(writer,
                                          value
                                         );
            }
        }
        writer.writeByte(JsonWriter.ARRAY_END);
    }


}
