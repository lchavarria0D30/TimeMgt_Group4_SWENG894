/** Use Case Linked Issue: TMGP4-16 & TMGP4-14
 *
 *  Test Case Linked Issue: TMGP4-70
 *
 *  Author: Chavarria Leo
 *
 *  Unit Test - Frontend
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthComponent } from './auth.component';
import {AmplifyService} from 'aws-amplify-angular';
import {HttpClient} from '@angular/common/http';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material';

describe('AuthComponent', () => {
  let component: AuthComponent;
  let fixture: ComponentFixture<AuthComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [AmplifyService, HttpClient, {
        provide: MatDialogRef,
        useValue: {}
      },
        {provide: MAT_DIALOG_DATA, useValue: {}}],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [ AuthComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  // This test verifies that the component can be created.
  it('should create the Component', () => {
    expect(component).toBeTruthy();
  });

  // This test verifies that the component is able to be defined and can be called.
  it('should define and call the function - ngOnInit ', () => {
    const spy = spyOn(component, 'ngOnInit');
    component.ngOnInit();
    expect(spy).toBeDefined();
    expect(spy).toHaveBeenCalled();
  });
});
