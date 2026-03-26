# URL Shortener Service (Spring Boot + H2)

## Run

Prereqs: Java 21+ and Maven.

```bash
mvn spring-boot:run
```

## Swagger UI

- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## API

- `POST /api/urls` body: `{ "url": "https://example.com/very/long" }`
- `GET /api/urls/{code}` returns stats (including click count)
- `GET /{code}` redirects (302) to the original URL and increments click count

## H2 Console

- `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:urlshortener`
- user: `sa`
- password: empty

