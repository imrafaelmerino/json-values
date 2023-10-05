12.3.0 
New method JsSpec.withReqKeys
New methods JsObjGen.withReqKeys and JsObjGen.withNonNullValues
Static factory methods to create specs and generators: up to 50 key-spec and key-gen pairs

12.3.1
Bug: Some static factory methods were missing in JsSpec

12.3.2
Improved javadoc
Improved implementation of JsObjGen 

12.4.0
JsObjGen with optional and nullable fields, generates the whole json and with no null vales 50% of the times
refactor some tests
Improved javadoc

12.5.0
upgrade java-fun library
better optionals and nullable distribution in JsObjGen
New methods:
 JsIntGen.biased(min)
 JsLongGen.biased(min)
 JsLongGen.arbitrary(min)
 JsIntGen.arbitrary(min)