import { Dialog } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';
import { UserManagementComponent } from '../user-management.component';
import { AccountService } from 'src/app/data-access/services/account.service';
import { UserModel } from 'src/app/data-access/model/user.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-internal-user',
  templateUrl: './internal-user.component.html',
  styleUrls: ['./internal-user.component.scss']
})
export class InternalUserComponent implements OnInit {

  users: UserModel[] = [];

  displayedColumns: string[] = ['fname','lname', 'email','phone', 'role','status', 'action'];

  constructor(private dialog: Dialog,
    private accountService: AccountService,
    private toastrService: ToastrService) { }

  ngOnInit(): void {
    this.getInternalUsers();

  }

  getInternalUsers() {
    this.accountService.getInternalUser().subscribe(resp=>{
      if(resp.success === "SUCCESS") {
          this.users = resp.response;
      }
    })
  }

  add() {
    this.dialog.open(UserManagementComponent,{width:'550px'}).closed.subscribe(res=>{
      this.getInternalUsers();
    })
  }

  inactivate(user: UserModel) {

      this.accountService.inactivateUser(user).subscribe(resp=>{
        if(resp.success === 'SUCCESS') {
          this.getInternalUsers()
        } else {
            this.toastrService.error(resp.errorMessage)
        }
      })
  }

  activate(user: UserModel) {

    this.accountService.activateUser(user).subscribe(resp=>{
      if(resp.success === 'SUCCESS') {
        this.getInternalUsers()
      } else {
          this.toastrService.error(resp.errorMessage)
      }
    })
}

  update(user:UserModel) {
      this.dialog.open(UserManagementComponent,{
        width:'550px',
        data: {
          user:user
        }
      }).closed.subscribe(res=>{
        this.getInternalUsers();
      })
  }
}
