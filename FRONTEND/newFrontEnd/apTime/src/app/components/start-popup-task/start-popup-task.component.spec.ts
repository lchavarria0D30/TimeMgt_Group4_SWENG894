import {async, ComponentFixture, fakeAsync, getTestBed, TestBed} from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../../modules/material.module';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import { CreateTaskDialogComponent } from '../create-task-dialog/create-task-dialog.component';
import { DeleteTaskDialogComponent } from '../delete-task-dialog/delete-task-dialog.component';
import { EditTaskDialogComponent } from '../edit-task-dialog/edit-task-dialog.component';
import { StartTaskDialogComponent } from '../start-task-dialog/start-task-dialog.component';
import { ConfirmTaskDialogComponent } from '../confirm-task-dialog/confirm-task-dialog.component';
import { StartPopupTaskComponent } from './start-popup-task.component';
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
    close: () =>{ }
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

  it('should create Component', () => {
    expect(component).toBeTruthy();
  });


  it('should call the service for getCategory - No Error', () => {
    expect(component.onNoClick).toBeTruthy();
    const categoryMineRequest = httpMock.expectOne('http://localhost:8001/category/');
    categoryMineRequest.flush([]);
   /* const categoryPublicRequest = httpMock.expectOne('http://localhost:8001/category/public');
    categoryPublicRequest.flush([]);
    httpMock.verify();*/
  });

  it('should call the service for getCategory - Error ', () => {
    expect(component.onNoClick).toBeTruthy();
    const categoryMineRequest = httpMock.expectOne('http://localhost:8001/category/');
    categoryMineRequest.error(new ErrorEvent('Generated testing error'));
/*    const categoryPublicRequest = httpMock.expectOne('http://localhost:8001/category/public');
    categoryPublicRequest.flush([]);*/
    httpMock.verify();
  });

  it('Test onNoClick DialogRef - Close', () => {
    let spy = spyOn(component.dialogRef, 'close').and.callThrough();
    component.onNoClick();
    expect(spy).toHaveBeenCalled();
    // add expects to test that onNoClick does what it is supposed to
  });

  it('should call onYesClick', async(() => {
    let spy = spyOn(component, 'onYesClick').and.callThrough();
    component.onYesClick();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));
});



// Future Unit Test Implementation
/*it('should call onYesClick: DialogRef - Close', () => {
    let spy = spyOn(component.dialogRef, 'close').and.returnValue( );
    component.onYesClick();
    expect(spy).toHaveBeenCalled();
    // add expects to test that onNoClick does what it is supposed to
  });*/


