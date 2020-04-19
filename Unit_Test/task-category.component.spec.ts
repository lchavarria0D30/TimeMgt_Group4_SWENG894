/** Linked Issue: TMGP4-43 & TMGP4-39
 *
 *  Test Case Linked Issue: TMGP4-109
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import { TaskCategoryComponent } from './task-category.component';
import {CustomMaterialModule} from '../../modules/material.module';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
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
  MatSelectModule
} from '@angular/material';
import {FormControl} from '@angular/forms';


describe('TaskCategoryComponent', () => {
  let component: TaskCategoryComponent;
  let fixture: ComponentFixture<TaskCategoryComponent>;
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
        useValue: dialogMock },
        { provide: MAT_DIALOG_DATA, useValue: {}}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ TaskCategoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskCategoryComponent);
    component = fixture.componentInstance;
    httpMock = getTestBed().get(HttpTestingController);
    fixture.detectChanges();
  });

  // This test verifies that the component is able to be created.
  it('This test verifies that the component is able to be created', () => {
    expect(component).toBeTruthy();
  });

  // This test verifies onCancel is defined and called
  it('This test verifies onCancel is defined and called', fakeAsync(() => {
    const spy = spyOn(component, 'onCancel').and.callThrough();
    component.onCancel();
    tick(10000);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies createCategory is defined and called
  it('This test verifies createCategory is defined and called', fakeAsync(() => {
    const spy = spyOn(component, 'createCategory').and.callThrough();
    component.createCategory(1);
    tick(10000);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies createCat is defined and called
  it('This test verifies createCat is defined and called', fakeAsync(() => {
    const spy = spyOn(component, 'createCat').and.callThrough();
    const isPublic = new FormControl();
    component.isPublic.valueOf();
    tick(50000);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies onStats is defined and called
  it('This test verifies onStats is defined and called', fakeAsync(() => {
    const spy = spyOn(component, 'onStats').and.callThrough();
    component.onStats();
    tick(10000);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  it('should be Defined and call - openSnackBar', fakeAsync(() => {
    const message = 'OpenSnackTest';
    const action = 'Start';
    const spy = spyOn(component, 'openSnackBar').and.callThrough();
    component.openSnackBar(message, action);
    tick(10000);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies findCat is defined and called
  it('This test verifies findCat is defined and called', fakeAsync(() => {
    const num = 1;
    const spy = spyOn(component, 'findCat').and.callThrough();
    component.findCat(num);
    tick(10000);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

});
