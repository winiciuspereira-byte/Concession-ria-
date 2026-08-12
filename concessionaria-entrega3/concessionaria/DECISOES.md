# Decisões de Modelagem — Concessionária Marcelo Gomes

## Entrega 1

| # | Ponto | Decisão | Justificativa |
|---|-------|---------|---------------|
| 1 | Cor do carro | `String` (texto livre) | Cada marca nomeia cores de forma diferente; enum seria engessado |
| 2 | Telefone e e-mail | Obrigatórios (`nullable = false`) | Case diz que promoções são enviadas pelos dois canais |
| 3 | Placa | `nullable = true`, `unique = true` | Carro zero pode chegar sem placa antes do emplacamento |
| 4 | Quilometragem zero | `Integer` com valor `0` | Zero é válido; `null` seria ambíguo |
| 5 | Preço | `BigDecimal` | Evita erros de arredondamento de `double`/`float` com dinheiro |
| 6 | Status do carro | `enum` fixo | Os três estados (DISPONIVEL, RESERVADO, VENDIDO) cobrem todo o ciclo de vida |
| 7 | Ano fabricação vs. ano modelo | Dois campos separados | São informações distintas; o próprio case ressalta a diferença |

## Entrega 2

| # | Ponto | Decisão | Justificativa |
|---|-------|---------|---------------|
| 8 | Status no DTO de entrada | Não entra | Todo carro cadastrado começa como DISPONIVEL; quem cadastra não escolhe o status |
| 9 | CPF no DTO de saída | Não sai | Dado sensível; não deve ser exposto em listagens da API |
| 10 | Ano máximo de fabricação | `@Max(2026)` | Barrar anos no futuro como 2202, conforme reclamação explícita do Marcelo |
| 11 | Preço mínimo | `@DecimalMin("0.01")` | Preço zero não faz sentido; reclamação explícita no case |
| 12 | CPF com formato fixo | `@Pattern` `\d{3}\.\d{3}\.\d{3}-\d{2}` | Barrar letras no meio do CPF, conforme reclamação explícita |
| 13 | Chassi com 17 caracteres | `@Size(min=17, max=17)` | Padrão internacional VIN tem exatamente 17 caracteres |
| 14 | Quilometragem | `@PositiveOrZero` | Não pode ser negativa; zero é válido para carro novo |

## Entrega 3

| # | Ponto | Decisão | Justificativa |
|---|-------|---------|---------------|
| 15 | Filtro por cor | Case-insensitive (`LOWER`) | "Prata" e "prata" devem retornar o mesmo resultado |
| 16 | Filtro por ano | Busca em `anoFabricacao` e `anoModelo` | Cliente pode pesquisar pelo ano do modelo ou da fabricação; ambos são válidos |
| 17 | Filtros combinados | Cor E ano podem ser usados juntos | Mais flexibilidade na busca do estoque |
