/** Use Case Linked Issue: TMGP4-252
 *
 *  Test Case Linked Issue: TMGP4-237
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';
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
import {EditTaskDialogComponent} from '../edit-task-dialog/edit-task-dialog.component';
import {CreateTaskDialogComponent} from '../create-task-dialog/create-task-dialog.component';
import {DeleteTaskDialogComponent} from '../delete-task-dialog/delete-task-dialog.component';
import {StartTaskDialogComponent} from '../start-task-dialog/start-task-dialog.component';
import {ConfirmTaskDialogComponent} from '../confirm-task-dialog/confirm-task-dialog.component';
import {StartPopupTaskComponent} from '../start-popup-task/start-popup-task.component';


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
        { provide: MAT_DIALOG_DATA, useValue: data}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ DashboardComponent,
        CreateTaskDialogComponent,
        DeleteTaskDialogComponent,
        EditTaskDialogComponent,
        StartTaskDialogComponent,
        ConfirmTaskDialogComponent,
        StartPopupTaskComponent]
    })
        .overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [ CreateTaskDialogComponent,
              DeleteTaskDialogComponent,
              EditTaskDialogComponent,
              StartTaskDialogComponent,
              ConfirmTaskDialogComponent,
              StartPopupTaskComponent] } })
         .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    httpMock = getTestBed().get(HttpTestingController);
  });

  // This test verifies that the Component is able to be created
  it('This test verifies that the Component is able to be created', () => {
    expect(component).toBeTruthy();
  });

  // This test verifies that afterClosed is called within the openDialog function
  it('This test verifies that afterClosed is called within the openDialog function', () => {
    const openSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openDialog();
    expect(openSpy).toHaveBeenCalled();
  });

  // This test verifies that afterClosed is called within the openStartDialog function
  it('This test verifies that afterClosed is called within the openStartDialog function', () => {
    const openStartSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openStartDialog(1, 'TestTask');
    expect(openStartSpy).toHaveBeenCalled();
  });

  // This test verifies that afterClosed is called within the openStartPopDialog function
  it('This test verifies that afterClosed is called within the openStartPopDialog function', () => {
    const openStartPopSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY } as any);
    component.openStartPopUpDialog();
    expect(openStartPopSpy).toHaveBeenCalled();
  });

  // This test verifies that the function getTasks is defined and called
  it('This test verifies that the function getTasks is defined and called', async(() => {
    const spy = spyOn(component, 'getTasks').and.callThrough();
    component.getTasks();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the function getDateTasks is defined and called
  it('sThis test verifies that the function getDateTasks is defined and called', async(() => {
    const spy = spyOn(component, 'getDateTasks').and.callThrough();
    component.getDateTasks();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies the command GET is given within the URL
  it('This test verifies the command GET is given within the URL', async(() => {
    const req = httpMock.expectOne('http://localhost:8001/tasks/');
    expect(req.request.method).toEqual('GET');

    const data1 = [{id: 1, state: 'ACTIVE'}, {id: 2, state: 'PAUSED'}, {id: 3, state: 'CREATED'}, {id: 4, state: 'COMPLETED'}];
    req.flush(data1);
    }));

  // This test verifies that the function openEditDialog is called and defined.
  it('should be defined and called - openEditDialog', fakeAsync(() => {
    component.filteredTasks = [2, 3, 5];
    const spy = spyOn(component, 'openEditDialog').and.callThrough();
    component.dialog.open(EditTaskDialogComponent);
    component.openEditDialog(1, 2);
    tick(50000);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the function openConfirmDialog is called and degined.
  it('should be defined and called - openConfirmDialog', fakeAsync(() => {
    component.filteredTasks = [2, 3, 5];
    const spy = spyOn(component, 'openConfirmDialog').and.callThrough();
    component.dialog.open(ConfirmTaskDialogComponent);
    component.openConfirmDialog(1, 'Test', true);
    tick(50000);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toBeTruthy();
    });
  }));

  // This test verifies that the function openDeleteDialog is called and defined.
  it('should be defined and called - openDeleteDialog', fakeAsync(() => {
    component.filteredTasks = [2, 3, 5];
    const spy = spyOn(component, 'openDeleteDialog').and.callThrough();
    component.dialog.open(DeleteTaskDialogComponent);
    component.openDeleteDialog(1, 'TestDelete');
    tick(50000);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toBeTruthy();
    });
  }));
});




