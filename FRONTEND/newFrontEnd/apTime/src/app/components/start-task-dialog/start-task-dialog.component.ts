/**
 *
 * Author: Yanisse
 * Jira Task: TMGP4-47
 * Description: The component code for the start task dialog box. The user confirms whether he wants to start the task
 * or not.
 *
 **/

import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HttpClient} from '@angular/common/http';
import {SessionService} from '../../services/session.service';
import {DialogData} from '../tasks/tasks.component';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-start-task-dialog',
  templateUrl: './start-task-dialog.component.html',
  styleUrls: ['./start-task-dialog.component.css']
})
export class StartTaskDialogComponent implements OnInit {
  id;
  name;
  isConcurrent = false;
  task;

  constructor(
      public dialogRef: MatDialogRef<StartTaskDialogComponent>,
      private http: HttpClient,
      private sessionService: SessionService,
      @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  onYesClick(): void {
    this.startTask(this.id);

    if (this.isConcurrent)
      this.dialogRef.close();
  }

  onCompleteClick(i: number): void {
    console.log('Complete clicked!');
    this.completeTask(i);
  }

  onSuspendClick(i: number): void {
    console.log('Suspend clicked!');
    this.suspendTask(i);
  }

  ngOnInit() {
    this.name = this.data.name;
    this.id = this.data.id;
  }

  startTask(i: number): void {
    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};
    console.log("StartTask");
    const body = { startDate: new Date()
    };

    this.http.post(environment.baseUrl+'/tasks/task/' + i + '/start', body, { headers }).subscribe({
      next: data => {
        console.log(data)
        this.dialogRef.close();
      },
      error: error => {
        console.error('There was an error!', error)
        //console.log("the error: " + error.error.errorType)
        if(error.error.errorType == 'Concurrent_Active_Task_Not_Allowed') {
          console.log('There is another task running');
          this.isConcurrent = true;
          // console.log(error.error.payload);
          this.task = error.error.payload;
        }
      }
    });
  }

  suspendTask(i: number): void {
    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};
    console.log("SuspendTask");
    const body = {
    };

    this.http.post(environment.baseUrl+'/tasks/task/' + i + '/pause', body, { headers }).subscribe({
      next: data => {
        // console.log(data)
        this.startTask(this.id);
      },
      error: error => console.error('There was an error!', error)
    });
  }

  completeTask(i: number): void {
    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};
    console.log("CompleteTask");
    const body = {
    };

    this.http.post(environment.baseUrl+'/tasks/task/' + i + '/complete', body, { headers }).subscribe({
      next: data => {
        // console.log(data)
        this.startTask(this.id);

      },
      error: error => console.error('There was an error!', error)
    });
  }

}
