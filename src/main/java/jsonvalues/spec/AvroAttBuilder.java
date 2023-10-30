package jsonvalues.spec;

import jsonvalues.JsArray;

public final class AvroAttBuilder {
    private AvroAttBuilder(String name) {
        this.name = name;
    }

    private final String name;
    private String namespace;
    private String doc;
    private JsArray aliases;

    public static AvroAttBuilder of(String name) {
        return new AvroAttBuilder(name);
    }


    public AvroAttBuilder withNamespace(String avroNamespace) {
        this.namespace = avroNamespace;
        return this;
    }

    public AvroAttBuilder withDoc(String avroDoc) {
        this.doc = avroDoc;
        return this;
    }

    public AvroAttBuilder withAliases(JsArray aliases) {
        this.aliases = aliases;
        return this;
    }

    AvroAtt build() {
        return new AvroAtt(name, namespace, doc, aliases);
    }
}