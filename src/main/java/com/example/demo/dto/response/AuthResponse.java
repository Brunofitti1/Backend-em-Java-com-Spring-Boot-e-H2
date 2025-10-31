package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de autenticação
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String type;
    private String email;
    private String message;

    /**
     * Cria uma resposta de autenticação bem-sucedida
     */
    public static AuthResponse success(String token, String email) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .email(email)
                .message("Login realizado com sucesso!")
                .build();
    }
}
