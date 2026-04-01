**14.0.1-SNAPSHOT**

- Build: sanitized and reordered `pom.xml` for maintainability.
- Build: upgraded key Maven plugins (`compiler`, `surefire`, `jar`, `javadoc`, `source`, `gpg`, `nexus-staging`).
- Build: added `maven-enforcer-plugin` to enforce Java 21+ and Maven 3.6.3+.
- Build: switched compiler configuration from `source/target` to `release=21`.
- Dependencies: upgraded test dependency to `junit-jupiter` `5.11.3`.
- Dependencies: upgraded `java-fun` dependency to `4.0.0-SNAPSHOT`.
- Docs: reformatted and expanded README and changelog content.

**14.0.0**

- Build: Error Prone fixes and plugin dependency upgrades in `pom.xml`.

**14.0.0-RC3**

- Improvement: `JsSpecs.mapOfXXX` specs now report correct error paths in `test`.
- Improvement: `JsSpecs.mapOfXXX` specs now return multiple error events when multiple errors exist.

**14.0.0-RC2**

- Fix: `SpecToGen.convert` return type adjusted to avoid wildcard return type usage.

**14.0.0-RC1**

- Breaking: spec parsers return `JsBigInt` (instead of `JsBigDec`) for integer numbers that do not fit in `long`.
- Breaking: removed `biased(nBits)` and `arbitrary(nBits)` in favor of improved alternatives.
- Feature: added `SpecToJsonSchema` (draft `2019-09`) to convert specs to JSON Schema.
- Feature: added `SpecToGen` to convert specs into generators.
- Feature: added `JsBigIntGen.biased(min, max)` and `JsBigIntGen.arbitrary(min, max)`.
- Feature: added `Cons` spec.
- Fix: fixed `writeBinary` behavior in `JsWritter` when binary length is one byte.

**13.4.0**

- Fix: removed accidental `Examples.java` source leak.

**13.3.0**

- Compatibility: backward compatible with `13.2.0`.
- Dependencies: upgraded to `java-fun` `2.0.0`.
- Platform: migrated from `Random` to Java 17 `RandomGenerator` API.

**13.2.0**

- Feature: improved named-spec workflow for Avro integration when using `JsSpecs.ofNamedSpec(name, spec)`.
- Feature: added overloaded `JsEnumBuilder.build(JsArray)`.
- Docs: proofreading and javadoc typo fixes.

**13.1.0**

- Feature: added `JsSpecs.ofNamedSpec(name, spec)` for named specs of any type.
- Feature: added `JsObjSpec.concat(JsObjSpec)` to compose object specs.
- Feature: `JsObjSpecParser` and `JsArraySpecParser` now accept named specs.
- Feature: refactored `JsObjGen` to support recursive generators using `NamedGen` from `java-fun`.
- Dependencies: upgraded to `java-fun` `1.4.0`.
- Docs: added a new README section with a modeling inheritance example.
- Tracking: issue [#195](https://github.com/imrafaelmerino/json-values/issues/195).

**13.0.0**

- Feature: added Avro support through `AvroSpec` (used by `avro-values`).
- Feature: added `JsObjSpecBuilder`, `JsFixedBuilder`, and `JsEnumBuilder` for Avro-friendly spec definitions.
- Feature: support for recursive data types through named specs and builder caches.
- Feature: builder metadata can be reused by parsers (aliases, default values, etc.).
- Feature: added `oneOf` specs and parsers with reader marks and rollback support.
- Feature: added `JsArray.of(varargs)` and `JsArray.ofXXX(list)` constructors.
- Feature: added `JsSpecs.mapOfSpec(JsSpec)` and `JsSpecs.arrayOfSpec(JsSpec)`.
- Refactor: `JsObjSpecParser.of` and `JsArraySpecParser.of` replace constructors.
- Refactor: `JsReader` visibility reduced from public API.
- Refactor: `JsSpec.readNextValue(JsReader)` replaced with `JsSpec.parse(String)`.
- Refactor: `reduce` methods no longer return `Optional` to avoid allocation overhead.
- Refactor: moved `JsSerializerException` to `spec` package to reduce constructor visibility.
- Refactor: internal classes and tests reorganized (no client-facing impact).
- Improvement: decimal specs can parse integer numbers as decimals.
- Removal: removed `JsSpecs.number` and related specs (use decimal-based specs instead).
- Feature: added `JsDoubleSpec` and related helpers (`JsSpecs.arrayOfDouble`, etc.).
- Module: removed automatic module name to avoid split-package conflicts with Avro-related libraries.

**12.9.0**

- Feature: added `JsObj.set(key, primitive)` and `JsObj.set(path, primitive)`.

**12.8.0**

- Feature: added `JsObjGen.concat(JsObjGen)`.

**12.7.0**

- Fix: removed unnecessary preview-feature dependency in build setup.
- Improvement: eliminated compilation warnings in source and tests.

**12.6.0**

- Dependencies: upgraded `java-fun` to `1.3.2`.

**12.5.0**

- Dependencies: upgraded `java-fun`.
- Improvement: better optional/nullable distribution in `JsObjGen`.
- Feature: added `JsIntGen.biased(min)`, `JsLongGen.biased(min)`, `JsLongGen.arbitrary(min)`, and `JsIntGen.arbitrary(min)`.

**12.4.0**

- Feature: `JsObjGen` support for optional and nullable fields.

**12.3.2**

- Docs: javadoc improvements.
- Improvement: internal implementation improvements in `JsObjGen`.

**12.3.1**

- Fix: restored missing static factory methods in `JsSpec`.

**12.3.0**

- Feature: added `JsSpec.withReqKeys`.
- Feature: added `JsObjGen.withReqKeys` and `JsObjGen.withNonNullValues`.
- Feature: expanded static factory methods for specs and generators up to 50 key/spec pairs.
