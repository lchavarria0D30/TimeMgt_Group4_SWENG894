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

@Component({
  selector: 'app-create-task-dialog',
  templateUrl: './create-task-dialog.component.html',
  styleUrls: ['./create-task-dialog.component.css']
})
export class CreateTaskDialogComponent implements OnInit {
  scheduledStart;
  scheduledEnd;
  token;
  selectedCategory = '';
  categories;
  timeRegex = /^(?:(?:1[0-2]|0?[1-9]):[0-5]\d\s*[AaPp][Mm])?$/;

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
      @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  onYesClick(): void {

    this.scheduledStart = this.dateConversion(this.data.ssTime, this.data.ssDate);
    this.scheduledEnd = this.dateConversion(this.data.seTime, this.data.seDate);

    console.log(this.scheduledStart);
    console.log(this.scheduledEnd);
    console.log('Before POST');

    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};

    const body = { name: this.data.name,
      description: this.data.description,
      category: this.selectedCategory,
      scheduledstart: this.scheduledStart,
      scheduledend: this.scheduledEnd,
    };

    console.log("the category: " + this.selectedCategory);

    this.http.post('http://localhost:8001/tasks/newtask', body, { headers }).subscribe({
      next: data => console.log(data),
      error: error => console.error('There was an error!', error)
    });


    this.dialogRef.close();
  }

  dateConversion(time: string, date: Date): Date {
    const tempDate = date;
    const tempTime = time;
    console.log('IN dateconversion');
    const parts = tempTime.match(/(\d+):(\d+) (AM|PM)/);
    if (parts) {
      let hours = parseInt(parts[1], 10);
      const  minutes = parseInt(parts[2], 10);
      const  tt = parts[3];
      if (tt === 'PM' && hours < 12) { hours += 12; }
      date.setHours(hours, minutes, 0, 0);
    }

    return tempDate;
  }

  ngOnInit() {
    console.log("getCategory");
    this.getCategory();
  }

  getCategory(): void {
    this.token = this.sessionService.getToken();

    const headers = { Authorization: 'Bearer ' + this.token
    };

    this.http.get('http://localhost:8001/category/mine', { headers }).subscribe({
      next: data => {
        this.categories = data
        console.log(this.categories);
      },
      error: error => console.error('There was an error!', error)
    });
  }

}

