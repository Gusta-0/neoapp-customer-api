# neoapp-customer-api

## Diagrama de Classes

```mermaid
classDiagram
    direction TB

    class Cliente {
        - Long id
        - String nome
        - String email
        - LocalDate dataNascimento
        - String cpf
        + Integer calcularIdade()
    }

    class ClienteRepositorio {
        <<interface>>
        + Optional~Cliente~ buscarPorEmail(String email)
    }

    class ClienteServico {
        - ClienteRepositorio clienteRepositorio
        + Cliente incluirCliente(Cliente cliente)
        + Cliente atualizarCliente(Long id, Cliente cliente)
        + Page~Cliente~ listarClientes(Pageable pageable)
        + List~Cliente~ buscarPorFiltros(Map<String,String> filtros)
        + void excluirCliente(Long id)
    }

    class ClienteControlador {
        - ClienteServico clienteServico
        + ResponseEntity<ClienteDTO> incluirCliente(ClienteDTO dto)
        + ResponseEntity<ClienteDTO> atualizarCliente(Long id, ClienteDTO dto)
        + ResponseEntity<Void> excluirCliente(Long id)
        + Page<ClienteDTO> listarClientes(int pagina, int tamanho, Map<String,String> filtros)
        + ResponseEntity<ClienteDTO> buscarPorId(Long id)
    }

    class ClienteDTO {
        - Long id
        - String nome
        - String email
        - LocalDate dataNascimento
        - String cpf
        - Integer idade
    }

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

    ClienteRepositorio <|.. JpaRepository
    ClienteServico --> ClienteRepositorio
    ClienteControlador --> ClienteServico
    ClienteControlador --> ClienteDTO
    ClienteServico --> ClienteDTO
    JwtServico --> AutenticacaoControlador
    AutenticacaoControlador --> RequisicaoAutenticacao
    UsuarioDetalhesServico --> UserDetails
