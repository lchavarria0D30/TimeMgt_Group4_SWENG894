/**
 *
 * Author: Yanisse
 * Jira Task: TMPG4-14
 * Description: The service in charge of saving and sharing the session details through the application.
 *
 **/

import { Injectable } from '@angular/core';
import { Auth } from 'aws-amplify';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  token;
  expirationTime;

  constructor() {}

  setToken(theToken: string): void {
    this.token = theToken;
  }

  getToken(): string {
    return this.token;
  }

    setExpirationTime(expirationTime: number): void {
        this.expirationTime = expirationTime;
    }

    getExpirationTime(): number {
        return this.expirationTime;
    }

  async refreshSession(): Promise<void> {
        // console.log('Refresh: ', new Date());
      try {
          const cognitoUser = await Auth.currentAuthenticatedUser();
          const currentSession = await Auth.currentSession();
          cognitoUser.refreshSession(currentSession.getRefreshToken(), (err, session) => {
              // console.log('the session is refreshed: ');
              // console.log('session', err, session);

              const { idToken, refreshToken, accessToken } = session;
              // do whatever you want to do now :)
              this.token = accessToken.getJwtToken() ;
              this.expirationTime = accessToken.getExpiration();
              console.log('the new token: ' + this.token);

          });
      } catch (e) {
          console.log('Unable to refresh Token', e);
      }
  }

}
