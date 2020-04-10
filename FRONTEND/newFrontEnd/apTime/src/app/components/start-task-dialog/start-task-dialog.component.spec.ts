/** Linked Issue: TMGP4-47: Start Task
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, fakeAsync, TestBed} from '@angular/core/testing';
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
import {NotificationsServiceService} from '../notification';
import {EMPTY} from 'rxjs';
import {NotificationsComponent} from '../notification/notifications/notifications.component';
import any = jasmine.any;

describe('StartTaskDialogComponent', () => {
  let component: StartTaskDialogComponent;
  let fixture: ComponentFixture<StartTaskDialogComponent>;

  const dialogMock = {
    close: () =>{ }
  };

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
    let spy = spyOn(component.dialogRef, 'close').and.callThrough();
    component.onNoClick();
    expect(spy).toHaveBeenCalled();
    // add expects to test that onNoClick does what it is supposed to
  });

  it('should call onYesClick', async(() => {
    let spy = spyOn(component, 'onYesClick').and.callThrough();
    component.onYesClick();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should call onCompleteClick', async(() => {
    let num = 1;
    let spy = spyOn(component, 'onCompleteClick').and.callThrough();
    component.onCompleteClick(num);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should call onSuspendClick', async(() => {
    let num = 1;
    let spy = spyOn(component, 'onSuspendClick').and.callThrough();
    component.onSuspendClick(num);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should call startTask', async(() => {
    let num = 1;
    let spy = spyOn(component, 'startTask').and.callThrough();
    component.startTask(num);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should call suspendTask', async(() => {
    let num = 1;
    let spy = spyOn(component, 'suspendTask').and.callThrough();
    component.suspendTask(num);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should call completeTask', async(() => {
    let num = 1;
    let spy = spyOn(component, 'completeTask').and.callThrough();
    component.completeTask(num);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));
    /*  it('should close dialog when onNoClick called', async(() => {
        spyOn(component, 'onNoClick').and.callThrough();
        fixture.whenStable().then(() => {
          console.log('should close dialog when onNoClick called');
          console.log('component.dialogRef', component.dialogRef);
          component.onNoClick();
          expect(component.onNoClick).toBeDefined();
          expect(component.onNoClick).toHaveBeenCalledTimes(1);
        });
      }));*/

      it('should be Defined onYesClick', fakeAsync(() => {
        spyOn(component, 'onYesClick').and.callThrough();
        fixture.whenStable().then(() => {
          expect(component.onYesClick).toBeDefined();
          expect(component.onYesClick).toHaveBeenCalledTimes(0);
        });
      }));

    });
