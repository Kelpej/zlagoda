import {Component} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Observable} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {CustomerCard, CustomerCardService} from "../../services/customer-card/customer-card.service";
import {CardDialogComponent} from "../../dialogs/card-dialog/card-dialog.component";
import {StatsDialogComponent} from "../../dialogs/cashier-stats-dialog/stats-dialog.component";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'customer-cards-view',
  templateUrl: './customer-cards-view.component.html',
  styleUrls: ['./customer-cards-view.component.scss']
})
export class CustomerCardsViewComponent {
  public readonly columnsToDisplay = ["holderName", "holderSurname", "holderPatronymic", "discount"]
  public dataSource = new MatTableDataSource<CustomerCard>([]);
  public isLoading = true;
  private customerCards$: Observable<CustomerCard[]> = new Observable<CustomerCard[]>();

  constructor(private dialog: MatDialog,
              private snackBar: MatSnackBar,
              private customerCardService: CustomerCardService) {
    this.dataSource.filterPredicate = (card, filter) => card.holderSurname.toLowerCase().includes(filter.toLowerCase());
    this.fetchCustomerCards();
  }

  private fetchCustomerCards() {
    this.isLoading = true;
    this.customerCards$ = this.customerCardService.getAll();

    this.customerCards$.subscribe(cards => {
      this.dataSource.data = cards;
      this.isLoading = false;
    });
  }

  onCustomerCardClicked(card: CustomerCard) {
    this.dialog.open(CardDialogComponent, { data: { card } })
      .afterClosed()
      .subscribe(() => this.fetchCustomerCards());
  }

  onAddCustomerCardClicked(): void {
    this.dialog.open(CardDialogComponent, { data: {}})
      .afterClosed()
      .subscribe(() => this.fetchCustomerCards())
  }

  findBySurname($event: string) {
    this.dataSource.filter = $event;
  }

  showCustomerStats() {
    this.customerCardService.getStats()
      .subscribe(statistics => this.dialog.open(StatsDialogComponent, { data: { statistics }}))
  }

  showNumbersOfBestCustomers() {
    this.customerCardService.getBestClientsByCashiers()
      .subscribe(byCashiersCount => {
        this.customerCardService.getBestClientsByProducts()
          .subscribe(byProductsCount => {
            this.snackBar.open(`There are ${byCashiersCount} clients who received receipts from all cashiers & ${byProductsCount} who bought all products at least once`)
          });
      });
  }
}
