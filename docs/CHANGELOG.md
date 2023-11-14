12.3.0
New method JsSpec.withReqKeys
New methods JsObjGen.withReqKeys and JsObjGen.withNonNullValues
Static factory methods to create specs and generators: up to 50 key-spec and key-gen pairs

12.3.1
Bug: Some static factory methods were missing in JsSpec

12.3.2
Improved javadoc
Improved implementation of JsObjGen

12.4.0
JsObjGen with optional and nullable fields, generates the whole json and with no null vales 50% of the times
refactor some tests
Improved javadoc

12.5.0
- upgrade java-fun library
- better optionals and nullable distribution in JsObjGen
- New methods:
  `JsIntGen.biased(min)`
  `JsLongGen.biased(min)`
  `JsLongGen.arbitrary(min)`
  `JsIntGen.arbitrary(min)`

12.6.0
- upgrade java-fun to 1.3.2

12.7.0
- Bug: Previous versions compiled without enabling preview features. All preview features have been eliminated as they are
  no longer necessary and were exclusively used for internal purposes.
- Eliminate compilation warnings in both source and test code.

12.8.0
- JsObjGen new method:
   `concat(JsObjGen)`

12.9.0
- JsObj new methods: 
   `set(key,primitive)`
   `set(path,primitive)`

13.0.0
- added avro support for some specs with the interfaz AvroSpec (not public is used by avro-values proyect)
- `JsObjSpecBuilder`, `JsFixedBuilder` and `JsEnumBuilder` to facilitate integration with avro. It
   caches specs by name and now is possible to define recursive data types with `JsObjSpecs.ofNamedSpec`
- If spec builder is used to create specs and parsers, the metadata of the spec
  can be used for parsing, for example aliases and default values.
- added oneOf specs and parsers (JsReader now support set marks and rollback to that marks!)
- added `JsArray.of(varargs)` and `JsArray.ofXXX(list)`  methods to create arrays from primitives and list of primitives
- added map of spec: `JsSpecs.mapOfSpec(JsSpec spec)`
- added array of spec: `JsSpecs.arrayOfSpec(JsSpec spec)`
- Refactor public classes: 
    - `JsObjSpecParser.of` and `JsArraySpecParser.of` instead of constructors
    - `JsReader` visibility change (is not public)
    -  `JsSpec.readNextValue(JsReader)` -> `JsSpec.parse(String)`. Method used by jio-console and was changed
        since it not necessary to create a JsReader
    - `reduce` methods doesn't return Optional (implementation inefficient since create a lot of Optional objects)
    - `JsSerializerException` move to spec package to reduce visibility of constructors
- Refactor internal classes and tests (clients not affected)
- decimal specs like `JsSpecs.decimal` can parse any kind of numeric values (integers as well)
- Removed `JsSpecs.number` and related specs like `arrayOfNumbers`  (use decimal instead)
- Added `JsDoubleSpec` and other related specs (`JsSpecs.arrayOfDouble` and others related)
- Removed automatic module name. I'll keep unnamed json-values because of split packages are
  not allowed in the module system and then there is a problem with avro-spec and all the libraries
  that defined the package `jsonvalues.spec` (already defined in json-values)