package jsonvalues.spec;

import java.util.function.Function;
import java.util.function.Supplier;
import jsonvalues.JsArray;
import jsonvalues.JsNull;
import jsonvalues.JsValue;

abstract class JsArrayReader extends AbstractReader {

  static final JsArray EMPTY = JsArray.empty();
  private final AbstractReader elementReader;

  JsArrayReader(final AbstractReader reader) {
    this.elementReader = reader;
  }

  JsValue nullOrArray(DslJsReader reader,
                      ArraySchemaConstraints arrayConstraints
                     ) throws JsParserException {

    return reader.wasNull() ?
           JsNull.NULL :
           array(reader,
                 arrayConstraints
                );
  }

  JsArray array(DslJsReader reader,
                ArraySchemaConstraints arrayConstraints
               ) throws JsParserException {
    if (checkIfEmpty(isEmptyArray(reader),
                     arrayConstraints,
                     reader.getPositionInStream()
                    )
    ) {
      return EMPTY;
    }
    var array = EMPTY.append(elementReader.value(reader));
    while (reader.readNextToken() == ',') {
      reader.readNextToken();
      JsValue value = elementReader.value(reader);
      if (arrayConstraints != null && arrayConstraints.uniqueItems() && array.containsValue(value)) {
        throw JsParserException.reasonAt(ParserErrors.DUPLICATED_ARRAY_ITEM,
                                         reader.getPositionInStream()
                                        );

      }
      array = array.append(value);
      if (arrayConstraints != null) {
        checkSize(array.size() > arrayConstraints.maxItems(),
                  ParserErrors.TOO_LONG_ARRAY.apply(arrayConstraints.maxItems()),
                  reader.getPositionInStream()
                 );
      }

    }
    if (arrayConstraints != null) {
      checkSize(array.size() < arrayConstraints.minItems(),
                ParserErrors.TOO_SHORT_ARRAY.apply(arrayConstraints.minItems()),
                reader.getPositionInStream()
               );
    }

    reader.checkArrayEnd();
    return array;
  }

  private boolean checkIfEmpty(boolean isEmpty,
                               ArraySchemaConstraints constraints,
                               long reader
                              ) {
    if (isEmpty) {
      if (constraints != null) {
        checkSize(constraints.minItems() > 0,
                  ParserErrors.EMPTY_ARRAY.apply(constraints.minItems()),
                  reader
                 );
      }
      return true;
    }
    return false;
  }

  @Override
  public JsArray value(final DslJsReader reader) throws JsParserException {
    if (isEmptyArray(reader)) {
      return EMPTY;
    }
    var array = EMPTY.append(elementReader.value(reader));
    while (reader.readNextToken() == ',') {
      reader.readNextToken();
      array = array.append(elementReader.value(reader));
    }
    reader.checkArrayEnd();
    return array;

  }


  private boolean isEmptyArray(final DslJsReader reader) throws JsParserException {
    checkSize(reader.last() != '[',
              ParserErrors.EXPECTING_FOR_ARRAY_START,
              reader.getPositionInStream()
             );
    reader.readNextToken();
    return reader.last() == ']';
  }

  public JsValue nullOrArraySuchThat(final DslJsReader reader,
                                     final Function<JsArray, JsError> fn
                                    ) throws JsParserException {

    return reader.wasNull() ?
           JsNull.NULL :
           arraySuchThat(reader,
                         fn
                        );

  }


  JsArray arraySuchThat(final DslJsReader reader,
                        final Function<JsArray, JsError> fn
                       ) throws JsParserException {

    JsArray array = value(reader);
    JsError result = fn.apply(array);
    if (result == null) {
      return array;
    }
    throw JsParserException.reasonAt(ParserErrors.JS_ERROR_2_STR.apply(result),
                                     reader.getPositionInStream()
                                    );
  }

  JsArray arrayEachSuchThat(final DslJsReader reader,
                            final Supplier<JsValue> f,
                            final ArraySchemaConstraints arrayConstraints
                           ) throws JsParserException {

    if (checkIfEmpty(isEmptyArray(reader),
                     arrayConstraints,
                     reader.getPositionInStream()
                    )) {
      return EMPTY;
    }
    JsArray array = EMPTY.append(f.get());
    while (reader.readNextToken() == ',') {
      reader.readNextToken();
      array = array.append(f.get());
      if (arrayConstraints != null) {
        checkSize(array.size() > arrayConstraints.maxItems(),
                  ParserErrors.TOO_LONG_ARRAY.apply(arrayConstraints.maxItems()),
                  reader.getPositionInStream()
                 );
      }
    }
    if (arrayConstraints != null) {
      checkSize(array.size() < arrayConstraints.minItems(),
                ParserErrors.TOO_SHORT_ARRAY.apply(arrayConstraints.minItems()),
                reader.getPositionInStream()
               );
    }

    reader.checkArrayEnd();
    return array;

  }

  private void checkSize(boolean error,
                         String message,
                         long reader
                        ) {
    if (error) {
      throw JsParserException.reasonAt(message,
                                       reader
                                      );
    }
  }

  JsValue nullOrArrayEachSuchThat(final DslJsReader reader,
                                  final Supplier<JsValue> fn,
                                  final ArraySchemaConstraints arrayConstraints
                                 ) throws JsParserException {

    return reader.wasNull() ?
           JsNull.NULL :
           arrayEachSuchThat(reader,
                             fn,
                             arrayConstraints
                            );
  }


}
