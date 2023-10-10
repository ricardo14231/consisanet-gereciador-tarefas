import { AbstractControl, ValidatorFn } from "@angular/forms";

export function stringEmptyValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: boolean } | null => {
    const value = control.value;
    if (value === null || value === '' || value.trim() === '') {
      return { 'stringEmpty': true };
    } else {
      return null;
    }
  };    
}