package jsonvalues.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import jsonvalues.JsBigDec;
import jsonvalues.JsBigInt;
import jsonvalues.JsDouble;
import jsonvalues.JsInt;
import jsonvalues.JsLong;
import jsonvalues.JsObj;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestsEquals {


  @Test
  public void testJsBigIntEquals() {

    Assertions.assertEquals(JsBigInt.of(BigInteger.TEN),
                            JsDouble.of(10d));
    Assertions.assertEquals(JsBigInt.of(BigInteger.TEN),
                            JsInt.of(10));

  }

  @Test
  public void testJsDoubleEquals() {

    Assertions.assertEquals(JsDouble.of(1.00d),
                            JsInt.of(1));

    Assertions.assertEquals(JsDouble.of(1.00d),
                            JsLong.of(1));

    Assertions.assertEquals(JsDouble.of(1.00d),
                            JsBigDec.of(BigDecimal.ONE));

    Assertions.assertEquals(JsDouble.of(1.00d),
                            JsBigInt.of(BigInteger.ONE));
  }


  public void test(){



    var json1 = """
        {"binary":null,"enum":["a","b","a","a","b","a","a","b","a","a"],"instant":["-682416459-09-19T14:30:47Z","-776087424-11-10T13:25:08Z","-552314144-04-27T10:03:08Z","-917921567-01-23T00:51:27Z","-323808584-06-26T08:41:02Z","-221054485-09-06T11:48:41Z","-708063544-09-05T10:45:41Z","-414241437-05-10T13:28:29Z","-974540747-01-28T18:49:18Z","-805927558-12-16T02:53:10Z"],"boolean":[false,false,true,true,true,false,true,false,false,true],"bigint":[0,3,5,5,0,0,3,4,7,3],"int":[-718602114,1114258522,-1214453429,1106272122,-1441639603,240848965,862338630,466068436,-323589930,315209385],"str":["L","A","d","Z","k","j","F","G","a","Y"],"decimal":null,"double":[0.17696393573203806,0.5506228842388637,0.3908085586188099,0.8586861581425229,0.3928822831156801,0.34439497913627837,0.890467648954047,0.8538601975448448,0.929195032378487,0.723926867804456],"fixed":["6yo=","LOY=","+/I=","7T8=","a3Q=","pMA=","eOg=","VeI=","NOU=","q84="],"long":[3576738060007192233,-4084870707152120485,4649605247245686077,-1807803483677604154,-4715552503800542953,5505780189314228243,2125935776715406346,5177499343115736339,1204721567610956737,-745285182779464652]}
        """;

    var json2 = """
        {"binary":null,"enum":["a","b","a","a","b","a","a","b","a","a"],"instant":["-682416459-09-19T14:30:47Z","-776087424-11-10T13:25:08Z","-552314144-04-27T10:03:08Z","-917921567-01-23T00:51:27Z","-323808584-06-26T08:41:02Z","-221054485-09-06T11:48:41Z","-708063544-09-05T10:45:41Z","-414241437-05-10T13:28:29Z","-974540747-01-28T18:49:18Z","-805927558-12-16T02:53:10Z"],"boolean":[false,false,true,true,true,false,true,false,false,true],"bigint":[0,3,5,5,0,0,3,4,7,3],"int":[-718602114,1114258522,-1214453429,1106272122,-1441639603,240848965,862338630,466068436,-323589930,315209385],"str":["L","A","d","Z","k","j","F","G","a","Y"],"decimal":null,"double":[0.1769639402627945,0.5506228804588318,0.3908085525035858,0.8586861491203308,0.3928822875022888,0.3443949818611145,0.890467643737793,0.8538601994514465,0.9291950464248657,0.723926842212677],"fixed":["6yo=","LOY=","+/I=","7T8=","a3Q=","pMA=","eOg=","VeI=","NOU=","q84="],"long":[3576738060007192233,-4084870707152120485,4649605247245686077,-1807803483677604154,-4715552503800542953,5505780189314228243,2125935776715406346,5177499343115736339,1204721567610956737,-745285182779464652]}
        """;

    Assertions.assertEquals(JsObj.parse(json1),JsObj.parse(json2));

  }


}
