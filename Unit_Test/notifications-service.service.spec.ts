/** Use Case Linked Issue: TMGP4-40
 *
 *  Test Case Linked Issue: TMGP4-239
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, TestBed} from '@angular/core/testing';

import { NotificationsServiceService } from './notifications-service.service';
import {NotificationType} from './notification_model';
import any = jasmine.any;

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

    // This test verifies that the Component is created.
    expect(notifyReceived).toBeTruthy();
  }));

  // This test verifies that the function remaind is defined and called
  it('This test verifies that the function remaind is defined and called', () => {
    const message = 'Test Message';
    const id = '123Test';
    const opt = any;
    const service: NotificationsServiceService = TestBed.get(NotificationsServiceService);
    spyOn(service, 'remaind').and.callThrough();
    service.remaind(message, id, opt);
    expect(service.remaind).toBeDefined();
    expect(service.remaind).toHaveBeenCalled();
  });

  // This test verifies that the function clear is defined and called
  it('This test verifies that the function clear is defined and called', () => {
    const id = '123Test';
    const service: NotificationsServiceService = TestBed.get(NotificationsServiceService);
    const spy = spyOn(service, 'clear').and.callThrough();
    service.clear(id);
    expect(spy).toBeDefined();
    expect(spy).toHaveBeenCalled();
  });
});
