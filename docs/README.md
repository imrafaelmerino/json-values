<p align="center">
  <img src="./logo/package_twitter_if9bsyj4/base/full/coverphoto/base_logo_white_background.png" alt="json-values logo"/>
</p>

<p align="center">
  <a href="https://search.maven.org/artifact/com.github.imrafaelmerino/json-values">
    <img alt="Maven Central" src="https://img.shields.io/maven-central/v/com.github.imrafaelmerino/json-values"/>
  </a>
  <a href="https://github.com/imrafaelmerino/json-values/blob/main/LICENSE">
    <img alt="License" src="https://img.shields.io/badge/license-Apache%202.0-blue"/>
  </a>
  <a href="https://www.buymeacoffee.com/imrafaelmerino">
    <img alt="Buy Me a Coffee" src="https://img.shields.io/badge/Buy%20Me%20a%20Coffee-%E2%98%95-yellow"/>
  </a>
</p>

# json-values

`json-values` is a functional Java library to model, transform, validate, parse, and generate immutable JSON values.

It is designed for teams that want:

- Immutable JSON trees (`JsObj`, `JsArray`, and primitives).
- Typed accessors and path-based updates.
- Validation with composable specs (`JsSpec`, `JsObjSpec`, `JsArraySpec`).
- Property-based data generation from specs and from manual generators.
- JSON Schema conversion and Avro-oriented metadata builders.

## Table of Contents

