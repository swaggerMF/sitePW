# TripBoard implementation tasks

The checked items reflect what is complete at this commit.

## Project milestones

- [x] Initialize separate Angular and Spring Boot projects
- [x] Configure PostgreSQL and schema migrations
- [x] Registration, password hashing, JWT and authentication UI
- [ ] Trip CRUD backend and frontend
- [ ] Membership and access authorization
- [ ] Itinerary CRUD and chronological UI
- [ ] Expense CRUD and deterministic equal splitting
- [ ] Balances, settlements and debt simplification
- [ ] Suggestion CRUD, unique votes and activity conversion
- [ ] Trip dashboard and demo data
- [ ] Business logic and access integration tests
- [ ] Responsive UI, validation and browser flow verification
- [ ] Requirements, technical specification and complete setup guide

## Feature details

- [x] Separate Angular and Spring Boot folders, pinned dependencies and ignored secrets/build outputs
- [ ] Nine JPA entities, six versioned PostgreSQL migrations, constraints and safe cascades
- [x] Validated account registration, BCrypt hashing, JWT issue/verification and expiry checks
- [x] Login/register pages, route guard, session restoration and JWT interceptor
- [ ] Trip cards and create/edit/delete dialogs; consistent nested trip navigation
- [ ] Member lookup by registered username/email; owner-only add/remove operations
- [ ] Creator/owner editing rules and trip membership checks on every resource
- [ ] Activity create/view/edit/delete; chronological day grouping and date validation
- [ ] Expense create/view/edit/delete; payer and participant selection
- [ ] Exact equal shares, stable remainder-cent allocation and duplicate-participant validation
- [ ] Per-member balances, corrected settlement signs and simplified transfers
- [ ] Payment recording/deletion; financial history protects member removal
- [ ] Open suggestion create/view/edit/delete, one changeable vote per user
- [ ] Owner accept/reject actions and once-only accepted-idea conversion
- [ ] Dashboard spending, upcoming activities, personal balance and shortcuts
- [ ] Opt-in empty-database demo seeding with externally configured passwords
- [ ] Responsive desktop/mobile views, native modal dialogs, confirmations and error states
- [ ] Separate Angular templates and formatted Java/TypeScript/CSS sources
- [ ] Unit tests for splitting, balances, settlements and debt simplification
- [ ] Integration tests for CRUD, authorization, validation, vote uniqueness/change and conversion
- [ ] Real PostgreSQL migration and integration-suite verification
- [ ] Browser regression for account lifecycle, core CRUD, voting, settlements and mobile overflow
- [ ] Patched frontend development dependency; clean npm audit
- [ ] Maven wrapper and environment-loading backend runner
- [ ] Redact passwords and tokens from authentication diagnostic output

## Completed in this commit

Add Angular login and registration pages, session handling, a route guard and a JWT interceptor.
