# Architecture Principles

This document defines high-level architectural principles.
Details may evolve, but the following rules should remain stable.

## Core Principles
- Prefer **clear domain boundaries** over premature optimization
- Domain logic must not depend on infrastructure concerns
- Controllers should be thin; business logic belongs to services or domain models
- Persistence models (JPA entities) should reflect domain intent, not API shape

## Modularity
- Each domain feature should have a clear ownership (package/module)
- Cross-domain communication should be explicit and minimal
- Avoid cyclic dependencies between packages

## Change Tolerance
- Architecture should allow incremental change
- Avoid designs that require large refactors for small feature additions
