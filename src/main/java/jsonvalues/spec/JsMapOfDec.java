package jsonvalues.spec;

import jsonvalues.*;

import java.util.List;


final class JsMapOfDec extends AbstractMap implements JsSpec, AvroSpec {
     JsMapOfDec(boolean nullable) {
        super(nullable);
    }

    @Override
    public JsSpec nullable() {
        return new JsMapOfDec(true);
    }

    @Override
    public JsSpecParser parser() {
        return JsSpecParsers.INSTANCE.ofMapOfDecimal(nullable);
    }

    @Override
    public List<SpecError> test(JsPath path,
                                JsValue value) {
       return test(path,value,it -> !it.isDecimal(),ERROR_CODE.DECIMAL_EXPECTED);
    }



}
