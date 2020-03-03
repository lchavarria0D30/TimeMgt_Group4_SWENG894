import { Component, OnInit, Inject } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { SessionService } from '../../services/session.service';
import { HttpHeaders } from '@angular/common/http';
import { Validators, FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { CreateTaskDialogComponent } from '../create-task-dialog/create-task-dialog.component';
import { DeleteTaskDialogComponent } from '../delete-task-dialog/delete-task-dialog.component';
import { EditTaskDialogComponent } from '../edit-task-dialog/edit-task-dialog.component';

export interface DialogData {
  name: string;
  category: string;
  description: string;
  ssDate: Date;
  ssTime: string;
  seDate: Date;
  seTime: string;
  asDate: Date;
  asTime: string;
  aeDate: Date;
  aeTime: string;
  number: string;
  id: number;
  token: string;
  task: any;
}

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {

  tasks;
  token;


  ngOnInit() {
    this.getTasks();

  }

  constructor(public dialog: MatDialog, private http: HttpClient,
              private sessionService: SessionService ) {}

  openDialog(): void {
    const dialogRef = this.dialog.open(
    CreateTaskDialogComponent, {
      // width: '250px',
      data: {
      token: this.token
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The create dialog was closed');
      this.getTasks();
    });
  }

  openDeleteDialog(i: number, name: string): void {

    const dialogRef = this.dialog.open(
        DeleteTaskDialogComponent, {
      // width: '250px',
      data: {
      id: i,
      name
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The delete dialog was closed');
      this.getTasks();
    });
  }

  openEditDialog(i: number, index: number): void {
    const dialogRef = this.dialog.open(
    EditTaskDialogComponent, {
      data: {
      task:  this.tasks[index],
      token: this.token
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The edit dialog was closed');
      this.getTasks();
    });
  }

  getTasks(): void {
      this.token = this.sessionService.getToken();

      const headers = { Authorization: 'Bearer ' + this.token
       };

      this.http.get('http://localhost:8001/tasks/', { headers }).subscribe({
      next: data => this.tasks = data,
      error: error => console.error('There was an error!', error)
      });
  }


}
