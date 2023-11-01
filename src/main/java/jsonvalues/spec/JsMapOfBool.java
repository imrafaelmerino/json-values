package jsonvalues.spec;

import jsonvalues.*;

import java.util.List;


final class JsMapOfBool extends AbstractMap implements JsSpec, AvroSpec {
     JsMapOfBool(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfBool(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfBool(nullable);
    }

    @Override
    public List<SpecError> test(JsPath path,
                                JsValue value) {
       return test(path,value,it -> !it.isBool(),ERROR_CODE.BOOLEAN_EXPECTED);
    }



}
