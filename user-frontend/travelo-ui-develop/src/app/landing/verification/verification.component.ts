import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { NonNullableFormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/data-access/services/login.service';

@Component({
  selector: 'app-verification',
  templateUrl: './verification.component.html',
  styleUrls: ['./verification.component.scss']
})
export class VerificationComponent implements OnInit {
  otp: string = '';
  error: any;

  constructor(private router: Router, private dialog: DialogRef,
    @Inject(DIALOG_DATA) public data: { phoneNo: string, fname: string, lname: string},
    private loginService: LoginService,
    private fb: NonNullableFormBuilder,) { }

  ngOnInit(): void {
  }

  verifyOtp() {
    if(this.otp.length === 6) {
        let req = {
          otp: this.otp,
          phoneNo: this.data.phoneNo,
          firstName: this.data.fname,
          lastName: this.data.lname
        }
        this.loginService.verifyOtp(req)
        .subscribe(res=>{
          if(res.status === 'success') {
            localStorage.setItem("auth", res.jwt);
            this.dialog.close();
            this.router.navigateByUrl("/home");
          } else if(res.status === "failed") {
            this.error = res.message;
          }
         
        })
    }
  }

  onOtpChange(event: any) {
    this.otp = event;
  }
}
