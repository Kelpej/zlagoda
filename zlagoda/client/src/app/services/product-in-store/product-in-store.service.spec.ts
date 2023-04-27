import { TestBed } from '@angular/core/testing';

import { ProductInStoreService } from './product-in-store.service';

describe('ProductInStoreService', () => {
  let service: ProductInStoreService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductInStoreService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
