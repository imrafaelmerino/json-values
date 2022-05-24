package jsonvalues.parser;

import com.dslplatform.json.JsParserException;
import jsonvalues.gen.*;
import jsonvalues.spec.JsObjParser;
import jsonvalues.spec.JsObjSpec;
import jsonvalues.spec.JsSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestErrors {

    @Test
    public void test_parsing_obj() {

        JsObjSpec spec = JsObjSpec.strict("a",
                                          JsSpecs.integer());

        JsObjParser parser = new JsObjParser(spec);


        Assertions.assertThrows(JsParserException.class,
                                () -> parser.parse("[]"));
    }


}
