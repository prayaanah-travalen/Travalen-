import { Dialog, DialogRef } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import { NonNullableFormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from 'src/app/data-access/services/account.service';
import { CreateAccountComponent } from '../create-account/create-account.component';
import { ForgetPasswordComponent } from '../forget-password/forget-password.component';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {
  userForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });
  error: any;
  showPass: boolean = false;

  showPasswordSec: boolean = false;
  showEmailSec: boolean = true;
  constructor( private fb: NonNullableFormBuilder,
    private accountService: AccountService,
    private dialogRef: DialogRef,
    private router: Router,
    private dialog: Dialog) { }

  ngOnInit(): void {
  }

  showPasswordSection() {
    if (this.userForm.controls['email'].valid) {
      this.showPasswordSec = true;
      this.showEmailSec = false;
    } else {
      this.userForm.controls['email'].markAsTouched();
    }
  }

  signIn() {
    // Mark all fields as touched to trigger validation
    Object.keys(this.userForm.controls).forEach(key => {
      const control = this.userForm.get(key);
      control?.markAsTouched();
    });

    if(!this.userForm.valid) {
      return false;
    }

    let req = {
      emailId: this.userForm.get('email')?.getRawValue(),
      password: this.userForm.get('password')?.getRawValue()
    }

    this.accountService.signIn(req).subscribe(resp=>{
      if(resp.status === 'SUCCESS') {
        localStorage.setItem("auth", resp.jwt);
        this.dialogRef.close();
        this.router.navigateByUrl("/travalen/dashboard");
      } else {
        this.error = resp.message;
      }
    });

    return true;
  }

  createAccount() {
    this.dialogRef.close();
    this.dialog.open(CreateAccountComponent);
  }
  forgotPassword() {
    this.dialogRef.close();
    this.dialog.open(ForgetPasswordComponent)
  }

}
