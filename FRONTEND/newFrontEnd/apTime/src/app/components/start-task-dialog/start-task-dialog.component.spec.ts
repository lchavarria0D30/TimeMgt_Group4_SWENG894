import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StartTaskDialogComponent } from './start-task-dialog.component';

describe('StartTaskDialogComponent', () => {
  let component: StartTaskDialogComponent;
  let fixture: ComponentFixture<StartTaskDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StartTaskDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StartTaskDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