- [Compatibility](#compatibility)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Core JSON Types](#core-json-types)
- [Working with Paths (`JsPath`)](#working-with-paths-jspath)
- [Immutable Transformations](#immutable-transformations)
- [Validation with Specs](#validation-with-specs)
- [Parsing with Spec Parsers](#parsing-with-spec-parsers)
- [Generator Guide](#generator-guide)
- [SpecToGen Configuration](#spectogen-configuration)
- [Generator Cookbook (All Public Factories)](#generator-cookbook-all-public-factories)
- [Combinators Cookbook (All Public Methods)](#combinators-cookbook-all-public-methods)
- [JSON Schema Conversion](#json-schema-conversion)
- [Avro-Oriented Builders](#avro-oriented-builders)
- [Optics](#optics)
- [Error Handling and Debugging](#error-handling-and-debugging)
- [DX Notes and Recommendations](#dx-notes-and-recommendations)
- [Related Docs and Projects](#related-docs-and-projects)

## Compatibility

- Java: `21+`
- Build tool: Maven `3.6.3+`
- This branch currently uses `java-fun` `4.0.0-SNAPSHOT`.

## Installation

Stable release (Maven Central):

```xml
<dependency>
  <groupId>com.github.imrafaelmerino</groupId>
  <artifactId>json-values</artifactId>
  <version>14.0.0</version>
</dependency>
```

Snapshot workflow (this branch):

```xml
<dependency>
  <groupId>com.github.imrafaelmerino</groupId>
  <artifactId>json-values</artifactId>
  <version>14.0.1-SNAPSHOT</version>
</dependency>
```

Because this branch depends on `java-fun` `4.0.0-SNAPSHOT`, install `java-fun` first in local Maven cache:

```bash
cd /path/to/java-fun
mvn -q -DskipTests install
```

## Quick Start

```java
import jsonvalues.*;
import jsonvalues.spec.*;

import static jsonvalues.spec.JsSpecs.*;

JsObjSpec personSpec = JsObjSpec.of(
        "name", str(),
        "age", integer(i -> i >= 0),
        "languages", arrayOfStr()
).withOptKeys("languages");

JsObj person = JsObj.of(
        "name", JsStr.of("Rafael"),
        "age", JsInt.of(37),
        "languages", JsArray.of("Java", "Kotlin")
);

var errors = personSpec.test(person);
if (!errors.isEmpty()) {
    throw new IllegalArgumentException(errors.toString());
}

JsObj parsed = JsObjSpecParser.of(personSpec).parse(person.toString());
System.out.println(parsed.toPrettyString());
```

## Core JSON Types

Main value types:

- `JsObj`: immutable JSON object.
- `JsArray`: immutable JSON array.
- `JsStr`, `JsInt`, `JsLong`, `JsDouble`, `JsBigDec`, `JsBigInt`, `JsBool`, `JsBinary`, `JsInstant`, `JsNull`.
- `JsValue`: common abstraction.

Creation examples:

```java
JsObj obj = JsObj.of(
        "id", JsInt.of(1),
        "name", JsStr.of("Ana"),
        "active", JsBool.TRUE,
        "roles", JsArray.of("admin", "ops")
);

JsArray arr = JsArray.of(
        JsStr.of("x"),
        JsInt.of(1),
        JsObj.of("ok", JsBool.TRUE)
);

JsObj parsedObj = JsObj.parse("{\"x\":1}");
JsArray parsedArr = JsArray.parse("[1,2,3]");
```

Typed accessors (nullable and with default):

```java
String name = obj.getStr("name");
int age = obj.getInt("age", 0);
boolean active = obj.getBool("active", false);
JsArray roles = obj.getArray("roles", JsArray.empty());
```

## Working with Paths (`JsPath`)

`JsPath` lets you target nested keys and indexes with immutable updates:

```java
JsPath cityPath = JsPath.path("/address/city");
JsPath secondRole = JsPath.path("/roles/1");

JsObj source = JsObj.of(
        "address", JsObj.of("city", JsStr.of("Madrid")),
        "roles", JsArray.of("admin", "ops")
);

JsObj updated = source
        .set(cityPath, JsStr.of("Barcelona"))
        .set(secondRole, JsStr.of("platform"));

JsValue city = updated.get(cityPath);
```

Useful factories:

- `JsPath.empty()`
- `JsPath.path("/a/b/0")`
- `JsPath.fromKey("a")`
- `JsPath.fromIndex(0)`

## Immutable Transformations

Both `JsObj` and `JsArray` support functional transformation methods:

```java
JsObj transformed = obj
        .filterKeys(k -> !k.startsWith("_"))
        .mapKeys(String::toLowerCase)
        .mapValues(v -> v.isStr() ? JsStr.of(v.toJsStr().value().trim()) : v);

JsArray normalized = JsArray.of(1, 2, 3, 4)
        .mapValues(v -> JsInt.of(v.toJsInt().value() * 10))
        .filterValues(v -> v.toJsInt().value() >= 20);
```

Reduction:

```java
Integer sum = JsArray.of(1, 2, 3)
        .reduce(Integer::sum, pair -> pair.value().toJsInt().value());
```

## Validation with Specs

`JsSpecs` exposes a broad catalog of validators for primitives, arrays, maps, tuples, unions, and named specs.

### Primitive Specs

```java
import static jsonvalues.spec.JsSpecs.*;

JsSpec s1 = str();
JsSpec s2 = str(x -> !x.isBlank());
JsSpec s3 = integer(i -> i >= 0);
JsSpec s4 = longInteger();
JsSpec s5 = doubleNumber(d -> d >= 0.0);
JsSpec s6 = decimal();
JsSpec s7 = bigInteger();
JsSpec s8 = bool();
JsSpec s9 = binary();
JsSpec s10 = fixedBinary(16);
JsSpec s11 = instant();
JsSpec s12 = any(v -> v.isObj() || v.isArray());
JsSpec s13 = cons(JsStr.of("USD"));
```

### Arrays, Tuples, and Maps

```java
JsArraySpec tags = arrayOfStr();
JsArraySpec ints = arrayOfInt();
JsArraySpec decs = arrayOfDec();
JsArraySpec tupleSpec = tuple(str(), integer(), bool());

JsSpec mapOfInts = mapOfInt();
JsSpec mapOfUsers = mapOfObj(JsObjSpec.of("id", integer()));
JsArraySpec arrayOfUser = arrayOfSpec(JsObjSpec.of("id", integer()));
```

### Object Specs

```java
JsObjSpec addressSpec = JsObjSpec.of(
        "street", str(),
        "city", str(),
        "zip", str()
).withOptKeys("zip");

JsObjSpec personSpec = JsObjSpec.of(
        "name", str(),
        "age", integer(i -> i >= 0),
        "address", addressSpec
).withOptKeys("age");
```

Optional and nullable:

```java
JsObjSpec nullableCity = JsObjSpec.of(
        "city", str().nullable()
);

JsObjSpec lenient = personSpec.lenient();
```

### Union and Enumerated Values

```java
JsSpec role = oneStringOf("admin", "viewer", "ops");
JsSpec fixed = oneValOf(JsInt.of(1), JsInt.of(2), JsInt.of(3));
JsSpec userId = oneSpecOf(integer(), str());
```

### Named Specs and Recursion

```java
JsSpec registered = JsSpecs.ofNamedSpec("Money", JsObjSpec.of(
        "amount", decimal(),
        "currency", oneStringOf("EUR", "USD")
));

JsSpec lookup = JsSpecs.ofNamedSpec("Money");
```

## Parsing with Spec Parsers

Use parsers when you need parse + validate in one step.

```java
JsObjSpecParser objParser = JsObjSpecParser.of(personSpec);
JsObj person = objParser.parse("{\"name\":\"Ana\",\"age\":20,\"address\":{\"street\":\"A\",\"city\":\"B\"}}");

JsArraySpecParser arrParser = JsArraySpecParser.of(arrayOfInt());
JsArray numbers = arrParser.parse("[1,2,3]");
```

Supported inputs:

- `parse(String)`
- `parse(byte[])`
- `parse(InputStream)`

## Generator Guide

There are two main paths:

- Generate from specs: `SpecToGen`.
- Build manual generators: `Js*Gen` + `Combinators`.

### Generate from Specs

```java
SpecToGen specToGen = SpecToGen.DEFAULT;
Gen<JsObj> people = specToGen.convert(personSpec);

JsObj sample = people.sample().get();
```

Override specific paths:

```java
import fun.gen.Gen;
import java.util.Map;

Map<JsPath, Gen<? extends JsValue>> overrides = Map.of(
        JsPath.path("/age"), JsIntGen.biased(18, 99)
);

Gen<JsObj> custom = SpecToGen.DEFAULT.convert(personSpec, overrides);
```

### Validate Generated Data Against a Spec

```java
JsObjGen userGen = JsObjGen.of(
        "name", JsStrGen.alphabetic(1, 20),
        "age", JsIntGen.arbitrary(0, 120)
);

Gen<JsObj> validUsers = userGen.suchThat(personSpec);
Gen<JsObj> invalidUsers = userGen.suchThatNo(personSpec);
```

## SpecToGen Configuration

`SpecGenConfBuilder` lets you configure generation ranges and probabilities globally:

```java
SpecToGen configured = SpecToGen.of(
        new SpecGenConfBuilder()
                .withOptionalKeyProbability(4)
                .withNullableKeyProbability(3)
                .withArraySize(0, 20)
                .withObjSize(0, 20)
                .withKeyLength(1, 40)
                .withStringLength(0, 120)
                .withIntSize(-1000, 1000)
                .withLongSize(-1000L, 1000L)
                .withDoubleSize(-1000.0, 1000.0)
                .withBigDecSize(new java.math.BigDecimal("-1000"), new java.math.BigDecimal("1000"))
                .withBigIntSize(new java.math.BigInteger("-1000"), new java.math.BigInteger("1000"))
                .withBinaryLength(0, 128)
                .withInstantRange(java.time.Instant.parse("2000-01-01T00:00:00Z"), java.time.Instant.parse("2030-01-01T00:00:00Z"))
);
```

## Generator Cookbook (All Public Factories)

All examples below return `Gen<...>` values and can be sampled with `sample()` or `sample(n)`.

### `JsIntGen`

```java
Gen<JsInt> g1 = JsIntGen.arbitrary();
Gen<JsInt> g2 = JsIntGen.arbitrary(10);
Gen<JsInt> g3 = JsIntGen.arbitrary(-100, 100);
Gen<JsInt> g4 = JsIntGen.biased();
Gen<JsInt> g5 = JsIntGen.biased(10);
Gen<JsInt> g6 = JsIntGen.biased(-100, 100);
```

### `JsLongGen`

```java
Gen<JsLong> g1 = JsLongGen.arbitrary();
Gen<JsLong> g2 = JsLongGen.arbitrary(10L);
Gen<JsLong> g3 = JsLongGen.arbitrary(-1000L, 1000L);
Gen<JsLong> g4 = JsLongGen.biased();
Gen<JsLong> g5 = JsLongGen.biased(10L);
Gen<JsLong> g6 = JsLongGen.biased(-1000L, 1000L);
```

### `JsDoubleGen`

```java
Gen<JsDouble> g1 = JsDoubleGen.arbitrary();
Gen<JsDouble> g2 = JsDoubleGen.arbitrary(-100.0, 100.0);
Gen<JsDouble> g3 = JsDoubleGen.biased();
Gen<JsDouble> g4 = JsDoubleGen.biased(-100.0, 100.0);
```

### `JsBigDecGen`

```java
import java.math.BigDecimal;

Gen<JsBigDec> g1 = JsBigDecGen.arbitrary();
Gen<JsBigDec> g2 = JsBigDecGen.arbitrary(new BigDecimal("-10.5"), new BigDecimal("10.5"));
Gen<JsBigDec> g3 = JsBigDecGen.arbitrary(-100L, 100L);
Gen<JsBigDec> g4 = JsBigDecGen.biased();
Gen<JsBigDec> g5 = JsBigDecGen.biased(new BigDecimal("-10.5"), new BigDecimal("10.5"));
Gen<JsBigDec> g6 = JsBigDecGen.biased(-100L, 100L);
```

### `JsBigIntGen`

```java
import java.math.BigInteger;

Gen<JsBigInt> g1 = JsBigIntGen.arbitrary();
Gen<JsBigInt> g2 = JsBigIntGen.arbitrary(BigInteger.valueOf(-1000), BigInteger.valueOf(1000));
Gen<JsBigInt> g3 = JsBigIntGen.biased();
Gen<JsBigInt> g4 = JsBigIntGen.biased(BigInteger.valueOf(-1000), BigInteger.valueOf(1000));
```

### `JsBoolGen`

```java
Gen<JsBool> g1 = JsBoolGen.arbitrary();
```

### `JsBinaryGen`

```java
Gen<JsBinary> g1 = JsBinaryGen.arbitrary(0, 32);
Gen<JsBinary> g2 = JsBinaryGen.biased(0, 32);
```

### `JsInstantGen`

```java
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

Gen<JsInstant> g1 = JsInstantGen.arbitrary();
Gen<JsInstant> g2 = JsInstantGen.biased();
Gen<JsInstant> g3 = JsInstantGen.arbitrary(0L, 4_102_444_800L);
Gen<JsInstant> g4 = JsInstantGen.biased(0L, 4_102_444_800L);
Gen<JsInstant> g5 = JsInstantGen.biased(Instant.parse("2020-01-01T00:00:00Z"), Instant.parse("2030-01-01T00:00:00Z"));
Gen<JsInstant> g6 = JsInstantGen.arbitrary(
        ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
        ZonedDateTime.of(2030, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
);
Gen<JsInstant> g7 = JsInstantGen.biased(
        ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
        ZonedDateTime.of(2030, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
);
```

### `JsStrGen`

```java
Gen<JsStr> c1 = JsStrGen.digit();
Gen<JsStr> c2 = JsStrGen.letter();
Gen<JsStr> c3 = JsStrGen.alphabetic();
Gen<JsStr> c4 = JsStrGen.ascii();
Gen<JsStr> c5 = JsStrGen.alphanumeric();

Gen<JsStr> s1 = JsStrGen.digits(3, 8);
Gen<JsStr> s2 = JsStrGen.digits(5);
Gen<JsStr> s3 = JsStrGen.letters(3, 8);
Gen<JsStr> s4 = JsStrGen.letters(5);
Gen<JsStr> s5 = JsStrGen.alphabetic(3, 8);
Gen<JsStr> s6 = JsStrGen.alphabetic(5);
Gen<JsStr> s7 = JsStrGen.ascii(3, 8);
Gen<JsStr> s8 = JsStrGen.ascii(5);
Gen<JsStr> s9 = JsStrGen.alphanumeric(3, 8);
Gen<JsStr> s10 = JsStrGen.alphanumeric(5);
Gen<JsStr> s11 = JsStrGen.arbitrary(0, 20);
Gen<JsStr> s12 = JsStrGen.arbitrary(10);
Gen<JsStr> s13 = JsStrGen.biased(0, 20);
Gen<JsStr> s14 = JsStrGen.biased(10);
```

### `JsArrayGen`

```java
Gen<JsArray> g1 = JsArrayGen.ofN(JsIntGen.arbitrary(0, 9), 5);
Gen<JsArray> g2 = JsArrayGen.arbitrary(JsStrGen.alphanumeric(1, 8), 0, 10);
Gen<JsArray> g3 = JsArrayGen.biased(JsBoolGen.arbitrary(), 0, 10);
```

### `JsTupleGen`

```java
Gen<JsArray> g1 = JsTupleGen.of(
        JsStrGen.alphabetic(1, 8),
        JsIntGen.arbitrary(0, 100),
        JsBoolGen.arbitrary()
);

Gen<JsArray> g2 = JsTupleGen.of(java.util.List.of(
        JsStrGen.alphabetic(1, 8),
        JsIntGen.arbitrary(0, 100),
        JsBoolGen.arbitrary()
));
```

### `JsObjGen`

`JsObjGen` provides overloaded `of(...)` factories (up to 50 key-generator pairs), plus incremental building via `set(...)`.

```java
JsObjGen base = JsObjGen.of(
        "id", JsIntGen.arbitrary(1, 1_000_000),
        "name", JsStrGen.alphabetic(1, 40),
        "active", JsBoolGen.arbitrary()
);

JsObjGen withAddress = base.set("address", JsObjGen.of(
        "street", JsStrGen.alphanumeric(1, 80),
        "city", JsStrGen.alphabetic(1, 40)
));

JsObjGen tuned = withAddress
        .withOptKeys("address")
        .withNullValues("name")
        .withOptionalProbability(4)
        .withNullableProbability(4);

JsObjGen allOptional = tuned.withAllOptKeys();
JsObjGen allNullable = tuned.withAllNullValues();
JsObjGen requiredOnly = tuned.withReqKeys("id", "name");
JsObjGen nonNullable = tuned.withNonNullValues("id", "active", "address");
JsObjGen merged = tuned.concat(JsObjGen.of("source", JsStrGen.alphabetic(3, 10)));

Gen<JsObj> valid = tuned.suchThat(personSpec);
Gen<JsObj> invalid = tuned.suchThatNo(personSpec, 5_000);
```

## Combinators Cookbook (All Public Methods)

`Combinators` come from `java-fun` and are fully compatible with `json-values` generators.

```java
import fun.gen.Combinators;
import fun.gen.Gen;
import fun.tuple.Pair;
import jsonvalues.*;
import jsonvalues.gen.*;

import java.util.*;
```

### `oneOf` (constant values)

```java
Gen<String> c1 = Combinators.oneOf("A", "B", "C");
Gen<String> c2 = Combinators.oneOf(List.of("X", "Y", "Z"));
Gen<String> c3 = Combinators.oneOf(Set.of("red", "green", "blue"));
```

### `nOf` (sample without repetition)

```java
Gen<List<String>> c4 = Combinators.nOf(List.of("a", "b", "c", "d"), 2);
Gen<Set<String>> c5 = Combinators.nOf(Set.of("a", "b", "c", "d"), 2);
```

### `oneOf` (generators)

```java
Gen<JsValue> c6 = Combinators.oneOf(
        JsIntGen.arbitrary(0, 10),
        JsStrGen.alphabetic(1, 3),
        JsBoolGen.arbitrary()
);

Gen<JsValue> c7 = Combinators.oneOfList(List.of(
        JsIntGen.arbitrary(0, 10),
        JsStrGen.alphabetic(1, 3),
        JsBoolGen.arbitrary()
));
```

### `freq` (weighted selection)

```java
Gen<JsValue> c8 = Combinators.freq(
        Pair.of(1, JsIntGen.biased(-10, 10)),
        Pair.of(3, JsIntGen.arbitrary(-10, 10))
);
```

### `nullable`

```java
Gen<JsStr> c9 = Combinators.nullable(JsStrGen.alphabetic(1, 8));
Gen<JsStr> c10 = Combinators.nullable(JsStrGen.alphabetic(1, 8), 20);
```

### `combinations`

```java
Gen<Set<Integer>> c11 = Combinators.combinations(2, List.of(1, 2, 3, 4));
Gen<Set<Integer>> c12 = Combinators.combinations(2, Set.of(1, 2, 3, 4));
```

### `subsets`

```java
Gen<Set<Integer>> c13 = Combinators.subsets(List.of(1, 2, 3));
Gen<Set<Integer>> c14 = Combinators.subsets(Set.of(1, 2, 3));
```

### `shuffle` and `swap`

```java
Gen<List<Integer>> c15 = Combinators.shuffle(List.of(1, 2, 3, 4, 5));

List<Integer> xs = new ArrayList<>(List.of(10, 20, 30));
Combinators.swap(xs, 0, 2);
```

## JSON Schema Conversion

```java
JsObj schema = SpecToJsonSchema.convert(personSpec);
System.out.println(schema.toPrettyString());
```

Overloads:

- `SpecToJsonSchema.convert(JsSpec)`
- `SpecToJsonSchema.convert(JsObjSpec)`
- `SpecToJsonSchema.convert(JsArraySpec)`

## Avro-Oriented Builders

For Avro metadata and schema interoperability:

- `JsObjSpecBuilder`
- `JsEnumBuilder`
- `JsFixedBuilder`

Example:

```java
JsObjSpec invoice = JsObjSpecBuilder.withName("Invoice")
        .withNamespace("com.acme.billing")
        .withDoc("Invoice payload")
        .build(JsObjSpec.of(
                "id", JsSpecs.str(),
                "amount", JsSpecs.decimal(),
                "currency", JsSpecs.oneStringOf("EUR", "USD")
        ));
```

## Optics

`JsObj` and `JsArray` expose optics for composable access and update.

```java
import fun.optic.Lens;
import fun.optic.Option;

Lens<JsObj, String> nameLens = JsObj.lens.str("name");
Option<JsObj, Integer> ageOpt = JsObj.optional.intNum("age");

JsObj p1 = JsObj.of("name", JsStr.of("ana"), "age", JsInt.of(20));
JsObj p2 = nameLens.modify.apply(String::toUpperCase).apply(p1);
JsObj p3 = ageOpt.modify.apply(x -> x + 1).apply(p2);
```

## Error Handling and Debugging

Validation without exceptions:

```java
List<SpecError> errors = personSpec.test(person);
if (!errors.isEmpty()) {
    errors.forEach(System.out::println);
}
```

Parse + validate with exceptions:

- `JsObjSpecParser` and `JsArraySpecParser` throw `JsParserException` on parse/validation failures.
- Serialization issues may throw `JsSerializerException`.

For ad-hoc parsing/serialization, `JsIO.INSTANCE` provides direct helpers.

## DX Notes and Recommendations

- Prefer `spec.test(value)` in business logic to collect all errors.
- Prefer parsers in ingestion boundaries where fail-fast behavior is desired.
- Use `SpecToGen` + overrides to keep generators aligned with your spec while still controlling hotspots.
- Use `biased(...)` generators for bug-hunting; use `arbitrary(...)` for broad distribution coverage.
- For object generators with many optional/nullable keys, tune:
  - `withOptionalProbability(int)`
  - `withNullableProbability(int)`
- Keep paths (`JsPath`) as constants in large projects to avoid typo-driven bugs.

## Related Docs and Projects

- [CHANGELOG](./CHANGELOG.md)
- [JavaDoc Style Guide](./JAVADOC_STYLE.md)
- [JavaDoc Audit Summary](./JAVADOC_AUDIT_SUMMARY.md)
- [Quality Workflow](./QUALITY_WORKFLOW.md)
- [java-fun](https://github.com/imrafaelmerino/java-fun)
- [avro-values](https://github.com/imrafaelmerino/avro-values)
