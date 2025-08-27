import { Dialog, DialogRef } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import { VerificationComponent } from '../verification/verification.component';
import { NonNullableFormBuilder, Validators } from '@angular/forms';
import { LoginService } from 'src/app/data-access/services/login.service';
import { Router } from '@angular/router';
import { CountryCode, CountryCodes } from 'src/app/data-access/models/country-code.model';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
	loginForm = this.fb.group({
		fname: [""],
		lname: [""],
		phoneNo:[""],
		countryCode:['+91']
	});

	otp: string = '';
	error: any;
	showOtpPage: boolean = false;
	countryCodes: Array<CountryCode> = CountryCodes;
	phone: string='';
	login: boolean = false;
  

	constructor(private dialogRef: DialogRef,
		private dialog: Dialog,
		private fb: NonNullableFormBuilder,
		private loginService: LoginService,
		private router: Router,) { }

	ngOnInit(): void {
		localStorage.clear();
	}

	close(){
		this.dialogRef.close();
	}

	sendOtp() {
		this.error = '';
		if(this.loginForm.get('phoneNo')?.getRawValue() === '') {
			this.loginForm.controls['phoneNo'].setErrors({ 'required': null })
		}
		if(this.loginForm.get('countryCode')?.getRawValue() === "") {
			this.loginForm.controls['countryCode'].setErrors({ 'required': true });
		  }
		if(this.loginForm.valid) {
			this.phone = this.loginForm.get('countryCode')?.getRawValue() + this.loginForm.get('phoneNo')?.getRawValue();
			this.loginService.sendOtp(this.phone, this.loginForm.get('fname')?.getRawValue(), this.loginForm.get('lname')?.getRawValue())
				.subscribe(res=>{
					if(res.status === 'success') {
						this.showOtpPage = true;
					} else {
						this.error = "Please input valid phone number";
					}
				});
		}
	}

	verifyOtp() {
		this.error = '';
		if(this.otp.length === 6) {
			let req = {
			  otp: this.otp,
			  phoneNo: this.phone,
			  firstName: this.loginForm.get('fname')?.getRawValue(),
			  lastName: this.loginForm.get('lname')?.getRawValue()
			}
			this.loginService.verifyOtp(req)
			.subscribe(res=>{
			  if(res.status === 'success') {
				localStorage.setItem("auth", res.jwt);
				this.dialogRef.close();
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

	  backToLogin() {
		this.error = '';
		this.showOtpPage = false;
	  }

}
