/** Linked Issue: TMGP4-47: Start Task
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import {async, ComponentFixture, fakeAsync, TestBed} from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../../modules/material.module';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA, Input} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { StartTaskDialogComponent } from './start-task-dialog.component';
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
import {NotificationsServiceService} from '../notification';
import {EMPTY} from 'rxjs';
import {NotificationsComponent} from '../notification/notifications/notifications.component';
import any = jasmine.any;

class MockStartComponent {
  public dialogRef = any;

  onNoClick(): void {
    this.dialogRef.call(null);
  }
}

describe('StartTaskDialogComponent', () => {
  let component: StartTaskDialogComponent;
  let fixture: ComponentFixture<StartTaskDialogComponent>;
  let start: MockStartComponent;


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
      declarations: [ StartTaskDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StartTaskDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be Defined onNoClick', async(() => {
    spyOn(component, 'onNoClick').and.callThrough();
    fixture.whenStable().then(() => {
      expect(component.onNoClick).toBeDefined();
      expect(component.onNoClick).toHaveBeenCalledTimes(0);
    });
  }));

  it('should be Defined onYesClick', fakeAsync(() => {
    spyOn(component, 'onYesClick').and.callThrough();
    fixture.whenStable().then(() => {
      expect(component.onYesClick).toBeDefined();
      expect(component.onYesClick).toHaveBeenCalledTimes(0);
    });
  }));

  it('Test onYesClick', () => {
    const mockStart = new MockStartComponent();
    const spy = spyOnAllFunctions(component.dialogRef).and.returnValue(null);
    expect(spy).toBeTruthy();
  });
});
