/** Use Case Linked Issue: TMGP4-39 , TMGP4-43
 *
 *  Test Case Linked Issue: TMGP4-290
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, TestBed} from '@angular/core/testing';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import { CategoryService } from './category.service';
import {DeleteTaskDialogComponent} from '../components/delete-task-dialog/delete-task-dialog.component';
import {BrowserDynamicTestingModule, platformBrowserDynamicTesting} from '@angular/platform-browser-dynamic/testing';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterTestingModule} from '@angular/router/testing';
import {MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../modules/material.module';
import {AmplifyService} from 'aws-amplify-angular';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';


describe('CategoryService', () => {

  beforeEach(async(() => {
    TestBed.resetTestEnvironment();
    TestBed.initTestEnvironment(BrowserDynamicTestingModule,
        platformBrowserDynamicTesting());
    TestBed.configureTestingModule({
      imports: [ HttpClientModule, BrowserAnimationsModule,
        RouterTestingModule.withRoutes([]),
        MatIconModule,
        MatSelectModule,
        CustomMaterialModule,
        MatFormFieldModule,
        MatInputModule,
        MatDialogModule,
      ],
      providers: [AmplifyService, HttpClient, {
        provide: MatDialogRef,
        useValue: {}},
        { provide: MAT_DIALOG_DATA, useValue: {}}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ DeleteTaskDialogComponent ]
    })
        .compileComponents();
  }));
  beforeEach(() => TestBed.configureTestingModule({}));

  // This test verifies that the Service is able to be constructed
  it('This test verifies that the Service is able to be constructed', () => {
    const service: CategoryService = TestBed.get(CategoryService);
    expect(service).toBeTruthy();
  });

  // This test verifies that the function getCategories is defined
  it('This test verifies that the function getCategories is defined', async(() => {
    const token = 'Test';
    const service: CategoryService = TestBed.get(CategoryService);
    const spy = spyOn(service, 'getCategories').and.callThrough();
    service.getCategories(token);
    expect(spy).toBeDefined();
  }));

  // This test verifies that the function getAvgCategories is defined
  it('This test verifies that the function getAvgCategories is defined', async(() => {
    const token = 'TestAvg';
    const service: CategoryService = TestBed.get(CategoryService);
    const spy = spyOn(service, 'getAvgCategories').and.callThrough();
    service.getAvgCategories(token);
    expect(spy).toBeDefined();
  }));

  it('should be call create', async(() => {
    const service: CategoryService = TestBed.get(CategoryService);
    spyOn(service, 'create').and.callThrough();
    expect(service.create).toBeTruthy();
  }));
});
