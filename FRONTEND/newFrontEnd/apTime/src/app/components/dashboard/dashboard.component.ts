import { Component, OnInit } from '@angular/core';
import { SessionService } from '../../services/session.service';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  tasks;
  token;
  theTask;

  constructor(private http: HttpClient,
              private sessionService: SessionService) { }

  ngOnInit() {
    this.getTasks();
  }

  selectedTask(i: number): void {
    this.theTask = this.tasks[i];
  }


  getTasks(): void {
    this.token = this.sessionService.getToken();

    const headers = { Authorization: 'Bearer ' + this.token
    };

    this.http.get('http://localhost:8001/tasks/', { headers }).subscribe({
      next: data => {
        this.tasks = data;
        // console.log(this.tasks);
        this.theTask = this.tasks[0];
      },
      error: error => console.error('There was an error!', error)
    });

  }

  startTask(i: number): void {
    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};

    const body = { id: i
    };

    this.http.post('http://localhost:8001/tasks/start', body, { headers }).subscribe({
      next: data => console.log(data),
      error: error => console.error('There was an error!', error)
    });
  }

}
