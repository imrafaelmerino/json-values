package jsonvalues.spec;

class SpecNotSupportedInAvro extends RuntimeException {
    SpecNotSupportedInAvro(JsSpec spec, AvroAtt avroAtt) {
        super("The avro schema %s can't be created because the spec %s is not supported in Avro. ".formatted(avroAtt.name, spec.getClass().getName()));
    }

    public SpecNotSupportedInAvro(JsSpec spec) {
        super("The avro schema can't be created because the spec %s is not supported in Avro. ".formatted(spec.getClass().getName()));

    }
}
