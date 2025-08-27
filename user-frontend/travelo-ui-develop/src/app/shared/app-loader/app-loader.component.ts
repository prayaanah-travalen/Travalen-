import { AfterContentChecked, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { LoaderService } from './services/loader.service';

@Component({
  selector: 'app-loader',
  templateUrl: './app-loader.component.html',
  styleUrls: ['./app-loader.component.scss']
})
export class AppLoaderComponent implements AfterContentChecked {

  loading: boolean = false;
  constructor(private loaderService: LoaderService, private ref: ChangeDetectorRef) {
    this.loaderService.isLoading.subscribe((x) => {
      this.loading = x;
    });
  }

  ngAfterContentChecked() {
    this.ref.detectChanges();
  }

}
