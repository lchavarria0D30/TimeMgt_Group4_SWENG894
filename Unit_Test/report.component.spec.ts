/** Use Case Linked Issue: TMGP4-26: Report on Tasks Completed
 *
 *  Test Case Linked Issue: TMGP4-243, TMGP4-245
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
import {CUSTOM_ELEMENTS_SCHEMA, } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReportComponent } from './report.component';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';



describe('ReportComponent', () => {
  let component: ReportComponent;
  let fixture: ComponentFixture<ReportComponent>;
  let httpMock: HttpTestingController;

  let matDate: MatDatepickerInputEvent<Date>;


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
        MatNativeDateModule
      ],
      providers: [AmplifyService,
        {
        provide: MatDialogRef,
        useValue: dialogMock
        },
        { provide: MAT_DIALOG_DATA, useValue: {}
        },
        {
          provide: MatDatepickerInputEvent,
          useValue: matDate,
        }],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ ReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportComponent);
    component = fixture.componentInstance;
    httpMock = getTestBed().get(HttpTestingController);
    matDate = getTestBed().get(MatDatepickerInputEvent);
    fixture.detectChanges();
  });

  // This test verifies that the Component is able to be created.
  it('This test verifies that the Component is able to be created.', () => {
    expect(component).toBeTruthy();
  });

  // This test verifies that the the function getReport has been defined and been called.
  it('should test that the function getReport is able to be defined and called', async(() => {
    const start = new Date();
    const end = new Date();
    const spy = spyOn(component, 'getReport').and.callThrough();
    component.getReport(start, end);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the the function addStart has been defined and been called.
  it('should test that the function addStart is able to be defined and called', async(() => {
    const taskName = 'TestString';
    // const start = new Date();
    const spy = spyOn(component, 'addStart').and.callThrough();
    // component.start = new Date();
    const mat2Date: any = { value : new Date() };

    component.addStart(taskName, mat2Date);

    component.start.getDate();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the the function addEnd has been defined and been called.
  it('should test that the function addEnd is able to be defined and called', async(() => {
    const taskName = 'TestString';
    // const start = new Date();
    const spy = spyOn(component, 'addEnd').and.callThrough();
    component.start = new Date();
    const endDate = new Date();
    endDate.setDate(component.start.getDate() + 5);
    const mat2Date: any = { value : endDate };

    component.addEnd(taskName, mat2Date);

    component.start.getDate();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));
});
