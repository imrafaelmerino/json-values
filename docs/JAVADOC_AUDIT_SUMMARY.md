# JavaDoc Audit Summary

Date: 2026-03-31

## Scope Reviewed

- Public methods in public API classes with priority on:
  - `jsonvalues.spec.JsSpecs`
  - `jsonvalues.spec.JsIO`
  - `jsonvalues.spec.JsObjSpecParser`
  - `jsonvalues.spec.JsArraySpecParser`
  - `jsonvalues.JsObj`
  - `jsonvalues.JsOptics`
  - `jsonvalues.spec.SpecGenConfBuilder`

## Main Incongruences Found

1. Return semantics mismatch in JavaDoc.
- Methods returning `Lens` documented as `@return an optional`.

2. Type semantics mismatch.
- Double-related methods documented with `long` wording.
- JSON array parser docs mentioning JSON object errors.

3. API abstraction mismatch.
- Docs mentioning `Try`-style wrapping where API actually throws exceptions.

4. Parameter naming/style inconsistency.
- `inputstream` vs `inputStream`.
- Typos and inconsistent type labels (`JsOb`, lowercase `json`).

5. Copy-paste drift in descriptions.
- Scalar methods described as if they were array element validators.

## Improvements Applied

### 1) Spec API (`JsSpecs`)

- Corrected semantic error-code documentation alignment for `doubleNumber(DoublePredicate)`.
- Updated wording to match actual behavior (double vs long, instant vs long).
- Added missing null checks in several schema overloads for more predictable API behavior.
- Added naming-consistent aliases:
  - `mapOfInt()` / `mapOfInt(IntegerSchema)`
  - `mapOfBigInt()` / `mapOfBigInt(BigIntSchema)`
- Corrected grammar/typos in `mapOf*` JavaDocs.

### 2) Parser API (`JsIO`, `JsObjSpecParser`, `JsArraySpecParser`)

- Fixed `@throws` descriptions to reference correct JSON type (`object` vs `array`).
- Removed `Try` terminology from parser JavaDocs and aligned with actual throw-based behavior.
- Renamed params in docs/signatures for consistency (`inputStream`).

### 3) Core API (`JsObj`, `JsOptics`, `SpecGenConfBuilder`)

- Fixed `JsObj` parse docs (`JsOb` typo, bytes/string throw conditions).
- Normalized `getObj` JavaDoc to clearly state null/default semantics.
- In `JsOptics`, corrected many Lens docs from `optional` to `lens` where applicable and improved wording consistency.
- Standardized terminology (`JSON` uppercase where updated).

## Validation

- Compilation check passed after updates:
  - `mvn -q -DskipTests test-compile`

## Remaining Opportunities

- Complete a full-class pass for all public classes under `jsonvalues` and `jsonvalues.gen` to enforce the new guide uniformly.
- Normalize style in legacy docs with lowercase `json` wording where still present.
- Add CI checks (or lint script) for common JavaDoc anti-patterns identified in this audit.
