/**
 *
 * Author: Yanisse
 * Jira Task: TMGP4-181, TMGP4-47, TMGP4-48, TMGP4-30, TMGP4-32, TMGP4-29
 * Description: The component code for the general tasks view. User can see and manage all his tasks in this view.
 *
 **/

import { Component, OnInit, Inject } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { SessionService } from '../../services/session.service';
import { HttpHeaders } from '@angular/common/http';
import { Validators, FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { CreateTaskDialogComponent } from '../create-task-dialog/create-task-dialog.component';
import { DeleteTaskDialogComponent } from '../delete-task-dialog/delete-task-dialog.component';
import { EditTaskDialogComponent } from '../edit-task-dialog/edit-task-dialog.component';
import { StartTaskDialogComponent } from '../start-task-dialog/start-task-dialog.component';
import { ConfirmTaskDialogComponent } from '../confirm-task-dialog/confirm-task-dialog.component';

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
  isDone: boolean;
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

  openConfirmDialog(i: number, name: string, isDone: boolean): void {

    const dialogRef = this.dialog.open(
        ConfirmTaskDialogComponent, {
          // width: '250px',
          data: {
            id: i,
            name,
            isDone: isDone
          }
        });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The confirm dialog was closed');
      this.getTasks();
    });
  }

  openEditDialog(i: number, index: number): void {
    // let theTask = this.tasks[index];
    // console.log("the task before: ", theTask);
    // let date = new Date(theTask.scheduledstart);
    // let ssTime = new Date(theTask.scheduledstart.substring(0, theTask.scheduledstart.length - 5));
    // let ssDate = ssTime.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});


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

  openStartDialog(i: number, name: string): void {
    const dialogRef = this.dialog.open(
        StartTaskDialogComponent, {
          data: {
            id: i,
            name
          }
        });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The start task dialog was closed');
      this.getTasks();
    });
  }


  getTasks(): void {
      this.token = this.sessionService.getToken();

      const headers = { Authorization: 'Bearer ' + this.token
       };

      this.http.get('http://localhost:8001/tasks/', { headers }).subscribe({
      next: data => {

        console.log(this.tasks);
        this.tasks = data

        this.tasks.sort((a, b) => {
          return <any> new Date(b.scheduledstart.substring(0, b.scheduledstart.length - 5)) -
          <any> new Date(a.scheduledstart.substring(0, a.scheduledstart.length - 5));
        });

        console.log('sorted: ', this.tasks);



      },
      error: error => console.error('There was an error!', error)
      });

  }

}
