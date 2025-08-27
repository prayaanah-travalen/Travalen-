import { DialogRef } from '@angular/cdk/dialog';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-refund-policy',
  templateUrl: './refund-policy.component.html',
  styleUrls: ['./refund-policy.component.scss'],
  standalone:true
})
export class RefundPolicyComponent implements OnInit {

  constructor(public dialog: DialogRef) { }

  ngOnInit(): void {
  }

}
