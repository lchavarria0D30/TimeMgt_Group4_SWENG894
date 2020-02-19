import { Component, OnInit } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { Category } from "./Category";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Location } from "@angular/common";
import { CategoryService } from "../services/category.service";

@Component({
  selector: "app-task-category",
  templateUrl: "./task-category.component.html",
  styleUrls: ["./task-category.component.css"],
  providers: [Location]
})
export class TaskCategoryComponent implements OnInit {
  public categoryForm: FormGroup;
  cat: Category;
  status: string;

  constructor(private location: Location, private catSevice: CategoryService) {}

  ngOnInit() {
    this.categoryForm = new FormGroup({
      name: new FormControl("", [
        Validators.required,
        Validators.maxLength(80)
      ]),
      description: new FormControl("", [
        Validators.required,
        Validators.maxLength(100)
      ])
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
      description: categoryFormValue.description
    };

    let apiUrl = "localhost/8001/category";
    this.catSevice.create(apiUrl, this.cat).subscribe(
      res => {
        this.status = "success";
      },
      error => {
        this.status = "failed";
      }
    );
  };
}
