package jsonvalues.spec;

import jsonvalues.JsPath;
import jsonvalues.JsValue;

import java.util.Set;

public interface JsSpec {


    /**
     * Returns the same spec with the nullable flag enabled
     *
     * @return the same spec with the nullable flag enabled
     */
    JsSpec nullable();



    /**
     * Returns the deserializer used during the parsing process to parse an array of bytes or string
     *
     * @return the deserializer used during the parsing process to parse an array of bytes or string
     * into a json value
     */
    JsSpecParser parser();

    /**
     * Low level method to parse a JSON value by value from a reader. Returns the next value
     * according to the current state of the reader if it conforms this spec, otherwise a
     * JsParserException is thrown
     * @param reader the reader
     * @return the next token as a JsValue
     */
    default JsValue readNextValue(JsReader reader){
        reader.readNextToken();
        return parser().parse(reader);
    }


    /**
     * verify if the given value satisfy this spec.
     *
     * @param parentPath the path where the tested value is located.
     * @param value      the tested value
     * @return a set of path/error pairs
     */
    Set<SpecError> test(final JsPath parentPath,
                        final JsValue value
    );

    default Set<SpecError> test(final JsValue value) {
        return test(JsPath.empty(),
                    value);
    }


}


