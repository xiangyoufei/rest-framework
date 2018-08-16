package com.example.demo.security;

/**
 * 令牌异常
 *
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }
    
}
