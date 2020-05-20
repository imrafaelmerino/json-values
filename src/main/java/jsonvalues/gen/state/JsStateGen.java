package jsonvalues.gen.state;

import jsonvalues.JsObj;
import jsonvalues.JsValue;
import jsonvalues.gen.JsGen;

import java.util.function.Function;

public interface JsStateGen extends Function<JsObj, JsGen<? extends JsValue>> { }
