/**
 *
 * Author: Yanisse
 * Jira Task: TMGP4-185
 * Description: The component code for the home page.
 *
 **/

import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SessionService } from '../../services/session.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  tasks;
  token;
  activeTasks;
  pausedTasks;
  toDoTasks;
  completedTasks;
  constructor(private http: HttpClient,
              private sessionService: SessionService ) { }

  ngOnInit() {
    this.getTasks();


  }

  getTasks(): void {
    this.token = this.sessionService.getToken();

    const headers = { Authorization: 'Bearer ' + this.token
    };

    this.http.get('http://localhost:8001/tasks/', { headers }).subscribe({
      next: data => {
        this.tasks = data
        if (this.tasks != undefined) {

          this.activeTasks = this.tasks.filter(
              function (tasks) {
                return tasks.state == 'ACTIVE';
              }
          )

          this.pausedTasks = this.tasks.filter(
              function (tasks) {
                return tasks.state == 'PAUSED';
              }
          )

          this.toDoTasks = this.tasks.filter(
              function (tasks) {
                return tasks.state == 'CREATED';
              }
          )

          this.completedTasks = this.tasks.filter(
              function (tasks) {
                return tasks.state == 'COMPLETED';
              }
          )

        }
      },
      error: error => console.error('There was an error!', error)
    });

    // console.log(this.tasks);
  }
}
