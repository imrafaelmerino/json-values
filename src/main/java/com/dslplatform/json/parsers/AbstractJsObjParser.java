package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;


import java.io.IOException;

abstract class AbstractJsObjParser extends AbstractParser
{

  protected boolean isEmptyObj(final JsonReader<?> reader) throws JsParserException
  {
    try
    {
      if (reader.last() != '{') throw reader.newParseError("Expecting '{' for map start");
      byte nextToken = reader.getNextToken();
      return nextToken == '}';
    }
    catch (IOException e)
    {
      throw new JsParserException(e.getMessage());
    }
  }

}
