/**
 *
 * Author(s): Asma/Yanisse
 * Jira Task: N/A
 * Description: The angular module that imports all the necessary modules from the Angular Material library.
 *
 **/

import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule } from '@angular/common/http';


import {
  MatNativeDateModule,
  MatSnackBarModule,
  MatIconModule,
  MatDialogModule,
  MatButtonModule,
  MatTableModule,
  MatPaginatorModule,
  MatSortModule,
  MatTabsModule,
  MatCheckboxModule,
  MatToolbarModule,
  MatCard,
  MatCardModule,
  MatFormField,
  MatFormFieldModule,
  MatMenuModule,
  MatListModule,
  MatDividerModule,
  MatDatepickerModule,
  MatProgressSpinnerModule,
  MatInputModule
} from '@angular/material';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSliderModule } from '@angular/material/slider';
import { Location } from '@angular/common';


@NgModule({
  imports: [
    MatTabsModule,
    MatDividerModule,
    MatSliderModule,
    MatSelectModule,
    MatRadioModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatSnackBarModule,
    MatIconModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatCheckboxModule,
    MatToolbarModule,
    MatCardModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    MatInputModule,
    MatMenuModule,
    MatDividerModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatPaginatorModule,
    FormsModule,
    HttpClientModule
  ],
  exports: [
    MatTabsModule,
    MatDividerModule,
    MatSliderModule,
    MatSelectModule,
    MatRadioModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatSnackBarModule,
    MatIconModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    MatListModule,
    MatSortModule,
    MatCheckboxModule,
    MatToolbarModule,
    MatCardModule,
    MatTableModule,
    MatTabsModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    MatInputModule,
    MatPaginatorModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ]
})
export class CustomMaterialModule {}
