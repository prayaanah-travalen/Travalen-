import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import { LoginComponent } from './login/login.component';
import { environment } from 'src/environments/environment.prod';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {

  adminUrl: string = environment.adminUrl;

  constructor(private dialog: Dialog,
    private router: Router) { }

  ngOnInit(): void {
  }

  login() {
    if(this.isLoginRequired() === false) {
      this.router.navigateByUrl("/home");
    } else {
      this.dialog.open(LoginComponent,{ 
        width: '395px',
        // height:'678px',
        // position:{
        //   right:'65px',
        //   bottom:'0px'
        // }
      
      });
    }
   
  }

  openAdmin() {
    window.open(this.adminUrl,"_blank")
  }

  isLoginRequired(){
    let jwt = localStorage.getItem("auth");
    if(jwt !== null) {
      let jwtData = jwt.split('.')[1]
      let decodedJwtJsonData = window.atob(jwtData)
      let decodedJwtData = JSON.parse(decodedJwtJsonData)

      if(Date.now() < decodedJwtData.exp * 1000) {
        return false;
      }
    }
    return true;
  }
}
