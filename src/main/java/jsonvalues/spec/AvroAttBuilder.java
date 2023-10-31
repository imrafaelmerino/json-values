package jsonvalues.spec;

import jsonvalues.JsArray;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class AvroAttBuilder {

    private static final String NAME_REGEX = "[A-Za-z_][A-Za-z0-9_.]*";
    private static final Pattern AVRO_NAME_PATTERN = Pattern.compile(NAME_REGEX);
    public static Predicate<String> isValidAvroName =
            name -> AVRO_NAME_PATTERN.matcher(name).matches();
    private final String name;
    private String namespace;
    private String doc;
    private JsArray aliases;

    private AvroAttBuilder(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public static AvroAttBuilder of(String name) {
        if (isValidAvroName.test(name)) {
            return new AvroAttBuilder(name);
        }
        throw new IllegalArgumentException("The name %s doesn't follow the regex %s".formatted(name, NAME_REGEX));
    }


    public AvroAttBuilder withNamespace(String namespace) {
        if (!isValidAvroName.test(Objects.requireNonNull(namespace))) {
            throw new IllegalArgumentException("The namespace %s doesn't follow the regex %s".formatted(namespace, NAME_REGEX));
        }
        this.namespace = namespace;
        return this;
    }

    public AvroAttBuilder withDoc(String doc) {
        this.doc = Objects.requireNonNull(doc);
        return this;
    }

    public AvroAttBuilder withAliases(List<String> aliases) {

        aliases.stream()
               .filter(isValidAvroName.negate())
               .findFirst()
               .ifPresent(alias -> {
                              throw new IllegalArgumentException("The alias %s doesn't follow the regex %s".formatted(name, NAME_REGEX));
                          }
                         );
        this.aliases = JsArray.ofStrs(aliases);

        return this;
    }

    AvroAtt build() {
        return new AvroAtt(name, namespace, doc, aliases);
    }
}