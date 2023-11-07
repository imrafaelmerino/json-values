package jsonvalues.spec;


import jsonvalues.JsValue;

final class JsArrayOfSpecReader extends JsArrayReader {

    JsArrayOfSpecReader(final JsParser parser) {
        super(new AbstractReader() {
            @Override
            JsValue value(JsReader reader) throws JsParserException {
                return parser.parse(reader);
            }
        });
    }


}
