package jsonvalues.spec;

import jsonvalues.JsValue;

import java.util.List;
import java.util.Map;


record MetaData(String name, String namespace, List<String> aliases, String doc, Map<String, String> fieldsDoc, Map<String, ORDERS> fieldsOrder, Map<String, List<String>> fieldsAliases, Map<String, JsValue> fieldsDefault) {

    enum ORDERS {ascending,descending,ignore}

}
