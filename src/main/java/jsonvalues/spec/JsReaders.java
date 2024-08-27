package jsonvalues.spec;

class JsReaders {

  public static final JsReaders READERS = new JsReaders();
  public final JsInstantReader instantReader;
  public final JsIntReader intReader;
  public final JsBinaryReader binaryReader;
  public final JsLongReader longReader;
  public final JsDoubleReader doubleReader;
  public final JsBigIntReader jsBigIntReader;
  public final JsBoolReader boolReader;
  public final JsDecimalReader decimalReader;
  public final JsStrReader strReader;
  public final JsNumberReader numberReader;
  public final JsValueReader valueReader;
  public final JsObjReader objReader;
  public final JsArrayOfValueReader arrayOfValueReader;
  public final JsArrayOfIntReader arrayOfIntReader;
  public final JsArrayOfDoubleReader arrayOfDoubleReader;
  public final JsArrayOfLongReader arrayOfLongReader;
  public final JsArrayOfDecimalReader arrayOfDecimalReader;
  public final JsArrayOfBigIntReader arrayOfBigIntReader;
  public final JsArrayOfObjReader arrayOfObjReader;
  public final JsArrayOfStringReader arrayOfStringReader;
  public final JsArrayOfBoolReader arrayOfBoolReader;

  public final JsObjReader mapOfBoolReader;

  public final JsObjReader mapOfIntegerReader;

  public final JsObjReader mapOfBigIntegerReader;


  public final JsObjReader mapOfDecimalReader;

  public final JsObjReader mapOfStringReader;


  public final JsObjReader mapOfInstantReader;

  public final JsObjReader mapOfBinaryReader;


  public final JsObjReader mapOfLongReader;
  public final JsObjReader mapOfDoubleReader;

  private JsReaders() {
    instantReader = new JsInstantReader();
    intReader = new JsIntReader();
    binaryReader = new JsBinaryReader();
    longReader = new JsLongReader();
    doubleReader = new JsDoubleReader();
    jsBigIntReader = new JsBigIntReader();
    boolReader = new JsBoolReader();
    decimalReader = new JsDecimalReader();
    strReader = new JsStrReader();
    numberReader = new JsNumberReader();
    valueReader = new JsValueReader();
    objReader = new JsObjReader(valueReader);
    arrayOfValueReader = new JsArrayOfValueReader(valueReader);
    valueReader.setArrayDeserializer(arrayOfValueReader);
    valueReader.setObjDeserializer(objReader);
    valueReader.setNumberDeserializer(numberReader);
    arrayOfIntReader = new JsArrayOfIntReader(intReader);
    arrayOfDoubleReader = new JsArrayOfDoubleReader(doubleReader);
    arrayOfLongReader = new JsArrayOfLongReader(longReader);
    arrayOfDecimalReader = new JsArrayOfDecimalReader(decimalReader);
    arrayOfBigIntReader = new JsArrayOfBigIntReader(jsBigIntReader);
    arrayOfObjReader = new JsArrayOfObjReader(objReader);
    arrayOfStringReader = new JsArrayOfStringReader(strReader);
    arrayOfBoolReader = new JsArrayOfBoolReader(boolReader);
    mapOfLongReader = new JsObjReader(longReader);
    mapOfIntegerReader = new JsObjReader(intReader);
    mapOfBoolReader = new JsObjReader(boolReader);
    mapOfDecimalReader = new JsObjReader(decimalReader);
    mapOfBigIntegerReader = new JsObjReader(jsBigIntReader);
    mapOfStringReader = new JsObjReader(strReader);
    mapOfInstantReader = new JsObjReader(instantReader);
    mapOfBinaryReader = new JsObjReader(binaryReader);
    mapOfDoubleReader = new JsObjReader(doubleReader);
  }


}
