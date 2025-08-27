import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PriceSlabManagementComponent } from './price-slab-management.component';

describe('PriceSlabManagementComponent', () => {
  let component: PriceSlabManagementComponent;
  let fixture: ComponentFixture<PriceSlabManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PriceSlabManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PriceSlabManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
