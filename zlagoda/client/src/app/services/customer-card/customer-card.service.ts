import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {StatRow} from "../../dialogs/cashier-stats-dialog/stats-dialog.component";

export type CustomerCard = {
  number: string,
  holderName: string,
  holderSurname: string,
  holderPatronymic: string,
  holderPhoneNumber: string,
  address: {
    city: string,
    street: string,
    zipCode: string,
  },
  discount: number
}

@Injectable({
  providedIn: 'root'
})
export class CustomerCardService {

  constructor(private http: HttpClient) {
  }

  private readonly apiUrl = '/customers'

  public getAll(): Observable<CustomerCard[]> {
    return this.http.get<CustomerCard[]>(this.apiUrl);
  }

  public create(card: CustomerCard): Observable<CustomerCard> {
    console.log(JSON.stringify(card));
    return this.http.post<CustomerCard>(this.apiUrl, JSON.stringify(card));
  }

  public edit(card: CustomerCard): Observable<CustomerCard> {
    return this.http.put<CustomerCard>(this.apiUrl, JSON.stringify(card));
  }

  public delete(card: CustomerCard): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${card.number}`);
  }

  public getStats(): Observable<StatRow[]> {
    return this.http.get<StatRow[]>(this.apiUrl + '/stats');
  }

  public getBestClientsByCashiers(): Observable<number> {
    return this.http.get<number>(this.apiUrl + '/count_best_by_cashiers');
  }

  public getBestClientsByProducts(): Observable<number> {
    return this.http.get<number>(this.apiUrl + '/count_best_by_products');
  }
}
