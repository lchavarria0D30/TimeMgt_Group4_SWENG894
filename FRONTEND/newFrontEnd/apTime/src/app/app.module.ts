import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { AuthComponent } from "./auth/auth.component";
import { ProfileComponent } from "./profile/profile.component";
import { SecureComponent } from "./secure/secure.component";
import { HomeComponent } from "./home/home.component";
import { TasksComponent } from './tasks/tasks.component';


import { AmplifyAngularModule, AmplifyService } from "aws-amplify-angular";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
//import UI from "@aws-amplify/ui";
import { HttpClientModule } from '@angular/common/http';
import { Validators, FormBuilder, FormGroup, FormControl } from '@angular/forms';
import { CustomMaterialModule } from "./modules/material.module";


//import {MatToolbarModule} from '@angular/material/toolbar';
//import {MatIconModule} from '@angular/material/icon';
//import {MatDialogModule} from '@angular/material/dialog';
//import {MatFormFieldModule} from '@angular/material/form-field';

//import { FormsModule } from '@angular/forms';
//import {MatButtonModule} from '@angular/material/button';
//import {MatInputModule} from '@angular/material';
import {MatMenuModule} from '@angular/material/menu';
//import {MatCardModule} from '@angular/material/card';
//import {MatDividerModule} from '@angular/material/divider';
//import {MatDatepickerModule} from '@angular/material/datepicker';
//import {MatNativeDateModule} from '@angular/material';


import { FlexLayoutModule } from '@angular/flex-layout';



import { DialogOverviewExampleDialog } from './tasks/tasks.component';
import { ConfirmDeleteDialog } from './tasks/tasks.component';
import { EditTaskDialog } from './tasks/tasks.component';
import { TaskCategoryComponent } from './task-category/task-category.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthComponent,
    ProfileComponent,
    SecureComponent,
    HomeComponent,
    TasksComponent,
    DialogOverviewExampleDialog,
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
  DialogOverviewExampleDialog, 
  ConfirmDeleteDialog,
  EditTaskDialog
  ]
})
export class AppModule {}
