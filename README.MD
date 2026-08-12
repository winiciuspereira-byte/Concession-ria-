# Concessionária Marcelo Gomes — API REST

API para gerenciamento de estoque de veículos e cadastro de clientes.

## Tecnologias

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- Spring Validation
- SpringDoc OpenAPI (Swagger)
- H2 (banco em memória)

## Como rodar

```bash
./mvnw spring-boot:run
```

- API: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:concessionaria` | User: `sa` | Password: *(vazio)*

## Endpoints

### Carros
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /carros | Cadastrar carro |
| GET | /carros | Listar carros (filtros: `?cor=prata`, `?ano=2024`) |
| GET | /carros/{id} | Buscar por ID |
| DELETE | /carros/{id} | Remover carro |

### Clientes
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /clientes | Cadastrar cliente |
| GET | /clientes | Listar clientes |
| GET | /clientes/{id} | Buscar por ID |
| DELETE | /clientes/{id} | Remover cliente |

## Decisões de modelagem

Ver [DECISOES.md](./DECISOES.md)
