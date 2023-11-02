package jsonvalues.spec;

import java.util.Objects;

public final class MetadataNotFound extends RuntimeException {

    private static final String MESSAGE = """
            In order to transform a JsObjSpec into an Avro Schema,
            the specification should contain associated metadata, such
            as a name at the very least. To accomplish this, construct
            the `JsObjSpec` using `JsObjSpecBuilder`, allowing customization
            of this metadata to align with your specific requirements.""";

    private static final String MESSAGE_1 = """
            In order to transform an Enum into an Avro Schema,
            the specification should contain associated metadata, such
            as a name at the very least. To accomplish this, construct
            the `JsEnum` using `JsEnumBuilder`, allowing customization
            of this metadata to align with your specific requirements.""";

    private static final String MESSAGE_2 = """
            In order to transform a Fixed binary into an Avro Schema,
            the specification should contain associated metadata, such
            as a name at the very least. To accomplish this, construct
            the `JsEnum` using `JsEnumBuilder`, allowing customization
            of this metadata to align with your specific requirements.""";

    private MetadataNotFound(String message) {
        super(Objects.requireNonNull(message));
    }

    public static MetadataNotFound errorParsingJsObSpecToSchema() {
        return new MetadataNotFound(MESSAGE);
    }
    public static MetadataNotFound errorParsingEnumToSchema() {
        return new MetadataNotFound(MESSAGE_1);
    }

    public static MetadataNotFound errorParsingFixedToSchema() {
        return new MetadataNotFound(MESSAGE_2);
    }

}
