package jsonvalues.spec;


import java.util.List;

record EnumMetaData(String name, String namespace, List<String> aliases, String doc, String defaultSymbol){}
