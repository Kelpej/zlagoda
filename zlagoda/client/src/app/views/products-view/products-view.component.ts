import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Product, ProductService} from "../../services/product/product.service";
import {Observable} from "rxjs";
import {ProductDialogComponent} from "../../dialogs/product-dialog/product-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {MatSort, Sort} from "@angular/material/sort";

@Component({
  selector: 'products-view',
  templateUrl: './products-view.component.html',
  styleUrls: ['./products-view.component.scss']
})
export class ProductsViewComponent {
  public readonly columnsToDisplay = ["name", "description", "category"]
  public dataSource = new MatTableDataSource<Product>([]);
  public isLoading = true;
  private products$: Observable<Product[]> = new Observable<Product[]>();
  private products: Product[] = [];

  @ViewChild(MatSort) sort!: MatSort;

  constructor(private dialog: MatDialog,
              private productService: ProductService,
              private _liveAnnouncer: LiveAnnouncer) {
    this.fetchProducts();
  }


  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
  }

  /** Announce the change in sort state for assistive technology. */
  announceSortChange(sortState: Sort) {
    const sort = sortState.active;
    const direction = sortState.direction;

    if (sort === 'name') {
      if (direction === 'asc') {
        this.dataSource.data = this.dataSource.data.sort((a, b) => a.name.localeCompare(b.name));
      } else if (direction === 'desc') {
        this.dataSource.data = this.dataSource.data.sort((a, b) => b.name.localeCompare(a.name));
      } else {
        this.dataSource.data = this.products.slice();
      }
    }
  }

  private fetchProducts() {
    this.isLoading = true;
    this.products$ = this.productService.getAll();

    this.products$.subscribe(products => {
      this.products = [...products];
      this.dataSource.data = products;
      this.isLoading = false;
    });
  }

  onProductClicked(product: Product) {
    this.dialog.open(ProductDialogComponent, { data: { product } })
      .afterClosed()
      .subscribe(() => this.fetchProducts());
  }

  onAddProductClicked(): void {
    this.dialog.open(ProductDialogComponent, { data: {}})
      .afterClosed()
      .subscribe(() => this.fetchProducts())
  }
}
