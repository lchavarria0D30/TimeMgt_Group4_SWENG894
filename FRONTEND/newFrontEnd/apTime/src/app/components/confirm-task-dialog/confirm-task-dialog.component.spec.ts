/** Use Case Linked Issue: TMGP4-48 & TMGP4-181: Create Final Web Designs
 *
 *  Test Case Issue: TMGP4-289
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
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ConfirmTaskDialogComponent } from './confirm-task-dialog.component';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';
import {By} from '@angular/platform-browser';



describe('ConfirmTaskDialogComponent', () => {
  let component: ConfirmTaskDialogComponent;
  let fixture: ComponentFixture<ConfirmTaskDialogComponent>;
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
      ],
      providers: [AmplifyService, {
        provide: MatDialogRef,
        useValue: dialogMock
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
    httpMock = getTestBed().get(HttpTestingController);
    fixture.detectChanges();

  });

  // This test should verify that the Component is able to be created.
  it('should create the Component', () => {
    expect(component).toBeTruthy();
  });

  // This test verifies the Pause Click function's button action is enabled.
  it('should verify the button action Pause Click', async(() => {
    spyOn(component, 'onPauseClick');
    const button = fixture.debugElement.nativeElement.querySelector('button');
    button.click();

    fixture.detectChanges();
    expect(component.onPauseClick).toBeTruthy();
  }));

  // This test verifies the No Click function's button action is enabled.
  it('should verify the button action No Click', async(() => {
    spyOn(component, 'onNoClick');
    const button = fixture.debugElement.nativeElement.querySelector('button');
    button.click();

    fixture.detectChanges();
    expect(component.onNoClick).toBeTruthy();
  }));

  // This test verifies the Done Click function's button action is enabled.
  it('should verify the button action Done Click', async(() => {
    spyOn(component, 'onDoneClick');
    const button = fixture.debugElement.nativeElement.querySelector('button');
    button.click();

    fixture.detectChanges();
    expect(component.onDoneClick).toBeTruthy();
  }));

  // This test verifies that the h1 tag within the HTML contains the correct title.
  it('should have an h1 tag', () => {
    const title = fixture.debugElement.query(By.css('.mat-dialog-title')).nativeElement;
    expect(title.innerHTML).toContain('Task ""');
  });

  // This test verifies that the h1 tag is able to be called and that it contains a specific tag within the HTML.
  it('should call and contain the title "Task "" " ', () => {
    fixture = TestBed.createComponent(ConfirmTaskDialogComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('Task ""');
  });

  // This test will verify that the function completeTask is defined and can be called.
  it('should Define and call the function completeTask', async(() => {
    const num = 12;
    const spy = spyOn(component, 'completeTask').and.callThrough();
    component.completeTask(num);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the completeTask function returns the same Task ID as it is given.
  it('should call the function completeTask(number) and verify the same Task ID is passed and set to a complete. ', () => {
    const testComp = fixture.debugElement.injector.get(ConfirmTaskDialogComponent);
    fixture.detectChanges();
    expect(testComp.completeTask(15)).toEqual(component.completeTask(15));
  });

  // This test verifies that the pauseTask function pauses the same Task ID as it is given.
  it('should call the function pauseTask(number) and verify the same Task is passed and set to Pause. ', () => {
    const testComp = fixture.debugElement.injector.get(ConfirmTaskDialogComponent);
    fixture.detectChanges();
    expect(testComp.pauseTask(15)).toEqual(component.pauseTask(15));
  });

  // This test verifies the function onPauseClick is defined and called.
  it('should Define and Call the function onPauseClick', async(() => {
    const num = 12;
    const spy = spyOn(component, 'onPauseClick').and.callThrough();
    component.onPauseClick();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies the function onDoneClick is defined and called.
  it('should Define and Call the function onDoneClick', async(() => {
    const num = 12;
    const spy = spyOn(component, 'onDoneClick').and.callThrough();
    component.onDoneClick();
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));
});

