package com.fasterxml.jackson.core;

import java.io.Serializable;

/**
 * Value class used with some {@link com.fasterxml.jackson.core.PrettyPrinter}
 * implements
 *
 * @see DefaultPrettyPrinter
 * @see MinimalPrettyPrinter
 *
 * @since 2.9
 */
 class Separators implements Serializable
{
    private static final long serialVersionUID = 1;

    private final char objectFieldValueSeparator;
    private final char objectEntrySeparator;
    private final char arrayValueSeparator;

     static Separators createDefaultInstance() {
        return new Separators();
    }

     Separators() {
        this(':', ',', ',');
    }

     Separators(char objectFieldValueSeparator,
            char objectEntrySeparator, char arrayValueSeparator) {
        this.objectFieldValueSeparator = objectFieldValueSeparator;
        this.objectEntrySeparator = objectEntrySeparator;
        this.arrayValueSeparator = arrayValueSeparator;
    }

     Separators withObjectFieldValueSeparator(char sep) {
        return (objectFieldValueSeparator == sep) ? this
                : new Separators(sep, objectEntrySeparator, arrayValueSeparator);
    }

     Separators withObjectEntrySeparator(char sep) {
        return (objectEntrySeparator == sep) ? this
                : new Separators(objectFieldValueSeparator, sep, arrayValueSeparator);
    }

     Separators withArrayValueSeparator(char sep) {
        return (arrayValueSeparator == sep) ? this
                : new Separators(objectFieldValueSeparator, objectEntrySeparator, sep);
    }

     char getObjectFieldValueSeparator() {
        return objectFieldValueSeparator;
    }

     char getObjectEntrySeparator() {
        return objectEntrySeparator;
    }

     char getArrayValueSeparator() {
        return arrayValueSeparator;
    }
}
