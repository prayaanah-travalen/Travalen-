import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { Component, Inject, OnInit } from '@angular/core';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss'],
  standalone: true
})
export class AlertComponent implements OnInit {

  message: string | undefined;

  constructor(  @Inject(DIALOG_DATA) public data: { message: string},
  private dialog: DialogRef) { }

  ngOnInit(): void {
    this.message = this.data.message;

  }

}
