import { DialogRef } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import { NonNullableFormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/data-access/services/account.service';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.scss']
})
export class ForgetPasswordComponent implements OnInit {

  showOtp = false;
  showemail: boolean = true;
  showPasswordSec: boolean = false;

  
  showPass: boolean = false;
  showConfirmPass: boolean = false;

  userForm = this.fb.group({
    email: ["",[Validators.email]],
    password: [""],
    confirmPassword:[""]
  })
  error: any;
  otp: string = '';
  otpError: string = '';

  constructor( private accountService: AccountService,
    private fb: NonNullableFormBuilder,
    private router: Router,
    private dialogRef: DialogRef,) { }

  ngOnInit(): void {
  }

  showOtpSec() {
    this.showOtp = true;
    this.showemail = false;
    this.showPasswordSec = false;
  }

  
  showPassSec() {
    this.showOtp = false;
    this.showemail = false;
    this.showPasswordSec = true;
  }

  sendOtp() {
    if(this.userForm.get('email')?.getRawValue() === "") {
      this.userForm.controls['email'].setErrors({ 'required': true });
    }

    if(!this.userForm.valid) {
      return false;
    }

    let req= {
      emailId: this.userForm.get('email')?.getRawValue()
    }

    this.accountService.sendOtp(req).subscribe(resp=>{
      if(resp.status === 'SUCCESS') {
        this.showOtpSec();
      } else {
        this.error = resp.message;
      }
    });

    return true;
  }

  onOtpChange(event: any) {
    this.otp = event;
  }

  verifyOtp() {
    this.otpError = '';
    if(this.otp === '') {
      this.otpError = "Please enter valid otp";
    }
    let req = {
      emailId: this.userForm.get('email')?.getRawValue(),
      otp:  this.otp
    }

    this.accountService.forgotPassOtpVerify(req).subscribe(resp=>{
        if(resp.status === 'SUCCESS') {
         this.showPassSec();
        } else {
          this.otpError = resp.message;
        }
    })
  }

  submit() {
    this.error = '';
    if(this.userForm.get('password')?.getRawValue() === "") {
      this.userForm.controls['password'].setErrors({ 'required': null });
    }

    if(this.userForm.get('password')?.getRawValue() !== this.userForm.get('confirmPassword')?.getRawValue()) {
      this.userForm.controls['confirmPassword'].setErrors({ 'required': null });
      // this.userForm.controls['password'].setErrors({ 'required': null });
    }

    if(!this.userForm.valid) {
      return false;
    }
    
    let req= {
      emailId: this.userForm.get('email')?.getRawValue(),
      password: this.userForm.get('password')?.getRawValue()
    }

    this.accountService.chamgePassword(req).subscribe(resp=>{
      if(resp.status === 'SUCCESS') {
        localStorage.setItem("auth", resp.jwt);
        this.dialogRef.close();
        this.router.navigateByUrl("/travalen/dashboard");
      } 
    });
   
    return true;
  }

}
