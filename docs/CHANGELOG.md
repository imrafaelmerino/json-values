# JSON-VALUES
## v7.0.0  ( Tue Mar 17 2020 11:04:03 GMT+0100 (Central European Standard Time) )


## Bug Fixes
  - 🐛 change of type of input parameter in JsArray.ofIterable(Iterator<JsValue>)
  Iterator<? extends JsValue> -> iterators of any JsValue subtype is now allowed

## Breaking changes
  - getStr renamed to getStrOpt and new getStr method returns String or null
  - getBool renamed to getBoolOpt and new getBool method returns Boolean or null
  - getInt renamed to getStrOpt and new getInt method returns Integer or null
  - getLong renamed to getStrOpt and new getLong method returns Long or null
  - getDouble renamed to getStrOpt and new getDouble method returns Double or null
  - getObj renamed to getStrOpt and new getObj method returns Obj or null
  - getArray renamed to getArrayOpt and new getArray method returns JsArray or null
  - getBigInt renamed to getBigIntOpt and new getBigInt method returns BigInteger or null
  - getBigDecimal renamed to getBigDecimalOpt and new getBigDecimal returns BigDecimal or null
  - fields() renamed to KeySet







