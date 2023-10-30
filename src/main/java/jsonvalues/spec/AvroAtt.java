package jsonvalues.spec;

import jsonvalues.JsArray;

 final class AvroAtt {

     final String name;
     final String namespace;
     final String doc;

     final JsArray aliases;

    public AvroAtt(String avroName, String avroNamespace, String avroDoc, JsArray aliases) {
        this.name = avroName;
        this.namespace = avroNamespace;
        this.doc = avroDoc;
        this.aliases = aliases;
    }
}
