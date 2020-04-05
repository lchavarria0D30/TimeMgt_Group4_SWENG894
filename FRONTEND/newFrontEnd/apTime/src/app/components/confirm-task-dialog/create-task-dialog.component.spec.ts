/** Linked Issue: TMGP4-29: Create Task
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import { CreateTaskDialogComponent } from './create-task-dialog.component';
import {CustomMaterialModule} from '../../modules/material.module';
import {HttpClient, HttpClientModule} from '@angular/common/http';
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
  MatButtonModule,
  MatRadioModule,
  MatSelectModule
} from '@angular/material';
import {DialogData} from '../tasks/tasks.component';
import {ConfirmTaskDialogComponent} from '../confirm-task-dialog/confirm-task-dialog.component';
import {SessionService} from '../../services/session.service';

describe('CreateTaskDialogComponent', () => {
  let component: CreateTaskDialogComponent;
  let fixture: ComponentFixture<CreateTaskDialogComponent>;

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
      providers: [AmplifyService, HttpClient,  {
        provide: MatDialogRef,
        useValue: {}},
        { provide: MAT_DIALOG_DATA, useValue: {}}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ CreateTaskDialogComponent, ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateTaskDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should onNoclick', () => {
    expect(component.onNoClick).toBeTruthy();
  });

  it('should onYesClick', () => {
    expect(component.onYesClick).toBeTruthy();
  });

  it('should dateConversion', () => {
    expect(component.dateConversion).toBeTruthy();
  });

  /**
  it('testing pauseTask(number) ', () => {
    // const testCreate = fixture.debugElement.injector.get(ConfirmTaskDialogComponent);
    fixture.detectChanges();
    expect(component.changeMinEndDate).toHaveBeenCalled();
  });
*/
  it('should be Defined onNoClick', async(() => {
    spyOn(component, 'onNoClick').and.callThrough();
    fixture.whenStable().then(() => {
      expect(component.onNoClick).toBeDefined();
      expect(component.onNoClick).toHaveBeenCalledTimes(0);
    });
  }));

  it('should be Defined onYesClick', async(() => {
    spyOn(component, 'onYesClick').and.callThrough();
    fixture.whenStable().then(() => {
      expect(component.onYesClick).toBeDefined();
      expect(component.onYesClick).toHaveBeenCalledTimes(0);
    });
  }));

  it('should be Defined changeMinEndDate', async(() => {
    spyOn(component, 'changeMinEndDate').and.callThrough();
    fixture.whenStable().then(() => {
      expect(component.changeMinEndDate).toBeDefined();
      expect(component.changeMinEndDate).toHaveBeenCalled();
    });
  }));
});




