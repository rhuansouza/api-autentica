package com.example.apiautentica.enums;

public enum UserRole {

    ADMINISTRADOR(new String[] {"/autentica2/teste","/admin/**", "/user/**", "/profile/**"}),
    ARQUIVISTA(new String[] {"/autentica/teste","/user/**", "/profile/**"}),
    GUEST(new String[] {"/public/**"});

    private final String[] allowedRoutes;

    UserRole(String[] allowedRoutes) {
        this.allowedRoutes = allowedRoutes;
    }

    public String[] getAllowedRoutes() {
        return allowedRoutes;
    }
}
