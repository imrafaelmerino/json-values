package jsonvalues.spec;

class JsParsers {
    public static final JsParsers PARSERS = new JsParsers();
    public final JsInstantReader instantParser;
    public final JsIntReader intParser;
    public final JsBinaryReader binaryParser;
    public final JsLongReader longParser;
    public final JsBigIntReader integralParser;
    public final JsBoolReader boolParser;
    public final JsDecimalReader decimalParser;
    public final JsStrReader strParser;
    public final JsNumberReader numberParser;
    public final JsValueReader valueParser;
    public final JsObjReader objParser;
    public final JsArrayOfValueReader arrayOfValueParser;
    public final JsArrayOfIntReader arrayOfIntParser;
    public final JsArrayOfLongReader arrayOfLongParser;
    public final JsArrayOfDecimalReader arrayOfDecimalParser;
    public final JsArrayOfBigIntReader arrayOfIntegralParser;
    public final JsArrayOfNumberReader arrayOfNumberParser;
    public final JsArrayOfObjReader arrayOfObjParser;
    public final JsArrayOfStringReader arrayOfStrParser;
    public final JsArrayOfBoolReader arrayOfBoolParser;

    public final JsObjReader mapOfBoolParser;

    public final JsObjReader mapOfIntegerParser;

    public final JsObjReader mapOfBigIntegerParser;


    public final JsObjReader mapOfDecimalParser;

    public final JsObjReader mapOfStringParser;

    public final JsObjReader mapOfObjParser;

    public final JsObjReader mapOfArrayParser;

    public final JsObjReader mapOfInstantParser;

    public final JsObjReader mapOfBinaryParser;


    public final JsObjReader mapOfLongParser;

    private JsParsers() {
        instantParser = new JsInstantReader();
        intParser = new JsIntReader();
        binaryParser = new JsBinaryReader();
        longParser = new JsLongReader();
        integralParser = new JsBigIntReader();
        boolParser = new JsBoolReader();
        decimalParser = new JsDecimalReader();
        strParser = new JsStrReader();
        numberParser = new JsNumberReader();
        valueParser = new JsValueReader();
        objParser = new JsObjReader(valueParser);
        arrayOfValueParser = new JsArrayOfValueReader(valueParser);
        valueParser.setArrayDeserializer(arrayOfValueParser);
        valueParser.setObjDeserializer(objParser);
        valueParser.setNumberDeserializer(numberParser);
        arrayOfIntParser = new JsArrayOfIntReader(intParser);
        arrayOfLongParser = new JsArrayOfLongReader(longParser);
        arrayOfDecimalParser = new JsArrayOfDecimalReader(decimalParser);
        arrayOfIntegralParser = new JsArrayOfBigIntReader(integralParser);
        arrayOfNumberParser = new JsArrayOfNumberReader(numberParser);
        arrayOfObjParser = new JsArrayOfObjReader(objParser);
        arrayOfStrParser = new JsArrayOfStringReader(strParser);
        arrayOfBoolParser = new JsArrayOfBoolReader(boolParser);
        mapOfLongParser = new JsObjReader(longParser);
        mapOfIntegerParser = new JsObjReader(intParser);
        mapOfBoolParser = new JsObjReader(boolParser);
        mapOfDecimalParser = new JsObjReader(decimalParser);
        mapOfBigIntegerParser = new JsObjReader(integralParser);
        mapOfStringParser = new JsObjReader(strParser);
        mapOfInstantParser = new JsObjReader(instantParser);
        mapOfObjParser = new JsObjReader(objParser);
        mapOfBinaryParser = new JsObjReader(binaryParser);
        mapOfArrayParser = new JsObjReader(arrayOfValueParser);
    }


}
