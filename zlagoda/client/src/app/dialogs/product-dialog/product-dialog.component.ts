import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Product, ProductService} from "../../services/product/product.service";
import {Category, CategoryService} from "../../services/category/category.service";
import {Observable} from "rxjs";

@Component({
  selector: 'employee-dialog',
  templateUrl: './product-dialog.component.html',
  styleUrls: ['./product-dialog.component.scss']
})
export class ProductDialogComponent {
  public formGroup: FormGroup;

  public categories$: Observable<Category[]>;

  public product: Product | null;
  public isEditing = false;

  constructor(private formBuilder: FormBuilder,
              public dialogRef: MatDialogRef<ProductDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private productService: ProductService,
              private categoryService: CategoryService) {

    this.product = data.product;
    this.isEditing = Boolean(this.product)

    this.formGroup = this.formBuilder.group({
      name: [this.product?.name, Validators.required],
      description: [this.product?.description, Validators.required],
      category: [this.product?.category, Validators.required],
    });

    this.categories$ = categoryService.getAll();
  }

  submitForm(): void {
    const newProduct = {...this.data?.product, ...this.formGroup.value};

    const response = this.isEditing ?
      this.productService.edit(newProduct) :
      this.productService.create(newProduct);

    response.subscribe(() => this.dialogRef.close())
  }

  deleteProduct(product: Product | null) {
    if (product) {
      this.productService.delete(product)
        .subscribe(() => this.dialogRef.close())
    }
  }
}
