import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PopularDestinationComponent } from './popular-destination.component';

describe('PopularDestinationComponent', () => {
  let component: PopularDestinationComponent;
  let fixture: ComponentFixture<PopularDestinationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PopularDestinationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PopularDestinationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
