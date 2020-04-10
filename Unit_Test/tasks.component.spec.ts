/** Linked Issue: TMGP4-229: Create Task
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, getTestBed, TestBed} from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../../modules/material.module';
// import {HttpClient, HttpClientModule} from '@angular/common/http';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatDialog} from '@angular/material/dialog';
import {EMPTY} from 'rxjs';
import { TasksComponent } from './tasks.component';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MatMenuModule,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';
import any = jasmine.any;

describe('TasksComponent', () => {
  let component: TasksComponent;
  let fixture: ComponentFixture<TasksComponent>;
  let httpMock: HttpTestingController;
  let num: number;
  let index: number;
  const dialogMock = {
    close: () =>{ }
  };

  beforeEach(async(() => {
    TestBed.resetTestEnvironment();
    TestBed.initTestEnvironment(BrowserDynamicTestingModule,
        platformBrowserDynamicTesting());
    TestBed.configureTestingModule({

      imports: [HttpClientTestingModule, BrowserAnimationsModule,
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
        provide: MatDialogRef, useValue: dialogMock
      },
        {provide: MAT_DIALOG_DATA, useValue: []}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [TasksComponent],
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TasksComponent);
    component = fixture.componentInstance;
    httpMock = getTestBed().get(HttpTestingController);
    fixture.detectChanges();
  });

  it('should create Component', () => {
    expect(component).toBeTruthy();
  });

  it('should openDialog New Test', () => {
    const openDialogSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openDialog();
    expect(openDialogSpy).toHaveBeenCalled();
  });

  it('should openDeleteDialog New Test', () => {
    const openDeleteSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openDeleteDialog(1, 'TestTask');
    expect(openDeleteSpy).toHaveBeenCalled();
  });

  it('should openConfirmDialog New Test', () => {
    const openConfirmSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openConfirmDialog(1, 'TestTrue', true, );
    expect(openConfirmSpy).toHaveBeenCalled();
  });

  it('should openStartDialog New Test', () => {
    const openStartSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openStartDialog(1, 'TestTask');
    expect(openStartSpy).toHaveBeenCalled();
  });

  it('should openStartPopUpDialog New Test', () => {
    const openPopSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openStartPopUpDialog(1, 'TestTask');
    expect(openPopSpy).toHaveBeenCalled();
  });

  it('should be Defined getTasks', async(() => {
    let spy = spyOn(component, 'getTasks').and.callThrough();
    component.getTasks();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should be Defined openDialog', async(() => {
    spyOn(component, 'openDialog').and.callThrough();
    fixture.whenStable().then(() => {
      expect(component.openDialog).toBeDefined();
      expect(component.openDialog).toHaveBeenCalledTimes(0);
    });
  }));
  /*it('should openEditDialog New Test', () => {
    let mockArray = [1,0];
    const openPopSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openEditDialog(1, mockArray[0]);
    expect(openPopSpy).toHaveBeenCalled();
  });*/

  /*it('should be Defined openDialog', async(() => {
    let spy = spyOn(component, 'openDialog').and.callThrough();
    component.openDialog();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));*/
});
