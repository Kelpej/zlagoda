import {Component, Input} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Receipt, ReceiptService} from "../../services/receipt/receipt.service";
import {flatMap, map, Observable} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {ReceiptDialogComponent} from "../../dialogs/receipt-dialog/receipt-dialog.component";
import {DatePipe} from "@angular/common";
import {AppState, getUser} from "../../state";
import {Store} from "@ngrx/store";
import {Employee} from "../../services/employee/employee.service";
import {DateRangeDialogComponent} from "../../dialogs/date-range-dialog/date-range-dialog.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {StatsDialogComponent} from "../../dialogs/cashier-stats-dialog/stats-dialog.component";

@Component({
  selector: 'receipts-view',
  templateUrl: './receipts-view.component.html',
  styleUrls: ['./receipts-view.component.scss']
})
export class ReceiptsViewComponent {
  public readonly columnsToDisplay = ["employeeName", "cardHolderName", "total", "printDate",]

  @Input() isManager: boolean | null = false;
  @Input() cashier: Employee | null = null;

  public dataSource = new MatTableDataSource<Receipt>([]);
  public isLoading = true;
  private receipts$: Observable<Receipt[]> = new Observable<Receipt[]>();

  constructor(private dialog: MatDialog,
              private snackBar: MatSnackBar,
              private datePipe: DatePipe,
              private receiptService: ReceiptService,
              private store: Store<AppState>,) {
    this.isManager = Boolean(this.isManager);
    this.fetchReceipts();
  }

  private fetchReceipts() {
    this.isLoading = true;
    this.receipts$ = this.receiptService.getAll();

    this.receipts$.pipe(
      map(receipts => {
        if (this.isManager) {
          return receipts;
        }

        return receipts.filter(receipt => receipt.employee.id === this.cashier?.id);
      })
    ).subscribe(receipts => {
      this.dataSource.data = receipts;
      this.isLoading = false;
    });
  }

  onReceiptClicked(receipt: Receipt) {
    this.dialog.open(ReceiptDialogComponent, { data: { receipt } })
      .afterClosed()
      .subscribe(() => this.fetchReceipts());
  }

  onAddReceiptClicked(): void {
    this.dialog.open(ReceiptDialogComponent, { data: {}, width: '500px', height: '600px'})
      .afterClosed()
      .subscribe(() => this.fetchReceipts())
  }

  getCardHolderName(receipt: Receipt): string {
    if (receipt.customerCard?.holderName && receipt.customerCard?.holderSurname) {
      return receipt.customerCard?.holderSurname + ' ' + receipt.customerCard?.holderName
    }

    return "–";
  }

  formatDate(date: Date) {
    return this.datePipe.transform(date, 'MM/dd/yyyy');
  }

  onSumOfPeriodClicked() {
    this.dialog.open(DateRangeDialogComponent, {
      data: {
        onSubmit: (from: Date, to: Date) => {
          this.receiptService.sumForRange(from, to)
            .subscribe(sum => this.snackBar.open(`Total: ${sum}`));
        }
      }
    });
  }

  onCashierStatsClicked() {
    this.dialog.open(DateRangeDialogComponent, {
      data: {
        onSubmit: (from: Date, to: Date) => {
          this.receiptService.cashierStatsForRange(from, to)
            .subscribe(statistics => this.dialog.open(StatsDialogComponent, { data : { statistics }}));
        }
      }
    });
  }
}
