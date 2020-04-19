/** Use Case Linked Issue: TMGP4-29: Create Task
 *
 *  Test Case Linked Issue: TMGP4-77
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
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';


describe('CreateTaskDialogComponent', () => {
  let component: CreateTaskDialogComponent;
  let fixture: ComponentFixture<CreateTaskDialogComponent>;
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
        useValue: dialogMock, },
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

  // This test verifies that the component is able to be created.
  it('should create the Component', () => {
    expect(component).toBeTruthy();
  });

  // This test verifies that the URL is able to be called and calls the function onNoClick when no error exist.
  it('should verify that the URL is able to be called - onNoClick Function', () => {
    expect(component.onNoClick).toBeTruthy();
    const categoryMineRequest = httpMock.expectOne(environment.baseUrl+'/category/');
    categoryMineRequest.flush([]);
    httpMock.verify();
  });

  // This test verified that the URL is able to be called and calls the function onNoClick when an error exist.
  it('should have error for failed category requests - onNoClick Function', () => {
    expect(component.onNoClick).toBeTruthy();
    const categoryMineRequest = httpMock.expectOne(environment.baseUrl+'/category/');
    categoryMineRequest.error(new ErrorEvent('Generated testing error'));
    httpMock.verify();
  });

  // This test verifies that the function onNoClick calls the dialogRef function 'close'.
  it('Test Function onNoClick - Method DialogRef - Close function is Called', () => {
    const spy = spyOn(component.dialogRef, 'close').and.callThrough();
    component.onNoClick();
    expect(spy).toBeDefined();
    expect(spy).toHaveBeenCalled();
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

  // This test verifies that the the function onNoClick has been defined and been called.
  it('should test that the function onNoClick is able to be defined and called', async(() => {
    const spy = spyOn(component, 'onNoClick').and.callThrough();
    component.onNoClick();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the the function changeMinEndDate has been defined and been called.
  it('should be Defined and Should be Called - changeMinEndDate', async(() => {
    const spy = spyOn(component, 'changeMinEndDate').and.callThrough();
    component.changeMinEndDate();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

 // This test verifies that the function onBackClick contains the expected Title
  it(' This test verifies that the function onBackClick contains the expected Title ' +
      'and is called', async(() => {
    const spy = spyOn(component, 'onBackClick').and.callThrough();
    component.dialogTitle.valueOf();
    component.onBackClick();
    fixture.whenStable().then(() => {
      expect(component.dialogTitle).toContain('New Task');
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the the function onSuggClick has been defined and been called.
  it('should onSuggClick is called and that the function WrongDate is called when an error is present, ' +
      'and that it does not send any information to the URL', () => {
    expect(component.onSuggClick).toBeTruthy();

    // set the dates so that the scheduled end date is before the start
    component.data.ssDate = new Date();
    component.data.ssTime = '01:00 PM';
    component.data.seDate = new Date( );
    component.data.seDate.setDate(component.data.ssDate.getDate() + 5);

    component.data.seTime = '01:30 PM';

    // run the onSuggClick method
    component.onSuggClick();

    // first check that the isWrongDate flag is set correctly
    expect(component.isWrongDate).toBeFalsy();

    // second check that the httpClient did post, to create new task
    const categoryMineRequest = httpMock.expectOne('http://localhost:8001/tasks/predict');

  });

  // should onSuggClick create new task (with wrong date)
  it('should onSuggClick create new task (with wrong date)', () => {
    expect(component.onSuggClick).toBeTruthy();

    // set the dates so that the scheduled end date is before the start
    component.data.ssDate = new Date();
    component.data.ssTime = '01:00 PM';
    component.data.seDate = new Date();

    component.data.seTime = '11:00 AM';

    // run the onSuggClick method
    component.onSuggClick();

    // first check that the isWrongDate flag is set correctly
    expect(component.isWrongDate).toBeTruthy();

  });

  // This test verifies that the the function onYesClick has been defined and been called.
  it('should onYesClick is called and that the function WrongDate is called when an error is present, ' +
      'and that it does not send any information to the URL', () => {
    expect(component.onYesClick).toBeTruthy();

    // set the dates so that the scheduled end date is before the start
    component.data.ssDate = new Date();
    component.data.ssTime = '01:00 PM';
    component.data.seDate = new Date( );
    component.data.seDate.setDate(component.data.ssDate.getDate() + 5);

    component.data.seTime = '01:30 PM';

    // run the onYesClick method
    component.onYesClick();

    // first check that the isWrongDate flag is set correctly
    expect(component.isWrongDate).toBeFalsy();

    // second check that the httpClient did post, to create new task
    const categoryMineRequest = httpMock.expectOne('http://localhost:8001/tasks/newtask');

  });

  // // should onSuggClick create new task (with wrong date)
  it('should onYesClick create new task (with wrong date)', () => {
    expect(component.onYesClick).toBeTruthy();

    // set the dates so that the scheduled end date is before the start
    component.data.ssDate = new Date();
    component.data.ssTime = '01:00 PM';
    component.data.seDate = new Date();

    component.data.seTime = '11:00 AM';

    // run the onYesClick method
    component.onYesClick();

    // first check that the isWrongDate flag is set correctly
    expect(component.isWrongDate).toBeTruthy();

  });
});
