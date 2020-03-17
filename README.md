# pay-n-park

## Technology used
- Java 8+
- Maven
- Postman/Any Rest Client
- Spring Boot
- H2 in-memory database
- JUnit
- Jacoco

## Setup and commands
- mvn clean install
- mvn spring-boot:run / directly from STS
- mvn test (unit testing and code coverage)
## REST APIs
1 . /api/initialize
- This api use for initialize application with default values like total parking slots, rates etc.
- This api should call first.

2. /api/entry
- This api use for allocate car parking slot for specific car

3. /api/exit/{carNumber}
- This api use for leave parking if carNumber is exists on same parking slot

4. /api/applypolicy
- This api use for change policy of particular parking slot

5. /api/slots
- This api retrive all parkingslots
