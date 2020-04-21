/** Use Case Linked Issue: TMGP4-184: Create Final Web Designs
 *
 *  Test Case Linked Issue: TMGP4-81
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
import { HomeComponent } from './home.component';
import {
  MatFormFieldModule,
  MatInputModule,
  MatDialogModule,
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatSelectModule
} from '@angular/material';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

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
      declarations: [HomeComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // This test verifies that the Component can be created
  it('should create', () => {
    expect(component).toBeTruthy();
  });
});