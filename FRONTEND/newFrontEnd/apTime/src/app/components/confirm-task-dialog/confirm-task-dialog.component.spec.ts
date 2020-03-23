import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmTaskDialogComponent } from './confirm-task-dialog.component';

describe('ConfirmTaskDialogComponent', () => {
  let component: ConfirmTaskDialogComponent;
  let fixture: ComponentFixture<ConfirmTaskDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmTaskDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmTaskDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
