import { LiveAnnouncer } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FileModel } from 'src/app/data-access/model/file.model';
import { ImageModel } from 'src/app/data-access/model/hotel-req.model';
import { HotelImage } from 'src/app/data-access/model/hotel.model';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
  ]
})
export class FileUploadComponent implements OnInit {
  imagePreviewList: FileModel[] = [];
  imageList: File[] = [];
  @Input() disabled: boolean = false;
  announcer = inject(LiveAnnouncer);
  @Output() imageEmitter = new EventEmitter<File[]>();
  @Output() deletedImageEmitter = new EventEmitter<number>();
  _savedImages: HotelImage[] = [];

  // @Input() set savedImage(val: HotelImage[]) {
  //   if (val && val.length > 0  && !this.processing) {
  //     this._savedImages = val;

      
  //   const existingImageMap = new Map();

  //     this.imagePreviewList = val.map(im => {
  //       const nameArray = im.imageName ? im.imageName.split(".") : ['image'];
  //       let file: FileModel = {
  //         url: im.image,
  //         imageId: im.imageId,
  //         imageFile: new File([this.base64ToArrayBuffer(im.imageByte || '')], nameArray[0])
  //       };
  //       return file;
  //     });

      
      
   
  //     this.imageList = [];
  //   } else {
  //     this._savedImages = [];
  //     this.imagePreviewList = [];
  //     this.imageList = [];
  //   }
  // }

  @Input() set savedImage(val: HotelImage[]) {
    // Only update if we have new images and we're not in the middle of processing
    if (val && val.length > 0 && !this.processing) {
      this._savedImages = val;
      
      // Create a map of existing images for quick lookup
      const existingImageMap = new Map();
      this.imagePreviewList.forEach(img => {
        if (img.imageId > 0) {
          existingImageMap.set(img.imageId, img);
        }
      });
      
      // Merge existing images with new ones
      const newImagePreviewList = val.map(im => {
        // Check if this image already exists in our preview list
        const existingImage = existingImageMap.get(im.imageId);
        
        if (existingImage) {
          return existingImage; // Keep the existing object to maintain references
        }
        
        const nameArray = im.imageName ? im.imageName.split(".") : ['image'];
        let file: FileModel = {
          url: im.image,
          imageId: im.imageId,
          imageFile: new File([this.base64ToArrayBuffer(im.imageByte || '')], nameArray[0])
        };
        return file;
      });
      
      // Add any new images that haven't been saved yet (imageId === 0)
      const newImages = this.imagePreviewList.filter(img => img.imageId === 0);
      this.imagePreviewList = [...newImagePreviewList, ...newImages];
      
      // Don't add saved images to imageList - they're already saved
      // But keep any new images that are pending upload
      this.imageList = this.imagePreviewList
        .filter(img => img.imageId === 0)
        .map(img => img.imageFile);
    } else if (!val || val.length === 0) {
      // Only reset if we're not processing and don't have any new images
      if (!this.processing && this.imagePreviewList.filter(img => img.imageId === 0).length === 0) {
        this._savedImages = [];
        this.imagePreviewList = [];
        this.imageList = [];
      }
    }
  }

  constructor() { }

  ngOnInit(): void {
    // Initialize empty arrays
    if (!this.imagePreviewList) {
      this.imagePreviewList = [];
    }
    if (!this.imageList) {
      this.imageList = [];
    }
  }

  processing: boolean = false;

  onDragOver(event: any) {
    event.preventDefault();
    event.stopPropagation();
  }

  onDropSuccess(event: any) {
    event.preventDefault();
    event.stopPropagation();
    
    if (this.disabled) return;
    
    this.onFileChange(event.dataTransfer.files);
  }

  onChange(event: any) {
    if (this.disabled) return;
    
    this.onFileChange(event.target.files);
    // Clear the input so the same file can be selected again
    event.target.value = '';
  }

  private onFileChange(files: FileList) {
    if (files && files.length) {
      this.processing = true;
      
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        
        // Validate file type
        if (!this.isValidImageFile(file)) {
          console.warn(`File ${file.name} is not a valid image type`);
          continue;
        }
        
        // Check if file already exists
        if (this.imageList.some(existingFile => 
          existingFile.name === file.name && 
          existingFile.size === file.size
        )) {
          console.warn(`File ${file.name} already exists`);
          continue;
        }
        
        const reader = new FileReader();
        
        reader.onload = (event) => {
          const url = event?.target?.result;
          if (url) {
            this.imagePreviewList.push({
              url: url,
              imageFile: file,
              imageId: 0 // New images have ID 0
            });
            this.imageList.push(file);
            
            // Emit the updated image list
            this.imageEmitter.emit([...this.imageList]);
          }
          
          // Stop processing when all files are loaded
          if (this.imagePreviewList.filter(img => img.imageId === 0).length === 
              Array.from(files).filter(f => this.isValidImageFile(f)).length) {
            this.processing = false;
          }
        };
        
        reader.onerror = () => {
          console.error(`Error reading file ${file.name}`);
          this.processing = false;
        };
        
        reader.readAsDataURL(file);
      }
      
      // If no valid files, stop processing
      if (!Array.from(files).some(f => this.isValidImageFile(f))) {
        this.processing = false;
      }
    }
  }

  private isValidImageFile(file: File): boolean {
    const validTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'];
    return validTypes.includes(file.type.toLowerCase());
  }

  remove(file: FileModel) {
    if (this.disabled) return;
    
    const index = this.imagePreviewList.indexOf(file);

    if (index >= 0) {
      this.imagePreviewList.splice(index, 1);
      
      // If it's a saved image (imageId > 0), emit deletion event
      if (file.imageId > 0) {
        this.deletedImageEmitter.emit(file.imageId);
      } else {
        // If it's a new image, remove from imageList
        const imageIndex = this.imageList.findIndex(img => 
          img.name === file.imageFile.name && 
          img.size === file.imageFile.size
        );
        if (imageIndex >= 0) {
          this.imageList.splice(imageIndex, 1);
        }
      }
      
      this.announcer.announce(`Removed image ${file.imageFile.name}`);
      this.imageEmitter.emit([...this.imageList]);
    }
  }

  base64ToArrayBuffer(base64: string): Uint8Array {
    if (!base64) return new Uint8Array(0);
    
    try {
      const binaryString = window.atob(base64);
      const binaryLen = binaryString.length;
      const bytes = new Uint8Array(binaryLen);
      for (let i = 0; i < binaryLen; i++) {
        const ascii = binaryString.charCodeAt(i);
        bytes[i] = ascii;
      }
      return bytes;
    } catch (error) {
      console.error('Error converting base64 to array buffer:', error);
      return new Uint8Array(0);
    }
  }

  handleFileDataType(ext: string): string {
    switch (ext.toLowerCase()) {
      case 'pdf':
        return 'application/pdf';
      case 'jpg':
      case 'jpeg':
        return 'image/jpeg';
      case 'png':
        return 'image/png';
      case 'gif':
        return 'image/gif';
      case 'webp':
        return 'image/webp';
      case 'tiff':
        return 'image/tiff';
      case 'docx':
        return 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';
      default:
        return 'application/octet-stream';
    }
  }
}