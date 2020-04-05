import { Component, OnInit } from '@angular/core';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { MatTableModule } from '@angular/material/table';
import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SessionService } from 'src/app/services/session.service';
import { FormControl } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
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
  displayReport = false;
  start: Date = new Date();
  end: Date = new Date();
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
    /*     const headers = {
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
      }); */
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

    //localhost:8001/
    http: this._http
      .get<Report[]>(
        'http://localhost:8001/report?startDate=' +
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
          //console.log(this.reportSrc);
          this.displayReport = true;
        },
        error: (error: Response) => console.error('There was an error!', error)
      });
  }

  addStart(type: string, event: MatDatepickerInputEvent<Date>) {
    this.start = event.value;
    console.log(' add event is called. .....' + this.start);
  }

  addEnd(type: string, event: MatDatepickerInputEvent<Date>) {
    if (this.start <= this.end) {
      this.end = event.value;
      this.getReport(this.start, this.end);
      console.log(
        ' start is  ' + this.start + '. .....' + '  end  is ' + this.end
      );
    }
  }
}
