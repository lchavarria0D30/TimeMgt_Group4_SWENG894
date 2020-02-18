import { Component, OnInit, Inject } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { Auth } from 'aws-amplify';
import { HttpClient } from '@angular/common/http';


export interface DialogData {
  name: string;
  category: string;
  sDate: string;
  sTime: string;
  eDate: string;
  eTime: string;
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
  sDate: string;
  sTime: string;
  eDate: string;
  eTime: string;
  number: string;
  id: number;

  ngOnInit() {
    const headers = { 'Authorization': 'Bearer my-token', 'My-Custom-Header': 'foobar' }
    const body = { title: 'Angular POST Request Example' }
    this.http.get<any>('localhost:8001/tasks', { headers }).subscribe({
    next: data => this.name = data.id,
    error: error => console.error('There was an error!', error)
    })
  }

  //number = "1";
  constructor(public dialog: MatDialog, private http: HttpClient) {}

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
      console.log('The dialog was closed');
      console.log(result);

      const headers = { 'Authorization': 'Bearer my-token', 'My-Custom-Header': 'foobar' }
      const body = { title: 'Angular POST Request Example' 

      }
      this.http.post<any>('https://jsonplaceholder.typicode.com/invalid-url', body, { headers }).subscribe({
      next: data => this.name = data.id,
      error: error => console.error('There was an error!', error)
      })
      
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
      console.log('The dialog was closed');
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
      console.log('The dialog was closed');
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
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick(): void {
    this.dialogRef.close();
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

  ngOnInit() {
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
  }

}


