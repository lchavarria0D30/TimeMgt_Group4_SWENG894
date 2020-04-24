/**
 *
 * Author: Yanisse
 * Jira Task: TMGP4-48 / TMGP4-181
 * Description: Component code for the confirm dialog pop-up. The user confirms whether he wants to complete or pause
 * the task.
 *
 **/

import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HttpClient} from '@angular/common/http';
import {SessionService} from '../../services/session.service';
import {DialogData} from '../tasks/tasks.component';
import {environment} from '../../../environments/environment'


@Component({
  selector: 'app-confirm-task-dialog',
  templateUrl: './confirm-task-dialog.component.html',
  styleUrls: ['./confirm-task-dialog.component.css']
})
export class ConfirmTaskDialogComponent implements OnInit {

  id;
  name;
  isDone;

  constructor(
      public dialogRef: MatDialogRef<ConfirmTaskDialogComponent>,
      private http: HttpClient,
      private sessionService: SessionService,
      @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  onPauseClick(): void {

    this.pauseTask(this.id);

  }

  onDoneClick(): void {

    this.completeTask(this.id);

  }


  ngOnInit() {
    this.name = this.data.name;
    this.id = this.data.id;
    this.isDone = this.data.isDone;
  }

  pauseTask(i: number): void {
    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};
    console.log("SuspendTask");
    const body = {
    };

    this.http.post(environment.baseUrl+'/tasks/task/' + i + '/pause', body, { headers }).subscribe({
      next: data => {
        console.log(data);
        this.dialogRef.close();
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
        console.log(data);
        this.dialogRef.close();
      },
      error: error => console.error('There was an error!', error)
    });
  }

}
