/**
 *
 * Author(s): Yanisse/Asma
 * Jira Task: N/A
 * Description: The main component code for application. Holds the component code to be used in all the other components
 * of the app.
 *
 **/

import { Component, OnInit } from '@angular/core';
import { AmplifyService } from 'aws-amplify-angular';
import { Auth } from 'aws-amplify';
import { SessionService } from './services/session.service';
import { Notification } from './components/notification/notification_model';
import { NotificationsServiceService } from './components/notification';
import { interval, Observable } from 'rxjs';
import { mapTo, startWith, map, flatMap, switchMap } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { ajax } from 'rxjs/ajax';


interface IQuote {
  categories: any[];
  created_at: string;
  icon_url: string;
  id: string;
  url: string;
  value: string;
}
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  title = 'apTime got new look';
  quote: Observable<string>;

  signedIn: boolean;
  user: any;
  greeting: string;
  constructor(
    private amplifyService: AmplifyService,
    private sessionService: SessionService,
    private nService: NotificationsServiceService,
    private _http: HttpClient
  ) {
    this.amplifyService.authStateChange$.subscribe(authState => {
      this.signedIn = authState.state === 'signedIn';
      if (!authState.user) {
        this.user = null;
        console.log('AQUI NOT ' + this.user);
      } else {
        this.user = authState.user;
        this.greeting = 'Hello ' + this.user.username;
        //console.log("User " + this.user);

        Auth.currentSession()
          .then(data =>
            sessionService.setToken(data.getAccessToken().getJwtToken())
          )
          .catch(err => console.log(err));
      }
    });
    //'https://api.chucknorris.io/jokes/random'
    //https://rxjs-dev.firebaseapp.com/api/ajax/ajax
    interval(3000)
      .pipe(
        startWith(0), // Starts immediatly
        mapTo(
          // Map to your request
          this._http.get<IQuote>('https://api.chucknorris.io/jokes/random')
          // ajax('https://api.chucknorris.io/jokes/random')
        ),
        flatMap(v => v),
        map(quote => quote.value) // Take the field you need
      )
      .subscribe(x => {
        console.log(x);
        nService.clear();
        nService.remaind(x, { autoClose: false, keepAfterRouteChange: true });
      });
  }


  ngOnInit() {
    // console.log(this.quote);
    //  .subscribe(res => this.statuses = res.statuses
  }
}
