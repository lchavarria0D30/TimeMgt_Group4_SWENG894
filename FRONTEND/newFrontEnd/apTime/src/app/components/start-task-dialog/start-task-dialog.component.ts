import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HttpClient} from '@angular/common/http';
import {SessionService} from '../../services/session.service';
import {DialogData} from '../tasks/tasks.component';

@Component({
  selector: 'app-start-task-dialog',
  templateUrl: './start-task-dialog.component.html',
  styleUrls: ['./start-task-dialog.component.css']
})
export class StartTaskDialogComponent implements OnInit {
  id;
  name;
  isConcurrent = false;


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

  onCompleteClick(): void {
    console.log('Complete clicked!');
  }

  onSuspendClick(): void {
    console.log('Suspend clicked!');
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

    this.http.post('http://localhost:8001/tasks/task/' + i + '/start', body, { headers }).subscribe({
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
        }
      }
    });
  }

  suspendTask(i: number): void {
    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};
    console.log("SuspendTask");
    const body = {
    };

    this.http.post('http://localhost:8001/tasks/task/' + i + '/pause', body, { headers }).subscribe({
      next: data => {
        console.log(data)
      },
      error: error => console.error('There was an error!', error)
    });
  }

  completeTask(i: number): void {
    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};
    console.log("CompleteTask");
    const body = {
    };

    this.http.post('http://localhost:8001/tasks/task/' + i + '/complete', body, { headers }).subscribe({
      next: data => console.log(data),
      error: error => console.error('There was an error!', error)
    });
  }

}
