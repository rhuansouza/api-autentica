package com.example.apiautentica.service;

import com.auth0.jwt.JWT;
import com.example.apiautentica.enums.UserRole;
import io.jsonwebtoken.*;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class JwtUtils {
    private long tokenValidityMilliseconds = 3600000; // Tempo de validade do token (1 hora)
    public static final String SECRET_KEY = "bc2e1f676370b8131e3a1f84fc6c1cd6c9266f279030c92604fde8cfa8a1e0d3";



    @Value("${jwt.token.secret}")
    private String chaveSecreta;
    public boolean decodeJwt(String jwt, ServletRequest request) {
        String uri = String.valueOf(((HttpServletRequest) request).getRequestURI());
        System.out.println("URI "+uri);
            try {
                String token = createToken();
                System.out.println("Token criado "+createToken());
                try {
                  //Extraindo Dados do Token
                  Claims claims = Jwts.parser()
                        .setSigningKey(chaveSecreta)
                        .parseClaimsJws(jwt)
                        .getBody();
                    System.out.println("Expiracao do token "+ claims.getExpiration());
                    //Validando se token é valido
                    if(claims.getExpiration().before(new Date())){
                        System.out.println("Token expirou");
                        return false;
                    }

                    String roleToken = JWT.decode(jwt).getClaims().get("role").toString().replace("\"","");
                    // Remova os colchetes iniciais e finais, depois divida a string usando a vírgula como delimitador
                    String[] rolesUsuario = roleToken.substring(1, roleToken.length() - 1).split(",");

                    for (UserRole roles : UserRole.values()) {
                        for(String roleUsuario : rolesUsuario){
                            if (roles.toString().equals(roleUsuario)) {
                                UserRole role = UserRole.valueOf(roleUsuario);
                                for (String route : role.getAllowedRoutes()) {
                                    if (route.equals(uri)){
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                    return false;



                } catch (ExpiredJwtException ex) {
                    // Token expirado
                    System.out.println("Token expirado"+ex);
                } catch (Exception e) {
                    // Outros erros, como token inválido
                    System.out.println("Token expirado");
                }
                return false;
        } catch (Exception e) {
            // Trate exceções, como token inválido ou expirado
                System.out.println("Erro "+e);
            return false;
        }
    }


    // Criação de token
    public String createToken() {

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityMilliseconds);
        Map<String, Object> claims = new HashMap<>();

        claims.put("sub", "autenticacao"); // Identificador do assunto
        claims.put("name", "Rhuan"); // Nome do usuário
        claims.put("iat", now); //Hora que criou o token
        claims.put("exp", validity); //Tempo de Expiração do token
        claims.put("role", "[ADMINISTRADOR,ARQUIVISTA]"); // Adicione o novo claim aqui

        return Jwts.builder()
                .setSubject("Rhuan")
                .setIssuedAt(now)
                .setExpiration(validity)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, chaveSecreta)
                .compact();

    }
}

