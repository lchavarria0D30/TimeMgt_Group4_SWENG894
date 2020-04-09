/** Linked Issue: TMGP4-38: Notification Task Exceeding Duration
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { MatIconModule} from '@angular/material/icon';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA, DebugElement} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NotificationsComponent } from './notifications.component';
import { NotificationType, Notification } from '../notification_model';
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
import {By} from '@angular/platform-browser';
import {NotificationsServiceService} from '../notifications-service.service';
import {SessionService} from '../../../services/session.service';
import {Router} from '@angular/router';

import any = jasmine.any;
import {EMPTY} from "rxjs";


class MockNotificationsComponent extends NotificationsComponent {
  public notes: any;
  public dialogRef: any;
  public filter: any;
  @Input() fade: boolean;

  removeNote(notes) {

    if (this.fade) {
      // fade out alert
      this.notes.find(x => x === alert).fade = true;

      // remove alert after faded out
      setTimeout(() => {
        this.notes = this.notes.filter(x => x !== alert);
      }, 2500);
    } else {
      // remove alert
      this.notes = this.notes.filter(x => x !== alert);
    }
  }
}

describe('NotificationsComponent', () => {
  let component: NotificationsComponent;
  let fixture: ComponentFixture<NotificationsComponent>;
  let de: DebugElement;
  let cancelButton: DebugElement;
  let cancelMessage: NotificationsComponent;
  let route: Router;
  let note: NotificationsServiceService;
  let http: HttpClient;
  let session: SessionService;
  let mock: MockNotificationsComponent;

  beforeEach(async(() => {
    mock = new MockNotificationsComponent(route, note, http, session);
    cancelMessage = new NotificationsComponent( route, note, http, session);
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

    cancelButton = fixture.debugElement.query(By.css('mat-raised-button'));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should test ', () => {
    component.ngOnInit();
    expect(component.ngOnInit).toBeTruthy();
  });

  it('Test Mock Remove Note', () => {
        const NoteTest = any;
        spyOn(mock, 'removeNote').and.returnValue(undefined);
        expect(mock.removeNote(NoteTest)).toBeTruthy();

  });

  /*it('Test removeNote', () => {
    const spy = spyOn(component.notes, 'find')
        .and
        .returnValue({afterClosed: () => EMPTY} as any);
    // component.removeNote();
    expect(spy).toHaveBeenCalled();
  });
  */


});
