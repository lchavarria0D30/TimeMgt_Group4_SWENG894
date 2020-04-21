/** Linked Issue: TMGP4-47
 *
 * Test Case Linked Issue: TMGP4-288
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../../modules/material.module';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA, Input} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { StartTaskDialogComponent } from './start-task-dialog.component';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatButtonModule,
  MatRadioModule,
  MatSelectModule
} from '@angular/material';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {NotificationsServiceService} from '../notification';
import {EMPTY} from 'rxjs';
import {NotificationsComponent} from '../notification/notifications/notifications.component';
import any = jasmine.any;

describe('StartTaskDialogComponent', () => {
  let component: StartTaskDialogComponent;
  let fixture: ComponentFixture<StartTaskDialogComponent>;
  let httpMock: HttpTestingController;

  const dialogMock = {
    close: () => { }
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
        useValue: dialogMock},
        { provide: MAT_DIALOG_DATA, useValue: {}}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ StartTaskDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StartTaskDialogComponent);
    component = fixture.componentInstance;
    httpMock = getTestBed().get(HttpTestingController);
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should define and call -  onNoClick', async(() => {
    spyOn(component, 'onNoClick').and.callThrough();
    fixture.whenStable().then(() => {
      expect(component.onNoClick).toBeDefined();
      expect(component.onNoClick).toHaveBeenCalledTimes(0);
    });
  }));

  it('Test onNoClick DialogRef - Close', () => {
    const spy = spyOn(component.dialogRef, 'close').and.callThrough();
    component.onNoClick();
    expect(spy).toHaveBeenCalled();
    // add expects to test that onNoClick does what it is supposed to
  });

  it('should call onYesClick', async(() => {
    const spy = spyOn(component, 'onYesClick').and.callThrough();
    component.onYesClick();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should call onCompleteClick', async(() => {
    const num = 1;
    const spy = spyOn(component, 'onCompleteClick').and.callThrough();
    component.onCompleteClick(num);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should call onSuspendClick', fakeAsync(() => {
    const num = 1;
    const spy = spyOn(component, 'onSuspendClick').and.callThrough();
    component.onSuspendClick(num);
    tick(20000);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should call startTask', async(() => {
    const num = 1;
    const spy = spyOn(component, 'startTask').and.callThrough();
    component.startTask(num);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should call suspendTask', async(() => {
    const num = 1;
    const spy = spyOn(component, 'suspendTask').and.callThrough();
    component.suspendTask(num);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should call completeTask', async(() => {
    const num = 1;
    const spy = spyOn(component, 'completeTask').and.callThrough();
    component.completeTask(num);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));


  it('should be Defined onYesClick', fakeAsync(() => {
    const spy = spyOn(component, 'onYesClick').and.callThrough();
    component.onYesClick();
    tick(20000);
    fixture.whenStable().then(() => {
    expect(spy).toBeDefined();
    expect(spy).toHaveBeenCalled();
    });
  }));
});
