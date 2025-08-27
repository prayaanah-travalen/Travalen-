import { Component, Inject, OnInit } from '@angular/core';
import { NonNullableFormBuilder } from '@angular/forms';
import { CountryCode, CountryCodes } from 'src/app/data-access/model/country-code.model';
import { AccountService } from '../data-access/services/account.service';
import { RoleModel, UserModel } from '../data-access/model/user.model';
import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  
  userForm = this.fb.group({
    email: [""],
    fname: [""],
    lname: [""],
    phone: [""],
    countryCode:["+91"],
    // role: this.fb.array([])
  });
  countryCodes: Array<CountryCode> = CountryCodes;

  roles: RoleModel[] = [];

  selectedRles: number[] = [];
  error: any;
  toModify: boolean = false;
  constructor( private fb: NonNullableFormBuilder,
    private accountService: AccountService,
    private dialogRef: DialogRef,
    @Inject(DIALOG_DATA) public data: { user:UserModel}) { }

  ngOnInit(): void {
    if(this.data && this.data.user) {
      this.userForm.patchValue({
        email: this.data.user.emailId,
        fname: this.data.user.firstName,
        lname: this.data.user.lastName,
        phone: this.data.user.phoneNo,
        countryCode:this.data.user.countryCode,
      });

      this.selectedRles = this.data.user.role.map(rl=>rl.roleId);
      this.userForm.controls['email'].disable();
      this.toModify =  true

    }
    this.getRoles();
  }

  getRoles() {
    this.accountService.getRoles().subscribe(resp=>{
        this.roles = resp.response;
    })
  }

  createUser() {
    let roles = this.roles.filter(rl=>this.selectedRles.includes(rl.roleId))
    let req: UserModel = {
      emailId: this.userForm.get('email')?.getRawValue(),
      firstName: this.userForm.get('fname')?.getRawValue(),
      lastName: this.userForm.get('lname')?.getRawValue(),
      phoneNo: this.userForm.get('phone')?.getRawValue(),
      countryCode: this.userForm.get('countryCode')?.getRawValue(),
      role: roles
    }

    if(this.data && this.data.user.userId) {
      req['userId'] = this.data.user.userId;
      this.accountService.updateUser(req).subscribe(resp=>{
        if(resp.success === "SUCCESS") {
          this.dialogRef.close();
        } else {
          this.error = resp.errorMessage;
        }
      });
    } else {
      this.accountService.createUser(req).subscribe(resp=>{
        if(resp.success === "SUCCESS") {
          this.dialogRef.close();
        } else {
          this.error = resp.errorMessage;
        }
    });
    }

  }

  roleChangeEvent(event : any[]) {
    this.selectedRles = event;
    
  }
}
