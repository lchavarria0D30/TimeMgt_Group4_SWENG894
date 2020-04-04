import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StartPopupTaskComponent } from './start-popup-task.component';

describe('StartPopupTaskComponent', () => {
  let component: StartPopupTaskComponent;
  let fixture: ComponentFixture<StartPopupTaskComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StartPopupTaskComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StartPopupTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
