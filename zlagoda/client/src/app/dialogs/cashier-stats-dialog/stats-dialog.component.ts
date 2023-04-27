import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatTableDataSource} from "@angular/material/table";

export type StatRow = {
  name: string,
  value: string,
}

@Component({
  selector: 'app-cashier-stats-dialog',
  templateUrl: './stats-dialog.component.html',
  styleUrls: ['./stats-dialog.component.scss']
})
export class StatsDialogComponent {
  public readonly columnsToDisplay = ['name', 'value']
  dataSource: MatTableDataSource<StatRow> = new MatTableDataSource<StatRow>();
  constructor(public dialogRef: MatDialogRef<StatsDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,) {
    this.dataSource.data = data.statistics;
  }
}
