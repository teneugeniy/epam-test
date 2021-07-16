# EPAM test task
- Spring Boot, kotlin, gradle, H2
- made from scratch
- Domain-Driven
- covered with unit-tests and integration tests
# Design
controllers -> endpoints, facades that do not contain any logic\
↓ ↑\
api-services -> requests processing services. handle the business logic\
↓ ↑\
mappers -> converting DTOs to domain models/commands/values and back\
↓ ↑\
entity-services -> intermediate level responsible for any mutation of entity objects\
↓ ↑\
repositories -> DAL services