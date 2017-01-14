package com.carnival.auth.config;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.List;

/**
 * JKS Generation
 * keytool -genkeypair -alias carnivaltest -keyalg RSA -keypass carnivalpass -keystore carnivaltest.jks -storepass carnivalpass
 * keytool -list -rfc --keystore carnivaltest.jks | openssl x509 -inform pem -pubkey
 * http://stytex.de/blog/2016/02/01/spring-cloud-security-with-oauth2/
 */

@Configuration
@EnableAuthorizationServer
@Slf4j
class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.oauth2.client.authorized-grant-types}")
    private String authorized_grant_types;
    @Value("${security.oauth2.client.scope}")
    private String scope;
    @Value("${security.oauth2.client.clientId}")
    private String clientId;
    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Environment env;

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        List<String> grantTypes = Lists.newArrayList(Splitter.on(",")
                .omitEmptyStrings().trimResults()
                .split(authorized_grant_types));
        clients
                .inMemory()
                .withClient(clientId)
                .secret(clientSecret)
                .scopes(scope)
                .authorizedGrantTypes(grantTypes.toArray(new String[grantTypes.size()]))
                .accessTokenValiditySeconds(3600) //1 hour
                .refreshTokenValiditySeconds(2592000) //30 days
                .and()
                .withClient("edge-service")
                .secret(env.getProperty("EDGE_SERVICE_SECRET"))
                .scopes(scope)
                .authorizedGrantTypes(grantTypes.toArray(new String[grantTypes.size()]))
                .accessTokenValiditySeconds(3600) //1 hour
                .refreshTokenValiditySeconds(2592000) //30 days
                .and()
                .withClient("reservation-service")
                .secret(env.getProperty("RESERVATION_SERVICE_SECRET"))
                .scopes(scope)
                .authorizedGrantTypes(grantTypes.toArray(new String[grantTypes.size()]))
                .accessTokenValiditySeconds(3600) //1 hour
                .refreshTokenValiditySeconds(2592000) //30 days
                .and()
                .withClient("venue-service")
                .secret(env.getProperty("VENUE_SERVICE_SECRET"))
                .scopes(scope)
                .authorizedGrantTypes(grantTypes.toArray(new String[grantTypes.size()]))
                .accessTokenValiditySeconds(3600) //1 hour
                .refreshTokenValiditySeconds(2592000) //30 days
        ;
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtTokenEnhancer()).authenticationManager(authenticationManager);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    @Bean
    public JwtAccessTokenConverter jwtTokenEnhancer() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("carnivaltest.jks"), "carnivalpass".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("carnivaltest"));
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

}