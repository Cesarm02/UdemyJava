package com.example.best_travel.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class SecurityConfig {


    @Value("${app.client.id}")
    private String clientId;
    @Value("${app.client.secret}")
    private String clientSecret;
    @Value("${app.client-scope-read}")
    private String scopeRead;
    @Value("${app.client-scope-write}")
    private String scopeWrite;
    @Value("${app.client-redirect-debugger}")
    private String redirectUri1;
    @Value("${app.client-redirect-spring-doc}")
    private String redirectUri2;

    private final UserDetailsService userDetailsService;

    public SecurityConfig(@Qualifier(value = "appUserService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());

        httpSecurity.exceptionHandling(e -> e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_RESOURCE)));

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .formLogin()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(PUBLIC_RESOURCES)
                .permitAll()
                .requestMatchers(USER_RESOURCES)
                .authenticated()
                .requestMatchers(ADMIN_RESOURCES)
                .hasRole(ADMIN_ROLE)
                .and()
                .oauth2ResourceServer()
                .jwt();

        return httpSecurity.build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider(BCryptPasswordEncoder encoder){
        var  authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(encoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
       return  authenticationProvider;

    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(BCryptPasswordEncoder encoder){
        var registeredClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(encoder.encode(clientSecret))
                .scope(scopeRead)
                .scope(scopeWrite)
                .redirectUri(redirectUri1)
                .redirectUri(redirectUri2)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder()

                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource){
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(){
        var rsaKey = generateKeys();
        var jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .refreshTokenTimeToLive(Duration.ofHours(8))
                .build();
    }

    private static KeyPair generateRsa(){
        KeyPair keyPair = null;
        try{
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

    private static RSAKey generateKeys(){
        var keyPair = generateRsa();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

    }

    private static final String[] PUBLIC_RESOURCES = {"/fly/**","/hotel/**","/swagger-ui/**", "/.well-known/**, ", "/v3/api-docs/**"};
    private static final String[] USER_RESOURCES = {"/tour/**","/ticket/**","/reservation/**"};
    private static final String[] ADMIN_RESOURCES = {"/user/**", "/report/**"};
    private static final String LOGIN_RESOURCE = "/login";
    private static final String ADMIN_ROLE = "ADMIN";

}
