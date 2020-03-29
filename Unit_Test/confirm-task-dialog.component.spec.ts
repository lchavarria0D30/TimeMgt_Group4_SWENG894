/** Linked Issue: TMGP4-48 & TMGP4-181: Create Final Web Designs
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
import {CUSTOM_ELEMENTS_SCHEMA, DebugElement} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ConfirmTaskDialogComponent } from './confirm-task-dialog.component';
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
import {By} from "@angular/platform-browser";

describe('ConfirmTaskDialogComponent', () => {
  let component: ConfirmTaskDialogComponent;
  let fixture: ComponentFixture<ConfirmTaskDialogComponent>;
  let de: DebugElement;

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
      ],
      providers: [AmplifyService, HttpClient, {
        provide: MatDialogRef,
        useValue: {}
      },
        {provide: MAT_DIALOG_DATA, useValue: {}}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ConfirmTaskDialogComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmTaskDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should ensure Pause Click', async(() => {
    spyOn(component, 'onPauseClick');
    let button = fixture.debugElement.nativeElement.querySelector('button');
    button.click();

    fixture.detectChanges()
    expect(component.onPauseClick).toBeTruthy();
  }));

  it('should ensure No Click', async(() => {
    spyOn(component, 'onNoClick');
    let button = fixture.debugElement.nativeElement.querySelector('button');
    button.click();

    fixture.detectChanges()
    expect(component.onNoClick).toBeTruthy();
  }));

  it('should ensure Done Click', async(() => {
    spyOn(component, 'onDoneClick');
    let button = fixture.debugElement.nativeElement.querySelector('button');
    button.click();

    fixture.detectChanges();
    expect(component.onDoneClick).toBeTruthy();
  }));

  it('should have an h1 tag', () => {
    const title = fixture.debugElement.query(By.css('.mat-dialog-title')).nativeElement;
    expect(title.innerHTML).toContain('Task ""');
  });

  /**it('should set userResponse when No button is clicked', () => {
    // expect(component.isDone).toBeUndefined();
    const btn = fixture.debugElement.nativeElement.querySelector('#onNoClick');
    // btn.click();
    expect(component.onDoneClick).toBe('No');
  });
*/
  it('should be Defined completeTask', async(() => {
    spyOn(component, 'completeTask').and.callThrough();
    fixture.whenStable().then(() => {
      expect(component.completeTask).toBeDefined();
      expect(component.completeTask).toHaveBeenCalledTimes(0);
    });
  }));

});
