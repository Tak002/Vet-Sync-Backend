# API Conventions

This document defines stable API-level principles.

## API Design
- APIs should be predictable and consistent
- Use clear resource-oriented paths
- Avoid breaking changes when possible

## Request / Response
- Validate input at API boundaries
- Response models should not expose internal entities directly
- Error responses must be consistent across APIs

## HTTP Semantics
- Use HTTP methods according to their intent
- Do not overload a single endpoint with multiple responsibilities

## Evolution
- APIs should be designed assuming future extension
- Avoid designs that make versioning unavoidable too early
