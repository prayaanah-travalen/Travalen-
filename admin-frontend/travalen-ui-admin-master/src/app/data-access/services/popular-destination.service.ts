import { Injectable } from '@angular/core';
import { CommonService } from './common.service';

@Injectable({
  providedIn: 'root'
})
export class PopularDestinationService {

  constructor(private commonService: CommonService) { }

  saveDestination(req: any, images: File[]) {

    let payload=new FormData();
    payload.append("popularDest", new Blob(
      [JSON.stringify(req)],
      { type: "application/json" }
    ));
    payload.append("image", images[0])
    payload.append("contentType", images[0].type)


    // roomImages.forEach(img=>   payload.append("roomImages", img));
    return this.commonService.httpPOST("popularDest/save", payload);
  }

  getPopularDest(){
    return this.commonService.httpGET("popularDest");
  }

  delete(req: any){
    return this.commonService.httpPOST("popularDest/delete", req);
  }
}
