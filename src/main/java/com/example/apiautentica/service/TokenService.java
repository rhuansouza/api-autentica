package com.example.apiautentica.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import static org.springframework.util.ObjectUtils.isEmpty;
@Service
@AllArgsConstructor
public class TokenService {
    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;
    private static final Long ONE_DAY_IN_SECONDS = 86400L;

    public String extractToken(String token) throws Exception {
        if (isEmpty(token)) {
            throw new Exception("The access token was not informed.");
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }
}
