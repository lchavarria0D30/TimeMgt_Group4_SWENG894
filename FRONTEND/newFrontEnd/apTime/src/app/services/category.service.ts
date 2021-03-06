/**
 *
 * Author: Asma
 * Jira Task:
 * Description:
 *
 **/

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Category } from '../components/task-category/Category';
import {Auth} from "aws-amplify";
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  constructor(private http: HttpClient) {}

  public create(url, category: Category) {
    return this.http.put(url, category);
  }

  public getCategories(token: string) {

    const headers = { Authorization: 'Bearer ' + token
    };

    return this.http.get(environment.baseUrl+'/category/', { headers });
  }

  public getAvgCategories(token: string) {

    const headers = { Authorization: 'Bearer ' + token
    };

    return this.http.get(environment.baseUrl+'/summary/mine', { headers });
  }

}
