/** This test file is not linked to any Use Case,
 *
 *  The file is used for information purposes only.
 *
 *  Author: Chavarria Leo
 *
 */
import {async, ComponentFixture, getTestBed, TestBed} from '@angular/core/testing';
import { MatIconModule} from '@angular/material/icon';
import {CustomMaterialModule} from '../../modules/material.module';
import {EMPTY} from 'rxjs';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {AmplifyService} from 'aws-amplify-angular';
import {BrowserDynamicTestingModule, platformBrowserDynamicTesting} from '@angular/platform-browser-dynamic/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {DialogData} from '../tasks/tasks.component';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatMenuModule,
  MatSelectModule
} from '@angular/material';
import { MeetTheTeamComponent } from './meet-the-team.component';


describe('MeetTheTeamComponent', () => {
  let component: MeetTheTeamComponent;
  let fixture: ComponentFixture<MeetTheTeamComponent>;
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
        MatMenuModule
      ],
      providers: [AmplifyService, {
        provide: MatDialogRef,
        useValue: dialogMock, },
        { provide: MAT_DIALOG_DATA, useValue: {}}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ MeetTheTeamComponent ]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MeetTheTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    httpMock = getTestBed().get(HttpTestingController);
  });

  // This test verifies that the Component is able to be created.
  it('This test verifies that the Component is able to be created.', () => {
    expect(component).toBeTruthy();
  });
});
