import {async, TestBed} from '@angular/core/testing';

import { SessionService } from './session.service';
import {CategoryService} from "./category.service";

describe('SessionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SessionService = TestBed.get(SessionService);
    expect(service).toBeTruthy();
  });

  it('should be call get', async(() => {
    const service: SessionService = TestBed.get(SessionService);
    spyOn(service, 'getToken').and.callThrough();
    expect(service.getToken).toBeDefined();
  }));

});
