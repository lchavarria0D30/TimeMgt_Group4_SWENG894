/**
 *
 * Author: Yanisse
 * Jira Task: TBD
 * Description: The component code for the dashboard. Users will see their current tasks in a dashboard view.
 *
 **/

import { Component, OnInit } from '@angular/core';
import { SessionService } from '../../services/session.service';
import {HttpClient} from '@angular/common/http';
import {FormControl} from '@angular/forms';
import {filter} from 'rxjs/operators';
import {CreateTaskDialogComponent} from "../create-task-dialog/create-task-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {StartPopupTaskComponent} from "../start-popup-task/start-popup-task.component";
import {StartTaskDialogComponent} from "../start-task-dialog/start-task-dialog.component";
import {ConfirmTaskDialogComponent} from "../confirm-task-dialog/confirm-task-dialog.component";
import {EditTaskDialogComponent} from "../edit-task-dialog/edit-task-dialog.component";
import {DeleteTaskDialogComponent} from "../delete-task-dialog/delete-task-dialog.component";
import {environment} from '../../../environments/environment';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  tasks;
  dateTasks;
  token;
  activeTasks = [];
  pausedTasks = [];
  toDoTasks = [];
  completedTasks = [];
  theTask;
  filteredTasks = [];
  date;


  constructor(public dialog: MatDialog,
              private http: HttpClient,
              private sessionService: SessionService ) { }

  ngOnInit() {
    const tempDate = new Date();

    tempDate.setHours(0, 0, 0, 0);

    this.date = new FormControl(tempDate);

    this.getTasks();

    this.getDateTasks();

  }

  openDialog(): void {
    const dialogRef = this.dialog.open(
        CreateTaskDialogComponent, {
          data: {
            token: this.token
          }
        });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The create dialog was closed');
      this.getTasks();
      this.getDateTasks();
    });
  }

  openStartPopUpDialog(): void {
    const dialogRef = this.dialog.open(
        StartPopupTaskComponent, {
          data: {
          }
        });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The start popup task dialog was closed');
      if (result !== undefined) {
        this.openStartDialog(result.id, result.name);
      }
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
      this.getDateTasks();
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
      this.getDateTasks();
    });
  }

  openEditDialog(i: number, index: number): void {

    const dialogRef = this.dialog.open(
        EditTaskDialogComponent, {
          data: {
            task:  this.filteredTasks[index],
            token: this.token
          }
        });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The edit dialog was closed');
      this.getTasks();
      this.getDateTasks();
    });
  }

  openDeleteDialog(i: number, name: string): void {

    const dialogRef = this.dialog.open(
        DeleteTaskDialogComponent, {
          data: {
            id: i,
            name
          }
        });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The delete dialog was closed');
      this.getTasks();
      this.getDateTasks();
    });
  }

  getDateTasks(): void {

    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};

    const body = { date: this.date.value.toISOString().substring(0, 10)
    };


    this.http.post(environment.baseUrl+'/tasks/due/start', body, { headers }).subscribe({
      next: data => {
        this.dateTasks = data;

        const fromDate = new Date(this.date.value);
        const toDate = new Date(this.date.value);

        fromDate.setHours(0, 0, 0, 0);

        toDate.setDate(toDate.getDate() + 1);
        toDate.setHours(0, 0, 0, 0);

        if (this.dateTasks !== undefined) {
          this.filteredTasks = this.dateTasks.filter(
              function (dateTasks) {
                  const date = new Date(dateTasks.scheduledstart);
                  if (isNaN(date.getTime())) {
                    return (new Date(dateTasks.scheduledstart.substring(0, dateTasks.scheduledstart.length - 5)) >= fromDate
                        && new Date(dateTasks.scheduledstart.substring(0, dateTasks.scheduledstart.length - 5)) < toDate);
                  } else {
                    return (new Date(dateTasks.scheduledstart) >= fromDate
                        && new Date(dateTasks.scheduledstart) < toDate);
                  }
              }
          );
        }

        if (data == null) {
          this.theTask = undefined;
        }


        this.filteredTasks.sort((a, b) => {
          const date = new Date(a.scheduledstart);
          if (isNaN(date.getTime())) {
            return <any>new Date(a.scheduledstart.substring(0, a.scheduledstart.length - 5)) -
                <any>new Date(b.scheduledstart.substring(0, b.scheduledstart.length - 5));
          } else {
            return <any>new Date(a.scheduledstart) -
                <any>new Date(b.scheduledstart);
          }
        });

      },
      error: error => console.error('There was an error!', error)
    });

  }

  getTasks(): void {
    this.token = this.sessionService.getToken();

    const headers = { Authorization: 'Bearer ' + this.token
    };

    this.http.get(environment.baseUrl+'/tasks/', { headers }).subscribe({
      next: data => {
        this.tasks = data
        if (this.tasks !== undefined) {

          this.activeTasks = this.tasks.filter(
              function (tasks) {
                return tasks.state === 'ACTIVE';
              }
          )

          this.pausedTasks = this.tasks.filter(
              function (tasks) {
                return tasks.state === 'PAUSED';
              }
          )

          this.toDoTasks = this.tasks.filter(
              function (tasks) {
                return tasks.state === 'CREATED';
              }
          )

          this.completedTasks = this.tasks.filter(
              function (tasks) {
                return tasks.state === 'COMPLETED';
              }
          );
        }
      },
      error: error => console.error('There was an error!', error)
    });

  }

}
