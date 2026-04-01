# Quality Workflow

This project provides three complementary quality lanes:

1. Fast validation (`test`)
2. Concurrency stress (`stress`)
3. Mutation testing (`mutation`)

## 1. Fast validation

Run the default test suite:

```bash
mvn test
```

## 2. Concurrency stress

Stress tests are tagged with `@Tag("stress")` and executed through a dedicated profile:

```bash
mvn -Pstress test
```

Current stress suites include parser and Avro-related cache/builder contention tests:

- `jsonvalues.spec.JsParsersConcurrencyTest`
- `jsonvalues.spec.AvroBuildersConcurrencyTest`

## 3. Mutation testing

Mutation testing is configured under the `mutation` profile.

Default scope (currently tuned for `NumberConverter*`):

```bash
mvn -Pmutation org.pitest:pitest-maven:mutationCoverage
```

Custom scope example:

```bash
mvn -Pmutation \
  -Dpit.targetClasses='jsonvalues.spec.*' \
  -Dpit.targetTests='jsonvalues.spec.*Test' \
  org.pitest:pitest-maven:mutationCoverage
```

## Suggested CI order

1. `mvn test`
2. `mvn -Pstress test` (nightly or pre-release)
3. `mvn -Pmutation ...` (nightly or targeted campaigns)
