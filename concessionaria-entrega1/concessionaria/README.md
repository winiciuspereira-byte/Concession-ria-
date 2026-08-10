# Concessionária Marcelo Gomes — API REST

Projeto Spring Boot desenvolvido em 3 entregas. Esta é a **Entrega 1**.

## Tecnologias

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- H2 (banco em memória para desenvolvimento)

## Como rodar

```bash
./mvnw spring-boot:run
```

O banco H2 pode ser acessado em: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:concessionaria`
- User: `sa` | Password: *(vazio)*

---

## Entidades

### Cliente
| Campo      | Tipo    | Nulo | Único |
|------------|---------|------|-------|
| id         | Long    | não  | sim   |
| cpf        | String  | não  | sim   |
| nome       | String  | não  | não   |
| telefone   | String  | não  | não   |
| email      | String  | não  | não   |

### Carro
| Campo          | Tipo       | Nulo | Único |
|----------------|------------|------|-------|
| id             | Long       | não  | sim   |
| modelo         | String     | não  | não   |
| marca          | String     | não  | não   |
| anoFabricacao  | Integer    | não  | não   |
| anoModelo      | Integer    | não  | não   |
| cor            | String     | não  | não   |
| placa          | String     | sim  | sim   |
| chassi         | String     | não  | sim   |
| quilometragem  | Integer    | não  | não   |
| preco          | BigDecimal | não  | não   |
| status         | Enum       | não  | não   |

Status possíveis: `DISPONIVEL`, `RESERVADO`, `VENDIDO`

---

## Endpoints

### Clientes
| Método | Rota           | Descrição          |
|--------|----------------|--------------------|
| POST   | /clientes      | Cadastrar cliente  |
| GET    | /clientes      | Listar clientes    |
| GET    | /clientes/{id} | Buscar por ID      |
| DELETE | /clientes/{id} | Remover cliente    |

### Carros
| Método | Rota        | Descrição       |
|--------|-------------|-----------------|
| POST   | /carros     | Cadastrar carro |
| GET    | /carros     | Listar carros   |
| GET    | /carros/{id}| Buscar por ID   |
| DELETE | /carros/{id}| Remover carro   |

---

## Exemplos de requisição

### Cadastrar cliente
```json
POST /clientes
{
  "nome": "Ana Silva",
  "cpf": "123.456.789-00",
  "telefone": "11999990000",
  "email": "ana@email.com"
}
```

### Cadastrar carro
```json
POST /carros
{
  "modelo": "Corolla",
  "marca": "Toyota",
  "anoFabricacao": 2023,
  "anoModelo": 2024,
  "cor": "Prata Switchblade",
  "placa": null,
  "chassi": "9BWZZZ377VT004251",
  "quilometragem": 0,
  "preco": 189990.00,
  "status": "DISPONIVEL"
}
```

---

## Decisões de modelagem

Ver [DECISOES.md](./DECISOES.md) para ambiguidades identificadas e justificativas.
