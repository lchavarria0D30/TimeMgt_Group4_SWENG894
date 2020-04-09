/** Linked Issue: TMGP4-229: Create Task
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../../modules/material.module';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TasksComponent } from './tasks.component';
import {MatDialog} from '@angular/material/dialog';

import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MatMenuModule,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';
import {EMPTY} from 'rxjs';
import {ConstructorDepErrorKind} from "@angular/compiler-cli/src/ngtsc/annotations/src/util";



describe('TasksComponent', () => {
  let component: TasksComponent;
  let fixture: ComponentFixture<TasksComponent>;


  beforeEach(async(() => {
    TestBed.resetTestEnvironment();
    TestBed.initTestEnvironment(BrowserDynamicTestingModule,
        platformBrowserDynamicTesting());
    TestBed.configureTestingModule({

      imports: [HttpClientModule, BrowserAnimationsModule,
        RouterTestingModule.withRoutes([]),
        MatIconModule,
        MatSelectModule,
        CustomMaterialModule,
        MatFormFieldModule,
        MatInputModule,
        MatDialogModule,
        MatMenuModule
      ],
      providers: [AmplifyService, HttpClient, {
        provide: MatDialogRef, MatDialog, useValue: {}
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
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should openDialog New Test', () => {
    expect(component.openDialog).toBeTruthy();
  });

  it('should openDialog New Test', () => {
    const openDialogSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openDialog();
    expect(openDialogSpy).toHaveBeenCalled();
  });

  it('should openDeleteDialog New Test', () => {
    const openDialogSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openDeleteDialog(1, 'TestTask');
    expect(openDialogSpy).toHaveBeenCalled();
  });

  it('should openConfirmDialog New Test', () => {
    const openDialogSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openConfirmDialog(1, 'TestTrue', true, );
    expect(openDialogSpy).toHaveBeenCalled();
  });

  it('should openStartDialog New Test', () => {
    const openDialogSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openStartDialog(1, 'TestTask');
    expect(openDialogSpy).toHaveBeenCalled();
  });

  it('should openStartPopUpDialog New Test', () => {
    const openDialogSpy = spyOn(component.dialog, 'open')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    component.openStartPopUpDialog(1, 'TestTask');
    expect(openDialogSpy).toHaveBeenCalled();
  });

  it('should be Defined openDialog', async(() => {
    spyOn(component, 'openDialog').and.callThrough();
    fixture.whenStable().then(() => {
      expect(component.openDialog).toBeDefined();
      expect(component.openDialog).toHaveBeenCalledTimes(0);
    });
  }));


});
