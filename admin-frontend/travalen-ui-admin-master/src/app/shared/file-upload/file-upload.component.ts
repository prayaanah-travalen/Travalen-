import { LiveAnnouncer } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FileModel } from 'src/app/data-access/model/file.model';
import { ImageModel } from 'src/app/data-access/model/hotel-req.model';
import { HotelImage } from 'src/app/data-access/model/hotel.model';
// import { NgxFileDropEntry, NgxFileDropModule } from 'ngx-file-drop';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss'],
  standalone:true,
  imports:[
    // NgxFileDropModule,
    CommonModule,  
    FormsModule,]
})
export class FileUploadComponent implements OnInit {
  imagePreviewList: FileModel[] = [];
  imageList: File[] = [];
  @Input() disabled: boolean = false
  announcer = inject(LiveAnnouncer);
  @Output() imageEmitter = new EventEmitter<File[]>();
  @Output() deletedImageEmitter = new EventEmitter<number>();
  _savedImages: HotelImage[] = [];

  @Input() set savedImage(val: HotelImage[]){
      this._savedImages = val;
      this.imagePreviewList  =  val.map(im=>{
        const nameArray = im.imageName.split(".")
        let file: FileModel ={
          url: im.image,
          imageId: im.imageId,
          imageFile: new File([this.base64ToArrayBuffer(im.imageByte)], nameArray[0])
        }
        return file;
      })
  }
  

  constructor(){}

  ngOnInit(): void {
    
  }

  processing: boolean = false;

  onDragOver(event: any) {
    event.preventDefault();
  }

  onDropSuccess(event: any) {
    event.preventDefault();

    this.onFileChange(event.dataTransfer.files);
  }

  onChange(event: any) {
    this.onFileChange(event.target.files);
  }

  private onFileChange(files:FileList) {
    if (files.length) {
     
     for(let i=0; i< files.length; i++){
      var reader = new FileReader();
      let url;
     
      reader.readAsDataURL(files[i]); // read file as data url
  
      reader.onload = (event) => { // called once readAsDataURL is completed
  
       url = event?.target?.result;
     
       this.imagePreviewList.push({url:url, imageFile: files[i], imageId:0});
       this.imageList.push(files[i]);
       
      }
      
     }
     this.imageEmitter.emit(this.imageList)
 
    }
  }


  remove(file:FileModel) {
    const index = this.imagePreviewList.indexOf(file);

		if (index >= 0) {
			this.imagePreviewList.splice(index, 1);
      this.imageList = this.imagePreviewList.map(file=> file.imageFile)
      if(file.imageId > 0){   this.deletedImageEmitter.emit(file.imageId); }
			this.announcer.announce(`Removed ${file}`);
		}
    this.imageEmitter.emit(this.imageList)
  }


  base64ToArrayBuffer(base64 : any)  {
    var binaryString = window.atob(base64);
    var binaryLen = binaryString.length;
    var bytes = new Uint8Array(binaryLen);
    for (var i = 0; i < binaryLen; i++) {
      var ascii = binaryString.charCodeAt(i);
      bytes[i] = ascii;
    }
    return bytes;
  }
  
  handleFileDataType(ext: string){
    switch (ext) {
      case 'pdf':
        return 'application/pdf';
      case 'jpg':
        return 'image/jpeg';
      case 'jpeg':
        return 'image/jpeg';
      case 'png':
        return 'image/png';
      case 'tiff':
        return 'image/tiff';
      case 'docx':
        return 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';
      
    }
    return '';
  }
}



