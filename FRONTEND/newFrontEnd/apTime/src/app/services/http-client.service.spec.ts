/** Use Case Linked Issue: TMGP4-184
 *
 *  Test Case Linked Issue: TMGP4-291
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import { TestBed } from '@angular/core/testing';

import { HttpClientService } from './http-client.service';

describe('HttpClientService', () => {
  // let service : HttpClientService;
  beforeEach(() => TestBed.configureTestingModule({}));

  // This test verifies that the Service can be called
  it('This test verifies that the Service can be called', () => {
    const service: HttpClientService = TestBed.get(HttpClientService);
    expect(service).toBeTruthy();
  });

  it('This Test verifies that the Http service is defined', () => {
    const service: HttpClientService = TestBed.get(HttpClientService);
    expect(service).toBeDefined();
  });
});
