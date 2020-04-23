/** Linked Issue: TMGP4-32
 *
 * Test Case Linked Issue: TMGP4-233
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, getTestBed, TestBed} from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../../modules/material.module';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { EditTaskDialogComponent } from './edit-task-dialog.component';

import {DialogData} from '../tasks/tasks.component';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';

describe('EditTaskDialogComponent', () => {
  let component: EditTaskDialogComponent;
  let fixture: ComponentFixture<EditTaskDialogComponent>;
  let httpMock: HttpTestingController;

  const dialogMock = {
    close: () => { }
  };

  const data: DialogData = {
    id : 1,
    task : {
      id: 1,
      categories: [{id: 1, owner: undefined, name: 'name', public: false}],
      description: 'testdesc',
      duration: undefined,
      end: undefined,
      name: 'test',
      scheduledstart: '2020-04-15T13:00:00.000+0000',
      scheduledEnd: '2020-04-15T14:00:00.000+0000',
      start: undefined,
      state: 'CREATED',
      userName: 'team4'
    },
    name: 'test',
    category: undefined,
    description: undefined,
    ssDate: undefined,
    ssTime: undefined,
    seDate: undefined,
    seTime: undefined,
    asDate: undefined,
    asTime: undefined,
    aeDate: undefined,
    aeTime: undefined,
    number: undefined,
    token: undefined,
    isDone: undefined
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
        useValue: dialogMock },
        { provide: MAT_DIALOG_DATA, useValue: data}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ EditTaskDialogComponent ]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditTaskDialogComponent);
    component = fixture.componentInstance;
    httpMock = getTestBed().get(HttpTestingController);
    fixture.detectChanges();
  });
  // This test verified that the Component is able to be created.
  it('should create the Component', () => {
    expect(component).toBeTruthy();
  });

  // This test verifies that the function onNoClick calls the dialogRef function 'close'.
  it('This test verifies that the function onNoClick calls the dialogRef function close', async(() => {
    const spy = spyOn(component.dialogRef, 'close').and.callThrough();
    component.onNoClick();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the the function onYesClick and the function WrongDate is called when an error is present.
  it('should call onYesClick and the function WrongDate is called when an error is present ' +
      'and that it does not send any information to the URL', () => {
    expect(component.onYesClick).toBeTruthy();

    // set the dates so that the scheduled end date is before the start
    component.task = component.data.task;
    component.task.ssDate = new Date();
    component.task.ssTime = '01:00 PM';
    component.task.seDate = new Date();
    component.task.seDate.setDate(component.task.ssDate.getDate() + 5);

    component.task.seTime = '01:30 PM';

    // run the onYesClick method
    component.onYesClick();

    // first check that the isWrongDate flag is set correctly
    expect(component.isWrongDate).toBeFalsy();

    // second check that the httpClient did post, to create new task
    httpMock.expectOne('http://localhost:8001/tasks/task');

  });

  // This test verifies when onYesClick is creates a new task (with wrong date)
  it('This test verifies when onYesClick is creates a new task (with wrong date)', () => {
    expect(component.onYesClick).toBeTruthy();

    // set the dates so that the scheduled end date is before the start
    component.task = component.data.task;
    component.task.ssDate = new Date();
    component.task.ssTime = '01:00 PM';
    component.task.seDate = new Date();

    component.task.seTime = '12:00 AM';

    // run the onYesClick method
    component.onYesClick();

    // first check that the isWrongDate flag is set correctly
    expect(component.isWrongDate).toBeTruthy();

  });

  // This test verifies that the the function onYesClick has been defined and been called.
  it('should onSuggClick is called and that the function WrongDate is called when an error is present, ' +
      'and that it does not send any information to the URL', () => {
    expect(component.onSuggClick).toBeTruthy();

    // set the dates so that the scheduled end date is before the start
    component.task = component.data.task;
    component.task.ssDate = new Date();
    component.task.ssTime = '01:00 PM';
    component.task.seDate = new Date( );
    component.task.seDate.setDate(component.task.ssDate.getDate() + 5);

    component.task.seTime = '01:30 PM';

    // run the onYesClick method
    component.onSuggClick();

    // first check that the isWrongDate flag is set correctly
    expect(component.isWrongDate).toBeFalsy();
  });

  // // should onSuggClick create new task (with wrong date)
  it('should onSuggClick create new task (with wrong date)', () => {
    expect(component.onSuggClick).toBeTruthy();

    // set the dates so that the scheduled end date is before the start
    component.task = component.data.task;
    component.task.ssDate = new Date();
    component.task.ssTime = '01:00 PM';
    component.task.seDate = new Date();

    component.task.seTime = '11:00 AM';

    // run the onYesClick method
    component.onSuggClick();

    // first check that the isWrongDate flag is set correctly
    expect(component.isWrongDate).toBeTruthy();

  });

  // should call onBackClick
  it('should call onBackClick', () => {
    expect(component.onBackClick).toBeTruthy();

    // run the onBackClick method
    component.onBackClick();

  });

  // This test verifies that the the function dateConversion has been defined and been called.
  it('should test that the function dateConversion is able to be defined and called', () => {
    const date = new Date();
    const time = '12:00PM';
    const spy = spyOn(component, 'dateConversion').and.callThrough();
    component.dateConversion(time, date);
    expect(spy).toBeDefined();
    expect(spy).toHaveBeenCalled();
  });

  // This test verifies that the the function changeMinEndDate has been defined and been called.
  it('should be Defined and Should be Called - changeMinEndDate', async(() => {
    const spy = spyOn(component, 'changeMinEndDate').and.callThrough();
    component.changeMinEndDate();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the the function getCategory has been defined and been called.
  it('This test verifies that the the function getCategory has been defined and been called', async(() => {
    const spy = spyOn(component, 'getCategory').and.callThrough();
    component.getCategory();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the function onAcceptClick has been defined and been called.
  it('This test verifies that the the function onAcceptClick has been defined and been called',
      async(() => {
    const spy = spyOn(component, 'onAcceptClick').and.callThrough();
    component.scheduledEnd = new Date();
    component.scheduledStart = new Date();
    component.suggestedDuration = 30;
    component.onAcceptClick();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));
});
