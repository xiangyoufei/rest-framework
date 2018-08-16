package com.example.demo.security;

public interface TokenManager {

    String createToken(String username);

    boolean checkToken(String token);
}
