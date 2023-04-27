import {Component} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Observable} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {
  ProductInStore,
  ProductInStoreService,
  ProductInStoreWithName
} from "../../services/product-in-store/product-in-store.service";
import {ProductInStoreDialogComponent} from "../../dialogs/product-in-store-dialog/product-in-store-dialog.component";

@Component({
  selector: 'products-in-store-view',
  templateUrl: './products-in-store-view.component.html',
  styleUrls: ['./products-in-store-view.component.scss']
})
export class ProductsInStoreViewComponent {
  public readonly columnsToDisplay = ["upc", "name", "quantity", "price", "onSale"]
  public dataSource = new MatTableDataSource<ProductInStoreWithName>([]);
  public isLoading = true;

  private products$: Observable<ProductInStoreWithName[]> = new Observable<ProductInStoreWithName[]>();

  constructor(private dialog: MatDialog,
              private productInStoreService: ProductInStoreService) {
    this.fetchProducts();
  }

  private fetchProducts(): void {
    this.isLoading = true;
    this.products$ = this.productInStoreService.getAll();

    this.products$.subscribe(products => {
      this.dataSource.data = products;
      this.isLoading = false;
    });
  }

  onProductClicked(product: ProductInStore) {
    this.dialog.open(ProductInStoreDialogComponent, { data: { product } })
      .afterClosed()
      .subscribe(() => this.fetchProducts());
  }

  onAddProductClicked(): void {
    this.dialog.open(ProductInStoreDialogComponent, { data: {}})
      .afterClosed()
      .subscribe(() => this.fetchProducts())
  }
}
