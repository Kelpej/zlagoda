import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerCardsViewComponent } from './customer-cards-view.component';

describe('CustomerCardsViewComponent', () => {
  let component: CustomerCardsViewComponent;
  let fixture: ComponentFixture<CustomerCardsViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CustomerCardsViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomerCardsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
