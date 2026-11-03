# Biblioteca API 📚

API REST para gerenciar o acervo de livros de uma biblioteca: cadastre, consulte, atualize e remova livros.

Feita com **Java 17**, **Spring Boot 4**, **Spring Data JPA** e **PostgreSQL**.

## Como rodar

Você precisa de **JDK 17**, **Maven** e **Docker**.

```bash
git clone https://github.com/DevTroli/biblioteca-api.git
cd biblioteca-api
mvn spring-boot:run
```

Pronto. O Spring Boot sobe o PostgreSQL sozinho (via `compose.yaml`) e a API fica em `http://localhost:8080`.

Ao abrir esse endereço no navegador, você cai na **documentação interativa (Swagger)**, onde pode testar cada endpoint.

## O que um livro tem

| Campo | Tipo | Regra |
|---|---|---|
| `id` | número | Gerado automaticamente |
| `titulo` | texto | Obrigatório |
| `autor` | texto | Obrigatório |
| `isbn` | texto | Obrigatório e único |
| `anoPublicacao` | número | Opcional |

## Endpoints

| Ação | Rota | Resposta |
|---|---|---|
| Listar livros | `GET /livros` | 200 |
| Buscar um livro | `GET /livros/{id}` | 200, ou 404 se não existir |
| Cadastrar | `POST /livros` | 201, 400 (campo vazio) ou 409 (ISBN repetido) |
| Atualizar | `PUT /livros/{id}` | 200, 400, 404 ou 409 |
| Remover | `DELETE /livros/{id}` | 204, ou 404 se não existir |

Exemplo de corpo para cadastrar ou atualizar:

```json
{
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "isbn": "9788535910663",
  "anoPublicacao": 1899
}
```

## Como testar

**Pelo navegador:** abra `http://localhost:8080` e use o botão **Try it out** em cada endpoint.

**Pelo Postman:**

1. Importe o arquivo `postman/biblioteca-api.postman_collection.json`.
2. Com a API rodando, abra o **Collection Runner** e execute a coleção.

A coleção roda 8 passos em ordem: cria um livro, tenta repetir o ISBN (409), envia um título vazio (400), lista, busca, atualiza, remove e confirma que o livro removido não existe mais (404). Cada passo já verifica o status esperado.

**Direto no banco:**

```bash
docker exec -it biblioteca-postgres psql -U biblioteca -d biblioteca
```

Dentro do `psql`, rode `SELECT * FROM livros;`.

## Estrutura

```text
src/main/java/br/com/biblioteca/
├── controller/   # rotas da API (LivroController) e redirecionamento da raiz
├── model/        # entidade Livro
├── repository/   # acesso ao banco (LivroRepository)
└── config/       # configuração da documentação
```

## Testes automatizados

```bash
mvn test
```

Os testes usam um banco H2 em memória, então não precisam do Docker.
