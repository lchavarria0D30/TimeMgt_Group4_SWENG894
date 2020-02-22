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
  headers = {
    Authorization: 'Bearer'
  };
  token: string = '';
  constructor(
    private location: Location,
    private catSevice: CategoryService,
    private amplify: AmplifyService,
    private http: HttpClient,
    private sessionService: SessionService
  ) {
    this.amplifyService = amplify;
    this.amplifyService.authStateChange$.subscribe(authState => {
      this.canCreateTask = authState.state === 'signedIn';
    });
  }

  ngOnInit() {
    this.categoryForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(80)])
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
      name: categoryFormValue.name
      // description: categoryFormValue.description
    };
    let jtoken = async () =>
      (await Auth.currentSession()).getIdToken().getJwtToken();

    console.log(jtoken);
    let apiUrl = 'http://localhost/8001/category';
    this.createCat(this.cat);
  };
  //create Category
  public createCat = body => {
    const headers = {
      Authorization: 'Bearer ' + this.sessionService.getToken()
    };

    this.http
      .post('http://localhost:8001/category', body, { headers })
      .subscribe({
        next: data => console.log(data),
        error: error => console.error('There was an error!', error)
      });
  };
}
