package jsonvalues;

import jsonvalues.console.JsArrayIO;
import jsonvalues.console.JsObjIO;

import java.util.concurrent.ExecutionException;

import static jsonvalues.console.JsIOs.read;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsConsole {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        JsObjIO obj = JsObjIO.of("a",
                                 read(str),
                                 "b",
                                 JsObjIO.of("c",
                                            read(integer),
                                            "d",
                                            read(bool),
                                            "e",
                                            JsObjIO.of("f",
                                                       read(arrayOfInt)
                                                      )
                                           ),
                                 "g",
                                 JsArrayIO.of(read(integer),
                                              read(str)
                                             )
                                );

        obj.exec("---------JSON-VALUES---------\n",
                 o -> "\nAnd the result is:" + o.toString() + "\n");


    }

}
