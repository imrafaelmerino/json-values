package jsonvalues.spec;

import com.dslplatform.json.JsSpecParser;
import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;

public interface JsSpec {
    /**
     Returns whether the key is optional.

     @return whether the key is optional.
     */
    boolean isRequired();

    /**
     Returns the same spec with the nullable flag enabled
     @return the same spec with the nullable flag enabled
     */
    JsSpec nullable();


    /**
     * Returns the same spec with the optional flag enabled
     @return the same spec with the optional flag enabled
     */
    JsSpec optional();

    /**
     Returns the deserializer used during the parsing process to parse an array of bytes or string

     @return the deserializer used during the parsing process to parse an array of bytes or string
     into a json value
     */
    JsSpecParser parser();


    /**
     verify if the given value satisfy this spec.

     @param parentPath the path where the tested value is located.
     @param value      the tested value
     @return a set of path/error pairs
     */
    Set<JsErrorPair> test(final JsPath parentPath,
                          final JsValue value
                         );


}


