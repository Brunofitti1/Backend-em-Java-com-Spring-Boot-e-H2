package com.example.demo.tools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

/**
 * Utilitário para geração e validação de tokens JWT simplificados
 * Implementação sem dependências externas (apenas Java nativo)
 */
@Component
public class JwtUtil {
    
    @Value("${jwt.secret:festo-sensors-jwt-secret-key-super-secreta-para-desenvolvimento-2024}")
    private String secret;
    
    @Value("${jwt.expiration:86400000}") // 24 horas em millisegundos
    private Long expiration;
    
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    
    /**
     * Gera um token JWT simplificado para o email fornecido
     * Formato: base64(header).base64(payload).base64(signature)
     */
    public String generateToken(String email) {
        try {
            long currentTime = System.currentTimeMillis();
            long expirationTime = currentTime + expiration;
            
            // Header JWT padrão
            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            
            // Payload com email e timestamps
            String payload = String.format(
                "{\"email\": \"%s\",\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}",
                email, email, currentTime / 1000, expirationTime / 1000
            );
            
            // Encode header e payload em Base64 URL-safe
            String encodedHeader = base64UrlEncode(header);
            String encodedPayload = base64UrlEncode(payload);
            
            // Cria assinatura HMAC SHA256
            String dataToSign = encodedHeader + "." + encodedPayload;
            String signature = createSignature(dataToSign);
            
            return dataToSign + "." + signature;
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }
    
    /**
     * Extrai o email do token
     */
    public String getEmailFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Token JWT inválido");
            }
            
            String payload = base64UrlDecode(parts[1]);
            
            // Parse simples do JSON para extrair email (com trim de espaços)
            int emailStart = payload.indexOf("\"email\":") + 8;
            // Pular espaços e aspas
            while (payload.charAt(emailStart) == ' ' || payload.charAt(emailStart) == '"') {
                emailStart++;
            }
            int emailEnd = payload.indexOf("\"", emailStart);
            
            return payload.substring(emailStart, emailEnd);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair email do token", e);
        }
    }
    
    /**
     * Verifica se o token é válido
     */
    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }
            
            // Verifica assinatura
            String dataToVerify = parts[0] + "." + parts[1];
            String expectedSignature = createSignature(dataToVerify);
            
            if (!expectedSignature.equals(parts[2])) {
                return false;
            }
            
            // Verifica expiração
            String payload = base64UrlDecode(parts[1]);
            int expStart = payload.indexOf("\"exp\":") + 6;
            int expEnd = payload.indexOf(",", expStart);
            if (expEnd == -1) expEnd = payload.indexOf("}", expStart);
            
            long expTimestamp = Long.parseLong(payload.substring(expStart, expEnd).trim());
            long currentTimestamp = System.currentTimeMillis() / 1000;
            
            return currentTimestamp < expTimestamp;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Cria assinatura HMAC SHA256
     */
    private String createSignature(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
        sha256Hmac.init(secretKey);
        
        byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(signedBytes);
    }
    
    /**
     * Encode Base64 URL-safe
     */
    private String base64UrlEncode(String data) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Decode Base64 URL-safe
     */
    private String base64UrlDecode(String encoded) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encoded);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
