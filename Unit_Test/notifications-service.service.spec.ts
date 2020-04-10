/** Linked Issue: TMGP4-40: Notification Task About Start
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, TestBed} from '@angular/core/testing';

import { NotificationsServiceService } from './notifications-service.service';
import {NotificationType} from "./notification_model";

describe('NotificationsServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: NotificationsServiceService = new NotificationsServiceService();
    expect(service).toBeTruthy();
  });

  it('should be Defined onNotify', async(() => {
    const service: NotificationsServiceService = new NotificationsServiceService();
    expect(service.onNotify).toBeDefined();
  }));

  it('should onNotify be called after notify', async(() => {
    const service: NotificationsServiceService = new NotificationsServiceService();
    let notifyReceived = false;
    service.onNotify(data => {
      data.subscribe(alert => {
        // do something we can measure
        notifyReceived = true;
      });
    });

    service.notify({
      type: NotificationType.LateNotification,
      id: '1',
      message: '',
      autoClose: false,
      keepAfterRouteChange: false,
      fade: false});

    expect(notifyReceived).toBeTruthy();
  }));

  it('should be Defined remaind', async(() => {
    const service: NotificationsServiceService = TestBed.get(NotificationsServiceService);
    spyOn(service, 'remaind').and.callThrough();
    // fixture.whenStable().then(() => {
    expect(service.remaind).toBeDefined();
    expect(service.remaind).toHaveBeenCalledTimes(0);

  }));
});
