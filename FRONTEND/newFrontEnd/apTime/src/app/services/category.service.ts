import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Category } from "../task-category/Category";

@Injectable({
  providedIn: "root"
})
export class CategoryService {
  constructor(private http: HttpClient) {}

  public create(url, category: Category) {
    return this.http.put(url, category);
  }
}
