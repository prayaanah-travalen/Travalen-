import { TestBed } from '@angular/core/testing';

import { PopularDestinationService } from './popular-destination.service';

describe('PopularDestinationService', () => {
  let service: PopularDestinationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PopularDestinationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
