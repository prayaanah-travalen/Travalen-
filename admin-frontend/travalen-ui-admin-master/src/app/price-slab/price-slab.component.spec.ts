import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PriceSlabComponent } from './price-slab.component';

describe('PriceSlabComponent', () => {
  let component: PriceSlabComponent;
  let fixture: ComponentFixture<PriceSlabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PriceSlabComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PriceSlabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
