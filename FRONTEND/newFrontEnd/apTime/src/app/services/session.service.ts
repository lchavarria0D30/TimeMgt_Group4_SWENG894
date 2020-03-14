import { Injectable } from '@angular/core';
import { Auth } from 'aws-amplify';


@Injectable({
  providedIn: 'root'
})
export class SessionService {

  token;

  constructor() { }

  setToken(theToken: string): void {
    this.token = theToken;
    console.log("Token: " + this.token);
  }

  getToken(): string {
    return this.token;
  }
}
