/** Linked Issue: TMGP4-29: Create Task
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, getTestBed, TestBed} from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import { CreateTaskDialogComponent } from './create-task-dialog.component';
import {CustomMaterialModule} from '../../modules/material.module';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {environment} from '../../../environments/environment';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';
import {SessionService} from '../../services/session.service';
import any = jasmine.any;
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";



describe('CreateTaskDialogComponent', () => {
  let component: CreateTaskDialogComponent;
  let fixture: ComponentFixture<CreateTaskDialogComponent>;
  let diaMatRef: CreateTaskDialogComponent;
  let httpMock: HttpTestingController;
  let mockTest: any;
  let session: SessionService;
  let spy = any;

  const dialogMock = {
    close: () =>{ }
  };

  beforeEach(async(() => {
    TestBed.resetTestEnvironment();
    TestBed.initTestEnvironment(BrowserDynamicTestingModule,
        platformBrowserDynamicTesting());
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule, BrowserAnimationsModule,
        RouterTestingModule.withRoutes([]),
        MatIconModule,
        MatSelectModule,
        CustomMaterialModule,
        MatFormFieldModule,
        MatInputModule,
        MatDialogModule,

      ],
      providers: [AmplifyService, {
        provide: MatDialogRef,
        useValue: dialogMock,},
        { provide: MAT_DIALOG_DATA, useValue: {}}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ CreateTaskDialogComponent, ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateTaskDialogComponent);
    component = fixture.componentInstance;
    httpMock = getTestBed().get(HttpTestingController);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component.onNoClick).toBeTruthy();
    const categoryMineRequest = httpMock.expectOne(environment.baseUrl+'/category/');
    categoryMineRequest.flush([]);
    /*const categoryPublicRequest = httpMock.expectOne(environment.baseUrl+'/category/public');
    categoryPublicRequest.flush([]);*/
    httpMock.verify();
  });

  it('should have error for failed category requests', () => {
    expect(component.onNoClick).toBeTruthy();
    const categoryMineRequest = httpMock.expectOne(environment.baseUrl+'/category/');
    categoryMineRequest.error(new ErrorEvent('Generated testing error'));
    /*const categoryPublicRequest = httpMock.expectOne(environment.baseUrl+'/category/public');
    categoryPublicRequest.flush([]);*/
    httpMock.verify();
  });

  it('Test onNoClick DialogRef - Close', () => {
    let spy = spyOn(component.dialogRef, 'close').and.callThrough();
    component.onNoClick();
    expect(spy).toHaveBeenCalled();
    // add expects to test that onNoClick does what it is supposed to
  });
  
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should onNoclick', () => {
    component.onNoClick();
    expect(component.onNoClick).toBeTruthy();
  });

  it('should onYesClick', () => {
    expect(component.onYesClick).toBeTruthy();
  });

  it('should dateConversion', () => {
    let date = new Date();
    let time = '12:00PM';
    component.dateConversion(time, date);
    expect(component.dateConversion).toBeTruthy();
  });

  it('should be Defined onNoClick', async(() => {
    spyOn(component, 'onNoClick').and.callThrough();
    fixture.whenStable().then(() => {
      expect(component.onNoClick).toBeDefined();
      expect(component.onNoClick).toHaveBeenCalledTimes(0);
    });
  }));

  it('should be Defined and Should be Called - changeMinEndDate', async(() => {
    let spy = spyOn(component, 'changeMinEndDate').and.callThrough();
    component.changeMinEndDate();
    fixture.whenStable().then(() => {
    expect(spy).toBeDefined();
    expect(spy).toHaveBeenCalled();
    });
  }));

  it('should be Defined and Should be Called - dateConversion', async(() => {
    let date = new Date();
    let time = '12:00PM';
    let spy = spyOn(component, 'dateConversion').and.callThrough();
    component.dateConversion(time, date);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));
});

