# TripBoard implementation tasks

The checked items reflect what is complete at this commit.

## Project milestones

- [x] Initialize separate Angular and Spring Boot projects
- [x] Configure PostgreSQL and schema migrations
- [x] Registration, password hashing, JWT and authentication UI
- [x] Trip CRUD backend and frontend
- [x] Membership and access authorization
- [x] Itinerary CRUD and chronological UI
- [x] Expense CRUD and deterministic equal splitting
- [x] Balances, settlements and debt simplification
- [x] Suggestion CRUD, unique votes and activity conversion
- [x] Trip dashboard and demo data
- [x] Business logic and access integration tests
- [x] Responsive UI, validation and browser flow verification
- [ ] Requirements, technical specification and complete setup guide

## Feature details

- [x] Separate Angular and Spring Boot folders, pinned dependencies and ignored secrets/build outputs
- [x] Nine JPA entities, six versioned PostgreSQL migrations, constraints and safe cascades
- [x] Validated account registration, BCrypt hashing, JWT issue/verification and expiry checks
- [x] Login/register pages, route guard, session restoration and JWT interceptor
- [x] Trip cards and create/edit/delete dialogs; consistent nested trip navigation
- [x] Member lookup by registered username/email; owner-only add/remove operations
- [x] Creator/owner editing rules and trip membership checks on every resource
- [x] Activity create/view/edit/delete; chronological day grouping and date validation
- [x] Expense create/view/edit/delete; payer and participant selection
- [x] Exact equal shares, stable remainder-cent allocation and duplicate-participant validation
- [x] Per-member balances, corrected settlement signs and simplified transfers
- [x] Payment recording/deletion; financial history protects member removal
- [x] Open suggestion create/view/edit/delete, one changeable vote per user
- [x] Owner accept/reject actions and once-only accepted-idea conversion
- [x] Dashboard spending, upcoming activities, personal balance and shortcuts
- [x] Opt-in empty-database demo seeding with externally configured passwords
- [x] Responsive desktop/mobile views, native modal dialogs, confirmations and error states
- [x] Separate Angular templates and formatted Java/TypeScript/CSS sources
- [x] Unit tests for splitting, balances, settlements and debt simplification
- [x] Integration tests for CRUD, authorization, validation, vote uniqueness/change and conversion
- [ ] Real PostgreSQL migration and integration-suite verification
- [x] Browser regression for account lifecycle, core CRUD, voting, settlements and mobile overflow
- [x] Patched frontend development dependency; clean npm audit
- [ ] Maven wrapper and environment-loading backend runner
- [ ] Redact passwords and tokens from authentication diagnostic output

## Completed in this commit

Harden authentication and validation, expand browser/security checks and format the source files.
