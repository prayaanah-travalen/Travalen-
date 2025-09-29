import { CommonModule } from '@angular/common';
import { Component, OnInit,ChangeDetectionStrategy } from '@angular/core';
import {MatMenuModule} from '@angular/material/menu';
import { Observable , of } from 'rxjs';
import { UserModel } from 'src/app/data-access/model/user.model';
import { UserStateService } from 'src/app/data-access/services/user-state.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  // changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
  imports:[MatMenuModule, CommonModule]
})
export class HeaderComponent implements OnInit {

  user$: Observable<UserModel | null> = of(null);

  user!: UserModel;
  constructor(private userStateService: UserStateService) { }


  ngOnInit(): void {

    this.user$ = this.userStateService.user$;

    let jwt = localStorage.getItem("auth");
    if(jwt !== null) {
      let jwtData = jwt.split('.')[1]
      let decodedJwtJsonData = window.atob(jwtData)
      let decodedJwtData = JSON.parse(decodedJwtJsonData)
      this.user = decodedJwtData.user;
    }
  }

  logout() {
    localStorage.clear();

    window.location.reload();

  }
}
