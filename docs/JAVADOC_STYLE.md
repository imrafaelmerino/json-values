# JavaDoc Style Guide

This guide defines how we write JavaDoc for public API in `json-values`.

## Goals

- Help humans use the API correctly on first read.
- Help AI agents infer method behavior, nullability, and failure modes.
- Keep docs aligned with real behavior (no aspirational docs).

## Scope

- Required for all `public` methods in `public` classes/records/enums.
- Strongly recommended for constructors and constants that are part of API usage.

## Writing Rules

1. Start with behavior, not implementation.
- Good: `Returns the JSON object at the given key or null if missing.`
- Avoid: `Gets value from internal map.`

2. Use precise API terms.
- Prefer `JSON object`, `JSON array`, `null`, `throws`.
- Avoid ambiguous wording like `value`, `data`, `thing` unless context is obvious.

3. Document nullability explicitly.
- For params: state if `null` is accepted.
- For return values: state if `null` can be returned and when.

4. Keep `@param` aligned with method signature semantics.
- Name and meaning must match method behavior exactly.
- Avoid copy/paste drift (`array` wording on non-array methods, etc.).

5. Keep `@return` type-accurate.
- If method returns `Lens`, JavaDoc must say `a lens`.
- If method returns `Option`, JavaDoc must say `an optional`.

6. Document thrown exceptions by condition.
- Prefer: `@throws JsParserException if the bytes do not represent a JSON array`.
- Avoid generic phrases like `if parsing fails` when a stronger condition is known.

7. Use consistent naming in docs and parameters.
- Prefer `inputStream` over `inputstream`.
- Match capitalization and terminology with the rest of API.

8. Do not promise abstractions not present in implementation.
- If method throws, do not mention `Try` or wrapper types unless actually returned.

9. Keep docs concise.
- One sentence summary + focused tags.
- Add detail only when needed to avoid misuse.

10. For overloads, explain differences.
- Especially when overloads differ only by schema type or default behavior.

## Template

```java
/**
 * One-line behavior summary in present tense.
 *
 * @param x meaning of x, nullability, constraints
 * @param y meaning of y, nullability, constraints
 * @return what is returned, including nullability
 * @throws SomeException exact condition(s) that trigger it
 */
```

## API Consistency Conventions

- Use `JSON` uppercase in prose.
- Prefer `object` / `array` over generic `value` when type is known.
- If method returns a default from supplier, document when supplier is used.
- If method is total (never throws for lookup), mention fallback behavior.

## Known Pitfalls to Avoid

- Wrong error-code wording (`double` method documented as `long`).
- Wrong structure wording (`JSON array` method documented as `JSON object`).
- Wrong return wording (`@return an optional` for lens methods).
- Copy-paste parameter wording (`schema for array element` on scalar method).

## Review Checklist (PR)

- Does summary describe behavior users observe?
- Are `@param` tags accurate and complete?
- Is `@return` correct for nullability and type semantics?
- Are `@throws` conditions concrete and true?
- Are overloads distinguishable?
- Is terminology consistent with this guide?

