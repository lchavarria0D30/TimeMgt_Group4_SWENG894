import {async, ComponentFixture, getTestBed, TestBed} from '@angular/core/testing';
import { MatDatepicker } from '@angular/material/datepicker';
import { MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../../modules/material.module';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatButtonModule,
  MatRadioModule,
  MatMenuModule,
  MatSelectModule
} from '@angular/material';
import { DashboardComponent } from './dashboard.component';
import {FormControl} from '@angular/forms';
import {EMPTY} from 'rxjs';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let httpMock: HttpTestingController;

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
        MatMenuModule
      ],
      providers: [AmplifyService, {
        provide: MatDialogRef,
        useValue: {}},
        { provide: MAT_DIALOG_DATA, useValue: {}}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ DashboardComponent ]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    httpMock = getTestBed().get(HttpTestingController);
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should call afterclosed for openDialog', () => {
    const openSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openDialog();
    expect(openSpy).toHaveBeenCalled();
  });

  it('should call afterclosed for openStartDialog', () => {
    const openStartSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openStartDialog(1, 'TestTask');
    expect(openStartSpy).toHaveBeenCalled();
  });

  it('should call afterclosed for openStartPopUpDialog', () => {
    const openStartPopSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openStartPopUpDialog(1, 'TestTask');
    expect(openStartPopSpy).toHaveBeenCalled();
  });

  it('should be defined and called - getTasks', async(() => {
    let spy = spyOn(component, 'getTasks').and.callThrough();
    component.getTasks();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should be defined and called - getDateTasks', async(() => {
    let spy = spyOn(component, 'getDateTasks').and.callThrough();
    component.getDateTasks();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));
});


/*
it('should be defined and called - openStartDialog', async(() => {    let spy = spyOn(component, 'getDateTasks').and.callThrough();
  let num = 1;
  let name = "Test Task";
  component.openStartDialog(num, name);
  fixture.whenStable().then(() => {
    expect(spy).toBeDefined();
    expect(spy).toHaveBeenCalled();
  });
}));*/
