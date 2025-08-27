import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CreateAccountComponent } from '../create-account/create-account.component';
import { SignInComponent } from '../sign-in/sign-in.component';


@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {

  constructor(private router:Router,
    private dialog: Dialog) { }

  ngOnInit(): void {
  }
 
  createAccount() {
    this.dialog.open(CreateAccountComponent,{width:"890px", height:"670px"})
  }

  signIn() {
    this.dialog.open(SignInComponent)
  }
}
