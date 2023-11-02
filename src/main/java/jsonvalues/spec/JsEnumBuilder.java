package jsonvalues.spec;

import jsonvalues.JsArray;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static jsonvalues.spec.AvroUtils.*;

public final class JsEnumBuilder {
    private String name;
    private String doc;

    private String defaultSymbol;
    private String nameSpace;
    private List<String> aliases;

    private JsEnumBuilder(final String name) {
        if (!isValidName.test(name))
            throw new IllegalArgumentException("The name of the Enum with name %s doesn't follow the pattern %s ".formatted(name, AVRO_NAME_PATTERN));

        this.name = name;
    }

    public static JsEnumBuilder name(final String name) {
        return new JsEnumBuilder(name);
    }

    public JsEnumBuilder aliases(List<String> aliases) {
        this.aliases = requireNonNull(aliases);
        for (String alias : aliases) {
            if (!isValidName.test(alias)) {
                throw new IllegalArgumentException("The alias %s of the Enum with name %s doesn't follow the pattern %s".formatted(alias,
                                                                                                                                   name,
                                                                                                                                   AVRO_NAME_PATTERN));
            }

        }
        return this;
    }

    public JsEnumBuilder namespace(final String nameSpace) {
        this.nameSpace = requireNonNull(nameSpace);
        if (!isValidNamespace.test(nameSpace))
            throw new IllegalArgumentException("The namespace of the Enum with name %s doesn't follow the pattern %s".formatted(name,
                                                                                                                                AVRO_NAMESPACE_PATTERN));

        return this;
    }

    public JsEnumBuilder doc(final String doc) {
        this.doc = requireNonNull(doc);
        return this;
    }

    public JsEnumBuilder defaultSymbol(final String symbol) {
        this.defaultSymbol = requireNonNull(symbol);
        return this;
    }


    public JsEnum symbols(final List<String> symbols) {
        if (defaultSymbol != null && !symbols.contains(defaultSymbol))
            throw new IllegalArgumentException("Default symbol %s must be contained in the list of possible symbols of the enum.".formatted(defaultSymbol));
        var metadata = new EnumMetaData(name, nameSpace, aliases, doc, defaultSymbol);
        return new JsEnum(false, JsArray.ofStrs(symbols), metadata);

    }
}