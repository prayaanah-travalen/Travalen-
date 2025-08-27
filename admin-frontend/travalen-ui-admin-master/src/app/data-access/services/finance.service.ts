import { Injectable } from '@angular/core';
import { CommonService } from './common.service';
import { FinanceModel } from '../model/finance.model';

@Injectable({
  providedIn: 'root'
})
export class FinanceService {

  constructor(private commonService: CommonService) { }

  saveFinance(req: FinanceModel) {
    return this.commonService.httpPOST("finance/save",req);
  }

  getFinance(req: any) {
    return this.commonService.httpPOST("finance",req);
  }
}
