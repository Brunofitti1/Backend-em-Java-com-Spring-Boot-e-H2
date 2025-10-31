package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.tools.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Serviço de autenticação simplificada
 * Aceita qualquer email/senha válidos e gera um token JWT
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtUtil jwtUtil;

    /**
     * Realiza o login de forma simplificada
     * Qualquer email válido + qualquer senha = autenticação bem-sucedida
     * 
     * @param request dados de login (email + senha)
     * @return resposta com token JWT
     */
    public AuthResponse login(LoginRequest request) {
        log.info("Tentativa de login para email: {}", request.getEmail());

        // Validação básica: apenas verifica se email e senha não estão vazios
        // (a validação de formato já foi feita pelo @Valid no controller)

        // Gera o token JWT com o email do usuário
        String token = jwtUtil.generateToken(request.getEmail());

        log.info("Login bem-sucedido para: {}", request.getEmail());

        return AuthResponse.success(token, request.getEmail());
    }

    /**
     * Valida um token JWT
     * 
     * @param token token a ser validado
     * @return true se válido, false caso contrário
     */
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    /**
     * Extrai o email do token
     * 
     * @param token token JWT
     * @return email contido no token
     */
    public String getEmailFromToken(String token) {
        return jwtUtil.getEmailFromToken(token);
    }
}
