package com.example.productcatalogservice.controllers;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private String token;
    private Collection<GrantedAuthority> authorities;

    public TokenAuthentication(String token, Collection<GrantedAuthority> authorities) {
        super(authorities); // Pass authorities to the parent class
        this.token = token;
        this.authorities = authorities;
        setAuthenticated(true); // Set authenticated to true if token is valid
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null; // You can modify this to return user details if necessary
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
