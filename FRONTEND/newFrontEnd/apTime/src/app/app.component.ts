import { Component } from '@angular/core';
import { AmplifyService } from 'aws-amplify-angular';
import { Auth } from 'aws-amplify';
import { SessionService } from './services/session.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'apTime got new look';

    signedIn: boolean;
    user: any;
    greeting: string;
    constructor( private amplifyService: AmplifyService, private sessionService: SessionService ) {
        this.amplifyService.authStateChange$
            .subscribe(authState => {
                this.signedIn = authState.state === 'signedIn';
                if (!authState.user) {
                    this.user = null;
                } else {
                    this.user = authState.user;
                    this.greeting = 'Hello ' + this.user.username;
                    Auth.currentSession()
                    .then(data => sessionService.setToken(data.getAccessToken().getJwtToken())
                    )
                    .catch(err => console.log(err));
                }
        });
    }

}
