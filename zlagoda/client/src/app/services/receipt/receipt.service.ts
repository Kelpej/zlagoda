import { Injectable } from '@angular/core';
import {Employee} from "../employee/employee.service";
import {CustomerCard} from "../customer-card/customer-card.service";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {StatRow} from "../../dialogs/cashier-stats-dialog/stats-dialog.component";

export type Receipt = {
  id: number,
  employee: Employee,
  customerCard?: CustomerCard,
  dateOfPrinting: Date,
  total: number,
  vat: number,
}

@Injectable({
  providedIn: 'root'
})
export class ReceiptService {

  constructor(private http: HttpClient) { }

  private readonly apiUrl = '/receipts'

  public getAll(): Observable<Receipt[]> {
    return this.http.get<Receipt[]>(this.apiUrl);
  }

  public create(receipt: Receipt): Observable<Receipt> {
    return this.http.post<Receipt>(this.apiUrl, JSON.stringify(receipt));
  }

  public edit(receipt: Receipt): Observable<Receipt> {
    return this.http.put<Receipt>(this.apiUrl, JSON.stringify(receipt));
  }

  public delete(receipt: Receipt): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${receipt.id}`);
  }

  public sumForRange(from: Date, to: Date) {
    return this.http.post<number>(this.apiUrl + '/sum', { from, to });
  }

  public cashierStatsForRange(from: Date, to: Date) {
    return this.http.post<StatRow[]>(this.apiUrl + '/stats', { from, to});
  }
}
