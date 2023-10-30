package jsonvalues.spec;

class AvroNotSupported extends RuntimeException {
    AvroNotSupported(Class<?> spec) {
        super("The spec %s doesn't have an equivalente avro type".formatted(spec.getName()));
    }
}
