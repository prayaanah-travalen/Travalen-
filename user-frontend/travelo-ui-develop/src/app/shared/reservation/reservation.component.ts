import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.scss'],
  standalone: true
})
export class ReservationComponent implements OnInit {

  constructor(private router: Router, ) { }

  ngOnInit(): void {
  }

  reserve() {
    this.router.navigateByUrl("/booking")
  }
}
