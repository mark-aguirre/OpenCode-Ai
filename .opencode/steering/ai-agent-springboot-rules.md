# AI Agent Rules for Spring Boot Development

## Core Principles
- **Vibe Coding First**: Prioritize developer experience and rapid iteration while maintaining code quality
- **Convention over Configuration**: Follow Spring Boot defaults unless there's a compelling reason
- **Modular by Design**: Even monoliths should be structured as modular components

## Architecture Rules

### Monolith Structure
```
src/main/java/
├── com/company/app/
│   ├── Application.java
│   ├── config/          # Configuration classes
│   ├── core/           # Core business logic
│   ├── infrastructure/ # External integrations
│   ├── shared/         # Common utilities
│   └── modules/        # Feature modules
```

### Module-Based Structure
Each module should be:
- **Self-contained**: Handle its own domain logic
- **Loosely coupled**: Minimize dependencies on other modules
- **Well-defined interfaces**: Clear API boundaries

```
modules/
├── user/
│   ├── UserModule.java
│   ├── domain/
│   ├── repository/
│   ├── service/
│   └── controller/
├── product/
└── order/
```

## Development Best Practices

### 1. Package Organization
- Use **feature-based packaging** over layer-based
- Keep related classes together
- Avoid deep package hierarchies (max 4-5 levels)

### 2. Dependency Management
- **Explicit dependencies**: Declare all required dependencies
- **Version management**: Use Spring Boot's dependency management
- **Avoid circular dependencies** between modules

### 3. Configuration Management
- **Externalize configuration**: Use application.yml/properties
- **Profile-specific configs**: Separate dev, test, prod configs
- **Environment variables**: For sensitive data

### 4. Database Design
- **Schema-first approach**: Design database before code
- **Migration scripts**: Use Flyway or Liquibase
- **Connection pooling**: Configure HikariCP properly

### 5. API Design
- **RESTful principles**: Use proper HTTP methods
- **Consistent naming**: Follow REST conventions
- **Version management**: Use API versioning
- **Error handling**: Standardized error responses

## Code Quality Rules

### 1. Spring Boot Specific
- **@SpringBootApplication**: Only in main application class
- **Component scanning**: Limit to specific packages
- **Conditional beans**: Use @Conditional annotations wisely
- **Profile-specific beans**: Use @Profile annotation

### 2. Bean Management
- **Prefer constructor injection** over field injection
- **Use @ConfigurationProperties** for type-safe configuration
- **Avoid @Autowired on fields**: Use constructor injection
- **Singleton scope**: Default unless stateful beans needed

### 3. Exception Handling
- **Custom exceptions**: Create domain-specific exceptions
- **Global exception handler**: Use @ControllerAdvice
- **Proper HTTP status codes**: Map exceptions to correct status
- **Logging**: Log exceptions with appropriate levels

### 4. Testing Strategy
- **Unit tests**: Test business logic in isolation
- **Integration tests**: Test module interactions
- **@SpringBootTest**: Use sparingly for full application tests
- **Test slices**: Use @WebMvcTest, @DataJpaTest, etc.

## Performance Rules

### 1. Database Optimization
- **Connection pooling**: Properly configure pool size
- **Query optimization**: Use pagination, avoid N+1 queries
- **Caching**: Implement appropriate caching strategies
- **Transaction boundaries**: Keep transactions short

### 2. Memory Management
- **Lazy loading**: Use for large collections
- **DTO projection**: Avoid fetching unnecessary data
- **Object lifecycle**: Manage object creation properly
- **Garbage collection**: Monitor memory usage

### 3. API Performance
- **Async processing**: Use @Async for long-running tasks
- **Response compression**: Enable GZIP compression
- **Rate limiting**: Implement API rate limiting
- **Caching headers**: Set appropriate cache headers

## Security Rules

### 1. Authentication & Authorization
- **Spring Security**: Use for authentication/authorization
- **JWT tokens**: For stateless authentication
- **Role-based access**: Implement proper role hierarchy
- **Method security**: Use @PreAuthorize, @Secured

### 2. Data Protection
- **Input validation**: Use @Valid, @Validated
- **SQL injection prevention**: Use parameterized queries
- **XSS protection**: Sanitize user inputs
- **HTTPS**: Enforce SSL/TLS in production

### 3. Configuration Security
- **Secrets management**: Use vault or environment variables
- **Password encryption**: Use strong hashing algorithms
- **Session management**: Configure session timeout properly
- **CORS configuration**: Restrict cross-origin requests

## Monitoring & Observability

### 1. Logging
- **Structured logging**: Use JSON format for logs
- **Log levels**: Use appropriate levels (ERROR, WARN, INFO, DEBUG)
- **Correlation IDs**: Track requests across services
- **Sensitive data**: Never log passwords or tokens

### 2. Metrics
- **Spring Boot Actuator**: Enable health, info, metrics endpoints
- **Micrometer**: Use for metrics collection
- **Custom metrics**: Track business-relevant metrics
- **Performance monitoring**: Monitor response times

### 3. Health Checks
- **Custom health indicators**: Implement domain-specific health checks
- **Dependency health**: Check external service health
- **Database connectivity**: Monitor database connections
- **Resource utilization**: Track memory, CPU usage

