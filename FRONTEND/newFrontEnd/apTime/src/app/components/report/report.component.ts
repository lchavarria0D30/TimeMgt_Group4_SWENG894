import { Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SessionService } from 'src/app/services/session.service';


export class Report {
  id: number;
  taskId: number;
  owner: string;
  type: string;
  difference: Date;
  scheduledDuration: Date;
  actualDuration: Date;
  efficiency: number;
}


@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  reportSrc;
  displayedColumns: string[] = [
    'id',
    'taskId',
    'owner',
    'type',
    'difference',
    'scheduledDuration',
    'actualDuration',
    'efficiency'
  ];
  dataSource: Report[];
  constructor(
    private route: Router,
    private _http: HttpClient,
    private sessionService: SessionService
  ) {}

  ngOnInit() {
    const headers = {
      Authorization: 'Bearer ' + this.sessionService.getToken(),
      'Content-Type': 'application/json'
    };
    this._http
      .get<Report[]>('http://localhost:8001/report/', {
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
  getReport() {
    const headers = {
      Authorization: 'Bearer ' + this.sessionService.getToken(),
      'Content-Type': 'application/json'
    };
    this._http
      .get<Report[]>('http://localhost:8001/report/', {
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
}
