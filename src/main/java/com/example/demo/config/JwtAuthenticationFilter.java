package com.example.demo.config;

import com.example.demo.tools.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Filtro JWT que intercepta todas as requisições e valida o token
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Pega o header Authorization
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;

        // Verifica se o header existe e começa com "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer "

            try {
                email = jwtUtil.getEmailFromToken(token);
            } catch (Exception e) {
                log.error("Erro ao extrair email do token: {}", e.getMessage());
            }
        }

        // Se o token é válido e não há autenticação no contexto
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (jwtUtil.validateToken(token)) {
                // Cria uma autenticação válida
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        new ArrayList<>() // Sem roles/authorities nesta versão simplificada
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Define a autenticação no contexto do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("Token JWT válido para: {}", email);
            }
        }

        // Continua o filtro
        filterChain.doFilter(request, response);
    }
}
