import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PopularDestDto } from 'src/app/data-access/models/dsetination.model';
import { UtilsService } from 'src/app/data-access/services/utils.service';

@Component({
  selector: 'app-popular-destination',
  templateUrl: './popular-destination.component.html',
  styleUrls: ['./popular-destination.component.scss'],
  standalone:true,
  imports:[CommonModule]
})
export class PopularDestinationComponent implements OnInit {

  popularDest:PopularDestDto[] = [];

  constructor(private utilService: UtilsService) { }

  ngOnInit(): void {
    this.utilService.getPopularDestination().subscribe(resp=>{
      this.popularDest = resp;
      if(this.popularDest && this.popularDest.length <= 0){
        this.popularDest = [
          {
            popularDestId:0,
            location:'',
            image: "./assets/image/gallery.png",
            url: ''
          },
          {
            popularDestId:0,
            location:'',
            image: "./assets/image/gallery (1).png",
            url: ''
          },
          {
            popularDestId:0,
            location:'',
            image: "./assets/image/gallery (2).png",
            url: ''
          },
          {
            popularDestId:0,
            location:'',
            image: "./assets/image/gallery (3).png",
            url: ''
          }
    
    ]
      }
    })
  }

}
