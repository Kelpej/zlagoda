import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateReceiptDialog } from './create-receipt-dialog.component';

describe('CreateCheckComponent', () => {
  let component: CreateReceiptDialog;
  let fixture: ComponentFixture<CreateReceiptDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateReceiptDialog ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateReceiptDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
