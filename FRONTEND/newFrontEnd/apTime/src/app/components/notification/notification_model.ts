/**
 *
 * Author: Asma
 * Jira Task:
 * Description:
 *
 **/

export class Notification {
  type: NotificationType;
  id: string;
  message: string;
  autoClose: boolean;
  keepAfterRouteChange: boolean;
  fade: boolean;

  constructor(init?: Partial<Notification>) {
    Object.assign(this, init);
  }
}

export enum NotificationType {
  LateNotification,
  Reminder
}
