import {Component, Inject, Input} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-date-range-dialog',
  templateUrl: './date-range-dialog.component.html',
  styleUrls: ['./date-range-dialog.component.scss']
})
export class DateRangeDialogComponent {
  formGroup: FormGroup;

  @Input() header = 'Pick range';
  onSubmit: (from: Date, to: Date) => void;

  constructor(public dialogRef: MatDialogRef<DateRangeDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private formBuilder: FormBuilder) {
    this.onSubmit = this.data.onSubmit;
    this.formGroup = formBuilder.group({
      from: [Date.now(), Validators.required],
      to: [Date.now(), Validators.required]
    });
  }

  submit() {
    const {from, to} = this.formGroup.value;
    this.onSubmit(from, to);
  }
}
