import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {JsonContentTypeInterceptor} from "./json-content-type.interceptor";

import {AppRoutingModule} from './app-routing.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatDialogModule} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {MatCardModule} from '@angular/material/card';
import {MatTabsModule} from '@angular/material/tabs';
import {MatTableModule} from "@angular/material/table";
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {EmployeeDialogComponent} from './dialogs/employee-dialog/employee-dialog.component';
import {ProductsViewComponent} from './views/products-view/products-view.component';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatIconModule} from "@angular/material/icon";
import {AppComponent} from './app.component';
import {CategoriesViewComponent} from './views/categories-view/categories-view.component';
import {SearchBarComponent} from './action-bar/search-bar.component';
import {CreateReceiptDialog} from './dialogs/create-receipt-dialog/create-receipt-dialog.component';
import {ProductDialogComponent} from './dialogs/product-dialog/product-dialog.component';
import {CategoryDialogComponent} from './dialogs/category-dialog/category-dialog.component';
import {CardDialogComponent} from './dialogs/card-dialog/card-dialog.component';
import {MatDatepickerModule} from "@angular/material/datepicker";
import {EmployeesViewComponent} from "./views/employees-view/employees-view.component";
import {DatePipe} from "@angular/common";
import {MatNativeDateModule} from "@angular/material/core";
import {ProductInStoreDialogComponent} from './dialogs/product-in-store-dialog/product-in-store-dialog.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {ProductsInStoreViewComponent} from './views/products-in-store-view/products-in-store-view.component';
import {CustomerCardsViewComponent} from './views/customer-cards-view/customer-cards-view.component';
import {ReceiptDialogComponent} from './dialogs/receipt-dialog/receipt-dialog.component';
import {MatListModule} from "@angular/material/list";
import {ReceiptsViewComponent} from './views/receipts-view/receipts-view.component';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {CheckoutProductComponent} from './dialogs/receipt-dialog/checkout-product/checkout-product.component';
import {StoreModule} from "@ngrx/store";
import {appReducer} from "./state";
import {MatSortModule} from "@angular/material/sort";
import {MatTooltipModule} from "@angular/material/tooltip";
import {DateRangeDialogComponent} from './dialogs/date-range-dialog/date-range-dialog.component';
import {MAT_SNACK_BAR_DEFAULT_OPTIONS, MatSnackBarModule} from "@angular/material/snack-bar";
import { StatsDialogComponent } from './dialogs/cashier-stats-dialog/stats-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    CardDialogComponent,
    EmployeesViewComponent,
    EmployeeDialogComponent,
    ProductsViewComponent,
    ProductDialogComponent,
    CategoryDialogComponent,
    CreateReceiptDialog,
    SearchBarComponent,
    CategoriesViewComponent,
    ProductInStoreDialogComponent,
    ProductsInStoreViewComponent,
    CustomerCardsViewComponent,
    ReceiptDialogComponent,
    ReceiptsViewComponent,
    CheckoutProductComponent,
    DateRangeDialogComponent,
    StatsDialogComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCardModule,
    MatTabsModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MatIconModule,
    MatDatepickerModule,
    MatCheckboxModule,
    MatListModule,
    MatAutocompleteModule,
    [StoreModule.forRoot({app: appReducer})],
    MatSortModule,
    MatTooltipModule,
    MatSnackBarModule,
  ],
  providers: [
    DatePipe,
    MatDatepickerModule,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JsonContentTypeInterceptor,
      multi: true
    },
    {
      provide: MAT_SNACK_BAR_DEFAULT_OPTIONS,
      useValue: {duration: 2500}
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
