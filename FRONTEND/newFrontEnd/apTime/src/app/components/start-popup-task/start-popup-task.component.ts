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
  dialogTitle = 'Starts at: ';

  suggestions;

  today = new Date();
  hours;
  minutes;
  categories = [];
  selectedCategory = '';
  suggView = false;
  factor;
  suggestedDuration;
  suggestedDate;

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
    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};

    const body = { name: this.taskName,
      description: 'PopUp Task',
      categories: [{id: this.selectedCategory}],
      scheduledstart: this.today,
      scheduledEnd: scheduledEnd
    };

    this.http.post('http://localhost:8001/tasks/newtask', body, { headers }).subscribe({
      next: data => {
        this.dialogRef.close(data);
      },
      error: error => console.error('There was an error!', error)
    });


  }

  onSuggClick() {
    const scheduledEnd = new Date (this.today.toString());
    scheduledEnd.setTime(this.today.getTime() + this.hours * 3600000 + this.minutes * 60000);

    const diff = Math.abs(scheduledEnd.getTime() - this.today.getTime() );
    const minutes = Math.floor((diff / 1000) / 60);

    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken() };
    const body = { Duration: minutes, CategoryID: this.selectedCategory };

    this.http
        .get(
            'http://localhost:8001/tasks/predict?duration=' +
            body.Duration +
            '&categoryId=' +
            body.CategoryID,
            { headers }
        )
        .subscribe({
          next: data => {
            console.log(data);
            this.suggestions = data;
            this.factor = this.suggestions.confidence;
            this.suggestedDuration = this.suggestions.duration;
          },
          error: error => console.error('There was an error!', error)
        });

    this.suggestedDate = new Date();

    this.suggestedDate.setTime(this.today.getTime() + this.suggestedDuration * 60000);

    this.dialogTitle = 'Suggestions - ';
    this.suggView = true;

  }

  onAcceptClick() {

    const diffHours = Math.abs(this.suggestedDate.getTime() - this.today.getTime()) / 3600000;
    const diffMins = (Math.abs(this.suggestedDate.getTime() - this.today.getTime()) % 3600000) / 60000;

    this.hours = Math.floor(diffHours);
    this.minutes = diffMins;

    this.dialogTitle = 'Starts at:  ';
    this.suggView = false;
  }

  onBackClick() {
    this.dialogTitle = 'Starts at:  ';
    this.suggView = false;
  }

  getCategory(): void {

    this.token = this.sessionService.getToken();

    const headers = { Authorization: 'Bearer ' + this.token
    };

    this.http.get('http://localhost:8001/category/', { headers }).subscribe({
      next: data => {
        this.categories = this.categories.concat(data);
        this.categories.sort((a, b) => {
          if (a.name > b.name) { return 1; }
          if (a.name < b.name) { return -1; }
          return 0;
        });
      },
      error: error => console.error('There was an error!', error)
    });
  }

}
