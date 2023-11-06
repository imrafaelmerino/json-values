package jsonvalues.spec;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.AvroUtils.*;

public final class JsFixedBuilder {
    private final String name;

    private String nameSpace;
    private List<String> aliases;

    private JsFixedBuilder(String name) {
        //validate name
        this.name = name;
    }

    public static JsFixedBuilder name(String name) {
        if (!isValidName.test(name))
            throw new IllegalArgumentException("The name of the Fixed binary %s doesn't follow the pattern %s ".formatted(name, AVRO_NAME_PATTERN));
        return new JsFixedBuilder(name.formatted());
    }

    public JsFixedBuilder namespace(String nameSpace) {
        this.nameSpace = requireNonNull(nameSpace);
        if (!isValidNamespace.test(nameSpace))
            throw new IllegalArgumentException("The namespace of the Fixed binary with name %s doesn't follow the pattern %s".formatted(name, AVRO_NAMESPACE_PATTERN));

        return this;
    }

    public JsFixedBuilder aliases(List<String> aliases) {
        this.aliases = requireNonNull(aliases);
        for (String alias : aliases) {
            if (!isValidName.test(alias)) {
                throw new IllegalArgumentException("The alias %s of the Fixed binary with name %s doesn't follow the pattern %s".formatted(alias,
                                                                                                                                           name,
                                                                                                                                           AVRO_NAME_PATTERN));
            }

        }
        return this;
    }


    public JsFixedBinary build(final int size) {
        if (size <= 0) throw new IllegalArgumentException("size < 0");
        var metadata = new FixedMetaData(name, nameSpace, aliases);
        return new JsFixedBinary(false, size, metadata);

    }
}