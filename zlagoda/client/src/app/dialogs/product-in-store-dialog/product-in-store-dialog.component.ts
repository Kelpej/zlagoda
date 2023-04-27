import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {
  ProductInStore,
  ProductInStoreService,
  ProductInStoreWithName
} from "../../services/product-in-store/product-in-store.service";
import {Product, ProductService} from "../../services/product/product.service";

@Component({
  selector: 'receipt-in-store-dialog',
  templateUrl: './product-in-store-dialog.component.html',
  styleUrls: ['./product-in-store-dialog.component.scss']
})
export class ProductInStoreDialogComponent {
  public formGroup: FormGroup;

  public products$: Observable<Product[]>;

  public product: ProductInStoreWithName | null;
  public isEditing = false;

  constructor(private formBuilder: FormBuilder,
              public dialogRef: MatDialogRef<ProductInStoreDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private productInStoreService: ProductInStoreService,
              private productService: ProductService) {

    this.product = data.product;
    this.isEditing = Boolean(this.product)

    this.formGroup = this.formBuilder.group({
      upc: [this.product?.upc, Validators.required],
      productId: [this.product?.productId, Validators.required],
      quantity: [String(this.product?.quantity ?? String()), Validators.required],
      price: [String(this.product?.price ?? String()) , Validators.required],
      onSale: [Boolean(this.product?.onSale)],
    });

    this.products$ = productService.getAll();
  }

  submitForm(): void {
    const newProduct = {...this.data?.product, ...this.formGroup.value};

    const response = this.isEditing ?
      this.productInStoreService.edit(newProduct) :
      this.productInStoreService.create(newProduct);

    response.subscribe(() => this.dialogRef.close())
  }

  deleteProduct(product: ProductInStore | null) {
    if (product) {
      this.productInStoreService.delete(product)
        .subscribe(() => this.dialogRef.close())
    }
  }
}
