import { Dialog, DialogRef } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import { NonNullableFormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CountryCode, CountryCodes } from 'src/app/data-access/model/country-code.model';
import { AccountService } from 'src/app/data-access/services/account.service';
import { SignInComponent } from '../sign-in/sign-in.component';

@Component({
  selector: 'app-create-account',
  templateUrl: './create-account.component.html',
  styleUrls: ['./create-account.component.scss']
})
export class CreateAccountComponent implements OnInit {

  showCreateAcc: boolean = true;
  showPassword: boolean = false;
  showContact: boolean = false;
  showOtp: boolean = false;

  showPass: boolean = false;
  showConfirmPass: boolean | undefined;

  otp: string = '';
  otpError: string = '';

  countryCodes: Array<CountryCode> = CountryCodes;

  userForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    fname: ['', [Validators.required, Validators.pattern('^[a-zA-Z ]*$')]],
    lname: ['', [Validators.required, Validators.pattern('^[a-zA-Z ]*$')]],
    phone: ['', [Validators.required, Validators.pattern('^[0-9]*$')]],
    password: ['', [Validators.required, Validators.minLength(10), 
      Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).*$')]],
    countryCode: ["+91", [Validators.required]],
    confirmPassword: ['', [Validators.required]],
    hotelName: ['', [Validators.required]],
    contactPerson: [''],
    contactNumber: [''],
    whatsappNum: ['', [Validators.pattern('^[0-9]*$')]],
    designation: ['', [Validators.required]],
    whatsappNumCcode: ["+91"]
  }, {
    validators: this.passwordMatchValidator
  })
  error: any;
  sameContactPerson: boolean = true;

  constructor( private fb: NonNullableFormBuilder,
    private accountService: AccountService,
    private router: Router,
    private dialogRef: DialogRef,
    private dialog: Dialog) { }

  ngOnInit(): void {
  }

  onOtpChange(event: any) {
    this.otp = event;
  }

  showContactSection() {
    // Only validate if the Continue button was clicked
    if (this.userForm.controls['email'].touched) {
      if(!this.userForm.controls['email'].valid) {
        return false;
      }
    }

    this.showContact = true;
    this.showCreateAcc = false;
    this.showPassword = false;
    this.showOtp = false;

    return true;
  }

  showPassowrdSection() {
    // Mark all required fields as touched to trigger validation
    Object.keys(this.userForm.controls).forEach(key => {
      const control = this.userForm.get(key);
      if (key !== 'whatsappNum' && key !== 'whatsappNumCcode' && key !== 'password' && key !== 'confirmPassword') {
        control?.markAsTouched();
      }
    });

    // Check if all required fields in the contact section are valid
    const contactFields = ['hotelName', 'fname', 'lname', 'phone', 'countryCode', 'designation'];
    const isContactValid = contactFields.every(field => this.userForm.get(field)?.valid);

    if (!isContactValid) {
      return false;
    }

    this.showPassword = true;
    this.showCreateAcc = false;
    this.showContact = false;
    this.showOtp = false;

    return true;
  }

  showOtpSection() {
    this.showOtp = true;
    this.showPassword = false;
    this.showCreateAcc = false;
    this.showContact = false;
  }

  showCreateAccSection() {
    this.showCreateAcc = true;
    this.showOtp = false;
    this.showPassword = false;
    this.showContact = false;
  }

  createAccount() {
    this.error = '';
    
    // Mark password fields as touched to trigger validation
    this.userForm.controls['password'].markAsTouched();
    this.userForm.controls['confirmPassword'].markAsTouched();

    // Check if password fields are valid
    if (!this.userForm.controls['password'].valid || !this.userForm.controls['confirmPassword'].valid) {
      return false;
    }

    // Check if passwords match
    if (this.userForm.get('password')?.value !== this.userForm.get('confirmPassword')?.value) {
      this.userForm.controls['confirmPassword'].setErrors({ passwordMismatch: true });
      return false;
    }
    
    let req = {
      emailId: this.userForm.get('email')?.getRawValue(),
      password: this.userForm.get('password')?.getRawValue(),
      firstName: this.userForm.get('fname')?.getRawValue(),
      lastName: this.userForm.get('lname')?.getRawValue(),
      phoneNo: this.userForm.get('phone')?.getRawValue(),
      countryCode: this.userForm.get('countryCode')?.getRawValue(),
      designation: this.userForm.get('designation')?.getRawValue(),
      hotelName: this.userForm.get('hotelName')?.getRawValue(),
      whatsappNumber: this.userForm.get('whatsappNumCcode')?.getRawValue() + this.userForm.get('whatsappNum')?.getRawValue(),
    }

    this.accountService.createAccount(req).subscribe(resp => {
      if (resp.status === 'SUCCESS') {
        this.showOtpSection();
      } else {
        this.error = resp.message;
      }
    });
   
    return true;
  }

  verifyOtp() {
    this.otpError = '';
    if(this.otp === '') {
      this.otpError = "Please enter valid otp";
    }
    let req = {
      emailId: this.userForm.get('email')?.getRawValue(),
      password: this.userForm.get('password')?.getRawValue(),
      firstName: this.userForm.get('fname')?.getRawValue(),
      lastName: this.userForm.get('lname')?.getRawValue(),
      phoneNo: this.userForm.get('phone')?.getRawValue(),
      countryCode:  this.userForm.get('countryCode')?.getRawValue(),
      otp:  this.otp,
      designation:  this.userForm.get('designation')?.getRawValue(),
      hotelName:  this.userForm.get('hotelName')?.getRawValue(),
      whatsappNumber:  this.userForm.get('whatsappNumCcode')?.getRawValue()+this.userForm.get('whatsappNumber')?.getRawValue(),
    }

    this.accountService.verifyOtp(req).subscribe(resp=>{
        if(resp.status === 'SUCCESS') {
          localStorage.setItem("auth", resp.jwt);
          this.dialogRef.close();
          this.router.navigateByUrl("/travalen/dashboard");
        } else {
          this.otpError = resp.message;
        }
    })
  }

  signIn() {
    this.dialogRef.close();
    this.dialog.open(SignInComponent)
  }

  passwordMatchValidator(form: any) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
    } else {
      confirmPassword?.setErrors(null);
    }
    return null;
  }

}
