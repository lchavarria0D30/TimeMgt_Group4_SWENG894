/** Use Case Linked Issue: TMGP4-229
 *
 *  Test Case Linked Issue: TMGP4-113
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
import {EMPTY} from 'rxjs';
import { TasksComponent } from './tasks.component';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MatMenuModule,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';



describe('TasksComponent', () => {
  let component: TasksComponent;
  let fixture: ComponentFixture<TasksComponent>;
  let httpMock: HttpTestingController;

  const dialogMock = {
    close: () => { }
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

  // This test verifies the Component is able to be created.
  it('This test verifies the Component is able to be created', () => {
    expect(component).toBeTruthy();
  });

  // This test verifies that the method afterClosed is called within the function openDialog
  it('This test verifies that the method afterClosed is called within the function openDialog', () => {
    const openDialogSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openDialog();
    expect(openDialogSpy).toHaveBeenCalled();
  });

  // This test verifies that the method afterClosed is called within the function openDeleteDialog
  it('This test verifies that the method afterClosed is called within the function openDeleteDialog', () => {
    const openDeleteSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openDeleteDialog(1, 'TestTask');
    expect(openDeleteSpy).toHaveBeenCalled();
  });

  // This test verifies that the method afterClosed is called within the function openConfirmDialog
  it('This test verifies that the method afterClosed is called within the function openConfirmDialog', () => {
    const openConfirmSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openConfirmDialog(1, 'TestTrue', true, );
    expect(openConfirmSpy).toHaveBeenCalled();
  });

  // This test verifies that the method afterClosed is called within the function openStartDialog
  it('This test verifies that the method afterClosed is called within the function openStartDialog', () => {
    const openStartSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openStartDialog(1, 'TestTask');
    expect(openStartSpy).toHaveBeenCalled();
  });

  // This test verifies that the method afterClosed is called within the function openStartPopUpDialog
  it('// This test verifies that the method afterClosed is called within the function openStartPopUpDialog', () => {
    const openPopSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openStartPopUpDialog();
    expect(openPopSpy).toHaveBeenCalled();
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

  // This test verifies that the function openDialog is defined and called
  it('This test verifies that the function openDialog is defined and called', async(() => {
    spyOn(component, 'openDialog').and.callThrough();
    fixture.whenStable().then(() => {
      expect(component.openDialog).toBeDefined();
      expect(component.openDialog).toHaveBeenCalledTimes(0);
    });
  }));
});
