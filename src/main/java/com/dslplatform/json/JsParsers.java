package com.dslplatform.json;

class JsParsers {
    public static final JsParsers PARSERS = new JsParsers();
    public final JsInstantParser instantParser;
    public final JsIntParser intParser;
    public final JsBinaryParser binaryParser;
    public final JsLongParser longParser;
    public final JsBigIntParser integralParser;
    public final JsBoolParser boolParser;
    public final JsDecimalParser decimalParser;
    public final JsStrParser strParser;
    public final JsNumberParser numberParser;
    public final JsValueParser valueParser;
    public final JsObjParser objParser;
    public final JsArrayOfValueParser arrayOfValueParser;
    public final JsArrayOfIntParser arrayOfIntParser;
    public final JsArrayOfLongParser arrayOfLongParser;
    public final JsArrayOfDecimalParser arrayOfDecimalParser;
    public final JsArrayOfBigIntParser arrayOfIntegralParser;
    public final JsArrayOfNumberParser arrayOfNumberParser;
    public final JsArrayOfObjParser arrayOfObjParser;
    public final JsArrayOfStringParser arrayOfStrParser;
    public final JsArrayOfBoolParser arrayOfBoolParser;

    public final JsObjParser mapOfBoolParser;

    public final JsObjParser mapOfIntegerParser;

    public final JsObjParser mapOfBigIntegerParser;


    public final JsObjParser mapOfDecimalParser;

    public final JsObjParser mapOfStringParser;

    public final JsObjParser mapOfObjParser;

    public final JsObjParser mapOfArrayParser;

    public final JsObjParser mapOfInstantParser;

    public final JsObjParser mapOfBinaryParser;


    public final JsObjParser mapOfLongParser;

    private JsParsers() {
        instantParser = new JsInstantParser();
        intParser = new JsIntParser();
        binaryParser = new JsBinaryParser();
        longParser = new JsLongParser();
        integralParser = new JsBigIntParser();
        boolParser = new JsBoolParser();
        decimalParser = new JsDecimalParser();
        strParser = new JsStrParser();
        numberParser = new JsNumberParser();
        valueParser = new JsValueParser();
        objParser = new JsObjParser(valueParser);
        arrayOfValueParser = new JsArrayOfValueParser(valueParser);
        valueParser.setArrayDeserializer(arrayOfValueParser);
        valueParser.setObjDeserializer(objParser);
        valueParser.setNumberDeserializer(numberParser);
        arrayOfIntParser = new JsArrayOfIntParser(intParser);
        arrayOfLongParser = new JsArrayOfLongParser(longParser);
        arrayOfDecimalParser = new JsArrayOfDecimalParser(decimalParser);
        arrayOfIntegralParser = new JsArrayOfBigIntParser(integralParser);
        arrayOfNumberParser = new JsArrayOfNumberParser(numberParser);
        arrayOfObjParser = new JsArrayOfObjParser(objParser);
        arrayOfStrParser = new JsArrayOfStringParser(strParser);
        arrayOfBoolParser = new JsArrayOfBoolParser(boolParser);
        mapOfLongParser = new JsObjParser(longParser);
        mapOfIntegerParser = new JsObjParser(intParser);
        mapOfBoolParser = new JsObjParser(boolParser);
        mapOfDecimalParser = new JsObjParser(decimalParser);
        mapOfBigIntegerParser = new JsObjParser(integralParser);
        mapOfStringParser = new JsObjParser(strParser);
        mapOfInstantParser = new JsObjParser(instantParser);
        mapOfObjParser = new JsObjParser(valueParser);
        mapOfBinaryParser = new JsObjParser(binaryParser);
        mapOfArrayParser = new JsObjParser(arrayOfValueParser);
    }


}
