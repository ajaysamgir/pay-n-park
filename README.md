# pay-n-park

pay-n-park application help your to manage parking slot allocation to temporary users on rent.

## Technology used
- Java 8+
- Maven
- Postman/Any Rest Client
- Spring Boot framework
- H2 in-memory database
- JUnit
- Jacoco
- GitHub

## Setup and commands
- Clone/Download project and run below commands for setup and run
- mvn clean install
- mvn spring-boot:run / directly from STS
- mvn test (unit testing and code coverage)

## REST APIs
1 . /api/initialize
- This api use for initialize application with default values like total parking slots, rates etc.
- This api should call first for initialize your app with configuration and basic data.

2. /api/entry
- This api use for allocate car parking slot for specific car which is request and it refuse to allocate parking slot if slot is not available of that specific car.

3. /api/exit/{carNumber}
- This api use for leave parking if carNumber is exists on same parking slot. validate car number and fetch bill also mark parking slot as a free once exit process done.

4. /api/applypolicy
- This api use for change policy of particular parking slot. update policy type of particular parking slot.

Please check Postman-api-collection directory for sample api request

5. /api/slots
- This api retrive all parkingslots which is existing in system with current status of slot is that booked or free.

Unit testing :
- Test all the positive and negative scenarios of all api's with dummy data.
- Code coverage for controller and service classes is greater than 90.