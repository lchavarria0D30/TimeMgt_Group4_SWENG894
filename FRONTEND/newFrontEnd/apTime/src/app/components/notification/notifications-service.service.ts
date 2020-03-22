/**
 *
 * Author: Asma
 * Jira Task:
 * Description:
 *
 **/

import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { NotificationType, Notification } from './notification_model';
import { NotificationModule } from './notification.module';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NotificationsServiceService {
  private subject = new Subject<Notification>();
  private defaultId = 'default-alert';

  // alerts subscription entiry
  /* onNotify(id = this.defaultId): Observable<Notification> {
    console.log('id passed to service from notification', id);

    return this.subject.asObservable().pipe(filter(x => x && x.id === id));
  }
  */
  onNotify(callme): Observable<Notification> {
    //console.log('id passed to service from notification', id);

    //return this.subject.asObservable().pipe(filter(x => x && x.id === id));
    return callme(
      this.subject.asObservable().pipe(filter(x => x != undefined))
    );
  }

  remaind(message: string, id: string, options?: any) {
    console.log('id for notification' + id);
    this.notify(
      new Notification({
        ...options,
        type: NotificationType.Reminder,
        id,
        message
      })
    );
  }

  // main alert method
  notify(note: Notification) {
    note.id = note.id || this.defaultId;
    console.log(' notify()supplied NotificationID :' + note.id);
    this.subject.next(note);
  }

  // clear alerts
  clear(id = this.defaultId) {
    this.subject.next(new Notification({ id }));
  }
}
