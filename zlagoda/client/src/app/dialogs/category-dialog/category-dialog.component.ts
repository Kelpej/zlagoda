import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Category} from "../../services/category/category.service";
import {CategoryService} from "../../services/category/category.service";

@Component({
  selector: 'employee-category-dialog',
  templateUrl: './category-dialog.component.html',
  styleUrls: ['./category-dialog.component.scss']
})
export class CategoryDialogComponent {
  public category: Category;
  public isEditing = false;

  public formGroup: FormGroup;
  constructor(private formBuilder: FormBuilder,
              public dialogRef: MatDialogRef<CategoryDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private categoryService: CategoryService) {

    this.category = data.category;
    this.isEditing = Boolean(this.category)

    this.formGroup = this.formBuilder.group({
      name: [this.category?.name, Validators.required],
    });
  }

  submitForm(): void {
    const newCategory = {...this.category, ...this.formGroup.value};

    const response = this.isEditing ?
      this.categoryService.edit(newCategory) :
      this.categoryService.create(newCategory);

    response.subscribe(() => this.dialogRef.close())
  }

  deleteCategory(category: Category | null) {
    if (category) {
      this.categoryService.delete(category)
        .subscribe(() => this.dialogRef.close())
    }
  }
}
