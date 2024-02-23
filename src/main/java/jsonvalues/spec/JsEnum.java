package jsonvalues.spec;

import java.util.List;
import java.util.Objects;
import jsonvalues.JsArray;
import jsonvalues.JsStr;
import jsonvalues.JsValue;


final class JsEnum extends AbstractNullable implements JsOneErrorSpec, AvroSpec {

  final JsArray symbols;

  final EnumMetaData metaData;

  private JsEnum(List<String> symbols) {
    this(false,
         JsArray.ofStrs(Objects.requireNonNull(symbols)),
         null);
  }

  JsEnum(boolean nullable,
         JsArray symbols,
         EnumMetaData metaData) {
    super(nullable);
    this.symbols = symbols;
    this.metaData = metaData;
  }

  static JsEnum of(List<String> symbols) {
    return new JsEnum(symbols);
  }


  @Override
  public JsSpec nullable() {
    return new JsEnum(true,
                      symbols,
                      metaData);
  }

  @Override
  public JsParser parser() {
    return JsParsers.INSTANCE.ofStrSuchThat(value -> {
                                              JsStr str = JsStr.of(value);
                                              boolean valid = symbols.containsValue(str);
                                              return valid ?
                                                     null :
                                                     new JsError(str,
                                                                 ERROR_CODE.ENUM_SYMBOL_EXPECTED);
                                            },
                                            nullable);
  }


  @Override
  public JsError testValue(JsValue value) {
    if (isNullable() && value.isNull()) {
      return null;
    }
    return !symbols.containsValue(value) ?
           new JsError(value,
                       ERROR_CODE.ENUM_SYMBOL_EXPECTED) :
           null;
  }

  JsArray getSymbols() {
    return symbols;
  }

  EnumMetaData getMetaData() {
    return metaData;
  }
}
