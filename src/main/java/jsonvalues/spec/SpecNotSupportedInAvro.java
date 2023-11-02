package jsonvalues.spec;

class SpecNotSupportedInAvro extends RuntimeException {

    private static final String MESSAGE_1 = """
            Converting the JsObjSpec with name %s into an Avro Schema is not posible
            because the spec %s associated to the key %s is not Avro compliance.""";
    private static final String MESSAGE_2 = """
            Converting the JsMapOfArraySpec into an Avro Schema is not posible
            because the spec of the array %s is not Avro compliance.""";

    private static final String MESSAGE_3 = """
            Converting the OneOf into an Avro Schema is not posible
            because the spec %s at index %s is not Avro compliance.""";

    private SpecNotSupportedInAvro(String message) {
        super(message);
    }

    static SpecNotSupportedInAvro errorConvertingOneOfIntoSchema(JsSpec spec, int index) {
        return new SpecNotSupportedInAvro(MESSAGE_3.formatted(spec.getClass().getName(),index));
    }

    static SpecNotSupportedInAvro errorConvertingObjSpecIntoSchema(String key,
                                                                   JsSpec spec,
                                                                   MetaData metaData
                                                                  ) {
        return new SpecNotSupportedInAvro(MESSAGE_1.formatted(metaData.name(),
                                                              spec.getClass().getName(),
                                                              key));
    }

    static SpecNotSupportedInAvro errorConvertingMapOfArraySpecIntoSchema(JsArraySpec spec
                                                                         ) {
        return new SpecNotSupportedInAvro(MESSAGE_2.formatted(spec.getClass().getName()));
    }
}
