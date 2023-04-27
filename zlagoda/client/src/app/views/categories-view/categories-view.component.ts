import { Component } from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Category, CategoryService} from "../../services/category/category.service";
import {Observable} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {CategoryDialogComponent} from "../../dialogs/category-dialog/category-dialog.component";
import {StatsDialogComponent} from "../../dialogs/cashier-stats-dialog/stats-dialog.component";

@Component({
  selector: 'category-view',
  templateUrl: './categories-view.component.html',
  styleUrls: ['./categories-view.component.scss']
})
export class CategoriesViewComponent {
  public readonly columnsToDisplay = ["name"]
  public dataSource = new MatTableDataSource<Category>([]);
  public isLoading = true;
  private categories$: Observable<Category[]> = new Observable<Category[]>();

  constructor(private dialog: MatDialog,
              private CategoryService: CategoryService) {
    this.fetchCategories();
  }

  private fetchCategories() {
    this.isLoading = true;
    this.categories$ = this.CategoryService.getAll();

    this.categories$.subscribe(categories => {
      this.dataSource.data = categories;
      this.isLoading = false;
    });
  }

  onCategoryClicked(category: Category) {
    this.dialog.open(CategoryDialogComponent, { data: { category } })
      .afterClosed()
      .subscribe(() => this.fetchCategories());
  }

  onAddCategoryClicked(): void {
    this.dialog.open(CategoryDialogComponent, { data: {}})
      .afterClosed()
      .subscribe(() => this.fetchCategories())
  }
}
