import {Component, Inject, Output, EventEmitter, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {SessionService} from "../../services/session.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";


export interface DialogData {
  id: string;
  name: string;
}


@Component({
  selector: 'app-start-popup-task',
  templateUrl: './start-popup-task.component.html',
  styleUrls: ['./start-popup-task.component.css']
})
export class StartPopupTaskComponent implements OnInit {
  token;
  taskName;
  today = new Date();
  hours;
  minutes;
  categories = [];
  selectedCategory;

  hoursRegex = /^([0-9]*)$/;
  minsRegex = /^([0-9]|[0-5][0-9])$/;

  nameFormControl = new FormControl('', [
    Validators.required
  ]);

  hoursFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern(this.hoursRegex)
  ]);

  minutesFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern(this.minsRegex)
  ]);

  constructor(public dialogRef: MatDialogRef<StartPopupTaskComponent>,
              private http: HttpClient,
              private sessionService: SessionService,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) { }

  ngOnInit() {
    this.getCategory();
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onYesClick(): void {
    const scheduledEnd = new Date (this.today.toString());
    scheduledEnd.setTime(this.today.getTime() + this.hours * 3600000 + this.minutes * 60000);

    // console.log('The start date: ' + this.today);
    // console.log('New Date: ' + scheduledEnd);

    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};

    const body = { name: this.taskName,
      description: 'PopUp Task',
      categories: [{id: this.selectedCategory}],
      scheduledstart: this.today,
      scheduledEnd: scheduledEnd
    };

    console.log('Request PopUp Body: ', body);

    this.http.post('http://localhost:8001/tasks/newtask', body, { headers }).subscribe({
      next: data => {
        console.log(data);
        this.dialogRef.close(data);
      },
      error: error => console.error('There was an error!', error)
    });


  }

  getCategory(): void {

    this.token = this.sessionService.getToken();

    const headers = { Authorization: 'Bearer ' + this.token
    };

    this.http.get('http://localhost:8001/category/', { headers }).subscribe({
      next: data => {
        this.categories = this.categories.concat(data);
      },
      error: error => console.error('There was an error!', error)
    });
  }

}
