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
  number;
  name;


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
    this.dialogRef.close();
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
      next: data => console.log(data),
      error: error => console.error('There was an error!', error)
    });
  }

}
