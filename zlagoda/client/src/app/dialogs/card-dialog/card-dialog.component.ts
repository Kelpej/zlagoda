import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CustomerCard, CustomerCardService} from "../../services/customer-card/customer-card.service";

@Component({
  selector: 'add-client-dialog',
  templateUrl: './card-dialog.component.html',
  styleUrls: ['./card-dialog.component.scss']
})
export class CardDialogComponent {
  public formGroup: FormGroup;
  public card: CustomerCard;

  public isEditing = false;

  constructor(private formBuilder: FormBuilder,
              public dialogRef: MatDialogRef<CardDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private customerCardService: CustomerCardService) {
    this.card = this.data.card;
    this.isEditing = Boolean(this.card);

    this.formGroup = this.formBuilder.group({
      number: [this.card?.number],
      holderName: [this.card?.holderName],
      holderSurname: [this.card?.holderSurname],
      holderPatronymic: [this.card?.holderPatronymic],
      holderPhoneNumber: [this.card?.holderPhoneNumber],
      city: [this.card?.address.city],
      zipCode: [this.card?.address.zipCode],
      street: [this.card?.address.street],
      discount: [this.card?.discount]
    })
  }

  submitForm(): void {
    const card = {...this.card, ...this.formGroup.value}

    const response = this.isEditing ?
      this.customerCardService.edit(card) :
      this.customerCardService.create(card);

    response.subscribe(() => this.dialogRef.close());
  }

  deleteCustomerCard(card: CustomerCard | null) {
    if (card) {
      this.customerCardService.delete(card)
        .subscribe(() => this.dialogRef.close())
    }
  }
}
