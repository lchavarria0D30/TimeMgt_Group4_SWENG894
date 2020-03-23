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


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  tasks;
  token;
  theTask;

  date = new FormControl(new Date());

  constructor(private http: HttpClient,
              private sessionService: SessionService) { }

  ngOnInit() {
    this.getTasks();
    this.getDateTasks();
  }

  selectedTask(i: number): void {
    this.theTask = this.tasks[i];
  }

  getDateTasks(): void {

    let fromDate = new Date(this.date.value);
    let toDate = new Date(this.date.value);

    fromDate.setHours(0,0,0,0);

    toDate.setDate(toDate.getDate() + 1);
    toDate.setHours(0,0,0,0);

    console.log(fromDate.toISOString());
    console.log(toDate.toISOString());

    console.log('Date changed:');
    console.log(this.date.value.toISOString().substring(0,10));
    console.log()
    let newDate = this.date.value.toISOString().substring(0,10);

    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};

    const body = { date: newDate
    };


    this.http.post('http://localhost:8001/tasks/due/start', body, { headers }).subscribe({
      next: data => console.log(data),
      error: error => console.error('There was an error!', error)
    });

  }


  getTasks(): void {
    this.token = this.sessionService.getToken();

    const headers = { Authorization: 'Bearer ' + this.token
    };

    this.http.get('http://localhost:8001/tasks/', { headers }).subscribe({
      next: data => {
        this.tasks = data;
        console.log("the tasks: ", this.tasks);
      },
      error: error => console.error('There was an error!', error)
    });

  }

}
