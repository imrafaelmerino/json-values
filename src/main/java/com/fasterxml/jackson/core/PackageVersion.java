package com.fasterxml.jackson.core;

/**
 * Automatically generated from PackageVersion.java.in during
 * packageVersion-generate execution of maven-replacer-plugin in
 * pom.xml.
 */
 final class PackageVersion implements Versioned {
     final static Version VERSION = VersionUtil.parseVersion(
            "2.13.3", "com.fasterxml.jackson.core", "jackson-core");

    @Override
    public Version version() {
        return VERSION;
    }
}
