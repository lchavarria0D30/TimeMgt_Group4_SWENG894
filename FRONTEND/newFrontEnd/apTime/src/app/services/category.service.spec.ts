import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import { CategoryService } from './category.service';
import {DeleteTaskDialogComponent} from "../components/delete-task-dialog/delete-task-dialog.component";
import {BrowserDynamicTestingModule, platformBrowserDynamicTesting} from "@angular/platform-browser-dynamic/testing";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {RouterTestingModule} from "@angular/router/testing";
import {MatIconModule} from "@angular/material/icon";
import {CustomMaterialModule} from "../modules/material.module";
import {AmplifyService} from "aws-amplify-angular";
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
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

describe('CategoryService', () => {
  let component: CategoryService;
  let fixture: ComponentFixture<CategoryService>;

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
      declarations: [ DeleteTaskDialogComponent ]
    })
        .compileComponents();
  }));
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CategoryService = TestBed.get(CategoryService);
    expect(service).toBeTruthy();
  });

  it('should be Defined create', async(() => {
    const service: CategoryService = TestBed.get(CategoryService);
    spyOn(service, 'create').and.callThrough();
    expect(service.create).toBeDefined();
    }));

  it('should be call create', async(() => {
    const service: CategoryService = TestBed.get(CategoryService);
    spyOn(service, 'create').and.callThrough();
    expect(service.create).toHaveBeenCalledTimes(0);
  }));
});
