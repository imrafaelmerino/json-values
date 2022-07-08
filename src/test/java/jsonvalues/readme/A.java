package jsonvalues.readme;


import jsonvalues.*;
import java.util.function.*;



public class A {

    public static void main(String[] args) {
        JsObj json = JsObj.parse("...");

        Function<String,String> toSnakeCase = s -> "...";
        json.mapKeys(toSnakeCase);

        Function<JsPrimitive, JsValue> trim =
                s -> s.isStr() ? s.toJsStr().map(String::trim) : s;
        json.mapValues(trim);

        json.mapValues(JsStr.prism.modify.apply(String::trim));
    }
}
