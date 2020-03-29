 package com.apptime.auth.config;

import java.util.Map;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

 /**
  * @author Bashiir Mohamed
  * This class transilates infrestucture auth 2.0 claims to spring security coresponding authorities.
  */
 @Component
public class AccessTokenConverter extends JwtAccessTokenConverter {
  private static final String COGNITO_GROUPS = "cognito:groups";
  private static final String SPRING_AUTHORITIES = "authorities";
  private static final String COGNITO_USERNAME = "username";
  private static final String SPRING_USER_NAME = "user_name";

   /**
    *
    * @param claims auth 2.0 claims
    * @return spring corresponding  authorities.
    */
  @Override
  public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {

    if (claims.containsKey(COGNITO_GROUPS)) {
      ((Map<String, Object>) claims).put(SPRING_AUTHORITIES, claims.get(COGNITO_GROUPS));
    }
    if (claims.containsKey(COGNITO_USERNAME)) {
      ((Map<String, Object>) claims).put(SPRING_USER_NAME, claims.get(COGNITO_USERNAME));
    }
    return super.extractAuthentication(claims);
  }
}
