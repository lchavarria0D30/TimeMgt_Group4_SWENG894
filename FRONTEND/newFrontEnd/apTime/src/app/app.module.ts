import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { AuthComponent } from "./components/auth/auth.component";
import { ProfileComponent } from "./components/profile/profile.component";
import { SecureComponent } from "./components/secure/secure.component";
import { HomeComponent } from "./components/home/home.component";
import { TasksComponent } from './components/tasks/tasks.component';


import { AmplifyAngularModule, AmplifyService } from "aws-amplify-angular";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
//import UI from "@aws-amplify/ui";
import { HttpClientModule } from '@angular/common/http';
import { Validators, FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { CustomMaterialModule } from "./modules/material.module";


import {MatMenuModule} from '@angular/material/menu';


import { FlexLayoutModule } from '@angular/flex-layout';



import { CreateTaskDialog } from './components/tasks/tasks.component';
import { ConfirmDeleteDialog } from './components/tasks/tasks.component';
import { EditTaskDialog } from './components/tasks/tasks.component';
import { TaskCategoryComponent } from './components/task-category/task-category.component';


@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    ProfileComponent,
    SecureComponent,
    HomeComponent,
    TasksComponent,
    CreateTaskDialog,
    ConfirmDeleteDialog,
    EditTaskDialog,
    TaskCategoryComponent
  ],
  imports: [BrowserModule, 
  AppRoutingModule, 
  AmplifyAngularModule, 
  BrowserAnimationsModule,
  FlexLayoutModule,
  HttpClientModule,
  MatMenuModule,
  CustomMaterialModule
  ],
  providers: [AmplifyService,
  CustomMaterialModule
  ],
  bootstrap: [AppComponent],
  entryComponents: [
  CreateTaskDialog, 
  ConfirmDeleteDialog,
  EditTaskDialog
  ]
})
export class AppModule {}
