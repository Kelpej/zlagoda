import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Employee, EmployeeService} from "../../services/employee/employee.service";
import {ageValidator} from "../form.helper";

@Component({
  selector: 'employee-dialog',
  templateUrl: './employee-dialog.component.html',
  styleUrls: ['./employee-dialog.component.scss']
})
export class EmployeeDialogComponent {
  public formGroup: FormGroup;
  public employee: Employee;

  public isEditing = false;

  constructor(private formBuilder: FormBuilder,
              public dialogRef: MatDialogRef<EmployeeDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private employeeService: EmployeeService) {
    this.employee = this.data.employee;
    this.isEditing = Boolean(this.employee);

    this.formGroup = this.formBuilder.group({
      name: [this.employee?.name],
      surname: [this.employee?.surname],
      patronymic: [this.employee?.patronymic],
      phoneNumber: [this.employee?.phoneNumber],
      role: [this.employee?.role],
      city: [this.employee?.city],
      zipCode: [this.employee?.zipCode],
      street: [this.employee?.street],
      birthday: [this.employee?.birthday, ageValidator(18)],
      employmentDay: [new Date()],
      salary: [this.employee?.salary],
      password: ['', Validators.required],
    })
  }

  submitForm(): void {
    const newEmployee = {...this.employee, ...this.formGroup.value}

    newEmployee.role = newEmployee.role.toUpperCase();

    const response = this.isEditing ?
      this.employeeService.edit(newEmployee) :
      this.employeeService.create(newEmployee);

    response.subscribe(() => this.dialogRef.close());
  }

  deleteEmployee(employee: Employee | null) {
    if (employee) {
      this.employeeService.delete(employee)
        .subscribe(() => this.dialogRef.close())
    }
  }
}
