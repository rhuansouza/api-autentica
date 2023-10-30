package com.example.apiautentica.config;

import com.example.apiautentica.enums.UserRole;
import com.example.apiautentica.service.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@Configuration
public class CustomFilter implements Filter {


    @Autowired
    JwtUtils jwtUtils;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Implemente sua lógica de filtro aqui
        // Exemplo: Verifique se um cabeçalho de autenticação está presente
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        System.out.println("Api-key "+((HttpServletRequest) request).getHeader("x-api-key"));
        System.out.println("Bearer token "+((HttpServletRequest) request).getHeader("Authorization"));
        // Remova o prefixo "Bearer "
        String token = ((HttpServletRequest) request).getHeader("Authorization").replace("Bearer ", "");
        String authorizationHeader = ((HttpServletRequest) request).getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        // O cabeçalho de autenticação está presente e começa com "Bearer"
            boolean retorno =  jwtUtils.decodeJwt(token, request);

            if (retorno) {
                    chain.doFilter(request, response);
            }else{
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                    //throw new ServletException("Ocorreu um erro na requisição"); // Interrompe a requisição
                    httpResponse.getWriter().write("Você não tem permissão para acessar esta rota");
                    // Se a validação for bem-sucedida, chame o próximo filtro na cadeia

            }


        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
         }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização do filtro (opcional)
    }

    @Override
    public void destroy() {
        // Liberação de recursos (opcional)
    }
}
