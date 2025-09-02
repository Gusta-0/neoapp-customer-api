
---

# 🌟 neoapp-customer-api 🌟

> Uma **API REST** completa para gerenciamento de clientes, segura, documentada e pronta para produção! 🚀

[![GitHub Repo](https://img.shields.io/badge/GitHub-neoapp--customer--api-blue?logo=github)](https://github.com/Gusta-0/neoapp-customer-api)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=java)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.4.5-green?logo=spring)](https://spring.io/projects/spring-boot)

---

## 📖 Descrição

O **neoapp-customer-api** é uma aplicação backend em **Java 17 + Spring Boot** para gerenciar informações de clientes.
Permite **inclusão, atualização, listagem e exclusão de clientes**, com busca por atributos, cálculo automático da idade e **segurança JWT**. 🔒

O projeto é perfeito para quem quer aprender sobre **Spring Boot, Spring Security, JPA, bancos de dados e Docker** na prática! 💡

---

## 🛠 Tecnologias Utilizadas

* ☕ **Java 17**
* 🌱 **Spring Boot**
* 🔒 **Spring Security + JWT**
* 🗃 **Spring Data JPA**
* 🐘 **PostgreSQL** (produção)
* 🟢 **H2 Database** (desenvolvimento/testes)
* 📦 **Gradle**
* 🐳 **Docker + Docker Compose**
* 📑 **Swagger** (documentação da API)

---

## ✨ Funcionalidades

* ✅ **CRUD completo de clientes**

    * Incluir novos clientes
    * Atualizar clientes existentes
    * Excluir clientes existentes
    * Listar clientes de forma **paginada**
* 🔍 **Busca por atributos** do cliente
* 🎂 **Idade calculada automaticamente** a partir da data de nascimento
* 🔒 **Autenticação e autorização com JWT**
* 📖 **Documentação interativa via Swagger**

---

## 🚀 Estrutura da API

| Método | Endpoint           | Descrição                    |
| ------ | ------------------ | ---------------------------- |
| POST   | `/clientes`        | Criar um novo cliente        |
| GET    | `/clientes`        | Listar clientes (paginado)   |
| GET    | `/clientes/{id}`   | Obter cliente por ID         |
| PUT    | `/clientes/{id}`   | Atualizar cliente existente  |
| DELETE | `/clientes/{id}`   | Excluir cliente              |
| GET    | `/clientes/search` | Buscar clientes por atributo |

> A documentação interativa do Swagger está disponível em: `http://localhost:8080/swagger-ui.html` 📑

---

## 🖥 Como Rodar o Projeto

### 1️⃣ Clonando o repositório

```bash
git clone https://github.com/Gusta-0/neoapp-customer-api.git
cd neoapp-customer-api
```

### 2️⃣ Rodando localmente (desenvolvimento)

```bash
./gradlew bootRun
```

* API disponível: `http://localhost:8080`
* Swagger UI: `http://localhost:8080/swagger-ui.html`

### 3️⃣ Rodando com Docker

#### Build da imagem 🐳

```bash
docker build -t neoapp-customer-api .
```

#### Executando com Docker Compose 📦

```bash
docker-compose up
```

* API disponível: `http://localhost:8080`
* PostgreSQL estará rodando em background 🐘

---

## 🎓 Aprendizado Obtido

Durante o desenvolvimento do **neoapp-customer-api**, aprendi e pratiquei:

* 🌱 **Spring Boot** e criação de APIs RESTful
* 🔒 **Spring Security + JWT** para autenticação e autorização
* 🗃 **Spring Data JPA** com H2 e PostgreSQL
* 📑 **Swagger** para documentação interativa
* 🐳 **Docker e Docker Compose** para execução containerizada
* 💻 Boas práticas de desenvolvimento e design de API

---

## 🚀 Melhorias Futuras

* ☁️ **Deploy em nuvem** (AWS, Azure ou GCP)
* 🧪 **Testes unitários e de integração**
* 🛡 Validações avançadas de dados
* 🔄 **Versionamento da API**
* ⚡ **Cache** para otimizar consultas frequentes
* 📊 **Logs centralizados e monitoramento da aplicação**

---

## 📬 Contato

Dúvidas ou sugestões? Abra uma **issue** no repositório ou entre em contato! ✉️

[GitHub Repo](https://github.com/Gusta-0/neoapp-customer-api)

---

## Diagrama de Classes

```mermaid
classDiagram
    direction TB

    %% Entidade principal
    class Cliente {
        - Long id
        - String nome
        - String email
        - LocalDate dataNascimento
        - String cpf
        + Integer calcularIdade()
    }

    %% Repositório
    class ClienteRepositorio {
        <<interface>>
        + Optional~Cliente~ buscarPorEmail(String email)
    }

    %% Serviço
    class ClienteServico {
        - ClienteRepositorio clienteRepositorio
        + ClienteResponseDTO incluirCliente(ClienteDTO dto)
        + ClienteResponseDTO atualizarCliente(Long id, ClienteDTO dto)
        + Page~ClienteResponseDTO~ listarClientes(Pageable pageable)
        + List~ClienteResponseDTO~ buscarPorFiltros(Map<String,String> filtros)
        + void excluirCliente(Long id)
    }

    %% Controller
    class ClienteControlador {
        - ClienteServico clienteServico
        + ResponseEntity<ClienteResponseDTO> incluirCliente(ClienteDTO dto)
        + ResponseEntity<ClienteResponseDTO> atualizarCliente(Long id, ClienteDTO dto)
        + ResponseEntity<Void> excluirCliente(Long id)
        + Page~ClienteResponseDTO~ listarClientes(int pagina, int tamanho, Map<String,String> filtros)
        + ResponseEntity<ClienteResponseDTO> buscarPorId(Long id)
    }

    %% DTOs
    class ClienteDTO {
        - Long id
        - String nome
        - String email
        - LocalDate dataNascimento
        - String cpf
        - Integer idade
    }

    class ClienteLoginDTO {
        - String email
        - String senha
    }

    class ClienteResponseDTO {
        - Long id
        - String nome
        - String email
        - LocalDate dataNascimento
        - String cpf
        - Integer idade
        - LocalDateTime criadoEm
        - LocalDateTime atualizadoEm
    }

    %% Mappers
    class ClienteMapper {
        + ClienteDTO toDTO(Cliente cliente)
        + Cliente toEntity(ClienteDTO dto)
        + ClienteResponseDTO toResponseDTO(Cliente cliente)
    }

    class ClienteUpdateMapper {
        + void atualizarEntidade(ClienteDTO dto, Cliente cliente)
    }

    %% Segurança
    class JwtServico {
        + String gerarToken(UserDetails usuario)
        + boolean validarToken(String token, UserDetails usuario)
        + String extrairUsuario(String token)
    }

    class AutenticacaoControlador {
        + String login(RequisicaoAutenticacao requisicao)
    }

    class RequisicaoAutenticacao {
        - String usuario
        - String senha
    }

    class UsuarioDetalhesServico {
        + UserDetails carregarUsuarioPorNome(String usuario)
    }

    %% Relações
    ClienteRepositorio <|.. JpaRepository
    ClienteServico --> ClienteRepositorio
    ClienteServico --> ClienteMapper
    ClienteServico --> ClienteUpdateMapper
    ClienteControlador --> ClienteServico
    ClienteControlador --> ClienteDTO
    ClienteControlador --> ClienteResponseDTO
    JwtServico --> AutenticacaoControlador
    AutenticacaoControlador --> RequisicaoAutenticacao
    UsuarioDetalhesServico --> UserDetails

