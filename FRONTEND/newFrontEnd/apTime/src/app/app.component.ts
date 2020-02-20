import { Component } from "@angular/core";
import { AmplifyService }  from 'aws-amplify-angular';
import { Auth } from 'aws-amplify';
import { SessionService } from "./services/session.service";


@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent {
  title = "apTime got new look";

    signedIn: boolean;
    user: any;
    greeting: string;
    constructor( private amplifyService: AmplifyService, private sessionService: SessionService ) {
        this.amplifyService.authStateChange$
            .subscribe(authState => {
                this.signedIn = authState.state === 'signedIn';
                if (!authState.user) {
                    this.user = null;
                    console.log("AQUI NOT " + this.user);
                } else {
                    this.user = authState.user;
                    this.greeting = "Hello " + this.user.username;
                    //console.log("User " + this.user);
                    
                    Auth.currentSession()
                    .then(data => 
                    //console.log(data.getAccessToken().getJwtToken())
                    sessionService.setToken(data.getAccessToken().getJwtToken())
                    )
                    .catch(err => console.log(err));
                    //console.log("Token AuthService: " + this.token)
                }
        });
    }

}
