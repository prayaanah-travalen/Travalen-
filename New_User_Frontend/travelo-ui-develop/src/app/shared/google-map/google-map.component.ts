import { Component, OnInit } from '@angular/core';
// import { AgmCoreModule, MouseEvent } from '@agm/core';
import { CommonModule, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GoogleMapsModule } from '@angular/google-maps';

@Component({
  selector: 'app-google-map',
  templateUrl: './google-map.component.html',
  styleUrls: ['./google-map.component.scss'],
  standalone:true,
   imports:[ 
    // AgmCoreModule,
    GoogleMapsModule,
  CommonModule,
  FormsModule,
NgFor]
})
export class GoogleMapComponent implements OnInit {

  constructor() { }

//   ngOnInit(): void {}

//     zoom: number = 8;
  
  
//   lat: number = 51.673858;
//   lng: number = 7.815982;

//   clickedMarker(label: string, index: number) {
//   }
  
//   mapClicked($event: MouseEvent) {
//     this.markers.push({
//       lat: $event.coords.lat,
//       lng: $event.coords.lng,
//       draggable: true,
//       label:''
//     });
//   }
  
//   markerDragEnd(m: marker, $event: MouseEvent) {
//   }
  
//   markers: marker[] = [
// 	  {
// 		  lat: 51.673858,
// 		  lng: 7.815982,
// 		  label: 'A',
// 		  draggable: true
// 	  },
// 	  {
// 		  lat: 51.373858,
// 		  lng: 7.215982,
// 		  label: 'B',
// 		  draggable: false
// 	  },
// 	  {
// 		  lat: 51.723858,
// 		  lng: 7.895982,
// 		  label: 'C',
// 		  draggable: true
// 	  }
//   ]
  

// }

// // just an interface for type safety.
// interface marker {
// 	lat: number;
// 	lng: number;
// 	label: string;
// 	draggable: boolean;
// }

zoom = 8;
center: google.maps.LatLngLiteral = { lat: 51.673858, lng: 7.815982 };

circleCenter: google.maps.LatLngLiteral = { lat: 51.673858, lng: 7.815982 };


markers: Array<google.maps.LatLngLiteral & { label: string; draggable: boolean }> = [
  { lat: 51.673858, lng: 7.815982, label: 'A', draggable: true },
  { lat: 51.373858, lng: 7.215982, label: 'B', draggable: false },
  { lat: 51.723858, lng: 7.895982, label: 'C', draggable: true },
];



ngOnInit(): void {}

addMarker(event: google.maps.MapMouseEvent) {
  if (event.latLng) {
    this.markers.push({
      lat: event.latLng.lat(),
      lng: event.latLng.lng(),
      label: '',
      draggable: true,
    });
  }
}

}