import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Category } from './Category';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { CategoryService } from '../services/category.service';
import { HttpClient } from '@angular/common/http';
import { AmplifyService } from 'aws-amplify-angular';
import { HttpHeaders } from '@angular/common/http';
import Auth from '@aws-amplify/auth';
import { SessionService } from '../services/session.service';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { FlexAlignStyleBuilder } from '@angular/flex-layout';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
@Component({
  selector: 'app-task-category',
  templateUrl: './task-category.component.html',
  styleUrls: ['./task-category.component.css'],
  providers: [Location]
})
export class TaskCategoryComponent implements OnInit {
  public categoryForm: FormGroup;
  cat: Category;
  status: string;
  amplifyService: any;
  canCreateTask = false;
  isPublic = false;
  headers = {
    Authorization: 'Bearer'
  };
  token: string = '';
  constructor(
    private location: Location,
    private catSevice: CategoryService,
    private amplify: AmplifyService,
    private http: HttpClient,
    private sessionService: SessionService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.amplifyService = amplify;
    this.amplifyService.authStateChange$.subscribe(authState => {
      this.canCreateTask = authState.state === 'signedIn';
    });
  }

  ngOnInit() {
    this.categoryForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.maxLength(80)
      ]),
      isPublic: new FormControl(false, [])
    });
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.categoryForm.controls[controlName].hasError(errorName);
  };

  public onCancel = () => {
    this.location.back();
  };

  public createCategory = categoryFormValue => {
    if (this.categoryForm.valid) {
      this.createCategoryOb(categoryFormValue);
    }
  };

  private createCategoryOb = categoryFormValue => {
    this.cat = {
      name: categoryFormValue.name,
      isPublic: this.isPublic
    };
    console.log(this.cat.isPublic);

    this.createCat();
  };
  //create Category
  public createCat = function() {
    const headers = {
      Authorization: 'Bearer ' + this.sessionService.getToken(),
      'Content-Type': 'application/json'
    };
    if (this.cat.isPublic) {
      console.log(this.cat.isPublic);
      this.http
        .post(
          'http://localhost:8001/category/public',
          { name: this.cat.name },
          { headers }
        )
        .subscribe({
          next: data => {
            console.log(data);
            this.openSnackBar('Category Created', 'redirecting to home');
            this.router.navigate(['/']);
            // this.location.go('/');
          },
          error: error => {
            console.error('There was an error!', error);
            this.openSnackBar(
              'Error occured while  creating category',
              'Try again!'
            );
          }
        });
    } else {
      this.http
        .post(
          'http://localhost:8001/category',
          { name: this.cat.name },
          { headers }
        )
        .subscribe({
          next: data => {
            console.log(data);
            this.openSnackBar('Category Created', 'redirecting to home');
            this.router.navigate(['/']);
            // this.location.go('/');
          },
          error: error => console.error('There was an error!', error)
        });
    }
  };
  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000
    });
  }
}
