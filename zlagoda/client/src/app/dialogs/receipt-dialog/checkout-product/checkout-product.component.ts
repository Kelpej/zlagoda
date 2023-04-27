import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ProductInStoreWithName} from "../../../services/product-in-store/product-in-store.service";

@Component({
  selector: 'checkout-product',
  templateUrl: './checkout-product.component.html',
  styleUrls: ['./checkout-product.component.scss']
})
export class CheckoutProductComponent {
  @Input() product: ProductInStoreWithName = {
    upc: 'UPC',
    name: 'Name',
    productId: 0,
    quantity: 1,
    price: 0,
    onSale: false,
  };

  @Output() quantityChange = new EventEmitter<number>();
  @Output() remove = new EventEmitter<ProductInStoreWithName>();

  onQuantityChange(quantity: string) {
    this.quantityChange.emit(parseInt(quantity));
  }
}
