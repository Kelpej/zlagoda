import { TestBed } from '@angular/core/testing';

import { ReceiptService } from './receipt.service';

describe('ReceiptServiceService', () => {
  let service: ReceiptService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReceiptService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
