# Decisões de Modelagem — Concessionária Marcelo Gomes

| # | Ponto | Decisão | Justificativa |
|---|-------|---------|---------------|
| 1 | Cor do carro | `String` (texto livre) | Cada marca nomeia cores de forma diferente; enum seria engessado |
| 2 | Telefone e e-mail | Obrigatórios (`nullable = false`) | Case diz que promoções são enviadas pelos dois canais |
| 3 | Placa | `nullable = true`, `unique = true` | Carro zero pode chegar sem placa antes do emplacamento |
| 4 | Quilometragem zero | `Integer` com valor `0` | Zero é válido; `null` seria ambíguo |
| 5 | Preço | `BigDecimal` | Evita erros de arredondamento de `double`/`float` com dinheiro |
| 6 | Status do carro | `enum` fixo | Os três estados (DISPONIVEL, RESERVADO, VENDIDO) cobrem todo o ciclo de vida |
| 7 | Ano fabricação vs. ano modelo | Dois campos separados | São informações distintas; o próprio case ressalta a diferença |
