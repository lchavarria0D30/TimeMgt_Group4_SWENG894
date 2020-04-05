import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { Subscription } from 'rxjs';
import { NotificationType, Notification } from '../notification_model';
import { NotificationsServiceService } from '../notifications-service.service';
import { Auth } from 'aws-amplify';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit, OnDestroy {
  @Input() id = 'default-alert';
  @Input() fade = true;
  notes: Notification[] = [];
  noteSubscription: Subscription;
  routeSubscription: Subscription;
  constructor(
    private route: Router,
    private noteService: NotificationsServiceService,
    private _http: HttpClient,
    private sessionService: SessionService
  ) {}

  ngOnInit() {
    // subscribe to new alert notifications
    console.log('in notification component init --');
    this.noteService.onNotify(data => {
      data.subscribe(alert => {
        console.log('notification component subscribing for notification --');
        // clear alerts when an empty alert is received
        if (!alert.message) {
          // filter out alerts without 'keepAfterRouteChange' flag
          this.notes = this.notes.filter(x => x.keepAfterRouteChange);
          // remove 'keepAfterRouteChange' flag on the rest
          this.notes.forEach(x => delete x.keepAfterRouteChange);
          return;
        }
        // add alert to array
        this.notes.push(alert);
        // auto close alert if required
        /*     if (alert.autoClose) {
                setTimeout(() => this.removeAlert(alert), 3000);
            } 
            */
      });
    });
  }

  ngOnDestroy() {
    // unsubscribe on destroy
    //this.noteSubscription.unsubscribe();
    this.routeSubscription.unsubscribe();
  }
  removeNote(alert: Notification) {
    if (this.fade) {
      // fade out alert
      this.notes.find(x => x === alert).fade = true;

      // remove alert after faded out
      setTimeout(() => {
        this.notes = this.notes.filter(x => x !== alert);
      }, 2500);
    } else {
      // remove alert
      this.notes = this.notes.filter(x => x !== alert);
    }
  }

  cancel(alert: Notification) {
    if (alert) {
      console.log(alert.id);
      const headers = {
        Authorization: 'Bearer ' + this.sessionService.getToken(),
        'Content-Type': 'application/json'
      };
      this._http
        .delete('http://localhost:8001/notification/' + alert.id, {
          headers
        })
        .subscribe({
          next: data => {
            console.log(data);
            this.notes = this.notes.filter(x => x.id !== alert.id);
          },
          error: error => console.error('There was an error!', error)
        });
    }
  }

  snooze(alert: Notification) {
    console.log(alert.id);
    const headers = {
      Authorization: 'Bearer ' + this.sessionService.getToken(),
      'Content-Type': 'application/json'
    };
    this._http
      .put(
        'http://localhost:8001/notification/' + alert.id + '/' + 'snooze',
        {},
        {
          headers
        }
      )
      .subscribe({
        next: data => {
          console.log(data);
          this.notes = this.notes.filter(x => x.id !== alert.id);
        },
        error: error => console.error('There was an error!', error)
      });
  }

  cssClass(alert: Notification) {
    if (!alert) {
      return;
    }

    const classes = ['alert', 'alert-dismissable'];

    const alertTypeClass = {
      [NotificationType.Reminder]: 'reminder',
      [NotificationType.LateNotification]: 'lateNote'
    };
    classes.push(alertTypeClass[alert.type]);

    if (alert.fade) {
      classes.push('fade');
    }

    return classes.join(' ');
  }
}
