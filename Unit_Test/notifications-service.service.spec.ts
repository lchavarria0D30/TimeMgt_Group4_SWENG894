/** Linked Issue: TMGP4-40: Notification Task About Start
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, TestBed} from '@angular/core/testing';

import { NotificationsServiceService } from './notifications-service.service';

describe('NotificationsServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: NotificationsServiceService = TestBed.get(NotificationsServiceService);
    expect(service).toBeTruthy();
  });

  it('should be Defined onNotify', async(() => {
    const service: NotificationsServiceService = TestBed.get(NotificationsServiceService);
    spyOn(service, 'onNotify').and.callThrough();
    // fixture.whenStable().then(() => {
    expect(service.onNotify).toBeDefined();
    expect(service.onNotify).toHaveBeenCalledTimes(0);

  }));

  it('should be Defined Notify', async(() => {
    const service: NotificationsServiceService = TestBed.get(NotificationsServiceService);
    spyOn(service, 'notify').and.callThrough();
    // fixture.whenStable().then(() => {
    expect(service.notify).toBeDefined();
    expect(service.notify).toHaveBeenCalledTimes(0);

  }));

  it('should be Defined remaind', async(() => {
    const service: NotificationsServiceService = TestBed.get(NotificationsServiceService);
    spyOn(service, 'remaind').and.callThrough();
    // fixture.whenStable().then(() => {
    expect(service.remaind).toBeDefined();
    expect(service.remaind).toHaveBeenCalledTimes(0);

  }));
});
