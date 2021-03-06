/**
 *
 * Author: Asma
 * Jira Task:
 * Description:
 *
 */

import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Category } from './Category';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { CategoryService } from '../../services/category.service';
import { HttpClient } from '@angular/common/http';
import { AmplifyService } from 'aws-amplify-angular';
import { HttpHeaders } from '@angular/common/http';
import Auth from '@aws-amplify/auth';
import { SessionService } from '../../services/session.service';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { FlexAlignStyleBuilder } from '@angular/flex-layout';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import {environment} from '../../../environments/environment';

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
  name;
  headers = {
    Authorization: 'Bearer'
  };
  token = '';

  categories = [];
  tabIndex = 0;

  publicCats = [];
  privateCats = [];

  statsCats = [];

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

    this.getCategories();

  }

  public hasError = (controlName: string, errorName: string) => {
    return this.categoryForm.controls[controlName].hasError(errorName);
  }

  public onCancel = () => {
    this.name = '';
    this.isPublic = false;
    // this.location.back();
  }

  public createCategory = categoryFormValue => {
    if (this.categoryForm.valid) {
      this.createCategoryOb(categoryFormValue);
    }
  }

  private getCategories() {
    this.catSevice.getCategories(this.sessionService.getToken()).subscribe({
      next: data => {
        this.categories = [];
        this.categories = this.categories.concat(data);

        this.categories.sort((a, b) => {
          if (a.name > b.name) { return 1; }
          if (a.name < b.name) { return -1; }
          return 0;
        });

        this.publicCats = this.categories.filter(
            cats => cats.public === true
        );

        this.privateCats = this.categories.filter(
            cats => cats.public === false
        );
      },
      error: error => {
        console.error('There was an error!', error);
      }
    });
  }

  private createCategoryOb = categoryFormValue => {
    this.cat = {
      name: categoryFormValue.name,
      isPublic: this.isPublic
    };

    this.createCat();
  }
  // create Category
  public createCat = function() {
    const headers = {
      Authorization: 'Bearer ' + this.sessionService.getToken(),
      'Content-Type': 'application/json'
    };
    if (this.cat.isPublic) {
      this.http
        .post(
          environment.baseUrl+'/category/public',
          { name: this.cat.name },
          { headers }
        )
        .subscribe({
          next: data => {
            console.log(data);
            this.openSnackBar('Category Created', 'redirecting to categories');
            this.getCategories();
            this.tabIndex = 0;
            this.name = '';
            this.isPublic = false;
            // this.router.navigate(['/']);
            // this.location.go('/');
          },
          error: error => {
            console.error('There was an error!', error);
            this.openSnackBar(
              'Error occured while creating category',
              'Try again!'
            );
          }
        });
    } else {
      this.http
        .post(
          environment.baseUrl+'/category',
          { name: this.cat.name },
          { headers }
        )
        .subscribe({
          next: data => {
            // console.log(data);
            this.openSnackBar('Category Created', 'redirecting to categories');
            this.getCategories();
            this.tabIndex = 0;
            this.name = '';
            this.isPublic = false;
            // this.router.navigate(['/']);
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

  onStats(): void {
    if (this.tabIndex === 2) {
      this.catSevice.getAvgCategories(this.sessionService.getToken()).subscribe({
        next: data => {
          this.statsCats = [];
          this.statsCats = this.statsCats.concat(data);

          for (let i in this.categories) {
            this.categories[i].avg = this.findCat(this.categories[i].id);
          }
        },
        error: error => console.error('There was an error!', error)
      });

    }
  }

  findCat(i: number): object {
    let found = this.statsCats.find(element => element.category.id === i);

    if (found === undefined) {
      found = {};
      found.summaryForAllUsers = {averageDuration: 'Not Sufficient Data'};
      found.summaryForCurrentUser = {averageDuration: 'Not Sufficient Data'};
    } else {
      if (found.summaryForAllUsers == null) {
        found.summaryForAllUsers = {averageDuration: 'Not Sufficient Data'};
      }

      if (found.summaryForCurrentUser == null) {
        found.summaryForCurrentUser = {averageDuration: 'Not Sufficient Data'};
      }
    }

    return found;
  }
}
