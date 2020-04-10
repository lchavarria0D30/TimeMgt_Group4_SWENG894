/** Linked Issue: TMGP4-32: Update Task
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
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { EditTaskDialogComponent } from './edit-task-dialog.component';
import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {SessionService} from '../../services/session.service';
import {DialogData} from '../tasks/tasks.component';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatButtonModule,
  MatToolbar,
  MatRadioModule,
  MatSelectModule
} from '@angular/material';

describe('EditTaskDialogComponent', () => {
  let component: EditTaskDialogComponent;
  let fixture: ComponentFixture<EditTaskDialogComponent>;

  const data: DialogData = {
    id : 1,
    task : {
      id: 1,
      scheduledstart: '',
      scheduledEnd: '',
      categories: [{id: 1, name: 'name'}],
    },
    name: 'test',
    category: undefined,
    description: undefined,
    ssDate: undefined,
    ssTime: undefined,
    seDate: undefined,
    seTime: undefined,
    asDate: undefined,
    asTime: undefined,
    aeDate: undefined,
    aeTime: undefined,
    number: undefined,
    token: undefined,
    isDone: undefined
  }

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
        { provide: MAT_DIALOG_DATA, useValue: data}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ EditTaskDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditTaskDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
