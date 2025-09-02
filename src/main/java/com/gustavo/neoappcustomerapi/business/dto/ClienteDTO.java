package com.gustavo.neoappcustomerapi.business.dto;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {
    @NotBlank(message = "O nome é obrigatório")
    @NotBlank(groups = OnCreate.class, message = "O nome é obrigatório")
    @Size(min = 2, max = 120, groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]{3,50}$",
            message = "O nome deve conter apenas letras e espaços, entre 3 e 50 caracteres")
    private String nome;

    @NotBlank(groups = OnCreate.class, message = "O e-mail é obrigatório")
    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "Formato de e-mail inválido")
    private String email;

    @NotBlank(groups = OnCreate.class, message = "A senha é obrigatória")
    @Size(min = 8, groups = {OnCreate.class, OnUpdate.class}, message = "A senha deve ter ao menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "A senha deve conter apenas letras e números")
    private String senha;

    @NotNull(groups = OnCreate.class, message = "A data de nascimento é obrigatória")
    @Past(groups = {OnCreate.class, OnUpdate.class}, message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    private String cpf;
}