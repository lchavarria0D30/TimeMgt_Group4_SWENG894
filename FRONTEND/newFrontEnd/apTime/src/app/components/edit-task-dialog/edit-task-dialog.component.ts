/**
 *
 * Author: Yanisse
 * Jira Task: TMGP4-32
 * Description: The component code for the edit task dialog box. The user inputs the details associated to the
 * to be updated task.
 *
 **/

import {Component, Inject, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HttpClient} from '@angular/common/http';
import {SessionService} from '../../services/session.service';
import {DialogData} from '../tasks/tasks.component';

@Component({
  selector: 'app-edit-task-dialog',
  templateUrl: './edit-task-dialog.component.html',
  styleUrls: ['./edit-task-dialog.component.css']
})
export class EditTaskDialogComponent implements OnInit {

  id: number;
  task: any;
  showActuals: boolean;

  scheduledStart;
  scheduledEnd;
  actualStart;
  actualEnd;
  selectedCategory = '';
  categories;
  minDate;
  minEndDate;
  timeRegex = /^(?:(?:1[0-2]|0?[1-9]):[0-5]\d\s[AP][M])?$/;
  isWrongDate = false;

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
      public dialogRef: MatDialogRef<EditTaskDialogComponent>,
      private http: HttpClient,
      private sessionService: SessionService,
      @Inject(MAT_DIALOG_DATA) public data: DialogData) {
    this.minDate = new Date();
    this.minEndDate = new Date();
  }

  onNoClick(): void {

    this.dialogRef.close();
  }

  onYesClick(): void {

    this.scheduledStart = this.dateConversion(this.task.ssTime, this.task.ssDate);
    this.scheduledEnd = this.dateConversion(this.task.seTime, this.task.seDate);
    // this.actualStart = this.dateConversion(this.task.asTime, this.task.asDate);
    // this.actualEnd = this.dateConversion(this.task.aeTime, this.task.aeDate);

    if (this.scheduledEnd <= this.scheduledStart) {

      this.isWrongDate = true;

    } else {
      this.isWrongDate = false;


      const headers = {Authorization: 'Bearer ' + this.sessionService.getToken()};

      const body = {
        id: this.task.id,
        name: this.task.name,
        description: this.task.description,
        category: this.task.category,
        scheduledstart: this.scheduledStart,
        scheduledEnd: this.scheduledEnd
        // actualstart: this.actualStart,
        // actualend: this.actualEnd

      };

      console.log('Log body before put: ', body);
      console.log('BEFORE put');
      this.http.put('http://localhost:8001/tasks/task', body, {headers}).subscribe({
        next: data => console.log(data),
        error: error => console.error('There was an error!', error)
      });


      this.dialogRef.close();
    }
  }

  onActualsClick(): void {
    this.showActuals = true;
    console.log('Actual click');
  }

  dateConversion(time: string, date: Date): Date {
     const tempDate = date;
     const tempTime = time;

     const parts = tempTime.match(/(\d+):(\d+) (AM|PM)/);
     if (parts) {
      let hours = parseInt(parts[1], 10);
      const  minutes = parseInt(parts[2], 10);
      const tt = parts[3];
      if (tt === 'PM' && hours < 12) { hours += 12; }
      if (tt == 'AM' && hours == 12) {hours = 0; }
      // console.log("hours: ", hours);
      // console.log("minutes: ", minutes);
      // console.log("date: ", date);
      date.setHours(hours, minutes, 0, 0);
    }

     return tempDate;
  }


  ngOnInit() {
    this.showActuals = false;
    this.getCategory();

    this.id = this.data.id;

    this.task = this.data.task;

    setTimeout(() => {
      let date = this.task.scheduledstart.substring(0, this.task.scheduledstart.length - 5);
      const ssDate = new Date(date);
      const ssTime = ssDate.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
      this.task.ssTime = ssTime;
      this.task.ssDate = ssDate;

      date = this.task.scheduledEnd.substring(0, this.task.scheduledEnd.length - 5);
      const seDate = new Date(date);
      const seTime = seDate.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
      this.task.seTime = seTime;
      this.task.seDate = seDate;

    });

    console.log('The task: ', this.task);


  }

  getCategory(): void {

    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()
    };

    this.http.get('http://localhost:8001/category/mine', { headers }).subscribe({
      next: data => {
        this.categories = data;
        console.log(this.categories);
      },
      error: error => console.error('There was an error!', error)
    });
  }

  changeMinEndDate() {
    this.minEndDate = this.task.ssDate;
  }
}

