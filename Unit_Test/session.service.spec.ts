/** Use Case Linked Issue: TMGP4-34
 *
 *  Test Case Linked Issue: TMGP4-292
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, TestBed} from '@angular/core/testing';

import { SessionService } from './session.service';


describe('SessionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  // This test verifies that the Service is able to be constructed
  it('This test verifies that the Service is able to be constructed', () => {
    const service: SessionService = TestBed.get(SessionService);
    expect(service).toBeTruthy();
  });

  // This test verifies that the function getToken is defined
  it('This test verifies that the function getToken is defined', async(() => {
    const service: SessionService = TestBed.get(SessionService);
    const spy = spyOn(service, 'getToken').and.callThrough();
    service.getToken();
    expect(spy).toBeDefined();
  }));

  // This test verifies that the function setToken is defined
  it('This test verifies that the function setToken is defined', async(() => {
    const service: SessionService = TestBed.get(SessionService);
    const token = 'TestSession';
    const spy = spyOn(service, 'setToken').and.callThrough();
    service.setToken(token);
    expect(spy).toBeDefined();
  }));

  // This test verifies that the function setExpirationTime is defined
  it('This test verifies that the function setExpirationTime is defined', async(() => {
    const service: SessionService = TestBed.get(SessionService);
    const expTime = 1255;
    const spy = spyOn(service, 'setExpirationTime').and.callThrough();
    service.setExpirationTime(expTime);
    expect(spy).toBeDefined();
  }));

  // This test verifies that the function getExpirationTime is defined
  it('This test verifies that the function getExpirationTime is defined', async(() => {
    const service: SessionService = TestBed.get(SessionService);
    const spy = spyOn(service, 'getExpirationTime').and.callThrough();
    service.getExpirationTime();
    expect(spy).toBeDefined();
  }));

  // This test verifies that the function refreshSession is defined
  it('This test verifies that the function refreshSession is defined', async(() => {
    const service: SessionService = TestBed.get(SessionService);
    const spy = spyOn(service, 'refreshSession').and.callThrough();
    service.refreshSession();
    expect(spy).toBeDefined();
  }));
});
