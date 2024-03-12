**12.3.0**

- New method JsSpec.withReqKeys New methods JsObjGen.withReqKeys and JsObjGen.withNonNullValues
- Static factory methods to create specs and generators: up to 50 key-spec and key-gen pairs

**12.3.1**

- Bug: Some static factory methods were missing in JsSpec

**12.3.2**

- Improved javadoc Improved implementation of JsObjGen

**12.4.0**

- JsObjGen with optional and nullable fields, generates the whole json and with no null vales 50% of
  the times refactor some tests Improved javadoc

**12.5.0**

- upgrade java-fun library
- better optionals and nullable distribution in JsObjGen
- New methods: `JsIntGen.biased(min)` `JsLongGen.biased(min)` `JsLongGen.arbitrary(min)`
  `JsIntGen.arbitrary(min)`

**12.6.0**

- upgrade java-fun to 1.3.2

**12.7.0**

- Bug: Previous versions compiled without enabling preview features. All preview features have been
  eliminated as they are no longer necessary and were exclusively used for internal purposes.
- Eliminate compilation warnings in both source and test code.

**12.8.0**

- JsObjGen new method: `concat(JsObjGen)`

**12.9.0**

- JsObj new methods: `set(key,primitive)` `set(path,primitive)`

**13.0.0**

- added avro support for some specs with the interfaz AvroSpec (not public is used by avro-values
  proyect)
- `JsObjSpecBuilder`, `JsFixedBuilder` and `JsEnumBuilder` to facilitate integration with avro. It
  caches specs by name and now is possible to define recursive data types with
  `JsObjSpecs.ofNamedSpec`
- If spec builder is used to create specs and parsers, the metadata of the spec can be used for
  parsing, for example aliases and default values.
- added oneOf specs and parsers (JsReader now support set marks and rollback to that marks!)
- added `JsArray.of(varargs)` and `JsArray.ofXXX(list)` methods to create arrays from primitives and
  list of primitives
- added map of spec: `JsSpecs.mapOfSpec(JsSpec spec)`
- added array of spec: `JsSpecs.arrayOfSpec(JsSpec spec)`
- Refactor public classes:
  - `JsObjSpecParser.of` and `JsArraySpecParser.of` instead of constructors
  - `JsReader` visibility change (is not public)
  - `JsSpec.readNextValue(JsReader)` -> `JsSpec.parse(String)`. Method used by jio-console and was
    changed since it's not necessary to create a JsReader
  - `reduce` methods doesn't return Optional (implementation inefficient since create a lot of
    Optional objects)
  - `JsSerializerException` move to spec package to reduce visibility of constructors
- Refactor internal classes and tests (clients not affected)
- decimal specs like `JsSpecs.decimal` can parse any kind of numeric values (integers as well)
- Removed `JsSpecs.number` and related specs like `arrayOfNumbers` (use decimal instead)
- Added `JsDoubleSpec` and other related specs (`JsSpecs.arrayOfDouble` and others related)
- Removed automatic module name. I'll keep unnamed json-values because of split packages are not
  allowed in the module system and then there is a problem with avro-spec and all the libraries that
  defined the package `jsonvalues.spec` (already defined in json-values)

**13.1.0**

- New features:
  - The addition of `JsSpecs.ofNamedSpec(name, spec)`, which allows the creation of any type of
    named spec. Unlike the previous version, where only `JsObjSpec` created with `JsObjSpecBuilder`
    were cached, this new method broadens the scope of cached named specs.
  - Introduction of `JsObjSpec.concat(JsObjSpec)`, a function that facilitates the concatenation of
    Json object specs. This enhancement streamlines code reuse and makes it more convenient.
  - Enhancements to `JsObjSpecParser` and `JsArraySpecParser` to accept `NamedSpec`, providing
    increased flexibility and compatibility.
  - Refactor `JsObjGen` to support recursive generators implemented in java-fun with `NamedGen`
  - Upgrade java-fun to version 1.4.0
- Doc:
  - Introducing a new section in the readme, featuring an illustrative implementation of
    [Modeling Inheritance](https://json-schema.org/blog/posts/modelling-inheritance)."
- Issues:
  - https://github.com/imrafaelmerino/json-values/issues/195

**13.2.0**

New features:

- Previous version 13.1.0 allows named specs of any type with `JsSpecs.ofNamedSpec(name, spec)`, but
  in the case of registering `JsObjSpec`, `JsEnum` and `JsFixedBinary` with that method instead of
  the builders `JsObjSpecBuilder`, `JsEnumBuilder` and `JsFixedBinaryBuilder`, we need to create the
  metadata object to be able to create Avro schemas with avro-spec

- JsEnumBuilder new overloaded build method `build(JsArray)`

- Doc—Proofreading javadoc typos

**13.3.0**

- Backward compatible with 13.2.0 version
- Upgrade to java-fun 2.0.0.
- This java-fun version works with the new Java 17 interface `RandomGenerator` instead of `Random`,
  that is an implementation.

**13.4.0**

- Examples.java class leak in source code

**14.0.0**

Breaking changes:

- JsSpec parsers returns `JsBigInt` instead of `JsBigDec` when numbers don't have a decimal part and
  don't fit in a `long` If you are parsing strings into Json using spec parsers and have to deal
  with big integer numbers, please take this into account.
- Removed methods `biased(nBits)` and `arbitrary(nBits)` since better alternatives has been added

New features:

- `SpecToJsonSchema` to convert specs into json-schema (SchemaDraft.DRAFT_2019_09)
- `SpecToGen` to convert specs into generators
- `JsBigIntGen` new methods `biased(min, max)` and `arbitrary(min,max)`
- `Cons` spec

Bugs:

- `writeBinary` method in `JsWritter` class when binary was one byte long
