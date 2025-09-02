# neoapp-customer-api

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

