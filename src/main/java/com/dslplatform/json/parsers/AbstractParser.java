package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.ParsingException;
import io.vavr.collection.HashMap;
import jsonvalues.*;

 abstract class AbstractParser
{
  final static HashMap<String, JsValue> EMPTY_MAP = HashMap.empty();
  final static JsObj EMPTY_OBJ = JsObj.empty();

   abstract JsValue value(final JsonReader<?> reader) throws JsParserException;

  JsValue nullOrValue(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      return reader.wasNull() ? JsNull.NULL : value(reader);
    }
    catch (ParsingException e)
    {
      throw new JsParserException(e.getMessage());
    }
  }


}
