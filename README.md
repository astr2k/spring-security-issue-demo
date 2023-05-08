## Demo project to reproduce spring-security issue #13140

Link to the issue: https://github.com/spring-projects/spring-security/issues/13140

This Spring Boot demo application has two REST endpoints that mapped to the same path but different HTTP methods - see `com.example.reproduce.controller.ItemController` class.

Permissions configuration: PUT endpoint may be executed only by user having "ADMIN" role, GET endpoint is permitted for all - see `com.example.reproduce.config.WebSecurityConfig` class.

Easiest way to reproduce the issue is to run test class `com.example.reproduce.controller.ItemControllerTest`.

May be reproduced also using Postman, etc.
Build application without tests (test will fail) as `mvn package -DskipTests=true`, launch as `java -jar target/reproduce-issue-0.0.1-SNAPSHOT.jar`.
After application starts, call all endpoints as anonymous user, do not login.
* First call GET endpoint `http://localhost:8080/api/v1/item/1` - returns `OK` (expected).
* Call PUT endpoint with same path `http://localhost:8080/api/v1/item/1` - returns `Unauthorized` (expected).
* Call again GET endpoint `http://localhost:8080/api/v1/item/1` - now returns `Unauthorized`, same response as in previous call.

Expected behavior - calling GET endpoint should not return `Unauthorized` status as it is permitted for all.

### Environment
Java 17, Spring Boot 3.0.6, Spring Security 6.0.3.
Reproducible with earlier versions too, also Spring Boot 2.x.x
