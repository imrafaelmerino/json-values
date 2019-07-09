import jsonvalues.{AbstractJsObj, JsStr}


//(1 to 5).foreach(it => println(new JsonGen().strGen.sample))
//(1 to 5).foreach(it => println(new JsonGen().intGen.sample))
//(1 to 5).foreach(it => println(new JsonGen().boolGen.sample))
//(1 to 5).foreach(it => println(new JsonGen().jsonArrStrGen.sample))
//(1 to 5).foreach(it => println(new JsonGen().jsonValueGen.sample))
//val gen = JsonGen().jsObjGen
//(1 to 10).foreach(it => {
//  println(it)
//  gen.sample.foreach(it=>println(it))
//})


//val x = "{\"u\":2529768981959829072,\"p\":\"p\",\"o\":{\"j\":\"v\",\"t\":\"r\",\"f\":[\"d\",\"e\",\"e\",\"g\"," +
//          "\"x\"],\"z\":\"z\"},\"d\":{\"t\":-1,\"u\":\"y\",\"m\":\"k\",\"k\":\"r\",\"o\":\"t\"}}"
//AbstractJsObj.parse(x).mapElems_(it => JsStr.parse("mapping is fun!"),
//                         p => p.isStr
//                           )



