/** Linked Issue: TMGP4-43: Create Private Task Category
 * & TMGP4-39: Create Public Task Category
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import { MatIconModule} from "@angular/material/icon";
import { TaskCategoryComponent } from './task-category.component';
import {CustomMaterialModule} from "../../modules/material.module";
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from "@angular/router/testing";
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatButtonModule,
  MatRadioModule,
  MatSelectModule
} from '@angular/material';
import {DialogData} from "../tasks/tasks.component";

describe('TaskCategoryComponent', () => {
  let component: TaskCategoryComponent;
  let fixture: ComponentFixture<TaskCategoryComponent>;

  beforeEach(async(() => {
    TestBed.resetTestEnvironment();
    TestBed.initTestEnvironment(BrowserDynamicTestingModule,
        platformBrowserDynamicTesting());
    TestBed.configureTestingModule({
      imports: [ HttpClientModule, BrowserAnimationsModule,
        RouterTestingModule.withRoutes([]),
        MatIconModule,
        MatSelectModule,
        CustomMaterialModule,
        MatFormFieldModule,
        MatInputModule,
        MatDialogModule,
      ],
      providers: [AmplifyService, HttpClient, {
        provide: MatDialogRef,
        useValue: {}},
        { provide: MAT_DIALOG_DATA, useValue: {}}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ TaskCategoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create', () => {
    expect(component.createCategory).toBeTruthy();
  });

  it('should create', () => {
    expect(component.onCancel).toBeTruthy();
  });

  it('should create', () => {
    expect(component.canCreateTask).toBeFalsy();
  });

  it('should be Defined and call - openSnackBar', fakeAsync(() => {
    let message = 'OpenSnackTest';
    let action = 'Start';
    let spy = spyOn(component, 'openSnackBar').and.callThrough();
    component.openSnackBar(message, action);
    tick(10000);
    fixture.whenStable().then(() => {
      expect(component.openSnackBar).toBeDefined();
      expect(component.openSnackBar).toHaveBeenCalled();
    });
  }));
});
