import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductInStoreDialogComponent } from './product-in-store-dialog.component';

describe('ProductInStoreDialogComponent', () => {
  let component: ProductInStoreDialogComponent;
  let fixture: ComponentFixture<ProductInStoreDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductInStoreDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductInStoreDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
