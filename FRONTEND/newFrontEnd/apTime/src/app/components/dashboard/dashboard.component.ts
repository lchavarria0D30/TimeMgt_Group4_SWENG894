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


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  tasks;
  token;
  activeTasks = [];
  pausedTasks = [];
  toDoTasks = [];
  completedTasks = [];
  theTask;
  filteredTasks = [];

  date = new FormControl(new Date());

  constructor(private http: HttpClient,
              private sessionService: SessionService ) { }

  ngOnInit() {
    this.getTasks();

    setTimeout(() => {
      this.getDateTasks();
    });

  }

  // selectedTask(i: number): void {
  //   this.theTask = this.filteredTasks[i];
  // }

  getDateTasks(): void {
    console.log('the date: ', this.date.value);
    const fromDate = new Date(this.date.value);
    const toDate = new Date(this.date.value);

    fromDate.setHours(0, 0, 0, 0);

    toDate.setDate(toDate.getDate() + 1);
    toDate.setHours(0, 0, 0, 0);

    if (this.tasks !== undefined) {
      this.filteredTasks = this.tasks.filter(
          function(tasks) {
            return (new Date(tasks.scheduledstart.substring(0, tasks.scheduledstart.length - 5)) >= fromDate
                && new Date(tasks.scheduledstart.substring(0, tasks.scheduledstart.length - 5)) < toDate);
          }
      );


      // if (this.filteredTasks == null || this.filteredTasks.length < 1) {
      //   this.theTask = undefined;
      // } else {
      //   this.selectedTask(0);
      // }
    }
    console.log('filtered: ', this.tasks);
    // const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};
    //
    // const body = { date: newDate
    // };
    //
    //
    //
    // this.http.post('http://localhost:8001/tasks/due/start', body, { headers }).subscribe({
    //   next: data => {
    //     this.tasks = data;
    //     console.log(data);
    //     if (data == null) {
    //       this.theTask = undefined;
    //     }
    //   },
    //   error: error => console.error('There was an error!', error)
    // });

  }

  getTasks(): void {
    this.token = this.sessionService.getToken();

    const headers = { Authorization: 'Bearer ' + this.token
    };

    this.http.get('http://localhost:8001/tasks/', { headers }).subscribe({
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

    // console.log(this.tasks);
  }

}
