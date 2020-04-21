/** Use Case Linked Issue: TMGP4-248
 *
 *  Test Case Linked Issue: TMGP4-340
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, getTestBed, TestBed} from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../../modules/material.module';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import { StartPopupTaskComponent } from './start-popup-task.component';
import {environment} from '../../../environments/environment';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';

describe('StartPopupTaskComponent', () => {
  let component: StartPopupTaskComponent;
  let fixture: ComponentFixture<StartPopupTaskComponent>;
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
      declarations: [ StartPopupTaskComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StartPopupTaskComponent);
    component = fixture.componentInstance;
    httpMock = getTestBed().get(HttpTestingController);
    fixture.detectChanges();
  });

  // This test verifies that the Component is able to be created.
  it('This test verifies that the Component is able to be created.', () => {
    expect(component).toBeTruthy();
  });

  // This test verifies the call to the service for getCategory within onNoClick - No Error
  it('This test verifies the call to the service getCategory within onNoClick - No Error', () => {
    expect(component.onNoClick).toBeTruthy();
    const categoryMineRequest = httpMock.expectOne(environment.baseUrl+'/category/');
    categoryMineRequest.flush([]);
    httpMock.verify();*/
  });

  // This test verifies the call to the function getCategory within onNoClick - Error
  it('This test verifies the call to the service getCategory within onNoClick - Error ', () => {
    expect(component.onNoClick).toBeTruthy();
    const categoryMineRequest = httpMock.expectOne(environment.baseUrl+'/category/');
    categoryMineRequest.error(new ErrorEvent('Generated testing error'));
    httpMock.verify();
  });

  // This test verifies the function call to the method close within DialogRef
  it('This test verifies the function call to the method close within DialogRef', () => {
    const spy = spyOn(component.dialogRef, 'close').and.callThrough();
    component.onNoClick();
    expect(spy).toBeDefined();
    expect(spy).toHaveBeenCalled();
  });

  // This test verifies onYesClick to be defined and called
  it('This test verifies onYesClick to be defined and called', async(() => {
    const spy = spyOn(component, 'onYesClick').and.callThrough();
    component.onYesClick();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies the onSuggClick can be created and that it calls getCategory
  it('This test verifies the onSuggClick can be created and that it calls getCategory', () => {
    expect(component.onSuggClick).toBeTruthy();

    // set the inputs before the start
    component.data.id = 'TaskNum';
    component.data.name = 'Test';
    const spy = spyOn(component, 'onSuggClick').and.callThrough();
    // run the onSuggClick method
    component.onSuggClick();

    // first check that the isWrongDate flag is set correctly
    expect(spy).toBeDefined();
    expect(spy).toHaveBeenCalled();

  });

  // This test verifies that the function onBackClick contains the expected Title
  it(' This test verifies that the function onBackClick contains the expected Title ' +
      'and is called', async(() => {
    const spy = spyOn(component, 'onBackClick').and.callThrough();
    component.dialogTitle.valueOf();
    component.onBackClick();
    fixture.whenStable().then(() => {
      expect(component.dialogTitle).toContain('Starts at: ');
      expect(spy).toHaveBeenCalled();
    });
  }));
});


