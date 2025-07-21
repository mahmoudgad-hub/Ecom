package com.example.ecom.auth.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtConfig {
    // Getters & Setters
    private String secret;
    private long accessExpiration;
    private long refreshExpiration;

    public void setaccessExpiration(long accessExpiration) {
        this.accessExpiration = accessExpiration;
    }
    public void setrefreshExpiration(long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }
}
