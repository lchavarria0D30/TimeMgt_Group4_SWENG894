/**
 *
 * Author: Yanisse
 * Jira Task: TMGP4-30
 * Description: The component code for the delete task dialog box. User confirms whether he wants to delete the task.
 *
 **/

import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HttpClient} from '@angular/common/http';
import {SessionService} from '../../services/session.service';
import {DialogData} from '../tasks/tasks.component';
import {environment} from '../../../environments/environment'

@Component({
  selector: 'app-delete-task-dialog',
  templateUrl: './delete-task-dialog.component.html',
  styleUrls: ['./delete-task-dialog.component.css']
})
export class DeleteTaskDialogComponent implements OnInit  {
  id;
  number;
  name;


  constructor(
      public dialogRef: MatDialogRef<DeleteTaskDialogComponent>,
      private http: HttpClient,
      private sessionService: SessionService,
      @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  onYesClick(): void {

    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};

    this.http.delete(environment.baseUrl+'/tasks/task/' + this.data.id, { headers }).subscribe({
      next: data => {
        console.log(data);
        this.dialogRef.close();
      },
      error: error => console.error('There was an error!', error)
    });

  }

  ngOnInit() {
    this.name = this.data.name;
  }

}
