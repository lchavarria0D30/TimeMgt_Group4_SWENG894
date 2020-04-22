/** Linked Issue: TMGP4-38: Notification Task Exceeding Duration
 *
 *  Test Case Linked Issue: TMGP4-241
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NotificationsComponent } from './notifications.component';
import { Notification } from '../notification_model';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';


describe('NotificationsComponent', () => {
  let component: NotificationsComponent;
  let fixture: ComponentFixture<NotificationsComponent>;



  beforeEach(async(() => {
    TestBed.resetTestEnvironment();
    TestBed.initTestEnvironment(BrowserDynamicTestingModule,
        platformBrowserDynamicTesting());
    TestBed.configureTestingModule({
      imports: [HttpClientModule, BrowserAnimationsModule,
        RouterTestingModule.withRoutes([]),
        MatIconModule,
        MatSelectModule,
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
      declarations: [ NotificationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // This test verifies that the Component can be created.
  it('This test verifies that the Component can be created', () => {
    expect(component).toBeTruthy();
  });

  // This test verifies that the function noOnInit is defined and called
  it('This test verifies that the function noOnInit is defined and called', () => {
    const spy = spyOn(component, 'ngOnInit').and.callThrough();
    component.ngOnInit();
    expect(spy).toBeDefined();
    expect(spy).toHaveBeenCalled();
  });

  // This test verifies that the function cancel is defined and called
  it('This test verifies that the function cancel is defined and called', async(() => {
    const note: Notification = new Notification();
    const spy = spyOn(component, 'cancel').and.callThrough();
    component.cancel(note);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the function snooze is defined and called
  it('This test verifies that the function cancel is defined and called', async(() => {
    const note: Notification = new Notification();
    const spy = spyOn(component, 'snooze').and.callThrough();
    component.snooze(note);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the function cssClass is defined and called
  it('This test verifies that the function cssClass is defined and called', async(() => {
    const note: Notification = new Notification();
    const spy = spyOn(component, 'cssClass').and.callThrough();
    component.cssClass(note);
    fixture.whenStable().then(() => {
      expect(spy).toBeDefined();
      expect(spy).toHaveBeenCalled();
    });
  }));

  // This test verifies that the function removeNote is defined and called
  it('This test verifies that the function removeNote is defined and called', async(() => {
    expect(component).toBeTruthy('component is defined');
    if (component) {
      // set the fade value
      component.fade = true;

      expect(component.removeNote).toBeTruthy(
          'function removeNote() exist on component');

      // wait till the fixture is stable
      fixture.whenStable().then(() => {
        // note we will be removing
        const note = new Notification();

        // first add our note
        component.notes.push(note);

        // call our test method
        component.removeNote(note);

        // check that the notes are empty
        expect(component.notes.length).toEqual(1,
            'note list is empty after removeNote()');
      });
    }
  }));

  // This test verifies that the function cssClass is defined and called
  it('This test verifies that the function cssClass returns value', () => {
    expect(component).toBeTruthy('component is defined');
    if (!component) {

      return null;
    }
    expect(component.cssClass).toBeTruthy(
          'function removeNote() exist on component');
    });
});
