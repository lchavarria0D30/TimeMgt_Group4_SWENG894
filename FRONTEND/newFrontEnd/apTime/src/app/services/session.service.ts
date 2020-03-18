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

  constructor() {}

  setToken(theToken: string): void {
    this.token = theToken;
    console.log("Token: " + this.token);
  }

  getToken(): string {
    return this.token;
  }
}
