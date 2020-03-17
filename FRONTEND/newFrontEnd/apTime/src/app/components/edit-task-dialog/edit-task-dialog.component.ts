import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {HttpClient} from '@angular/common/http';
import {SessionService} from '../../services/session.service';
import {DialogData} from '../tasks/tasks.component';

@Component({
  selector: 'app-edit-task-dialog',
  templateUrl: './edit-task-dialog.component.html',
  styleUrls: ['./edit-task-dialog.component.css']
})
export class EditTaskDialogComponent implements OnInit {

  id: number;
  task: any;
  showActuals: boolean;

  scheduledStart;
  scheduledEnd;
  actualStart;
  actualEnd;


  constructor(
      public dialogRef: MatDialogRef<EditTaskDialogComponent>,
      private http: HttpClient,
      private sessionService: SessionService,
      @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick(): void {

    this.dialogRef.close();
  }

  onYesClick(): void {

    this.scheduledStart = this.dateConversion(this.task.ssTime, this.task.ssDate);
    this.scheduledEnd = this.dateConversion(this.task.seTime, this.task.seDate);
    this.actualStart = this.dateConversion(this.task.asTime, this.task.asDate);
    this.actualEnd = this.dateConversion(this.task.aeTime, this.task.aeDate);

    const headers = { Authorization: 'Bearer ' + this.sessionService.getToken()};

    const body = {
      id: this.task.id,
      name: this.task.name,
      description: this.task.description,
      category: this.task.category,
      scheduledstart: this.scheduledStart,
      scheduledend: this.scheduledEnd,
      actualstart: this.actualStart,
      actualend: this.actualEnd

    };

    console.log('BEFORE put');
    this.http.put('http://localhost:8001/tasks/task', body, { headers }).subscribe({
      next: data => console.log(data),
      error: error => console.error('There was an error!', error)
    });


    this.dialogRef.close();
  }

  onActualsClick(): void {
    this.showActuals = true;
    console.log('Actual click');
  }

  dateConversion(time: string, date: Date): Date {
     const tempDate = date;
     const tempTime = time;

     const parts = tempTime.match(/(\d+):(\d+) (AM|PM)/);
     if (parts) {
      let hours = parseInt(parts[1], 10);
      const  minutes = parseInt(parts[2], 10);
      const tt = parts[3];
      if (tt === 'PM' && hours < 12) { hours += 12; }
      date.setHours(hours, minutes, 0, 0);
    }

     return tempDate;
  }


  ngOnInit() {
    this.showActuals = false;

    this.id = this.data.id;

    this.task = this.data.task;
    // let date = new Date(this.task.scheduledstart.substring(0, this.task.scheduledstart.length - 5));
    //
    // this.task.scheduledstart = new Date(this.task.scheduledstart.substring(0, this.task.scheduledstart.length - 5));
    // this.task.ssTime = date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
    setTimeout(() => {
      let date = this.task.scheduledstart.substring(0, this.task.scheduledstart.length - 5);
      let ssDate = new Date(date);
      let ssTime = ssDate.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
      this.task.ssTime = ssTime;
      this.task.scheduledstart = ssDate;
    });

    console.log('The task: ', this.task);

  }

}

