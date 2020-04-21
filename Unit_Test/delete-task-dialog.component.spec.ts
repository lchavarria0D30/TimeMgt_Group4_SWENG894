/** Linked Issue: TMGP4-30
 *
 *  Test Case Linked Issue: TMGP4-235
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, getTestBed, TestBed} from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../../modules/material.module';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DeleteTaskDialogComponent } from './delete-task-dialog.component';
import {By} from '@angular/platform-browser';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';


describe('DeleteTaskDialogComponent', () => {
  let component: DeleteTaskDialogComponent;
  let fixture: ComponentFixture<DeleteTaskDialogComponent>;
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
      declarations: [ DeleteTaskDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteTaskDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    httpMock = getTestBed().get(HttpTestingController);
  });

  // This test verifies that the Component is created
  it('should create the Component', () => {
    expect(component).toBeTruthy();
  });

  // This verifies that the function onNoClick is created
  it('should verify onNoClick', () => {
    expect(component.onNoClick).toBeTruthy();
  });

  // This verifies that the function onYesClick is created
  it('should verify onYesClick', () => {
    expect(component.onYesClick).toBeTruthy();
  });

  // This verifies that the when the no click buttong is clicked the title for it is called
  it('should set on No Click', () => {
    // tslint:disable-next-line:no-shadowed-variable
    const fixture = TestBed.createComponent(DeleteTaskDialogComponent);
    // const alert = 'Cancel Message';
    fixture.detectChanges();

    const h1 = fixture.debugElement.query(By.css('button'));
    h1.triggerEventHandler('click', {});
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('button')).nativeElement.innerText).toEqual('No');
  });

  // This test verifies that the onYesClick function is defined and called
  it('This test verifies that the onYesClick function is defined and called', async(() => {
    const spy = spyOn(component.dialogRef, 'close').and.callThrough();
    component.onYesClick();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));
});
