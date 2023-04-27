import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export type Employee = {
  id: number,
  name: string,
  surname: string,
  patronymic: string,
  phoneNumber: string,
  role: string,
  city: string,
  zipCode: string,
  street: string,
  birthday: Date,
  employmentDay: Date,
  salary: number
}

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private http: HttpClient) { }

  private readonly apiUrl = '/employees'

  public getAll(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.apiUrl);
  }

  public create(employee: Employee): Observable<Employee> {
    return this.http.post<Employee>(this.apiUrl,  JSON.stringify(employee));
  }

  public edit(employee: Employee): Observable<Employee> {
    return this.http.put<Employee>(this.apiUrl, JSON.stringify(employee));
  }

  public delete(employee: Employee): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${employee.id}`);
  }

  public countBest() {
    return this.http.get<number>(this.apiUrl + '/count_best');
  }
}
