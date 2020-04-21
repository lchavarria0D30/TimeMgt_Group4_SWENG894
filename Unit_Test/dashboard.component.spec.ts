/** Use Case Linked Issue: TMGP4-252
 *
 *  Test Case Linked Issue: TMGP4-237
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, getTestBed, TestBed} from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../../modules/material.module';
import { DashboardComponent } from './dashboard.component';
import {EMPTY} from 'rxjs';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {AmplifyService} from 'aws-amplify-angular';
import {BrowserDynamicTestingModule, platformBrowserDynamicTesting} from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {DialogData} from '../tasks/tasks.component';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatMenuModule,
  MatSelectModule
} from '@angular/material';



describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
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
        MatMenuModule
      ],
      providers: [AmplifyService, {
        provide: MatDialogRef,
        useValue: dialogMock, },
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
    component.openStartPopUpDialog();
    expect(openStartPopSpy).toHaveBeenCalled();
  });

  it('should be defined and called - getTasks', async(() => {
    const spy = spyOn(component, 'getTasks').and.callThrough();
    component.getTasks();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should be defined and called - getDateTasks', async(() => {
    const spy = spyOn(component, 'getDateTasks').and.callThrough();
    component.getDateTasks();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));


  /*it('should verify that the function getTasks is called within openDialog', async(() => {
    const num = 1;
    const name = 'Test Task';
    const bool = true;
    const spy = spyOn(component.dialog, 'open').and.callThrough();
    component.openConfirmDialog(num, name, bool);
    fixture.whenStable().then(() => {
      expect(spy).toBeTruthy();
      // expect(spy).toHaveBeenCalled();
    });
  }));*/
});



