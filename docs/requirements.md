# TripBoard requirements

## Purpose and users

TripBoard helps small groups of friends plan a trip collaboratively and keep shared expenses understandable. It is a university semester application demonstrating Angular, Spring Boot, PostgreSQL, relational modeling, REST, authentication, CRUD, business rules and incremental Git development.

## Functional requirements

- Register with a unique username/email; sign in with a securely hashed password and JWT.
- Create and browse trips with destination, description, dates and one currency. The owner can edit/delete a trip.
- Add already registered users by exact email or username. Only owners manage membership; non-members cannot access trip data.
- Create, view, edit and delete itinerary activities, grouped chronologically by day.
- Create, view, edit and delete open suggestions. Members have one changeable UP/DOWN vote per suggestion; totals and the current vote are visible.
- Owners accept/reject suggestions and convert accepted suggestions into scheduled activities once.
- Create, view, edit and delete expenses with a payer and selected participants. Split equally using exact decimal arithmetic and deterministic remainder cents.
- Display total spending, each member’s balance and simplified suggested transfers. Record settlements and allow corrections by deleting erroneous entries.
- Show a trip overview with dates, membership, upcoming activities, spending, personal balance and navigation shortcuts.
- Provide demo users and realistic trip data, enabled explicitly on an empty development database.

## Non-functional requirements

- Use a layered Java/Spring Boot backend with DTOs, Jakarta Validation and centralized error handling; do not return JPA entities.
- Persist application data in PostgreSQL with foreign keys, unique constraints and safe trip-specific cascades. Never cascade deletions into user accounts.
- Enforce every permission on the backend. Never store plaintext passwords or commit deployment secrets.
- Use a responsive, accessible Angular interface with form validation, loading indicators, empty states, errors and destructive-action confirmations.
- Test financial calculations, voting, authentication and access rules. Build both applications successfully and verify main browser flows.
- Run locally without Docker. Document exact versions, setup, constraints, demo credentials and architecture.
- Maintain a checked task list and meaningful chronological commits throughout implementation.

## Main flows

1. Register/sign in → create a trip → add registered travelers.
2. Open a trip → add/edit daily activities → view the itinerary.
3. Propose an idea → vote/change vote → owner accepts → select date/time → add to itinerary.
4. Add an expense → choose payer/participants → calculate exact shares → inspect balances.
5. Make a payment outside the app → record a settlement → see debts decrease.

## Scope decisions

Positive balances are receivables, so payments sent increase the sender’s balance and payments received decrease the receiver’s balance. Trip currency is immutable. Content is editable by its creator or the owner; only open ideas can change. Members with financial history cannot be removed. No email invitations, booking, maps, chat, external travel data or payment processing are included.
