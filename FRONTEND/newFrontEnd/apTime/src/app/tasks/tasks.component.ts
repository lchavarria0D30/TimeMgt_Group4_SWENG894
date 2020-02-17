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

  ngOnInit() {
    const headers = { 'Authorization': 'Bearer my-token', 'My-Custom-Header': 'foobar' }
    const body = { title: 'Angular POST Request Example' }
    this.http.get<any>('localhost:8001/tasks', { headers }).subscribe({
    next: data => name = data.id,
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
      this.name = result;
    });

  }

  openDeleteDialog(): void {
    const dialogRef = this.dialog.open(
    ConfirmDeleteDialog, {
      //width: '250px',
      data: {
      number: this.number
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      //this.animal = result;
    });
  }

  openEditDialog(): void {
    const dialogRef = this.dialog.open(
    EditTaskDialog, {
      //width: '250px',
      data: {
      number: this.number
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
  postId;


  constructor(
    public dialogRef: MatDialogRef<ConfirmDeleteDialog>,
    private http: HttpClient,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {data: {
      number : this.number
    }}

  onNoClick(): void {
    const headers = { 'Authorization': 'Bearer my-token', 'My-Custom-Header': 'foobar' }
    const body = { title: 'Angular POST Request Example' }
    this.http.post<any>('https://jsonplaceholder.typicode.com/invalid-url', body, { headers }).subscribe({
    next: data => name = data.id,
    error: error => console.error('There was an error!', error)
    })

    this.dialogRef.close();
  }

  onYesClick(): void {
    this.dialogRef.close();
  }

  ngOnInit() {
  }

}


@Component({
  selector: 'edit-task-dialog',
  templateUrl: 'edit-task-dialog.html',
  styleUrls: ['./tasks.component.css']
})
export class EditTaskDialog {

    id: string;
    title: string;


  constructor(
    public dialogRef: MatDialogRef<EditTaskDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {data: {

    }}

  onNoClick(): void {

    this.dialogRef.close();
  }

  onYesClick(): void {
    this.dialogRef.close();
  }

  ngOnInit() {
  }

}


