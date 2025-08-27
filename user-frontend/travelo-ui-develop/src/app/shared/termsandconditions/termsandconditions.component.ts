import { DialogRef } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-termsandconditions',
  templateUrl: './termsandconditions.component.html',
  styleUrls: ['./termsandconditions.component.scss'],
  standalone:true
})
export class TermsandconditionsComponent implements OnInit {

  constructor(private dialog: DialogRef) { }

  ngOnInit(): void {
  }


  close() {
    this.dialog.close();
  }

}
