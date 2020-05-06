package jsonvalues.spec;

import com.dslplatform.json.parsers.JsSpecParser;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;

public interface JsSpec
{
  /**
   @return whether or not the key is optional.
   */
  boolean isRequired();

  /**
   @return the same spec with the nullable flag enabled
   */
  JsSpec nullable();


  /**
   @return the same spec with the optional flag enabled
   */
  JsSpec optional();

  /**
   @return the deserializer used during the parsing process to parse an array of bytes or string
   into a json value
   */
  JsSpecParser parser();


  Set<JsErrorPair> test(final JsPath parentPath,
                        final JsValue value
                       );




}


