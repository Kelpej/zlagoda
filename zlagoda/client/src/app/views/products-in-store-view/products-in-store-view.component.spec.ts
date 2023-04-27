import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductsInStoreViewComponent } from './products-in-store-view.component';

describe('ProductsInStoreViewComponent', () => {
  let component: ProductsInStoreViewComponent;
  let fixture: ComponentFixture<ProductsInStoreViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductsInStoreViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductsInStoreViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
