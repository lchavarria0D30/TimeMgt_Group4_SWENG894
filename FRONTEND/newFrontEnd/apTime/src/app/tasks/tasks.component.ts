import { Component, OnInit, Inject } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { SessionService } from "../services/session.service";


export interface DialogData {
  name: string;
  category: string;
  ssDate: Date;
  ssTime: string;
  seDate: string;
  seTime: string;
  asDate: Date;
  asTime: string;
  aeDate: Date;
  aeTime: string;
  number: string;
  id: number;
}

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  
  name: string;
  category: string;
  sDate: Date;
  sTime: string;
  eDate: Date;
  eTime: string;
  number: string;
  id: number;
  token: string;

  ngOnInit() {
    this.token = this.sessionService.getToken();
    console.log("EL token: " + this.token)

    const headers = { 'Authorization': 'Bearer ' + this.token }
    this.http.get<any>('localhost:8001/', { headers }).subscribe({
    next: data => this.name = data.id,
    error: error => console.error('There was an error!', error)
    })
  }

  //number = "1";
  constructor(public dialog: MatDialog, private http: HttpClient,
  private sessionService: SessionService ) {}

  openDialog(): void {
    const dialogRef = this.dialog.open(DialogOverviewExampleDialog, {
      //width: '250px',
      data: {
      name: this.name, 
      category: this.category,
      sDate: this.sDate,
      sTime: this.sTime,
      eDate: this.eDate,
      eTime: this.eTime
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The create dialog was closed');
      console.log(result);
      
    });
  }

  openDeleteDialog(i: number): void {
    
    console.log();
    const dialogRef = this.dialog.open(
    ConfirmDeleteDialog, {
      //width: '250px',
      data: {
      id: i
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The delete dialog was closed');
      //this.animal = result;
    });
  }

  openEditDialog(i: number): void {
    console.log("The index: ", i);
    const dialogRef = this.dialog.open(
    EditTaskDialog, {
      //width: '250px',
      data: {
      id: i
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The edit dialog was closed');
      console.log(result);
      //this.animal = result;
    });
  }



}

@Component({
  selector: 'dialog-overview-example-dialog',
  templateUrl: 'dialog-overview-example-dialog.html',
  styleUrls: ['./tasks.component.css']
})
export class DialogOverviewExampleDialog {


  constructor(
    public dialogRef: MatDialogRef<DialogOverviewExampleDialog>,
    private http: HttpClient,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  onYesClick(): void {
  
    this.dateConversion(this.data.ssTime, this.data.ssDate)
    this.dateConversion(this.data.aeTime, this.data.aeDate)
    
    const headers = { 'Authorization': 'Bearer my-token' }
    const body = { name: this.data.name,
    start: this.data.ssDate
     }
    this.http.post<any>('localhost:8000/tasks', body, { headers }).subscribe({
    next: data => data,
    error: error => console.error('There was an error!', error)
    })


    this.dialogRef.close();
  }

  dateConversion(time: string, date: Date) : Date {
    
    var parts = time.match(/(\d+):(\d+) (AM|PM)/);
    if (parts) {
        var hours = parseInt(parts[1]),
            minutes = parseInt(parts[2]),
            tt = parts[3];
        if (tt === 'PM' && hours < 12) hours += 12;
        date.setHours(hours, minutes, 0, 0);
    }

    return date;
  }

  ngOnInit() {
  }

}

@Component({
  selector: 'confirm-delete-dialog',
  templateUrl: 'confirm-delete-dialog.html',
  styleUrls: ['./tasks.component.css']
})
export class ConfirmDeleteDialog {
  id;
  number;
  name;


  constructor(
    public dialogRef: MatDialogRef<ConfirmDeleteDialog>,
    private http: HttpClient,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {data: {
    }}

  onNoClick(): void {
    const headers = { 'Authorization': 'Bearer my-token', 'My-Custom-Header': 'foobar' }
    const body = { title: 'Angular POST Request Example' }
    this.http.post<any>('https://jsonplaceholder.typicode.com/invalid-url', body, { headers }).subscribe({
    next: data => this.name = data.id,
    error: error => console.error('There was an error!', error)
    })

    this.dialogRef.close();
  }

  onYesClick(): void {
    this.dialogRef.close();
  }

  ngOnInit() {
    this.id = this.data.id;
  }

}


@Component({
  selector: 'edit-task-dialog',
  templateUrl: 'edit-task-dialog.html',
  styleUrls: ['./tasks.component.css']
})
export class EditTaskDialog {

    id: number;
    name;
    start;
    duration;
    showActuals: boolean;


  constructor(
    public dialogRef: MatDialogRef<EditTaskDialog>,
    private http: HttpClient,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {data: {

    }}

  onNoClick(): void {

    this.dialogRef.close();
  }

  onYesClick(): void {
    this.dialogRef.close();
  }

  onActualsClick(): void {
    this.showActuals = true;
    console.log("Actual click");
  }

  ngOnInit() {
    this.showActuals = false;
    this.id = this.data.id;
    console.log("The id: ", this.data.id);

    const headers = { 'Authorization': 'Bearer my-token'}
      const body = { title: 'Angular POST Request Example' 

      }
      this.http.get<any>('localhost:8001/tasks/' + this.id, { headers }).subscribe({
      next: data => {
        this.name = data.name, 
        this.start = data.start,
        this.duration = data.duration
      },
      error: error => console.error('There was an error!', error)
      })

      console.log("The id: ", this.data);

  }

}
