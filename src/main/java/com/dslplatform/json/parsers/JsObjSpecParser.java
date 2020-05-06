package com.dslplatform.json.parsers;

import com.dslplatform.json.JsonReader;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import jsonvalues.JsObj;
import jsonvalues.JsValue;
import java.io.IOException;

 class JsObjSpecParser extends AbstractJsObjParser
{
  private final Map<String, JsSpecParser> parsers;
  protected final boolean strict;
  private final static JsValueParser valueParser = JsParsers.PARSERS.valueParser;


  private final static JsSpecParser defaultParser = valueParser::value;

  JsObjSpecParser(boolean strict,
                  final Map<String, JsSpecParser> parsers
                 )
  {
    this.strict = strict;
    this.parsers = parsers;
  }

    @Override
     JsObj value ( final JsonReader<?> reader) throws JsParserException
    {
      try
      {
        if (isEmptyObj(reader)) return EMPTY_OBJ;
        String key = reader.readKey();
        throwErrorIfStrictAndKeyMissing(reader,
                                        key
                                       );
        HashMap<String, JsValue> map = EMPTY_MAP.put(key,
                                                     parsers.getOrElse(key,
                                                                       defaultParser
                                                                      )
                                                            .parse(reader)
                                                    );
        byte nextToken;
        while ((nextToken = reader.getNextToken()) == ',')
        {
          reader.getNextToken();
          key = reader.readKey();
          throwErrorIfStrictAndKeyMissing(reader,
                                          key
                                         );
          map = map.put(key,
                        parsers.getOrElse(key,
                                          defaultParser
                                         )
                               .parse(reader)
                       );

        }
        if (nextToken != '}') throw reader.newParseError("Expecting '}' for map end");
        return new JsObj(map);
      }
      catch (IOException e)
      {
        throw new JsParserException(e.getMessage());
      }
    }

  private void throwErrorIfStrictAndKeyMissing(final JsonReader<?> reader,
                                               final String key
                                              ) throws com.dslplatform.json.ParsingException
  {
    if (strict && !parsers.containsKey(key))
    {
      throw reader.newParseError("There is no spec defined for the key " + key);
    }
  }


}
