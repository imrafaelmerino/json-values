package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.List;
import java.util.Map;

record EnumMetaData(String name, String namespace, List<String> aliases, String doc, String defaultSymbol){}
