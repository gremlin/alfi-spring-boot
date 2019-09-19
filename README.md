# ALFI ToDo
This POC demonstrates how to include Gremlin's ALFI into an existing Spring Boot application; in a non-intrusive and declarative way.

# Usage

Decorate any methods in your code with `@Attack` to take advantage of the default fault injection Gremlin provides
```java
@Attack(type = "ToDoController", fields = {"method=findToDoById"})
public HttpEntity<ToDoDto> findToDoById(@PathVariable ObjectId id) {...} 
```

Decorate any methods in your code with `@AdvancedAttack` to intercept method parameters for more advanced and creative fault injection scenarios. See `com.gremlin.todo.aspect.AdvancedAttackAspect` for an example on how to intercept method parameters.
```java
@AdvancedAttack(type = "ToDoController", fields = {"method=createToDo"})
public HttpEntity<ToDoDto> createToDo(@RequestBody ToDoDto toDoDto) {...}
```

# Environment Variables
| Name      | Value |
| ----------- | ----------- |
| GREMLIN_ALFI_IDENTIFIER      | ToDoApplication       |
| GREMLIN_TEAM_ID   | < team id here >        |
| GREMLIN_ALFI_ENABLED      | < true or false >       |
| GREMLIN_TEAM_CERTIFICATE_OR_FILE   | < file:///path/to/.pem >        |
| GREMLIN_TEAM_PRIVATE_KEY_OR_FILE      | < file:///path/to/.pem >        |