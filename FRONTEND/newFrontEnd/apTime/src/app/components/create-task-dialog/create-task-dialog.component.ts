/**
 *
 * Author: Yanisse
 * Jira Task: TMGP4-29
 * Description: The component code for the create task dialog box. The users inputs all the data associated to the task
 * to be created.
 *
 **/

import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HttpClient} from '@angular/common/http';
import {SessionService} from '../../services/session.service';
import {DialogData} from '../tasks/tasks.component';
import {environment} from '../../../environments/environment'

@Component({
  selector: 'app-create-task-dialog',
  templateUrl: './create-task-dialog.component.html',
  styleUrls: ['./create-task-dialog.component.css']
})
export class CreateTaskDialogComponent implements OnInit {
  dialogTitle = 'New Task';
  scheduledStart;
  scheduledEnd;
  token;
  selectedCategory = '';
  categories = [];
  minDate;
  minEndDate;
  timeRegex = /^(?:(?:1[0-2]|0?[1-9]):[0-5]\d\s[AP][M])?$/;
  isWrongDate = false;
  suggView = false;
  factor;
  suggestedDuration;
  suggestions;

  nameFormControl = new FormControl('', [
    Validators.required
  ]);

  sDateFormControl = new FormControl('', [
    Validators.required
  ]);

  eDateFormControl = new FormControl('', [
    Validators.required
  ]);

  sTimeFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern(this.timeRegex)
  ]);

  eTimeFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern(this.timeRegex)
  ]);

  constructor(
      public dialogRef: MatDialogRef<CreateTaskDialogComponent>,
      private http: HttpClient,
      private sessionService: SessionService,
      @Inject(MAT_DIALOG_DATA) public data: DialogData) {

    this.minDate = new Date();

  }

  ngOnInit() {

    this.getCategory();
  }

  onNoClick(): void {

    this.dialogRef.close();
  }

  onYesClick(): void {

    this.scheduledStart = this.dateConversion(this.data.ssTime, this.data.ssDate);

    this.scheduledEnd = this.dateConversion(this.data.seTime, this.data.seDate);

    if (this.scheduledEnd <= this.scheduledStart) {
      this.isWrongDate = true;

    } else {
      this.isWrongDate = false;

      const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};

      const body = { name: this.data.name,
        description: this.data.description,
        categories: [{id: this.selectedCategory}],
        scheduledstart: this.scheduledStart,
        scheduledEnd: this.scheduledEnd,
      };


      this.http.post(environment.baseUrl+'/tasks/newtask', body, { headers }).subscribe({
        next: data => console.log('new task created'),
        error: error => console.error('There was an error!', error)
      });

      this.dialogRef.close();
    }

  }

  onSuggClick() {

    this.scheduledStart = this.dateConversion(this.data.ssTime, this.data.ssDate);
    this.scheduledEnd = this.dateConversion(this.data.seTime, this.data.seDate);

    if (this.scheduledEnd <= this.scheduledStart) {
      this.isWrongDate = true;

    } else {
      this.isWrongDate = false;

      const diff = Math.abs(this.scheduledEnd.getTime() - this.scheduledStart.getTime());
      const minutes = Math.floor((diff / 1000) / 60);

      const headers = { Authorization: 'Bearer ' + this.sessionService.getToken() };
      const body = { Duration: minutes,
        CategoryID: this.selectedCategory
      };

      this.http.post(environment.baseUrl+'/tasks/predict', body , { headers }).subscribe({
        next: data => {
          this.suggestions = data;
          this.factor = this.suggestions.Confidence;
          this.suggestedDuration = this.suggestions.Duration;
        },
        error: error => console.error('There was an error!', error)
      });

      this.dialogTitle = 'Suggestions';
      this.suggView = true;
    }
  }

  onBackClick() {
    this.dialogTitle = 'New Task';
    this.suggView = false;
  }

  dateConversion(time: string, date: Date): Date {
    const tempDate = date;
    const tempTime = time;

    const parts = tempTime.match(/(\d+):(\d+) (AM|PM)/);
    if (parts) {
      let hours = parseInt(parts[1], 10);
      const  minutes = parseInt(parts[2], 10);
      const  tt = parts[3];
      if (tt === 'PM' && hours < 12) { hours += 12; }
      if (tt == 'AM' && hours == 12) {hours = 0; }
      date.setHours(hours, minutes, 0, 0);
    }

    return tempDate;
  }


  getCategory(): void {
    this.token = this.sessionService.getToken();

    const headers = { Authorization: 'Bearer ' + this.token
    };

    this.http.get(environment.baseUrl+'/category/', { headers }).subscribe({
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

  changeMinEndDate() {
      this.minEndDate = this.data.ssDate;
  }

}

