 package com.apptime.auth.config;
import java.util.Collections;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;

 /**
  * @author Bashiir Mohamed
  * This class configures spring security to use outh 2.0
  * it autowires ResoucreServerProperties.
  */
 @EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends ResourceServerConfigurerAdapter {

   /**
    *  Configuration properties for OAuth2 Resources.
    */
  private final ResourceServerProperties resource;

  public SecurityConfig(ResourceServerProperties resource) {
    this.resource = resource;
  }

   /**
    *
    * @param http configure http request to be routed through auth 2.0
    * @throws Exception IOExeption related to request processing
    */
  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.cors();

    http.csrf().disable();

    http.authorizeRequests()
        .antMatchers("/**")
        .permitAll()
        .anyRequest()
        .authenticated();
  }

   /**
    *
    * @return Returnes token converted to spring security authority
    */
  @Bean
  public TokenStore jwkTokenStore() {
    return new JwkTokenStore(
        Collections.singletonList(resource.getJwk().getKeySetUri()),
        new AccessTokenConverter(),
        null);
  }
}