## Deployment Rules

### 1. Containerization
- **Docker best practices**: Use multi-stage builds
- **Small images**: Minimize image size
- **Security scanning**: Scan images for vulnerabilities
- **Health checks**: Implement proper health checks

### 2. Configuration Management
- **External configuration**: Use environment variables
- **Configuration validation**: Validate required properties
- **Feature flags**: Implement feature toggles
- **A/B testing**: Support experimentation

### 3. CI/CD Pipeline
- **Automated testing**: Run tests on every commit
- **Code quality**: Use SonarQube or similar tools
- **Security scanning**: Scan dependencies for vulnerabilities
- **Rollback strategy**: Plan for quick rollbacks

## Vibe Coding Guidelines

### 1. Developer Experience
- **Hot reload**: Use Spring DevTools for rapid development
- **Good error messages**: Provide clear, actionable error messages
- **Documentation**: Document APIs with Swagger/OpenAPI
- **Local development**: Ensure easy local setup

### 2. Code Readability
- **Meaningful names**: Use descriptive variable and method names
- **Small methods**: Keep methods under 20 lines
- **Consistent style**: Follow Java coding conventions
- **Comments**: Add comments for complex business logic

### 3. Rapid Iteration
- **Feature flags**: Use to enable/disable features quickly
- **Schema evolution**: Design for backward compatibility
- **Incremental development**: Build features incrementally
- **Fast feedback**: Quick build and test cycles

## Module Communication Rules

### 1. Inter-Module Communication
- **Domain events**: Use event-driven communication
- **Shared kernel**: Minimize shared code between modules
- **API contracts**: Define clear interfaces between modules
- **Dependency direction**: High-level modules shouldn't depend on low-level modules

### 2. Data Sharing
- **DTOs**: Use Data Transfer Objects for inter-module communication
- **Immutable objects**: Prefer immutable data structures
- **Validation**: Validate data at module boundaries
- **Mapping**: Use MapStruct or similar for object mapping

## Technology Stack Rules

### 1. Core Technologies
- **Spring Boot**: Latest stable version
- **Java**: LTS versions preferred (Java 17, 21)
- **Build tool**: Maven or Gradle based on team preference
- **Database**: PostgreSQL for production, H2 for testing

### 2. Additional Libraries
- **Validation**: Hibernate Validator
- **JSON**: Jackson with proper configuration
- **Testing**: JUnit 5, Mockito, TestContainers
- **Documentation**: SpringDoc OpenAPI

### 3. Development Tools
- **IDE**: IntelliJ IDEA or VS Code
- **Code quality**: SpotBugs, PMD, Checkstyle
- **Dependency management**: OWASP Dependency Check
- **Container tools**: Docker, Docker Compose

## Anti-Patterns to Avoid

### 1. Architecture Anti-Patterns
- **God classes**: Avoid large, monolithic classes
- **Circular dependencies**: Never create circular dependencies
- **Tight coupling**: Minimize direct dependencies between modules
- **Shared databases**: Avoid database sharing between modules

### 2. Code Anti-Patterns
- **Anemic domain models**: Include business logic in domain objects
- **Magic numbers**: Use named constants
- **Deep nesting**: Keep code shallow and readable
- **Over-engineering**: Keep solutions simple and maintainable

### 3. Performance Anti-Patterns
- **N+1 queries**: Avoid in database operations
- **Excessive logging**: Don't log at DEBUG level in production
- **Memory leaks**: Properly manage resources and connections
- **Synchronous blocking**: Use async operations for I/O heavy tasks

## Continuous Improvement Rules

### 1. Code Reviews
- **Mandatory reviews**: All code must be reviewed
- **Checklist-based**: Use review checklists for consistency
- **Automated checks**: Integrate automated quality gates
- **Knowledge sharing**: Use reviews for team learning

### 2. Refactoring
- **Regular refactoring**: Schedule refactoring sessions
- **Technical debt**: Track and prioritize technical debt
- **Code coverage**: Maintain good test coverage
- **Performance testing**: Regular performance assessments

### 3. Learning & Adaptation
- **Stay updated**: Keep up with Spring Boot updates
- **Community involvement**: Participate in Spring community
- **Knowledge sharing**: Share learnings within the team
- **Experimentation**: Try new approaches in controlled environments

---

## Quick Reference Checklist

### Before Committing Code:
- [ ] Code follows package organization rules
- [ ] Dependencies are explicitly declared
- [ ] Configuration is externalized
- [ ] Tests are written and passing
- [ ] Security best practices are followed
- [ ] Logging is appropriate and structured
- [ ] Performance considerations are addressed

### Before Deploying:
- [ ] All tests pass in CI/CD pipeline
- [ ] Security scans are clean
- [ ] Health checks are implemented
- [ ] Monitoring is configured
- [ ] Documentation is updated
- [ ] Rollback plan is ready
- [ ] Load testing is completed

---

*Remember: These rules are guidelines, not strict laws. Adapt them based on your specific context, team size, and requirements. The goal is to maintain high-quality code while enabling rapid development and iteration.*