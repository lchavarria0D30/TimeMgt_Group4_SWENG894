import { Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SessionService } from 'src/app/services/session.service';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import {environment} from '../../../environments/environment';

export class Report {
  id: number;
  taskId: number;
  taskName: string;
  owner: string;
  type: string;
  difference: Date;
  scheduledDuration: Date;
  actualDuration: Date;
  efficiency: number;
  actualStartDate: string;
  actualStartTime: string;
  actualEndDate: string;
  actualEndTime: string;
}

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  reportSrc;
  displayReport = false;
  start: Date = new Date();
  end: Date = new Date();

  displayedColumns: string[] = [
    'id',
    'taskId',
    'taskName',
    'owner',
    'actualStartDate',
    'actualStartTime',
    'actualEndDate',
    'actualEndTime',
    'actualDuration',
    'scheduledDuration',
    'type',
    'difference',
    'efficiency'
  ];
  dataSource: Report[];
  constructor(
    private route: Router,
    private http: HttpClient,
    private sessionService: SessionService
  ) {}

  ngOnInit() {
    const headers = {
      Authorization: 'Bearer ' + this.sessionService.getToken(),
      'Content-Type': 'application/json'
    };
    this.http
      .get<Report[]>(environment.baseUrl+'/report/', {
        headers
      })
      .subscribe({
        next: data => {
          this.dataSource = data;
          console.log(this.reportSrc);
        },
        error: (error: Response) => console.error('There was an error!', error)
      });
  }

  /* {
    params: new HttpParams()
      .set('courseId', courseId.toString())
      .set('filter', filter)
      .set('sortOrder', sortOrder)
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString())
  }
  */
  getReport(start, end) {
    const headers = {
      Authorization: 'Bearer ' + this.sessionService.getToken(),
      'Content-Type': 'application/json'
    };
    this.http
      .get<Report[]>(
        environment.baseUrl+'/report?startDate=' +
          start +
          '&' +
          'endDate=' +
          end,
        {
          headers
        }
      )
      .subscribe({
        next: data => {
          this.dataSource = data;
          this.displayReport = true;
          console.log(this.reportSrc);
        },
        error: (error: Response) => console.error('There was an error!', error)
      });
  }

  addStart(type: string, event: MatDatepickerInputEvent<Date>) {
    this.start = event.value;
  }
  addEnd(type: string, event: MatDatepickerInputEvent<Date>) {
    if (this.start <= this.end) {
      this.end = event.value;
      this.getReport(this.start, this.end);
    }
  }
}
