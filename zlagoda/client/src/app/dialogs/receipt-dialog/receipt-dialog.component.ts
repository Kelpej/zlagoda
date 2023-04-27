import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {EMPTY, first, firstValueFrom, map, Observable} from "rxjs";
import {Receipt, ReceiptService} from "../../services/receipt/receipt.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ProductInStoreService, ProductInStoreWithName} from "../../services/product-in-store/product-in-store.service";
import {CustomerCard, CustomerCardService} from "../../services/customer-card/customer-card.service";
import {MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {AppState, getUser} from "../../state";
import {select, Store} from "@ngrx/store";
import {Employee} from "../../services/employee/employee.service";

@Component({
  selector: 'app-receipt-dialog',
  templateUrl: './receipt-dialog.component.html',
  styleUrls: ['./receipt-dialog.component.scss']
})
export class ReceiptDialogComponent implements OnInit {
  public formGroup: FormGroup;
  public searchControl: FormControl = new FormControl(String());

  public products$: Observable<ProductInStoreWithName[]>;
  public cards$: Observable<CustomerCard[]>;

  private seller: Employee | null = null;
  public receipt: Receipt | null;
  public isEditing = false;

  public filteredProducts$: Observable<ProductInStoreWithName[]> = EMPTY;
  public selectedProducts: Set<ProductInStoreWithName> = new Set([]);

  constructor(private store: Store<AppState>,
              private formBuilder: FormBuilder,
              public dialogRef: MatDialogRef<ReceiptDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private receiptService: ReceiptService,
              private productInStoreService: ProductInStoreService,
              private customerCardService: CustomerCardService) {

    this.receipt = data.receipt;
    this.isEditing = Boolean(this.receipt)

    this.formGroup = this.formBuilder.group({
      customerCard: [this.receipt?.customerCard],
    });

    this.products$ = productInStoreService.getAll();

    this.searchControl.valueChanges.pipe(
      map(name => this.filterProducts(name))
    ).subscribe(filteredProducts => this.filteredProducts$ = filteredProducts);

    this.cards$ = customerCardService.getAll();
  }

  ngOnInit() {
    this.store.select(getUser).subscribe(user => this.seller = user);
  }

  submitForm(): void {
    const goods = [...this.selectedProducts];

    console.warn(goods);

    const receipt = {...this.data?.receipt, ...this.formGroup.value, employee: this.seller, goods};

    const response = this.isEditing ?
      this.receiptService.edit(receipt) :
      this.receiptService.create(receipt);

    response.subscribe(() => {
      this.dialogRef.close()
    })
  }

  deleteReceipt(receipt: Receipt | null) {
    if (receipt) {
      this.receiptService.delete(receipt)
        .subscribe(() => this.dialogRef.close())
    }
  }

  onAddProduct($event: MatAutocompleteSelectedEvent) {
    this.searchControl.setValue(null);
    this.selectedProducts.add($event.option.value);
  }

  onRemoveProduct(product: ProductInStoreWithName) {
    this.selectedProducts.delete(product);
  }

  private filterProducts(name: any): Observable<ProductInStoreWithName[]> {
    if (typeof name !== 'string') {
      return EMPTY;
    }

    const normalizedName = name.toLowerCase();
    return this.products$.pipe(
      map(products => products.filter(product => {
        return !this.selectedProducts.has(product) && product.name.toLowerCase().startsWith(normalizedName)
      }))
    );
  }

  changeProductQuantity(product: ProductInStoreWithName, quantity: number): void {
    product.quantity = quantity;
  }
}
