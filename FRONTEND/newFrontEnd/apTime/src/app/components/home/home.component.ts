/**
 *
 * Author: Yanisse
 * Jira Task: TMGP4-185
 * Description: The component code for the home page.
 *
 **/

import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SessionService } from '../../services/session.service';
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
 ngOnInit() {

 }
}
