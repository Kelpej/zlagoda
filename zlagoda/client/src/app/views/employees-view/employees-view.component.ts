import {Component} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Observable} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {Employee, EmployeeService} from "../../services/employee/employee.service";
import {EmployeeDialogComponent} from "../../dialogs/employee-dialog/employee-dialog.component";
import {DatePipe} from "@angular/common";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'employees-view',
  templateUrl: './employees-view.component.html',
  styleUrls: ['./employees-view.component.scss']
})
export class EmployeesViewComponent {
  public readonly columnsToDisplay = ["name", "surname", "patronymic", "role", "salary", "birthday", "employmentDay"]
  public dataSource = new MatTableDataSource<Employee>([]);
  public isLoading = true;
  private employees: Observable<Employee[]> = new Observable<Employee[]>();

  constructor(private dialog: MatDialog,
              private snackBar: MatSnackBar,
              private datePipe: DatePipe,
              private employeeService: EmployeeService,
              ) {
    this.fetchEmployees();
  }

  private fetchEmployees() {
    this.isLoading = true;
    this.employees = this.employeeService.getAll();

    this.employees.subscribe(employees => {
      this.dataSource.data = employees;
      this.isLoading = false;
    });
  }

  onEmployeeClicked(employee: Employee) {
    this.dialog.open(EmployeeDialogComponent, { data: { employee } })
      .afterClosed()
      .subscribe(() => this.fetchEmployees());
  }

  onAddEmployeeClicked(): void {
    this.dialog.open(EmployeeDialogComponent, { data: {}})
      .afterClosed()
      .subscribe(() => this.fetchEmployees())
  }

  formatDate(date: Date) {
    return this.datePipe.transform(date, 'MM/dd/yyyy');
  }

  showNumberOfBestCashiers() {
    return this.employeeService.countBest()
      .subscribe(cashiersCount => this.snackBar.open(`There are ${cashiersCount} best cashiers`));
  }
}
