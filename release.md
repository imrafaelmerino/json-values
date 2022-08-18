VERSION 11.7.0

**REFACTOR**

JsObjSpec.strict  -> JsObjSpec.of

**CHANGES**

JsObjSpec.lenient factory methods have been deleted. To create strict ones with the 
static factory method of and then call the lenient method:

```code  

JsObSpec strictSpec = JsObjSpec.of(...);

JsObjSpec lenientSpec = strictSpec.lenient();

```

**IMPROVEMENTS**

New validation when creating JsObjSpec. The required and optional fields must
exist, otherwise an IllegalArgumentException is thrown

New validation when creating JsObjGen. The nullable and optional fields must
exist, otherwise an IllegalArgumentException is thrown