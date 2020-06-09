package jsonvalues;

import jsonvalues.console.JsArrayConsole;
import jsonvalues.console.JsObjConsole;

import java.util.concurrent.ExecutionException;

import static jsonvalues.console.JsIOs.read;
import static jsonvalues.spec.JsSpecs.*;

public class TestJsConsole {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        JsObjConsole obj = JsObjConsole.of("a",
                                           read(str),
                                           "b",
                                           JsObjConsole.of("c",
                                                           read(integer),
                                                           "d",
                                                           read(bool),
                                                           "e",
                                                           JsObjConsole.of("f",
                                                                           read(arrayOfInt)
                                                                          )
                                                          ),
                                           "g",
                                           JsArrayConsole.tuple(read(integer),
                                                                read(str)
                                                               )
                                          );

        obj.exec("---------JSON-VALUES---------\n",
                 o -> "\nAnd the result is:" + o.toString() + "\n");


    }

}
