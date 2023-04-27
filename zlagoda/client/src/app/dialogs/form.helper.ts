import {AbstractControl, ValidatorFn} from "@angular/forms";

export function ageValidator(minAge: number): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    if (control.value) {
      const birthDate = new Date(control.value);
      const now = new Date();
      let age = now.getFullYear() - birthDate.getFullYear();

      if (now < new Date(now.getFullYear(), birthDate.getMonth(), birthDate.getDate())) {
        age--;
      }

      return age >= minAge ? null : { 'invalidAge': { value: control.value } };
    }

    return null;
  };
}
