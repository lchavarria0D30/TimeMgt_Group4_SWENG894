import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { Subscription } from 'rxjs';
import { NotificationType, Notification } from '../notification_model';
import { NotificationsServiceService } from '../notifications-service.service';

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
    private noteService: NotificationsServiceService
  ) { }

  ngOnInit() {
    // subscribe to new alert notifications
    this.noteSubscription = this.noteService
      .onNotify(this.id)
      .subscribe(alert => {
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

        /*             // auto close alert if required
            if (note.autoClose) {
                setTimeout(() => this.removeAlert(alert), 3000);
            } */
      });
  }

  ngOnDestroy() {
    // unsubscribe on destroy
    this.noteSubscription.unsubscribe();
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

  cssClass(alert: Notification) {
    if (!alert) return;

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
