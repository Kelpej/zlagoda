import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export type ProductInStore = {
  upc: string,
  productId: number,
  quantity: number,
  price: number,
  onSale: boolean,
}

export type ProductInStoreWithName = {
  upc: string,
  productId: number,
  name: string,
  quantity: number,
  price: number,
  onSale: boolean,
}

@Injectable({
  providedIn: 'root'
})
export class ProductInStoreService {

  constructor(private http: HttpClient) { }

  private readonly apiUrl = '/products/store'

  public getAll(): Observable<ProductInStoreWithName[]> {
    return this.http.get<ProductInStoreWithName[]>(this.apiUrl);
  }

  public create(product: ProductInStore): Observable<ProductInStore> {
    console.log(JSON.stringify(product));
    return this.http.post<ProductInStore>(this.apiUrl, JSON.stringify(product));
  }

  public edit(product: ProductInStore): Observable<ProductInStore> {
    return this.http.put<ProductInStore>(this.apiUrl, JSON.stringify(product));
  }

  public delete(product: ProductInStore): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${product.upc}`);
  }
}
