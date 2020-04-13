/**
 *
 * Author(s): Yanisse/Asma
 * Jira Task: N/A
 * Description: The main component code for application. Holds the component code to be used in all the other components
 * of the app.
 *
 **/

import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { AmplifyService } from 'aws-amplify-angular';
import { Auth } from 'aws-amplify';
import { SessionService } from './services/session.service';
import { Notification } from './components/notification/notification_model';
import { NotificationsServiceService } from './components/notification';
import { interval, Observable } from 'rxjs';
import { mapTo, startWith, map, flatMap, switchMap } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ajax } from 'rxjs/ajax';

interface IQuote {
  id: string;
  owner: string;
  type: string;
  key: string;
  content: string;
  deliveredstring;
  remindTime: string;
}
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Time Helper';
  currentLoc = '';

  quote: Observable<string>;

  signedIn: boolean;
  user: any;
  greeting: string;
  constructor(
    private amplifyService: AmplifyService,
    private sessionService: SessionService,
    private nService: NotificationsServiceService,
    private titleService: Title,
    private _http: HttpClient
  ) {
    this.titleService.setTitle(this.title);

    this.amplifyService.authStateChange$.subscribe(authState => {
      this.signedIn = authState.state === 'signedIn';
      if (!authState.user) {
        this.user = null;
      } else {
        this.user = authState.user;
        this.greeting = 'Hello ' + this.user.username;
        // console.log('User ', this.user);

        Auth.currentSession()
          .then(data => {
            sessionService.setToken(data.getAccessToken().getJwtToken());
            sessionService.setExpirationTime(data.getAccessToken().getExpiration());
            console.log('Token: ' + this.sessionService.getToken());
            console.log('Init Date: ' + new Date());
            }
          )
          .catch(err => console.log(err));
      }
    });

 // 'https://api.chucknorris.io/jokes/random'
 // https://rxjs-dev.firebaseapp.com/api/ajax/ajax

  }

  ngOnInit() {

    Auth.currentSession()
      .then(data => {
        let headers = new HttpHeaders().set(
          'Authorization',
          'Bearer ' + data.getAccessToken().getJwtToken()
        );
        interval(3000)
          .pipe(
            startWith(0), // Starts immediatly
            mapTo(
              // Map to your request
              this._http.get<IQuote[]>(
                'http://localhost:8001/notification/new',
                { headers }
              )
            ),
            flatMap(v => v),
            map(quote => quote)
          )
          .subscribe(x => {
            if (x.length > 0) {
              console.log(x);
              this.nService.clear();
              this.nService.remaind(x[0].content, x[0].id, {
                autoClose: false,
                keepAfterRouteChange: true
              });
            }

            const timeNow = new Date();
            timeNow.setTime(timeNow.getTime() + 30 * 60000);
            // timeNow.setMinutes(timeNow.getMinutes() + 50);
            const currentUnixTime = timeNow.getTime() / 1000;

            if (currentUnixTime > this.sessionService.getExpirationTime()) {
                // console.log('the token is expired!');
              // console.log('Expiration in service: ', this.sessionService.getExpirationTime());
              // console.log(data.getAccessToken().getExpiration());
              this.sessionService.refreshSession();
              } else {
                // console.log('the token is valid');
              }


            headers = new HttpHeaders().set(
                'Authorization',
                'Bearer ' + this.sessionService.getToken());



          });
      })
      .catch(err => console.log(err));
  }
}
