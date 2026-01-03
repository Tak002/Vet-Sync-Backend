# Copilot Review Instructions

## Purpose
This repository uses GitHub Copilot **as a code reviewer**, not as a code generator.

The primary goal of reviews is to:
- Detect **logical bugs**
- Prevent **data integrity issues**
- Ensure **transactional safety**
- Maintain **API and domain consistency**

Copilot should prioritize **correctness and safety over style or verbosity**.

---

## Authoritative References (Must Be Considered)
When reviewing code, Copilot **must treat the following documents as authoritative**:

- `.github/docs/architecture.md`  
  → Overall system structure, module responsibilities, layering rules

- `.github/docs/api-conventions.md`  
  → REST conventions, URL design, request/response rules, error handling style

- `.github/docs/coding-style.md`  
  → Language-level conventions (Java, Spring, JPA), DTO vs Entity boundaries

- `.github/docs/security.md`  
  → Authentication, authorization, JWT handling, data exposure rules

If a PR **conflicts with these documents**, it must be flagged **even if the code works**.

---

## Review Principles
When reviewing a PR, always follow this order:

1. **Correctness over Style**
2. **Domain & Data Integrity over Convenience**
3. **Explicit failure over implicit DB errors**
4. **Minimal but precise feedback**

Avoid long explanations.  
Prefer: **Problem → Why it matters → Minimal fix suggestion**

---

## Language
- All review comments **must be written in Korean**
- Code elements (class, method, enum names) should remain in English
- Do not translate identifiers or code

---

## High-Priority Review Areas

### 1. Data Integrity & Persistence
Check carefully for:
- JPA ↔ DB schema mismatches  
  (ENUM, JSONB, nullable columns, FK, UNIQUE constraints)
- Repository method names that **do not match actual entity fields**
- Accidental misuse of IDs  
  (e.g. mixing `patientId` / `staffId` / `hospitalId`)
- Use of `EntityManager.getReference()` **without prior existence checks**

⚠️ Database constraints must be treated as a **last line of defense**, not normal control flow.

(Refer to `architecture.md`, `coding-style.md`)

---

### 2. Transactions
- All **write operations** must be inside a transaction
- Read-only queries should use `@Transactional(readOnly = true)`
- Watch for:
    - Exceptions caught but not rethrown → commit-time failures
    - Logic relying on dirty checking **outside** transaction boundaries

If a method mutates state and has no transaction → **flag it**.

(Refer to `architecture.md`)

---

### 3. Domain Rules & State Changes
Do **not** assume:
- Fixed state transition orders
- Fixed initial states
- Hardcoded enums being always valid

Instead, review whether:
- State changes are validated against **current state**
- Invalid transitions are explicitly rejected
- Business rules are enforced **in the service layer**, not the controller

If a state or rule is implicitly assumed → ask for explicit validation.

(Refer to `architecture.md`, domain rules)

---

### 4. Error Handling & Error Codes
- Errors should be **distinguishable by cause**
- Avoid collapsing multiple failure reasons into a single generic error
- Prefer **pre-checks** (`existsBy...`) over relying on DB exceptions
- If DB exceptions are unavoidable, ensure they are:
    - Properly mapped
    - Not leaking raw DB messages
    - Not logged as “Unhandled”

Flag any path where:
- A predictable error only appears at commit time
- The user cannot know *why* a request failed

(Refer to `api-conventions.md`)

---

### 5. API & DTO Consistency
- Controllers should never return entities directly
- Request / Response DTOs must:
    - Be explicit
    - Match API behavior
    - Not expose internal persistence concerns
- Swagger annotations should:
    - Reflect real constraints
    - Use realistic examples

If an API is ambiguous or misleading → point it out.

(Refer to `api-conventions.md`)

---

## Repository Method Review
Pay special attention to:
- Boolean methods (`existsBy...`)
    - Ensure the caller does **not invert meaning accidentally**
- Nested property paths
    - Must exactly match entity structure
- Method names implying behavior they don’t enforce

If a method name suggests validation but only checks existence → suggest renaming.

---

## Validation Rules
- Prefer **type safety** over runtime checks
- Strings:
    - `@NotBlank` when whitespace-only is invalid
    - `@NotEmpty` only when spaces are acceptable
- Numbers:
    - Always enforce range explicitly
- Dates / times:
    - Use proper types (`LocalDate`, `LocalDateTime`, integer hour, etc.)

Flag weak or misleading validation.

---

## Collections in APIs
- Use `List` by default
- Use `Set` **only if uniqueness is a business requirement**
- If order matters and no ordering is defined → flag it

---

## What NOT to Focus On
Unless it causes a real issue, avoid:
- Formatting
- Naming preferences
- Minor style differences
- Refactoring suggestions unrelated to correctness

---

## Review Tone Guidelines
- Be direct and factual
- Avoid speculative comments
- If something is risky, say **why**
- Prefer small, concrete fixes over large rewrites

---

## Summary Checklist
Before approving a PR, Copilot should be confident that:
- [ ] Data constraints are respected before persistence
- [ ] Transactions cover all mutations
- [ ] Domain rules are explicit and enforced
- [ ] Errors are meaningful and distinguishable
- [ ] APIs are predictable and consistent

If any of these are unclear → request changes.
