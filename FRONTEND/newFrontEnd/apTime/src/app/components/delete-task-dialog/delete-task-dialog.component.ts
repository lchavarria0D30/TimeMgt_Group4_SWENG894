import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HttpClient} from '@angular/common/http';
import {SessionService} from '../../services/session.service';
import {DialogData} from '../tasks/tasks.component';

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

    this.http.delete('http://localhost:8001/tasks/task/' + this.data.id, { headers }).subscribe({
      next: data => console.log(data),
      error: error => console.error('There was an error!', error)
    });

    this.dialogRef.close();
  }

  ngOnInit() {
    this.name = this.data.name;
  }

}
