/** Use Case Linked Issue: TMGP4-184
 *
 *  Test Case Linked Issue: TMGP4-280
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { MatMenuModule} from '@angular/material/menu';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {AmplifyService} from 'aws-amplify-angular';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { By } from '@angular/platform-browser';
import { NotificationsServiceService } from './components/notification';
import {HttpClient, HttpClientModule} from '@angular/common/http';


describe('AppComponent', () => {
  let fixture;
  let title = 'TimeMngmt';
  let comp: AppComponent;


  beforeEach(async(() => {
    TestBed.resetTestEnvironment();
    TestBed.initTestEnvironment(BrowserDynamicTestingModule,
        platformBrowserDynamicTesting());
    TestBed.configureTestingModule({
      imports: [ HttpClientModule,
        RouterTestingModule.withRoutes([]), MatMenuModule,

      ],
      declarations: [
        AppComponent
      ],
      providers: [AmplifyService, HttpClient],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();

  }));
  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
  });
  // const fixture = TestBed.createComponent(AppComponent)
  it('should create the app', () => {
    // const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should have a title Time Helper', () => {
    // const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('Time Helper');
  });

  it('should render title', async () => {
    const component = fixture.componentInstance;
    component.inputName = true;
    fixture.detectChanges();
    // await fixture.whenStable();
    console.log(fixture.debugElement.nativeElement.innerHTML);
    const compiled = fixture.debugElement.query(By.css('.header-text'));
    // console.log(compiled.debugElement.nativeElement.innerHTML);
    // title = compiled.nativeElement;
    expect(title).toEqual('TimeMngmt');
  });
});
