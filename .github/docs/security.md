# Security Principles

This document defines baseline security expectations.

## Authentication & Authorization
- Authentication and authorization concerns must be explicit
- Do not rely on client-side checks for access control
- Security rules should be enforced at server boundaries

## Data Protection
- Do not expose sensitive information in logs or responses
- Validate all externally supplied input

## Defaults
- Secure defaults are preferred over permissive behavior
- Fail fast and explicitly on security violations

## Awareness
- Security is a shared responsibility across all layers
