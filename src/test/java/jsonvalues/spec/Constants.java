package jsonvalues.spec;

import jsonvalues.JsArray;
import jsonvalues.JsObj;
import jsonvalues.JsStr;

final class Constants {

    static final JsArray NULL_STR_TYPE = JsArray.of("null", "string");
    static final JsArray STR_NULL_TYPE = JsArray.of("string", "null");
    static final JsStr STR_TYPE = JsStr.of("string");
    static final JsArray NULL_BOOL_TYPE = JsArray.of("null", "boolean");
    static final JsArray BOOL_NULL_TYPE = JsArray.of("boolean", "null");
    static final JsStr BOOL_TYPE = JsStr.of("boolean");
    static final JsArray NULL_LONG_TYPE = JsArray.of("null", "long");
    static final JsArray LONG_NULL_TYPE = JsArray.of("long", "null");
    static final JsArray NULL_INT_TYPE = JsArray.of("null", "int");
    static final JsArray INT_NULL_TYPE = JsArray.of("int", "null");
    static final JsStr INT_TYPE = JsStr.of("int");
    static final JsArray NULL_DOUBLE_TYPE = JsArray.of("null", "double");
    static final JsArray DOUBLE_NULL_TYPE = JsArray.of("double", "null");
    static final JsStr DOUBLE_TYPE = JsStr.of("double");
    static final JsArray NULL_BINARY_TYPE = JsArray.of("null", "bytes");
    static final JsArray BINARY_NULL_TYPE = JsArray.of("bytes", "null");
    static final JsStr BINARY_TYPE = JsStr.of("bytes");
    static final JsStr BIG_DECIMAL_TYPE = JsStr.of("bigdecimal");
    static final JsStr BIG_INTEGER_TYPE = JsStr.of("biginteger");
    static final JsStr ARRAY_TYPE = JsStr.of("array");
    static final String TYPE_FIELD = "type";
    static final String ITEMS_FIELD = "items";
    static final JsStr LONG_TYPE = JsStr.of("long");
    static final JsStr STRING_TYPE = JsStr.of("string");
    static final String LOGICAL_TYPE_FIELD = "logicalType";
    static final JsArray NUMBER_TYPE =
            JsArray.of(INT_TYPE, LONG_TYPE, DOUBLE_TYPE, JsObj.of(TYPE_FIELD, STRING_TYPE,
                                                                  LOGICAL_TYPE_FIELD, BIG_DECIMAL_TYPE));
    static final String VALUES_FIELD = "values";
    static final JsStr MAP_TYPE = JsStr.of("map");
    static final String NAME_FIELD = "name";
    static final JsStr ISO_TYPE = JsStr.of("iso-8601");
    static final JsStr BOOLEAN_TYPE = JsStr.of("boolean");
    static final JsStr NULL_TYPE = JsStr.of("null");
    static final JsStr RECORD_TYPE = JsStr.of("record");
    static final String FIELDS_FIELD = "fields";
    static final String ALIASES_FIELD = "aliases";
    static final String DEFAULT_FIELD = "default";
    static final String ORDER_FIELD = "order";
    static final String DOC_FIELD = "doc";
    static final String NAMESPACE_FIELD = "namespace";
    static final String SIZE_FIELD = "size";
    static final JsStr FIXED_TYPE = JsStr.of("fixed");
    static final String SYMBOLS_FIELD = "symbols";
    static final JsStr ENUM_TYPE = JsStr.of("enum");

    private Constants(){}
}
